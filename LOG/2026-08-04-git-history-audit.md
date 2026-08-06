# a42cd01 — Git history audited before going public: no machine paths or credentials, three findings captured

This audit is the mitigation the [repo-go-public] red flag was cleared by, so it had to run before the repo could be flipped. Its point is that making a repo public exposes the whole commit history, not just the current files — so a path or a personal line removed months ago is still published.

Four criteria passes ran across all six commits and every file ever added, one criterion held across the whole history at a time rather than re-deciding all four per file. Three passes came back clean. There are no absolute machine paths anywhere — notably `android/local.properties`, which holds the SDK path, and `android/.idea/`, which holds workspace state, were never committed, because the Android Gradle project's own .gitignore covers both. No email addresses appear in any file's content. No credentials or tokens exist; every match on the credential patterns was the word "token" in prose about token cost.

Three findings were captured. The email address in every commit's author and committer metadata is the one that cannot be fixed by editing a file — it needs a full history rewrite, which changes every hash and deserves its own decision. A candid line about Alex in the queue would read very differently on a public page than in a private planning doc. And the third is a judgment call rather than a leak: going public publishes the entire internal planning record — LOG, QUEUE, CLAUDE.md and the FAQ — which is arguably the most interesting thing in the repo, but should be a conscious choice rather than a default.

Worth recording that the audit deliberately did not fix anything. Two of the three findings point at rewriting history, which is destructive, and the item said so up front: this pass reports, and a later decision acts.

The item carried a red flag, cleared at processing rather than here. It was cleared by design, not by acceptance — the risk was removed from the go-public path by making this audit a prerequisite of it. That gate is not met yet, since the three findings are now the outstanding work.

**Files touched:** none — the audit read the full commit history and every file ever added, and edited nothing.

**Routed to Captures:** [git-history-email], [queue-candid-line], [planning-record-public], [session-payload-sample]

A forward-recommendation advisory, [advisory-2], was also filed after the commit, pointing the next planning session at these findings.

A fourth capture was filed at the close rather than during the audit. Staging for the commit surfaced an untracked file, `resources/research/session-start-payload-sample.json`, holding absolute machine paths and a session transcript path. It was left out of the commit and filed as [session-payload-sample] with an uncleared red flag. It is worth noting how it was caught: the audit reads committed history, so an untracked file is outside what it looks at, and it was the commit step's out-of-scope dirty-path check that found it.

**Approval outcomes:** all findings approved as-is.
