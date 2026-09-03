# 37384e4 — Shift given the prototype's three-state cycle, and the invisible-state defect separated from it

Nothing had ever decided what shift does. The build of [first-installable-build] chose the simplest thing that gives capitals at all — a flag that uppercases the next character and clears — because the item left shift to the service and no document said otherwise.

The user chose to keep the prototype's arrangement: one tap gives shift, a second inside the double-tap window gives caps lock, a third clears both. `hexboard17.html` carries it as a `caps` state with a 320 ms window.

The window is read from the phone rather than copied. SPEC's rule that every hold and repeat follows the phone's own settings does not literally reach a double tap, which is not a hold, but the same instinct does and this project has twice preferred the system's value to one of its own. `ViewConfiguration.getDoubleTapTimeout()` is believed to be that value at 300 ms and is written into the item as a premise to confirm at the start of the build rather than as a checked fact, because nobody read it.

Separated from the caps-lock question, and worth separating because it would have been true even had caps lock been refused: `KeyboardPanel` is never told about the shift state at all, so pressing shift changes nothing on screen — the key does not light and the labels stay lowercase. That is a defect rather than an unfinished feature. The user chose the prototype's treatment: the key lit, differently for shift and caps, and every letter label including accent alternatives drawn as a capital while either is on.

**Queue changes:** moved from Unprocessed into Processed below the readiness line with `Blocked by: [install-and-enable-on-pixel]`, `KeyboardPanel.kt` having taken four uncompiled changes that day. SPEC's layout details gained the cycle and the visible state.

**Work processed:** kept — [shift-behaviour].
