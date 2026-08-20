package tech.flintcraft.hexboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * One panel of keys, drawn from the config on the real zag geometry.
 *
 * Two things here are SPEC requirements rather than implementation choices, and they are
 * deliberately separate mechanisms:
 *
 *  - **Touch routing is nearest-centre.** A tap anywhere on the board goes to the key
 *    whose centre is closest, not to whichever circle happens to contain it. That is what
 *    makes the touch target larger than the drawn circle and leaves no gaps between keys.
 *    The tap handler therefore sits on the board, not on the individual keys.
 *  - **Every key gets its own accessibility node**, with its own label and bounds, because
 *    accessibility services largely bypass raw-touch routing. Each key's semantics carries
 *    an `onClick` action so TalkBack and switch access can activate it directly.
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(KeyGeometry.boardHeight(panel.rowCount, radius).dp)
                .pointerInput(keys, radius) {
                    detectTapGestures { offset ->
                        val hit = KeyGeometry.nearestCentre(
                            centres,
                            offset.x / density,
                            offset.y / density
                        )
                        if (hit >= 0) onKey(keys[hit])
                    }
                }
        ) {
            keys.forEachIndexed { index, key ->
                KeyCircle(key = key, centre = centres[index], radius = radius, onKey = onKey)
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
 */
@Composable
private fun KeyCircle(
    key: Key,
    centre: KeyGeometry.Point,
    radius: Float,
    onKey: (Key) -> Unit
) {
    val visibleRadius = KeyGeometry.visibleRadius(radius)
    val colors = colorsFor(key.kind)
    val description = accessibilityLabel(key)

    Box(
        modifier = Modifier
            .offset(x = (centre.x - visibleRadius).dp, y = (centre.y - visibleRadius).dp)
            .size((visibleRadius * 2f).dp)
            .clip(CircleShape)
            .background(colors.fill)
            .border(1.5.dp, colors.border, CircleShape)
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
private fun accessibilityLabel(key: Key): String = when {
    key.kind == Key.KIND_SPACE -> "Space"
    key.action == Key.ACTION_BACKSPACE -> "Backspace"
    key.action == Key.ACTION_ENTER -> "Enter"
    key.action == Key.ACTION_SHIFT -> "Shift"
    key.action == Key.ACTION_CURSOR_LEFT -> "Cursor left"
    key.action == Key.ACTION_CURSOR_RIGHT -> "Cursor right"
    else -> key.label
}

/** Fill, border and label colour for a key, by the kind the config gives it. */
private data class KeyColors(val fill: Color, val border: Color, val text: Color)

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
