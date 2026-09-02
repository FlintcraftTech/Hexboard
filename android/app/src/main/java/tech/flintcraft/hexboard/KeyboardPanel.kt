package tech.flintcraft.hexboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
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
    onKey: (Key) -> Unit = {}
) {
    val panels = remember(layout) { layout.allPanels.sortedBy { it.index } }
    if (panels.isEmpty()) return
    val home = remember(panels) { panels.indexOfFirst { it.id == "qwerty" }.coerceAtLeast(0) }
    val pagerState = rememberPagerState(initialPage = home) { panels.size }

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val height = remember(panels, maxWidth) {
            panels.maxOf { panel ->
                val radius = KeyGeometry.solveRadius(maxWidth.value, panel.maxCol + 1)
                KeyGeometry.boardHeight(panel.rowCount, radius)
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(height.dp),
            verticalAlignment = Alignment.Top
        ) { page ->
            KeyboardPanel(panel = panels[page], modifier = Modifier.fillMaxWidth(), onKey = onKey)
        }
    }
}

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
    onKey: (Key) -> Unit = {}
) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val keys = panel.allKeys
        val radius = KeyGeometry.solveRadius(maxWidth.value, panel.maxCol + 1)
        val centres = remember(keys, radius) {
            keys.map { KeyGeometry.centre(it.row, it.col, radius) }
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
                .height(KeyGeometry.boardHeight(panel.rowCount, radius).dp)
                .pointerInput(keys, radius) {
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
                                        geometry = popupGeometry(centres[index], key.accents.size, radius, boardWidth)
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
                    radius = radius,
                    glow = glows[index].value,
                    onKey = onKey
                )
            }
            popup?.let { AccentRow(it, radius) }
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
    val cell = KeyGeometry.visibleRadius(radius) * 2f
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
private fun AccentRow(popup: AccentPopup, radius: Float) {
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
        popup.options.forEachIndexed { index, option ->
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
 * One key: the visible circle, its label, and its accessibility node.
 *
 * The circle is drawn smaller than the touch target it stands for — the inset is
 * [KeyGeometry.VISIBLE_INSET] — so the node's bounds are the circle a person sees while
 * the area that answers a tap is larger.
 *
 * `glow` is how lit the key is, 0 at rest and 1 while pressed; the fill and border are
 * blended toward a lighter shade by that amount.
 */
@Composable
private fun KeyCircle(
    key: Key,
    centre: KeyGeometry.Point,
    radius: Float,
    glow: Float,
    onKey: (Key) -> Unit
) {
    val visibleRadius = KeyGeometry.visibleRadius(radius)
    val colors = colorsFor(key.kind)
    val description = accessibilityLabel(key)
    val fill = lerp(colors.fill, colors.lit, glow)
    val border = lerp(colors.border, colors.lit, glow)

    Box(
        modifier = Modifier
            .offset(x = (centre.x - visibleRadius).dp, y = (centre.y - visibleRadius).dp)
            .size((visibleRadius * 2f).dp)
            .clip(CircleShape)
            .background(fill)
            .border(1.5.dp, border, CircleShape)
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
            text = key.label,
            color = colors.text,
            fontWeight = if (key.large) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = with(LocalDensity.current) {
                KeyGeometry.labelSize(radius, key.large, key.label).dp.toSp()
            }
        )
    }
}

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

/** Fill, border, label colour for a key, and the lighter shade it lightens to when pressed. */
private data class KeyColors(val fill: Color, val border: Color, val text: Color) {
    val lit: Color get() = lerp(fill, Color.White, 0.35f)
}

/**
 * The single dark theme SPEC calls enough for v0, carried over from the layout preview so
 * a key looks the same on screen as it does in planning.
 */
private fun colorsFor(kind: String): KeyColors = when (kind) {
    Key.KIND_SPECIAL -> KeyColors(Color(0xFF16182A), Color(0xFF2C3358), Color(0xFF79A0FF))
    Key.KIND_SPACE -> KeyColors(Color(0xFF06311D), Color(0xFF0A6D44), Color(0xFF38D286))
    Key.KIND_PUNCTUATION -> KeyColors(Color(0xFF252530), Color(0xFF404055), Color(0xFFC7C7E7))
    Key.KIND_SYMBOL, Key.KIND_RARE ->
        KeyColors(Color(0xFF1B1D28), Color(0xFF2C2F44), Color(0xFFA0A6D3))
    else -> KeyColors(Color(0xFF2A2A38), Color(0xFF484858), Color(0xFFE2E2F2))
}
