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
     * is the decision, and how steep the fade is is a curve to judge on the phone.
     *
     * The first pass at these values was solid to 0.55 and transparent at 1.0, and it read
     * as a shrinkage: the hard disc it replaced was `radius - 3dp`, which is 0.86 of the
     * touch radius at the 22dp the Pixel 6 solves, so the part of a key that looks
     * definitely there fell by nearly a third — and labels, sized against the solid radius,
     * shrank with it. Stopping the fade at 1.0 also put nothing into the space between
     * keys, which is what the soft edge was for.
     *
     * So: solid to 0.75, most of the way back to the old disc while staying clear of an
     * edge so late it reads as hard again, and faded to nothing at 1.045.
     *
     * **Where 1.045 comes from, and why it is a ceiling rather than a taste.** Every one of
     * a key's six neighbours sits exactly one vertical step away — `2 * radius +
     * gap(radius)` — and the gap is `max(1.5, radius * 0.09)`. At any solved radius at or
     * above about 16.7dp the gap is 0.09 of the radius, so neighbouring centres are 2.09
     * radii apart and half that distance is 1.045: adjacent fades meet exactly and there is
     * no dead space between them. Below 16.7dp the 1.5dp floor takes over and the centres
     * sit proportionally further apart, so the fades fall a little short of meeting rather
     * than running into each other. There is no radius at which they overlap.
     *
     * A comment here previously named 1.18 as the point where neighbouring fades just meet.
     * That figure is wrong — at 1.18 radii they overlap substantially — and it is corrected
     * with the arithmetic beside it rather than merely replaced.
     */
    const val SOLID_FRACTION = 0.75f
    const val FADE_FRACTION = 1.045f

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

    /**
     * The share of the screen's height a keyboard may take.
     *
     * A proportion rather than a number of dp, which is how every other size in this file is
     * derived, and a half is the share a phone keyboard conventionally takes rather than a
     * figure invented here.
     */
    const val MAX_HEIGHT_SHARE = 0.5f

    /**
     * The largest radius that fits `cols` columns into `widthDp` **and** keeps the board
     * within its share of `availableHeightDp`.
     *
     * Without the height limit the width alone decides, and on a phone turned sideways the
     * width is wide enough to hit [MAX_RADIUS]: four rows at 34dp plus the strip above them
     * comes to about 394dp on a screen about 411dp tall, which is a screenful of keyboard
     * with a sliver of text field above it.
     *
     * The smaller of the two answers wins, floored at [MIN_RADIUS] as ever. In portrait the
     * height constraint should never bind — the board there comes to roughly 259dp on a
     * screen around 915dp tall, well inside half — and a fix that quietly shrank the keys
     * everyone already uses would be a worse defect than the one it repairs.
     *
     * @param stripDp the height of anything drawn above the keys, since it is part of what
     *   has to fit. Passed rather than computed here because the strip belongs to the board
     *   rather than to the geometry.
     */
    fun solveRadius(
        widthDp: Float,
        cols: Int,
        availableHeightDp: Float,
        rowCount: Int,
        stripDp: (Float) -> Float
    ): Float {
        val fromWidth = solveRadius(widthDp, cols)
        if (availableHeightDp <= 0f || rowCount <= 0) return fromWidth
        val ceiling = availableHeightDp * MAX_HEIGHT_SHARE
        if (boardHeight(rowCount, fromWidth) + stripDp(fromWidth) <= ceiling) return fromWidth

        // Board height grows monotonically with the radius, so the largest radius that fits
        // is found by walking down from the width's answer. A twentieth of a dp is finer
        // than any screen can show and bounds the loop at a few hundred steps.
        var radius = fromWidth
        while (radius > MIN_RADIUS &&
            boardHeight(rowCount, radius) + stripDp(radius) > ceiling
        ) {
            radius -= 0.05f
        }
        return radius.coerceAtLeast(MIN_RADIUS)
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
     * One position on a panel, for the purpose of working out what neighbours what.
     *
     * [participates] is false for a key that is not a candidate in a mis-tap: it gets no
     * neighbours of its own and appears in nobody else's list, while keeping its index so
     * the returned table lines up with the panel's own key list.
     */
    data class Slot(val row: Int, val col: Int, val participates: Boolean = true)

    /**
     * For each key on one panel, the keys that sit next to it.
     *
     * This is the table the planned predictive engine measures with: SPEC says a
     * substitution of a key for one of its six neighbours is a near-miss and any other
     * substitution is a real difference, and nothing else in the app knows which keys touch
     * which. It cannot be read out of the config, which carries `row` and `col` and no
     * geometry at all — which key touches which depends on the zag parity, and the zag is
     * the perceptual wedge and lives here.
     *
     * **How a neighbour is found.** In the hexagonal packing every one of a key's six
     * neighbours sits exactly one vertical step away — see [horizontalStep] for why the
     * diagonal ones come out at the same distance as the vertical ones. So a key's
     * neighbours are the keys on the same panel whose centre lies one vertical step from
     * its own. The tolerance exists because the centres are computed in floating point,
     * not because the spacing is approximate.
     *
     * **The result is scale-free.** Every distance in this file is a multiple of the
     * radius, so the same table comes out at any solved radius and the caller may pass
     * whichever one it happens to have.
     *
     * Three things settled about the model, recorded here because they look like gaps:
     *
     *  - a key at a panel's edge has fewer than six neighbours, and that is correct. Six is
     *    what an interior key happens to have; the measure needs a neighbour set, not a set
     *    of a particular size.
     *  - the space keys take no part. A tap that lands on a space ends the word, and ending
     *    the word is the correction moment itself, so a space is never something the user
     *    might have meant instead of a letter.
     *  - a set never spans two panels. A mis-tap cannot cross a swipe boundary, so this
     *    takes one panel's keys and knows nothing of the others.
     */
    fun neighbourTable(slots: List<Slot>, radius: Float = 22f): List<List<Int>> {
        val step = verticalStep(radius)
        val tolerance = step * 0.01f
        val centres = slots.map { centre(it.row, it.col, radius) }
        return slots.indices.map { index ->
            if (!slots[index].participates) return@map emptyList()
            val from = centres[index]
            slots.indices.filter { other ->
                other != index && slots[other].participates &&
                    kotlin.math.abs(distance(from, centres[other]) - step) <= tolerance
            }
        }
    }

    /** Straight-line distance between two board points, in dp. */
    fun distance(a: Point, b: Point): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    /**
     * The panel's keys as [Slot]s, with the space keys marked as taking no part.
     *
     * Kept beside [neighbourTable] because the exclusion is part of the model rather than
     * part of the config: `key-layout.json` says a key's kind is `space`, and what that
     * means for mis-tap distance is decided here.
     */
    fun slotsFor(panel: Panel): List<Slot> = panel.allKeys.map { key ->
        Slot(row = key.row, col = key.col, participates = key.kind != Key.KIND_SPACE)
    }

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
