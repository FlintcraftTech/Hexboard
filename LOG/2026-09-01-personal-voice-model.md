# [HASH] — Voice adaptation filed as the real answer to accents, after a claim of this session's was challenged and found wrong

This is the item the accent picker lost to. An accent list reaches whoever matches one of its entries; adapting to the individual voice is the only approach that does not care what the speaker's first language was, which is the case the user raised.

Three choices are theirs and each carries the alternatives it beat. **Training happens on the phone**, over a server — rejected because uploading recordings of someone's voice is what the rest of this keyboard refuses — and over the user's own computer, rejected because it asks a phone user to run a training job on a laptop. **Enrolment audio is destroyed once adaptation has run**, over keeping it on the device.

That last choice has a consequence written into the item rather than glossed: accumulating enrolment across short sittings, which had been proposed in the same conversation, is not available, because accumulating requires keeping recordings between sittings. So either one sitting is enough, or adaptation runs incrementally on material then destroyed — and whether that degrades what was already learned is unchecked. It is recorded as a real open question for the build.

**A claim made earlier in this session was wrong and the user caught it.** Claude had put speaker adaptation at around 30 minutes of clean audio and designed the enrolment question around that figure. Challenged, and researched properly, the figure describes full fine-tuning — which at low data is the *worst* available technique, because it overfits a single speaker. LoRA adaptation updates roughly 3% of parameters and beats full fine-tuning in exactly that regime; speaker-embedding conditioning needs seconds and no training job; one 2026 method adapts while decoding with no enrolment at all. Carrying the wrong number forward would have set the user's enrolment burden by the most expensive technique available. Filed as `workshop/resources/research/speaker-adaptation-cost.md`.

One distinction the challenge turned on, recorded because it is the source of the wrong number generally: ElevenLabs' half-hour standard is for voice cloning — synthesising speech that sounds like a person — not for adapting recognition to them.

Three things keep this in Unprocessed, none of them a decision anyone can make at a desk: no packaged Android implementation of any rung was found; Whisper does not stream while the models that do are less accurate, which is the thing adaptation is meant to fix; and the published gains are measured on elderly and pathological speech rather than on the multilingual speakers this exists for.

**Queue changes:** [personal-voice-model] filed in Unprocessed, held against [in-keyboard-voice-input], carrying a cleared red flag and a research citation. SPEC gained the voice-adaptation principle.

**Work processed:** kept in Unprocessed, held — [personal-voice-model]. Red flag cleared by design plus informed consent.
