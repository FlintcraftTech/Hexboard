# 8f705a3 — [claude-md-phase-ran] built: the phase line now says the app has run on a Pixel 6, and is already one build behind

Closed 2026-09-02 16:18. Eleventh item of the run.

The replacement wording was written on the item at planning and transcribed exactly: implementation is under way and has run on a Pixel 6, there is no input method service yet, and design-before-coding still holds. The greps the item named confirm the old clauses are gone and the new one is present.

One thing to know, and the reason a phase line rather than a what-has-run line was chosen: this same run built the input method service, so "no input method service yet" was one build behind by the time the run ended. That is the designed cost — the phase turns over when the keyboard can be switched on, which the install walkthrough decides — and a capture, [status-lines-after-install], holds the turnover against that walkthrough together with the README's status section, which is behind the same way.

Rule gate: not needed — no rule is added or removed; a stale statement of fact replaced.

Tick: done, confirmed: greps for "none of it has been run" and "compile-and-view-panel" return nothing and "has run on a Pixel 6" finds the new sentence.

**Files touched:** `CLAUDE.md`.

**Routed to Captures:** [status-lines-after-install], filed by the post-run rescan and held against [install-and-enable-on-pixel].
