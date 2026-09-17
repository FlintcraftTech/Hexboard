# [HASH] — `temp/` brought under the ignore rules, filed as a build in the morning and done by the setup run in the afternoon

Recorded 2026-09-17 13:59.

Found in a message this project's own INBOX carried — one of two outbound defect reports held locally, whose closing note observed that this project's `.gitignore` did not cover `temp/` although the method's rules describe that folder as gitignored and scaffolded by setup. Checked here rather than taken on trust: the file named `FAQ/`, one research payload sample, `android/.idea/`, `INBOX/` and `.throughliner/`, and nothing else, and no `temp/` folder existed.

The exposure is small but real. The method hands walkthrough drafts to `temp/`, so the first draft written there would sit untracked in a public repository waiting for a commit that staged everything — and the folder is for disposable material by definition, which is exactly what nobody would think to check before committing.

A grep for the visibility wording found the ripple that made this two files rather than one: `CLAUDE.md` states which parts of the project go public and named only `FAQ/` and `INBOX/` as staying out, so a third ignored folder made that sentence incomplete.

It was kept as a build, then held behind [setup-top-up] when the owner asked whether running setup would help — it would, and the ordering mattered because the visibility line sits inside `CLAUDE.md`'s plugin-managed block, which a top-up may rewrite. Doing setup first meant the sentence was written once rather than written and overwritten.

Both halves then landed inside that same session's setup run: `temp/` created with its ignore line, and `temp/` added to the visibility sentence when the managed block was refreshed.

**Queue changes:** [temp-not-gitignored] filed, kept into Processed, held behind [setup-top-up], and removed from Processed once its work had landed. `.gitignore` gained the folder with its comment; `CLAUDE.md`'s visibility line names all three folders.

**Work processed:** kept and then completed — [temp-not-gitignored].
