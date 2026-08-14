# [HASH] — Root `.gitignore` created and `FAQ/` untracked; the session-payload red flag carried through as cleared

The repository had no root `.gitignore` at all — only `android/.gitignore` — so this item created one, and it was written complete in one pass rather than created and reopened. Three paths went in, for three different reasons, and each carries a comment in the file saying which.

`FAQ/` documents how the method works. It is installed into each project deliberately so it can be consulted mid-session, so the folder had to keep working on disk; what it should not do is ship with a public keyboard repository, where it is documentation about a workflow that a visitor to a keyboard project has no use for. `git rm --cached -r FAQ/` removed the two files from git's index without touching the working copy.

The second path is the red flag this item carried, and it was already cleared at planning. `resources/research/session-start-payload-sample.json` holds this machine's account paths, a session ID and a local transcript path. The clearing decision is worth restating because the intuitive fix was tried first and was wrong: an earlier plan was to delete the file, and deleting it is self-defeating, because the session-start hook writes it again whenever it is absent — so each new session would recreate it with that session's paths in it. Ignoring rather than deleting is what actually removes the risk: the file stays on disk, harmless, and no session can stage it. That correction was made at the /plan session of 2026-08-06 and this build simply implemented it.

The third path, `android/.idea/`, is not a risk at all. Android Studio writes it, the git-history audit confirmed it has never been committed, and nothing in it is exposed — it was noise in every `git status`. It rides in this item purely because this item creates the file.

All five verifications the item specified passed: `git ls-files FAQ/` returns nothing; both FAQ files are still on disk; neither the payload sample nor `android/.idea/` appears as untracked any more; and the FAQ removal is staged cleanly.

The known limitation, accepted when this was decided rather than discovered now: the two FAQ files are in every commit made so far, so untracking cleans the current tree and all future commits but not the history, and anyone browsing past commits can still read them. Full removal would need the history rewrite ruled out in [git-noreply-email]. The payload sample carries no such limitation — it has never been committed, and this item ensures it never can be.

Red flag: carried, and cleared. It cleared by being designed out — the ignore line makes staging the file impossible — rather than by being accepted.

**Files touched:** `.gitignore` (created; later extended by the same session's format migration with `INBOX/` and `.throughliner/` lines).

**Routed to Captures:** [throughliner-doc-drift]
