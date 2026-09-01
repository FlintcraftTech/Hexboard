# Voice input for a keyboard on Android: what the platform gives, and why locale is not accent

Researched 2026-09-01 for `[in-keyboard-voice-input]` and the accent picker that
came out of it. Two questions were live: can a keyboard dictate without the user
switching to another keyboard, and can it offer a choice of accent.

## What the platform gives an input method

**An input method can run recognition itself, so no keyboard switch is needed.**
`SpeechRecognizer.isOnDeviceRecognitionAvailable(context)` and
`SpeechRecognizer.createOnDeviceSpeechRecognizer(context)` are available from
API 31, and from API 33 on-device recognition is forced — the call fails rather
than silently falling back if no local engine is present. The `RECORD_AUDIO`
permission is required.

**Two existing keyboards are built this way**, WhisperInput and Transcribro, so
this is a trodden path rather than an inference from the API surface.

**Practical requirements named in the documentation:** set the recognition
listener before dispatching any command or no callbacks arrive, and call
`destroy()` on the recognizer when finished.

**The API-level gap matters for this project.** Hexboard's minimum is API 26 and
the on-device recognizer starts at 31, so devices in that band have no on-device
route at all. The design decision taken from this — voice input is absent there
rather than falling back to the network recognizer, which would send audio off
the device — is recorded on the queue item, not here.

## Why locale selection does not answer an accent complaint

**What is selectable is a locale, and the recognisable English set is organised
by where a variety of English is spoken natively** — around en-AU, en-CA, en-GB,
en-HK, en-IE, en-IN, en-NG, en-NZ, en-PH, en-SG, en-US, with Android's own
on-device coverage narrower than that and varying by handset and installed
language packs. From API 33 there are recognition-support APIs that report what a
given device actually has.

**That list helps whoever matches an entry and nobody else.** It has Indian
English and Nigerian English; it has no entry for a second-language English
speaker whose accent comes from their first language rather than from a
national variety. In a multicultural country that is a large share of the
people the feature is for — which is the reason the accent picker was dropped
in favour of `[personal-voice-model]`.

**Accent-specific open models exist and the set is thin.** Fine-tuned Whisper
variants for British versus American English, some UK regional dialect work, and
models trained on GLOBE, a multi-accent English corpus. Real, but nothing
resembling a menu of accents, and weakest exactly at second-language English.

## What this does not settle

Which English locales any particular device has installed — that is a runtime
question the recognition-support APIs answer on the handset, not something this
file can state. Whether on-device recognition genuinely keeps audio local, which
is what the API claims and what a build should confirm before the README repeats
it. And nothing here was run: this is documentation and project READMEs, read on
2026-09-01.

## Frame assessment

- **TIME RANGE** — Not applicable to a period the product addresses. The
  relevant range is Android version coverage, which is stated above and is the
  live constraint.
- **PEOPLE** — The first half applies to every user. The second half is
  specifically about people the locale list does not serve, which is the group
  that prompted the question, so the frame matches the need here better than
  most findings in this folder.
- **FRESHNESS** — Android APIs are additive and stable, so the capability half
  ages slowly; what moves is device coverage, which improves as handsets update.
  Re-check the API-level floor if the project's minimum SDK ever changes.
- **RISK IF WRONG** — Low for the capability half, since a build discovers a
  wrong API claim immediately at compile or first run. Higher for the coverage
  claim: designing on the assumption that a given locale is present on a user's
  phone would fail silently for that user, which is why the runtime support APIs
  exist and should be used rather than a hard-coded list.
- **ALTERNATIVES** — Considered and rejected: the network recognizer, on privacy
  grounds recorded on the queue item; the accent picker, on coverage grounds
  above. Never investigated: whether a third-party on-device engine could be
  bundled instead of the platform recognizer, which would change the API-level
  floor entirely and is the obvious thing to look at if the API 26–30 gap ever
  matters enough.
