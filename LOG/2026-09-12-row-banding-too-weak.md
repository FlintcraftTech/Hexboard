# [HASH] — [row-banding-too-weak] banding narrowed to the letter keys, because five kind colours were fragmenting every band

Session of 2026-09-12, 12:54.

The user's account of the board on the Pixel 6 was that the alternating shades barely show and read as randomly coloured rather than as bands. The entry had already caught that the second half is the more useful one: "too faint" points at one constant, "randomly coloured" points at the fact that a key's base fill comes from its kind — letter, punctuation, special, space, symbol, rare — so one tint over five bases gives five results and no band.

**The arithmetic made the faintness worse than it looked.** `ROW_TINT = 0.2` is a fifth of the way toward a key's *lit* colour, and lit is itself only 35% of the way to white, so an odd row was about 7% lighter than an even one — inside the range that reads as a slightly different colour rather than as a band.

**So the fix is narrowing rather than shouting.** The banding applies to letter keys only: they carry the recognition the band exists for — QWERTYUIOP is the string almost everyone knows on sight, which is how SPEC's familiarity principle is defended — and they share one base fill, so the tint produces one consistent step. `ROW_TINT` goes to 0.35, stated as a ratio rather than a look: a press travels the whole way to lit, so the resting band stays three times closer to its own row's colour than a pressed key is, which is SPEC's never-mistaken-for-a-press constraint made checkable.

A consequence stated rather than discovered: the bottom row, being punctuation and spaces, carries no band. There is no familiar letter string there for a band to reinforce.

The largest option — bringing the six kind colours closer together, which the entry suspected might be the right one — was deliberately held back: it changes the whole board's look to fix a row-reading problem, and if letters-only banding reads correctly the kind colours are doing their own job.

**Queue changes:** [row-banding-too-weak] designed out and cleared to run; SPEC's banding bullet rewritten from rows to letter keys.

**Work processed:** kept — [row-banding-too-weak].
