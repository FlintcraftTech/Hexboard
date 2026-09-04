package tech.flintcraft.hexboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The relationships a soft key edge has to keep, whatever the fade is tuned to.
 *
 * SPEC: a key is drawn as a soft-edged circle with no border, solid through the middle and
 * fading to nothing at the edge of the area it accepts — drawn as wide as it is tappable
 * rather than sitting well inside it. The two fractions are tunable and how the fade looks
 * is a judgment to make on the phone; what this protects is the structure underneath, so a
 * later tuning pass cannot quietly break it:
 *
 *  - the solid part ends before the fade does, so there is a fade at all;
 *  - the fade reaches the whole touch target, so the drawn key is as wide as the area it
 *    accepts and nothing is silently given away at the edge;
 *  - a label sits inside the solid part rather than out on the fade;
 *  - the edge is expressed as fractions of the radius, so it stays in proportion on a
 *    narrow key instead of eating a larger share of a smaller one.
 *
 * One thing here is deliberately not tested: whether a border is drawn. That is a Compose
 * drawing fact rather than a geometry one, and a unit test cannot see it — the guarantee is
 * that `KeyCircle` makes no `border` call and paints a radial gradient instead.
 */
class KeyEdgeTest {

    @Test
    fun theSolidPartEndsBeforeTheFadeDoes() {
        assertTrue(
            "The solid fraction (${KeyGeometry.SOLID_FRACTION}) must be strictly less than " +
                "the outer fraction (${KeyGeometry.FADE_FRACTION}), or there is no fade at all " +
                "and the key is a hard disc again.",
            KeyGeometry.SOLID_FRACTION < KeyGeometry.FADE_FRACTION
        )
    }

    @Test
    fun theDrawnKeyReachesItsWholeTouchTarget() {
        assertTrue(
            "The outer fraction is ${KeyGeometry.FADE_FRACTION}, so the key is drawn smaller " +
                "than the area it accepts. SPEC says a key is drawn as wide as it is tappable.",
            KeyGeometry.FADE_FRACTION >= 1f
        )
        RADII.forEach { radius ->
            assertEquals(
                "At radius ${radius}dp the fade should reach ${radius * KeyGeometry.FADE_FRACTION}dp.",
                (radius * KeyGeometry.FADE_FRACTION).toDouble(),
                KeyGeometry.fadeRadius(radius).toDouble(),
                0.001
            )
        }
    }

    @Test
    fun everyLabelFitsInsideTheSolidPart() {
        val labels = listOf("q", "Q", "?", "Space", "Backspace", "@")
        RADII.forEach { radius ->
            val solid = KeyGeometry.solidRadius(radius)
            labels.forEach { label ->
                listOf(false, true).forEach { large ->
                    val size = KeyGeometry.labelSize(radius, large, label)
                    assertTrue(
                        "At radius ${radius}dp the label \"$label\" is sized ${size}dp, which " +
                            "is outside the solid part at ${solid}dp — the glyph would sit on " +
                            "the fade.",
                        size <= solid
                    )
                }
            }
        }
    }

    @Test
    fun theEdgeStaysInProportionOnANarrowKey() {
        val shareOfArea = RADII.map { radius ->
            val drawn = KeyGeometry.fadeRadius(radius)
            (drawn * drawn) / (radius * radius)
        }
        val smallest = shareOfArea.min()
        val largest = shareOfArea.max()
        assertEquals(
            "A key's drawn share of its touch area should be the same at every radius — the " +
                "absolute inset this replaced gave away more of a small key than a large one. " +
                "Got $smallest at the narrowest and $largest at the widest.",
            largest.toDouble(),
            smallest.toDouble(),
            0.001
        )
    }

    private companion object {
        /** The narrowest and widest keys the solver will produce, and one in between. */
        val RADII = listOf(KeyGeometry.MIN_RADIUS, 22f, KeyGeometry.MAX_RADIUS)
    }
}
