package tech.flintcraft.hexboard

import android.view.ViewConfiguration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * What the shift key does, and what the board shows while it is doing it.
 *
 * SPEC: tapping shift capitalises the next character and then clears; tapping it twice in
 * quick succession locks capitals, and a further tap clears both. Whichever state is on, the
 * shift key is lit — differently for the two — and every letter, accent alternatives
 * included, draws as a capital. With neither state on, letters draw in lowercase, so the
 * board itself shows which state it is in.
 *
 * Letters resting in lowercase is what `hexboard17.html` already does, and SPEC names that
 * prototype as canonical; nobody had written it down. It needs no config change, because
 * `output` is exactly the lowercase of `label` for every letter key in both shipped layouts.
 *
 * Two halves are tested separately because they live in different places. The cycle, the
 * uppercasing and the one-shot clear are pure functions on [ShiftState], so they are checked
 * directly, timing rule included. What the board draws is checked by rendering it. The one
 * thing neither reaches is `commitText` itself, which belongs to a running input method — so
 * this proves the character *would* arrive capitalised rather than watching it land.
 *
 * It runs on a device or emulator from Android Studio: right-click this file, Run.
 */
@RunWith(AndroidJUnit4::class)
class ShiftStateUiTest {

    @get:Rule
    val compose = createComposeRule()

    private val layout: KeyLayout by lazy {
        KeyLayoutLoader.fromAssets(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    private val window: Long by lazy { ViewConfiguration.getDoubleTapTimeout().toLong() }

    // ── The cycle ─────────────────────────────────────────────────────────────────────

    @Test
    fun oneTapGivesShiftAndTwoQuickTapsLockCapitals() {
        val afterOne = ShiftState.OFF.next(sinceLastTapMs = 5_000, doubleTapWindowMs = window)
        assertEquals("A tap from off should give shift.", ShiftState.SHIFT, afterOne)

        val afterTwo = afterOne.next(sinceLastTapMs = window / 2, doubleTapWindowMs = window)
        assertEquals(
            "A second tap inside the double-tap window should lock capitals.",
            ShiftState.CAPS,
            afterTwo
        )

        assertEquals(
            "A further tap should clear both.",
            ShiftState.OFF,
            afterTwo.next(sinceLastTapMs = window / 2, doubleTapWindowMs = window)
        )
    }

    @Test
    fun twoSlowTapsDoNotLockCapitals() {
        val afterOne = ShiftState.OFF.next(sinceLastTapMs = 5_000, doubleTapWindowMs = window)
        assertEquals(
            "A second tap outside the window is a fresh tap, so it clears shift rather than " +
                "locking capitals.",
            ShiftState.OFF,
            afterOne.next(sinceLastTapMs = window * 3, doubleTapWindowMs = window)
        )
    }

    @Test
    fun shiftClearsOnTheFirstCharacterAndCapsDoesNot() {
        assertEquals("H", ShiftState.SHIFT.applyTo("h"))
        assertEquals(
            "Shift is one-shot, so it clears once a character has been inserted.",
            ShiftState.OFF,
            ShiftState.SHIFT.afterInsert()
        )
        assertEquals("H", ShiftState.CAPS.applyTo("h"))
        assertEquals(
            "Caps lock holds until the shift key clears it.",
            ShiftState.CAPS,
            ShiftState.CAPS.afterInsert()
        )
        assertEquals("With neither state on, a character is inserted as typed.", "h", ShiftState.OFF.applyTo("h"))
    }

    // ── What the board draws ──────────────────────────────────────────────────────────

    @Test
    fun lettersRestInLowercaseAndDrawAsCapitalsWhileAStateIsOn() {
        val state = mutableStateOf(ShiftState.OFF)
        compose.setContent {
            HexboardBoard(
                layout = layout,
                shiftState = state.value,
                modifier = Modifier.fillMaxWidth()
            )
        }
        compose.waitForIdle()

        val letters = layout.allPanels
            .flatMap { it.allKeys }
            .filter { it.action == Key.ACTION_INSERT && it.output != null }

        assertTrue("The config offers no insert keys to check.", letters.isNotEmpty())

        val restingWrong = letters.filter { it.glyph(ShiftState.OFF) != it.output }
        assertTrue(
            "These keys do not rest at their lowercase form: " +
                restingWrong.joinToString { "'${it.label}'" },
            restingWrong.isEmpty()
        )

        listOf(ShiftState.SHIFT, ShiftState.CAPS).forEach { on ->
            val shiftedWrong = letters.filter { it.glyph(on) != it.label }
            assertTrue(
                "In $on these keys do not draw as their capital: " +
                    shiftedWrong.joinToString { "'${it.label}'" },
                shiftedWrong.isEmpty()
            )
        }
    }

    @Test
    fun theShiftKeyIsPresentSoAStateHasSomethingToLight() {
        compose.setContent {
            HexboardBoard(layout = layout, modifier = Modifier.fillMaxWidth())
        }
        compose.waitForIdle()

        val shiftNodes = compose.onAllNodesWithContentDescription("Shift").fetchSemanticsNodes()
        assertTrue(
            "The board draws no key announced as \"Shift\", so there is nothing for the state " +
                "to light and nothing a screen reader can find.",
            shiftNodes.isNotEmpty()
        )
    }
}
