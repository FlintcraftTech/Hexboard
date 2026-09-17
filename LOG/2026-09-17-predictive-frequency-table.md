# 88b95f2 — A frequency table filed with its licence question closed at source, then held until the engine ships

Recorded 2026-09-17 13:59. Sits inside the commonness work recorded under [uniform-neighbours-predictive], which is what gave this a concrete job.

SCOWL's bands are coarse and one holds about 38,000 words, so "the commonest candidate" often resolves to a band rather than a word and both the correction engine and the completion slots fall back on taking the shorter word. A real frequency table is what would replace that, and this entry is it.

**The blocker was removed by doing the read rather than deferring it.** The entry said nothing could be specified until someone got past the proof-of-work bot challenge in front of the Leipzig Corpora Collection and read the licence at source — second-hand sources said CC BY 4.0. A plain page fetch still returns only the challenge; the browser pane solves it and reaches the content. The Terms of Usage page says the downloadable text corpora are CC BY, which confirms the sources, and adds a distinction they missed: the project's *other* data is CC BY-NC, so the licence depends on which artifact is taken. The page names no version where the sources said 4.0, so that much stays second-hand, and automated queries are prohibited except through the project's own web services.

What holds it now is evidence rather than permission. Nobody has typed on the engine, because it is not built: if ordering by band and then by the shorter word reads acceptably, this ships about a megabyte and a licence obligation for nothing, and if it reads badly, *how* it fails is what would say which corpus to pick — news or web-collected text disagree about what is common, and the research file already names that as a gap in SPEC.

The hold is written `Blocked by: [uniform-neighbours-predictive] until built`, the suffix being what makes it wait for the engine to exist rather than to be agreed — the field this project reported as defective on 2026-09-12, used here for the case it was asked for.

One step is explicitly not done: fetching an actual corpus package is a download rather than a page read, nobody has tried one, and the owner noted he could do it himself. That is recorded on the item as the fallback and becomes a `[user]` item at that point rather than now.

**Queue changes:** [predictive-frequency-table] filed in Unprocessed and held; `workshop/resources/research/word-list-licence-and-frequency.md` amended with the at-source read and its index line updated; `TOOLS.md` gained the fact that the browser pane gets through that kind of challenge.

**Work processed:** kept as a held capture — [predictive-frequency-table].
