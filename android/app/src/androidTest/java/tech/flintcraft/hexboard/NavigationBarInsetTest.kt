package tech.flintcraft.hexboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.height
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The bottom key row must not share a band with Android's back, home and recents buttons.
 *
 * The navigation bar takes touches in its own region, so a row 3 key drawn across it is
 * partly unusable rather than partly hidden — worst for the two space bars, which sit in
 * the middle where the system buttons are. `HexboardImeService` therefore pads its input
 * view by the navigation-bar inset, and this test carries both halves of that promise:
 *
 *  1. the padded board reserves at least the navigation-bar inset below its lowest key, so
 *     no key is left sitting in the bar's band;
 *  2. the padding costs exactly that inset and no more — so where the inset is zero, on a
 *     device with no navigation bar, the board is not one pixel taller than before.
 *
 * It renders the board with the same modifier chain the service uses. That is the closest a
 * test can get: the service's real input-method window cannot be driven from here, and
 * whether the edge-to-edge enforcement formally reaches an `InputMethodService` input view
 * is undocumented — see the item's own note. The remedy is correct under either answer,
 * which is what the second test protects.
 *
 * It runs on a device or emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class NavigationBarInsetTest {

    @get:Rule
    val compose = createComposeRule()

    private val layout: KeyLayout by lazy {
        KeyLayoutLoader.fromAssets(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun thePaddedBoardReservesTheNavigationBarBelowItsLowestKey() {
        var insetPx = 0
        compose.setContent {
            insetPx = WindowInsets.navigationBars.getBottom(LocalDensity.current)
            HexboardBoard(
                layout = layout,
                modifier = Modifier
                    .testTag(BOARD)
                    .fillMaxWidth()
                    .background(Color(0xFF0D0D12))
                    .navigationBarsPadding()
            )
        }
        compose.waitForIdle()

        val reserved = boardBottomPx() - lowestKeyBottomPx()
        assertTrue(
            "The board reserves only $reserved px below its lowest key, against a " +
                "navigation-bar inset of $insetPx px. The bottom key row would share its band " +
                "with the system buttons, which take the taps aimed at it.",
            reserved >= insetPx - 1f
        )
    }

    @Test
    fun thePaddingCostsTheInsetAndNothingMore() {
        val padded = mutableStateOf(true)
        var insetPx = 0
        compose.setContent {
            insetPx = WindowInsets.navigationBars.getBottom(LocalDensity.current)
            HexboardBoard(
                layout = layout,
                modifier = Modifier
                    .testTag(BOARD)
                    .fillMaxWidth()
                    .let { if (padded.value) it.navigationBarsPadding() else it }
            )
        }
        compose.waitForIdle()
        val withPadding = boardHeightPx()

        padded.value = false
        compose.waitForIdle()
        val withoutPadding = boardHeightPx()

        assertEquals(
            "The padding should add exactly the navigation-bar inset of $insetPx px — the " +
                "padded board is $withPadding px against $withoutPadding px unpadded. Where " +
                "the inset is zero the two must be identical, so no height is spent on a " +
                "device with no navigation bar.",
            (withoutPadding + insetPx).toDouble(),
            withPadding.toDouble(),
            1.0
        )
    }

    /** The bottom edge of the board itself, padding included, in pixels. */
    private fun boardBottomPx(): Float =
        compose.onNodeWithTag(BOARD).getBoundsInRoot().bottom.value * compose.density.density

    /** How tall the board is, padding included, in pixels. */
    private fun boardHeightPx(): Float =
        compose.onNodeWithTag(BOARD).getBoundsInRoot().height.value * compose.density.density

    /** The bottom edge of the lowest key node the board draws, in pixels. */
    private fun lowestKeyBottomPx(): Float {
        val keys = compose.onAllNodes(hasClickAction()).fetchSemanticsNodes()
        assertTrue("The board rendered no keys to measure.", keys.isNotEmpty())
        return keys.maxOf { it.boundsInRoot.bottom }
    }

    private companion object {
        const val BOARD = "hexboard-board"
    }
}
