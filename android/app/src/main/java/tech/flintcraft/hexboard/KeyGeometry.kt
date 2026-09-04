package tech.flintcraft.hexboard

import kotlin.math.max
import kotlin.math.sqrt

/**
 * The single home of Hexboard's perceptual geometry.
 *
 * SPEC calls the geometry inviolable and the key config excludes it by design, so every
 * position, radius and label size in the app is computed here and nowhere else. The rules
 * are ported from `planning/layout-preview.html`, which carries the working parity and
 * circle maths lifted from the frozen prototype `hexboard17.html`.
 *
 * Everything here is in density-independent units — a Float of dp rather than a Dp — so
 * the maths stays plain Kotlin and testable without Compose. Callers wrap the results in
 * `.dp` at the point of drawing.
 *
 * The zag rule: odd columns sit half a vertical step lower than even ones. That is what
 * buys the key size, and it is why column parity, not row, decides vertical offset.
 */
object KeyGeometry {

    /** Columns in a full letter panel. The prototype's `PANEL_COLS`. */
    const val PANEL_COLS = 10

    /** Breathing room reserved at the left and right edges of the board. */
    const val EDGE = 6f

    /**
     * Where a key's fill stops being solid and where it has faded to nothing, as fractions
     * of the touch radius.
     *
     * SPEC: a key is drawn as a soft-edged circle with no border — solid through the middle,
     * fading to nothing at the edge of the area it accepts. So a key is drawn as wide as it
     * is tappable rather than sitting well inside it, and there is no hard boundary to aim
     * within, only a soft target to aim at.
     *
     * They are fractions of the radius rather than absolute dp so the edge stays in
     * proportion on a narrow key. The absolute 3dp inset they replaced gave away a larger
     * share of a smaller key — worse on an eleven-wide layout, and down to about 56% of the
     * touch area at [MIN_RADIUS].
     *
     * These two are tunable constants rather than decisions: whether the edge is soft at all
     * is the decision, and how steep the fade is is a curve to judge on the phone. This is
     * the conservative starting point — it lights more area than the old 3dp disc while
     * putting nothing new into the gaps, so it cannot read as more crowded than before. The
     * generous end worth trying is solid to 0.45 and transparent at about 1.18, where
     * neighbouring fades just meet.
     */
    const val SOLID_FRACTION = 0.55f
    const val FADE_FRACTION = 1.0f

    /** Bounds on the solved radius, so a very narrow or very wide screen still reads. */
    const val MIN_RADIUS = 12f
    const val MAX_RADIUS = 34f

    /** SPEC: uppercase glyphs render at 0.92x lowercase, so capitals stop overfilling. */
    const val UPPERCASE_SCALE = 0.92f

    /** Fraction of the visible circle a label fills: large keys sit back a little. */
    const val LABEL_FRACTION = 0.90f
    const val LARGE_LABEL_FRACTION = 0.78f

    /**
     * How far an odd row's resting fill travels toward the pressed colour.
     *
     * The rows are still the QWERTY rows and only zigzag, and people read left to right, so
     * the row is the unit that carries recognition — QWERTYUIOP is a string almost everyone
     * knows on sight. Banding says *this is the keyboard you already use*, which is what
     * SPEC's familiarity principle is defended by. It runs across rows rather than columns
     * because nobody reads a keyboard downwards.
     *
     * A fifth of the way, so the tint introduces no new colour and cannot clash with the
     * theme, and its distance from a pressed key is measurable rather than a matter of
     * taste: a press travels the whole way, so a pressed key stays four-fifths brighter than
     * its unpressed neighbours even in a tinted row. The figure is the smallest step
     * expected to read as deliberate rather than as a rendering artefact, and it is one
     * constant, adjustable once it has been seen on a real screen.
     */
    const val ROW_TINT = 0.2f

    /** How far this row's resting fill sits along the line from the fill to the lit colour. */
    fun rowTint(row: Int): Float = if (row % 2 == 0) 0f else ROW_TINT

    /** A point on the board, in dp from the board's top-left corner. */
    data class Point(val x: Float, val y: Float)

    /** Column parity — 1 for the columns that sit half a key lower. */
    fun parity(col: Int): Int = ((col % 2) + 2) % 2

    /** The gap between neighbouring touch targets, which grows with the keys. */
    fun gap(radius: Float): Float = max(1.5f, radius * 0.09f)

    /** Vertical distance between one row's centres and the next. */
    fun verticalStep(radius: Float): Float = 2f * radius + gap(radius)

    /**
     * Horizontal distance between neighbouring columns.
     *
     * The sqrt(3)/2 factor is what makes the packing hexagonal: with the odd columns
     * dropped half a step, every key sits the same distance from each of its six
     * neighbours.
     */
    fun horizontalStep(radius: Float): Float = verticalStep(radius) * (sqrt(3f) / 2f)

    /**
     * The largest touch radius that fits `cols` columns into `widthDp`.
     *
     * Ported from the prototype's `solveR`. The gap depends on the radius and the radius
     * depends on the gap, so it is solved by iterating rather than in closed form; twenty
     * passes is far past where it settles.
     */
    fun solveRadius(widthDp: Float, cols: Int = PANEL_COLS): Float {
        if (widthDp <= 0f) return MIN_RADIUS
        var r = 22f
        repeat(20) {
            val step = verticalStep(r) * (sqrt(3f) / 2f)
            val boardWidth = (cols - 1) * step + 2f * r
            r *= (widthDp - EDGE * 2f) / boardWidth
        }
        return r.coerceIn(MIN_RADIUS, MAX_RADIUS)
    }

    /** The centre of the touch target for a key at this row and column. */
    fun centre(row: Int, col: Int, radius: Float): Point {
        val vs = verticalStep(radius)
        val hs = horizontalStep(radius)
        return Point(
            x = EDGE + radius + col * hs,
            y = radius + row * vs + if (parity(col) == 1) vs * 0.5f else 0f
        )
    }

    /** Where the key's fill is still fully solid — the part with a definite edge to aim at. */
    fun solidRadius(radius: Float): Float = radius * SOLID_FRACTION

    /** Where the fill has faded to nothing: the outer extent of what is drawn at all. */
    fun fadeRadius(radius: Float): Float = radius * FADE_FRACTION

    /**
     * How far right to shift a panel narrower than its layout's widest, so it sits centred.
     *
     * All three letter panels of a layout draw at one key size — the largest that fits the
     * widest of them — so a narrower panel does not fill the board's width. Centring is what
     * it does with the difference: half of it on each side. Spreading the keys to fill the
     * width instead was refused, because that breaks the hexagonal packing SPEC calls
     * inviolable. Zero when this panel is the widest.
     */
    fun centringIndent(radius: Float, cols: Int, widestCols: Int): Float =
        ((boardWidth(radius, widestCols) - boardWidth(radius, cols)) / 2f).coerceAtLeast(0f)

    /** Board width for a panel `cols` columns wide, including both edge margins. */
    fun boardWidth(radius: Float, cols: Int = PANEL_COLS): Float =
        (cols - 1) * horizontalStep(radius) + 2f * radius + EDGE * 2f

    /**
     * Board height for a panel of `rowCount` zag rows.
     *
     * The deepest point is an odd column on the last row, half a step below its
     * neighbours, so the bottom edge sits at `2r + (rowCount - 0.5) * vs`.
     */
    fun boardHeight(rowCount: Int, radius: Float): Float =
        2f * radius + (rowCount - 0.5f) * verticalStep(radius) + EDGE

    /**
     * Label size for a key, in dp.
     *
     * Sized against the solid part of the key rather than the touch target or the fade, so
     * a glyph sits inside the definite middle of the circle rather than out on the soft
     * edge where the fill is already going. Capitals are scaled down per SPEC.
     */
    fun labelSize(radius: Float, large: Boolean, label: String): Float {
        val fraction = if (large) LARGE_LABEL_FRACTION else LABEL_FRACTION
        val capsScale = if (isUppercase(label)) UPPERCASE_SCALE else 1f
        return solidRadius(radius) * fraction * capsScale
    }

    /** True when the label is a capital letter — the case the uppercase scale is for. */
    private fun isUppercase(label: String): Boolean =
        label.isNotEmpty() && label.all { it.isLetter() && it.isUpperCase() }

    /**
     * Nearest-centre (Voronoi) hit-testing: the index of the key whose centre is closest.
     *
     * SPEC's intended routing. Every point on the board belongs to exactly one key, so
     * there are no gaps between circles and no z-order tie-breaks to get wrong. Returns
     * -1 when there are no centres at all.
     *
     * **Why full coverage matters, and not merely that it holds.** A tap that lands nowhere
     * is a deletion — a character the user meant to type and did not get. The planned
     * predictive engine weights a substitution by which keys neighbour which, so it can
     * repair a wrong letter; it cannot repair a letter that was never entered. Dead space
     * between circles would therefore produce the one failure that engine cannot fix, which
     * is the reason to route by nearest centre rather than by whichever circle contains the
     * point.
     */
    fun nearestCentre(centres: List<Point>, x: Float, y: Float): Int {
        var best = -1
        var bestDistance = Float.MAX_VALUE
        centres.forEachIndexed { index, centre ->
            val dx = centre.x - x
            val dy = centre.y - y
            val distance = dx * dx + dy * dy
            if (distance < bestDistance) {
                bestDistance = distance
                best = index
            }
        }
        return best
    }
}
