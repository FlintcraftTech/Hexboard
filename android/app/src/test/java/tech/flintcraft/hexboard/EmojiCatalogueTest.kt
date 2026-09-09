package tech.flintcraft.hexboard

import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

/**
 * A guard on the bundled emoji list, so a format change fails loudly.
 *
 * `EmojiCatalogue.parse` reads the codepoints before the first semicolon and keeps only the
 * entries whose status column says `fully-qualified`. Both are stable in the format as it
 * stands — and a format change would not fail anything: a moved column, a renamed status or
 * a different comment convention would simply make the parse return few entries or none,
 * and the symptom would be emoji panels arriving empty on somebody's phone, long after the
 * file was swapped.
 *
 * **Why these three assertions rather than a count.** "At least 3,781 emoji" is a bare
 * number that goes wrong the next time Unicode adds any, and an exact count fails on every
 * legitimate refresh — which trains people to edit the test instead of reading it. Each
 * assertion below is instead a proportion, or a requirement the app already has:
 *
 *  - the parse yields at least as many entries as the panels display, a figure
 *    [EmojiCatalogue] computes from its own `ROWS` and `COLS`, since fewer than that means
 *    the board cannot be filled;
 *  - the file carries more than one group heading, since the groups are what the whole-list
 *    browser is organised by and a format change would collapse them;
 *  - the parsed count is at least half the file's non-comment lines. This is the one that
 *    catches a moved status column: a wrong column reads as an unrecognised status and drops
 *    nearly everything, taking the ratio to near zero, while the ordinary mix of
 *    fully-qualified and lesser-qualified spellings sits well above half.
 *
 * A plain JVM test reading `resources/emoji-test.txt` from the repository, the way
 * `KeyLayoutValidationTest` reads the shipped key config. `parse` is `internal`, so it is
 * reachable from the test source set with nothing opened up for it.
 *
 * Step 4 of the `emoji-data-refresh` cycle in `CYCLES.md` runs this as part of every turn.
 */
class EmojiCatalogueTest {

    private val lines: List<String> by lazy { emojiFile().readLines(Charsets.UTF_8) }
    private val parsed: List<String> by lazy { EmojiCatalogue.parse(lines) }

    @Test
    fun theParseFillsEveryPanel() {
        val capacity = EmojiCatalogue.PANELS * EmojiCatalogue.PER_PANEL
        assertTrue(
            "The bundled emoji file parsed to ${parsed.size} entries, and the five panels " +
                "hold $capacity between them. Fewer than that and the board cannot be filled " +
                "— which is what an emoji panel arriving empty on a phone looks like from " +
                "here.",
            parsed.size >= capacity
        )
    }

    @Test
    fun theFileStillCarriesItsGroupHeadings() {
        val groups = lines.filter { it.startsWith("# group:") }
        assertTrue(
            "The emoji file carries ${groups.size} group headings. The whole-list browser is " +
                "organised by those groups, so a format change that collapsed them would " +
                "leave the browser with nothing to sort by.",
            groups.size > 1
        )
    }

    @Test
    fun mostOfTheFilesEntriesAreStillRecognised() {
        val data = lines.filter { it.isNotBlank() && !it.startsWith("#") }
        assertTrue(
            "The emoji file has no data lines at all, so nothing below can be judged.",
            data.isNotEmpty()
        )
        val share = parsed.size.toDouble() / data.size
        assertTrue(
            "Only ${parsed.size} of ${data.size} data lines were recognised, a share of " +
                "${"%.3f".format(share)}. The file ordinarily mixes fully-qualified entries " +
                "with lesser-qualified spellings of the same emoji and sits well above half; " +
                "a share near zero means the status column has moved and the parse is reading " +
                "the wrong thing, which would empty the panels silently.",
            share >= 0.5
        )
    }

    /**
     * Finds resources/emoji-test.txt. Same approach as `KeyLayoutValidationTest`: the Gradle
     * build passes the repo root in as a system property, and the walk-up fallback keeps the
     * test runnable from an IDE with a different working directory.
     */
    private fun emojiFile(): File {
        val fromProperty = System.getProperty("hexboard.repoRoot")?.let { File(it, EMOJI_PATH) }
        if (fromProperty != null && fromProperty.isFile) return fromProperty

        var dir: File? = File(System.getProperty("user.dir"))
        while (dir != null) {
            val candidate = File(dir, EMOJI_PATH)
            if (candidate.isFile) return candidate
            dir = dir.parentFile
        }
        fail(
            "Could not find $EMOJI_PATH. Looked at the hexboard.repoRoot system property and " +
                "walked up from ${System.getProperty("user.dir")}."
        )
        error("unreachable")
    }

    private companion object {
        const val EMOJI_PATH = "resources/emoji-test.txt"
    }
}
