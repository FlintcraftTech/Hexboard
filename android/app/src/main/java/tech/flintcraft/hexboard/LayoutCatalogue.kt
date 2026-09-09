package tech.flintcraft.hexboard

import android.content.Context

/**
 * Which layouts this install carries, grouped by language and ordered within each group.
 *
 * SPEC: Hexboard ships a layout for each language it supports, every layout is present in
 * every install because a layout is small, and the user chooses theirs in the app's own
 * settings. This is the list that picker shows.
 *
 * **The list is discovered rather than declared.** The assets are enumerated and every
 * `key-layout*.json` is read, so a further language reaches the picker by adding one file
 * to `resources/` and changing nothing else — which is the promise SPEC makes about adding
 * a language. A hard-coded list in Kotlin would make it two changes.
 *
 * **Ordering is a set position, never a count.** Each config carries an integer `order`,
 * and a config without one sorts after every config that has one. Ordering by how often a
 * layout gets used was refused outright: it is the only true measure of popularity and it
 * would be the first thing in Hexboard to report what a user does back to a server, against
 * every other feature's posture. Nothing here counts anything.
 */
object LayoutCatalogue {

    /** One layout, as the picker needs to show it. */
    data class Entry(
        /** The asset filename, which is what the loader opens. */
        val assetName: String,
        val id: String,
        val name: String,
        /** BCP 47 tag. No display name travels with it — the reader's own device names it. */
        val language: String,
        val order: Int?,
        val isDefault: Boolean
    )

    private const val PREFIX = "key-layout"
    private const val SUFFIX = ".json"

    /** Every shipped layout, grouped by language tag, each group in its own order. */
    fun fromAssets(context: Context): Map<String, List<Entry>> =
        group(read(context, assetNames(context)))

    /** The asset filenames that are layout configs. */
    fun assetNames(context: Context): List<String> =
        context.assets.list("").orEmpty()
            .filter { it.startsWith(PREFIX) && it.endsWith(SUFFIX) }
            .sorted()

    private fun read(context: Context, names: List<String>): List<Entry> =
        names.mapNotNull { name ->
            val layout = runCatching { KeyLayoutLoader.fromAssets(context, name) }.getOrNull()
                ?: return@mapNotNull null
            Entry(
                assetName = name,
                id = layout.id,
                name = layout.name,
                language = layout.language,
                order = layout.order,
                isDefault = layout.isDefault
            )
        }

    /**
     * Groups by language and sorts within each group.
     *
     * Kept separate from the reading so it can be checked without a device: the grouping
     * and ordering rules are the part with a decision in them.
     */
    fun group(entries: List<Entry>): Map<String, List<Entry>> =
        entries
            .groupBy { it.language }
            .mapValues { (_, group) ->
                group.sortedWith(
                    compareBy<Entry> { it.order ?: Int.MAX_VALUE }.thenBy { it.name }
                )
            }
            .toSortedMap()
}
