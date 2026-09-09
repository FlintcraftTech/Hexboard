package tech.flintcraft.hexboard

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

/**
 * The committed English word list, checked against what SPEC asks of it.
 *
 * This runs against `resources/wordlist-en.txt` in the repository rather than against the
 * copy in the app's assets, the same way `KeyLayoutValidationTest` reads the key config.
 * Whether the list actually reaches the assets is a fact about a running app and belongs to
 * an install rather than to a unit test.
 *
 * The no-proper-nouns check is the one worth explaining. SPEC bars proper nouns from the
 * shipped dictionary because a word corrected into somebody's name is the most irritating
 * failure autocorrect has. The generator satisfies that by leaving SCOWL's own `upper` and
 * `proper-names` categories out, and this asserts the result rather than the intention —
 * which is what makes the rule checkable instead of merely stated.
 */
class WordListTest {

    private val list: WordList by lazy {
        listFile().bufferedReader(Charsets.UTF_8).use(WordList.Companion::parse)
    }

    private val header: List<String> by lazy {
        listFile().readLines(Charsets.UTF_8).takeWhile { it.isBlank() || it.startsWith("#") }
    }

    @Test
    fun theCommittedListParses() {
        assertTrue(
            "The word list parsed to ${list.size} entries. A list this small means the " +
                "format the generator writes and the format WordList reads have come apart.",
            list.size > 10_000
        )
    }

    @Test
    fun ordinaryWordsArePresent() {
        ORDINARY.forEach { word ->
            assertTrue(
                "'$word' is not in the word list. SPEC says a word already in the dictionary " +
                    "is never corrected, so an everyday word missing here is a word the " +
                    "keyboard would feel free to change.",
                list.contains(word)
            )
            assertNotNull(
                "'$word' is in the list with no size level, so nothing can break a tie with it.",
                list.levelOf(word)
            )
        }
    }

    @Test
    fun properNounsAreAbsent() {
        NAMES.forEach { name ->
            assertFalse(
                "'$name' is in the word list. SPEC bars proper nouns from the shipped " +
                    "dictionary — the generator leaves SCOWL's upper and proper-names " +
                    "categories out, so a name here means the wrong categories were read.",
                list.contains(name)
            )
        }
    }

    @Test
    fun theHeaderNamesItsSourceAndSize() {
        val text = header.joinToString("\n")
        assertTrue(
            "The list's header does not name the SCOWL release it came from, so it cannot " +
                "be regenerated identically. Header was:\n$text",
            Regex("SCOWL\\s+\\S+").containsMatchIn(text)
        )
        assertTrue(
            "The list's header does not name the size level it was generated at. Header " +
                "was:\n$text",
            Regex("size level\\s+\\d+").containsMatchIn(text)
        )
    }

    /**
     * Finds resources/wordlist-en.txt. Same approach as `KeyLayoutValidationTest`: the
     * Gradle build passes the repo root in as a system property, and the walk-up fallback
     * keeps the test runnable from an IDE with a different working directory.
     */
    private fun listFile(): File {
        val fromProperty = System.getProperty("hexboard.repoRoot")?.let { File(it, LIST_PATH) }
        if (fromProperty != null && fromProperty.isFile) return fromProperty

        var dir: File? = File(System.getProperty("user.dir"))
        while (dir != null) {
            val candidate = File(dir, LIST_PATH)
            if (candidate.isFile) return candidate
            dir = dir.parentFile
        }
        fail(
            "Could not find $LIST_PATH. It is generated once by hand with " +
                "scripts/generate-word-list.py and committed; see that script's header."
        )
        error("unreachable")
    }

    private companion object {
        const val LIST_PATH = "resources/wordlist-en.txt"

        /** Everyday words, a contraction, and both national spellings of one word. */
        val ORDINARY = listOf(
            "the", "keyboard", "because", "thought", "don't", "colour", "color"
        )

        /**
         * Common first names and place names, in the capitalised form they are written in.
         * The word list is built from SCOWL's lowercase word categories, so none of these
         * should be present in any form.
         */
        val NAMES = listOf(
            "John", "Sarah", "Michael", "London", "Paris", "Berlin", "Australia"
        )
    }
}
