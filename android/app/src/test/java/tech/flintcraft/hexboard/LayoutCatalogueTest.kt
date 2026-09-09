package tech.flintcraft.hexboard

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * How the layout picker's list is built: grouped by language, ordered within a group, and
 * resolving a stored choice against what is actually installed.
 *
 * These run on the JVM over hand-made entries rather than over the shipped configs, because
 * what is being checked is the grouping and fallback rules rather than the content of any
 * config. Whether real configs reach the app is `ShippedConfigsTest`'s, and whether picking
 * one changes the board is `LayoutSwitchingUiTest`'s.
 */
class LayoutCatalogueTest {

    private fun entry(
        id: String,
        language: String,
        order: Int?,
        isDefault: Boolean = false,
        name: String = id
    ) = LayoutCatalogue.Entry(
        assetName = "key-layout-$id.json",
        id = id,
        name = name,
        language = language,
        order = order,
        isDefault = isDefault
    )

    @Test
    fun layoutsAreGroupedByLanguage() {
        val grouped = LayoutCatalogue.group(
            listOf(
                entry("qwerty-en", "en", 0, isDefault = true),
                entry("qwerty-ru", "ru", 0),
                entry("dvorak-en", "en", 1)
            )
        )
        assertEquals(
            "The picker groups by language, so two English layouts belong in one group.",
            setOf("en", "ru"),
            grouped.keys
        )
        assertEquals(2, grouped.getValue("en").size)
        assertEquals(1, grouped.getValue("ru").size)
    }

    @Test
    fun aGroupIsOrderedBySetPosition() {
        val grouped = LayoutCatalogue.group(
            listOf(
                entry("third", "en", 2),
                entry("first", "en", 0),
                entry("second", "en", 1)
            )
        )
        assertEquals(
            "Within a language, layouts run in the order each config declares — lower first.",
            listOf("first", "second", "third"),
            grouped.getValue("en").map { it.id }
        )
    }

    @Test
    fun aLayoutWithNoOrderSortsLast() {
        val grouped = LayoutCatalogue.group(
            listOf(
                entry("unordered", "en", null),
                entry("ordered", "en", 5)
            )
        )
        assertEquals(
            "A config that declares no order sorts after every config that declares one.",
            listOf("ordered", "unordered"),
            grouped.getValue("en").map { it.id }
        )
    }

    @Test
    fun aStoredChoiceIsUsedWhenItIsStillInstalled() {
        assertEquals(
            "key-layout-qwerty-ru.json",
            LayoutPreference.resolve(ALL, storedId = "qwerty-ru")
        )
    }

    @Test
    fun aStoredIdThatIsNoLongerInstalledFallsBackToTheDefault() {
        assertEquals(
            "A stored id naming a layout that has since been removed must not leave the " +
                "keyboard with nothing to draw — it falls back to the config marked default.",
            "key-layout-qwerty-en.json",
            LayoutPreference.resolve(ALL, storedId = "qwerty-xx")
        )
        assertEquals(
            "Nothing stored means the default too.",
            "key-layout-qwerty-en.json",
            LayoutPreference.resolve(ALL, storedId = null)
        )
    }

    @Test
    fun withNoDefaultDeclaredTheFirstInstalledLayoutIsUsed() {
        val noDefault = ALL.map { it.copy(isDefault = false) }
        assertEquals(
            "key-layout-qwerty-en.json",
            LayoutPreference.resolve(noDefault, storedId = "gone")
        )
    }

    private companion object {
        val ALL = listOf(
            LayoutCatalogue.Entry(
                "key-layout-qwerty-en.json", "qwerty-en", "QWERTY (English)", "en", 0, true
            ),
            LayoutCatalogue.Entry(
                "key-layout-qwerty-ru.json", "qwerty-ru", "ЙЦУКЕН (Russian)", "ru", 0, false
            )
        )
    }
}
