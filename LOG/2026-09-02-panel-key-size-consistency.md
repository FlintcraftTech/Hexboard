# [HASH] — All three panels of a layout made to share one key size, after a misread scope produced an answer the user reversed

On the Russian layout QWERTY divides the board's width by eleven while RARE and SYMBOLS divide it by ten, so swiping between panels makes every key about ten per cent larger or smaller and moves every centre. It appears only on layouts whose panels differ in width, which today means Russian alone.

The argument that decided it came from a choice the code had already made. `HexboardBoard` computes the board's height as the tallest panel's and uses it for all three, so a swipe never resizes the board vertically; sizing the radius per panel answers the same question the other way, and the two were simply inconsistent. All three panels now take the radius that fits the widest, and narrower panels sit centred.

Two things went wrong on the way and both are recorded because they shaped the outcome. Claude described per-panel sizing as making the board "unstable", which overstates it — nothing breaks, taps still route, accessibility nodes still land; it is a resize, and a visual and motor change rather than a fault. And the user's first answer, to let each panel size its own, was given on the reading that the proposal was about key sizes differing *between languages*. It is not: nothing here crosses languages, and SPEC already says a wider alphabet gets correspondingly smaller keys, so Russian's keys are smaller than English's whatever happens. Once the scope was clear the user chose shared sizing. The reversal is recorded as a correction of scope, not indecision.

The trade in the terms it was finally decided on: per-panel sizing costs a ten per cent resize on every swipe, shared sizing costs RARE and SYMBOLS ten per cent of their key size permanently, on a keyboard whose headline is large keys.

Spreading a narrower panel's keys to fill the width instead of centring was refused — it breaks the hexagonal packing SPEC calls inviolable.

**Queue changes:** moved from Unprocessed into Processed below the readiness line with `Blocked by: [install-and-enable-on-pixel]`. SPEC's layout details gained that panels never resize the board, with the cross-language case restated so the two are not confused again.

**Work processed:** kept — [panel-key-size-consistency].
