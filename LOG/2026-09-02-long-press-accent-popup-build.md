# PLACEHOLDER — [long-press-accent-popup] built: holding a key with alternatives shows them in a row above it, slide to choose, release to type

Closed 2026-09-02 16:18. Fourth item of the run, third of the four `KeyboardPanel.kt` items, reusing the hold detection [backspace-key-repeat] introduced.

The gesture is the prototype's, which SPEC names canonical: on a hold, the key's alternatives appear in a row above it with the first highlighted, sliding sideways moves the highlight, releasing types it, and releasing without sliding types the first — kept over Gboard's release-gives-the-base at planning because it saves a slide for the commonest case. Timing is the phone's touch-and-hold delay, per the SPEC sentence added that day, replacing the prototype's fixed 320 ms.

The row is drawn by the board, which already owns press state, and positioned as the prototype's `openLP` does: centred over the key, clamped inside the board's edges. Selection follows the pointer's horizontal position only, clamped to the row's ends, so a thumb that drifts past the row keeps the last option rather than losing it. The chosen accent is committed as a synthetic insert key through the ordinary callback, so the service treats it like any character (shift included). A cancelled pointer closes the row and types nothing.

One limit found while writing it, not decided: on the top row the board has nothing above the key, so the row clamps to the top edge and overlaps the neighbouring keys. The prototype had a bar above its keys to clamp into. Filed as [accent-row-top-row]; the first run on the phone will show how bad it looks.

Tick: done, UNCONFIRMED: needs the app run on the Pixel 6 (hold e, slide, release; release without sliding types è). Known limit written in code: on the top row the popup clamps to the board's top edge and overlaps neighbouring keys, since the board has no room above row 0.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

**Routed to Captures:** [accent-row-top-row], filed by the post-run rescan.
