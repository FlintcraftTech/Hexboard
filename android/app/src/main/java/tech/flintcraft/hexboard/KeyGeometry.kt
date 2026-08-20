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
     * How much smaller the drawn circle is than the touch target, per side.
     *
     * SPEC: the visible circle is intentionally smaller than the touch target — a bigger
     * targeting zone, and less crowded aesthetics.
     */
    const val VISIBLE_INSET = 3f

    /** Bounds on the solved radius, so a very narrow or very wide screen still reads. */
    const val MIN_RADIUS = 12f
    const val MAX_RADIUS = 34f

    /** SPEC: uppercase glyphs render at 0.92x lowercase, so capitals stop overfilling. */
    const val UPPERCASE_SCALE = 0.92f

    /** Fraction of the visible circle a label fills: large keys sit back a little. */
    const val LABEL_FRACTION = 0.90f
    const val LARGE_LABEL_FRACTION = 0.78f

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

    /** The radius of the circle that is actually drawn — smaller than the touch target. */
    fun visibleRadius(radius: Float): Float = radius - VISIBLE_INSET

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
     * Sized against the visible circle rather than the touch target, so the glyph fits
     * what the eye sees. Capitals are scaled down per SPEC.
     */
    fun labelSize(radius: Float, large: Boolean, label: String): Float {
        val fraction = if (large) LARGE_LABEL_FRACTION else LABEL_FRACTION
        val capsScale = if (isUppercase(label)) UPPERCASE_SCALE else 1f
        return visibleRadius(radius) * fraction * capsScale
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
