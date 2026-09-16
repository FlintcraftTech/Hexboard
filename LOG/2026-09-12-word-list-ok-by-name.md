# 770b778 — [word-list-ok-by-name] lowercase "ok" added by name, the way the pronoun "I" already is

Session of 2026-09-12, 12:54.

Split out of [word-list-size-level], whose entry carries the reasoning: `2026-09-12-word-list-size-level.md`. This is the one case that is known rather than suspected and needs no evidence to settle — `ok` is in no SCOWL file at or below level 60, so the correction engine would be free to change it.

Added by name rather than by raising the level, because `scripts/generate-word-list.py` already does exactly that for the pronoun `I`, and raising the level would admit every obscure word in between — which the engine could then correct toward, the trade level 60 was deliberately making. Cleared to run, with the regenerated list carrying the word and the existing test asserting it, so a later regeneration at a different level cannot drop it silently.

**Queue changes:** [word-list-ok-by-name] filed and cleared to run.

**Work processed:** kept — [word-list-ok-by-name].
