package tech.flintcraft.hexboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
// click, like the swipes below, is an extension on TouchInjectionScope rather than a member,
// so it has to be imported. Its absence is what failed the instrumented compile of 2026-09-09.
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The five emoji panels SPEC has promised throughout, reached by dragging the board up from the keys.
 *
 * The content is Unicode's own published list in its own display order; the panels, the
 * geometry and the gestures are Hexboard's. What this checks is the wiring between them —
 * that a swipe reaches them, that there are five, that tapping one commits its characters.
 *
 * The last test is the important one and is not about emoji at all. `[panel-switch-gestures]`
 * had to solve a trap where a pager consuming the pointer-down killed the key press
 * underneath it; this item adds a second pager on the other axis, so the same trap arrives
 * again. A letter still committing after both pagers are in place is therefore checked rather
 * than assumed.
 *
 * It runs on a device or emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class EmojiPanelsUiTest {

    @get:Rule
    val compose = createComposeRule()

    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val layout: KeyLayout by lazy { KeyLayoutLoader.fromAssets(context) }
    private val panels: List<List<EmojiCatalogue.EmojiKey>> by lazy {
        EmojiCatalogue.fromAssets(context)
    }

    // ── The catalogue ─────────────────────────────────────────────────────────────────

    @Test
    fun theBundledListGivesFiveFullPanels() {
        assertEquals(
            "SPEC promises five emoji panels.",
            EmojiCatalogue.PANELS,
            panels.size
        )
        panels.forEachIndexed { index, panel ->
            assertEquals(
                "Panel $index should be full at ${EmojiCatalogue.PER_PANEL} emoji.",
                EmojiCatalogue.PER_PANEL,
                panel.size
            )
        }
    }

    @Test
    fun nothingUnqualifiedOrDuplicatedReachesAPanel() {
        val everything = panels.flatten().map { it.characters }
        assertTrue(
            "Some slot is empty, so a panel would draw a circle with nothing in it.",
            everything.none { it.isEmpty() }
        )
        assertEquals(
            "The same emoji appears on more than one panel: " +
                everything.groupingBy { it }.eachCount().filterValues { it > 1 }.keys,
            everything.size,
            everything.distinct().size
        )
    }

    // ── The gestures ──────────────────────────────────────────────────────────────────

    @Test
    fun draggingUpReachesTheEmojiPanelsAndDownReturns() {
        showBoard()

        val aLetter = layout.allPanels.first { it.id == "qwerty" }
            .allKeys.first { it.action == Key.ACTION_INSERT }
        // What a key publishes is its accessibility label, which for a letter is the config's
        // `label` — 'Q' — rather than its `output` — 'q'. Asking for the output found nothing
        // and failed this test at its first line, before any gesture ran.
        val letterOnScreen = accessibilityLabel(aLetter)

        assertTrue("The letters are not showing to begin with.", isShowing(letterOnScreen))

        compose.onRoot().performTouchInput { swipeUp() }
        compose.waitForIdle()
        val firstEmoji = panels[EmojiCatalogue.HOME].first().characters
        assertTrue(
            "Dragging the board up did not reach the emoji panels — '$firstEmoji' from the " +
                "home emoji panel is not on screen.",
            isShowing(firstEmoji)
        )

        // Started from the middle rather than from the top, which is what `swipeDown()` does
        // on its own. The board's control strip occupies the top of the root and is not part
        // of the vertical pager, so a drag beginning there is never handed to the pager and
        // the board stays on the emoji page. The upward drag has no such trouble — it begins
        // at the bottom, which is inside the pager already.
        compose.onRoot().performTouchInput { swipeDown(startY = centerY, endY = bottom) }
        compose.waitForIdle()
        assertTrue("Dragging back down did not return to the letters.", isShowing(letterOnScreen))
    }

    @Test
    fun swipingSidewaysMovesBetweenEmojiPanels() {
        showBoard()
        compose.onRoot().performTouchInput { swipeUp() }
        compose.waitForIdle()

        compose.onRoot().performTouchInput { swipeLeft() }
        compose.waitForIdle()

        val neighbour = panels[EmojiCatalogue.HOME + 1].first().characters
        assertTrue(
            "A sideways swipe did not move to the next emoji panel — '$neighbour' is not on " +
                "screen.",
            isShowing(neighbour)
        )
    }

    @Test
    fun tappingAnEmojiCommitsItsCharacters() {
        val committed = mutableListOf<String>()
        showBoard(onEmoji = { committed += it })
        compose.onRoot().performTouchInput { swipeUp() }
        compose.waitForIdle()

        val target = panels[EmojiCatalogue.HOME].first()
        compose.onRoot().performTouchInput {
            click(centreInRoot(target.characters))
        }
        compose.waitForIdle()

        assertEquals(
            "Tapping '${target.characters}' should commit exactly its characters.",
            listOf(target.characters),
            committed
        )
    }

    @Test
    fun aLetterStillCommitsWithBothPagersInPlace() {
        val typed = mutableListOf<Key>()
        showBoard(onKey = { typed += it })

        val qwerty = layout.allPanels.first { it.id == "qwerty" }
        val key = qwerty.allKeys.first { it.action == Key.ACTION_INSERT }

        compose.onRoot().performTouchInput {
            click(centreInRoot(accessibilityLabel(key)))
        }
        compose.waitForIdle()

        assertEquals(
            "A key press stopped committing once the second pager wrapped the board — this is " +
                "the pointer-consumption trap, arriving again on the vertical axis.",
            listOf(key.output),
            typed.map { it.output }
        )
    }

    // ── Helpers ───────────────────────────────────────────────────────────────────────

    private fun showBoard(
        onKey: (Key) -> Unit = {},
        onEmoji: (String) -> Unit = {}
    ) {
        compose.setContent {
            HexboardBoard(
                layout = layout,
                emojiPanels = panels,
                modifier = Modifier.fillMaxWidth(),
                onKey = onKey,
                onEmoji = onEmoji
            )
        }
        compose.waitForIdle()
    }

    private fun isShowing(description: String): Boolean =
        compose.onAllNodesWithContentDescription(description).fetchSemanticsNodes().isNotEmpty()

    /**
     * Where the key carrying this description actually sits, in the root's own pixels.
     *
     * The taps here were computed from `KeyGeometry.centre` instead, which gives a point
     * inside a *panel*. Under `HexboardBoard` the panel is not the root: the control strip
     * sits above it in the same column, so every computed point was short by the strip's
     * height and the taps landed on the wrong thing entirely. Reading the node's own bounds
     * asks the board where it put the key rather than predicting it, and so cannot drift
     * again when something new is added above the keys.
     *
     * The tap stays a raw touch at that point rather than `performClick()`, because what
     * these tests exist to catch is a pager consuming the pointer before the key sees it —
     * and the semantics click path would bypass exactly the routing under test.
     */
    private fun centreInRoot(description: String): Offset {
        val node = compose.onAllNodesWithContentDescription(description)
            .fetchSemanticsNodes()
            .firstOrNull()
            ?: error("Nothing on screen carries the description '$description' to tap.")
        return node.boundsInRoot.center
    }
}
