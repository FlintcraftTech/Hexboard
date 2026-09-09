package tech.flintcraft.hexboard

import android.inputmethodservice.InputMethodService
import android.os.SystemClock
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import tech.flintcraft.hexboard.ui.theme.HexboardTheme

/**
 * Hexboard as an Android input method: the thing the system lists in its keyboard picker
 * and shows when a text field is focused.
 *
 * The input view is a [ComposeView] hosting the same panel the app screen draws, and each
 * key's output goes through the current input connection.
 *
 * **Compose inside an `InputMethodService` has no lifecycle, saved-state or view-model
 * owners by default**, and a `ComposeView` without them crashes the first time it is
 * shown. So the service is its own owner of all three and installs them on the view tree
 * — on the window's decor view as well as on the Compose view, since Compose looks them
 * up from the root. This is the known trap, and this class exists in this shape because
 * of it.
 */
class HexboardImeService : InputMethodService(),
    LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateController.savedStateRegistry

    /**
     * The key config for the layout the user chose.
     *
     * Observable rather than a plain field, because the choice is re-read each time the
     * keyboard is shown and the board has to redraw when it has changed. That re-read is
     * what makes a change in the app's settings take effect without restarting anything:
     * the settings screen and this service are one application, so both read the same
     * preference store.
     */
    private var keyLayout by mutableStateOf(KeyLayout())

    /** Unicode's emoji list, laid out across the five panels, read once per service instance. */
    private var emojiPanels: List<List<EmojiCatalogue.EmojiKey>> = emptyList()

    /**
     * The shift key's three states, as the prototype defines them: one tap gives shift, a
     * second tap inside the double-tap window gives caps lock, and a further tap clears both.
     */
    private var shiftState by mutableStateOf(ShiftState.OFF)

    /** When the shift key was last tapped, for telling a double tap from two single ones. */
    private var lastShiftTap = 0L

    override fun onCreate() {
        super.onCreate()
        savedStateController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        loadChosenLayout()
        emojiPanels = EmojiCatalogue.fromAssets(this)
    }

    /** Reads whichever layout the user picked in the app, falling back to the default. */
    private fun loadChosenLayout() {
        keyLayout = KeyLayoutLoader.fromAssets(this, LayoutPreference.assetNameFor(this))
    }

    override fun onCreateInputView(): View {
        loadChosenLayout()
        val view = ComposeView(this)
        installOwners(view)
        window?.window?.decorView?.let(::installOwners)

        view.setContent {
            HexboardTheme {
                HexboardBoard(
                    layout = keyLayout,
                    shiftState = shiftState,
                    emojiPanels = emojiPanels,
                    onEmoji = { characters ->
                        currentInputConnection?.commitText(characters, 1)
                    },
                    // The background is applied before the padding so it fills the
                    // navigation bar's band too, rather than leaving a bare strip there.
                    // navigationBarsPadding() lifts the bottom key row clear of the
                    // navigation bar, which otherwise takes the touches aimed at it; the
                    // inset is zero where there is no bar, so no height is spent then.
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0D0D12))
                        .navigationBarsPadding(),
                    onKey = ::handleKey
                )
            }
        }
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        return view
    }

    private fun installOwners(view: View) {
        view.setViewTreeLifecycleOwner(this)
        view.setViewTreeViewModelStoreOwner(this)
        view.setViewTreeSavedStateRegistryOwner(this)
    }

    override fun onStartInputView(info: android.view.inputmethod.EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        // The system reuses one input view across appearances, so onCreateInputView may not
        // run again after a change in the app's settings. Re-reading here is what makes the
        // choice take effect the next time the keyboard is shown.
        loadChosenLayout()
        shiftState = ShiftState.OFF
        lastShiftTap = 0L
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        store.clear()
        super.onDestroy()
    }

    /**
     * What a key does to the field being edited.
     *
     * Insert keys commit their character. The keys that act rather than type are sent as
     * the key events every editor already understands, so backspace respects a selection
     * and enter performs a single-line field's action rather than inserting a newline into
     * it.
     */
    private fun handleKey(key: Key) {
        val connection = currentInputConnection ?: return
        when (key.action) {
            Key.ACTION_INSERT -> {
                val text = key.output ?: return
                connection.commitText(shiftState.applyTo(text), 1)
                shiftState = shiftState.afterInsert()
            }
            Key.ACTION_BACKSPACE -> sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL)
            Key.ACTION_ENTER -> sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER)
            Key.ACTION_CURSOR_LEFT -> sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT)
            Key.ACTION_CURSOR_RIGHT -> sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT)
            Key.ACTION_SHIFT -> cycleShift()
        }
    }

    /**
     * The prototype's three-state cycle on the shift key.
     *
     * A tap from off gives shift. A second tap inside the double-tap window gives caps lock.
     * A tap from anywhere else clears both. The window is the phone's own
     * [ViewConfiguration.getDoubleTapTimeout] rather than the prototype's hard-coded 320 ms:
     * this project has twice preferred the system's value to a number of its own, and SPEC's
     * rule that holds and repeats follow the phone's settings comes from the same instinct.
     */
    private fun cycleShift() {
        val now = SystemClock.uptimeMillis()
        shiftState = shiftState.next(
            sinceLastTapMs = now - lastShiftTap,
            doubleTapWindowMs = ViewConfiguration.getDoubleTapTimeout().toLong()
        )
        lastShiftTap = now
    }
}

/**
 * Off, one-shot shift, or caps lock — what the shift key cycles between.
 *
 * The three rules below are the whole of the behaviour, kept here as pure functions so they
 * can be checked without a running input method: the service holds the state and the clock,
 * and decides nothing else itself.
 */
enum class ShiftState {
    OFF,
    SHIFT,
    CAPS;

    /** True while letters draw as capitals and an inserted character is uppercased. */
    val isOn: Boolean get() = this != OFF

    /**
     * Where a tap on the shift key leads, given how long ago the last one was.
     *
     * The prototype's cycle: one tap gives shift, a second inside the double-tap window gives
     * caps lock, and a tap from anywhere else clears both.
     */
    fun next(sinceLastTapMs: Long, doubleTapWindowMs: Long): ShiftState = when {
        this == SHIFT && sinceLastTapMs < doubleTapWindowMs -> CAPS
        this == OFF -> SHIFT
        else -> OFF
    }

    /** What this state does to a character on its way into the field. */
    fun applyTo(text: String): String = if (isOn) text.uppercase() else text

    /** Shift is one-shot and clears on the first character; caps lock holds. */
    fun afterInsert(): ShiftState = if (this == SHIFT) OFF else this
}
