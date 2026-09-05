# [HASH] — [dictation-biasing-saved-words] split out once the on-device API turned out to permit biasing at all

Split out of [speech-output-correction] on 2026-09-05; that item's record for the same date carries the reasoning.

Worth finding from this slug: Android's API 33 carries a **second** biasing extra, `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT`, which would plausibly bring the phone's contacts into recognition. It is deliberately not set. The user asked whether contacts could be avoided simply by not enabling it, and they can — the two are separate switches. The reasoning that goes with the answer: SPEC's no-proper-nouns rule governs the autocorrect dictionary, where the harm is a typed word being turned into a name, and dictating a friend's name correctly is a benefit rather than that harm. So nothing forbids the device-context switch; it is off because the conservative default matches everything else here, and because Android never documents what "device context" contains.
