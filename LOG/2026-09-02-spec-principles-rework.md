# 73231d1 — SPEC's principles measured rather than judged, and the rebalancing filed as work after the count showed a deferred feature outweighing the project's whole differentiator

[spec-coherence-readthrough] asked the user for four things, and two of them turned out to need no judgment about what the user meant — only a careful read. Those two were done in this planning session under the resolve-now rule and became this item; the reasoning for the split is in the [spec-coherence-readthrough] entry of the same date.

**The proportion finding, measured rather than asserted, which is why it is worth acting on.** Word counts of the Principles bullets: the perceptual wedge, which SPEC calls inviolable and which is the project's entire differentiator, runs 22 words. Predictive text, deferred and unbuilt, runs 309. Key inventory runs 269, layout-per-language 198, and clipboard, voice input and voice adaptation 119, 116 and 89. Every other bullet is under 30. Inside the list a reader skims, a deferred feature outweighs the reason the project exists by fourteen to one.

The count alone overstates it, and that fairness is recorded on the item so the fix does not overcorrect: the wedge is also described at length in "How it works", so the bullet is not its only home. What is wrong is the balance within one list.

**Five pieces of machinery and history to come out**, each checked against where it already lives rather than assumed to be duplicated. The predictive-text principle's three sentences on deriving the neighbour table at runtime are near-identical to text already on [uniform-neighbours-predictive], confirmed by reading it, so that is a deletion rather than a relocation. The key-inventory principle's config filenames and field list already sit in the config's own `about` text and in README.md. The uppercase-glyph line ends in implementation plus how the decision was made. Two sentences about the prototype are history. And the wedge paragraph carries a re-litigation instruction, which is CLAUDE.md's job and which CLAUDE.md already does.

**What was checked and left alone**, recorded so the rework does not go further than it should: the clipboard, voice-input and voice-adaptation principles are behavioural throughout, and the accessibility line's justification prevents a real error about accessibility services bypassing touch routing.

Stated on the item because the failure mode is obvious: this is a rewrite for balance and admission, never for length. A true sentence about a live feature is not evicted for being long, and no target figure is set.

**Queue changes:** created from a verification pass run in-session, cleared to run, and placed ahead of [spec-coherence-readthrough], which is now held against it.

**Work processed:** kept, cleared to run — [spec-principles-rework].
