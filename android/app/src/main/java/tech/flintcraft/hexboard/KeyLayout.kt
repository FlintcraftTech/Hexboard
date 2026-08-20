package tech.flintcraft.hexboard

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

/**
 * The key inventory, as read from `resources/key-layout.json`.
 *
 * The config is the single source of truth for which characters exist and where they
 * sit (SPEC, "the key inventory is governed by config, never by code"), so these classes
 * mirror its shape and add nothing of their own. Geometry is deliberately absent — it
 * lives in [KeyGeometry].
 *
 * `android/app/build.gradle.kts` copies the config into the app's assets at build time,
 * so there is no second copy checked in.
 *
 * Gson instantiates these without running their constructors, so every field that the
 * config may omit is declared nullable and read through a non-null accessor. Kotlin
 * default arguments would not survive that and would leave nulls in non-null types.
 */
data class KeyLayout(
    val schemaVersion: Int = 0,
    private val panels: List<Panel>? = null
) {
    val allPanels: List<Panel> get() = panels.orEmpty()

    /** The panel with this `id` (`rare`, `qwerty`, `symbols`), or null if the config has none. */
    fun panel(id: String): Panel? = allPanels.firstOrNull { it.id == id }
}

data class Panel(
    val index: Int = 0,
    val id: String = "",
    val name: String = "",
    private val rows: List<RowBounds>? = null,
    private val keys: List<Key>? = null
) {
    val allRows: List<RowBounds> get() = rows.orEmpty()
    val allKeys: List<Key> get() = keys.orEmpty()

    /** How many zag rows this panel draws. */
    val rowCount: Int get() = allRows.size

    /** The widest column index any row reaches, which is what the board has to fit. */
    val maxCol: Int get() = allRows.maxOfOrNull { it.colMax } ?: 0
}

data class RowBounds(
    val index: Int = 0,
    val colMin: Int = 0,
    val colMax: Int = 0
)

data class Key(
    val row: Int = 0,
    val col: Int = 0,
    val label: String = "",
    /** The character this key commits. Null on keys that act rather than type. */
    val output: String? = null,
    val action: String = ACTION_INSERT,
    val kind: String = KIND_LETTER,
    /** Drawn at the same size as any other key; its label is set in bold at a smaller fraction. */
    val large: Boolean = false,
    val id: String? = null,
    private val longPress: List<String>? = null,
    val justification: String? = null
) {
    /** The accents reachable by holding this key. Empty on keys that have none. */
    val accents: List<String> get() = longPress.orEmpty()

    companion object {
        const val ACTION_INSERT = "insert"
        const val ACTION_BACKSPACE = "backspace"
        const val ACTION_ENTER = "enter"
        const val ACTION_SHIFT = "shift"
        const val ACTION_CURSOR_LEFT = "cursorLeft"
        const val ACTION_CURSOR_RIGHT = "cursorRight"

        const val KIND_LETTER = "letter"
        const val KIND_PUNCTUATION = "punctuation"
        const val KIND_SPACE = "space"
        const val KIND_SPECIAL = "special"
        const val KIND_RARE = "rare"
        const val KIND_SYMBOL = "symbol"
    }
}

/** Reads the key config out of the app's assets. */
object KeyLayoutLoader {

    /** The asset name the Gradle copy task writes the config to. */
    const val ASSET_NAME = "key-layout.json"

    fun fromAssets(context: Context, assetName: String = ASSET_NAME): KeyLayout =
        context.assets.open(assetName).use { stream ->
            InputStreamReader(stream, Charsets.UTF_8).use { reader ->
                Gson().fromJson(reader, KeyLayout::class.java)
            }
        }
}
