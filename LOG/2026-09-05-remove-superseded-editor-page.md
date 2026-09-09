# 5f9d97e — [remove-superseded-editor-page] created after the rescan found a file kept for a reason that had since been deleted

Surfaced by this session's rescan and processed with the user rather than filed. The first framing was wrong and is worth recording as wrong: the page looked like a leftover nobody had noticed. It was the opposite — on 2026-08-21 `hexboard-editor.html` was deliberately moved from the repository root into `planning/` with `git mv`, README gained a paragraph describing it, and that session's record states plainly that deletion lost, on the ground that its drag-and-drop interaction design is the expensive half of the future work it was kept for.

That future work was [variant-editor], deleted on 2026-09-02 when the user replaced the contributor-facing editor with copying each language's own standard. So the one recorded reason for the keep went with it, and nobody returned to the keeping decision. That is the same shape as two other things caught this session — a decision resting on a premise the project later removed — and the third instance of it in one day.

What is actually at stake is small and real: the repository is public, so a stranger finds a page titled "HexBoard Layout Editor" with a working board and an Export button, and a README paragraph saying it is groundwork for something coming. Nothing is coming. Against that, git retains the drag-and-drop design either way, so nothing anyone could want back is lost. The alternative offered — keeping the file and rewriting the paragraph to say it has no live successor — was not taken.

**Queue changes:** [remove-superseded-editor-page] created and cleared to run, cross-referenced with [status-lines-after-install], which edits a different paragraph of the same README section.

**Work processed:** kept — [remove-superseded-editor-page].
