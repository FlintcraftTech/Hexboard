# [HASH] — Autocorrect bounded to the commonest half of the word list, separating what the engine recognises from what it may produce

Recorded 2026-09-17 13:59. This entry carries the reasoning for four items settled together; [completion-slots], [word-list-size-level] and [predictive-frequency-table] cite it rather than restating it.

The session's owner brought a specification written with another assistant — a four-tier dictionary searched by prefix, with tiers cascading as each ran out of matches. Most of it was refused and the refusal is the useful part. Every tier in that design is reached by prefix, which treats the first letter as certain; that is the exact mechanism this project threw out on 2026-08-20, on the owner's own complaint that a wrong first letter is far worse than a wrong later one. It also cannot use the six-neighbour geometry that is this engine's whole advantage — type `vha` for `cha` and every tier returns nothing.

What survived is the tiering, reframed from a lookup order into a bound on the correction target. The engine now separates two sets: every word in the shipped list is recognised and so never corrected however rare it is, while a correction may only ever produce a word from the commonest bands. The bound is written as a proportion rather than a figure — the commonest bands that together make up no more than half the list, which re-derives itself if the generation level changes — because a bare number is a limit with no derivation.

The honest limit is written into the item: SCOWL's bands are coarse and one of them holds about 38,000 words, so within a band there is no ordering at all and the engine falls back on the shorter word. That is a stand-in for frequency data, named as such.

What this fixed, which was not the aim: the trade [word-list-size-level] had been stuck on since 2026-09-05. That entry could not raise the list's generation level to catch missing everyday words, because a higher level admitted obscure words the engine could correct toward. Separating recognition from production removes the coupling entirely.

The owner's own want was narrower than any of this — not to see suggestions in random order, with commoner words preferred. That want is what the whole cluster serves, and a wrong turn on the way is recorded on [completion-slots].

**Queue changes:** [uniform-neighbours-predictive] rewritten in place, cleared to run as before, with the band ceiling replacing the tie-break it carried; the cross-reference written onto [word-list-ok-by-name].

**Work processed:** kept — [uniform-neighbours-predictive], amended rather than newly processed.

**Advisory:** filed — [forward-advisory]
