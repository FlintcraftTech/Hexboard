package tech.flintcraft.hexboard

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Every layout config reaches the app, and nothing else from `resources/` does.
 *
 * The Gradle copy task used to ship one hard-coded filename, so a second language existed in
 * the repository and was invisible to the app. It now copies every `key-layout*.json`, which
 * means a further language is one new file and no build change — and it copies nothing else,
 * because the manifests are generated from the configs for people to read and would be a
 * second copy of what the app already parses.
 *
 * This test is manifest rule 1 — verify the shipped key set before shipping — applied to the
 * configs themselves rather than to the keys inside one of them.
 *
 * It runs on a device or emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class ShippedConfigsTest {

    private val assets by lazy {
        InstrumentationRegistry.getInstrumentation().targetContext.assets
    }

    private val shipped: List<String> by lazy { assets.list("")?.toList().orEmpty() }

    @Test
    fun everyLayoutConfigIsShipped() {
        EXPECTED.forEach { name ->
            assertTrue(
                "$name is not among the app's assets. Shipped: ${shipped.sorted()}",
                name in shipped
            )
        }
    }

    @Test
    fun everyShippedConfigNamesItsLanguageAndItsPositionWithinIt() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val problems = LayoutCatalogue.assetNames(context).mapNotNull { name ->
            val layout = KeyLayoutLoader.fromAssets(context, name)
            when {
                layout.id.isBlank() -> "$name carries no id."
                layout.language.isBlank() ->
                    "$name carries no language, so the picker cannot group it."
                layout.order == null ->
                    "$name carries no order, so its position within its language is undefined."
                else -> null
            }
        }
        assertTrue(
            "Shipped configs the layout picker cannot place:\n" +
                problems.joinToString("\n") { "  - $it" },
            problems.isEmpty()
        )
    }

    @Test
    fun everyShippedConfigParsesAsALayoutWithPanels() {
        val configs = shipped.filter { it.startsWith("key-layout") && it.endsWith(".json") }
        assertTrue("No key-layout*.json reached the app at all.", configs.isNotEmpty())

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val problems = configs.mapNotNull { name ->
            val parsed = runCatching { KeyLayoutLoader.fromAssets(context, name) }
            val layout = parsed.getOrNull()
            when {
                layout == null ->
                    "$name did not parse as a layout: ${parsed.exceptionOrNull()?.message}"
                layout.allPanels.isEmpty() -> "$name parsed but declares no panels."
                else -> null
            }
        }
        assertTrue(
            "Shipped configs that are not usable layouts:\n" +
                problems.joinToString("\n") { "  - $it" },
            problems.isEmpty()
        )
    }

    @Test
    fun noManifestAndNoImageIsShipped() {
        val strays = shipped.filter { it.startsWith("key-manifest") || it.isImage() }
        assertTrue(
            "These reached the app's assets and should not have: $strays. The manifests are " +
                "generated from the configs for people to read, and nothing in the app reads " +
                "the images.",
            strays.isEmpty()
        )
    }

    private fun String.isImage(): Boolean =
        listOf(".png", ".jpg", ".jpeg", ".gif", ".webp", ".svg").any { endsWith(it, true) }

    private companion object {
        /**
         * The layouts Hexboard ships. Named rather than discovered, because what this asserts
         * is that each one *arrived* — a discovered list would pass with every config missing.
         */
        val EXPECTED = listOf(
            "key-layout.json",
            "key-layout-ru.json",
            "key-layout-fr.json",
            "key-layout-de.json",
            "key-layout-es.json",
            "key-layout-pt.json",
            "key-layout-it.json"
        )
    }
}
