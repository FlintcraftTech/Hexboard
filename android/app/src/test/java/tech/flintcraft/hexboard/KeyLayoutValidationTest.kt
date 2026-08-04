package tech.flintcraft.hexboard

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

/**
 * Validates resources/key-layout.json against the manifest's inviolable rules.
 *
 * The config is the single source of truth for the key inventory, so the app's key set
 * cannot drift from it. What that guarantee does not cover is whether the config itself
 * is sound — which is what these checks are for. They cover manifest rule 2 (no unresolved
 * duplicates) and rule 4 (empty slots are opportunities, not acceptable gaps), and they
 * enforce rule 3 (no silent changes) by failing the build when a change breaks either.
 *
 * This test needs no keyboard and no device — it runs against the config file alone.
 * Every failure names the offending character and panel, so the message is actionable
 * without opening the JSON.
 */
class KeyLayoutValidationTest {

    private data class Key(
        val panel: String,
        val row: Int,
        val col: Int,
        val label: String,
        val output: String?,
        val action: String,
        val kind: String,
        val longPress: List<String>,
        val justification: String?
    ) {
        /** How a failure message refers to this key. */
        val where: String get() = "'$label' on $panel at row $row col $col"
    }

    private data class RowBounds(val index: Int, val colMin: Int, val colMax: Int)

    private data class EmptySlot(val row: Int, val col: Int, val note: String?)

    private data class Panel(
        val name: String,
        val rows: List<RowBounds>,
        val keys: List<Key>,
        val emptySlots: List<EmptySlot>
    )

    private val panels: List<Panel> by lazy { loadPanels() }

    // ── Rule 2 — no unresolved duplicates ──────────────────────────────────

    @Test
    fun `no output character appears on more than one panel without a justification`() {
        val byOutput = panels
            .flatMap { it.keys }
            .filter { it.output != null }
            .groupBy { it.output!! }

        val problems = mutableListOf<String>()
        for ((output, keys) in byOutput) {
            val panelNames = keys.map { it.panel }.distinct()
            if (panelNames.size <= 1) continue
            val unjustified = keys.filter { it.justification.isNullOrBlank() }
            if (unjustified.isNotEmpty()) {
                problems += "Character ${describe(output)} appears on panels " +
                    "${panelNames.joinToString(", ")} — " +
                    "${unjustified.joinToString(", ") { it.where }} " +
                    "carries no \"justification\" field. Manifest rule 2: resolve the duplicate, " +
                    "or justify why both are needed."
            }
        }
        assertNoProblems(problems)
    }

    @Test
    fun `no output character is repeated within a panel without a justification`() {
        val problems = mutableListOf<String>()
        for (panel in panels) {
            val byOutput = panel.keys.filter { it.output != null }.groupBy { it.output!! }
            for ((output, keys) in byOutput) {
                if (keys.size <= 1) continue
                val unjustified = keys.filter { it.justification.isNullOrBlank() }
                if (unjustified.isNotEmpty()) {
                    problems += "Character ${describe(output)} appears ${keys.size} times on " +
                        "${panel.name} — ${unjustified.joinToString(", ") { it.where }} " +
                        "carries no \"justification\" field. A deliberate convenience duplicate " +
                        "must say so; anything else is a mistake."
                }
            }
        }
        assertNoProblems(problems)
    }

    // ── Slot integrity ─────────────────────────────────────────────────────

    @Test
    fun `no two keys share a row and column within a panel`() {
        val problems = mutableListOf<String>()
        for (panel in panels) {
            val bySlot = panel.keys.groupBy { it.row to it.col }
            for ((slot, keys) in bySlot) {
                if (keys.size > 1) {
                    problems += "${panel.name} row ${slot.first} col ${slot.second} holds " +
                        "${keys.size} keys: ${keys.joinToString(", ") { describe(it.label) }}. " +
                        "One slot, one key."
                }
            }
        }
        assertNoProblems(problems)
    }

    @Test
    fun `every key and empty slot falls inside its panel's declared bounds`() {
        val problems = mutableListOf<String>()
        for (panel in panels) {
            val bounds = panel.rows.associateBy { it.index }
            fun check(row: Int, col: Int, what: String) {
                val rowBounds = bounds[row]
                if (rowBounds == null) {
                    problems += "$what sits on ${panel.name} row $row, which the panel does " +
                        "not declare. Declared rows: ${bounds.keys.sorted().joinToString(", ")}."
                    return
                }
                if (col < rowBounds.colMin || col > rowBounds.colMax) {
                    problems += "$what sits at col $col, outside ${panel.name} row $row's " +
                        "declared bounds (cols ${rowBounds.colMin}–${rowBounds.colMax})."
                }
            }
            panel.keys.forEach { check(it.row, it.col, it.where) }
            panel.emptySlots.forEach {
                check(it.row, it.col, "Empty slot on ${panel.name} at row ${it.row} col ${it.col}")
            }
        }
        assertNoProblems(problems)
    }

    // ── Rule 4 — empty slots are opportunities, not acceptable gaps ────────

    @Test
    fun `every declared slot is either filled or carries a note explaining why it is empty`() {
        val problems = mutableListOf<String>()
        for (panel in panels) {
            val filled = panel.keys.map { it.row to it.col }.toSet()
            val declaredEmpty = panel.emptySlots.associateBy { it.row to it.col }

            for (row in panel.rows) {
                for (col in row.colMin..row.colMax) {
                    val slot = row.index to col
                    val isFilled = slot in filled
                    val empty = declaredEmpty[slot]
                    if (isFilled && empty != null) {
                        problems += "${panel.name} row ${row.index} col $col is declared empty " +
                            "but also holds a key. It must be one or the other."
                    } else if (!isFilled && empty == null) {
                        problems += "${panel.name} row ${row.index} col $col is neither filled " +
                            "nor declared as an empty slot. Manifest rule 4: an empty slot is an " +
                            "opportunity, so say why it is empty or fill it."
                    } else if (empty != null && empty.note.isNullOrBlank()) {
                        problems += "${panel.name} row ${row.index} col $col is declared empty " +
                            "with no note. Manifest rule 4: say why it is empty."
                    }
                }
            }

            for ((row, col) in declaredEmpty.keys) {
                if (panel.rows.none { it.index == row }) {
                    problems += "${panel.name} declares an empty slot at row $row col $col, " +
                        "on a row the panel does not declare."
                }
            }
        }
        assertNoProblems(problems)
    }

    // ── Long-press accent lists ───────────────────────────────────────────

    @Test
    fun `every long-press accent list is well-formed with distinct characters`() {
        val problems = mutableListOf<String>()
        for (panel in panels) {
            for (key in panel.keys) {
                if (key.longPress.isEmpty()) continue

                if (key.output == null) {
                    problems += "${key.where} has a long-press list but emits no character of " +
                        "its own. Long-press belongs on output keys."
                }
                val blank = key.longPress.count { it.isEmpty() }
                if (blank > 0) {
                    problems += "${key.where} has $blank empty entries in its long-press list."
                }
                val duplicates = key.longPress
                    .groupBy { it }
                    .filter { it.value.size > 1 }
                    .keys
                if (duplicates.isNotEmpty()) {
                    problems += "${key.where} repeats ${duplicates.joinToString(", ") { describe(it) }} " +
                        "in its long-press list. Accents must be distinct."
                }
                if (key.output in key.longPress) {
                    problems += "${key.where} lists its own character ${describe(key.output!!)} " +
                        "as a long-press accent."
                }
            }
        }
        assertNoProblems(problems)
    }

    // ── Sanity: the config is actually there and populated ────────────────

    @Test
    fun `the config loads and holds all three panels`() {
        assertTrue(
            "Expected 3 panels in key-layout.json, found ${panels.size}",
            panels.size == 3
        )
        panels.forEach { panel ->
            assertTrue("Panel ${panel.name} declares no keys", panel.keys.isNotEmpty())
        }
    }

    // ── Loading ───────────────────────────────────────────────────────────

    private fun loadPanels(): List<Panel> {
        val file = configFile()
        val root = Gson().fromJson(file.readText(), JsonObject::class.java)
        return root.getAsJsonArray("panels").map { element ->
            val panel = element.asJsonObject
            val name = panel.get("name").asString
            Panel(
                name = name,
                rows = panel.getAsJsonArray("rows").map {
                    val row = it.asJsonObject
                    RowBounds(
                        index = row.get("index").asInt,
                        colMin = row.get("colMin").asInt,
                        colMax = row.get("colMax").asInt
                    )
                },
                keys = panel.getAsJsonArray("keys").map {
                    val key = it.asJsonObject
                    Key(
                        panel = name,
                        row = key.get("row").asInt,
                        col = key.get("col").asInt,
                        label = key.get("label").asString,
                        output = key.get("output")?.takeIf { v -> !v.isJsonNull }?.asString,
                        action = key.get("action").asString,
                        kind = key.get("kind").asString,
                        longPress = key.getAsJsonArray("longPress")?.map { a -> a.asString }
                            ?: emptyList(),
                        justification = key.get("justification")
                            ?.takeIf { v -> !v.isJsonNull }?.asString
                    )
                },
                emptySlots = (panel.getAsJsonArray("emptySlots") ?: com.google.gson.JsonArray())
                    .map {
                        val slot = it.asJsonObject
                        EmptySlot(
                            row = slot.get("row").asInt,
                            col = slot.get("col").asInt,
                            note = slot.get("note")?.takeIf { v -> !v.isJsonNull }?.asString
                        )
                    }
            )
        }
    }

    /**
     * Finds resources/key-layout.json. The Gradle build passes the repo root in as a
     * system property; the walk-up fallback keeps the test runnable from an IDE that
     * launches it with a different working directory.
     */
    private fun configFile(): File {
        val fromProperty = System.getProperty("hexboard.repoRoot")
            ?.let { File(it, CONFIG_PATH) }
        if (fromProperty != null && fromProperty.isFile) return fromProperty

        var dir: File? = File(System.getProperty("user.dir"))
        while (dir != null) {
            val candidate = File(dir, CONFIG_PATH)
            if (candidate.isFile) return candidate
            dir = dir.parentFile
        }
        fail(
            "Could not find $CONFIG_PATH. Looked at the hexboard.repoRoot system property " +
                "and walked up from ${System.getProperty("user.dir")}."
        )
        error("unreachable")
    }

    /** Renders a character for a failure message, naming the invisible ones. */
    private fun describe(char: String): String = when (char) {
        " " -> "space"
        "\n" -> "newline"
        "\t" -> "tab"
        else -> "'$char'"
    }

    private fun assertNoProblems(problems: List<String>) {
        if (problems.isNotEmpty()) {
            fail(
                "key-layout.json failed validation:\n" +
                    problems.sorted().joinToString("\n") { "  - $it" }
            )
        }
    }

    private companion object {
        const val CONFIG_PATH = "resources/key-layout.json"
    }
}
