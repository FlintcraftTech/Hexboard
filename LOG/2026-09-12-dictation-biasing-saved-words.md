# [HASH] — [dictation-biasing-saved-words] designable at last, because the saved-word list it hands the recogniser now has a design

Session of 2026-09-12, 12:54.

The item had stayed a capture for one reason: [predictive-saved-words] was itself undesigned, so there was no list to hand the recogniser and what a build changes could not be stated. That reason went the same session, when the save gesture and the list surface were settled — recorded in `2026-09-12-predictive-saved-words.md`.

The mechanism was already established: Android's `EXTRA_BIASING_STRINGS` from API 33, a list of strings the recogniser leans toward, so the influence happens inside recognition where the research says it has to. Nothing about what was said is stored — it needs a list of words, and the saved-word list is already exactly that.

One thing settled here rather than left to the build: the whole list is sent, most-recently-saved first. Android's documentation states no limit, so if the platform takes fewer than are offered, recency decides which survive — the best available proxy for what someone is about to dictate. Whether a cap exists at all is something the build finds out, and nothing depends on the answer beyond the ordering.

`EXTRA_ENABLE_BIASING_DEVICE_CONTEXT` stays off, and the entry is explicit that this is a privacy choice rather than a rule: SPEC's no-proper-nouns rule governs the autocorrect dictionary, where the harm is a typed word turned into a name, and dictating a friend's name correctly is a benefit rather than that harm. It is left off because Android never says what "device context" contains. Its absence is asserted in a test rather than a comment, being one line away from reversal by accident.

**Queue changes:** [dictation-biasing-saved-words] designed out and moved into Processed, held against [predictive-saved-words] and [in-keyboard-voice-input]; SPEC's voice principle gained the biasing sentence at the /done gate.

**Work processed:** kept — [dictation-biasing-saved-words].
