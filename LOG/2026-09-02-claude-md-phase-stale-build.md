# 1979343 — CLAUDE.md's phase line replaced, and by the end of the same session it was already half wrong again

`CLAUDE.md` told every session that opened this project "Current phase: extended planning, no implementation." That had not been true for some time: `MainActivity.kt`, `KeyboardPanel.kt` and `KeyLayout.kt` exist, and the Compose keyboard already draws its keys from the config. Because `CLAUDE.md` is loaded at the start of every session, it is the first thing shaping what a fresh session believes exists — one reading "no implementation" starts from a wrong picture and can propose building what is already built.

The old sentence was doing a real job as well as a wrong one, and the replacement keeps it. "Extended planning" told sessions not to rush into code, and that instinct still held, because nothing written had been compiled or run. So the new wording carries both facts — there is real Kotlin here, and none of it has been seen to run — with [compile-and-view-panel] named as the item that would change that. Replacing the line with "implementation has started" alone would have traded one wrong picture for another.

This belongs in `CLAUDE.md` rather than `SPEC.md` because it describes how to work on the project rather than what the project is, which is the distinction those two files are most often confused across, and in this direction specifically.

**The half of it that is now stale is worth flagging for the next planning session.** Later in this same session, [compile-and-view-panel] was driven to its end: the app compiled, installed on the Pixel 6, drew its keys and typed the right characters. So the clause saying nothing has been compiled or run is no longer true, and the item it names as the thing that would change that has now happened. The sentence is not rewritten here — a build does not write product truth or project rules a second time in the session that wrote them once — so it stands as the one visibly stale clause in the file.

**Files touched:** `CLAUDE.md`.

**Routed to Captures:** none.

Tick: done, confirmed — a grep for "no implementation" returns nothing, and the replacement names both that Kotlin exists and that it had not been run.

Depth: short.

Rule gate: not needed, no rule is added. This replaced a stale statement of fact about what exists in the repository. The one rule-like force the old sentence carried, that sessions should not rush into code, is kept as an amendment to that same sentence — its parent — rather than as anything freestanding, so nothing new competes for a reader's attention.
