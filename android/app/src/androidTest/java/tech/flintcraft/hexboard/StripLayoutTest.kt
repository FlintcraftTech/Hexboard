package tech.flintcraft.hexboard

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
// onAllNodes is a member of the test rule rather than a free function, so there is nothing at
// that name to import — unlike onNodeWithTag below, which is an extension. The stray import is
// what failed the instrumented compile of 2026-09-09.
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The board carries a row above the keys, and that row costs real height.
 *
 * The row is where the keyboard's own controls go — the microphone, the clipboard button,
 * predictive text's candidates — and it is empty until one of those lands. What can be
 * checked while it is empty is its geometry, which is the part that was designed:
 *
 *  1. the board's total height exceeds the tallest panel's by the row's reserved height,
 *     so the row is added rather than taken out of the keys;
 *  2. the row's drawn band equals one vertical step for the panel on screen, and is never
 *     under Android's 48dp minimum touch target;
 *  3. the top key row begins below the band rather than under it.
 *
 * The height is derived from the panel's own solved radius rather than fixed in dp, so it
 * stays in proportion on an eleven-wide layout and at any screen width. That derivation is
 * what test 2 protects.
 *
 * It runs on a device or emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class StripLayoutTest {

    @get:Rule
    val compose = createComposeRule()

    private val layout: KeyLayout by lazy {
        KeyLayoutLoader.fromAssets(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    private val panels by lazy { layout.allPanels.sortedBy { it.index } }

    /** The one radius the board solves for the whole layout, from its widest panel. */
    private fun sharedRadius(): Float = KeyGeometry.solveRadius(
        compose.onRoot().getBoundsInRoot().width.value,
        panels.maxOf { it.maxCol + 1 }
    )

    @Test
    fun theStripAddsItsHeightToTheBoardRatherThanTakingItFromTheKeys() {
        showBoard()

        val tallestPanel = panels.maxOf { KeyGeometry.boardHeight(it.rowCount, sharedRadius()) }
        val boardHeight = compose.onNodeWithTag(BOARD).getBoundsInRoot().height.value
        val strip = compose.onNodeWithTag(STRIP_TAG).getBoundsInRoot().height.value

        assertEquals(
            "The board should be exactly the tallest panel plus the strip's reserved " +
                "height. Tallest panel ${tallestPanel}dp, strip ${strip}dp, board ${boardHeight}dp.",
            (tallestPanel + strip).toDouble(),
            boardHeight.toDouble(),
            1.0
        )
    }

    @Test
    fun theBandIsOneVerticalStepAndNeverUnderTheMinimumTouchTarget() {
        showBoard()

        val expected = stripHeight(sharedRadius())
        val actual = compose.onNodeWithTag(STRIP_TAG).getBoundsInRoot().height.value

        assertEquals(
            "The band should be one vertical step for the panel on screen — ${expected}dp — " +
                "rather than ${actual}dp.",
            expected.toDouble(),
            actual.toDouble(),
            1.0
        )
        assertTrue(
            "The band is ${actual}dp, below Android's ${MIN_STRIP_HEIGHT}dp minimum touch " +
                "target. A control drawn in it would be too small to hit.",
            actual >= MIN_STRIP_HEIGHT - 1f
        )
    }

    @Test
    fun theTopKeyRowBeginsBelowTheBand() {
        showBoard()

        val stripBottom = compose.onNodeWithTag(STRIP_TAG).getBoundsInRoot().bottom.value
        val keys = compose.onAllNodes(hasClickAction()).fetchSemanticsNodes()
        assertTrue("The board rendered no keys to measure.", keys.isNotEmpty())
        val highestKeyTop = keys.minOf { it.boundsInRoot.top } / compose.density.density

        assertTrue(
            "The top key row starts at ${highestKeyTop}dp, above the band's bottom edge at " +
                "${stripBottom}dp — so the keys are drawn under the strip rather than below it.",
            highestKeyTop >= stripBottom - 1f
        )
    }

    private fun showBoard() {
        compose.setContent {
            HexboardBoard(
                layout = layout,
                modifier = Modifier.testTag(BOARD).fillMaxWidth()
            )
        }
        compose.waitForIdle()
    }

    private companion object {
        const val BOARD = "hexboard-board"
    }
}
