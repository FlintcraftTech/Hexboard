package tech.flintcraft.hexboard

import android.content.Context
import java.io.InputStreamReader

/**
 * The emoji Hexboard offers, read from Unicode's own published list.
 *
 * SPEC: the emoji panels carry content taken from the Unicode standard's own published list
 * and display order rather than an inventory curated here, so they sit outside the key
 * config's manifest rules — the panels are Hexboard's own, on Hexboard's geometry, while
 * what fills them is not invented here. That is the same division the layouts already
 * follow.
 *
 * `emoji-test.txt` is the file Unicode publishes per emoji version, and its own header
 * describes it as data for keyboards. It lists every emoji in CLDR display order — the order
 * keyboards use — grouped into groups and subgroups. Bundling it is what let the five panels
 * survive: the alternative, Jetpack's `emoji2-emojipicker`, keeps its catalogue internal and
 * hands over its content only through its own scrolling-grid view, so taking the platform's
 * content meant taking the platform's interface with it.
 *
 * The file's version travels in its own header as published, so nothing here records it
 * separately.
 */
object EmojiCatalogue {

    /** The asset name the Gradle copy task writes Unicode's list to. */
    const val ASSET_NAME = "emoji-test.txt"

    /** Panels, and the grid each one holds — the prototype's five panels of fifty. */
    const val PANELS = 5
    const val ROWS = 5
    const val COLS = KeyGeometry.PANEL_COLS
    const val PER_PANEL = ROWS * COLS

    /** Which panel a downward swipe opens on: the middle one, as the prototype does. */
    const val HOME = 2

    /** One emoji in its slot on a panel. */
    data class EmojiKey(val characters: String, val row: Int, val col: Int)

    /**
     * Reads the bundled list and lays it out across the five panels.
     *
     * Only fully-qualified entries are taken. The file also carries minimally-qualified and
     * unqualified spellings of the same emoji, which are there so a keyboard can recognise
     * what other software emits rather than so it can offer them, and a handful of component
     * entries — skin-tone modifiers and hair components — which are pieces of an emoji rather
     * than emoji anyone would type on their own.
     */
    fun fromAssets(context: Context, assetName: String = ASSET_NAME): List<List<EmojiKey>> =
        context.assets.open(assetName).use { stream ->
            InputStreamReader(stream, Charsets.UTF_8).use { reader ->
                distribute(parse(reader.readLines()))
            }
        }

    /**
     * Every fully-qualified emoji in the file, in the order the file gives them.
     *
     * A data line is codepoints, a semicolon, a status, then a comment carrying the emoji
     * itself. The codepoints before the semicolon are what this reads: they are unambiguous,
     * where the comment's leading character is easy to mis-slice on a multi-codepoint emoji.
     */
    internal fun parse(lines: List<String>): List<String> =
        lines.mapNotNull { line ->
            if (line.startsWith("#") || line.isBlank()) return@mapNotNull null
            val semicolon = line.indexOf(';')
            if (semicolon < 0) return@mapNotNull null
            val status = line.substring(semicolon + 1).substringBefore('#').trim()
            if (status != "fully-qualified") return@mapNotNull null
            val codepoints = line.substring(0, semicolon).trim()
                .split(' ')
                .filter { it.isNotEmpty() }
                .map { it.toIntOrNull(16) }
            if (codepoints.isEmpty() || codepoints.any { it == null }) return@mapNotNull null
            codepoints.filterNotNull().joinToString("") { String(Character.toChars(it)) }
        }

    /**
     * Lays the front of the list across the five panels, the way the prototype does.
     *
     * The middle panel is home, so it takes the first fifty — the most common, CLDR order
     * being roughly that — and the panels to its right take the next hundred. The two panels
     * to the left take the fourth and fifth fifty, each reversed, so that moving outward from
     * home in either direction moves further down the list rather than jumping.
     *
     * Slots are filled down each column before moving right, which is what the prototype
     * does and what keeps a partly-filled panel ragged at its right edge rather than along
     * its bottom.
     *
     * Everything past the first two hundred and fifty is not shown. That is the prototype's
     * arrangement rather than a decision taken here, and the obvious next question — reaching
     * the rest — is its own work.
     */
    internal fun distribute(all: List<String>): List<List<EmojiKey>> {
        fun slice(index: Int): List<String> =
            all.drop(index * PER_PANEL).take(PER_PANEL)

        val panels = arrayOfNulls<List<String>>(PANELS)
        panels[HOME] = slice(0)
        panels[HOME + 1] = slice(1)
        panels[HOME + 2] = slice(2)
        panels[HOME - 1] = slice(3).reversed()
        panels[HOME - 2] = slice(4).reversed()

        return panels.map { panel ->
            panel.orEmpty().mapIndexed { index, characters ->
                EmojiKey(characters = characters, row = index % ROWS, col = index / ROWS)
            }
        }
    }
}
