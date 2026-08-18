# 67a7dd0 — The method's new name written into the two files that instruct rather than record

The method this project runs on was called Sovereign Implementer and is now called Throughliner. The /setup migration earlier on 2026-08-14 updated the scaffolding it owns but does not rewrite content, so the old name was left standing in several places while the README, written the same day, already used the new one. The repo was inconsistent with itself at the moment it was about to become public.

The decision that shaped this build was how far back to go, and it was settled at the /plan session of 2026-08-14 on the user's instruction: update the live instruction, leave the records alone. The reason is that a LOG entry is a record of what a session actually said at the time, so rewriting it would misreport that session. The same argument covers queue prose describing past sessions. That split is what makes this a two-file build rather than a repo-wide find-and-replace, and it is why a grep for the old name still returns matches — five LOG files and QUEUE.md — which is the correct result rather than an unfinished job.

Four occurrences changed. `CLAUDE.md` carried the name in its opening line and again in the paragraph explaining why the planning record is published. `.gitignore` carried it in the comment saying why `FAQ/` is ignored and in the comment above the session-start payload sample. All four are instructions to a reader or to Claude about how this project works now, not descriptions of what happened before.

Worth noting for a future session: the opening line of `CLAUDE.md` sits inside the plugin-managed block that /setup rewrites. Editing it by hand was still right — the installed plugin left the old name there — but a later /setup run is the thing that would either confirm or undo this particular line.

**Files touched:**
- CLAUDE.md: two "Sovereign Implementer" mentions changed to "Throughliner" — the opening line, and the go-public paragraph under Project rules
- .gitignore: two "Sovereign Implementer" mentions changed to "Throughliner" — the FAQ/ comment and the session-start payload sample comment

**Routed to Captures:** none
