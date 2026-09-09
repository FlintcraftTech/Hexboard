package tech.flintcraft.hexboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The keyboard cannot grow to fill a sideways screen, and portrait is untouched.
 *
 * `solveRadius` used to take a width and a column count and consult no height at all, and
 * on a Pixel 6 turned sideways the width is wide enough to reach `MAX_RADIUS`: four rows at
 * 34dp plus the row above them comes to about 394dp on a screen about 411dp tall, leaving a
 * sliver of text field. Nobody has seen that — the finding is arithmetic — which is exactly
 * why it is asserted here rather than checked by eye.
 *
 * The second test is the more important one. A height bound that quietly shrank the keys in
 * portrait would be a worse defect than the one it repairs, so portrait must solve to
 * precisely what the width-only solver returns.
 *
 * Both are arithmetic over `KeyGeometry`, so neither needs a device.
 */
class BoardHeightBoundTest {

    @Test
    fun portraitSolvesToExactlyWhatTheWidthAloneGives() {
        ROW_COUNTS.forEach { rows ->
            COLUMN_COUNTS.forEach { cols ->
                val widthOnly = KeyGeometry.solveRadius(PORTRAIT_WIDTH, cols)
                val bounded = solve(PORTRAIT_WIDTH, cols, PORTRAIT_HEIGHT, rows)
                assertEquals(
                    "In portrait ($PORTRAIT_WIDTH by ${PORTRAIT_HEIGHT}dp) a $rows-row, " +
                        "$cols-column board should solve to the same radius as before the " +
                        "height bound existed. The board there comes to " +
                        "${occupied(rows, bounded)}dp against a ceiling of " +
                        "${PORTRAIT_HEIGHT * KeyGeometry.MAX_HEIGHT_SHARE}dp, so the height " +
                        "must not be binding.",
                    widthOnly.toDouble(),
                    bounded.toDouble(),
                    0.0001
                )
            }
        }
    }

    @Test
    fun landscapeIsHeldToHalfTheScreen() {
        ROW_COUNTS.forEach { rows ->
            COLUMN_COUNTS.forEach { cols ->
                val bounded = solve(LANDSCAPE_WIDTH, cols, LANDSCAPE_HEIGHT, rows)
                val ceiling = LANDSCAPE_HEIGHT * KeyGeometry.MAX_HEIGHT_SHARE
                assertTrue(
                    "In landscape ($LANDSCAPE_WIDTH by ${LANDSCAPE_HEIGHT}dp) a $rows-row, " +
                        "$cols-column board takes ${occupied(rows, bounded)}dp, past the " +
                        "${ceiling}dp a keyboard may have.",
                    occupied(rows, bounded) <= ceiling + 0.5f
                )
                assertTrue(
                    "The landscape radius came out at ${bounded}dp, at or below " +
                        "MIN_RADIUS. The bound should shrink the keys, not floor them.",
                    bounded > KeyGeometry.MIN_RADIUS
                )
            }
        }
    }

    @Test
    fun theWidthStillDecidesWhereThereIsHeightToSpare() {
        val bounded = solve(PORTRAIT_WIDTH, 10, availableHeightDp = 0f, rows = 4)
        assertEquals(
            "With no height given the solver has nothing to bound against and must fall back " +
                "to the width alone, so a caller that has no height to offer is unaffected.",
            KeyGeometry.solveRadius(PORTRAIT_WIDTH, 10).toDouble(),
            bounded.toDouble(),
            0.0001
        )
    }

    private fun solve(width: Float, cols: Int, availableHeightDp: Float, rows: Int): Float =
        KeyGeometry.solveRadius(
            widthDp = width,
            cols = cols,
            availableHeightDp = availableHeightDp,
            rowCount = rows,
            stripDp = ::stripHeight
        )

    /** What the board plus the row above it actually takes. */
    private fun occupied(rows: Int, radius: Float): Float =
        KeyGeometry.boardHeight(rows, radius) + stripHeight(radius)

    private companion object {
        /** The Pixel 6, the handset this project has solved against throughout. */
        const val PORTRAIT_WIDTH = 411f
        const val PORTRAIT_HEIGHT = 915f
        const val LANDSCAPE_WIDTH = 915f
        const val LANDSCAPE_HEIGHT = 411f

        /** Four rows for a letter panel, five once the emoji panels set the height. */
        val ROW_COUNTS = listOf(4, EmojiCatalogue.ROWS)

        /** Ten for the English board, eleven for the German and Russian ones. */
        val COLUMN_COUNTS = listOf(10, 11)
    }
}
