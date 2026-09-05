# 987cb5c — One key size across a layout's three panels, narrower panels centred

Written 2026-09-04 at 15:58.

Settled with the user on 2026-09-02. Each panel used to solve its own radius from its own widest row, so a layout whose panels differ in width resized every key by about ten per cent on each swipe and moved every centre. Nothing was broken by that — taps routed, nodes landed, accent rows drew — it was a visual and motor change rather than a fault.

The argument that decided it came from the code's own existing choice: `HexboardBoard` already computed the board's height as the tallest panel's and used it for all three, so a swipe never resized the board vertically. Sizing the radius per panel answered the same question the other way, and this makes them consistent.

The trade was stated in the terms it was decided on: per-panel sizing costs a ten per cent resize on every swipe, shared sizing costs the narrower panels ten per cent of their key size permanently, on a keyboard whose headline is large keys. He chose shared.

A clarification worth keeping, because the same confusion will recur: this shares a radius only between the three panels *within* one layout. Nothing is shared across languages — SPEC already says a wider alphabet gets correspondingly smaller keys, so Russian's keys stay smaller than English's whatever happens here. His first answer was given on the reading that this was a cross-language rule and reversed once the scope was clear, which is recorded so a later reader does not take the reversal for indecision. Claude also described per-panel sizing as making the board "unstable" in the exchange before that, which overstated it; the honest description is the resize above, and that is what the decision was finally made on.

**A deliberate deviation from the item's file list.** The item named `KeyGeometry.kt` as read-only and put the centring in `KeyboardPanel.kt`. The centring offset went into `KeyGeometry.kt` instead, because that file's own standing rule is that every position, radius and label size in the app is computed there and nowhere else — putting a horizontal offset in the panel would have broken it. The rule won over the file list.

Refused: each panel sizing its own, which is the behaviour this replaced and contradicts the height decision already made; and spreading a narrower panel's keys to fill the width instead of centring, which breaks the hexagonal packing SPEC calls inviolable.

**Confirmed:** nothing.

**Unconfirmed, transcribed from the tick:** `SharedRadiusTest` is Android Studio's to run, and swiping between panels on the Pixel 6 is what shows the keys no longer resize.

**Files touched:** `KeyboardPanel.kt` (HexboardBoard solves one radius and hands it down; KeyboardPanel gained radius/widestCols parameters and centres a narrower panel; `stripHeight` now takes a radius, ~35 lines), `KeyGeometry.kt` (`centringIndent()`, 12 lines), `StripLayoutTest.kt` (follows the shared radius, 8 lines), `SharedRadiusTest.kt` (created, 4 tests, 103 lines).

**Routed to Captures:** none from this item.

**Depth:** short.

[rare-row2-unindent] shipped in this same run and removes the English instance of the mismatch, so what remains for this item is Russian, whose QWERTY genuinely is eleven columns against SYMBOLS' ten.
