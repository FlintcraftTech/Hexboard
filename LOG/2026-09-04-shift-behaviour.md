# 987cb5c — Shift became a three-state cycle, and letters now rest in lowercase

Written 2026-09-04 at 16:01.

Nothing had ever decided what the shift key does. [first-installable-build] left it to the service and that build chose the simplest thing giving capitals at all: one flag, uppercase the next character, clear. The prototype `hexboard17.html` had a three-state cycle nobody had adopted or refused.

Hexboard keeps the prototype's double-tap caps lock, settled by the user on 2026-09-02: one tap gives shift, a second inside the double-tap window gives caps lock, a further tap clears both.

The window is read from the phone rather than hard-coded. The prototype uses a literal 320 ms; SPEC's rule that every hold and repeat follows the phone's own settings does not literally reach a double tap, which is not a hold, but the same instinct does, and this project has twice preferred the system's value to a number of its own. `ViewConfiguration.getDoubleTapTimeout()` is what the build used, and its actual value remains unread — the item flagged that and it is still true.

**Where the unshifted letter comes from, settled on 2026-09-03 and the reason this item cleared.** Seeing the keyboard run showed every letter drawn as a capital and raised a question the item had been assuming an answer to: the config gives each key one `label`, already a capital, so on the face of it there is no lowercase form to switch to. The prototype settled it — it redraws each label uppercase when shift or caps is on and lowercase when neither is, and does the same for accent alternatives — so letters resting in lowercase is a decision the canonical reference had already made and nobody had written down. And no config change was needed to honour it: `output` is exactly the lowercase of `label` for every letter key. This build re-checked that key by key across both shipped layouts and it holds for all of them, and the code falls back to lowercasing the label anyway, so a further layout cannot break it silently.

The alternative was capitals always, with the shift state signalled by the shift key's lighting alone — what the code did and what several phone keyboards do. It lost because the prototype had already chosen otherwise and SPEC names that prototype as canonical, so keeping the old behaviour would have been a silent departure from the reference rather than a decision.

The shift state is visible, settled on 2026-09-02: the shift key lights while either state is on, differently for the two, and every letter label including accent alternatives draws uppercase. Before this, `KeyboardPanel` was never told the state at all, so pressing shift changed nothing on screen — a defect rather than an unfinished feature, and one that would have been true even had caps lock been refused.

The build put the cycle, the uppercasing and the one-shot clear on the enum as pure functions, so the service holds the state and the clock and decides nothing else itself, and so the behaviour can be checked without a running input method.

Refused: keeping one-shot shift alone, which leaves the prototype's caps lock unreplaced with nothing chosen in its place; the prototype's hard-coded 320 ms, a number of our own where the system publishes one; capitals always; and a second config field for the unshifted form, unnecessary since `output` already is that.

**Confirmed:** that `output` is the lowercase of `label` for every letter key in both shipped configs, re-checked key by key.

**Unconfirmed, transcribed from the tick:** `ShiftStateUiTest` is Android Studio's to run. `ViewConfiguration.getDoubleTapTimeout()` is used as the window and its value is still unread — the item flagged that, and the phone is where it shows.

**Files touched:** `HexboardImeService.kt` (`shifted` replaced by a ShiftState enum with `next()`/`applyTo()`/`afterInsert()`, a last-tap timestamp and `cycleShift()`; state passed to the board, ~50 lines), `KeyboardPanel.kt` (shiftState threaded through HexboardBoard, KeyboardPanel, KeyCircle and AccentRow; `Key.glyph()` and `KeyColors.litFor()` with two lit constants, ~50 lines), `ShiftStateUiTest.kt` (created, 5 tests, 152 lines).

**Routed to Captures:** none from this item.

**Depth:** short.

SPEC gained its matching sentence on 2026-09-03, in planning — it already said letters draw as capitals while a shift state is on and said nothing about the resting state, which is how the gap survived.
