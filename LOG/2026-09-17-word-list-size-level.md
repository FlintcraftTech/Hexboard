# [HASH] — Word-list size level settled at 60 by reading SCOWL's own README, and the entry deleted as a finding rather than work

Recorded 2026-09-17 13:59. Sits inside the commonness work recorded under [uniform-neighbours-predictive].

This entry had asked since 2026-09-05 which SCOWL size level the English word list should be generated at, and had been held because answering it wanted someone typing on an engine that does not exist. The owner chose to pick a level anyway rather than wait, which is what sent this to a read nobody had done.

Nobody here had ever read what the levels mean. SCOWL's author states it directly: 60 is "the size used for default spell checking dictionary", 70 the large one, 80 holds "all the strange and unusual words people like to use in word games such as Scrabble", and 85 holds words that may no longer be modern English — with the explicit recommendation, "For spell checking I recommend using size 60. This size is the largest size that I am fairly confident does not contain any misspellings or invalid words," and 80 and above named as unreasonable for spell checking outright.

So the real choice was only ever 60 or 70, where the entry's framing assumed a much wider range, and the level this project picked by reasoning about a trade is the one SCOWL recommends — for the same concern, stated better: the largest size confidently free of invalid words is also the largest that gives a typo no rare real word to hide behind.

That left nothing to build. No file changes, and the question of what else the list lacks already has a standing answer in the generator's add-a-word-by-name mechanism — so the entry was a finding rather than work, and its content went to the research file before the entry was removed.

**Queue changes:** [word-list-size-level] deleted from Unprocessed; the finding written into `workshop/resources/research/word-list-licence-and-frequency.md` with its index line; the reasoning strengthened on [word-list-ok-by-name], which is cleared to run and is the pattern for any further missing word.

**Work processed:** deleted — [word-list-size-level].
