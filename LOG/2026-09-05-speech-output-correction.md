# 5f9d97e — [speech-output-correction] narrowed from a correction stage to a flag on the recognition request, after its blocker came back saying there is no accuracy gap

[recogniser-gap-comparison] was driven on 2026-09-04 and found the two recognisers basically identical, the single difference being that Gboard adds punctuation. That removes the recognition-quality half of this item entirely — the gap it was written to close does not exist on read speech — and leaves formatting.

The drive record left open whether punctuation is something a finished transcript can recover. It is not the question: Android added `EXTRA_ENABLE_FORMATTING` in API 33, with quality and latency options and a companion for suppressing trailing punctuation on partial results. So the punctuation happens inside recognition, which is where the research said it has to — Gboard has no separate autocorrect stage either. Quality was chosen over latency because the microphone is press-and-hold with one utterance per hold, so nobody is watching for the next word.

The same API level answered the item's second route, which it had recorded as unresearched: `EXTRA_BIASING_STRINGS` exists, so biasing recognition toward the user's own vocabulary is possible and needs no stored transcript. That is split out as [dictation-biasing-saved-words]. The storage tension this item carried — that matching Gboard might mean keeping a per-user record of what someone says — is gone: a flag on a request holds no transcript.

The cost is stated rather than glossed: these extras are API 33, where on-device recognition itself starts at 31, so devices on 31 and 32 get dictation with no punctuation. At the close, the spec-sync gate caught that SPEC's voice principle never said dictated words arrive punctuated at all, and the sentence was written.

**Queue changes:** [speech-output-correction] rewritten and moved into Processed below the line, blocked by [in-keyboard-voice-input] and placed after it; [dictation-biasing-saved-words] created as a capture blocked by [predictive-saved-words]; SPEC's voice principle gained a punctuation sentence at the close.

**Work processed:** kept — [speech-output-correction]. Filed — [dictation-biasing-saved-words].
