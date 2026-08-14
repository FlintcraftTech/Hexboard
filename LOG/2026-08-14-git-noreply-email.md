# def1f5a — This repo's git identity repointed at the GitHub noreply address, with the history rewrite left ruled out

One `git config --local` command, and the whole of the item's value is in what it deliberately does not do.

The original capture weighed rewriting the repo's history to remove the personal address from six commits. Planning ruled that out and this build did not revisit it. The reasoning stands: the same address is already visible in commit metadata across two other public repositories, so a rewrite here would not un-publish anything, while it would change every commit hash — invalidating every hash reference in `LOG/` and `QUEUE.md`, and forcing a rewrite of the pushed remote. Real cost, no protection bought. So this item is tidiness for future commits and nothing more, and it is worth saying plainly rather than letting a later reader assume the address was scrubbed.

The local setting was needed because a global identity set during an earlier planning session does not reach this repository — a local `user.email` overrides it, and this repo had one.

A further fact recorded at planning and unchanged by this build: the address also sits in tracked file *content* at commit `a42cd01`, inside an earlier version of the queue item that described it by naming it. Removing that occurrence needs the same rewrite, so the same reasoning applies and it stands as a recorded fact rather than an open question.

The verification this item calls for — checking the author metadata on the next commit — is satisfied by this session's own commit, which is the first made under the new setting.

**Files touched:** none. `git config --local user.email` changes `.git/config`, which is not part of the working tree.

**Routed to Captures:** [throughliner-doc-drift]
