package tech.flintcraft.hexboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.view.ViewConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * The whole keyboard surface: the layout's three letter panels in a horizontal pager,
 * opening on QWERTY, which SPEC names home.
 *
 * Panel switching wraps the whole board rather than any key, because nearest-centre
 * routing means a tap belongs to the board, not to a circle. Which panel is showing is
 * state held here, at the surface, and the pager gives the drag-follows-finger feel the
 * prototype has. The surface is sized to the tallest panel so the board does not jump in
 * height as pages change.
 */
@Composable
fun HexboardBoard(
    layout: KeyLayout,
    modifier: Modifier = Modifier,
    shiftState: ShiftState = ShiftState.OFF,
    emojiPanels: List<List<EmojiCatalogue.EmojiKey>> = emptyList(),
    onKey: (Key) -> Unit = {},
    onEmoji: (String) -> Unit = {}
) {
    val panels = remember(layout) { layout.allPanels.sortedBy { it.index } }
    if (panels.isEmpty()) return
    val home = remember(panels) { panels.indexOfFirst { it.id == "qwerty" }.coerceAtLeast(0) }
    val pagerState = rememberPagerState(initialPage = home) { panels.size }
    // SPEC's five emoji panels sit below the letters, reached by swiping down. The prototype's
    // arrangement is a vertical track containing the horizontal one, so the vertical pager
    // wraps rather than replaces the panel pager.
    val verticalState = rememberPagerState(initialPage = 0) { if (emojiPanels.isEmpty()) 1 else 2 }
    val emojiState = rememberPagerState(initialPage = EmojiCatalogue.HOME) {
        emojiPanels.size.coerceAtLeast(1)
    }

    BoxWithConstraints(modifier.fillMaxWidth()) {
        // One radius for the whole layout, solved from its widest panel. Sizing each panel
        // from its own widest row resized every key by about ten per cent on each swipe and
        // moved every centre; the board's height was already the tallest panel's for exactly
        // that reason, and this answers the same question the same way. The cost, chosen
        // knowingly: a narrower panel's keys are permanently the widest panel's size rather
        // than briefly larger.
        //
        // Nothing is shared across layouts. A wider alphabet still gets correspondingly
        // smaller keys, per SPEC — this shares a radius only between the three panels within
        // one layout.
        val widestCols = remember(panels) { panels.maxOf { it.maxCol + 1 } }
        // The tallest thing the board has to hold, in rows. The emoji panels are five rows
        // where a letter panel is four, so they set the height once they exist.
        val tallestRows = remember(panels, emojiPanels.isEmpty()) {
            val letters = panels.maxOf { it.rowCount }
            if (emojiPanels.isEmpty()) letters else maxOf(letters, EmojiCatalogue.ROWS)
        }
        // The screen's height rather than this view's: an input method's view wraps its own
        // content vertically, so the constraint here would be whatever the board asked for
        // and could never bound it.
        val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()
        val radius = remember(widestCols, maxWidth, screenHeight, tallestRows) {
            KeyGeometry.solveRadius(
                widthDp = maxWidth.value,
                cols = widestCols,
                availableHeightDp = screenHeight,
                rowCount = tallestRows,
                stripDp = ::stripHeight
            )
        }
        // The tallest thing the vertical track has to hold. The emoji panels are five rows
        // where a letter panel is four, so once they exist they set the height — the board
        // must not change height on a downward swipe any more than on a sideways one.
        val height = remember(panels, radius, emojiPanels.isEmpty()) {
            val letters = panels.maxOf { KeyGeometry.boardHeight(it.rowCount, radius) }
            if (emojiPanels.isEmpty()) letters
            else maxOf(letters, KeyGeometry.boardHeight(EmojiCatalogue.ROWS, radius))
        }
        val stripHeight = remember(radius) { stripHeight(radius) }

        Column(Modifier.fillMaxWidth()) {
            ControlStrip(height = stripHeight)
            VerticalPager(
                state = verticalState,
                modifier = Modifier.fillMaxWidth().height(height.dp)
            ) { board ->
                if (board == 0) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth().height(height.dp),
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        KeyboardPanel(
                            panel = panels[page],
                            radius = radius,
                            widestCols = widestCols,
                            shiftState = shiftState,
                            modifier = Modifier.fillMaxWidth(),
                            onKey = onKey
                        )
                    }
                } else {
                    HorizontalPager(
                        state = emojiState,
                        modifier = Modifier.fillMaxWidth().height(height.dp),
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        EmojiPanel(
                            keys = emojiPanels[page],
                            radius = radius,
                            modifier = Modifier.fillMaxWidth(),
                            onEmoji = onEmoji
                        )
                    }
                }
            }
        }
    }
}

/**
 * One emoji panel, drawn on the same zag geometry as the letters.
 *
 * The panels are Hexboard's own and the content is Unicode's, which is the division SPEC
 * already applies to layouts. So the circles, the zag and the nearest-centre routing are the
 * same here as on a letter panel — an emoji is committed on release exactly as a key is.
 *
 * The two things a letter panel has that this does not are accents and repeat: an emoji has
 * no alternatives to hold for and nothing to repeat, so the pointer loop here is the plain
 * press-and-release case.
 */
@Composable
private fun EmojiPanel(
    keys: List<EmojiCatalogue.EmojiKey>,
    radius: Float,
    modifier: Modifier = Modifier,
    onEmoji: (String) -> Unit = {}
) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val centres = remember(keys, radius) {
            keys.map { KeyGeometry.centre(it.row, it.col, radius) }
        }
        val density = LocalDensity.current.density
        val scope = rememberCoroutineScope()
        val glows = remember(keys) { keys.map { Animatable(0f) } }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(KeyGeometry.boardHeight(EmojiCatalogue.ROWS, radius).dp)
                .pointerInput(keys, radius) {
                    trackPresses(
                        resolve = { x, y -> KeyGeometry.nearestCentre(centres, x / density, y / density) },
                        onDown = { index -> scope.launch { glows[index].snapTo(1f) } },
                        onMove = { _, _, _ -> false },
                        onUp = { index ->
                            onEmoji(keys[index].characters)
                            scope.fadeBack(glows[index])
                        },
                        onCancel = { index -> scope.fadeBack(glows[index]) }
                    )
                }
        ) {
            keys.forEachIndexed { index, emoji ->
                EmojiCircle(
                    emoji = emoji,
                    centre = centres[index],
                    radius = radius,
                    glow = glows[index].value,
                    onEmoji = onEmoji
                )
            }
        }
    }
}

/** One emoji in its slot: the same soft-edged circle a key gets, with the glyph on top. */
@Composable
private fun EmojiCircle(
    emoji: EmojiCatalogue.EmojiKey,
    centre: KeyGeometry.Point,
    radius: Float,
    glow: Float,
    onEmoji: (String) -> Unit
) {
    val fadeRadius = KeyGeometry.fadeRadius(radius)
    val solidStop = KeyGeometry.SOLID_FRACTION / KeyGeometry.FADE_FRACTION
    val colors = colorsFor(Key.KIND_SYMBOL)
    val resting = lerp(colors.fill, colors.lit, KeyGeometry.rowTint(emoji.row))
    val fill = lerp(resting, colors.lit, glow)

    Box(
        modifier = Modifier
            .offset(x = (centre.x - fadeRadius).dp, y = (centre.y - fadeRadius).dp)
            .size((fadeRadius * 2f).dp)
            .background(
                Brush.radialGradient(
                    solidStop to fill,
                    1f to fill.copy(alpha = 0f)
                )
            )
            .semantics(mergeDescendants = true) {
                contentDescription = emoji.characters
                role = Role.Button
                onClick(label = "press") {
                    onEmoji(emoji.characters)
                    true
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji.characters,
            color = colors.text,
            fontSize = with(LocalDensity.current) {
                KeyGeometry.labelSize(radius, false, emoji.characters).dp.toSp()
            }
        )
    }
}

/**
 * The row above the keys, where the keyboard's own controls go.
 *
 * It is empty today. Three features each add a control of their own later: the microphone
 * at its right-hand end, the clipboard button at its left, and predictive text's candidates
 * filling the middle. Until one of them lands the board carries a visibly empty band, which
 * was weighed and accepted — obviously unfinished rather than wrong.
 *
 * A control here may be drawn larger than a key and stand proud of the band. An input
 * method's window is sized to its view and Compose clips to bounds, so nothing can be
 * painted outside the keyboard's own rectangle: whichever feature introduces such a control
 * reserves its overspill as real height on top of the band. With the row empty nothing
 * stands proud, so the reserved height is the band alone.
 */
@Composable
private fun ControlStrip(height: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .testTag(STRIP_TAG)
            .fillMaxWidth()
            .height(height.dp)
            .background(STRIP_BACKGROUND)
    )
}

/** How the tests find the strip; it has no label of its own, being empty. */
internal const val STRIP_TAG = "hexboard-control-strip"

/** Enough of a shade against the board to read as a band, and no divider line. */
private val STRIP_BACKGROUND = Color(0xFF121422)

/**
 * How tall the strip's drawn band is, in dp: one row's vertical pitch of the layout's own
 * solved radius, floored at Android's 48dp minimum touch target.
 *
 * Derived rather than fixed, so it stays in proportion on an eleven-wide layout and at any
 * screen width without a second rule, and so a control in it can never end up a different
 * size from the keys beneath it. A fixed dp value was the alternative and lost on exactly
 * that.
 */
internal fun stripHeight(radius: Float): Float =
    max(MIN_STRIP_HEIGHT, KeyGeometry.verticalStep(radius))

/** Android's minimum touch target, which the band never falls below. */
internal const val MIN_STRIP_HEIGHT = 48f

/**
 * One panel of keys, drawn from the config on the real zag geometry.
 *
 * Two things here are SPEC requirements rather than implementation choices, and they are
 * deliberately separate mechanisms:
 *
 *  - **Touch routing is nearest-centre.** A tap anywhere on the board goes to the key
 *    whose centre is closest, not to whichever circle happens to contain it. That is what
 *    makes the touch target larger than the drawn circle and leaves no gaps between keys.
 *    The pointer handler therefore sits on the board, not on the individual keys, and so
 *    does every piece of per-key press state: which keys are down, and how lit each is.
 *  - **Every key gets its own accessibility node**, with its own label and bounds, because
 *    accessibility services largely bypass raw-touch routing. Each key's semantics carries
 *    an `onClick` action so TalkBack and switch access can activate it directly.
 *
 * Press feedback is the key's own highlight and nothing else (SPEC): the resolved key
 * lightens the instant a finger lands, holds while it is down, and fades back once it
 * lifts. Nothing marks where the finger actually landed. Each finger is tracked on its
 * own, so several keys can be fading at once.
 *
 * A key commits on release, except the repeating keys — backspace and the cursor keys —
 * which act on the press and then keep acting while held (see [repeatWhileHeld]); a
 * repeating key stays lit for as long as it repeats. A held key with accents opens its
 * row of alternatives after the same delay (see [AccentPopup]); a key never both repeats
 * and pops up, since the two are keyed off disjoint things (action, and the accents list
 * the config gives only to insert keys).
 *
 * No position is computed here. Everything comes from [KeyGeometry].
 */
@Composable
fun KeyboardPanel(
    panel: Panel,
    modifier: Modifier = Modifier,
    radius: Float? = null,
    widestCols: Int? = null,
    shiftState: ShiftState = ShiftState.OFF,
    onKey: (Key) -> Unit = {}
) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val keys = panel.allKeys
        // The board hands down one radius for the whole layout, and the widest panel's column
        // count so a narrower panel knows how far to shift to sit centred. Standing alone —
        // a preview, or a test driving one panel — the panel solves its own.
        val solved = radius ?: KeyGeometry.solveRadius(maxWidth.value, panel.maxCol + 1)
        val widest = widestCols ?: (panel.maxCol + 1)
        val indent = KeyGeometry.centringIndent(solved, panel.maxCol + 1, widest)
        val centres = remember(keys, solved, indent) {
            keys.map {
                val centre = KeyGeometry.centre(it.row, it.col, solved)
                centre.copy(x = centre.x + indent)
            }
        }
        val density = LocalDensity.current.density
        val scope = rememberCoroutineScope()
        val glows = remember(keys) { keys.map { Animatable(0f) } }
        // The hold timer running for each pressed key, if its key has one.
        val holds = remember(keys) { arrayOfNulls<Job>(keys.size) }
        val boardWidth = maxWidth.value
        var popup by remember(keys) { mutableStateOf<AccentPopup?>(null) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(KeyGeometry.boardHeight(panel.rowCount, solved).dp)
                .pointerInput(keys, solved) {
                    trackPresses(
                        resolve = { x, y -> KeyGeometry.nearestCentre(centres, x / density, y / density) },
                        onDown = { index ->
                            val key = keys[index]
                            scope.launch { glows[index].snapTo(1f) }
                            when {
                                key.repeats -> {
                                    onKey(key)
                                    holds[index] = scope.launch { repeatWhileHeld { onKey(key) } }
                                }
                                key.accents.isNotEmpty() -> holds[index] = scope.launch {
                                    delay(ViewConfiguration.getLongPressTimeout().toLong())
                                    popup = AccentPopup(
                                        keyIndex = index,
                                        options = key.accents,
                                        selected = 0,
                                        geometry = popupGeometry(centres[index], key.accents.size, solved, boardWidth)
                                    )
                                }
                            }
                        },
                        onMove = { index, x, _ ->
                            val open = popup
                            if (open != null && open.keyIndex == index) {
                                popup = open.copy(selected = open.geometry.selectionAt(x / density, open.options.size))
                                true // the slide is ours; the pager must not read it as a swipe
                            } else {
                                false
                            }
                        },
                        onUp = { index ->
                            holds[index]?.cancel()
                            holds[index] = null
                            val open = popup
                            when {
                                open != null && open.keyIndex == index -> {
                                    popup = null
                                    onKey(keys[index].asAccent(open.options[open.selected]))
                                }
                                !keys[index].repeats -> onKey(keys[index])
                            }
                            scope.fadeBack(glows[index])
                        },
                        onCancel = { index ->
                            holds[index]?.cancel()
                            holds[index] = null
                            if (popup?.keyIndex == index) popup = null
                            scope.fadeBack(glows[index])
                        }
                    )
                }
        ) {
            keys.forEachIndexed { index, key ->
                KeyCircle(
                    key = key,
                    centre = centres[index],
                    radius = solved,
                    glow = glows[index].value,
                    shiftState = shiftState,
                    onKey = onKey
                )
            }
            popup?.let { AccentRow(it, solved, shiftState) }
        }
    }
}

/** How long a key stays fully lit after release before the fade starts, and the fade. */
private const val HOLD_MS = 40
private const val FADE_MS = 150

private fun CoroutineScope.fadeBack(glow: Animatable<Float, *>) {
    launch { glow.animateTo(0f, tween(durationMillis = FADE_MS, delayMillis = HOLD_MS)) }
}

/**
 * Which keys repeat while held: backspace and the two cursor keys, and nothing else.
 *
 * Keyed off the key's action rather than a config field, because repeating is what an
 * action does, not which keys exist. A held letter is its accent menu, so letters never
 * repeat; enter, shift and space do not either.
 */
private val Key.repeats: Boolean
    get() = action == Key.ACTION_BACKSPACE ||
        action == Key.ACTION_CURSOR_LEFT ||
        action == Key.ACTION_CURSOR_RIGHT

/**
 * Re-fires an action for as long as the coroutine lives, at the phone's own timing.
 *
 * SPEC: every hold on the keyboard waits the phone's touch-and-hold delay and repeats at
 * the phone's own rate. [ViewConfiguration.getLongPressTimeout] is the value Android's
 * accessibility "Touch & hold delay" setting changes, and [ViewConfiguration.getKeyRepeatDelay]
 * is the system's own repeat interval — both read from the platform source on 2026-09-02.
 * They are read at press time, so a settings change applies to the next press with no
 * restart. The first fire happened on the press itself; this supplies the rest.
 */
private suspend fun repeatWhileHeld(fire: () -> Unit) {
    delay(ViewConfiguration.getLongPressTimeout().toLong())
    while (true) {
        fire()
        delay(ViewConfiguration.getKeyRepeatDelay().toLong())
    }
}

/**
 * The board's pointer loop. Every finger is followed from down to up independently, and
 * each is resolved to a key index at the moment it lands; it stays bound to that key
 * however far it slides, which is what lets a slide along the accent row select.
 *
 * Nothing here consumes the pointer-down, which is what lets a pager wrapped around the
 * board still see a swipe (the consumed-down hazard `detectTapGestures` had). Movement is
 * consumed only when `onMove` claims it — a slide along the accent row — so the pager
 * does not read that slide as a swipe. A finger whose events another handler has
 * consumed — a panel swipe taking over — is cancelled: its key fades with nothing
 * committed, so a swipe never leaves a key lit, a key repeating or a popup open.
 */
private suspend fun androidx.compose.ui.input.pointer.PointerInputScope.trackPresses(
    resolve: (x: Float, y: Float) -> Int,
    onDown: (index: Int) -> Unit,
    onMove: (index: Int, x: Float, y: Float) -> Boolean,
    onUp: (index: Int) -> Unit,
    onCancel: (index: Int) -> Unit
) {
    awaitPointerEventScope {
        val pressed = mutableMapOf<PointerId, Int>()
        while (true) {
            val event = awaitPointerEvent()
            for (change in event.changes) {
                val held = pressed[change.id]
                when {
                    change.changedToDownIgnoreConsumed() -> {
                        val hit = resolve(change.position.x, change.position.y)
                        if (hit >= 0) {
                            pressed[change.id] = hit
                            onDown(hit)
                        }
                    }
                    held != null && change.isConsumed -> {
                        pressed.remove(change.id)
                        onCancel(held)
                    }
                    held != null && change.changedToUpIgnoreConsumed() -> {
                        pressed.remove(change.id)
                        onUp(held)
                    }
                    held != null -> {
                        if (onMove(held, change.position.x, change.position.y)) change.consume()
                    }
                }
            }
        }
    }
}

// ── Long-press accent popup ───────────────────────────────────────────────────────────

/**
 * The row of alternatives shown above a held key, as the prototype draws it: the key's
 * accents in a row, the first highlighted, the base character not among them (a tap gives
 * that). Sliding sideways moves the highlight; releasing types it; releasing without
 * sliding types the first.
 */
private data class AccentPopup(
    val keyIndex: Int,
    val options: List<String>,
    val selected: Int,
    val geometry: PopupGeometry
)

/** Where the row sits on the board, in dp. Internal so the key audit test can aim at a cell. */
internal data class PopupGeometry(val left: Float, val top: Float, val cell: Float, val height: Float) {
    /** Which cell a horizontal position picks — clamped to the ends, so a thumb that drifts past the row keeps the last option. */
    fun selectionAt(x: Float, count: Int): Int =
        ((x - left) / cell).toInt().coerceIn(0, count - 1)
}

/** Gap between the held key's touch circle and the row, and padding inside the row. */
private const val POPUP_GAP = 8f
private const val POPUP_PAD = 4f

/**
 * Places the row centred over the held key and clamps it inside the board's edges, as the
 * prototype's `openLP` does. On the top row it is clamped to the board's top edge, so it
 * overlaps the keys above the held key rather than leaving the board.
 */
internal fun popupGeometry(
    centre: KeyGeometry.Point,
    count: Int,
    radius: Float,
    boardWidth: Float
): PopupGeometry {
    // A cell is one key's drawn width, which is now the key's whole touch extent.
    val cell = KeyGeometry.fadeRadius(radius) * 2f
    val width = count * cell + POPUP_PAD * 2f
    val height = cell + POPUP_PAD * 2f
    val left = (centre.x - width / 2f).coerceIn(KeyGeometry.EDGE, (boardWidth - width - KeyGeometry.EDGE).coerceAtLeast(KeyGeometry.EDGE))
    val top = (centre.y - radius - POPUP_GAP - height).coerceAtLeast(0f)
    return PopupGeometry(left = left + POPUP_PAD, top = top + POPUP_PAD, cell = cell, height = height)
}

/** The accent character as a key the board can commit through the ordinary callback. */
private fun Key.asAccent(character: String): Key =
    copy(label = character, output = character, action = Key.ACTION_INSERT)

@Composable
private fun AccentRow(popup: AccentPopup, radius: Float, shiftState: ShiftState) {
    val g = popup.geometry
    val width = popup.options.size * g.cell + POPUP_PAD * 2f
    Box(
        modifier = Modifier
            .offset(x = (g.left - POPUP_PAD).dp, y = (g.top - POPUP_PAD).dp)
            .size(width.dp, g.height.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1F2030))
            .border(1.5.dp, Color(0xFF3A3D5C), RoundedCornerShape(12.dp))
    ) {
        popup.options.forEachIndexed { index, rawOption ->
            // SPEC: every letter label draws as a capital while a shift state is on, accent
            // alternatives included.
            val option = if (shiftState.isOn) rawOption.uppercase() else rawOption
            val chosen = index == popup.selected
            Box(
                modifier = Modifier
                    .offset(x = (POPUP_PAD + index * g.cell).dp, y = POPUP_PAD.dp)
                    .size(g.cell.dp)
                    .clip(CircleShape)
                    .background(if (chosen) Color(0xFF79A0FF) else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (chosen) Color(0xFF0D0D12) else Color(0xFFE2E2F2),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = with(LocalDensity.current) {
                        KeyGeometry.labelSize(radius, false, option).dp.toSp()
                    }
                )
            }
        }
    }
}

/**
 * One key: the soft-edged circle, its label, and its accessibility node.
 *
 * SPEC: a key is drawn as a soft-edged circle with no border — solid through the middle,
 * fading to nothing at the edge of the area it accepts. So the fill is a radial gradient
 * from the key's colour out to fully transparent, and there is no border: a border in a
 * lighter colour than the fill was the hardest edge on the key, drawn exactly where the
 * fill stopped, and it would reinstate the boundary the fade exists to dissolve.
 *
 * The key is now drawn as wide as it is tappable rather than sitting well inside it. The
 * fade is what keeps a board of large keys from looking crowded, so nothing new lands in
 * the gaps even though the drawn extent grew.
 *
 * `glow` is how lit the key is, 0 at rest and 1 while pressed. It supplies the gradient's
 * centre colour rather than a flat fill, so a pressed key lightens across its whole extent
 * — the key's own highlight being the only press feedback there is (SPEC).
 */
@Composable
private fun KeyCircle(
    key: Key,
    centre: KeyGeometry.Point,
    radius: Float,
    glow: Float,
    shiftState: ShiftState,
    onKey: (Key) -> Unit
) {
    val fadeRadius = KeyGeometry.fadeRadius(radius)
    val solidStop = KeyGeometry.SOLID_FRACTION / KeyGeometry.FADE_FRACTION
    val glyph = key.glyph(shiftState)
    val colors = colorsFor(key.kind).litFor(key, shiftState)
    val description = accessibilityLabel(key)
    // The key rests at its row's tint and travels the whole way to `lit` when pressed, so a
    // pressed key stays well clear of any resting colour on the board.
    val resting = lerp(colors.fill, colors.lit, KeyGeometry.rowTint(key.row))
    val fill = lerp(resting, colors.lit, glow)

    Box(
        modifier = Modifier
            .offset(x = (centre.x - fadeRadius).dp, y = (centre.y - fadeRadius).dp)
            .size((fadeRadius * 2f).dp)
            .background(
                Brush.radialGradient(
                    solidStop to fill,
                    1f to fill.copy(alpha = 0f)
                )
            )
            .semantics(mergeDescendants = true) {
                contentDescription = description
                role = Role.Button
                onClick(label = "press") {
                    onKey(key)
                    true
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = glyph,
            color = colors.text,
            fontWeight = if (key.large) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = with(LocalDensity.current) {
                KeyGeometry.labelSize(radius, key.large, glyph).dp.toSp()
            }
        )
    }
}

/**
 * Which glyph a key draws, given the shift state.
 *
 * SPEC: with neither shift nor caps on, letters draw in lowercase, so the board itself shows
 * which state it is in. The config gives each letter key a capital `label` and its lowercase
 * in `output` — checked key by key across both shipped layouts — so no new config field is
 * needed and no layout file changes. A further layout could break that pairing, so the
 * fallback is to lowercase the label rather than to trust it.
 *
 * Only insert keys are affected. The keys that act rather than type keep their own glyph,
 * which is a symbol rather than a letter with a case.
 */
internal fun Key.glyph(shiftState: ShiftState): String = when {
    action != Key.ACTION_INSERT -> label
    shiftState.isOn -> label
    else -> output ?: label.lowercase()
}

/**
 * The shift key lights while either state is on, and differently for the two.
 *
 * Without this, pressing shift changed nothing on screen at all — the panel was never told
 * the state — which is a defect rather than an unfinished feature, and would have been one
 * even had caps lock been refused. Caps lock is lit harder than shift, so the two read apart
 * at a glance: a one-shot state that is about to clear should look less settled than a lock.
 */
private fun KeyColors.litFor(key: Key, shiftState: ShiftState): KeyColors = when {
    key.action != Key.ACTION_SHIFT -> this
    shiftState == ShiftState.SHIFT -> copy(fill = lerp(fill, lit, SHIFT_LIT), text = lit)
    shiftState == ShiftState.CAPS -> copy(fill = lerp(fill, lit, CAPS_LIT), text = lit)
    else -> this
}

/** How far the shift key's fill travels toward `lit` in each state. */
private const val SHIFT_LIT = 0.45f
private const val CAPS_LIT = 0.85f

/**
 * What a screen reader announces for a key.
 *
 * A letter or punctuation key announces its own glyph, which is what a reader expects.
 * The keys that act rather than type have no useful glyph, so they announce their action.
 */
internal fun accessibilityLabel(key: Key): String = when {
    key.kind == Key.KIND_SPACE -> "Space"
    key.action == Key.ACTION_BACKSPACE -> "Backspace"
    key.action == Key.ACTION_ENTER -> "Enter"
    key.action == Key.ACTION_SHIFT -> "Shift"
    key.action == Key.ACTION_CURSOR_LEFT -> "Cursor left"
    key.action == Key.ACTION_CURSOR_RIGHT -> "Cursor right"
    else -> key.label
}

/** Fill and label colour for a key, and the lighter shade it lightens to when pressed. */
private data class KeyColors(val fill: Color, val text: Color) {
    val lit: Color get() = lerp(fill, Color.White, 0.35f)
}

/**
 * The single dark theme SPEC calls enough for v0, carried over from the layout preview so
 * a key looks the same on screen as it does in planning.
 */
private fun colorsFor(kind: String): KeyColors = when (kind) {
    Key.KIND_SPECIAL -> KeyColors(Color(0xFF16182A), Color(0xFF79A0FF))
    Key.KIND_SPACE -> KeyColors(Color(0xFF06311D), Color(0xFF38D286))
    Key.KIND_PUNCTUATION -> KeyColors(Color(0xFF252530), Color(0xFFC7C7E7))
    Key.KIND_SYMBOL, Key.KIND_RARE -> KeyColors(Color(0xFF1B1D28), Color(0xFFA0A6D3))
    else -> KeyColors(Color(0xFF2A2A38), Color(0xFFE2E2F2))
}
