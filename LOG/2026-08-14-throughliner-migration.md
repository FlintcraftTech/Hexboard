# [HASH] — Project migrated to the current Throughliner document format: INBOX added, version markers renamed, two ignore lines added

This ran mid-session, because the session-start check refused to proceed normally: the project's documents were on format 1 and the installed plugin expects format 3. The warning is deliberate about why it halts rather than degrading — a session reading documents in a shape it no longer parses reports a confidently wrong picture instead of an error.

The migration adds what a newer version introduced; it does not rewrite content. What it found already in place — SPEC.md, QUEUE.md, LOG/, FAQ/, `resources/research/` — it left alone. QUEUE.md needed no conversion because it was already in the two-section Processed/Unprocessed shape.

What changed. An `INBOX/` folder with an `archive/` inside it, the project's mailbox for messages other projects send it. `.gitignore` gained a line for it, and one for `.throughliner/`, the transient signal saying a file is being written right now so an editor open on the same document can hold off. Neither belongs in a repository: a read message is archived rather than deleted, so a committed mailbox would accumulate another project's raw text permanently, long after its content had been carried into this project's own words. Nothing was in the mailbox to be exposed — the folder was created empty this session.

The version markers were renamed rather than added to. The method was called Sovereign Implementer until this format change, and both marker files were named for it; `.si-version` was deleted and `.throughliner-version` written in its place, so later sessions do not read a file the plugin no longer writes to. `.throughliner-format-epoch` was written last, once the rest had actually landed, because writing it early would silence the warning while the project was still on the old shape and nothing would raise it again.

One thing the migration surfaced and did not fix. The current format expects every item held below the readiness line to name the queue item blocking it, and three items here state their condition in prose instead — two of them waiting on something in the world rather than on any queued item, which under the new shape has to be filed as its own item first. That is processing work, not formatting, so it is filed as part of [throughliner-doc-drift] for a planning session.

**Files touched:** `.gitignore` (two lines added); `.throughliner-version` (new); `.throughliner-format-epoch` (new); `.si-version` (deleted); `INBOX/archive/` (new, empty).

**Routed to Captures:** [throughliner-doc-drift]
