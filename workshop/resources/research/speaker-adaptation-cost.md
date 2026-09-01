# How much of your voice a speech model actually needs

Researched 2026-09-01 for `[personal-voice-model]`. The question came from a
challenge by the project's owner: an earlier session claim that adapting speech
recognition to one speaker needs about 30 minutes of audio was put against the
observation that faster methods exist. They were right, and this file records why.

## The correction

**The 30-minute figure describes full fine-tuning, and full fine-tuning is the
worst available option when data is scarce.** The literature is explicit that
full fine-tuning overfits on a single speaker's limited data, and that
parameter-efficient methods beat it there rather than merely approximating it.
Carrying the 30-minute number into a design would have set the enrolment burden
by the most expensive technique available.

**One distinction the challenge itself turned on.** The comparison offered was
ElevenLabs' half-hour standard, which is for voice *cloning* — synthesising
speech that sounds like a person. This is the opposite direction: adapting
*recognition* to a person. The techniques and their costs are different, so the
cloning figure does not transfer, but the instinct that cheaper methods exist
was correct for recognition too.

## The ladder, cheapest first

- **Zero-shot online adaptation via audio-textual prompts.** No enrolment at all.
  Speech and text embeddings from the current and a few preceding utterances are
  fused into a compact speaker prompt, adapting while decoding, at low latency.
  Published gains are small: 0.61% and 1.22% absolute word-error-rate reduction
  on English and Cantonese elderly-speech datasets.
- **Speaker-embedding conditioning.** An x-vector or comparable embedding is
  computed from a short sample and fed into the model through conditioning
  layers, with no training job at all. Described as adapting to speaker
  characteristics without extensive adaptation.
- **LoRA adaptation.** Updates roughly 3% of parameters. Reported to outperform
  fully fine-tuned models across adaptation methods at low data, specifically
  because it alleviates the overfitting full fine-tuning suffers there.
  Characterised as fast adaptation with 1–10% additional parameters and minimal
  computational cost, suitable for low-resource deployment.
- **Full fine-tuning.** The 30-minute-plus route, and the one to reach for last.

## What this does not settle

**Nobody has measured any of this for the case that motivated it.** The results
cited come from elderly and pathological speech datasets. The motivating case
here is second-language English speakers in a multicultural country, and no
figure in this file speaks to it.

**No packaged Android implementation of any rung was found.** The distance
between a paper reporting cheap adaptation and a keyboard performing it on a
handset is engineering, not a library call, and this file should not be read as
saying otherwise.

**Streaming remains unsolved and is keyboard-specific.** Whisper does not stream,
so transcription arrives in a block when the speaker stops rather than word by
word. Vosk streams and is small enough for a phone but is less accurate to begin
with — which is the thing adaptation is meant to fix, so the two constraints
pull against each other and the resolution is not in this research.

## Frame assessment

- **TIME RANGE** — Not applicable to a period the product addresses. What matters
  instead is publication recency: the zero-shot prompt work is from June 2026, so
  this is a field moving quickly and a finding whose ladder may gain a rung
  within a year.
- **PEOPLE** — Applies poorly to the people it was researched for, and this is
  the finding's weakest point. Its evidence base is elderly and pathological
  speech; the intended beneficiary is a second-language English speaker. The
  mechanisms plausibly transfer, the measured gains do not.
- **FRESHNESS** — Amended on a cycle, and fast. Re-check before any build starts
  rather than trusting this file's ladder.
- **RISK IF WRONG** — Moderate and self-announcing. Being wrong means an
  enrolment that delivers less improvement than promised, which shows up as a
  keyboard that still mishears its user — visible immediately, not silently.
  What it does not warrant is designing a long enrolment on this file's
  authority: the cost of being wrong lands on the user's half hour.
- **ALTERNATIVES** — Considered: all four rungs above. Not investigated:
  training a small model from scratch on the user's voice, which nothing
  suggests is viable on a handset; and commercial adaptation services, which are
  ruled out by the design decision that audio never leaves the device rather
  than by any finding here.
