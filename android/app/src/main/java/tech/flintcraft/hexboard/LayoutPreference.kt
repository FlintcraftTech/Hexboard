package tech.flintcraft.hexboard

import android.content.Context

/**
 * Which layout the user chose, remembered between one keyboard and the next.
 *
 * An ordinary stored preference, and it needs to be nothing more: the settings screen and
 * the input method service are the same application, so both read the same store with no
 * cross-process machinery. The service re-reads it when the keyboard is next shown, which
 * is what makes a change in the app take effect without restarting anything.
 *
 * **The fallback is the point of the resolve step.** A stored id naming a layout that is no
 * longer installed — a config removed between versions — must not leave the keyboard with
 * nothing to draw. It falls back to the config marked `isDefault`, and to the first shipped
 * config if somehow none is.
 */
object LayoutPreference {

    private const val STORE = "hexboard"
    private const val KEY = "layoutId"

    /** The id the user chose, or null where they have not chosen one. */
    fun storedId(context: Context): String? =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE).getString(KEY, null)

    /** Remembers this layout as the chosen one. */
    fun store(context: Context, layoutId: String) {
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, layoutId)
            .apply()
    }

    /** The asset filename of the layout to draw, resolved against what is installed. */
    fun assetNameFor(context: Context): String =
        resolve(
            LayoutCatalogue.fromAssets(context).values.flatten(),
            storedId(context)
        )

    /**
     * Which of these layouts to use, given a stored id.
     *
     * Pure, so the fallback chain is checkable without a device.
     */
    fun resolve(entries: List<LayoutCatalogue.Entry>, storedId: String?): String {
        val chosen = entries.firstOrNull { it.id == storedId }
        val fallback = entries.firstOrNull { it.isDefault } ?: entries.firstOrNull()
        return (chosen ?: fallback)?.assetName ?: KeyLayoutLoader.ASSET_NAME
    }
}
