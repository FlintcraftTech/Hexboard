# 770b778 — [curly-quote-double-route] both routes kept, and SPEC now says a long-press alternative is not a duplicate

Session of 2026-09-12, 12:54.

`“ ” ‘ ’` became SYMBOLS keys on 2026-09-05 and were already long-press alternatives on `'` and `"`, so each is reachable two ways. Nothing objected: SPEC's manifest rules bar a character living on more than one *panel*, and a long-press alternative is not a key on a panel, so the validator was silent and correctly so.

**The entry framed the stake as accent slots, and reading the configs weakened that.** The popup row is capped at what fits above a key, so alternatives are finite — but `'` carries three and `"` carries four, where `o` and `a` each carry eight. Dropping the four quotes would free slots on the two keys least short of them, and would take the typographic quotes away from where a hand already is mid-sentence in exchange for a swipe to SYMBOLS.

So both routes stay, and the general question was settled in SPEC rather than in one layout's config: a character reachable both as a key and behind a hold is a convenience rather than a duplicate. That answers every future layout whose config gains typographic quotes, which is what the entry asked for, and it needed no new field in the config schema for a written justification to live in.

It also closed a loose end in [russian-panel-gaps]: "elsewhere" in that item's no-duplicate test means *on another panel*, not "behind a hold", or the rule would empty most of the quote slots it exists to fill.

**The same reasoning produced the opposite outcome later the same session**, in [spanish-letter-panel-gaps], where ª and º were promoted to a key and removed from `a` and `o`'s alternatives — because there the lists are the full ones. Same argument, different facts.

**Queue changes:** [curly-quote-double-route] deleted after SPEC gained the clause; [russian-panel-gaps]'s rule qualified in the same move.

**Work processed:** deleted — [curly-quote-double-route].
