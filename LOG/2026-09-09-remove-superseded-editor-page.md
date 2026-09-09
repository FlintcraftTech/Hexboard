# [HASH] — [remove-superseded-editor-page] a page kept for a future that was later cancelled, and nobody went back to the keeping decision

This session ran across 2026-09-05 and 2026-09-09.

`planning/hexboard-editor.html` was kept deliberately. On 2026-08-21 it was moved from the repository root into `planning/` with `git mv` and README gained a paragraph describing it, and that session's record states plainly that deletion was weighed and lost, on the ground that "its drag-and-drop interaction design is the expensive half of that future work". The future work it named was [variant-editor].

[variant-editor] was deleted on 2026-09-02, when the user replaced a contributor-facing editor with layouts copied from each language's own standard. The one recorded reason for keeping the page went with it, and nobody returned to the keeping decision. That is the shape worth naming rather than treating as tidying — a decision resting on a premise the project itself later removed — and it is the third instance of it this project has caught.

What was actually at stake is small either way. This repository is public, so a stranger browsing it finds a page titled "HexBoard Layout Editor" with a working drag-and-drop board and an Export button, and a README paragraph calling it prior art for a future the project decided against. Against that, keeping the file costs a stale paragraph. **Git retains the drag-and-drop design either way**, which is what makes the removal reversible and is why `git rm` was used rather than an untracked disappearance.

The alternative offered on 2026-09-05 — keep the file and rewrite the paragraph to say it is prior art with no live successor — was not taken: it leaves a public repository advertising a working editor for a cancelled feature, and the paragraph would then exist only to explain why the file exists.

All three clauses of the observation were run. `planning/` holds `layout-preview.html` and `dictation-prompter.html` and no editor page; a grep for `hexboard-editor` across the repository returns only records and queue prose describing its history; and `git log` on the deleted path still returns the commit that filed it.

**Files touched:** `planning/hexboard-editor.html` (deleted), `README.md`.

**Routed to Captures:** none.
