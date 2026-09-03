# 37384e4 — Voice input designed out to a buildable item: the microphone at the right end of a new row above the keys, released by lifting off the screen

This was the last of the big undesigned features to have a stated reason for sitting in Unprocessed, and the reason was one question: where the microphone control lives. The answer settles the file list, because a key declared in the layout config reaches `resources/key-layout.json`, the manifest rules and every language's layout, while a board control reaches none of them.

The user settled it: the microphone sits at the right-hand end of a row above the keys — the strip where autocorrect suggestions go. So it is a board control and no language config is touched by it.

Three alternatives were read out of the code and lost. A key in QWERTY's row 3 loses outright: columns 0–9 are all occupied — shift, `?`, `,`, `!`, space, `'`, space, `"`, `.`, `-` — so a microphone key there evicts punctuation, which SPEC's manifest rules make a deliberate loss rather than a placement. A key on RARE or SYMBOLS, where row 3 does have gaps, defeats the feature's own purpose, since a microphone you must swipe to reach is most of the way back to switching keyboards. And a long-press on an existing key spends no pixels but has no free hold to spend, accents and backspace repeat having taken them.

Two further decisions closed the item. How tall the row is, and what "fully released" means. The second is the more interesting: the user chose that release means the finger leaving the screen, with no distance threshold at all — once the hold has begun, that pointer owns the recording until it lifts, wherever it has wandered. Drift then cannot cut a sentence short at any distance, which is what the growing control was introduced to prevent, and it removes a magic number rather than choosing a value for one. The one case needing its own answer is Android cancelling the pointer outright, where recording stops and whatever was transcribed is kept.

The row itself left this item later the same session, into [suggestion-strip], once [persistent-clipboard] turned out to need it too; that entry carries the reasoning and the row's own design. This item now adds the microphone to a row it does not build.

What still holds it is [recogniser-gap-comparison] — the user's dictation test on the phone — because whether the platform recogniser is good enough to build a thumb-held control around is a premise this whole design rests on rather than a detail.

**Queue changes:** moved from Unprocessed into Processed below the readiness line. Its hold moved off the shipped [first-installable-build] onto `Blocked by: [recogniser-gap-comparison], [suggestion-strip]`. SPEC's voice principle gained where the control lives, that it is not a key, and what release means.

**Work processed:** kept — [in-keyboard-voice-input].
