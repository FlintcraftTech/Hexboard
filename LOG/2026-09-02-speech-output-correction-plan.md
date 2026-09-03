# 37384e4 — An observation about Gboard correcting a dictated homophone, and why it narrows this item rather than settling it

The user dictated a passage, switched to typing by hand, and later noticed "rose" where they had meant "rows". Tapping the word offered "rows" — a homophone correction on text dictated during a session that had since become an ordinary typing session with the microphone off. Their question: does Gboard tag which words arrived by voice and keep that tag afterwards, or would it have offered the same correction on typed text?

Looked up the same day, and it does not settle it. What was found points the second way: Google's own description of the proofread feature says it checks "any typed, pasted or voice-dictated text", naming all three origins together, so an origin-blind proofreader explains what the user saw without any provenance tracking at all. Nothing found describes Gboard tagging words by how they arrived. That is recorded as *not found* rather than as established absent, and the wording is the user's doing: Claude first called the observation evidence for one explanation, and they pointed out it is ambiguous between two.

Why it matters here. An origin-blind corrector needs no record of what was dictated, which is exactly the storage this project refuses everywhere else. The tension this item records — that matching Gboard might mean keeping a per-user transcript — is therefore narrower than it looked, at least for the homophone half of the job. It does not touch the recognition-quality half, which is still what [recogniser-gap-comparison] has to measure.

The affordance the observation depends on turned out to be a gap of its own and is filed separately as [tap-word-alternatives].

**Queue changes:** the finding written into the item, which stays in Unprocessed held against [recogniser-gap-comparison]. The ordering with [tap-word-alternatives] is written on this item.

**Work processed:** amended, not processed — [speech-output-correction].
