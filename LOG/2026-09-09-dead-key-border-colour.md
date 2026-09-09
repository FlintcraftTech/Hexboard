# [HASH] — [dead-key-border-colour] five dead colour values removed, with the one live border left exactly where it is

This session ran across 2026-09-05 and 2026-09-09.

[soft-key-edge] removed the 1.5dp border from every key, because a border in a lighter colour than the fill was the hardest edge on the key and would have reinstated the boundary the fade exists to dissolve. What it did not remove was the colour that fed it: `KeyColors` still declared a `border` property and `colorsFor` still supplied a value for each of the five key kinds, and nothing read any of them.

**It is dead rather than reserved.** SPEC says a single dark theme is enough for v0 with theming later, so a spare colour field could plausibly be held for a future theme — except SPEC also says a key is drawn as a soft-edged circle with no border, so no theme will want one.

Why spend anything on five lines nothing depends on: this repository is public, and a colour named `border` on a keyboard that draws no borders reads to a stranger as a leftover rather than a decision, which is exactly what it was.

**The thing the item wrote down to protect, and it was right to.** There is one live `.border(...)` in `KeyboardPanel.kt`, on the accent popup's container — the rounded panel that floats above the board while a key is held. It is correct and stays: it separates a floating panel from the board behind it, where the no-border rule is about keys. Whoever does this tidy-up greps for `border`, finds two things and is one careless moment from removing both.

**One departure from the item's observation, decided at the build.** It asked for a grep returning no mention of a border "in a comment describing a key". Two such mentions survive, in `KeyboardPanel.kt` and `KeyGeometry.kt`, and both state SPEC's rule that a key is drawn with no border — which is the reason the code makes no border call. Deleting the statement of a rule to satisfy a grep would remove the thing that explains the code.

The compile half of the observation could not be run at build time and was proved on 2026-09-09: the app compiles and installs, so no construction site was missed.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

**Routed to Captures:** none.
