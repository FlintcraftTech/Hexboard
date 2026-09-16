# 770b778 — [personal-voice-model] the research re-checked, one unknown split in two, and the item dated out to 2027-03-12

Session of 2026-09-12, 12:54.

**This entry also records a correction Claude made mid-session and should not have needed to.** Presenting the item, Claude put its central architectural choice — whether Hexboard would carry a speech model of its own — to the user as an open fork. It was not open: the choice was settled on 2026-09-01, is written in the item's own prose, in SPEC's voice-adaptation principle and in the cited research file, and the user caught it in one line. The mechanics of how that happened are in the method report sent the same day and summarised in the chat-level record.

**The re-check the research file itself asks for was then run, and it moved one of the three unknowns.** That file's blunt line was that no packaged Android implementation of any adaptation rung was found, and that line hides a split. *Running* an adapted model on Android now has a named route — sherpa-onnx, a packaged on-device runtime with Android support that streams and runs fine-tuned Whisper models converted to ONNX — which also softens the streaming-versus-accuracy tension the file recorded, since a streaming runtime and a fine-tuned Whisper are not the either-or it implied. *Adapting* a model on the handset still has no packaged route at all, and that is the half this item turns on, because training on the phone is the choice the whole design rests on. Written into `workshop/resources/research/speaker-adaptation-cost.md` with its index line.

The third unknown is untouched: the published gains are measured on elderly and pathological speech, not on the multilingual speakers this exists for.

**So it is better understood and still not designable, and it was dated rather than left to recur.** `Not before: 2027-03-12` — six months, matching the pace the research file says this field moves. The old `Blocked by: [in-keyboard-voice-input]` came off in the same move: that item is processed and cleared, so the field held nothing back, and the date is the honest statement of what is actually being waited on. The date was the user's to approve, because a date on a capture is the one hold that removes an item from view without anything resolving.

**Queue changes:** [personal-voice-model] gained a `Not before:` date and lost its blocker line; the sherpa-onnx finding written into the research file and its index.

**Work processed:** kept in Unprocessed, dated — [personal-voice-model].
