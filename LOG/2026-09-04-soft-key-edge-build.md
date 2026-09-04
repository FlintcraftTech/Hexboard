# [HASH] — Keys drawn as a radial fade to their whole touch target, with the border removed

Written 2026-09-04 at 15:56. The planning record that designed this work is `2026-09-04-soft-key-edge.md`; this is the build.

Raised by the user on 2026-09-03 from using the keyboard rather than looking at it: the circles still appear a little small, enlarging them would make the board look crowded, and the problem is *perceived* key size, which lowers press confidence and slows typing. His proposal was that a soft edge might make it as clear as possible that there is no hard circular boundary, only a soft circular target.

The measurement behind that: `VISIBLE_INSET = 3f` shaved 3dp off every key before drawing, so on the Pixel 6's solved 22dp touch radius the drawn circle was about 19dp — roughly 86% of the radius and about 74% of the area. A quarter of every key was deliberately invisible. And his crowding prediction was arithmetic rather than a hunch: neighbouring centres sit about 46dp apart, so drawing at 19dp leaves an 8dp gap, and taking the inset to zero would leave about 2dp — visibly nearly touching. Simply drawing them bigger really does trade one complaint for the other.

A second thing the code showed strengthened the case: each key carried a 1.5dp border in a *lighter* colour than the fill, so the boundary was emphasised twice — once by the fill stopping and once by a brighter ring exactly where it stopped. Any soft edge had to remove the border or the border would reinstate what the fade exists to dissolve.

This serves the perceptual wedge rather than threatening it, which SPEC requires be questioned rather than waved through. The wedge is a claim about *corners*, and a radial fade has none; beyond that a radial gradient is centre-emphasising by construction, and aiming centrally is exactly the behaviour the wedge predicts and wants. The opposite risk is real and stays recorded: fade too far and there is nothing definite to aim at, which would cost confidence instead of building it.

Expressing the edge as fractions of the radius also removed a scaling defect by construction. The absolute 3dp inset gave away a larger share of a smaller key — worse on an eleven-wide layout, and down to about 56% of the touch area at `MIN_RADIUS = 12`.

The two fractions are tunable constants rather than decisions, settled with the user on 2026-09-03: whether the edge is soft at all was the decision, and how steep the fade is is one curve inside otherwise fully described work. They start at the conservative end — solid to 55% of the radius, transparent at 100% — which lights more area than the old disc while putting nothing new into the gaps, so it cannot read as more crowded than before. The generous variant, solid to 45% and transparent at about 118% where neighbouring fades just meet, is the other end worth trying. His standing instruction, given the same day: small visual adjustments of this kind are made during the build rather than pinned in advance.

**A consequence nobody costed, found at the tick and filed.** The item directed label sizing to take the solid fraction so glyphs stay off the fade, and it does — but the solid part is 0.55 of the radius where the old drawn disc was about 0.86 of it, so every glyph is now roughly a third smaller. That is per instruction rather than a slip, and it was never weighed against the original complaint, which was that things look too small. Filed as [label-size-after-soft-edge].

Refused: reducing the inset toward zero (the user's own objection, confirmed by the arithmetic); keeping the border and softening only the fill, which changes least where it matters most; a second absolute dp constant, which reproduces the scaling defect; and deciding the falloff from a rendered comparison, since a comparison page built in the scratchpad could not be got in front of him, on which instruction the values rode into the build as tunables instead.

**Confirmed:** nothing.

**Unconfirmed, transcribed from the tick:** `KeyEdgeTest` is a unit test but nothing here can run Gradle, so Android Studio runs it. Two things want eyes on the phone rather than a test: whether the fade reads as a softer target or as a vaguer one, and that labels are now sized against the solid part (0.55 of the radius) where they were sized against a 3dp-inset disc, so glyphs are smaller than before — the item directs that sizing, and how small is too small is a judgment for the handset.

**Files touched:** `KeyGeometry.kt` (`VISIBLE_INSET`/`visibleRadius` replaced by `SOLID_FRACTION`, `FADE_FRACTION`, `solidRadius()`, `fadeRadius()`; `labelSize` sizes against the solid part, ~30 lines), `KeyboardPanel.kt` (KeyCircle paints a radial gradient over the full touch extent with no border; accent-row cell takes the fade radius, ~25 lines), `KeyConfigUiTest.kt` (slot tolerance takes `solidRadius` in place of the removed `visibleRadius`, 3 lines), `KeyEdgeTest.kt` (created, 4 tests, 95 lines).

**Routed to Captures:** [label-size-after-soft-edge], [dead-key-border-colour].

**Depth:** short.

[row-tint] was placed immediately after this and judged against it: the tint is a resting fill, and once the fill fades at the edge the same fraction covers different visible area. [dead-key-border-colour] records the border colour this left behind unused.
