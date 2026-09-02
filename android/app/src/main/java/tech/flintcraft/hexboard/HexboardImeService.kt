package tech.flintcraft.hexboard

import android.inputmethodservice.InputMethodService
import android.view.KeyEvent
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
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

    /** The key config, read once per service instance. */
    private lateinit var keyLayout: KeyLayout

    /** One-shot shift: the next inserted character is uppercased, then shift clears. */
    private var shifted by mutableStateOf(false)

    override fun onCreate() {
        super.onCreate()
        savedStateController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        keyLayout = KeyLayoutLoader.fromAssets(this)
    }

    override fun onCreateInputView(): View {
        val view = ComposeView(this)
        installOwners(view)
        window?.window?.decorView?.let(::installOwners)

        view.setContent {
            HexboardTheme {
                HexboardBoard(
                    layout = keyLayout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0D0D12)),
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
        shifted = false
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
                connection.commitText(if (shifted) text.uppercase() else text, 1)
                shifted = false
            }
            Key.ACTION_BACKSPACE -> sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL)
            Key.ACTION_ENTER -> sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER)
            Key.ACTION_CURSOR_LEFT -> sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_LEFT)
            Key.ACTION_CURSOR_RIGHT -> sendDownUpKeyEvents(KeyEvent.KEYCODE_DPAD_RIGHT)
            Key.ACTION_SHIFT -> shifted = !shifted
        }
    }
}
