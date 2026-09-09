package tech.flintcraft.hexboard

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

/**
 * The neighbour table the planned predictive engine measures mis-taps with.
 *
 * SPEC says a substitution of a key for one of its six neighbours is a near-miss and any
 * other substitution is a real difference. That measure needs to know which keys touch
 * which, and nothing in the config says so — it carries `row` and `col` and no geometry,
 * because the zag rule is the perceptual wedge and lives in [KeyGeometry].
 *
 * These run against both shipped configs rather than a fixture, so a layout whose rows are
 * eleven keys wide is covered by the same assertions as the ten-wide one. No device and no
 * keyboard: the table is arithmetic over the config.
 */
class NeighbourTableTest {

    @Test
    fun aKeyWellInsideQwertyHasExactlySixNeighbours() {
        forEachConfig { name, layout ->
            val panel = layout.panel("qwerty")
            if (panel == null) {
                fail("$name has no qwerty panel.")
                return@forEachConfig
            }
            val table = KeyGeometry.neighbourTable(KeyGeometry.slotsFor(panel))
            val index = indexOf(panel, row = 1, col = 2)
            assertEquals(
                "In $name, '${panel.allKeys[index].label}' sits in the middle of the board with " +
                    "a full ring of keys around it, so it should have six neighbours.",
                6,
                table[index].size
            )
        }
    }

    @Test
    fun aKeyAtTheLeftEdgeOfARowHasFewer() {
        forEachConfig { name, layout ->
            val panel = layout.panel("qwerty")
            if (panel == null) {
                fail("$name has no qwerty panel.")
                return@forEachConfig
            }
            val table = KeyGeometry.neighbourTable(KeyGeometry.slotsFor(panel))
            val edge = table[indexOf(panel, row = 1, col = 0)].size
            assertTrue(
                "In $name, the key at the left edge of the middle row has $edge neighbours. " +
                    "It has no column to its left, so it must have fewer than six — six is " +
                    "what an interior key happens to have, not a requirement of the measure.",
                edge in 1..5
            )
        }
    }

    @Test
    fun everyNeighbourRelationshipIsMutual() {
        forEachPanel { name, panel ->
            val table = KeyGeometry.neighbourTable(KeyGeometry.slotsFor(panel))
            table.forEachIndexed { index, neighbours ->
                neighbours.forEach { other ->
                    assertTrue(
                        "In $name's ${panel.id} panel, key $index lists $other as a neighbour " +
                            "but $other does not list $index. Touching is symmetric, so a " +
                            "one-way entry means the distance test is picking up something " +
                            "other than adjacency.",
                        table[other].contains(index)
                    )
                }
            }
        }
    }

    @Test
    fun neitherSpaceKeyIsANeighbourOrHasNeighbours() {
        forEachPanel { name, panel ->
            val table = KeyGeometry.neighbourTable(KeyGeometry.slotsFor(panel))
            panel.allKeys.forEachIndexed { index, key ->
                if (key.kind != Key.KIND_SPACE) return@forEachIndexed
                assertTrue(
                    "In $name's ${panel.id} panel, the space key at row ${key.row} col " +
                        "${key.col} has neighbours of its own. A tap on a space ends the word, " +
                        "which is the correction moment itself, so it is never something the " +
                        "user might have meant instead.",
                    table[index].isEmpty()
                )
                assertFalse(
                    "In $name's ${panel.id} panel, the space key at row ${key.row} col " +
                        "${key.col} appears in another key's neighbour set.",
                    table.any { it.contains(index) }
                )
            }
        }
    }

    @Test
    fun theTableIsTheSameAtBothEndsOfTheSolvableRange() {
        forEachPanel { name, panel ->
            val slots = KeyGeometry.slotsFor(panel)
            assertEquals(
                "In $name's ${panel.id} panel, the neighbour table computed at the narrowest " +
                    "solvable radius differs from the one computed at the widest. Every " +
                    "distance in KeyGeometry is a multiple of the radius, so the table is " +
                    "meant to be scale-free and a difference here means something is not.",
                KeyGeometry.neighbourTable(slots, KeyGeometry.MIN_RADIUS),
                KeyGeometry.neighbourTable(slots, KeyGeometry.MAX_RADIUS)
            )
        }
    }

    // ── Reading the shipped configs ────────────────────────────────────────

    private fun indexOf(panel: Panel, row: Int, col: Int): Int {
        val index = panel.allKeys.indexOfFirst { it.row == row && it.col == col }
        if (index < 0) fail("No key at row $row col ${col} on the ${panel.id} panel.")
        return index
    }

    private fun forEachConfig(body: (String, KeyLayout) -> Unit) {
        CONFIG_PATHS.forEach { path ->
            body(File(path).name, Gson().fromJson(configFile(path).readText(), KeyLayout::class.java))
        }
    }

    private fun forEachPanel(body: (String, Panel) -> Unit) {
        forEachConfig { name, layout -> layout.allPanels.forEach { body(name, it) } }
    }

    /**
     * Finds a config in `resources/`. The Gradle build passes the repo root in as a system
     * property; the walk-up fallback keeps the test runnable from an IDE that launches it
     * with a different working directory. Same approach as `KeyLayoutValidationTest`.
     */
    private fun configFile(path: String): File {
        val fromProperty = System.getProperty("hexboard.repoRoot")?.let { File(it, path) }
        if (fromProperty != null && fromProperty.isFile) return fromProperty

        var dir: File? = File(System.getProperty("user.dir"))
        while (dir != null) {
            val candidate = File(dir, path)
            if (candidate.isFile) return candidate
            dir = dir.parentFile
        }
        fail(
            "Could not find $path. Looked at the hexboard.repoRoot system property and " +
                "walked up from ${System.getProperty("user.dir")}."
        )
        error("unreachable")
    }

    private companion object {
        val CONFIG_PATHS = listOf("resources/key-layout.json", "resources/key-layout-ru.json")
    }
}
