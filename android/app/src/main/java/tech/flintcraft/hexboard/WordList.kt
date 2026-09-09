package tech.flintcraft.hexboard

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * The English word list the planned correction engine compares finished words against.
 *
 * SPEC: correction happens at the word boundary — pressing space compares the finished
 * word against the dictionary and replaces it with the closest match — and a word already
 * in the dictionary is never corrected. Both of those questions are answered here, and
 * nothing else is: measuring closeness needs the neighbour table in [KeyGeometry], and
 * choosing between two equally close candidates needs the level this list carries.
 *
 * The list is generated once by hand, by `scripts/generate-word-list.py`, and committed to
 * the repository — `android/app/build.gradle.kts` copies it into the assets alongside the
 * key configs and Unicode's emoji data. It is not fetched at build time, so a build depends
 * on no network and on nobody else's uptime.
 *
 * **No proper nouns.** SCOWL separates names into categories of their own and the generator
 * leaves those out, so the absence is a property of what was generated rather than of a
 * filter that runs here. A word corrected into someone's name is the most irritating
 * failure autocorrect has, which is why SPEC bars it.
 *
 * **The level is a commonness ranking, not a frequency.** SCOWL ships no frequency data;
 * what it has is size levels, and a word's lowest level is a coarse measure of how common
 * it is. Lower is commoner. A real frequency table is a later question.
 */
class WordList private constructor(private val levels: Map<String, Int>) {

    /** How many words the list carries. */
    val size: Int get() = levels.size

    /**
     * Is this exact word in the list?
     *
     * Exact, and deliberately so: the comparison the engine does is against the word as
     * typed, and a lenient match here would quietly answer a different question than the
     * one SPEC's never-correct-a-real-word rule asks.
     */
    fun contains(word: String): Boolean = levels.containsKey(word)

    /** This word's SCOWL size level, or null when the word is not in the list. */
    fun levelOf(word: String): Int? = levels[word]

    companion object {
        /** The asset name the Gradle copy task writes the list to. */
        const val ASSET_NAME = "wordlist-en.txt"

        fun fromAssets(context: Context, assetName: String = ASSET_NAME): WordList =
            context.assets.open(assetName).use { stream ->
                InputStreamReader(stream, Charsets.UTF_8).buffered().use(::parse)
            }

        /**
         * Reads the generated format: comment lines opening with `#`, then one entry per
         * line as the word, a tab, and its level.
         *
         * `internal` so a unit test can run it against the committed file without a device.
         */
        internal fun parse(reader: BufferedReader): WordList {
            val levels = HashMap<String, Int>()
            reader.forEachLine { line ->
                if (line.isBlank() || line.startsWith("#")) return@forEachLine
                val tab = line.indexOf('\t')
                if (tab <= 0) return@forEachLine
                val word = line.substring(0, tab)
                val level = line.substring(tab + 1).trim().toIntOrNull() ?: return@forEachLine
                levels[word] = level
            }
            return WordList(levels)
        }
    }
}
