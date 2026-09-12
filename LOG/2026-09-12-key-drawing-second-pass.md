# [HASH] — [key-drawing-second-pass] labels sized against the key rather than its solid core, which decouples them from the fade and reverses a decision made blind

Session of 2026-09-12, 12:54.

The user's verdict after seeing the soft key edge on the Pixel 6 on 2026-09-09 — the first time anyone had looked at values [soft-edge-fraction-values] picked by arithmetic and shipped unseen. Two complaints: the gradient is a thin ring rather than a soft edge, and the labels are too small.

**They were coupled, which is why they had to be settled together.** `labelSize` multiplied the *solid* radius, so lengthening the fade by lowering `SOLID_FRACTION` would have shrunk every label and made the second complaint worse.

**The second complaint reverses a decision, and the decision was sound on its own terms.** `KeyGeometry.kt` says the label is sized against the solid part "so a glyph sits inside the definite middle of the circle rather than out on the soft edge" — [soft-key-edge]'s reasoning, which [soft-edge-fraction-values] upheld when it refused the fade radius. It had never been looked at; it has now. The user's framing decided it: a key should be sized "like if I placed the key over a shape in inkscape instead of inside a cell in excel" — the circle is the object, and what is drawn inside it is drawing.

Three values, all tunable rather than derived truths, chosen so the build is fully described and expected to move once the phone has been looked at. `LABEL_FRACTION` stays 0.90 and `LARGE_LABEL_FRACTION` stays 0.78, applied to the solved radius instead of the solid core — so no number is invented and a lowercase label goes from 0.675 of the radius to 0.9. `SOLID_FRACTION` becomes 0.5: half the key solid, half fading, a proportion rather than a figure picked to look right, taking the gradient band from 0.295 of the radius to 0.545. `FADE_FRACTION` stays 1.045, where neighbouring fades just meet, because the outer size is the part the user said was right.

[judge-drawing-values-on-phone] is the sitting that judges all of it.

**Queue changes:** [key-drawing-second-pass] designed out and cleared to run.

**Work processed:** kept — [key-drawing-second-pass].
