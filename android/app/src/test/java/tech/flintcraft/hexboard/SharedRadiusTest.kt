package tech.flintcraft.hexboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * All three letter panels of a layout draw at one key size, and a narrower panel sits centred.
 *
 * SPEC: the largest size that fits the widest of them, with the narrower panel centred, so
 * switching panels never resizes the board. Sizing each panel from its own widest row meant a
 * swipe changed every key by about ten per cent and moved every centre — the board's height
 * was already the tallest panel's for exactly that reason, and this answers the same question
 * the same way.
 *
 * Two relationships carry that, and this holds both:
 *
 *  1. the radius the board hands every panel is what the widest panel alone would have
 *     solved, not what each panel would have solved for itself;
 *  2. a narrower panel is shifted right by half the difference in board width, so it is
 *     centred rather than left-aligned — and its keys keep their spacing, since spreading
 *     them to fill the width would break the hexagonal packing SPEC calls inviolable.
 *
 * The widths used are the real case: a layout whose QWERTY panel is eleven columns wide
 * against a ten-column sibling, which is what the Russian layout is.
 *
 * Nothing here is shared across layouts. A wider alphabet still gets correspondingly smaller
 * keys — this shares a radius only between the panels within one layout.
 */
class SharedRadiusTest {

    @Test
    fun everyPanelTakesTheWidestPanelsRadius() {
        val shared = KeyGeometry.solveRadius(SCREEN, WIDE)
        val ownSolve = KeyGeometry.solveRadius(SCREEN, NARROW)

        assertEquals(
            "The shared radius should be what the widest panel alone would solve.",
            KeyGeometry.solveRadius(SCREEN, WIDE).toDouble(),
            shared.toDouble(),
            0.0001
        )
        assertTrue(
            "The narrower panel would solve ${ownSolve}dp on its own against the shared " +
                "${shared}dp. If those were equal there would be nothing for this item to fix.",
            ownSolve > shared
        )
    }

    @Test
    fun aNarrowerPanelIsCentredRatherThanLeftAligned() {
        val shared = KeyGeometry.solveRadius(SCREEN, WIDE)
        val difference = KeyGeometry.boardWidth(shared, WIDE) - KeyGeometry.boardWidth(shared, NARROW)

        assertEquals(
            "A narrower panel should be shifted by half the difference in board width — " +
                "${difference / 2f}dp of the ${difference}dp it leaves over.",
            (difference / 2f).toDouble(),
            KeyGeometry.centringIndent(shared, NARROW, WIDE).toDouble(),
            0.0001
        )

        val wideLeftmost = KeyGeometry.centre(0, 0, shared).x
        val narrowLeftmost =
            KeyGeometry.centre(0, 0, shared).x + KeyGeometry.centringIndent(shared, NARROW, WIDE)
        assertTrue(
            "The narrower panel's leftmost key centre sits at ${narrowLeftmost}dp, not right " +
                "of the widest panel's at ${wideLeftmost}dp — so it is left-aligned rather " +
                "than centred.",
            narrowLeftmost > wideLeftmost
        )
    }

    @Test
    fun theWidestPanelIsNotShifted() {
        val shared = KeyGeometry.solveRadius(SCREEN, WIDE)
        assertEquals(
            "The panel the radius was solved from fills the width, so it must not move.",
            0.0,
            KeyGeometry.centringIndent(shared, WIDE, WIDE).toDouble(),
            0.0001
        )
    }

    @Test
    fun centringDoesNotChangeTheSpacingBetweenKeys() {
        val shared = KeyGeometry.solveRadius(SCREEN, WIDE)
        val indent = KeyGeometry.centringIndent(shared, NARROW, WIDE)
        val first = KeyGeometry.centre(0, 0, shared).x + indent
        val second = KeyGeometry.centre(0, 1, shared).x + indent

        assertEquals(
            "Centring shifts the whole panel and must leave the hexagonal packing alone.",
            KeyGeometry.horizontalStep(shared).toDouble(),
            (second - first).toDouble(),
            0.0001
        )
    }

    private companion object {
        /** The Pixel 6's width in dp, which is the screen this project measures against. */
        const val SCREEN = 411f
        const val WIDE = 11
        const val NARROW = 10
    }
}
