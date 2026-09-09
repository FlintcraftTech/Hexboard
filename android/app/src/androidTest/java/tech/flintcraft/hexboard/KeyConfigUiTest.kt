package tech.flintcraft.hexboard

import android.view.ViewConfiguration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
// click is an extension on TouchInjectionScope rather than a member, unlike the down, moveTo
// and up used for the long-press drags below, so it has to be imported. Its absence is what
// failed the instrumented compile of 2026-09-09.
import androidx.compose.ui.test.click
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.abs

/**
 * Walks every key in the shipped config and proves the one list reaches the screen intact.
 *
 * The config is authoritative, so the app's key set cannot disagree with it — what can
 * still go wrong is wiring: a key that renders nothing, renders in the wrong slot, or
 * emits the wrong character. For every key on every panel this checks three things, and
 * for every long-press accent a fourth:
 *
 *  1. an accessibility node with the key's label exists, with its bounds centred where the
 *     geometry puts that row and column;
 *  2. a tap at that centre reaches the board's nearest-centre routing and emits a key with
 *     exactly the config's output and action;
 *  3. holding the key and releasing on each cell of its accent row emits exactly that
 *     accent.
 *
 * Every failure names the character and its panel position. This is manifest rule 1 —
 * verify the shipped key set before shipping — made mechanical. It runs on a device or
 * emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class KeyConfigUiTest {

    @get:Rule
    val compose = createComposeRule()

    private val layout: KeyLayout by lazy {
        KeyLayoutLoader.fromAssets(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun everyKeyOnEveryPanelRendersInItsSlotAndEmitsItsCharacter() {
        // Content is set once per test, so the panel under test is state the test switches.
        val showing = mutableStateOf(layout.allPanels.firstOrNull())
        val emitted = mutableListOf<Key>()
        compose.setContent {
            showing.value?.let { panel ->
                KeyboardPanel(panel = panel, modifier = Modifier.fillMaxWidth()) { emitted += it }
            }
        }

        val problems = mutableListOf<String>()
        for (panel in layout.allPanels) {
            showing.value = panel
            compose.waitForIdle()
            problems += checkPanel(panel, emitted)
        }
        if (problems.isNotEmpty()) {
            fail("Key wiring failed:\n" + problems.joinToString("\n") { "  - $it" })
        }
    }

    @Test
    fun theConfigHasPanelsToCheck() {
        assertTrue("The config declares no panels", layout.allPanels.isNotEmpty())
        layout.allPanels.forEach { assertTrue("Panel ${it.name} has no keys", it.allKeys.isNotEmpty()) }
    }

    /** Drives every key on the panel currently showing, collecting what the board emits. */
    private fun checkPanel(panel: Panel, emitted: MutableList<Key>): List<String> {
        val density = compose.density.density
        val widthDp = compose.onRoot().getBoundsInRoot().width.value
        val radius = KeyGeometry.solveRadius(widthDp, panel.maxCol + 1)
        val problems = mutableListOf<String>()

        for (key in panel.allKeys) {
            val where = "'${key.label}' on ${panel.name} at row ${key.row} col ${key.col}"
            val centre = KeyGeometry.centre(key.row, key.col, radius)

            // 1. A node with the key's label sits where the geometry puts this slot.
            val label = accessibilityLabel(key)
            val nodes = compose.onAllNodesWithContentDescription(label).fetchSemanticsNodes()
            // How far a node's centre may sit from the slot's own centre before it counts as
            // the wrong slot: the solid part of the key, which is well inside a neighbour.
            val tolerance = KeyGeometry.solidRadius(radius) * density
            val atSlot = nodes.any { node ->
                val c = node.boundsInRoot.center
                abs(c.x - centre.x * density) < tolerance && abs(c.y - centre.y * density) < tolerance
            }
            if (nodes.isEmpty()) {
                problems += "$where renders no node labelled \"$label\"."
            } else if (!atSlot) {
                problems += "$where has a node labelled \"$label\" but none centred at its slot " +
                    "(expected about ${centre.x.toInt()},${centre.y.toInt()} dp)."
            }

            // 2. A tap at the slot's centre emits exactly this key.
            emitted.clear()
            compose.onRoot().performTouchInput {
                click(Offset(centre.x * density, centre.y * density))
            }
            compose.waitForIdle()
            val got = emitted.lastOrNull()
            when {
                got == null -> problems += "$where emitted nothing when tapped."
                got.output != key.output || got.action != key.action ->
                    problems += "$where emitted ${describe(got)} when tapped; the config says ${describe(key)}."
            }

            // 3. Each accent is reachable by hold-and-slide and emits its own character.
            key.accents.forEachIndexed { index, accent ->
                emitted.clear()
                val geometry = popupGeometry(centre, key.accents.size, radius, widthDp)
                val cellX = geometry.left + geometry.cell * (index + 0.5f)
                compose.onRoot().performTouchInput {
                    down(Offset(centre.x * density, centre.y * density))
                }
                compose.mainClock.advanceTimeBy(ViewConfiguration.getLongPressTimeout().toLong() + 100)
                compose.waitForIdle()
                compose.onRoot().performTouchInput {
                    moveTo(Offset(cellX * density, geometry.top * density))
                    up()
                }
                compose.waitForIdle()
                val accentGot = emitted.lastOrNull()
                if (accentGot?.output != accent) {
                    problems += "$where, accent ${index + 1} '$accent': hold-and-slide emitted " +
                        "${accentGot?.let(::describe) ?: "nothing"}."
                }
            }
        }
        return problems
    }

    private fun describe(key: Key): String =
        if (key.output != null) "'${key.output}' (${key.action})" else "no character (${key.action})"
}
