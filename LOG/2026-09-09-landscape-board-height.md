# [HASH] — [landscape-board-height] the keyboard stopped taking the whole sideways screen, and turning the phone showed what that costs

This session ran across 2026-09-05 and 2026-09-09.

`solveRadius` took a width and a column count and consulted no height at all. On a Pixel 6 turned sideways the width is wide enough to reach `MAX_RADIUS`, and four rows at 34dp plus the row above them comes to about 394dp on a screen about 411dp tall — a screenful of keyboard with a sliver of text field above it. Nobody had seen that; it was arithmetic over `KeyGeometry.kt`, found while designing the split-board item, and filed apart from it because the split moves the halves sideways and does nothing about height, so on its own it would have delivered thumb-reachable halves on a board that had swallowed the screen.

The fix is a second constraint rather than a new rule: solve from the width as before, then reduce where necessary so the board plus the strip fits within half the available height, floored at `MIN_RADIUS`. Half is the share a phone keyboard conventionally takes rather than a figure invented here, and it is a proportion, which is how every other size in this file is derived. The width-only signature stays reachable for callers with no height to give.

**What it must not do is change the portrait board**, and the test asserts exactly that: in portrait the board comes to roughly 259dp against a screen around 915dp, well inside half, so the height must never bind — and `BoardHeightBoundTest` checks that portrait solves to precisely what the width-only solver returns, at four and five rows and ten and eleven columns. A fix that quietly shrank the keys everyone already uses would be a worse defect than the one it repairs.

The screen height is read through `LocalConfiguration` rather than from the layout constraints, because an input method's view wraps its own content vertically — so the constraint there is whatever the board asked for and could never bound it. The item flagged those field names as unread at source; the compiler settled it.

**Turned sideways on 2026-09-09 for the first time, and the bound holds.** The board plus strip fits inside half the screen. What it exposed is horizontal: the board is now narrower than the screen for the first time, and every key position is measured from its left edge, so the spare width collects on the right and the keys bunch into the left third while the strip above them runs edge to edge. The honest cost of the bound is also now visible — with the emoji panels present it takes the radius to about 13dp, barely above the 12dp floor.

Claude first framed the spare width as a choice between centring the board and splitting it. The user's answer was neither: however much of the neighbouring panels fits in that space should be showing. That is a better answer than either, and the mis-framed capture was deleted and refiled as [landscape-reveal-neighbours] in his terms.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`, `android/app/src/test/java/tech/flintcraft/hexboard/BoardHeightBoundTest.kt`.

**Routed to Captures:** [landscape-reveal-neighbours].
