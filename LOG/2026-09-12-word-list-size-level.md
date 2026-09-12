# [HASH] — [word-list-size-level] split: "ok" fixed by name now, and how the size level gets chosen held until the engine can be typed on

Session of 2026-09-12, 12:54.

Lowercase `ok` is in no SCOWL file at or below level 60, so the shipped list does not carry it and the correction engine would be free to change a typed "ok" into something else. `OK` is there at level 35; only the lowercase form is missing.

**The fix is smaller than it looks because the machinery exists.** `scripts/generate-word-list.py` already adds a word back by name — the pronoun `I`, which SCOWL files with proper names because it is capitalised, sits in `ALWAYS_CAPITALISED` precisely so a dictionary without it cannot leave the engine free to change a typed `I`. Lowercase "ok" is the same shape of problem and takes the same shape of answer, and it drags in no obscure words the way raising the level would. Split out as [word-list-ok-by-name] and cleared to run.

**What stays here is the question the entry is actually about**: how the level gets chosen at all, and what else the list is missing. Nobody has looked, and looking means typing on the engine — until then a different number is only a different guess, and a higher level admits obscure words the engine could correct *toward*, which is the trade level 60 was making. `Blocked by: [uniform-neighbours-predictive]` records that ordering.

**One honest limit named at the time:** that blocker is already in Processed, so the field records the ordering for a reader and does no holding — this entry will keep being offered. That observation became the second method report of the session.

**Queue changes:** [word-list-ok-by-name] filed and cleared to run; [word-list-size-level] kept in Unprocessed with its blocker line and the split written in.

**Work processed:** kept — [word-list-ok-by-name], [word-list-size-level].
