package tech.flintcraft.hexboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The rows alternate between two resting colours, so the zigzag still reads as QWERTY rows.
 *
 * SPEC: rows alternate between two resting key colours, far enough apart to notice and
 * nowhere near far enough to be mistaken for a press. The banding is what says the rows are
 * still the QWERTY rows and only zigzag, and it runs across rows rather than columns because
 * reading runs left to right.
 *
 * The last test is the one that matters most and is easy to lose: the tint must stay well
 * short of the pressed colour. A press travels the whole way from wherever a key rests to
 * `lit`, so a tint creeping toward `lit` would make a resting key in an odd row look pressed
 * — and press feedback is the key's own highlight and nothing else, so there would be
 * nothing else to tell them apart. That failure would merely look wrong on a screen; here it
 * fails a test instead.
 */
class RowTintTest {

    @Test
    fun evenRowsRestAtTheUntintedFill() {
        listOf(0, 2, 4, 6).forEach { row ->
            assertEquals(
                "Row $row is even and should rest at the plain fill.",
                0f.toDouble(),
                KeyGeometry.rowTint(row).toDouble(),
                0.0001
            )
        }
    }

    @Test
    fun everyOddRowRestsAtTheSameTint() {
        val tints = listOf(1, 3, 5, 7).map { KeyGeometry.rowTint(it) }
        assertTrue(
            "Odd rows should all carry one tint rather than a step per row, which would " +
                "accumulate down the board instead of reading as banding. Got $tints.",
            tints.distinct().size == 1
        )
        assertTrue("The odd-row tint is zero, so there is no banding at all.", tints.first() > 0f)
    }

    @Test
    fun theTintIsOneFifth() {
        assertEquals(
            "The tint should be one fifth of the way from the resting fill to the pressed " +
                "colour — the smallest step expected to read as deliberate.",
            0.2.toDouble(),
            KeyGeometry.ROW_TINT.toDouble(),
            0.0001
        )
        assertEquals(
            "rowTint should hand back exactly ROW_TINT for an odd row.",
            KeyGeometry.ROW_TINT.toDouble(),
            KeyGeometry.rowTint(1).toDouble(),
            0.0001
        )
    }

    @Test
    fun theTintStaysWellShortOfAPress() {
        assertTrue(
            "The odd-row tint is ${KeyGeometry.ROW_TINT} of the full press travel. A resting " +
                "key would be too close to a pressed one to tell apart.",
            KeyGeometry.ROW_TINT < FULL_PRESS
        )
        assertTrue(
            "A pressed key should stay at least four fifths brighter than a resting key in a " +
                "tinted row; the gap here is ${FULL_PRESS - KeyGeometry.ROW_TINT}.",
            FULL_PRESS - KeyGeometry.ROW_TINT >= 0.8f - 0.0001f
        )
    }

    private companion object {
        /** How far a press travels: all the way from the resting colour to `lit`. */
        const val FULL_PRESS = 1f
    }
}
