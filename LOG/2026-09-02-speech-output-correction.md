# [HASH] — Gboard's speech correction researched, and the feature reshaped by what the architecture turned out to be

The user raised correcting dictated speech as work separate from the keyboard's own autocorrect, with a stated target of matching Gboard, and asked for the question to be researched rather than assumed: in their words, it looks like AI but might not be.

It is AI, and has been since 2019 — Gboard's on-device recogniser is an all-neural end-to-end model, character-level and streaming, quantised to run on the phone. The useful finding is the shape rather than the fact. **There is no separate autocorrect stage.** Correction happens inside recognition, where the model predicts likely word sequences and resolves an ambiguous stretch of sound by what makes a plausible sentence, before any transcript exists. Where a second stage exists it is language-model rescoring, which re-ranks the recogniser's own N best guesses at the whole utterance; it chooses between candidates the acoustic model already proposed and cannot repair a word appearing in none of them.

The consequence for the feature is direct: a corrector applied to a finished transcript has strictly less to work with than the recogniser had, because the audio is gone and with it every alternative considered and rejected. It can tidy output and it cannot close a gap in recognition quality. That is why the measurement comes first.

**A tension the item records as the user's decision rather than an implementation detail.** Google's own documentation says transcripts of what the user says and types are saved on the device and that corrections improve dictation for that user. Part of Gboard's advantage is a per-user record of how someone speaks and how they fix it — the class of storage this project refuses everywhere else, predictive text keeping only deliberately saved words and enrolment audio being destroyed after adaptation. Matching Gboard by the same means would reverse that, and nothing was decided.

The unknown that sizes the whole problem: Gboard uses Google's own recogniser while Hexboard would call Android's public on-device one, and nothing found establishes whether those are the same engine on a Pixel. If they are, the remaining gap is punctuation, capitalisation and name biasing rather than accuracy. That is answerable by experiment, which is why the work split three ways.

Research: `workshop/resources/research/gboard-speech-correction.md`, which records that the architecture papers are from 2019 and 2020 — sound on where correction happens, not a description of what Gboard ships today.

**Queue changes:** three items filed — [ondevice-recogniser-test] and [recogniser-gap-comparison] into the held region of Processed, and this design item into Unprocessed behind them; [in-keyboard-voice-input] gained the microphone-permission cleanup and the comparison's bearing on it.

**Work processed:** kept in Unprocessed, held — [speech-output-correction].
