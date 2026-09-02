# How Gboard corrects dictated speech, and what a keyboard can match

Researched 2026-09-02, for the question of whether Hexboard should correct the output of speech recognition, and what "as good as Gboard" would actually require.

## It is AI, and it has been since 2019

Gboard's on-device speech recognition is an all-neural end-to-end model — a recurrent neural network transducer (RNN-T), character-level and streaming, which emits words as you speak rather than after you stop. It shipped on Pixel in March 2019. The trained model was around 450MB and was quantized to roughly 80MB for the device, giving about a fourfold speed-up. Google reported it matching the accuracy of the server-side model it replaced.

## There is no separate autocorrect stage, and this is the finding that matters

The instinct is that recognition produces a transcript and something then fixes it, the way a keyboard's autocorrect fixes typing. That is not the architecture. Correction happens *inside* recognition: the model is trained to predict likely word sequences, so a phonetically ambiguous stretch is resolved by what makes a plausible sentence, before any transcript exists.

Where a second stage exists it is **language-model rescoring**, not spellchecking. The recogniser emits N best hypotheses for an utterance and an external language model re-ranks them. It chooses between whole candidate transcriptions the acoustic model already proposed; it cannot repair a word that appears in none of them.

So a corrector bolted onto a finished transcript is working with strictly less information than the recogniser had — it has lost the audio, and with it every alternative the recogniser considered and rejected. That does not make it useless, but it means it cannot close a gap in recognition quality, only tidy the output.

## What Gboard adds on top of recognition

Its advanced voice typing, which requires the Android System Intelligence app, layers on: automatic punctuation as you speak; automatic capitalisation of sentence openings; spoken editing commands; and biasing toward names in the user's contacts, so a name is recognised as a name.

**And it personalises from corrections.** Google's own help documentation states that transcripts of what the user says and types are saved on the device, and that changes made to dictated text are used to improve the feature for that user.

That last point is the one that bears on Hexboard directly. Part of Gboard's edge over a fresh recogniser is a per-user record of what that user says and how they fix it — the same class of stored writing this project has refused for predictive text, where the engine stores only words the user deliberately saved. **"Match Gboard's performance" and "store nothing about what the user says" are in tension, and the tension is real rather than a matter of implementation.**

## The unknown that decides the size of the problem

Gboard uses Google's own recogniser. Hexboard would call the public `SpeechRecognizer` on-device API. **Whether those are the same underlying engine on a Pixel is not established by any source found here**, and it is the question that decides whether a correction layer is needed at all:

- if the public API resolves to the same Android System Intelligence model Gboard uses, Hexboard starts at or near Gboard's recognition quality for free, and the remaining gap is punctuation, capitalisation and contact biasing rather than accuracy;
- if it resolves to something weaker, the gap is in recognition itself, which a post-hoc corrector cannot close.

This is answerable by experiment rather than by reading — dictate the same passages through Gboard and through a test app using the on-device API on the same handset, and compare. Nothing here was run.

## Published accuracy figures, and why they are weak evidence

Secondary sources put Google voice typing at roughly 85–92% accuracy in good conditions and 80–85% in a noisy environment. These come from blog comparisons rather than from a controlled benchmark, they do not state their corpus or their speakers, and word error rate is heavily dependent on both. Treat them as an order of magnitude, not a target to measure against.

## Frame assessment

- **TIME RANGE** — the architecture findings are from 2019–2020 published work and are the foundation rather than the current state; Google has shipped further voice-typing features since, including AI rewriting of rambling dictation, which was not investigated. SPEC states no time range for how long a design premise should hold.
- **PEOPLE** — this concerns anyone dictating on the keyboard, but the accuracy figures are for standard-accented English speakers, which is precisely the population the project's own voice work exists to move beyond. The figures therefore describe the best case for Gboard, not the case Hexboard cares most about.
- **FRESHNESS** — mixed, and this is the weakest part. The RNN-T architecture description is six years old. It is safe as an account of *where correction happens*, which has not changed in shape, and unsafe as an account of what Gboard ships today.
- **RISK IF WRONG** — building a transcript corrector on the assumption that Gboard has one would produce a component that cannot reach its stated target however well it is built. Warrants the experiment above before any design, not a red flag: nothing here exposes data. The stored-transcript tension is a separate matter and needs the user's explicit decision if performance parity is ever pursued seriously.
- **ALTERNATIVES** — three routes are visible and none was chosen here: rely on the platform recogniser and add only punctuation and capitalisation; add contact and saved-word biasing where the API permits it; or correct the transcript against the user's own saved-word list. Whether the on-device API exposes biasing at all was not researched.

## Sources

- [An All-Neural On-Device Speech Recognizer — Google Research](https://research.google/blog/an-all-neural-on-device-speech-recognizer/)
- [Streaming End-to-End Speech Recognition for Mobile Devices](https://arxiv.org/pdf/1811.06621)
- [Two-Pass End-to-End Speech Recognition](https://arxiv.org/pdf/1908.10992)
- [Use advanced voice typing features — Gboard Help](https://support.google.com/gboard/answer/11197787?hl=en)
- [Gboard on Pixel phones now uses an on-device neural network for speech recognition — VentureBeat](https://venturebeat.com/ai/gboard-on-pixel-phones-now-uses-an-on-device-neural-network-for-speech-input/)
