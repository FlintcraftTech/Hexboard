# [HASH] — [files-line-format-drift] filed from the rescan: every queue entry names its changed files under a heading the digest cannot read, so three of its checks — one of them a safety check — have been silently dead since the queue began

Session of 2026-09-18, 15:42.

Found at this session's rescan by noticing that the digest's "Files named by two or more items" block reported zero while `KeyboardPanel.kt` is named by five cleared items. The first account of it was a guess at the cause; reading `scripts/queue_digest.py` turned it into a fact.

**What the digest matches.** `FILES_LINE_RE` is `^\**Files\b[^:]*:` — a line whose first word is Files, followed by a colon — and the reader then takes that line plus the bullets beneath it. Every entry in this queue writes `**What the build changes.**` instead, so the reader never opens on any of the thirty-four entries. The `Reads but does not change:` lines these entries also carry are unaffected, `READS_LINE_RE` matching them already.

**Three checks read that line and all three are inert here**, which is what turned a cosmetic observation into work: the merge-candidate block, which exists so two entries touching one file can be settled together and has reported nothing for this project since the queue began; the placement flag for an entry whose Files line names nothing or names its own design's output; and — the one that matters — the flag for a cleared entry whose Files text names `QUEUE.md`, because queue content is planning work a build may not write. That last one fails open. It cannot refuse what it cannot read, so an entry telling a build to edit the queue would pass it silently.

**The drift is ours rather than the method's**, and saying so is what kept this out of the feedback channel. The procedure refers to the Files line throughout; this project adopted a prose heading early and never carried it back. The observation that nothing warns when a whole queue has no Files line at all is a fair one about the method, and is deliberately not this item's business.

**Filed `[freeform]` rather than as a build, for a reason the method states directly:** amending a queue entry's own wording is planning work and can never clear as a build, so a run would skip it forever with nothing reporting why. It is also too large for a planning conversation to carry entry by entry.

**Placed after [nested-wrap] rather than before it**, because that item rewrites the path inside these same bullets when the product moves down into `hexboard/` — doing this first means editing the same lines twice. Placement and prose rather than a blocking line: both items are done by hand, so a field would buy nothing and would hide this entry from view. The reciprocal sentence was written into [nested-wrap].

The `CLAUDE.md` sentence rides in this item rather than being filed separately, because without it the drift returns on the next entry written, and a planning session may not write that file — the same constraint that produced [claude-md-swipe-wording] and [settings-steps-name-a-search] as items of their own.

Two other rescan findings were corrections rather than work and were made in the session: a sentence in the research file filed the same day, already stale because it described [selection-formatting-markers] as filed rather than built; and a duplicated filing stamp on [licence-out-of-readme] dating from 2026-09-17, the same mistake this session made twice more and caught on its own entries.

**Queue changes:** [files-line-format-drift] filed and moved into Processed, cleared, placed second in the region after [nested-wrap], which gained the reciprocal ordering sentence; `workshop/resources/research/keyboard-applied-text-styling.md` corrected; the duplicated filing stamp removed from [licence-out-of-readme].

**Work processed:** kept — [files-line-format-drift].
