# [HASH] — SPEC's principles rebalanced by moving machinery out, not by cutting to a length

Within the list a reader skims, a deferred feature outweighed the reason the project exists: the perceptual wedge ran 22 words against predictive text's 309. That measurement is what the item was filed on, and it overstates the problem on its own — the wedge is also described at length in "How it works" — so what was wrong was the balance inside one list rather than the coverage of the wedge.

The rewrite followed SPEC's own admission rule: a sentence describing internal fields, file formats or the steps a component runs through belongs in the doc owning that mechanism, with SPEC naming the behaviour instead. Out of the key-inventory principle came the config filenames and the list of fields a layout config carries, both of which already live in that config's `about` text and in README.md. Out of the predictive-text principle came the three sentences deriving the neighbour table from the config plus the zag rule and stating when it is computed — near-identical text already sits on [uniform-neighbours-predictive], so that was a deletion rather than a relocation. Out of the layout-per-language principle came the a-layout-is-one-config-file mechanism, which the key-inventory principle now carries alone.

Four fragments elsewhere in the document went with them: the uppercase-glyph line's scaling-factor clause, which is implementation plus a note on how the decision was made; the prototype's overlapping square boxes and the fresh-effort-not-a-port line, both history about the prototype rather than truth about the product; and the wedge paragraph's "not to be re-litigated" and "not re-argued from the armchair", which are guidance about how to work on the project and which `CLAUDE.md` already carries.

What the rework deliberately did not touch, checked in the same pass so a later reader knows the omission was a decision: the clipboard, voice-input and voice-adaptation principles are behavioural throughout — they say what the keyboard does, not how — and the accessibility line's justification prevents a real error about accessibility services bypassing touch routing, so it stays.

**This was never a word budget, and the numbers are recorded only as evidence that machinery left.** Key inventory ran 269 words and now runs 237, layout-per-language 198 and now 177, predictive text 309 and now 255. A true sentence about a live feature was not evicted for being long.

**Files touched:** `SPEC.md`.

**Routed to Captures:** none.

Tick: done, confirmed — a grep for `key-layout.json`, `key-manifest.md`, `re-litigated`, `line-by-line` and `armchair` returns nothing; the predictive-text bullet no longer says when the neighbour table is computed; and every feature described before still carries a sentence.

Depth: short.

Rule gate: not needed — no rule was authored or amended.
