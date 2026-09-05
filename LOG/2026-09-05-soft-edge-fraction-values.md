# [HASH] — [soft-edge-fraction-values] created from the user's own diagnosis that the gradient had been built inside the old circle rather than around it, with the code's "1.18" figure corrected to 1.045

This session ran across 2026-09-04 and 2026-09-05.

The user said, in the middle of processing something else, that the soft edge looked wrong because the gradient had been built inside what was the boundary of the old circles rather than around them where there is a little space. Reading `KeyGeometry.kt` confirmed it exactly: `SOLID_FRACTION = 0.55` against an old hard disc at `radius - 3dp`, which is 0.86 of the touch radius at the Pixel 6's solved 22dp. So the part of a key reading as definitely there had fallen from 0.86 to 0.55 — one cause behind two separate complaints, since label size is a fixed fraction of the solid radius and the glyphs shrank by a third at the same moment.

Offered a comparison affordance on the phone or a picked value shipped with the install, the user chose the picked value; last session had already failed to get a comparison page in front of him. Solid goes to 0.75 and the fade to 1.045. The second figure matters: `KeyGeometry.kt`'s own comment named the generous end as "about 1.18, where neighbouring fades just meet", and neighbouring centres sit 2.09 radii apart, so they meet at 1.045 and would overlap substantially at 1.18. That wrong figure was written yesterday and would have been trusted by whoever tuned these next. The correction rides with the values, with the derivation beside it — including that below a solved radius of about 16.7dp the 1.5dp gap floor takes over and the fades fall short of meeting rather than overlapping, so 1.045 is a ceiling at every radius the board can solve.

[label-size-after-soft-edge] was merged into this item and deleted: same cause, and its three options are carried as refused alternatives. SPEC's key-drawing bullet was reworded, since a fade to 1.045 no longer stops at "the edge of the area it accepts".

**Queue changes:** [soft-edge-fraction-values] created and cleared to run; [label-size-after-soft-edge] merged into it and deleted; SPEC's soft-edge sentence reworded.

**Work processed:** kept — [soft-edge-fraction-values]. Deleted — [label-size-after-soft-edge].
