# [HASH] — Variant identity split out as its own buildable item after half the reason for deferring it proved false

[variant-editor] and [layout-switching] had both been skipped on 2026-08-20 against a single stated reason: a contributed config has no way to say which variant it is, and that schema "follows from a build, not from a decision anyone can make now". Re-reading that reason against the actual config showed only half of it survives.

The config was read rather than remembered. Its top-level fields are `schemaVersion`, `about`, `generates`, `manifestRules`, `rule1Note`, `nonConsumers` and `panels` — nothing identifying which layout it is, so the gap is real. But three parts of the schema are desk decisions that nothing about a working app would change: whether a variant is its own file, what identifies it, and what happens to the generated manifest. What genuinely waits on a build is how the app enumerates layouts at runtime and where it stores the user's pick.

The recommendation to split was Claude's; the user agreed to it. The three decisions were taken here: one file per variant, since a contribution should add one file and touch nothing else, which is the reason contribution beat forking in the first place; an `id`, a `name` and a stated fallback, with `key-layout.json` itself as that fallback; and `generates` becoming a per-layout path rather than one fixed one.

One thing folded in during the same discussion. The config's `about` field still opened "This file is the single source of truth for which characters exist" — wording SPEC dropped on 2026-08-20 and narrowed again today. The config was the last place the retired phrase survived. The user agreed to fold its rewrite into this build rather than file it separately, since this item already edits that file and regenerates the manifest from it.

The item states explicitly what it does not do: no Android code changes, because Gson ignores JSON fields the data classes do not declare. That is recorded as the reason no Kotlin file is listed rather than as a tested claim, since Gradle cannot run on this machine.

Placed at the top of the cleared region rather than its end, because [repo-go-public] is marked `Runs alone` and its own recorded placement puts it last among the build items — anything filed after it would never be reached in an unattended run.

**Queue changes:** [variant-schema] created and moved to Processed, cleared to run, at the top of the cleared region. Its SPEC sentence was written at this session's close rather than left to the build — see the chat-level entry.

**Work processed:** kept — [variant-schema].
