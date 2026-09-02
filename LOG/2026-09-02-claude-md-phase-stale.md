# [HASH] — CLAUDE.md's "no implementation" phase line filed for correction, with the instinct it carried kept rather than dropped

Surfaced by /rescan. The project rules in `CLAUDE.md` say "Current phase: extended planning, no implementation", and that has not been true for some time: `MainActivity.kt`, `KeyboardPanel.kt` and `KeyLayout.kt` all exist, the Compose keyboard already draws from the config, and several build items are cleared to run.

It matters more than a stale sentence usually would because `CLAUDE.md` loads at the start of every session, so it is the first thing shaping what a fresh session believes exists. The failure mode is concrete: a session proposing to build what is already built, or treating the existing Kotlin as hypothetical.

The reason this is a rewrite rather than a deletion is that the line does a real job as well as a wrong one. "Extended planning" told sessions not to rush into code, and that still holds, because nothing written has been compiled or run — [compile-and-view-panel] is the cleared `[user]` item that would establish it. Replacing the line with "implementation has started" alone would trade one wrong picture for another, so the new wording has to carry both facts.

Routed to `CLAUDE.md` rather than `SPEC.md` deliberately: it describes how to work on the project rather than what the project is, which is the distinction those two files are most often confused across, and in this direction specifically.

The queue lint caught something the session would otherwise have missed — an item touching a rules file owes a rule-gate disposition, and without one a build halts rather than composing one. The gate was read and a disposition written onto the item: not needed, because no rule is added; the old sentence's one rule-like force survives as an amendment to its own parent rather than as anything freestanding.

**Queue changes:** filed into Unprocessed by /rescan, processed in the same session, and cleared to run after [spec-principles-rework]; a `Rule gate:` line added.

**Work processed:** kept, cleared to run — [claude-md-phase-stale].
