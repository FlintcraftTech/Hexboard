# [HASH] — [verify-switch-access-ondevice] split out of the TalkBack item after the obvious way to drive it turned out to hide the board, and given the two questions only a keyboard raises

Recorded 2026-09-04 at 17:00. This session ran across 2026-09-03 and 2026-09-04.

Surfaced by the rescan. Rewriting [verify-a11y-ondevice] earlier in the session had cut switch access from its walkthrough and said it should be filed separately if wanted — and then nothing filed it. SPEC's accessibility bullet names screen readers **and switch access** together and says the per-key nodes are a separate requirement from nearest-centre routing, so a TalkBack pass proves half the promise.

**The obvious route defeats itself, which is what shaped the item.** The usual cheap switch is a Bluetooth keyboard, and the user has one — but Hexboard inherits `onEvaluateInputViewShown()` and hides whenever a hard keyboard connects, so that route leaves you scanning a board that is not on screen. Android's Switch Access takes three switch sources: an external keyboard, the phone's own buttons including the volume keys, or Camera Switches driven by facial gestures. The volume keys need no hardware and connect nothing, so nothing hides. That collision is now written on [physical-keyboard-handover] as well, since running the two in one sitting would reproduce it.

**The user's question is what made the item honest about its own reach.** They asked what happens for a user whose switch is not set up this way. The answer is that the switch source sits upstream of everything Hexboard does — whatever is pressed, Android walks the same accessibility tree with the same machinery, and Hexboard never sees the switch — so reachability, order and labelling are identical whatever someone presses, and the volume keys stand in for the pressing rather than for the scanning. The item says plainly that it tests nothing about anyone's actual switch hardware.

But the question also found something the two-switch test hides. Switch Access can run on one switch, where the highlight advances on a timer and the press only selects. A letter panel carries around thirty keys, so on a timer the highlight could take a very long time to reach a key near the end — a problem a keyboard has and most apps do not. The walkthrough now covers both modes, and asks for the auto-scan time to a far key as a number.

The part with real information in it is the scan order. Odd columns sit half a key lower, so the board has no straight rows, and a scan traversing by position may hop down-and-up instead of running along each row. That is a property of this project's own geometry rather than a generic accessibility question, and nobody has looked. What a bad result would mean is recorded rather than designed: grouping the board for scanning, which would be its own work.

**Queue changes:** [verify-switch-access-ondevice] created and cleared to run, placed with the other `[user]` items at the end of the cleared region. [verify-a11y-ondevice]'s note about switch access being left out now names it and says which half each item covers; [physical-keyboard-handover] gained the do-not-run-together line.

**Work processed:** [verify-switch-access-ondevice] kept into Processed, cleared to run. Surfaced by the rescan and processed in the same session rather than filed as a capture.

**Advisory:** not needed — the close's recommendation names no single item to start from.
