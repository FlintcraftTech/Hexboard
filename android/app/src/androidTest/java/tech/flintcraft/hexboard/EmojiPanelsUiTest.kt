package tech.flintcraft.hexboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.width
import androidx.compose.ui.test.getBoundsInRoot
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
 * The five emoji panels SPEC has promised throughout, reached by swiping down from the keys.
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
    fun swipingDownReachesTheEmojiPanelsAndUpReturns() {
        showBoard()

        val aLetter = layout.allPanels.first { it.id == "qwerty" }
            .allKeys.first { it.action == Key.ACTION_INSERT }

        assertTrue("The letters are not showing to begin with.", isShowing(aLetter.output!!))

        compose.onRoot().performTouchInput { swipeDown() }
        compose.waitForIdle()
        val firstEmoji = panels[EmojiCatalogue.HOME].first().characters
        assertTrue(
            "A downward swipe did not reach the emoji panels — '$firstEmoji' from the home " +
                "emoji panel is not on screen.",
            isShowing(firstEmoji)
        )

        compose.onRoot().performTouchInput { swipeUp() }
        compose.waitForIdle()
        assertTrue("An upward swipe did not return to the letters.", isShowing(aLetter.output!!))
    }

    @Test
    fun swipingSidewaysMovesBetweenEmojiPanels() {
        showBoard()
        compose.onRoot().performTouchInput { swipeDown() }
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
        compose.onRoot().performTouchInput { swipeDown() }
        compose.waitForIdle()

        val target = panels[EmojiCatalogue.HOME].first()
        compose.onAllNodesWithContentDescription(target.characters)
            .fetchSemanticsNodes()
            .firstOrNull()
            ?: error("The home emoji panel does not show '${target.characters}' to tap.")

        val density = compose.density.density
        val radius = sharedRadius()
        val centre = KeyGeometry.centre(target.row, target.col, radius)
        compose.onRoot().performTouchInput {
            click(Offset(centre.x * density, centre.y * density))
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
        val density = compose.density.density
        val centre = KeyGeometry.centre(key.row, key.col, sharedRadius())

        compose.onRoot().performTouchInput {
            click(Offset(centre.x * density, centre.y * density))
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

    /** The one radius the board solves for the whole layout, from its widest panel. */
    private fun sharedRadius(): Float = KeyGeometry.solveRadius(
        compose.onRoot().getBoundsInRoot().width.value,
        layout.allPanels.maxOf { it.maxCol + 1 }
    )
}
