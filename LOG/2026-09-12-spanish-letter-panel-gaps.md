# 770b778 — [spanish-letter-panel-gaps] three holes down Spanish's right edge, filled with the two inverted marks and an ordinal key

Session of 2026-09-12, 12:54.

Found while reading every shipped config for the structural keys, which is not what the read was for: Spanish has no key at (0,10), (2,10) or (3,10) on its letter panel. It is eleven columns wide to fit ñ and the rest is the ten-wide English arrangement, so the extra column is empty on three of four rows. SPEC calls an empty slot an opportunity rather than an acceptable gap, and these sit on the panel a Spanish typist looks at most.

All four obvious candidates were already reachable, but only behind holds — `?` carried ¿, `!` carried ¡, and the ordinals sat in `a` and `o`'s accent lists. That matters differently for the two pairs: the inverted marks open every Spanish question and exclamation, so leaving them behind a hold makes required punctuation slower than optional punctuation.

So ¿ takes row 3 col 10, among the punctuation; ¡ takes row 2 col 10; and a º key at row 0 col 10 carries ª behind it, the two being a pair used together.

**º and ª come out of `a` and `o`'s lists in the same move, which is the opposite call to [curly-quote-double-route]'s and rests on the same argument.** There, freeing slots on keys carrying three and four bought nothing; here `a` and `o` carry eight each and are the fullest lists in the layout.

A knock-on accepted knowingly: [russian-panel-gaps]'s fill rule named these four as Spanish's fallback characters, so promoting them spends that fallback and Spanish is expected to finish with about two SYMBOLS slots the rule cannot fill — which that rule leaves empty and reports rather than inventing something. Held against the fill item, since the two builds write the same config.

**Queue changes:** [spanish-letter-panel-gaps] filed, designed out and moved into Processed, held against [russian-panel-gaps], whose fallback list was corrected in the same move.

**Work processed:** kept — [spanish-letter-panel-gaps].
