package tech.flintcraft.hexboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Choosing a layout changes which keys the board draws.
 *
 * The picker and the board are the two halves of one promise. The Russian config has shipped
 * into the app's assets since the copy task began taking every `key-layout*.json`, and until
 * a picker existed nothing could select it — so it was in the app and inert. This drives a
 * choice and asserts the board follows: Cyrillic keys after choosing Russian, Latin ones
 * after choosing English again.
 *
 * The list and the board here are the same functions the app's own screen uses, wired up
 * directly rather than through `MainActivity`, so what is under test is the catalogue,
 * the resolve and the board rather than that screen's arrangement.
 *
 * It runs on a device or emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class LayoutSwitchingUiTest {

    @get:Rule
    val compose = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val catalogue by lazy { LayoutCatalogue.fromAssets(context) }

    @Test
    fun bothShippedLayoutsAreOfferedGroupedByLanguage() {
        assertTrue(
            "The picker reads its list from the assets, and should find the English and " +
                "Russian layouts there. Found: ${catalogue.keys}",
            catalogue.keys.containsAll(listOf("en", "ru"))
        )
    }

    @Test
    fun choosingRussianDrawsCyrillicAndChoosingEnglishBringsLatinBack() {
        val entries = catalogue.values.flatten()
        val english = entries.firstOrNull { it.language == "en" }
        val russian = entries.firstOrNull { it.language == "ru" }
        assumeTrue(
            "Both an English and a Russian layout must be installed for this test.",
            english != null && russian != null
        )
        val englishName = english!!.name.ifEmpty { english.id }
        val russianName = russian!!.name.ifEmpty { russian.id }

        compose.setContent {
            var chosenId by remember { mutableStateOf(english.id) }
            val assetName = LayoutPreference.resolve(entries, chosenId)
            val layout = remember(assetName) { KeyLayoutLoader.fromAssets(context, assetName) }
            Column {
                entries.forEach { entry ->
                    Text(
                        text = entry.name.ifEmpty { entry.id },
                        modifier = Modifier.clickable { chosenId = entry.id }
                    )
                }
                layout.panel("qwerty")?.let { panel ->
                    KeyboardPanel(panel = panel, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        compose.onNodeWithContentDescription(LATIN_KEY).assertIsDisplayed()

        compose.onNodeWithText(russianName).performClick()
        compose.waitForIdle()
        compose.onNodeWithContentDescription(CYRILLIC_KEY).assertIsDisplayed()

        compose.onNodeWithText(englishName).performClick()
        compose.waitForIdle()
        compose.onNodeWithContentDescription(LATIN_KEY).assertIsDisplayed()
    }

    private companion object {
        /** A letter the English board has and the Russian one does not. */
        const val LATIN_KEY = "Q"

        /** The first letter of the Russian board's top row. */
        const val CYRILLIC_KEY = "Й"
    }
}
