# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
**Lifted on 2026-09-09.** Both blockers shipped in the build run of 2026-09-05 to 2026-09-09: `KeyGeometry.neighbourTable` derives the six-neighbour sets, and the SCOWL word list is generated and bundled. Both are covered by unit tests, and all 50 unit tests passed on the Pixel 6 on 2026-09-09, so the foundations are built *and* verified rather than merely written.
Autocorrect for Hexboard: when the user presses space, the word just finished is compared against the shipped dictionary and, if it is not already a word, replaced by the closest match.

Captured by you, sharpened on 2026-08-06, reshaped by your decisions of 2026-08-20 and 2026-09-01, and designed out on 2026-09-04. The full decision history is in the records under this slug; what follows is what a build needs.

**Rewritten on 2026-09-04, when you decided to design this now.** It had sat in Unprocessed saying it could not be designed because SPEC held predictive text until the first working keyboard existed. It does exist — Hexboard was installed on the Pixel 6 and typed what it was aimed at on 2026-09-03 — so the gate had been met and nobody had looked at the item in that light. Two foundations were split out ([predictive-neighbour-table], [predictive-dictionary-bundle]) and the saved-word store was split out as [predictive-saved-words]; this item is now the correction engine alone.

**Why the engine is built rather than borrowed, which is the whole argument.** Closeness is measured with the key geometry: substituting a key for one of its six neighbours is a near-miss and costs little, any other substitution is a real difference and costs a lot. That is a neighbour-weighted edit distance, and no general-purpose library can compute it, because none of them know which keys touch which. In a hexagonal tessellation every interior key sits the same distance from each of its six neighbours, so the confusion set is uniform in a way a rectangular keyboard's is not — horizontal neighbours there are closer than diagonal ones.

**Your reason for wanting it, which the design answers.** Your complaint about ordinary autocomplete is that getting the first letter wrong is far worse than getting a later one wrong, because a prefix lookup reads words forwards and so treats the first letter as certain. Correcting at the word boundary dissolves that: the whole word is in hand, so no letter is trusted more than any other and the first-letter case stops being special rather than being compensated for.

**Three guards, all in SPEC.** A word already in the dictionary exactly is never corrected, or the engine mangles deliberate spellings. A correction is undone by pressing backspace immediately after it lands, because silent unrevertable autocorrect is the most resented behaviour keyboards have. And nothing correctly spelled is ever changed on the user's behalf — your principle of 2026-09-03, raised from Gboard adding an apostrophe to every "its"; on this engine it was already true twice over, since the dictionary guard covers "its" and an apostrophe insertion is not a neighbour substitution.

**Ties are broken by the word list's own size level**, which is a coarse commonness ranking, and no second frequency source is bundled — settled with [predictive-dictionary-bundle] on 2026-09-04, where the reasoning and the rejected sources live.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/Autocorrect.kt` — new. The neighbour-weighted edit distance between a typed word and a candidate, and the choice of closest match with the size level breaking ties. Pure functions over a word, a neighbour table and the word list, so they can be checked without a running input method.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — accumulates the current word as keys are committed, fires the correction when a space arrives, replaces the committed text where a correction is made, and undoes it when the very next key is backspace. A word already in the list is committed untouched.
- `android/app/src/test/java/tech/flintcraft/hexboard/AutocorrectTest.kt` — new, carrying the first half of the observation below.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/AutocorrectUiTest.kt` — new, carrying the second half.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` for the neighbour table and `WordList.kt` for lookups, both built by this item's blockers.

**The observation that shows it landed:** `AutocorrectTest` passes, asserting that a word one neighbour-substitution from a dictionary word is corrected to it, that a word one non-neighbour substitution away is left alone, that a word already in the list is never changed, and that between two equally-near candidates the one at the commoner size level wins. `AutocorrectUiTest` types a near-miss followed by a space and asserts the corrected word is in the field, then presses backspace and asserts the original is back. Nothing on this machine can run either, so running them is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** Correcting per keystroke, searching forwards from the pressed key and its six neighbours and ranking by plausibility times frequency — this was the 2026-08-06 design and it lost to word-boundary correction on 2026-08-20, because it exists to work around reading a word before it is finished, which correcting at the boundary simply removes; it was also far more machinery, needing a per-keystroke candidate interface. Adopting an existing library — none knows the key geometry, which is the entire source of the advantage. Learning from what the user types — designed out on 2026-08-20 and now [predictive-saved-words]' business rather than this item's. Correcting grammar or punctuation — barred by SPEC.

**A reasoning this item is the home of, recorded because it is the standing defence of the board's routing.** You asked on 2026-09-02 whether the touch targets should be circles that kiss, leaving dead space between them. They must not. This engine repairs a *wrong* character cheaply, because the six-neighbour geometry says which letter was probably meant; it cannot repair a *missing* one, because a deletion carries no signal about which letter failed to arrive or where. Dead space would produce exactly the failure this engine is worst at in order to avoid the one it is best at. Full-coverage nearest-centre routing is what avoids it, and that reasoning now sits in `KeyGeometry.nearestCentre`'s own documentation as well.

Cites research: `workshop/resources/research/word-list-licence-and-frequency.md`.

Rests on: the neighbour table and the word list, both built by this item's two blockers rather than assumed; SPEC's three guards, read from `SPEC.md` on 2026-09-04; that this project's own predictive-text deferral has been met, established by the install of 2026-09-03 recorded in [install-and-enable-on-pixel].

[predictive-saved-words] is held against this item, being the store the engine would consult once it exists. [tap-word-alternatives] is a different mechanism and this engine cannot reach it: it would never touch "rose" for "rows", both being real words several keys apart. Those orderings are written on all three entries.

#### Tapping a finished word to see alternatives, which nothing here can do [tap-word-alternatives]
**Lifted on 2026-09-09.** The word list shipped in the build run of 2026-09-05 to 2026-09-09, and [verify-this-runs-build-on-device] was driven to its end on 2026-09-09 — the app builds, installs, types and switches layouts on the Pixel 6, so the row above the keys this item fills is compiled and running rather than written blind.
Tap a word already sitting in the text and the row above the keys offers replacements for it; tap one and it takes the word's place.

Captured by you on 2026-09-02, from your own phone: you tapped a mistyped word and Gboard offered the right one. The word was "rose" where you had meant "rows". Designed out on 2026-09-04 after the research this had been waiting on.

**Why the predictive engine cannot cover this**, which is why it is a separate feature rather than a corner of one. [uniform-neighbours-predictive] corrects on the space bar using a neighbour-weighted edit distance, and its first guard is that a word already in the dictionary is never corrected. "rose" is a real word, and it is two edits from "rows" rather than one neighbour substitution. So by the time a word sits finished in the text, either autocorrect already fixed it or it is exactly the kind of word geometry cannot reach.

**Where the alternatives come from, settled on 2026-09-04: homophones derived from the CMU Pronouncing Dictionary.** CMUdict maps words to phoneme sequences, so words with identical sequences are homophones by construction — a grouping rather than a curated list, which means there is no list to trust and none to license. "rose" and "rows" are both `R OW1 Z`. Its licence makes use for any research or commercial purpose completely unrestricted and asks that its origin be acknowledged where it is redistributed, which is the same shape as the Unicode and FlorisBoard notices `README.md` already carries.

**Two routes were closed by the same research, and both are the obvious thing to try again.** A language model over the sentence is unavailable: Android reaches one through Gemini Nano and the ML Kit GenAI APIs, whose supported-device list starts at the Pixel 9 — this project's handset is a Pixel 6 and SPEC's minimum is API 26, so it fails for the developer and for most of the audience at once, and bundling a model of our own is not a fallback at roughly two billion parameters. And `pimentel/homophones`, the curated list that is the first search hit, states no licence at all, so it cannot go into a public repository.

**How the keyboard knows which word was tapped**, checked rather than assumed on 2026-09-04: `onUpdateSelection()` fires when the user taps to move the cursor, and `InputConnection.getTextBeforeCursor` and `getTextAfterCursor` read the text either side of it, which is enough to recover the word the cursor landed in. Read from Android's reference documentation, not run.

**This is the offering side of your own SPEC principle**, added 2026-09-03 from Gboard adding an apostrophe to every "its": nothing correctly spelled is ever changed on the user's behalf, and offering an alternative the user can tap is permitted where applying one is not. Nothing here changes a word unless a candidate is tapped, and no word is marked as wrong.

**What the build changes.**
- `scripts/generate-homophones.py` — new. Reads a CMUdict release, groups entries by phoneme sequence, keeps only groups whose members all appear in the shipped word list, and writes the table with a header naming the CMUdict version it came from.
- `resources/homophones-en.txt` — new, generated by that script and committed alongside the key configs, the emoji data and the word list.
- `android/app/build.gradle.kts` — the existing copy task also takes the homophone table into the app's assets.
- `android/app/src/main/java/tech/flintcraft/hexboard/Homophones.kt` — new. Loads the table and answers one question: what are the alternatives for this word, if any.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — on a selection change, reads the word the cursor landed in, asks for its alternatives and publishes them; replaces the word when one is chosen.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the row above the keys draws the alternatives and reports which was tapped. The row's height is unchanged: this fills the space [suggestion-strip] shipped empty.
- `README.md` — the Notices section gains CMUdict's acknowledgment.
- `android/app/src/test/java/tech/flintcraft/hexboard/HomophonesTest.kt` — new, carrying the first half of the observation.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/TapWordAlternativesUiTest.kt` — new, carrying the second half.

Reads but does not change: `resources/wordlist-en.txt`, which [predictive-dictionary-bundle] ships and which the generation script filters against.

**The observation that shows it landed:** `HomophonesTest` passes, asserting that "rose" offers "rows", that a word with no homophone offers nothing, and that every word in the table appears in the shipped word list — which is the filter made checkable rather than asserted. `TapWordAlternativesUiTest` types a sentence containing a homophone, taps that word, asserts its alternative appears in the row above the keys, taps the alternative and asserts the text now reads with the replacement and nothing else changed. Nothing here can run either, so running them is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** A language model over the sentence — no on-device route below the Pixel 9. `pimentel/homophones` — no licence. Reusing the neighbour-weighted distance over the word list — autocorrect has already fired by the time a word is finished, so what is left is what geometry cannot reach. Wiktionary's pronunciation data — CC BY-SA, raising the same ShareAlike question `wordfreq` raised for frequencies. Highlighting a word the keyboard thinks is wrong — barred by SPEC, and it would make the feature something that acts rather than offers.

**What is deliberately not answered.** Whether homophones alone are enough: a user tapping a word may have meant something merely similar rather than something identical-sounding, and nothing measures how often. That is a question for after this is used, not a reason to hold it — the motivating case is a homophone.

Cites research: `workshop/resources/research/word-alternative-sources.md`, which carries the licences, the device list and what it does not settle.

Rests on: CMUdict's licence and its phoneme-sequence content, read from the cmusphinx/cmudict repository on 2026-09-04; the ML Kit GenAI supported-device list starting at the Pixel 9, read from Google's own documentation on 2026-09-04, which that file flags as amended on a cycle; `onUpdateSelection()` firing on a cursor tap and `InputConnection` reading the surrounding text, read from Android's reference on 2026-09-04 and not run; that the row above the keys exists and works, which [suggestion-strip] built on 2026-09-04 and nothing has yet compiled — which is why [verify-this-runs-build-on-device] is the second blocker.

[speech-output-correction] records what is known about how Gboard does the equivalent, and found that Google describes its proofreading as origin-blind — checking typed, pasted and dictated text alike — which is why this feature needs no record of what arrived by voice. [uniform-neighbours-predictive] is a different mechanism and cannot reach these cases. Those orderings are written on all three entries.

#### Voice input inside the keyboard, held open by the thumb [in-keyboard-voice-input]
Red flag · State: cleared
**Lifted on 2026-09-09.** [verify-this-runs-build-on-device] was driven to its end that day: the app builds, installs, types and switches layouts on the Pixel 6, so the row this item's microphone sits in is compiled and running rather than written blind.
**The hold was repointed on 2026-09-04, and both of the old blockers are answered rather than dropped.** [recogniser-gap-comparison] was driven to its end that day: the platform's on-device recogniser and Gboard's produced basically identical transcripts, differing only in punctuation, so the premise this item was held against — that the recognition is good enough to build a thumb-held control around — is established. Its remaining formatting gap is [speech-output-correction], which now waits on this item rather than the other way round. [suggestion-strip] was built the same day and shipped the empty row this item's microphone sits in, but nothing has compiled it, so what stands in for it here is [verify-this-runs-build-on-device], the walkthrough that installs and checks that build. The one caveat from the comparison, recorded rather than buried: it was run on read passages, which the user established mid-drive is the easy case, so it says the two engines match and does not say either is good in ordinary hesitant speech.

**The strip left this item on 2026-09-02, later the same session.** This item no longer builds the row above the keys — [suggestion-strip] does, and this one adds the microphone to its right end. The reason is that [persistent-clipboard] turned out to need the same row for its own button and is held by nothing, while this item is held by a test on the phone; leaving the container here would have parked the clipboard behind a dictation comparison that has nothing to do with clipboards. The row's height derivation, its reserved overspill and the cost in key size moved to that item with it and are not repeated here.

Hold moved on 2026-09-02. [first-installable-build] shipped that day, and the design was completed the same session, so what remains is not a missing keyboard but an unmeasured recogniser: [recogniser-gap-comparison] establishes whether the platform's on-device recognition is good enough to build a thumb-held control around. That is a premise this whole item rests on rather than a detail, so it holds the build.

Captured by you on 2026-09-01 and designed with you the same session. Your requirement, and the reason the feature exists at all: people should not have to switch to another keyboard to dictate, because they switch away and never switch back.

**That requirement is satisfiable, and this was checked rather than assumed.** An input method can run recognition itself — `SpeechRecognizer.isOnDeviceRecognitionAvailable()` and `createOnDeviceSpeechRecognizer()` exist from API 31, with on-device recognition forced from API 33 — and it needs the `RECORD_AUDIO` permission. Two existing keyboards, WhisperInput and Transcribro, are built this way, so this is a trodden path rather than a hopeful one. Read from Android's documentation and those projects on 2026-09-01; nothing was run.

**The privacy risk, raised by Claude and settled with you in the same exchange, which is what clears the flag above.** A keyboard requesting microphone permission is on its face indistinguishable from a keyboard that listens to you, and this repository is public and will be read by people deciding whether to trust it. `RECORD_AUDIO` on an input method is exactly what a malicious keyboard would ask for. Four things answer that, and three of them are design rather than assurance:

- **Recognition is on-device and audio never leaves the phone.**
- **Where a device cannot recognise on-device, voice input is simply unavailable.** This is the conservative arm of a real fork and it costs something: the project's minimum is API 26, and the on-device recogniser starts at 31, so devices between those levels get no voice input at all. The alternative was falling back to the network recogniser, which would send audio off the device — rejected, because a keyboard that quietly ships your voice somewhere on older hardware makes every other claim here worthless.
- **The microphone is open only while the control is held down.** Your choice, from a straight comparison with tap-to-start-tap-to-stop. The reason it won: "the microphone is open only while you are holding the button" is a claim a stranger can verify by using the keyboard, rather than one they have to take on trust from an indicator and a timeout. The cost was named and accepted — press-and-hold is tiring for anything longer than a sentence and you cannot dictate while looking away, so comfortable long-form dictation is a later question, not this item's.
- **No audio is retained once the words are transcribed**, and the mic is visibly indicated while open.

**The control grows while held and shrinks when fully released.** Yours, on 2026-09-01, and it answers the objection to press-and-hold rather than restating it: the button is hard to keep hold of while you are moving about, and a thumb drifting slightly off it loses the message halfway through. Enlarging the control the moment it is held makes the target forgiving exactly when forgiveness is needed, and shrinking it back on release keeps the resting keyboard uncluttered. It is the project's own thesis — a generous touch target — applied to the one control where losing your grip costs a whole sentence rather than one character. What still needs settling at build time is what counts as "fully released", since the point of the growth is that small movements must not end the recording.

**Where the mic control lives, settled by you on 2026-09-02.** It sits in a row above the board — the strip where autocorrect suggestions go — at the right-hand end of that strip. So it is a control belonging to the board, not a key declared in the layout config: `resources/key-layout.json`, the manifest rules and every language's layout are untouched by it, which is what a per-language config should be untouched by.

Three alternatives were read out of the code and lost, recorded so none is re-proposed. A key in QWERTY's row 3 loses outright: columns 0–9 are all occupied (shift, `?`, `,`, `!`, space, `'`, space, `"`, `.`, `-`), so a mic key there evicts a punctuation key, which SPEC's manifest rules make a deliberate loss rather than a placement. A key on RARE or SYMBOLS, where row 3 does have gaps, defeats the feature's own purpose — a microphone you must swipe to reach is most of the way back to switching keyboards. And a long-press on an existing key spends no pixels but has no free hold to spend: holds already carry accent menus and backspace repeat.

**What the choice costs, stated rather than glossed.** `HexboardBoard` today sizes itself to exactly the tallest panel and draws nothing else, so the strip is new vertical space on a keyboard whose whole argument is larger keys. The strip is the standard place a phone keyboard puts this row, so the cost is the ordinary one every keyboard pays, but it is a cost and SPEC's geometry principle is what it is paid against.

**This item builds the strip, settled by you on 2026-09-02.** Nothing has built it, and the feature that would normally own it — predictive text — is deferred by SPEC until after the first working keyboard, so waiting for it means waiting behind something with no date. This item therefore adds the strip as a board-level row above the pager, carrying the mic control at its right end and nothing else; predictive text later fills the rest of it with candidates rather than creating it. The alternative was filing the strip as its own item and holding this one against it — rejected as a hop with nothing in it, the strip being layout with no design left once its height is chosen.

**How tall the strip is, settled with you on 2026-09-02.** The strip's drawn band is one row's vertical pitch — `KeyGeometry.verticalStep` of the same radius the board already solved for that panel — floored so it never falls below 48dp. Derived rather than fixed, so it stays in proportion on the eleven-wide Russian layout and on any screen width without a second rule, and so the mic can never end up a different size from the keys under it. A fixed dp value was the alternative and lost on exactly that: it drifts out of proportion on every board that is not ten wide.

**The mic is drawn larger than a key and sits proud of the strip**, your call in the same exchange — a stylistic overspill past the strip's edge rather than a control confined to the band. One structural consequence, which changes what the build does rather than how it looks: an input method's window is sized to its view and Compose clips to bounds, so nothing can be painted outside the keyboard's own rectangle. The view therefore reserves the overspill as real height at the top and leaves it empty except where the mic occupies it. So the strip's reserved height is one row's pitch plus the overspill, while its drawn band is the pitch alone.

**What counts as "fully released", settled by you on 2026-09-02: the finger leaving the screen, with no distance threshold.** Once the hold has begun that pointer owns the recording until it lifts, anywhere on the screen, whether or not it has slid off the control. Drift cannot cut a sentence short at any distance, which is what the growing control existed to prevent, and it removes the magic number rather than choosing a value for it. The one case needing its own answer is Android cancelling the pointer outright — a system gesture, the notification shade — where recording stops and whatever was transcribed is kept rather than discarded.

**A defeated alternative, recorded so it is not revived.** An accent picker — offering the English varieties a device supports, presented as accents rather than locales — was designed and then dropped by you on 2026-09-01. It reached only speakers who happen to match one of a short list organised by where a variety of English is spoken natively, and missed the case you actually raised: a second-language English speaker in a multicultural country, who matches no entry. Your verdict was that it compares poorly with training on the user's own voice, which is [personal-voice-model]. Recognition here therefore uses the language the phone is set to, with no accent setting of its own.

Cites research: `workshop/resources/research/android-voice-input-and-accents.md`, which carries the API levels, the API 26–30 gap, the recognisable English varieties and why that list cannot answer an accent complaint.

Two things this item inherits from work filed on 2026-09-02. [ondevice-recogniser-test] puts a temporary dictation screen and the `RECORD_AUDIO` permission into `MainActivity` to measure the recogniser early; **removing both belongs here**, since this item is what introduces the microphone properly, and a test screen carrying that permission must not survive into anything published. And [recogniser-gap-comparison] measures how the platform recogniser compares with Gboard's, which tells this item whether the recognition it gets is good enough to build the press-and-hold control around at all. That ordering is written on all three items.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `HexboardBoard` gains the strip above the pager and adds its reserved height to the board's own; the mic is drawn at the strip's right-hand end as a circle larger than the board's key radius, standing proud of the strip's band; press-and-hold grows it, a lift anywhere on the screen ends the recording, and a cancelled pointer stops it while keeping what was transcribed.
- `android/app/src/main/java/tech/flintcraft/hexboard/VoiceInput.kt` — new. Wraps the on-device recogniser: the API 31 and `isOnDeviceRecognitionAvailable()` gate, one utterance per hold, no network fallback, no audio retained. Where the gate fails the mic is absent rather than degraded.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — commits the recognised words into the field the keyboard is attached to, and routes the runtime `RECORD_AUDIO` request.
- `android/app/src/main/AndroidManifest.xml` — the `RECORD_AUDIO` permission's TEMPORARY marker removed; the permission becomes permanent and is the microphone's own rather than the test screen's.
- `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt` — the temporary dictation screen from [ondevice-recogniser-test] removed, availability line and Dictate button with it.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/VoiceControlUiTest.kt` — new. Carries the observation below.

Reads but does not change: `resources/key-layout.json` (to confirm the mic is not in it), `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` (for `verticalStep` and the solved radius).

**The observation that shows it landed:** the instrumented test asserts the strip is present above the keys, the mic's accessibility node sits at its right-hand end with a bounds at least 48dp square and larger than a key's, `MainActivity` no longer exposes a Dictate button, and a hold-then-lift on the mic opens and closes exactly one recognition session. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** A mic key in QWERTY's row 3 — no free column, so it evicts punctuation. A mic key on RARE or SYMBOLS — a microphone you must swipe to reach is most of the way back to switching keyboards. A long-press on an existing key — no free hold, accents and repeat already hold them. A fixed dp strip height — drifts out of proportion on any board that is not ten wide. A distance threshold for release — a magic number where none is needed. Filing the strip as its own item — a hop with nothing in it. The accent picker and the network fallback, both recorded above.

Rests on: the on-device recogniser's API levels and the `RECORD_AUDIO` requirement, read from Android's documentation on 2026-09-01; that on-device recognition genuinely keeps audio on the device, which is what the API claims and what a build should confirm before the README repeats it; Android's 48dp minimum touch target, read from Google's own accessibility guidance on 2026-09-02, with 24dp the WCAG 2.5.8 level-AA floor; that an input method's window is sized to its view and Compose clips to bounds, so an overspill must be reserved as real height — reasoned from how the board is drawn today, not run; that the platform recogniser is good enough to build a press-and-hold control around, which is unverified and is exactly what [recogniser-gap-comparison] measures.

[uniform-neighbours-predictive] inherits the strip rather than creating it: predictive text fills the row this item introduces. That ordering is written on both items.

#### Clipboard history that persists, with a screen and drag-to-bin deletion [persistent-clipboard]
Red flag · State: cleared
**Lifted on 2026-09-09.** [verify-this-runs-build-on-device] was driven to its end that day: the app builds, installs and types on the Pixel 6, so the row this item's clipboard button sits in is compiled and running rather than written blind.
**The hold was repointed on 2026-09-04.** [suggestion-strip] built the row this item's clipboard button lives in on 2026-09-04, so it is no longer in the queue — but nothing has compiled it, and this item's whole route in depends on that row working. What stands in for it is [verify-this-runs-build-on-device], the walkthrough that installs and checks that build.

Captured by you on 2026-09-01, and designed with you in the same session. Your complaint is with Gboard: its clipboard clears, and losing something you copied twenty minutes ago is the annoyance this removes.

**The retention rule, in your words and then in mine.** You asked for "the last 20 items or hour of content" — more than twenty items under an hour old is fine, and at over an hour old everything past twenty falls off. Restated as the rule a build implements: a clip is kept if it is younger than an hour, or if it is among the twenty most recent; it is dropped only when both fail. So fifty clips copied in one hour are all kept while that hour lasts, and once they have all aged past it, the newest twenty remain.

**The screen and the deletion gesture, also yours.** A clipboard screen in the manner of Gboard's. An item is deleted by holding it and dragging it to a bin that appears while the drag is in progress.

**No clear-all, and the reason is a threat model rather than a simplification.** Your reason: a cleared clipboard is itself suspicious — someone looking over your shoulder wonders why it is empty. Per-item deletion removes what you want gone while the rest still looks ordinary, and the retention rule handles the bulk case by itself. This is a defeated alternative worth recording, because a clear-all button is the obvious thing to add and looks like an omission rather than a decision.

**The privacy risk, raised by Claude at the moment the idea landed and settled with you in the same exchange, which is what clears the flag above.** A clipboard history is a file on the phone holding the last hour of everything copied, and what passes through a clipboard includes passwords, one-time codes and card numbers. Persisting it is a real increase in exposure over a clipboard that clears. You were told that plainly, and the design below is what you chose rather than dropping the feature.

The protections, and what each is worth:
- **Clips from a password field are kept five minutes, not an hour.** Password fields are detectable: an input method receives an `EditorInfo` when it attaches to a field, and its `inputType` carries the password variations — this is how keyboards suppress learning in password boxes. Verified against Android's input-method documentation on 2026-09-01.
- **Clips the source app marks sensitive get the same five minutes.** Android's flag is `ClipDescription.EXTRA_IS_SENSITIVE` from API 33, with the string constant `"android.content.extra.IS_SENSITIVE"` usable below it — which matters, since this project's minimum is API 26. Its documented worth is limited and the item must not overstate it: Android states the flag adds no security and is a rendering hint the *source application* chooses to set, so it protects only where that app bothered.
- **The store is encrypted at rest and never leaves the device.**
- **The five-minute rule is stated in the app where the user can read it**, because Android's own guidance for input methods is that a password is not saved anywhere without explicitly informing the user.

**Two limits that are stated rather than solved.** A clip copied while the keyboard is not attached to the field cannot be classified, so it gets the ordinary hour-and-twenty rule — the common case is covered, since copying usually happens through the app's selection toolbar while the keyboard is up, but the gap is real. And none of this makes a persistent clipboard safe; it makes it safer, and it remains a larger target than one that clears. That is the trade you accepted knowingly.

**A defeated alternative, recorded so it is not re-proposed.** Claude first proposed refusing to store anything copied from a password field at all. You replaced it with the five-minute expiry, and the reason it lost is that exclusion breaks the case people actually hit — you deliberately copy a password to paste it, and it is not there. Five minutes is long enough to paste and short enough not to sit in an hour of history.

**How the clipboard screen is reached, settled by you on 2026-09-02: a button at the left end of the strip above the keys.** That strip is introduced by [in-keyboard-voice-input], which puts the microphone at its right end; the clipboard takes the left. The reason this route wins is that it spends nothing — no gesture, no key slot, and no vertical space beyond what the strip already costs — where every other route spends something. It is also where Gboard puts its own clipboard entry, so it is the familiar place to look.

Two routes were ruled out the same day. Reaching it by a swipe is not available: horizontal swipe already moves between the three letter panels, which [panel-switch-gestures] shipped that morning as a pager. And reaching it the way emoji are reached does not exist — a grep of the Kotlin on 2026-09-02 found no vertical swipe and no emoji panel anywhere, so SPEC's promise of five emoji panels is unimplemented and cannot be built on.

**What the clipboard screen replaces, settled by you on 2026-09-02: the board area only, with the strip staying put.** The row above the keys does not move or change height; the panels beneath it are swapped for the clip list, and the clipboard button at the strip's left end is a toggle — tap to open, tap again to return to the keys.

Three reasons, recorded because the alternatives look reasonable. The keyboard's total height never changes, so nothing under the thumb jumps when the list opens or closes, where a full-screen or taller panel would shift the very text field being pasted into. The way back is the control you came in by, so there is nothing new to learn. And it keeps the clipboard inside the keyboard rather than sending the user to an app screen, which would mean leaving the text field — the failure this whole project designs against. An app-screen clipboard and a taller overlay are the two defeated alternatives.

The cost, accepted knowingly: the board area is four rows tall, so three or four clips are visible at a time and the rest are reached by scrolling. Gboard makes the same trade and it is not avoidable without changing the keyboard's height.

The original hold, [first-installable-build], shipped on 2026-09-02, and the reasons this sat in Unprocessed have gone with it: the panel structure is settled, and the screen now has a home. What it waits on instead is [suggestion-strip], the row its button lives in.

**Tapping a clip pastes it and returns to the keys**, settled by you on 2026-09-02 — one action for the thing the list was opened for, where a second tap on the toggle would make the common case two gestures. A tap and a hold therefore do different things and neither can be mistaken for the other. What this rules out, recorded rather than left implicit: there is no way to select a clip without pasting it, and no multi-paste. Neither was wanted — the retention rule already handles keeping things.

**The bin appears across the bottom of the board area while a drag lasts**, settled by you the same day, and vanishes when the drag ends. The bottom edge because that is where a thumb naturally pulls something it wants rid of, and because the list scrolls vertically, so a bin at the top would sit where the next clip arrives from. Releasing anywhere other than on the bin returns the clip to the list, making an aborted drag the default rather than something to aim for. A permanently visible bin was the alternative and lost twice over: it spends space in an area already showing only three or four clips, and it can receive an accidental tap that a drag-only target cannot.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/ClipboardStore.kt` — new. Holds the history and the retention rule (kept if under an hour old or among the twenty most recent, dropped only when both fail; five minutes instead for password-field and source-marked-sensitive clips), encrypted at rest, with read and per-item delete. Nothing leaves the device.
- `android/app/src/main/java/tech/flintcraft/hexboard/ClipboardScreen.kt` — new. The scrolling clip list drawn into the board area, tap-to-paste-and-return, hold-and-drag with the bin across the bottom, and the sentence stating the five-minute rule where the user can read it.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the clipboard button at the left end of the row [suggestion-strip] builds, and the swap of the board area between the panels and the clip list. The row itself is untouched: this item adds a control to it and does not change its height.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — watches the clipboard, classifies each clip from the attached field's `EditorInfo.inputType` and the clip's own sensitive flag, and commits a pasted clip into the field.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/ClipboardUiTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** the instrumented test asserts that the strip's left-end button swaps the board area for the clip list and back without the keyboard's total height changing; that a tap on a clip commits its text and returns to the keys; that a hold shows the bin across the bottom, a release on it removes that clip and a release elsewhere does not; and that a clip stored from a password-typed field is gone after five minutes while an ordinary one is not. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

Held below the line against [suggestion-strip], which builds the row this item's button lives in. That ordering is written on both items.

Rests on: `EditorInfo.inputType` exposing password variations to an input method, and `ClipDescription.EXTRA_IS_SENSITIVE` existing from API 33 with a usable string constant below it — both read from Android's documentation on 2026-09-01, neither run; that an encrypted-at-rest store is available to an input method without further permission, which is assumed rather than checked and should be confirmed at the start of the build.

#### Accent row on the top row overlaps the neighbouring keys [accent-row-top-row]
**Lifted on 2026-09-09.** [verify-this-runs-build-on-device] was driven to its end that day and the strip this row clamps into is compiled and on the phone. One caveat travels with the lift: this item's observation is an instrumented test, and the instrumented suite is currently failing on every test that renders the board — [instrumented-tests-no-composition].
The accent row for a top-row key draws into the strip above the keys instead of over its own row's neighbours.

**The hold was repointed on 2026-09-04.** [suggestion-strip] built the row this clamps into on 2026-09-04 and has left the queue, but nothing has compiled it. [verify-this-runs-build-on-device] is the walkthrough that installs and checks that build, and it stands in here.

**Settled with you on 2026-09-02, and the fix arrived the same session.** The accent row is drawn inside the board's bounds and clamped to the top edge, which is what `hexboard17.html` does — but the prototype had a bar above its keys to clamp *into*, and the board had nothing above row 0. [suggestion-strip], designed earlier in this session, restores exactly that bar: a row one vertical step tall, which is slightly more than a key's diameter, so an accent row for a top-row key fits in it. This is not a workaround; it is the arrangement the prototype's clamping was written against.

**The cost, stated rather than discovered.** While a top-row key is held, its accent row covers the suggestion row and the microphone. That is transient and nobody reads suggestions mid-hold, but it is a real overlap and worth seeing on the phone.

**Three options refused, each with what defeated it.** Drawing the row below the key on the top row — accents would appear above on three rows and below on one, and the overlap merely moves onto row 1. Drawing it in a window that may extend above the input view — the genuinely general answer, and what commercial keyboards do, but it is a second window with its own lifecycle and dismissal, which is a great deal of machinery for a case the strip already answers; worth revisiting only if the strip is ever removed. Shrinking the row — degrades the feature on every row to fix it on one.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the accent row's clamp takes the strip's reserved height as its ceiling rather than the board's top edge, so a top-row key's alternatives rise into the strip.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/AccentRowTopRowTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** the instrumented test holds a top-row key and asserts that the accent row's bounds sit entirely above the top key row — overlapping the strip rather than any key — and that holding a key on a lower row is unchanged.

Held below the line against [suggestion-strip], which builds the row this clamps into. That ordering is written on both items.

Rests on: the accent row's current clamping, read from `KeyboardPanel.kt` on 2026-09-02, not run; that one vertical step of strip height is enough to hold an accent row, which follows from the arithmetic and has not been seen.

Filed by /rescan on 2026-09-02 from the run that built [long-press-accent-popup]. The row is drawn by the board, inside the board's bounds, clamped to the top edge as the prototype clamps it — but the prototype had a bar above its keys to clamp into, and the board has nothing above row 0. So holding Е or E on the top row draws the alternatives over the neighbouring keys of that same row. Options not yet weighed: draw the row below the key on the top row, draw it in a window that may extend above the input view, or shrink the row. Seen only in reasoning; the first run on the Pixel 6 will show how bad it looks.

#### Panels do not run continuously into each other, and the cause is geometric [panel-seam-gap]
Swiping between panels shows a gap at the seam: the columns of the outgoing panel and the incoming one sit further apart than the columns within either. Reported by you on 2026-09-09, on the real keyboard, with a screenshot of RARE and QWERTY mid-swipe.

**This is your original complaint of 2026-09-03, and the earlier diagnosis was half right.** [symbols-panel-empty-slots] took "the next panel does not run continuously from the last one" to be the symbols panel's ten empty slots, checked that `HorizontalPager` sets no `pageSpacing`, and concluded the pager was not the cause. The empty slots were real and are now filled. The seam is a second cause, and it survives them.

**The arithmetic, which is what makes it a design question rather than a bug.** Within a panel, neighbouring columns sit `horizontalStep` apart — `verticalStep * √3/2`, about 1.81 radii, and less than two radii precisely because the packing is hexagonal. Across a seam, the distance is different: the last column's centre sits `radius + EDGE` from its page's right edge, the first column of the next page sits `EDGE + radius` from its left edge, so the two centres are `2 * radius + 2 * EDGE` apart — about 2.55 radii at the Pixel 6's solved size. That is roughly 40% wider than a within-panel step, which is well past what an eye misses.

The zag itself carries across correctly: column 9 is odd and column 0 is even, so the parity alternates as it should. Only the spacing is wrong.

**Worth being clear about what is at stake**, since it is a mid-swipe appearance rather than a typing fault: nothing mis-routes and no key is unreachable. What it costs is the impression that the three panels are one board — which is the impression the horizontal swipe exists to give.

**Settled with you on 2026-09-12: a pager page stops being the width of the screen and becomes a whole number of column pitches wide.** Ten columns on a ten-wide board, eleven on the Russian one, each page `columns * horizontalStep` across, with the first and last column centres half a pitch from their page's edges. Adjacent pages then tile at the pitch, so the seam is one `horizontalStep` by construction rather than by a correction applied afterwards. The outermost circles overhang their page edge by about a tenth of a radius, which is what the half-pitch margin costs and is why the screen edges still want `EDGE` as the pager's own padding rather than each page's.

**This replaces a decision taken earlier the same day and the reason it lost should not be lost with it.** The first answer was a negative `pageSpacing` of `2 * EDGE + 2 * radius - horizontalStep`, overlapping full-width pages by exactly the surplus. It closes the seam and nothing is wrong with the arithmetic — but a screen-width page has the spare width of a sideways phone *inside* it, so no neighbouring panel can ever appear there, and [landscape-reveal-neighbours] needs precisely that. Two mechanisms would then be doing one job. The page-width route also removes the open risk the overlap carried: Google's issue tracker has an entry titled "HorizontalPager with negative pageSpacing causes…" whose page is behind a sign-in, so nobody here could read what it reports.

**What it costs, accepted knowingly: a sliver of the neighbouring panel shows in portrait too**, roughly six or seven dp at each edge at the Pixel 6's portrait radius, because a board of pitch-width pages is slightly narrower than the screen. That is a permanent change to how the board looks at rest, not only during a swipe, and it was weighed against needing a second mechanism for landscape.

**Two further options refused, each with what defeated it.** Dropping `EDGE` to zero — narrows the seam without closing it, since two radii still exceed the column pitch, and spends the board's breathing room to get a partial fix. Rebuilding the three panels as one wide board that scrolls by panel — the only route making the seam impossible rather than closed, and far too large a change for a fault that costs an impression.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — a function giving a panel's page width for a solved radius and a column count: `columns * horizontalStep`, with column centres at `(i + 0.5) * horizontalStep` from the page's left edge, expressed in the same terms as the existing step derivations so it moves with them.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the `HorizontalPager` takes a fixed page size of that width instead of filling the viewport, with `EDGE` moving from each page's own inset to the pager's padding so the screen edges keep their breathing room. Key positions within a panel come from the same function, so the drawing and the paging cannot disagree.
- `android/app/src/test/java/tech/flintcraft/hexboard/SeamSpacingTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** `SeamSpacingTest` passes, asserting that the last column of one page and the first column of the next sit exactly `horizontalStep` apart — the same pitch as two columns inside a panel — that this holds across the whole solvable radius range rather than at one sample, the way `KeyEdgeTest` already checks the fade, and that it holds for an eleven-column board as well as a ten. Whether the join reads as continuous is a judgment on the phone, made by [judge-drawing-values-on-phone], which is held against this item and three others; the instrumented suite is not available as a check while [instrumented-tests-no-composition] stands. That ordering is written on all five entries.

Rests on: `HorizontalPager` accepting a fixed page size smaller than its viewport, which is the ordinary carousel arrangement and was read from Compose's pager documentation on 2026-09-12, not run; the seam arithmetic itself, computed from `KeyGeometry.kt` and recorded above on 2026-09-09; that `HorizontalPager` set no `pageSpacing` as of the build of [symbols-panel-empty-slots], checked by that build.

[landscape-reveal-neighbours] is held against this item: it centres the current page and decides what a tap on a revealed key does, both of which need pages to be board-width first. That ordering is written on both entries.

Filed 2026-09-09 09:58, stamped by the queue tool.

#### Gradient too short and labels too small, seen on the phone at the shipped values [key-drawing-second-pass]
Captured by you on 2026-09-09, looking at the soft key edge on the Pixel 6 for the first time. [soft-edge-fraction-values] picked its two numbers by arithmetic and shipped them unseen, and its own walkthrough step said an opinion either way was the result. This is that opinion.

**The outer size is right and the fade is too thin.** Your account: the area of the circles is correct, so the gradient should extend further *inwards*, not outwards. That reads directly onto the two constants — `FADE_FRACTION = 1.045` is where a key stops being drawn and is where you want it, while `SOLID_FRACTION = 0.75` is where the fade begins and is too far out, leaving the gradient as a thin ring rather than a soft edge.

**The labels are too small, and your reason is a different one from the size complaint.** Your words: it "should be sized independently of the gradient or any other content in the key. Like if I placed the key over a shape in inkscape instead of inside a cell in excel." The label should be sized against the key as an object, not against a box drawn inside it.

**That reverses a decision, which is why this is a capture rather than a fix.** `labelSize` multiplies the *solid* radius, deliberately: [soft-key-edge] wanted a glyph to sit inside the definite middle of the circle rather than out on the fading part, and [soft-edge-fraction-values] refused sizing labels against the fade radius on exactly that ground. The refusal was sound on its own terms and it has now been looked at, which the decision never was.

**The two halves are coupled, which is the reason to settle them together.** Lowering the solid fraction to lengthen the fade shrinks every label, because labels are sized off it — so doing the first without the second makes the second complaint worse. Decoupling the label from the solid radius is what lets the fade be tuned freely afterwards.

**Settled with you on 2026-09-12: the label is sized against the key's own solved radius.** The key is the object and what is drawn inside it is drawing, which is your Inkscape framing taken literally. It also breaks the coupling outright, so the fade can be retuned afterwards without moving a single label.

Two alternatives lost. Sizing against the fade radius — it is what is actually drawn, but it reintroduces the same coupling in a different place, since the fade is the thing most likely to be retuned. Keeping the solid radius and raising `LABEL_FRACTION` — it cannot pass 1.0 without putting glyphs on the fade, which is precisely what sizing against the solid core exists to prevent, so it buys little and keeps the coupling.

**The three values, settled the same day and tunable rather than derived truths.** `LABEL_FRACTION` stays 0.90 and `LARGE_LABEL_FRACTION` stays 0.78 — no number is invented, and what changes is what they are a fraction of, which takes a lowercase label from 0.675 of the radius to 0.9, about a third larger. `SOLID_FRACTION` becomes 0.5: half the key solid, half fading, a proportion rather than a figure picked to look right, which takes the gradient band from 0.295 of the radius to 0.545. `FADE_FRACTION` stays 1.045, where neighbouring fades just meet, because the outer size is the part you said was right.

What settles the values is your eye on the phone, and one move is expected rather than a surprise — that becomes its own capture rather than making this item the last word.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — `labelSize` multiplies the solved radius rather than `solidRadius`; `SOLID_FRACTION` 0.75 to 0.5; the doc comment above `labelSize` rewritten, since it currently states the reasoning this item reverses — that a glyph sits inside the definite middle of the circle rather than out on the soft edge. `FADE_FRACTION` and both label fractions keep their values.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyEdgeTest.kt` — the existing assertions that the fade meets its neighbours across the solvable range still hold at the new solid fraction, and gain the observation below.

**The observation that shows it landed:** `KeyEdgeTest` passes, asserting that a label's size is a fixed proportion of the solved radius and does not change when `SOLID_FRACTION` changes — the decoupling made checkable rather than asserted — and that the fade still meets neighbouring fades exactly across the whole solvable radius range. Whether the board now looks right is a judgment on the phone, and [judge-drawing-values-on-phone] is the sitting that makes it — held against this item and three others. That ordering is written on all five entries.

Rests on: 1.045 radii being where neighbouring fades just meet, derived and corrected by [soft-edge-fraction-values] on 2026-09-05; the current constants and `labelSize`'s base, read from `KeyGeometry.kt` on 2026-09-12; your look at the board on the Pixel 6 on 2026-09-09, which is the only evidence any of these values has ever been tested against.
Filed 2026-09-09 09:50, stamped by the queue tool.

#### Fill every non-English layout's empty symbol slots, quotes from CLDR per language [russian-panel-gaps]
Six layouts have empty key positions on their symbol panels, and what fills them is decided once, by a rule, rather than language by language. Russian has ten empty on SYMBOLS and three more on RARE; French, German, Spanish, Portuguese and Italian have the same ten on SYMBOLS each.

**[latin-panel-gaps] was folded in here on 2026-09-12 and deleted**, being the same ten slots on the five Latin layouts built on 2026-09-05. Its content is carried: those five copy the English SYMBOLS panel unchanged, the English fill's four curly quotes are wrong for each of them — French sets off speech with « », German with „ and its partner, and Spanish, Portuguese and Italian use « » alongside the curly forms — and its own conclusion that the four quote slots want a per-language answer while the other six do not, being punctuation rather than orthography. That split is what the rule below is built on.

Found on 2026-09-05 while filling the English symbols panel, by counting both configs rather than by anything failing. The Russian symbols panel is four rows of ten with thirty keys, empty at exactly the same positions as the English one — (0,1) (0,3) (0,5) (1,2) (1,4) (1,6) (2,5) (3,0) (3,2) (3,4) — which is unsurprising, since it was transcribed from the English config's shape. Its RARE panel is three rows of eleven with thirty keys, empty at (0,10) (1,10) and (2,0).

**Why it is not simply "copy the English answer".** [symbols-panel-empty-slots] fills the English slots with four curly quotes among other things, and Russian punctuation does not work that way: « » are the primary quotation marks and „ " the secondary pair. So the four characters doing the most work in the English fill are the wrong four here. SPEC's manifest rules bind each layout individually, which is exactly the case this is.

**The three RARE gaps are a transcription artefact, read from the file on 2026-09-12.** The Russian RARE panel carries exactly the same thirty characters as the English one, in the same order — `~ \` | < > ¬ ∞ √ ∑ π « » ° € £ ¥ © ® § ¶ ™ … – — × ÷ ± ≠ ≤ ≥` — laid into a grid eleven wide rather than ten, so three positions are left over at (0,10), (1,10) and (2,0). Nothing was dropped and nothing Russian was weighed; it is the English panel in a wider frame.

**The rule, settled with you on 2026-09-12, in three parts applied in order.**

1. **The four quote slots come from Unicode's CLDR**, whose `delimiters` element gives every locale a primary and an alternate pair — four characters, which is exactly four slots. Published data rather than a judgment made here, on the same footing as the bundled emoji list.
2. **Unless that exact character already appears on another panel of the same layout**, in which case the slot falls through rather than taking a second copy. **A long-press alternative is not "elsewhere" for this test** — settled on 2026-09-12 with [curly-quote-double-route] and now stated in SPEC: a character behind a hold is not a key on a panel, so it is a convenience rather than a duplicate. Reading it the other way would empty most of the quote slots this rule exists to fill, since the quote keys carry curly quotes as alternatives on every layout that inherited them from English. This matters on most of them: « » already sit on RARE on every layout, so French, Spanish, Italian, Portuguese and Russian would each gain an unresolved duplicate under part 1 alone, which SPEC's manifest rules forbid without a deliberate justification. Russian therefore takes the secondary pair „ " and drops two slots through; German, whose primary pair is on no panel, takes all four.
3. **A slot still empty takes, in order, any character another item is evicting — for Russian that is the apostrophe, reserved here on 2026-09-12 because [russian-missing-cursor-right] takes its row-3 slot for cursor-right and SPEC forbids losing a key — then the locale's currency symbol from CLDR where no panel carries it — ₽ for Russian — then the language's own punctuation with no home: № for Russian, and ª and º for Portuguese. **Spanish's fallback is spent**: ¡, ¿, ª and º all became letter-panel keys in [spanish-letter-panel-gaps] on 2026-09-12, so Spanish is expected to finish with about two slots this rule cannot fill, which is the last clause below doing its job rather than a failure. Where the list runs out, the build leaves the slot and files a capture naming the layout and how many are left.** It never invents a character. SPEC says a freed slot is filled with a character that has no other home, *agreed first*, so a build choosing one to satisfy the rule would break the rule it was satisfying.

The other six — `• ← → ½ ¢ ≈` — are copied unchanged to every layout, being punctuation rather than orthography.

**Why a rule rather than thirteen choices.** It answers every future language on the day its layout is transcribed, instead of leaving a per-layout judgment with no source behind it. The alternative considered was transcribing what each language's own keyboards offer, the way the letters were transcribed, and it is not available: FlorisBoard organises symbol layouts by script and region — `western.json` covers French, German, Spanish, Portuguese and Italian alike and there is no Russian one at all — read on 2026-09-12 and recorded in `workshop/resources/research/open-source-layout-sources.md`.

**And a prior question the answer depends on.** Hexboard no longer holds a layout back for a native reader's confirmation — you removed that requirement on 2026-09-04 in favour of the in-app report route, [layout-error-report]. So this is a judgment made from sources rather than from a reader, and the honest route is to transcribe what Russian keyboards actually offer rather than to reason about it, the way [language-starter-layouts] transcribed the letters. Where that data lives for symbol panels specifically was not established: FlorisBoard's character layouts cover letters, and its symbol arrangements were not read.

The hold against [symbols-panel-empty-slots] came off on 2026-09-12: that item shipped in the build run that ended on 2026-09-09, filling the English ten, and this rule follows its shape where the shape transfers.

**What the build changes.**
- `scripts/generate-symbol-fill.py` — new. Reads CLDR's delimiters and currency for each shipped layout's language tag, applies the three parts of the rule against that layout's existing key set, and reports what each slot would take and which slots the rule leaves empty.
- `resources/key-layout-ru.json`, `-fr`, `-de`, `-es`, `-pt`, `-it` — the empty SYMBOLS positions filled with what the rule yields, and the Russian RARE positions at (0,10), (1,10) and (2,0) filled from part 3.
- `resources/key-manifest*.md` — regenerated from the configs, never hand-edited, per SPEC.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` — gains the assertion carrying the observation below.
- `README.md` — the Notices section names CLDR alongside the existing Unicode notice, the data being Unicode's under the licence already read.

**The observation that shows it landed:** `KeyLayoutValidationTest` passes over all seven configs, asserting that no layout's SYMBOLS panel has an empty position except where the rule ran out, that no character appears on two panels of the same layout, and that each layout's quote characters match CLDR's delimiters for its language tag — which makes the rule checkable rather than asserted. Any slot the rule leaves empty is named in a capture by the build rather than filled.

**Options already refused, each with what defeated it.** Copying the English fill to every layout — four curly quotes are the wrong four for five of the six languages. Transcribing each language's symbol panel from FlorisBoard — its symbol layouts are per script and region, so the data does not exist. Deciding per language when each is transcribed — the honest description of doing nothing, and it leaves each layout's panel resting on a judgment with no source. Letting the build choose a character where the fallback runs out — SPEC requires a freed slot's character to be agreed first.

Rests on: CLDR's `delimiters` element supplying four quote characters per locale across 574 locales, read from CLDR's own LDML specification and from library documentation quoting it on 2026-09-12, with the per-locale values not each read — the build takes them from the data rather than from this item; FlorisBoard's symbol layouts being per script and region, read from its directory listing on 2026-09-12; the two configs' key counts and empty positions, counted on 2026-09-05 and the RARE panel's contents re-read on 2026-09-12; ₽ and № being Russian-used characters absent from every panel, which is a claim about the language rather than a read — the currency half is checkable in CLDR by the build, and the in-app report route is the backstop for the rest, the native-reader requirement having been removed from SPEC on 2026-09-04.

#### Row banding does not read as banding — the board looks randomly coloured [row-banding-too-weak]
Captured by you on 2026-09-09, seeing the alternating row tint on the Pixel 6 for the first time. Your account: the alternating shades are barely showing, and it reads as kind of randomly coloured rather than as bands.

**The second half of that is the more useful half.** "Too faint" would point at one constant — `ROW_TINT = 0.2` in `KeyGeometry.kt`, a fifth of the way from a key's resting fill toward its pressed colour. "Randomly coloured" points somewhere else: a key's base colour comes from its *kind*, and letter, punctuation, special, space, symbol and rare each have their own fill. Those vary within a row and across it, so a fifth-of-a-step tint laid over five different base colours produces five different results and no visible band.

So raising the figure may make the board louder without making it read as rows at all. The banding competes with the kind colouring, and nobody has looked at the two together.

**Why the banding exists**, from [row-tint]: the rows are still the QWERTY rows and only zigzag, and people read left to right, so the row is the unit that carries recognition — QWERTYUIOP is a string almost everyone knows on sight. Banding is what says *this is the keyboard you already use*, which is what SPEC's familiarity principle is defended by. That reason is untouched by this; what is in question is whether the current drawing delivers it.

**The constraint the figure was chosen against still holds:** a press travels the whole way to the lit colour, so a resting tint must stay far enough from that to never be mistaken for a pressed key. Whatever replaces 0.2 has to keep that distance.

**The arithmetic, worked on 2026-09-12, makes the faintness worse than it looks.** `ROW_TINT = 0.2` is a fifth of the way from a key's resting fill toward its *lit* colour, and `lit` is itself `lerp(fill, White, 0.35)`. So an odd row ends about 7% lighter than an even one — inside the range that reads as a slightly different colour rather than as a band.

**Settled with you on 2026-09-12: the banding applies to letter keys only, and its depth becomes a third.** The letters carry the recognition — the band exists to say QWERTYUIOP is where you expect it — and they share one base fill, so a tint over them produces one consistent step rather than five different ones laid over five kind colours. That removes the competition rather than shouting over it. `ROW_TINT` goes from 0.2 to 0.35: a press travels the whole way to lit, so at a third the resting band stays three times closer to its own row's colour than a pressed key is, which is SPEC's never-mistaken-for-a-press constraint stated as a ratio rather than judged by eye. In absolute terms an odd letter row goes from about 7% lighter to about 12%.

The value is a tunable rather than a derived truth, like [key-drawing-second-pass]'s three: chosen here so the build is fully described, settled by the eye on the phone, and one move is expected rather than a surprise.

**A consequence stated rather than discovered: the bottom row carries no band**, being punctuation and spaces with no letters in it. That is honest rather than a gap — there is no familiar letter string there for a band to reinforce.

**Options already refused, each with what defeated it.** Raising the tint alone — it makes the board louder without making it read as rows, since the five kind colours still fragment every band. Bringing the six kind colours closer together — the largest change available, and it alters the whole board's look to fix a row-reading problem; held back deliberately, to be revisited only if letters-only banding still does not read.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — `ROW_TINT` 0.2 to 0.35, with the derivation written beside it.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the row tint is applied only where the key's kind is a letter; every other kind draws its own fill untinted.
- `android/app/src/test/java/tech/flintcraft/hexboard/RowTintTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** `RowTintTest` passes, asserting that two letter keys on adjacent rows have different fills, that a punctuation, space or special key has the same fill on either row, and that a banded letter key's fill is nearer its own row's resting colour than to the lit colour a press produces — the never-mistaken-for-a-press constraint made checkable. Whether the board now reads as rows is a judgment on the phone.

Rests on: the five kind fills and `lit` being 35% toward white, read from `KeyboardPanel.kt` on 2026-09-12; `ROW_TINT`'s current value and meaning, read from `KeyGeometry.kt` the same day; your look at the board on the Pixel 6 on 2026-09-09, which is the only evidence this has been tested against.

Wants the phone to hand, like [key-drawing-second-pass], and the same sitting: [judge-drawing-values-on-phone] is that sitting, filed on 2026-09-12 and held against this item and three others. That ordering is written on all five entries.
Filed 2026-09-09 09:50, stamped by the queue tool.

#### Layout picker lists languages in an order that looks arbitrary to a reader [picker-language-order]
The picker groups layouts by language and sorts those groups by BCP 47 tag, so with the seven shipped layouts it reads German, English, Spanish, French, Italian, Portuguese, Russian — de, en, es, fr, it, pt, ru. That is alphabetical by a code the reader never sees.

Found on 2026-09-09 while driving [verify-this-runs-build-on-device], by reading `LayoutCatalogue.group`, which ends in `toSortedMap()` over the tag.

**SPEC settled the order *within* a language and says nothing about the order *between* them.** It requires layouts grouped by language and ordered within a language by a set position, which is exactly what the code does. The gap is real rather than a departure from a decision.

Small either way at seven layouts, and it grows with every language added.

**Settled with you on 2026-09-12: the language groups sort by each language's display name in the device's own language.** The order comes from data the platform already holds rather than from a field anyone maintains, and a reader whose phone is in Russian sees the languages named in Russian and sorted as Russian sorts them. The order changing with the device language is the behaviour working rather than a drawback. SPEC's layouts principle gained the sentence the same day.

**Options already refused, each with what defeated it.** Putting the current layout's language first — it pins the entry the reader is least likely to want, since the picker is opened in order to change layout, and the list rearranges itself between visits. A declared order per language, like the position each config already carries within its language — stable and correct, and one more field to get right on every layout added forever, to solve what the platform's own data solves for free. Leaving the tag sort — alphabetical by a code the reader never sees, which is the complaint.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/LayoutCatalogue.kt` — `group` sorts the language groups by the display name Android gives for each config's language tag in the current locale, rather than by the tag itself. The within-language ordering by declared position is untouched.
- `android/app/src/test/java/tech/flintcraft/hexboard/LayoutCatalogueTest.kt` — gains the observation below.

**The observation that shows it landed:** the catalogue test passes, asserting that with the seven shipped configs and an English device the groups come back in English display-name order — English, French, German, Italian, Portuguese, Russian, Spanish rather than the tag order `de, en, es, fr, it, pt, ru` — and that within a language the declared positions still decide.

Rests on: `LayoutCatalogue.group` ending in a sorted map over the language tag, read on 2026-09-09; Android supplying a language's display name for a tag in the current locale, which is ordinary platform behaviour and was reasoned from rather than run; the seven shipped configs and their tags, read on 2026-09-09.
Filed 2026-09-09 09:21, stamped by the queue tool.

#### Add lowercase "ok" to the word list by name [word-list-ok-by-name]
Lowercase `ok` is in no SCOWL file at or below level 60, so the shipped list does not carry it and the correction engine would be free to change a typed "ok" into something else. `OK` is present at level 35; only the lowercase form is missing.

Split out of [word-list-size-level] on 2026-09-12, which keeps the general question — how the size level gets chosen, and what else is missing — and holds it against the engine. This is the one case that is known rather than suspected, and it wants no evidence to settle.

**Why by name rather than by raising the level.** `scripts/generate-word-list.py` already adds a word back by name: the pronoun `I` sits in `ALWAYS_CAPITALISED` because SCOWL files it with proper names, and a dictionary without it would leave the engine free to change a typed `I`. This is the same shape of problem and takes the same shape of answer. Raising the level to reach "ok" would also admit every obscure word at the levels in between, which the engine could then correct *toward* — the trade level 60 was deliberately making.

**What the build changes.**
- `scripts/generate-word-list.py` — a named add-back for lowercase words SCOWL omits, seeded with `ok`, alongside the existing `ALWAYS_CAPITALISED` backstop and documented the same way: what was missing, and what a list without it would let the engine do.
- `resources/wordlist-en.txt` — regenerated, so the committed list carries the word.

**The observation that shows it landed:** the regenerated `resources/wordlist-en.txt` contains a line for `ok`, and the existing word-list test asserts it — the same check the pronoun `I` already has, so a future regeneration at a different level cannot drop it silently.

Rests on: `ok` being absent at every SCOWL level at or below 60 and `OK` present at 35, read from the generated list on 2026-09-05; the `ALWAYS_CAPITALISED` backstop and its reasoning, read from `scripts/generate-word-list.py` on 2026-09-12.
Filed 2026-09-12 12:36, stamped by the queue tool.

#### The app screen has two keyboards on it and they type into different places [app-screen-two-keyboards]
The app screen draws Hexboard's preview board at the bottom, and since 2026-09-05 it also carries a real text field — the "What is wrong" box the problem report is written in. Tapping the preview board appends to a scratch line under the report; tapping the text field opens whichever keyboard the phone has selected. So the screen shows a keyboard that does not type into the field right above it.

Found on 2026-09-09 by the user while driving [verify-this-runs-build-on-device]: he typed on the preview board expecting the report box to fill, and the characters appeared below the button instead.

**Why it happened.** The preview board predates the report entry. It is development scaffolding — `MainActivity`'s own comment says so — and it exists to prove the config reaches the screen and that a tap lands on the key it was aimed at. It has no input connection and appends to a state variable. Nothing was wrong with that until a screen with a real field grew underneath it.

**Settled with you on 2026-09-12: the board becomes a picture of the chosen layout, sitting with the picker, and does not type at all.** The scratch line goes with the typing, leaving the report's own field as the only thing on the screen that takes input. A board that never responds is read as a picture rather than as a broken keyboard, which removes the confusion at its root; and it is the same call [landscape-reveal-neighbours] took the same day, for the same reason — a key you cannot properly use should not type. SPEC gained the sentence the same day.

**Why not simply delete the board, which the entry offered as the cheapest route.** Both of its original jobs — proving the config reaches the screen and that a tap lands on the key aimed at — are now done by typing on the real keyboard. But the layout picker built on 2026-09-05 gave it a second job nobody noticed it had acquired: it is the only place a layout can be looked at before it is chosen, and there are seven of them. Deleting it would take that away to fix a confusion that a non-typing board also fixes.

**Options already refused, each with what defeated it.** Wiring the preview to whatever field has focus — the most work, and it produces two keyboards on screen at once, since Android raises the real one when the field is focused. Moving the report to a screen of its own — adds navigation to an app that has none, and leaves the same confusion wherever the two next sit together. Deleting the board — loses the layout preview the picker wants.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt` — the board is drawn without its key callback and without the scratch line, moved to sit with the layout picker; the development-scaffolding comment is replaced by what it now is.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/LayoutSwitchingUiTest.kt` — the existing picker test gains the observation below.

**The observation that shows it landed:** the picker test taps a key on the app screen's board and asserts that nothing is appended anywhere on the screen, and that choosing a different layout still changes the board that is drawn — so the preview is proved inert and still a preview.

Rests on: the board's callback and scratch line living in `MainActivity`, and its own comment describing it as development scaffolding, read on 2026-09-09; the picker being on the same screen, built 2026-09-05.
Filed 2026-09-09 09:30, stamped by the queue tool.

#### Hexboard is hard to find in the keyboard switcher, because its own name is the small line [switcher-subtype-label]
Android's "Change Keyboard" card shows each entry's subtype label in large text with the app's name small beneath it. Hexboard's subtype label is "English (QWERTY)", so its row reads "English (QWERTY)" large and "Hexboard" small — and someone looking for the word Hexboard scans past it.

Found on the Pixel 6 on 2026-09-09 while driving [verify-this-runs-build-on-device]. The user was told to switch to Hexboard, opened the card, and reported that Hexboard was not on it. It was, in third place. This is the first time anyone has used that card to reach Hexboard.

**Why it reads that way.** `ime_subtype_label` in `android/app/src/main/res/values/strings.xml` is "English (QWERTY)". Gboard's two rows on the same card read "English (Australia)" with "QWERTY" and "Handwriting" beneath, so the convention Android expects is a language in the large line and a variant in the small one — and "Hexboard" is neither.

**Settled with you on 2026-09-12: one subtype, labelled "Hexboard".** The row then carries the word a person is scanning for in the line they scan. It repeats the app name on the second line, which is the price of being findable and costs nothing else: with a single subtype there is no variant for the large line to distinguish, so the convention it would otherwise follow — a language large, a variant small, as Gboard's two rows do — has nothing to say here.

**The question underneath was answered the same day: Hexboard does not declare a subtype per layout.** Subtypes are how an input method exposes languages to the system, so seven of them would put a second language chooser in Android's own switcher. SPEC deliberately puts that choice in the app's settings so no typing gesture is spent on it, and two choosers for one setting is how they come to disagree — with the switcher's being the one nobody maintains. This also answers why "English (QWERTY)" had to change regardless of findability: since the picker shipped on 2026-09-05 one install types seven languages, so a subtype announcing English is a claim the app no longer honours.

**What the build changes.**
- `android/app/src/main/res/values/strings.xml` — `ime_subtype_label` becomes "Hexboard".

**The observation that shows it landed:** `ime_subtype_label` reads "Hexboard" and the subtype declaration still declares exactly one subtype. How the card renders with the same word on both lines is not something this machine can see, so it wants a look at the Change Keyboard card — recorded here as a check to make rather than as a fact anyone has established. [judge-drawing-values-on-phone] is the sitting that makes it, held against this item and three others. That ordering is written on all five entries.

**Options already refused, each with what defeated it.** Labelling it "English" — matches Gboard's shape and repeats the false claim, since the install types seven languages. A subtype per layout — a second language chooser in the system switcher, against SPEC's decision to keep that choice in the app. Leaving it — the reported symptom is a person failing to find the keyboard they were looking at.

Rests on: the card showing the subtype label large and the app name small, and Gboard's two rows, both observed on the Pixel 6 on 2026-09-09; `ime_subtype_label`'s current value, read from `strings.xml` the same day.
Filed 2026-09-09 09:46, stamped by the queue tool.

#### CLAUDE.md still says emoji are reached by swiping down [claude-md-swipe-wording]
`CLAUDE.md`'s project rules describe "emoji panels reached by vertical swipe down". On the phone they are reached by dragging the board *up*, confirmed by the user on 2026-09-09.

Split out of [spec-emoji-swipe-wording] on 2026-09-12, which fixed the same wrong phrase in SPEC the same day. It is a separate item for one reason: a planning session may not write `CLAUDE.md`, the same reason [settings-steps-name-a-search] exists as its own item.

**Why the phrase is wrong in a way that is easy to miss.** The emoji panels sit below the letters in the vertical stack, so a reader moves *down the stack* to reach them — and moving down a stack means dragging the content up. The sentence describes where the panels sit and reads as an instruction for the hand. A walkthrough written from it on 2026-09-09 asked for the wrong gesture, which is how it was found.

**What the build changes.**
- `CLAUDE.md` — the project-rules paragraph's swipe sentence reworded to say what the finger does, matching the wording SPEC now carries: the three letter panels sit side by side in the order RARE, QWERTY, SYMBOLS with QWERTY home, dragging the board left reaching SYMBOLS and right reaching RARE; the emoji panels sitting below the letters, reached by dragging the board up.

**The observation that shows it landed:** a grep of `CLAUDE.md` for "swipe down" returns nothing, and the paragraph names what the finger does in both directions.

Rests on: the emoji panels being page 1 of a `VerticalPager` with the letters at page 0, read from `KeyboardPanel.kt` and confirmed by hand on the Pixel 6 on 2026-09-09; SPEC's matching wording, written on 2026-09-12.
Filed 2026-09-12 12:45, stamped by the queue tool.

#### Stale build output still sits in the synced folder the relocation moved it out of [stale-android-build-dir]
`android/app/build/` holds `generated`, `intermediates`, `kotlin` and `outputs`, all dated 3 September 2026. Nothing has written there since: [build-output-off-drive] moved the build output to the path named in `android/local.properties`, and the build of 2026-09-09 wrote entirely to that path, confirmed by checking both folders after it ran.

Noticed at the close of 2026-09-09, checking that the relocation had actually taken effect.

**It is the exact problem the relocation was for.** That folder sits inside the Google Drive folder this project lives in, so it is being synced — Gradle output being synced while Gradle writes it is one of the two reasons the relocation exists, and the Windows path-length ceiling is the other. Leftover output is not being written any more, so it cannot corrupt a build, but it is still bulk in a synced folder for no purpose.

**Deleting it is safe and nobody has confirmed it.** Build output is regenerable by definition and the folder is gitignored, so nothing is lost. The one thing worth a look first is whether the folder holds anything that is *not* build output — a stray file put there by hand would be invisible to git and gone with the directory.

Small, and it needs a person to say go rather than a rule: the method's own file-safety line is that a folder Claude did not create this session is never presumed rubbish.

**That look was done on 2026-09-12 and the folder is clean.** It holds exactly four top-level entries, every one a canonical Gradle output directory — `generated`, `intermediates`, `kotlin`, `outputs` — with nothing hand-placed among them or at the level below, and comes to 51 MB. `android/app/build` is the only stale build directory in the tree: there is no `android/build`, and `android/local.properties` still carries the `hexboard.buildDir` setting that sends output elsewhere, so deleting this one does not invite Gradle to recreate it. **You agreed to the deletion the same day**, which is the person saying go that the paragraph above asks for.

It was deliberately left for a build run rather than done in that planning session: a planning session may write only the project's own documents, and the route that would have allowed it there was offered and declined.

**What the build changes.**
- `android/app/build/` — deleted. Nothing else is touched.

**The observation that shows it landed:** `android/app/build` does not exist, and `android/local.properties` still names `hexboard.buildDir`, so the next build still writes off the synced folder.

Rests on: the folder's contents, size and dates, read on 2026-09-12; the relocation having taken effect, established by the build of 2026-09-09 writing entirely to the path in `local.properties` and confirmed by checking both folders after it ran.
Filed 2026-09-09 13:21, stamped by the queue tool.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
**Lifted on 2026-09-09.** [verify-this-runs-build-on-device] was driven to its end that day, so the build this checks is installed and running on the Pixel 6.
**Held again on 2026-09-04.** You deferred this during the build run of 2026-09-04 because that run's work is not on the phone, and nothing in the queue said so — so it kept being offered as ready. It now waits on [verify-this-runs-build-on-device], which installs that build. That ordering is written on both entries.

Captured by you. A check that Hexboard's keys behave properly for someone using a screen reader or switch access, run on the phone with those services turned on.

Two things to confirm: that nearest-centre routing still selects the key you aimed at while an accessibility service is intercepting touches, and that each key exposes an accessibility node with the right label and bounds. SPEC treats these as separate requirements, because accessibility services largely bypass raw-touch routing — so both must be correct and one passing says nothing about the other.

**Cleared to run on 2026-09-03**, when the install run put Hexboard on the Pixel 6 as a switched-on keyboard — the lift-condition this item had carried since 2026-08-20, TalkBack being untestable against a keyboard nobody has switched on. [first-installable-build] had been dropped from the hold on 2026-09-02, having shipped that day.

Why it cannot be Claude's: it needs accessibility services running on a real handset, and there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator].

**The walkthrough was written on 2026-09-03**, this item having had none — it said only "confirm two things with accessibility services active" and never said how to switch them on. It reaches Settings by searching rather than by a menu path, for the reason recorded in [physical-keyboard-handover]: the path this project wrote down for the install did not match the handset, and Android moves these screens between versions. Whether the search works on this handset is itself unverified, so step 1 keeps Accessibility as its stated fallback and says which route it establishes. [settings-steps-name-a-search] waits on that answer, from whichever of this item and [physical-keyboard-handover] you run first.

The walkthrough:
1. Open Settings and type `talkback` into its search box. Look for: a result that opens a screen with a TalkBack on/off switch — turn it on. If the search finds nothing useful, TalkBack lives under Accessibility. Nobody has run this search on the Pixel 6, so which of the two works is the first thing this step establishes. TalkBack will start speaking, and a tutorial may appear; dismiss it.
2. With TalkBack on, remember the gesture change: one tap now selects and announces, and a double-tap activates. Open anything with a text field and double-tap into it. Look for: Hexboard's keys appearing, and TalkBack announcing something when you touch one.
3. Drag one finger slowly across a row of keys without lifting. Look for: TalkBack naming each key as you cross it, and the name changing at roughly the point the circles meet rather than early or late.
4. Pick three keys spread across the board and, for each, tap it and then double-tap to enter it. Look for: the character that arrives matching the key TalkBack named.
5. Turn TalkBack off the same way you turned it on. Look for: normal tapping returning.
6. Report three things: any key TalkBack named wrongly or did not name at all, any place where the name changed noticeably before or after your finger crossed between two keys, and whether every character you entered matched what was announced.

Switch access is deliberately not in this walkthrough and is now [verify-switch-access-ondevice], filed on 2026-09-03. It needs its own setup, and it turned out to have a question of its own worth asking — whether the scan order makes sense across the zigzag — so bundling it here would have buried that. TalkBack and Switch Access exercise the same accessibility nodes, so this item covers the labelling and bounds half and that one covers reachability, order and scanning speed. That ordering is written on both entries.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

#### [user] Check the board under Switch Access, for reachability, scan order and how long a key takes [verify-switch-access-ondevice]
**Lifted on 2026-09-09.** [verify-this-runs-build-on-device] was driven to its end that day, so the build this scans is installed and running on the Pixel 6.
**Held on 2026-09-04.** You deferred this during the build run of 2026-09-04 because that run's work is not on the phone, and nothing in the queue said so. It now waits on [verify-this-runs-build-on-device], which installs that build. That ordering is written on both entries.

Switch Access is Android's way of using a phone without touching the screen: a physical button moves a highlight from one on-screen element to the next, and a second press selects whatever is highlighted. The switch can be a purpose-built button, a keyboard key, one of the phone's own volume keys, or a facial gesture read by the camera. This item checks that Hexboard's keys work under it.

**Why it exists.** SPEC's accessibility bullet names screen readers **and switch access** together, and says the per-key nodes are a separate requirement from nearest-centre routing. So a TalkBack pass proves half of that promise and this proves the other half. It was cut from [verify-a11y-ondevice] on 2026-09-03, where it had been named and left out for needing its own setup; that item's own note points here.

**The route to drive it, settled on 2026-09-03 after the obvious one turned out to defeat itself.** The usual cheap switch is a Bluetooth keyboard, and you have one — but Hexboard inherits `onEvaluateInputViewShown()` and hides whenever a hard keyboard connects, so that route would leave you scanning a board that is not on screen. Android's Switch Access takes three switch sources: an external USB or Bluetooth keyboard, the phone's own buttons including the volume keys, or Camera Switches driven by facial gestures. The volume keys need no hardware and connect nothing, so nothing hides. Read from Google's accessibility help pages on 2026-09-03.

**What this tests, and what it does not.** The switch source sits upstream of everything Hexboard does: whatever is pressed, Android walks the same accessibility tree with the same scanning machinery, and Hexboard never sees the switch at all. So reachability, order and labelling are the same whatever someone presses, and the volume keys stand in for the pressing rather than for the scanning. It tests nothing about anyone's actual switch hardware, and that limit is stated rather than glossed.

**The part with real information in it: scan order across the zigzag.** Odd columns sit half a key lower than even ones, so the board has no straight rows. A scan that traverses by position may hop down-and-up-and-down instead of running along each row, which would be tiring and confusing to use. Nobody has looked, and it is a property of this project's own geometry rather than a generic accessibility question.

**And the harsh case, which the two-switch test hides.** Switch Access can run on one switch, where the highlight advances on a timer and the press only selects. A letter panel carries around thirty keys, so on a timer the highlight could take a very long time to reach a key near the end. That is a problem a keyboard has and most apps do not. Raised by you on 2026-09-03, from asking what happens for a user whose switch is not set up the way this walkthrough assumes.

Why it cannot be Claude's: it needs accessibility services running on a real handset and a person watching a highlight move, and there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator].

The walkthrough:
1. Open Settings and type `switch access` into its search box. Look for: a result that opens a screen with a Switch Access on/off switch. If the search finds nothing useful, it lives under Accessibility. Nobody has run this search on the Pixel 6, so which of the two works is the first thing this step establishes.
2. Turn Switch Access on and follow its setup as far as assigning switches. Assign **volume up** to Next and **volume down** to Select. Look for: the setup accepting both keys, and a highlight box appearing on screen when you press volume up.
3. Open anything with a text field and bring Hexboard up. Press volume up repeatedly to walk the highlight across the board. Look for: every key taking the highlight in turn, and each one announced or shown with the character it types.
4. Watch the path the highlight takes across a row of keys. Look for: whether it runs along the row, or jumps between the higher and lower columns — that zigzag hop is the thing this step exists to catch, so describe the path rather than answering yes or no.
5. Press volume down on a highlighted key. Look for: that character arriving in the text field, and not a different one.
6. Go back into Switch Access settings and turn on auto-scan, so the highlight advances on a timer and only one switch is needed. Look for: the highlight moving by itself.
7. From the start of the board, time how long it takes the highlight to reach a key near the end of the bottom row. Look for: the number of seconds, which is the whole point of this step.
8. Turn Switch Access off. Look for: normal tapping returning.
9. Report five things: any key the highlight never reached or named wrongly, what the path looked like across the zigzag, whether the selected character was the one shown, the auto-scan time to a far key, and anything that made it awkward that these steps did not ask about.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

**What a bad result means, so the report is worth making.** A confusing scan order or an unacceptable auto-scan time is a design question about grouping the board for scanning — rows as groups, or panels as groups, so the highlight descends into a row rather than crossing every key. That would be its own work and is deliberately not designed here.

Rests on: Switch Access accepting the phone's own volume keys as switches, and offering auto-scan on a single switch, both read from Google's accessibility help pages on 2026-09-03 and not tried; `onEvaluateInputViewShown()`'s inherited hiding, read from Android's documentation on 2026-09-02, which is why the keyboard route was rejected; that the scan traverses by position rather than by declaration order, which is unverified and is exactly what step 4 looks at.

[verify-a11y-ondevice] covers the TalkBack half and names this item as the other; [physical-keyboard-handover] must not be run in the same sitting, since its Bluetooth keyboard hides the board this one scans. Those orderings are written on all three entries.

#### [user] Check Hexboard hides for a physical keyboard and comes back intact [physical-keyboard-handover]
**Lifted on 2026-09-09.** [verify-this-runs-build-on-device] was driven to its end that day, so the build this pairs a keyboard against is installed and running on the Pixel 6.
**Held again on 2026-09-04.** You deferred this during the build run of 2026-09-04 because that run's work is not on the phone, and nothing in the queue said so. It now waits on [verify-this-runs-build-on-device], which installs that build. That ordering is written on both entries.

**Cleared to run on 2026-09-03**, when the install run put Hexboard on the Pixel 6 as a switched-on keyboard, which is what step 1 below needs.

A test with a Bluetooth or USB keyboard paired to the Pixel 6: Hexboard should get out of the way while it is connected, and come back working when it is not.

Raised by you on 2026-09-02, asking whether interactivity with physical keyboards needs testing at all and what the standard even is.

**The standard is Android's and Hexboard already has it, which is what narrowed this to one check.** `InputMethodService` decides whether to draw its input view in `onEvaluateInputViewShown()`, and the default answer is to show the keyboard unless a hard keyboard is available. `HexboardImeService` extends `InputMethodService` and does not override that method, so the hiding is inherited rather than written. The trigger is the keyboard connecting, not typing starting somewhere else — the platform re-evaluates on the configuration change a pairing produces. Read from Android's documentation on 2026-09-02, not run.

**So what is worth testing is not the hiding but the surviving.** Hexboard's input view is Compose, hosted with lifecycle, view-model and saved-state owners installed by hand on the view and its decor view — the arrangement [first-installable-build] had to design around because it is the known crash trap. The platform destroys and recreates that view when a keyboard connects and disconnects, and nothing has ever exercised that path. Nothing here makes a failure likely; it is untested rather than suspected.

**A feature this test deliberately does not ask for.** Keyboards like Gboard offer a setting to keep the on-screen keyboard visible while a physical one is attached, by overriding that same method to return true on the user's say-so. That is a feature rather than a standard, nobody has asked for it, and this item is not it.

Why it cannot be Claude's: it needs a physical keyboard paired to a phone, and there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator] and [install-and-enable-on-pixel]. You confirmed on 2026-09-02 that you have a keyboard to pair, which is what made this worth filing at all.

The walkthrough:
1. With Hexboard switched on and showing, open anything with a text field and tap into it. Look for: the circular keys in zig-zag rows, as usual.
2. Put the physical keyboard into pairing mode, then open Settings and type `bluetooth` into its search box. Look for: a result that opens the Bluetooth device list, with your keyboard offered as something to pair with. If the search finds nothing useful, the list lives under Connected devices. Nobody has run this search on the Pixel 6, so which of the two works is the first thing this step establishes.
3. Pair it, then return to the text field. Look for: Hexboard's keys disappearing from the screen, leaving the text field with more room.
4. Type a few characters on the physical keyboard. Look for: the characters arriving in the text field.
5. Disconnect the keyboard — switch it off, or unpair it on the same Bluetooth screen. Look for: Hexboard's keys returning by themselves when you next tap into a text field.
6. With the board back, tap several keys and swipe sideways between panels. Look for: characters arriving as before, and the panels changing — a board that draws but does not respond is the failure this step is for.
7. Report four things: whether the board hid, whether it came back, whether it still worked afterwards, and anything the Run panel's log printed while you were doing it.

**Step 2 was rewritten on 2026-09-03 to reach Settings by searching rather than by a path.** It named Settings → Connected devices, which nobody had checked against the handset — and the install walkthrough's own written path turned out not to match the Pixel 6 when it was driven that day. Android moves these screens between versions and the search box does not, so a step naming a term to search and a screen to look for should survive what a menu path does not. That is reasoning rather than a checked fact: whether the search actually reaches the Bluetooth list on this handset is unverified, which is why the step keeps Connected devices as its stated fallback.

[settings-steps-name-a-search] is held against this item for exactly that reason: it would write the search-box instruction into `CLAUDE.md` as the standing rule for every future walkthrough, and driving this one is the cheapest thing that shows whether the search works. So say in your report which of the two routes got you there. That ordering is written on both entries.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

Do not run this in the same sitting as [verify-switch-access-ondevice]. The hiding this item tests is exactly what would defeat that one: a Bluetooth keyboard connected here takes Hexboard off the screen, and that item needs the board visible to scan it. That is why it drives Switch Access from the volume keys instead. That ordering is written on both entries.

Rests on: `onEvaluateInputViewShown()`'s default behaviour, read from Android's documentation on 2026-09-02 and not run; that the platform destroys and recreates the input view on a keyboard connection rather than merely hiding it, which is the reason for the test and is itself what the test would establish.

#### [user] Speak into the prompter and say whether it made you hesitate normally [prompter-elicits-natural-speech]
**Lifted on 2026-09-09.** [rsvp-dictation-prompter] built the page in the run of 2026-09-05 to 2026-09-09, so `planning/dictation-prompter.html` exists and step 1 below has something to open.
A short sitting with the prompter page open, to find out whether phrase-at-a-time speaker-paced prompting actually elicits ordinary speech — the claim everything else about it rests on.

**Why it exists.** [rsvp-dictation-prompter] builds the page; nothing about building it shows whether the idea works. That question needs a person speaking and noticing how they sounded, which is the whole of what this item is. It is the reason the page is being built cheaply rather than as part of the enrolment flow it is really for.

Why it cannot be Claude's: it needs someone to speak aloud and report how their own speech felt, which is a judgment only the speaker can make.

The walkthrough:
1. Open `planning/dictation-prompter.html` by double-clicking it. Look for: one short phrase, large, in the middle of an otherwise empty page.
2. Get a text field ready on your phone with dictation running — whichever keyboard you like, since this is about the prompting rather than the recogniser. Look for: the microphone listening and words arriving when you speak.
3. Work through one whole passage, pressing space for each new phrase and speaking it. Look for: whether you hesitated the way you do in ordinary speech, or slipped into a reading-aloud voice.
4. Do a second passage, this time deliberately trying to go fast. Look for: whether hurrying makes you read rather than speak — which is what would tell us the pacing has to stay unhurried.
5. Report three things: whether you sounded like yourself talking or like yourself reading, how many corrections the dictation needed compared with a normal message, and anything about the page itself that got in the way.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

**What each answer would mean, written now so two impressions are not data nobody knows how to read.** If it produced ordinary hesitant speech, the design carries over to [enrolment-prompter] intact and the remaining questions there are about passage content rather than about the mechanism. If it produced reading-aloud speech anyway, the mechanism does not do what it was designed to do, and the honest response is to say so and stop rather than to add refinements — the whole point of a cheap trial is that it is allowed to fail. If it landed somewhere between, the thing to record is what made the difference, since that is what a later design would have to control for.

[rsvp-dictation-prompter] builds the page; [enrolment-prompter] is what this trial is protecting from being built on a wrong premise. Those orderings are written on all three entries.

--- Cleared to run above this line ---

#### Settings steps name a search term rather than a menu path, as a CLAUDE.md rule [settings-steps-name-a-search]
Blocked by: [physical-keyboard-handover]
One sentence added to `CLAUDE.md`'s project rules, saying how a walkthrough step should send someone into Android's Settings.

**Why it exists.** The install walkthrough of 2026-09-02 wrote a five-level menu path — Settings → System → Languages & input → On-screen keyboard → Manage on-screen keyboards — and when it was driven on 2026-09-03 the Pixel 6 had no such path. The destination screen was titled **Keyboard apps**. The step's outcome was reached anyway, because the person driving it went and found the screen, which is exactly the kind of rescue a walkthrough must not depend on. Android moves these screens between versions, so a path written down is stale on a schedule nobody here controls.

**Why a rule rather than three corrections.** Two further walkthroughs had already inherited the same instinct — [physical-keyboard-handover] named Settings → Connected devices, and [verify-a11y-ondevice] asked for accessibility services to be on without saying how — and both were rewritten by hand on 2026-09-03. The instinct is what produces them, so the fix belongs at the level that governs how walkthrough steps are written rather than at each instance. `CLAUDE.md` already carries the neighbouring rule, added 2026-09-02, that a GUI step names something visible to click or a menu path with any shortcut only as an aside; this narrows that rule for the one surface where written paths have actually failed.

**Held against [physical-keyboard-handover], and the reason is that this rule rests on a fact nobody has checked.** Whether searching Android's Settings for a term actually reaches these screens on the Pixel 6 is unverified — no `adb` on this machine, no way to read that phone's Settings app, and the search was proposed rather than tried. Writing it into `CLAUDE.md` as the standing rule would make it govern every future walkthrough on the strength of a guess, which is the same mistake at one level up. [physical-keyboard-handover] and [verify-a11y-ondevice] both now open by searching, each carrying the older menu path as its stated fallback, so the first of them to be driven settles it. That ordering is written on both entries.

**What the build changes.**
- `CLAUDE.md` — one sentence in the project rules, beside the existing GUI-step sentence: a step sending the user into Android Settings names a term to type into the Settings search box and the screen title to look for, rather than a menu path.

**The observation that shows it landed:** `CLAUDE.md`'s project rules contain a sentence naming the Settings search box, and the existing GUI-step sentence is still there beside it rather than replaced.

**Options already refused.** Correcting the three walkthroughs and writing no rule — the instinct that produced them survives, and the next walkthrough repeats it; two of the three were already written before anyone noticed. Writing the rule now, ahead of the check — makes an unverified route binding on everything written afterwards. Replacing the existing GUI-step sentence rather than sitting beside it — that sentence governs every GUI surface, not just Settings, and Android Studio has no search box of this kind.

Rests on: the install walkthrough's path failing against the Pixel 6, and the destination screen being titled Keyboard apps, both reported by the user on 2026-09-03 from the drive itself; that Android's Settings search reaches these screens, which is **unverified as of 2026-09-03** and is what the hold exists for.

**A second edit to the same paragraph was accepted on 2026-09-05, deliberately.** [walkthrough-steps-quote-screen-text] adds a rule about quoting on-screen text exactly, in this same part of `CLAUDE.md`, and was cleared to run rather than merged with this one. It rests on nothing unverified — quoting text read from the source is correct by construction — where this item rests on a Settings-search route nobody has tried, and holding a sound rule behind an unsound one to save a second small edit is the wrong trade. That sentence is worded generally enough that this one will read as a narrowing of it rather than a third unrelated rule. That reasoning is written on both entries.

Filed on 2026-09-03 at 16:31, split out of the capture that reported the wrong Settings path because a planning session may not write `CLAUDE.md`. That capture's other half — correcting the two live walkthroughs — was done in the same session, and the capture was then deleted.

#### Ask the recogniser to punctuate what it hears, rather than correcting it afterwards [speech-output-correction]
Blocked by: [in-keyboard-voice-input]
Dictated text arrives with punctuation and capitalisation, because the recognition request asks the platform for them. There is no correction stage on the finished transcript at all.

Captured by you on 2026-09-02 as "correcting what the speech recogniser returns", separate from the keyboard's own autocorrect by your instruction, with the target being dictation as good as Gboard's. **Rewritten on 2026-09-04**, when the measurement it was waiting on came back and turned a large open question into a flag on a request.

**What the comparison found, and what it removed from this item.** [recogniser-gap-comparison] was driven on 2026-09-04. The transcripts through Gboard and through this project's own on-device recogniser were basically identical, and the single difference was that **Gboard adds punctuation**. So the recognition-quality half of this item has nothing in it: there is no accuracy gap to close, and the whole of what is left is formatting. The item had been written expecting the opposite and carried three routes for closing a gap; two of them are gone with the gap.

**The remaining question was whether punctuation is something a finished transcript can recover, and it turns out not to be the question at all.** Android's speech recogniser takes `EXTRA_ENABLE_FORMATTING` from API 33, with `FORMATTING_OPTIMIZE_QUALITY` and `FORMATTING_OPTIMIZE_LATENCY` to choose between, and `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION` to stop punctuation appearing after the last word of a partial result. So the punctuation happens inside recognition, where the research said it has to — `workshop/resources/research/gboard-speech-correction.md` established that Gboard has no separate autocorrect stage either, correction happening inside an all-neural recogniser. This project asks for the same thing through a documented flag rather than rebuilding it.

**Quality rather than latency, chosen on 2026-09-04.** [in-keyboard-voice-input] makes the microphone press-and-hold with one utterance per hold, so the text lands when the thumb lifts rather than streaming in word by word. Nobody is watching for the next word to appear, which is what latency optimisation buys. If partial results are ever shown while the hold is in progress, `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION` is what stops a comma flickering at the end of every partial.

**The cost, stated rather than glossed.** These extras are API 33. On-device recognition itself starts at API 31, so voice input is already unavailable below that; this adds a narrower band — devices on API 31 and 32 get dictation with no punctuation. The keyboard does not fail there and does not fall back to anything: it simply does not ask for formatting, and the words arrive unpunctuated. That is a smaller gap than the API 26–30 one already accepted for voice input itself, and it is accepted on the same reasoning.

**Why SPEC's correctly-spelled principle is not in tension with this**, which is worth stating because it looks as though it should be. That principle bars reaching into a finished word the user spelled — an apostrophe added to "its" is the case it came from. Asking the recogniser to punctuate is not that: nothing the user typed is touched, and the punctuation is part of what recognition produces rather than an edit applied to it afterwards. The principle's own wording already anticipated this, permitting punctuation and capitalisation of a *dictated* passage as something the recogniser did not supply rather than a correction of anything typed.

**The storage tension this item recorded has gone, and the reason should not be lost.** The item held that matching Gboard might mean keeping a per-user record of what someone says and how they fix it, which is the class of storage this project refuses everywhere else. Nothing in this design stores anything: a flag on a request holds no transcript. What would have needed storage is biasing recognition toward the user's own vocabulary, and that is split out as [dictation-biasing-saved-words] — where it turns out not to need a transcript either, since Android takes an explicit list of strings.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/VoiceInput.kt` — the recognition request sets `EXTRA_ENABLE_FORMATTING` to `FORMATTING_OPTIMIZE_QUALITY` on API 33 and above, and `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION` wherever partial results are shown. Below API 33 neither is set and the request is otherwise unchanged.
- `android/app/src/test/java/tech/flintcraft/hexboard/VoiceFormattingTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt`, to confirm the committed text needs no post-processing once the recogniser is formatting.

**The observation that shows it landed:** `VoiceFormattingTest` passes, asserting that a request built for API 33 or above carries the formatting extra set to the quality value, and that a request built for a lower API level carries neither formatting extra — so the gate is checkable rather than asserted. Whether the punctuation is actually any good is a judgment on the phone, and belongs to whoever next dictates through the keyboard rather than to a test.

**Options already refused, each with what defeated it.** A corrector applied to the finished transcript — the audio and every rejected alternative are gone by then, so it can tidy output but cannot close a recognition gap; and there is no gap to close. Correcting the transcript against the saved-word list — biasing does the same job inside recognition, where it is worth more. Optimising formatting for latency — nobody is watching partial text under a press-and-hold microphone. Adding punctuation ourselves with rules over the finished text — that is the corrector above wearing a smaller hat, and it would reach into text the recogniser produced without knowing where the speaker paused.

Cites research: `workshop/resources/research/gboard-speech-correction.md`, which states plainly that its architecture papers are from 2019 and 2020 — sound on where correction happens, not a description of what Gboard ships today.

Rests on: `EXTRA_ENABLE_FORMATTING`, `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION`, `FORMATTING_OPTIMIZE_QUALITY` and `FORMATTING_OPTIMIZE_LATENCY` all arriving in API 33, read from Android's own API 33 difference report on 2026-09-04 and not run; the two recognisers being equivalent but for punctuation, established by the drive of [recogniser-gap-comparison] on 2026-09-04 — on read passages, which that drive itself established is the easy case; on-device recognition starting at API 31, read from Android's documentation on 2026-09-01.

Held against [in-keyboard-voice-input], which creates the recognition request this item configures — there is nothing to set a flag on until that exists. That ordering is written on both entries.

**An observation of yours from 2026-09-02 that this item no longer carries.** You noticed "rose" where you had meant "rows" in dictated text, tapped it, and were offered "rows" — and asked whether Gboard tags which words arrived by voice. The lookup found Google describing its proofreading as checking typed, pasted and dictated text alike, so an origin-blind proofreader explains it with no provenance tracking at all; nothing found describes Gboard tagging words by origin, recorded as *not found* rather than established absent, on your own caution that the observation is ambiguous between the two explanations. That affordance is [tap-word-alternatives], which was designed out on 2026-09-04 around homophones from CMUdict and needs no record of what was dictated. That ordering is written on both entries.

#### Reach the whole emoji list, not just the 250 the panels hold [emoji-panel-reach]
Blocked by: [persistent-clipboard]
One of the emoji panels' 250 slots becomes a control that swaps the board area for a scrolling grid of every emoji Unicode publishes, in its own groups; tapping one types it and returns to the keys.

Filed by the build of [emoji-panels] on 2026-09-04 at 12:44, which found the reach and could not write the SPEC sentence a build may not write. **Designed out on 2026-09-05, and SPEC's sentence written the same day.**

**What the build found.** `resources/emoji-test.txt` at Unicode 16.0 carries 3,781 fully-qualified emoji. The board has five panels of fifty slots — ten columns by five rows, the prototype's arrangement — so 250 are reachable and the remaining 3,531 sit in the bundled file and on no panel.

**Why that was not a defect in the build.** The item said to slice the parsed list into five panels in the file's own order and named the prototype as the reference for the arrangement. The prototype fills 250 slots from the front of the list, centre panel first and the outer two reversed, so that moving outward from home in either direction moves further down the list. The build did exactly that, and CLDR order is roughly commonest-first, so the 250 shown are the ones most likely to be wanted.

**Why it is still worth fixing.** A keyboard offering a fifteenth of the emoji that exist is a limitation a user meets the first time they want a flag, a less common animal or a food that is not in the top 250 — and today there is no way to type it at all.

**One of the three feared costs turned out not to exist.** This item was filed saying scrolling panels would break the fixed-height promise the clipboard screen was designed around. They would not: [persistent-clipboard] settled precisely this shape on 2026-09-02 — the board area is replaced by a scrolling list while the keyboard's total height never changes, and the same control returns to the keys. That is a browser inside a fixed-height board rather than a scrolling panel, and the mechanism this needs was already designed for another feature.

**So the answer costs one emoji slot.** The five panels stay exactly as they are — 250 commonest, no scrolling, fixed positions a user can learn — and one of those slots carries a control that opens the browser. The two alternatives this item feared both cost more: more than five panels spends a swipe, and a search field spends space in the row above the keys, which already has the microphone at its right end, the clipboard button at its left and suggestions in the middle.

**Nothing here is governed by the manifest rules**, which is worth stating because it looks as though it should be. SPEC puts the emoji panels outside the manifest's scope deliberately: their content comes from Unicode's published list and display order rather than an inventory curated here, so the four rules — no key lost, no unresolved duplicates, no silent changes, empty slots filled — do not bind them, and spending one slot on a control is not an eviction under any rule.

**Held against [persistent-clipboard], and it is a real hold rather than tidiness.** That item builds the swap of the board area for a scrolling view, and this reuses it. Two independent implementations of the same swap is how a keyboard ends up with two slightly different ways of covering its own keys, which a user notices and cannot name.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/EmojiCatalogue.kt` — exposes the whole parsed list with Unicode's own group and subgroup for each entry, rather than only the 250 sliced into panels.
- `android/app/src/main/java/tech/flintcraft/hexboard/EmojiBrowser.kt` — new. The scrolling grid drawn into the board area, grouped by Unicode's groups with their names as headings, reusing the board-area swap [persistent-clipboard] builds. A tap types the emoji and returns to the keys.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — one slot on an emoji panel draws the control instead of an emoji, and opens the browser.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/EmojiBrowserUiTest.kt` — new, carrying the observation below.

Reads but does not change: `resources/emoji-test.txt`, for the group structure the browser headings come from.

**The observation that shows it landed:** the instrumented test asserts that the control opens the browser without the keyboard's total height changing, that the browser can be scrolled to an emoji outside the first 250 and tapping it commits that character and returns to the keys, and that the five panels still carry 249 emoji plus the control. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** More than five panels — spends a swipe, on a board whose horizontal swipe already means "change letter panel" and whose vertical swipe already means "emoji". A search field in the row above the keys — that row has three claimants already. Scrolling the panels themselves — would change where a learned emoji sits, which is the thing fixed panels buy. Shipping only 250 and saying so in SPEC — considered on 2026-09-05 and rejected: a user who wants the 251st has no route at all, and the browser costs one slot.

Rests on: 3,781 fully-qualified emoji at Unicode 16.0 and the five-panel arrangement, both established by the build of [emoji-panels] on 2026-09-04; the board-area swap keeping the keyboard's height constant, designed in [persistent-clipboard] on 2026-09-02 and not yet built; SPEC placing the emoji panels outside the manifest rules, read from `SPEC.md` on 2026-09-05; that `emoji-test.txt` carries group and subgroup structure, read by the [emoji-panels] build on 2026-09-04.

Interacts with the `emoji-data-refresh` cycle in `CYCLES.md`, which replaces the bundled file when Unicode ships a version — a browser over the whole list makes a stale file more visible rather than less, since every emoji is now reachable. That connection is written in both places.

#### Words the user deliberately saves, kept on the phone and readable by them [predictive-saved-words]
Red flag · State: cleared
Blocked by: [uniform-neighbours-predictive]
A store of words the user has explicitly chosen to add, which the autocorrect treats as dictionary words so it stops correcting them.

**Split out of [uniform-neighbours-predictive] on 2026-09-04**, when that item was decomposed into buildable work. It is the one part of the engine that stores anything, so it carries its own risk and its own consent trail rather than riding inside the correction item.

**Why it exists, in your framing.** A keyboard that cannot learn a surname or a street name is worse to use. Without this the engine mangles a name every time it is typed, since a word not in the shipped list is exactly what the correction machinery reaches for.

**The privacy position, and it changed footing on 2026-09-01 — this is the part a later session must not misread.** The original decision of 2026-08-20 was a fixed dictionary learning from nothing and recording nothing, chosen so the engine never holds a record of your writing on the device, which is the exposure Android's own keyboard warning is about. That designed the risk out. On 2026-09-01 you were told plainly that saving words means the device holds a file of words you typed, that this is smaller than silent learning because each word is a deliberate act rather than a harvest, and that it stops the "stores nothing" claim being true. You chose the saved-word store knowingly. So the flag is cleared **by your informed consent to a smaller risk**, not by design-out, and three constraints came with that consent and bind the build:

- the list grows only when you deliberately save a word — nothing is added by observing what is typed;
- it never leaves the device;
- you can read it and delete from it.

**How a word gets saved, settled with you on 2026-09-12: by tapping an offer that appears in the row above the keys the moment you undo a correction.** SPEC already has that moment — a correction is reverted by pressing backspace immediately after it lands — and the press is you telling the keyboard the word was right and it was wrong, which is the same information a save carries. So the offer rides an interaction that already exists: it spends no key, no gesture and no vertical space, and the row is empty at exactly that instant. A second route adds a word by hand on the settings screen.

Three alternatives lost, each with what defeated it. A long-press on a word already in the text collides with Android's own text selection, and a plain tap is taken by [tap-word-alternatives], which offers homophones there. A dedicated key evicts a punctuation key, which SPEC's manifest rules make a real loss rather than a placement. The settings screen alone means leaving the text field to save a word, which is the failure this project designs against — so it is the second route rather than the only one.

**What the list offers, settled with you the same day: read, delete, add by hand, and clear-all — no export and no lock.** The clear-all is the one that needs its reason recorded, because [persistent-clipboard] deliberately refuses one and copying that across would be wrong. The clipboard's reason is a threat model: an emptied clipboard is itself a disclosure, since someone looking over your shoulder wonders why it is empty. A saved-word list is not a record of what you did — it is a list you curated, and an empty one is the state every install starts in, so emptying it discloses nothing. An export was refused as a route off the device, which is the condition this store's consent came with; a lock as security theatre, the keyboard having no notion of who is using it and Android's screen lock already standing in front of the phone.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/SavedWords.kt` — new. The list on the device, with add, read, delete and clear. Nothing leaves the device and nothing is added except by an explicit save.
- `android/app/src/main/java/tech/flintcraft/hexboard/Autocorrect.kt` — consults the saved list alongside the shipped word list, so a saved word is never corrected and a near-miss corrects *toward* one. That is what treating them as dictionary words has to mean, or typing a surname is still mangled.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — publishes the save offer at the instant a correction is undone, and commits the word when it is tapped.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the row above the keys draws that offer and reports the tap.
- `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt` — the settings screen gains the word-list surface: the words, delete, add by hand, clear-all.
- `android/app/src/test/java/tech/flintcraft/hexboard/SavedWordsTest.kt` — new, carrying the first half of the observation.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/SavedWordsUiTest.kt` — new, carrying the second half.

**The observation that shows it landed:** `SavedWordsTest` passes, asserting that a saved word is never corrected, that a word one neighbour-substitution from a saved word corrects to it, that delete and clear remove words, and that nothing enters the list by any route but an explicit save. `SavedWordsUiTest` asserts the offer appears after an undo and at no other moment, that tapping it adds the word, and that the settings screen lists, deletes and clears. Nothing here can run either, so running them is Android Studio's, on the Pixel 6.

**Held against [uniform-neighbours-predictive]** because the undo the save offer hangs off does not exist until the correction engine does, and the store's whole purpose is to be consulted by it. That ordering is written on both entries.

Rests on: SPEC's predictive-text principle, which already states that the engine does not learn and that the saved-word list grows only by a deliberate save, stays on the device and is the user's to read and delete — read from `SPEC.md` on 2026-09-04; the consent exchange of 2026-09-01, recorded in that session's record under [uniform-neighbours-predictive]; that a correction is undone by the next backspace, which is [uniform-neighbours-predictive]'s own specification and is not built, so the moment this offer hangs off does not exist yet; that the row above the keys exists and runs, built by [suggestion-strip] and confirmed on the Pixel 6 by the drive of [verify-this-runs-build-on-device] on 2026-09-09; Android's long-press on text being taken by its own selection, which is ordinary platform behaviour and was reasoned from rather than tried.

#### Spare width sideways should reveal the neighbouring panels, not sit empty [landscape-reveal-neighbours]
Blocked by: [panel-seam-gap]
Raised by you on 2026-09-09, turning the keyboard sideways for the first time. The board occupies roughly the left third of the screen and the rest is empty; your account is that however much of the RARE and SYMBOLS panels fits in that spare width should be showing.

**Why the space appeared, which is new as of the same day.** The radius used to be solved from the width alone, so the board was always exactly as wide as the screen and there was never any spare. [landscape-board-height] made the radius the smaller of the width's answer and the height's, and sideways the height wins — about 13dp against the 34dp the width would allow. So the board is now narrower than the screen for the first time, and every key position is measured from the board's left edge, so what is left over collects on the right.

**Your answer is better than the two obvious ones, and it is worth saying why.** Centring the board would make the emptiness symmetrical rather than remove it. Spreading the columns to fill the width is barred outright — it breaks the hexagonal packing SPEC calls inviolable, and `centringIndent` already carries that refusal for the narrow-panel case. Showing the neighbouring panels uses the space for the thing the space is next to, and it costs no gesture: the panels are already laid out side by side in a pager, and the swipe that reaches them is unchanged.

**It makes [panel-seam-gap] matter more, which is the thing to notice before building it.** That seam is currently visible only during a swipe. If the neighbours are permanently on screen, the gap between the last column of one panel and the first of the next is permanently on screen too — so the seam probably has to be closed first, or the reveal will look like three boards rather than one.

**And that item was settled on 2026-09-12 in a way this one may want to reopen.** The seam is closed by overlapping full-width pager pages with a negative page spacing. The route refused there was making each page narrower than the screen, which fixes the same arithmetic and leaves a sliver of the neighbouring panel showing — refused as a change to how the board looks at rest rather than a repair, which is precisely what this item asks for. So the honest position is that this item is the case for the refused route, and designing it means weighing the two again rather than building on top of the first. That reasoning is written on both entries.

**What a tap on a revealed key does, settled by you on 2026-09-12: nothing at all.** A revealed key is drawn and is inert. Your reasons, and they are yours rather than a summary of mine: an accidental press must not scroll the screen about, which is what a tap-to-switch-panel would produce from the very presses most likely to be accidental; a key that "can't be seen properly or fully" should not type; those keys are the rarer characters anyway, so typing from them serves uses that probably do not exist; and some people will find the missing choice annoying, which you weighed and accepted because inertness is what "entrains the swiping of the board" and pushes the gesture's discoverability a little further.

Claude recommended the opposite — a tap moving to that key's panel — and lost on those grounds. The cost of the decision taken, recorded so the symptom is diagnosable rather than mysterious: a tap that does nothing is the thing people retry, so if the revealed keys ever read as a broken keyboard, this is where that came from.

**Typing from a revealed key was refused on a second ground as well**, which matters because it would have been the expensive way to reach the same place: nearest-centre routing is defined within one panel, and the correction engine's neighbour table deliberately excludes sets that span panels, settled when [predictive-neighbour-table] was built on 2026-09-09. Tappable neighbours would reopen shipped, tested geometry for an edge case.

**The current panel is centred, with its neighbours either side.** That follows from the reveal rather than being a separate choice: pitch-width pages plus pager padding of half the leftover width puts QWERTY in the middle of a sideways screen with RARE and SYMBOLS showing to its left and right, which also removes the bunched-to-the-left look the phone showed on 2026-09-09. In portrait the leftover is a few dp, so the same rule leaves the board where it already sits.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the pager's content padding becomes half the difference between the viewport and a page, so the current panel is centred and the neighbours occupy the rest; keys drawn on a page that is not the current one take no touch input.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/LandscapeRevealUiTest.kt` — new, carrying the second half of the observation.
- `android/app/src/test/java/tech/flintcraft/hexboard/LandscapeRevealTest.kt` — new, carrying the first half.

**The observation that shows it landed:** `LandscapeRevealTest` passes, asserting that for a viewport wider than a page the padding centres the current page and the leftover is split equally, and that for a viewport the width of a page the padding is zero. `LandscapeRevealUiTest` puts the device in landscape, asserts keys from the neighbouring panels are drawn either side of the current one, taps one of them and asserts that no character arrives and the panel does not change. Nothing here can run either, so running them is Android Studio's, on the Pixel 6 — and the instrumented half cannot be trusted while [instrumented-tests-no-composition] stands.

Rests on: pages being a whole number of column pitches wide, which is [panel-seam-gap]'s settled mechanism and is not built; `HorizontalPager` accepting content padding that centres a fixed-size page, read from Compose's pager documentation on 2026-09-12 and not run; the sideways radius being about 13dp against the 34dp the width alone would allow, computed by [landscape-board-height] on 2026-09-09.

Held against [panel-seam-gap], which makes pages board-width; there is nothing to centre and nothing to reveal until that lands. That ordering is written on both entries.

[split-layout-wide-screens] answered the same screen a different way — the board separating into two halves under the thumbs — and was **deleted on 2026-09-12**, so this item is the only answer for a wide screen. Its premise of an unreachable middle did not survive the height bound of 2026-09-09, which draws a sideways board about a third of the screen's width; a tablet in landscape solves against the 34dp radius cap and comes out centred and whole in the same way. Its full design, and the five alternatives it refused, are in the record under that slug. A tablet case arriving later restarts from that design rather than from nothing.
Filed 2026-09-09 10:03, stamped by the queue tool.

#### Bias dictation toward the words the user has saved, and nothing else [dictation-biasing-saved-words]
Blocked by: [predictive-saved-words], [in-keyboard-voice-input]
Hand the speech recogniser the user's own saved words so it is likelier to hear them — a surname, a street name, a term from their work — without storing anything about what they have said.

Split out of [speech-output-correction] on 2026-09-04, when that item narrowed to asking the platform to punctuate. Biasing was one of the three routes it carried and is the only one still live, and it is a different feature rather than a detail of that one.

**What makes it possible, and it answers a question the original item recorded as unresearched.** That item asked whether the on-device API permits biasing at all. It does: Android added `EXTRA_BIASING_STRINGS` in API 33 — "optional list of strings, towards which the recognizer should bias the recognition results". So the influence happens inside recognition, which is where `workshop/resources/research/gboard-speech-correction.md` established that correction has to happen if it is to be worth anything.

**Why this is the version of personalised dictation this project can have.** Gboard's advantage here comes partly from keeping a per-user record of what someone says and how they correct it, which is the class of storage refused everywhere else here — predictive text keeps only words deliberately saved, and enrolment audio for [personal-voice-model] is destroyed after adaptation. Biasing needs none of that: it needs a list of words, and [predictive-saved-words] is already a list of words the user chose to keep. Nothing new is stored and nothing about speech is recorded.

**A second switch that is deliberately left alone.** API 33 also carries `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT`, "optional boolean to enable biasing towards device context" — which would plausibly bring the phone's contacts into recognition. It is not set, so it is not asked for. This is a privacy choice rather than a rule: SPEC's no-proper-nouns rule governs the autocorrect dictionary, where the harm is a typed word being turned into a name, and dictating a friend's name correctly is a benefit rather than that harm. So nothing forbids the device-context switch; it is left off because the conservative default matches everything else here, and turning it on would be the user's decision to make knowingly. Android's documentation never says what "device context" contains, which is a further reason not to opt into it blind.

**Designed out on 2026-09-12, when the reason it was a capture stopped being true.** It had said that [predictive-saved-words] was itself undesigned — nobody having settled how a word gets saved or where the list is read — so there was no list to hand the recogniser. Both were settled the same day: a word is saved by tapping the offer that appears when a correction is undone, and the settings screen carries the list with add, delete and clear. So the list has a design behind it and this can be described.

**Which words are sent, settled the same day.** The whole list, most-recently-saved first. Android's documentation describes the biasing list as a list of strings without stating a limit, so if the platform takes fewer than are offered, recency decides which survive — it is the best available proxy for what someone is about to dictate. Whether a cap exists at all is something the build finds out; nothing here depends on the answer beyond the ordering.

**The second switch stays off, and that is a privacy choice rather than a rule.** `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT` would plausibly pull the phone's contacts into recognition. SPEC's no-proper-nouns rule governs the autocorrect dictionary, where the harm is a typed word turned into a name; dictating a friend's name correctly is a benefit rather than that harm, so nothing forbids the switch. It is left off because Android never says what "device context" contains, and opting in blind is not what the rest of this keyboard does. Turning it on would be the user's decision, knowingly.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/VoiceInput.kt` — the recognition request sets `EXTRA_BIASING_STRINGS` to the saved words, most recent first, on API 33 and above, and sets `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT` nowhere. Below API 33 the request is unchanged.
- `android/app/src/test/java/tech/flintcraft/hexboard/VoiceBiasingTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/SavedWords.kt`, the list built by [predictive-saved-words].

**The observation that shows it landed:** `VoiceBiasingTest` passes, asserting that a request built for API 33 or above carries the saved words as the biasing extra in most-recent-first order, that a request built below API 33 carries neither biasing extra, and that the device-context extra is absent from every request — the last being the one worth a test rather than a comment, since it is a privacy choice that would otherwise be one line away from being reversed by accident.

**What is not established.** Whether the extras' defaults are what the documentation implies — they are described as optional and as enabling a behaviour, which reads as off unless set, without the default being stated outright. That is checkable on the phone once there is a microphone to check with, and it is worth checking rather than assuming, since being wrong about the device-context default would mean contacts influencing recognition without anyone choosing it.

Held below the line against [predictive-saved-words], which is the list, and [in-keyboard-voice-input], which is the recognition request this configures — there is nothing to bias and nothing to set it on until both exist. Those orderings are written on all three entries.

Rests on: `EXTRA_BIASING_STRINGS` and `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT` arriving in API 33, read from Android's API 33 difference report and the class reference on 2026-09-04 and not run; the recogniser being reachable at all on this handset, established by the drive of [recogniser-gap-comparison] on 2026-09-04.

[speech-output-correction] configures the same recognition request for punctuation and is the sibling of this one; [predictive-saved-words] is the list it would draw on. Those orderings are written on all three entries.

#### Russian layout has no cursor-right key, and nothing checks for one [russian-missing-cursor-right]
Blocked by: [russian-panel-gaps]
`resources/key-layout-ru.json` carries `⇤` cursor-left at row 2 col 0 and no `⇥` cursor-right anywhere. Every other shipped layout has both. So a Russian typist can step the caret left and not right.

Found on 2026-09-05 while building [first-batch-layouts], by running the five new configs through a check for the structural keys and then running the same check over the two configs already shipped.

**How it happened, as far as the file shows.** The Russian rows are eleven wide, so row 2 is `⇤` then nine letters then `⌫`, and the enter key moved down to row 3 col 10. Cursor-right had no slot left and appears simply to have been dropped rather than rehoused. The layout has never been typed on — [russian-layout-check] has sat dated a month out since 2026-09-02 — so nobody has missed it.

**The second half is the one that matters more.** `KeyLayoutValidationTest` checks slots, bounds, duplicates and accent lists, and checks nothing about a layout carrying the keys a keyboard needs. A layout missing backspace, enter or shift would pass every rule the project has. That is the check that would have caught this on the day the file landed, and it is worth more than the one-key fix.

So this is two pieces of work in one item: give Russian a cursor-right key, and assert in the validation test that every shipped layout carries each of the five structural actions.

**Where the key goes, settled with you on 2026-09-12: row 3 col 5, which the apostrophe holds today.** The Russian letter panel is completely full — unlike Spanish, which is also eleven wide and carries three empty positions — so the key has to come from somewhere. Russian orthography does not use the apostrophe, so on that row it is the key doing least work for a Russian typist; it is there because the row was transcribed wholesale from English rather than chosen for Russian. Everything else on the row earns its place: the two space bars are SPEC's, shift and enter are structural, and `? , ! " . -` are all used in Russian.

**No key is lost, which SPEC requires.** The apostrophe rehouses onto a Russian symbol panel: [russian-panel-gaps] fills those thirteen slots and now reserves one for it, written into that entry the same day. That is why this item waits on it — two builds writing the same slots would collide, and the fill would otherwise take all thirteen. That ordering is written on both entries.

**What the build changes.**
- `resources/key-layout-ru.json` — row 3 col 5 becomes cursor-right; the apostrophe takes the slot reserved for it by [russian-panel-gaps].
- `resources/key-manifest-ru.md` — regenerated from the config, never hand-edited, per SPEC.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` — a new assertion over every shipped config: each carries all five structural actions — shift, backspace, enter, cursor-left and cursor-right — exactly once.

**The observation that shows it landed:** `KeyLayoutValidationTest` passes over all seven configs with the new assertion, and fails against a config with any one of the five removed — which is the half worth more than the Russian fix, since it is what would have caught this on the day the file landed. The Russian manifest shows cursor-right on row 3 and the apostrophe on a symbol panel.

**Options already refused, each with what defeated it.** Evicting a different row-3 key — every other one is used in Russian, or is structural, or is a space bar SPEC requires. Dropping to one space bar to free a slot — SPEC's two space bars are a deliberate compromise, not spare capacity. Leaving Russian without cursor-right and asserting only the other four actions — it writes the defect into the check as though it were the design. Putting cursor-right behind a hold on cursor-left — holds on the cursor keys are taken, since SPEC has them repeat while held.

Rests on: the Russian letter panel being full and the apostrophe sitting at row 3 col 5 with no other route into the layout, both read from `resources/key-layout-ru.json` on 2026-09-12; the same read showing Spanish's three empty letter-panel slots, which is why "eleven wide" is not itself the reason; the apostrophe being absent from Russian orthography, which is a claim about the language rather than a read, and the in-app report route is the backstop for it.

#### Spanish's letter panel has three empty slots down its right edge [spanish-letter-panel-gaps]
Blocked by: [russian-panel-gaps]
`resources/key-layout-es.json` has no key at (0,10), (2,10) or (3,10) on its QWERTY panel. The board is eleven columns wide to fit ñ at row 1, and the rest of it is the ten-wide English arrangement, so the extra column is empty on three of its four rows.

Found on 2026-09-12 while settling [russian-missing-cursor-right], by reading every shipped config's letter panel to see which carried the five structural actions. Russian's panel turned out to be completely full and Spanish's turned out to have three holes; neither was what the read was for. SPEC's manifest rules say an empty slot is an opportunity rather than an acceptable gap, and these three sit on the panel a Spanish typist looks at most.

**Settled with you on 2026-09-12: ¿ at row 3 col 10, ¡ at row 2 col 10, and a º key at row 0 col 10 carrying ª behind it.** All four characters are reachable today, but only behind holds — `?` carries ¿, `!` carries ¡, and the ordinals sit in the accent lists of `a` and `o`. The inverted marks open every Spanish question and exclamation, so leaving them behind a hold makes required punctuation slower than optional punctuation; row 3 is the punctuation row, so ¿ sits among its relatives. The ordinals share one key because they are a pair used together and neither needs a slot of its own.

**º and ª come out of the `o` and `a` accent lists in the same move, and that is the opposite call to [curly-quote-double-route]'s.** That entry kept `“ ” ‘ ’` reachable both as SYMBOLS keys and behind holds, because freeing slots on `'` and `"` — which carry three and four alternatives — bought nothing. Here `a` and `o` carry eight each and are the two most crowded lists in the layout, so the same move buys something real. The reasoning is the same and the facts differ, which is why the outcomes differ.

**A knock-on, accepted knowingly.** [russian-panel-gaps]'s fill rule names ¡, ¿, ª and º as Spanish's fallback characters for its SYMBOLS panel. Promoting them here spends that fallback, so Spanish's SYMBOLS panel will finish with about two slots the rule cannot fill — which that rule leaves empty and reports rather than inventing something. That is the rule working as designed, and it is why this item waits on it: the two builds write the same config and the fill needs to know these four are gone.

**What the build changes.**
- `resources/key-layout-es.json` — keys added at (0,10) as º with ª behind it, (2,10) as ¡ and (3,10) as ¿; º removed from `o`'s alternates and ª from `a`'s.
- `resources/key-manifest-es.md` — regenerated from the config, never hand-edited, per SPEC.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` — the existing checks cover it; no new assertion, since the structural-actions assertion arrives with [russian-missing-cursor-right] and the no-empty-slots question is the fill rule's.

**The observation that shows it landed:** the Spanish manifest shows no empty position on its letter panel, ¿ and ¡ as keys rather than alternatives, and `a` and `o` carrying seven alternatives each rather than eight.

**Options already refused.** Leaving the three empty — SPEC calls an empty slot an opportunity rather than an acceptable gap, and these are on the most-used panel. Giving ª and º a slot each — they are a pair used together, and it would spend the row 0 slot for no gain. Keeping them in the accent lists as well as on the new key — the crowding on `a` and `o` is the whole reason the promotion is worth anything.

Rests on: the three empty positions and the current accent lists, read from `resources/key-layout-es.json` on 2026-09-12; ¿ and ¡ opening Spanish questions and exclamations, and ª and º being an ordinal pair, which are claims about the language rather than reads — the in-app report route is the backstop, the native-reader requirement having been removed from SPEC on 2026-09-04.
Filed 2026-09-12 12:39, stamped by the queue tool.

#### [user] Look at the four visual changes on the phone and say whether they read right [judge-drawing-values-on-phone]
Blocked by: [key-drawing-second-pass], [row-banding-too-weak], [panel-seam-gap], [switcher-subtype-label]
One sitting with the Pixel 6, after those four have been built, judging changes that were all settled by arithmetic and have never been seen.

Filed on 2026-09-12, at the rescan of the planning session that settled them. Each of the four ends by saying its value wants an eye on the phone, and none of them is that sitting — so the check existed as four sentences inside other items and as nothing anyone could run.

**What was decided by derivation rather than by looking.** The gradient now begins at half the radius instead of three quarters, so the soft edge is nearly twice as wide. Labels are sized against the key's own radius rather than its solid core, which makes them about a third larger and decouples them from the fade. The row band travels a third of the way to the lit colour instead of a fifth, and applies to letter keys only. The panel seam is closed by making each pager page a whole number of column pitches. And the keyboard switcher's row is relabelled "Hexboard". Every one is defensible and none is evidence.

**Why one sitting rather than four items.** The phone is in hand once and the build installed once; four walkthroughs would each open by asking for a build that is already running. It also lets the report compare rather than rule separately, which matters because a longer gradient and larger labels both change how a key reads, and whether the board is better is a judgment about the whole board.

Why it cannot be Claude's: it is a judgment about how something looks, made by the person whose complaint produced the changes, and there is no test for "reads as bands". Gradle, Java and adb are all absent from this machine, recorded in `TOOLS.md`.

The walkthrough:
1. Open the project in Android Studio with the Pixel 6 connected, and press Run. Look for: the app installing and its own screen appearing on the phone.
2. Open the keyboard switcher — the "Change Keyboard" card — and pick Hexboard. Look for: a row whose large line reads "Hexboard", and whether you found it without hunting.
3. Tap into any text field and look at the keys without typing. Look for: whether each key reads as a soft-edged circle rather than a disc with a thin ring, and whether the letters are large enough.
4. Look across the rows rather than at single keys. Look for: whether the alternating shades read as bands running left to right, or still as randomly coloured keys.
5. Swipe slowly from QWERTY to SYMBOLS and back. Look for: whether the columns keep their spacing across the join, so the two panels read as one board rather than two.
6. Report five things: the switcher row, the key edges, the label size, whether the banding reads as rows, and whether the join looks continuous — and for anything that is wrong, which direction it is wrong in, since these are values to move rather than faults to fix.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

**What a bad result means, written now so the report is worth making.** Each of the five has a named constant or arrangement behind it, so "too thin", "too small", "too faint" or "still a gap" each point at one thing to move rather than at a redesign. A result that says the board looks worse overall, rather than that one value is off, is the one worth stopping on — it would mean the changes interact, which nothing here has modelled.

Held against the four items it judges, so it lifts only when all of them have shipped. Those orderings are written on all five entries.
Filed 2026-09-12 12:50, stamped by the queue tool.

## Unprocessed

> Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Catch a complaint inside the app before it becomes a Play Store review [feedback-funnel-before-store]
Blocked by: [play-store-release]
A general route for telling Hexboard something is wrong — not just a wrong key on a layout — aimed at reaching people while the complaint is still fixable rather than after it has been left as a one-star review.

Captured by you on 2026-09-04, while settling how layout errors get found once the reader-confirmation requirement goes. Your aim in your own framing: capture people before they go to the Play Store, accepting that this may mean a lot of email, and that agent screening is what you would add if the load got heavy — a small price for a better listing.

**Why it is filed rather than built now.** [layout-error-report] is the narrow piece the SPEC change needed. This is the wider funnel, and Hexboard has no Play Store listing, no verified keyboard on a handset, and no users — so designing the shape of a complaint channel now would be designing months ahead of the thing it serves, against a volume nobody can estimate.

**The hold was repointed on 2026-09-12.** [layout-error-report] shipped in the build run that ended on 2026-09-09, so the narrow report exists and there is something to generalise from — but that was only half the deferral reason. The half that still stands is that there is no listing and nobody using the keyboard, and every question this entry leaves open is a question about a volume and an audience that do not exist. So it now waits on [play-store-release], filed the same day because nothing in the queue named the release and several entries lean on it. That ordering is written on both entries, and this returns by itself the session that release is taken up.

**What is already settled and carries over, so this is not started from nothing.** Three constraints come from [layout-error-report] and are not up for rediscovery: the report must be built from a fixed list of fields with no path from the text field into it; nothing is sent by the app itself, the person sending it from their own mail app after seeing the text; and the address lives in gitignored `local.properties` rather than in this public repository.

**And one policy line, read on 2026-09-04.** Google's in-app review guidance forbids asking the user any question before or while presenting the rating prompt, including "are you enjoying the app?" — filtering for happy reviewers inflates ratings and is prohibited. So the funnel may never be a mood check that routes unhappy people away from the rating flow. What is permitted is a standing report route that is always available and attached to no prompt. Any design here starts from that.

**What is genuinely open.** Whether the funnel is one entry or several by kind of problem; whether it lives only in the app or also somewhere reachable from the keyboard without spending a gesture; whether a report can carry a screenshot, and what that would mean for the no-typed-text rule when the screenshot is of a text field; and what screening looks like when volume arrives — your own suggestion of an agent is the starting point rather than a decided answer.

Rests on: Google's in-app review guidance, read on 2026-09-04, which is the kind of policy amended on a cycle and should be re-read before this is designed; the three constraints from [layout-error-report], which are design decisions of this project rather than external facts.

#### Deliver each language's word list after install rather than inside the app [dictionary-asset-packs]
Blocked by: [play-store-release]
A language's word list reaches the phone when that language is used, rather than every word list being carried by every install.

Filed on 2026-09-05, from the plan settled with you the same day in [language-list-choice]. Your question is what produced it: whether a language brings its whole dictionary with it, and whether languages should be downloaded and activated separately. For layouts the answer was no — 17 KB each is not worth a mechanism. For dictionaries it is yes.

**The mechanism, read from Android's own documentation on 2026-09-05.** Google Play Asset Delivery. An app bundle declares asset packs — containers of assets with no executable code — in one of three delivery modes: install-time, which counts toward the download size shown on the listing; fast-follow, downloaded automatically after installation; and on-demand, downloaded while the app runs. Neither of the last two counts toward the listed size.

**The property that makes it usable here at all: Google Play performs the delivery and the app makes no network request of its own.** No server, no CDN, and no internet permission on a keyboard — which is the thing this project's whole posture refuses. An app that fetched its own dictionaries would be a keyboard with network access, and no amount of explaining would make that a good look in a public repository.

**What it costs.** Publishing as an Android App Bundle through Google Play. A build distributed any other way — a sideloaded APK, another store — cannot use it, so those routes would need every dictionary shipped inside the app or none at all. Whether Hexboard ever distributes outside Play is undecided and this item does not decide it.

**Stated rather than glossed: Android's documentation frames asset packs as a feature for games**, describing them as containers of game assets replacing legacy expansion files. Nothing read says a non-game app may not use them and the mechanism is an app-bundle feature rather than a games programme, but no non-game example was read, and the size limits were noted as existing rather than read. Both want confirming before this is built.

**Why it waits.** [predictive-dictionary-bundle] ships the English word list inside the app, which is the right shape while there is one. This item only earns its complexity at the second dictionary, and there is no second dictionary until a language's word list has been found and its licence read — one hunt per language, per `workshop/resources/research/language-cost-layouts-versus-dictionaries.md`. Building the delivery mechanism before there is anything to deliver would be machinery ahead of its cargo.

**The hold was repointed on 2026-09-12.** [predictive-dictionary-bundle] shipped in the build run that ended on 2026-09-09, so that blocker is spent — but it was never the binding one. Two things gate this, and neither has moved. There is still no second dictionary: seven layouts ship and one has a word list, and SPEC already says a layout without one is fully usable with correction simply absent, so that state is deferred by design rather than broken. And Play Asset Delivery requires publishing as an App Bundle through Google Play, which is [play-store-release] — the named hold, because it is the one another queue entry can resolve. The second-dictionary trigger is written here rather than filed as work, since SPEC adds languages as they are asked for and nobody has asked. That ordering is written on both entries.

**What is not settled.** Which delivery mode fits — on-demand when a layout is first chosen is the obvious shape, but fast-follow for the phone's own system language may be better, and nobody has weighed them. What the keyboard does while a dictionary is downloading, which is a real moment: the layout works and correction is simply absent, so the honest answer may be that nothing needs to happen. And whether a failed or refused download leaves any state worth reporting to the user.

Cites research: `workshop/resources/research/language-cost-layouts-versus-dictionaries.md`.

Rests on: Play Asset Delivery's three modes, its no-network-request property and its App Bundle requirement, all read from Android's documentation on 2026-09-05 and nothing run; that a word list is a megabyte or two per language, which is an estimate rather than a measurement and is flagged as such in the research file.

[language-list-choice] carries the plan this implements. [predictive-dictionary-bundle] ships the first word list the old way. Those orderings are written on all three entries.

#### Prompt the speaker phrase by phrase while the voice model is being trained [enrolment-prompter]
Blocked by: [personal-voice-model]
The enrolment session shows what to say one phrase at a time, at the speaker's own pace, so the model adapts to how they actually talk rather than to how they read aloud.

**This is what you were actually thinking of**, said on 2026-09-05 when the idea was being filed as a testing instrument. You raised it on 2026-09-04 during a dictation test, so it was written up as a way of testing recognisers; the use you had in mind was the sitting where someone trains the model.

**Why it matters more here than in a test.** A test measured on reading-aloud speech gives a flattering number. A *model* trained on reading-aloud speech adapts to the wrong voice entirely — it fits the speech you produce when reading, and is then asked to recognise the speech you produce when talking. The mismatch is not a measurement error; it is baked into what the model learned. [personal-voice-model] already records that enrolment must be a deliberate session the user starts, and this says what happens inside it.

**And the fairness point lands harder here too.** A method that requires fluent reading aloud gives anyone with dyslexia or a reading disability a worse *personal model* — a permanently worse keyboard — rather than a worse test result. That is the same argument you made on 2026-09-04, applied to the case where the consequence sticks.

**The design carries over from [rsvp-dictation-prompter] intact**: one phrase at a time, large, nothing visible ahead of it; the speaker advancing it rather than a timer; a rotating set large enough that repeated sittings do not teach the content. All of that was settled with you on 2026-09-04 and none of it changes by moving into the app.

**What does change, and it is a genuine design question rather than a detail.** A dictation test wants speech representative of how someone talks. Enrolment may instead want **phonetic coverage** — passages chosen so the model hears every sound it needs, which is a different criterion for what fills the rotating set and can pull directly against naturalness. Nobody has looked at which wins, or whether a set can satisfy both. That question is this item's and is not answered here.

**Two more things it inherits from [personal-voice-model] rather than deciding.** How long a sitting has to be, which that item records as genuinely unknown and dependent on which adaptation technique is used. And that enrolment audio is destroyed once adaptation has run, so a sitting cannot be accumulated across days — which makes it matter more that a single sitting produces usable speech.

**Why it stays a capture.** [personal-voice-model] is itself undesignable — three unknowns recorded on it, none a desk decision — and there is no enrolment session for a prompter to live inside. What a build changes cannot be stated until that exists. The `Blocked by:` line means do not offer this again while that item is open; it returns by itself.

**And it may not survive its own trial.** [prompter-elicits-natural-speech] tests, cheaply and long before any of this, whether phrase-at-a-time speaker-paced prompting produces ordinary speech at all. If it does not, this item's premise is gone and it should be re-examined rather than built.

Rests on: [personal-voice-model]'s record of enrolment being a user-started session with audio destroyed after adaptation, and of the sitting length being unknown, read from that entry on 2026-09-05; the prompter design settled with you on 2026-09-04; that reading-aloud speech and ordinary speech differ enough to matter to an adapted model, which is your own observation from the drive of [recogniser-gap-comparison] and has not been measured.

#### Everyday words the shipped list does not carry, "ok" among them [word-list-size-level]
Blocked by: [uniform-neighbours-predictive]
The generated English word list is SCOWL size level 60 and below, and lowercase "ok" is not in it. A word the list does not carry is a word the correction engine will feel free to change, so a common one missing is a correction nobody wants.

Found on 2026-09-05 while building [predictive-dictionary-bundle], by reading the generated list rather than by anything failing. "OK" is present at level 35; the lowercase form is not present at any level at or below 60. Nobody has looked for what else is missing.

The level is one argument to `scripts/generate-word-list.py`, so regenerating at a higher level is one command — but a higher level also adds obscure words the engine could then correct *toward*, which is the trade the choice of 60 was making. The research file already names this as unsettled: whether SCOWL's size levels are a good enough proxy needs the engine to exist and be tried against real typing.

So this is not "raise the level" — it is the question of how the level gets chosen, and it wants the correction engine working first. [uniform-neighbours-predictive] is that engine.

**Split on 2026-09-12: the "ok" case left here and the general question stayed.** [word-list-ok-by-name] adds that one word by name, the way `scripts/generate-word-list.py` already adds the pronoun `I` back, and is cleared to run — it is the one case that is known rather than suspected and needs no evidence to settle. What remains here is the question the entry is actually about: how the size level gets chosen at all, and what else the list is missing. That wants someone typing on the engine, because until then there is no evidence to choose a level with and a different number is only a different guess. Held against [uniform-neighbours-predictive], which is cleared to run and not yet built. That ordering is written on both entries.

#### Hexboard cannot be used until the phone has been unlocked once after a reboot [direct-boot-unavailable]
Android says so when the keyboard is switched on: "Note: After a reboot, this app can't start until you unlock your phone". So after a restart the user types their unlock method on whatever keyboard the system falls back to, not on Hexboard.

Seen on the Pixel 6 on 2026-09-09 while driving [verify-this-runs-build-on-device]. Android shows that dialog for any input method that does not declare itself able to run before the user's storage is unlocked, and Hexboard does not declare it.

**Why it is not simply a flag to set.** Running before first unlock means running without access to the app's ordinary storage, so anything the keyboard reads at startup has to move to device-protected storage to be readable then. Today that is the chosen-layout preference; the clipboard history SPEC describes is the opposite case, being encrypted at rest and deliberately not readable early. So the work is deciding what Hexboard needs before unlock and moving only that, rather than flipping a manifest attribute.

**And it may be the right answer to do nothing.** Most third-party keyboards behave exactly this way and their users type the first unlock on the system keyboard without noticing. The cost of leaving it is one unfamiliar keyboard once per reboot; the cost of fixing it is a storage split that touches everything the keyboard reads at startup.

Filed because it is a permanent property of the app that no document mentions, not because it is known to need fixing.

**Written into SPEC on 2026-09-12, and kept as a capture.** SPEC now states the behaviour and why — the keyboard's state lives in storage readable only after the first unlock, the clipboard deliberately so — which closes the half of this entry that was about nothing documenting it.

**What stays open is the design, and it is not designable yet.** The question is what Hexboard must be able to read before unlock, and two of its three answers belong to features nobody has built: the saved-word list would have to move to device-protected storage, and the clipboard history must not, being encrypted at rest precisely so it does not survive into a locked phone's keyboard. Designing the split now means guessing about both.

**One argument for eventually supporting it, recorded because the do-nothing case looks stronger than it is.** The entry weighs one unfamiliar keyboard per reboot against a storage split, which is a fair trade for most people and not for the person this keyboard is for: someone who chose Hexboard because larger targets make them accurate is denied that accommodation at exactly the moment a mistyped unlock costs most. SPEC treats accessibility as a requirement rather than a nicety, so this is a reason to revisit rather than to close.

The trigger for taking it up again is [persistent-clipboard] and [predictive-saved-words] being built, since between them they settle what the keyboard reads at startup. Named in prose rather than as a blocker, because both are already in Processed and a `Blocked by:` line would record the ordering without holding anything back.
Filed 2026-09-09 09:43, stamped by the queue tool.

#### Speech recognition that adapts to its own user's voice, trained on the phone [personal-voice-model]
Red flag · State: cleared
Not before: 2027-03-12
Captured by you on 2026-09-01 and designed with you the same session. Your complaint is concrete: Gboard requires you to talk in an American accent to be understood.

**This is the answer to that complaint, and an accent picker is not.** An accent list can only offer the English varieties a device supports, organised by where a variety is spoken natively — Indian English, Nigerian English, Singaporean English. It reaches whoever matches an entry and misses everyone else, and your objection was that Australia is a multicultural country: a second-language English speaker matches no entry at all. Adapting to the individual voice is the only approach that does not care what the speaker's first language was. You dropped the picker on those grounds, and that decision is recorded on [in-keyboard-voice-input] as well.

**Training happens on the phone.** Your choice, from three: on the phone, on a server, or on the user's own computer. The server route was rejected because uploading recordings of someone's voice is exactly what the rest of this keyboard refuses to do, and the user's-own-computer route because it asks a phone user to run a training job on a laptop, which most of the audience will not do. The cost accepted with the on-phone choice is a job heavy enough to want charge and idle time.

**Enrolment audio is destroyed once adaptation has run.** Your choice, over keeping it on the device. The phone ends up holding an adapted model and no recordings, which is the strongest position available and the one consistent with everything else here. What it costs is stated below rather than glossed.

**The cost of destroying the audio, which defeats an earlier idea and must not be quietly restored.** Accumulating enrolment across short sittings — two or three minutes at a time, quality improving over weeks — was proposed and is not available under this choice, because accumulating requires keeping the recordings between sittings. So either one sitting must be enough on its own, or adaptation runs incrementally after each sitting on material that is then destroyed, and nobody here has checked whether incremental adaptation without the earlier audio degrades what was already learned. That is a real open question for the build, not a detail.

**How much audio this actually needs, which is where an earlier claim was wrong.** A previous turn of this session put it at about 30 minutes; you challenged that, and the challenge was right. The research is filed as `workshop/resources/research/speaker-adaptation-cost.md`. Its answer: 30 minutes describes full fine-tuning, which at low data is the *worst* technique available because it overfits a single speaker. LoRA adaptation updates roughly 3% of parameters and beats full fine-tuning in exactly the low-data regime a phone user is in; speaker-embedding conditioning needs seconds and no training job; and one 2026 method adapts while decoding with no enrolment at all. So the design should reach for the cheap rungs and treat full fine-tuning as the last resort rather than the default.

**The privacy risk, raised by Claude and settled with you across this session's exchange, which is what clears the flag above.** Enrolment means recording the user's voice, which is more sensitive than anything else this keyboard holds. Three things answer it: the audio never leaves the device, it is destroyed once adaptation has run, and enrolment is a deliberate session the user starts rather than anything that happens in the background. The residual, accepted knowingly: while an enrolment session is running the microphone is open and recordings exist on disk, so the window is real even though it is short and user-initiated.

**Not designable yet, and this is why it sits in Unprocessed rather than below the line.** Three things are unknown and none of them is a decision anyone can make at a desk. No packaged Android implementation of any rung was found, so the engineering distance between the research and a Pixel is unmeasured. Whisper does not stream, so words would arrive in a block rather than as spoken, while the models that do stream are less accurate to begin with — which is the thing adaptation is meant to fix, so the two constraints pull against each other and nothing here resolves them. And the published gains are modest and measured on elderly and pathological speech, not on the multilingual speakers this exists for, so the size of the win is genuinely unknown.

**The first of those three moved on 2026-09-12 and the item is still not designable.** The re-check split it in two: *running* an adapted model on Android has a route now — sherpa-onnx, which also softens the streaming-versus-accuracy tension — while *adapting* one on the phone has none, and that is the half this item turns on. The detail and its sources are in `workshop/resources/research/speaker-adaptation-cost.md`, amended the same day.

**Held by a date, set with you on 2026-09-12.** What it waits for is outside the project — an on-device adaptation route, which nobody here controls — so no queue entry can release it and without a date it is offered and set aside every session. Six months matches how fast the research file says this field moves. The `Blocked by: [in-keyboard-voice-input]` line came off at the same time, that item now being processed and cleared, so it held nothing back. [prompter-elicits-natural-speech] tests the premise enrolment rests on and [enrolment-prompter] is what a good result feeds.

Cites research: `workshop/resources/research/speaker-adaptation-cost.md`.

Rests on: that research file's ladder, read from published work on 2026-09-01 and nothing run; the field's own speed, which that file flags — the zero-shot work is from June 2026, so the ladder is to be re-checked before any build rather than trusted as written.

An article proposing this as a piece for the site project was sent on 2026-09-01, carrying the same caveats and an explicit warning that novelty is unverified. That send is in `INBOX/sent.md`.

#### Publish Hexboard to the Play Store, and everything that first release needs [play-store-release]
The first public release: a listing, a signed build, whatever policy and content declarations Google requires of an input method, and the decisions that come with going from a keyboard on one handset to a keyboard other people install.

Filed on 2026-09-12 at a planning session, because several entries wait on this and nothing in the queue named it. It is a capture rather than designed work: nobody has looked at what Google asks of a keyboard app, what the listing says, how the build is signed, or whether the first release is public or a closed test with a handful of people.

**Why it is worth having in the queue now even though it is months off.** Three things already lean on it, and two of them are held against it by slug — [feedback-funnel-before-store] and [dictionary-asset-packs], both repointed here on 2026-09-12. [feedback-funnel-before-store] is a complaint channel whose whole purpose is catching people before they leave a review, and there are no reviews to catch until there is a listing. [dictionary-asset-packs] delivers each language's word list through Google Play Asset Delivery, which requires publishing as an App Bundle through Play and therefore cannot be built or tested before this. And SPEC already says a language's word list arrives separately, delivered without Hexboard making any network request of its own, which is a promise that rests on the Play route existing.

**Left in Unprocessed deliberately on 2026-09-12, having been discussed the same session it was filed.** Nothing here can be designed, and the reason is not that the writing is thin: whether to release, and when, is the project owner's decision rather than work anyone can specify, and everything specifiable follows from it. Two consequences were named and accepted. This entry will surface near the top of most planning sessions, because two entries cite it and the ordering rule ranks by how much other work an item would release — it is a standing reminder of a decision rather than work waiting to be done, and being set aside each session is the correct outcome. And a `Not before:` date was considered and refused: a date is for something outside the project's control, and a date here would be a guess about the owner's own intentions.

**What is genuinely open**, and none of it is a desk decision today: whether the first release is public, closed or internal; what an input method has to declare about the data it handles, which for a keyboard is the sensitive question and touches the clipboard store and the microphone directly; how the build is signed and where that key lives; and what the listing says about a keyboard whose differentiator is a perceptual claim.

Rests on: [layout-error-report]'s address living in gitignored `local.properties`, which means a released build needs that value supplied some other way — a real consequence of a decision already taken, noticed on 2026-09-12 and not yet thought through.
Filed 2026-09-12 12:30, stamped by the queue tool.

#### Key labels announce uppercase to a screen reader while the key draws lowercase [key-label-case-vs-glyph]
Noticed while working [instrumented-tests-no-composition] on 2026-09-12. Every key publishes `key.label` as its accessibility description — `accessibilityLabel` in `KeyboardPanel.kt`. For a letter the config's label is uppercase ('Q') and its output is lowercase ('q'). SPEC says that with neither shift nor caps on, letters draw in lowercase, and `KeyCircle` honours that through `key.glyph(shiftState)`. So the key on screen reads 'q' while a screen reader announces 'Q', and the two disagree in exactly the state the board spends most of its time in.

Whether that is wrong is a product question rather than a bug: the announcement may reasonably be case-neutral, or it may reasonably track the glyph and so change with shift state. What is certain is that the description ignores `shiftState` entirely, while everything else the key draws does not.

It surfaced as a test failure rather than as a report from a person: `EmojiPanelsUiTest` asked whether the key's *output* was on screen, found nothing, and fell before its first gesture. That test now asks for the accessibility label instead, which is what the other UI tests already did — so the test is no longer wrong about the code, and this item is the remaining question about whether the code is right.

Filed 2026-09-12 21:03, stamped by the queue tool.

#### [user] Stop the Pixel 6 sleeping mid-run, which fails every instrumented test that renders [phone-sleeps-during-test-run]
A Compose UI test needs a resumed activity to have a composition to look at. When the handset's screen goes dark the activity is no longer resumed, `fetchSemanticsNodes` finds nothing, and every test that renders fails at once with `No compose hierarchies found in the app` — while every test touching no UI passes. That split is what twelve failures looked like on 2026-09-09, and eight of the twelve were nothing but this.

It is not a one-off. It recurred on 2026-09-12 during this item's own verification run: the suite takes around fifty seconds, the screen's timeout is shorter than that, and a run started on an awake phone still ends on a dark one. So the instrumented suite cannot be trusted on this handset until the screen is made to stay on, and each recurrence costs a full run to diagnose again.

The fix is a phone setting rather than anything in the repository, which is why this is user work: Android's developer options carry a switch that keeps the screen on while the device is charging, and the phone is charging over the same cable that carries the test run.

**The walkthrough.**
1. On the Pixel 6, open the Settings app and tap the search box at the top. Type `Stay awake`. Look for a result whose title is `Stay awake` — it sits under Developer options.
2. Tap that result. The phone opens Developer options scrolled to the switch, with `Stay awake` highlighted and a description below it about the screen never sleeping while charging. Turn the switch on.
3. With the phone plugged into the computer, leave it untouched for longer than its normal screen timeout and check the screen is still lit. That is the observable: a screen still on after the timeout would have darkened it.

**Settled on 2026-09-17 by a different route, and the walkthrough above was not what did it.** Searching the Settings app for `Stay awake` returned no such result on this handset, so step 1 found nothing to tap and steps 2 and 3 never ran. The screen timeout was raised to thirty minutes instead, which outlasts the suite's fifty seconds by a wide margin and so removes the failure without any developer option at all.

Two things that follow, written down because the difference matters later. The timeout is a plain display setting, so it survives nothing in particular — a factory reset, a settings restore or somebody shortening it again brings the failures straight back, where the developer-options switch would have been tied to charging and so harder to undo by accident. And a suite that grows past thirty minutes would sleep again; the margin is wide as of 2026-09-17 and is not permanent.

The original walkthrough is left above rather than rewritten, because it records what was tried and did not work, and because whoever reaches for the developer-options route on another handset should know it was not available on this one.

**Why this is not solved in the tests instead.** A test could hold a wake lock, but every test file would have to, and a file added later would silently lack it. The setting covers the whole suite once, including tests nobody has written yet.

Filed while working [instrumented-tests-no-composition].
Filed 2026-09-12 21:07, stamped by the queue tool.

#### [user] Find out whether the D-U-N-S application of 2026-05-17 produced a number [duns-outcome-unknown]
Reported by the Drive cleanup project on 2026-09-14, in a message this project's INBOX carried; it hands nothing over and asks for no reply.

A D-U-N-S number is the business identifier the app stores require before anything is published under a company name rather than under a person's. One was applied for on 2026-05-17, through CSC Australia's request form, from a separate Google account set up on 2026-05-05 as a business identity. A reply carrying a case reference arrived. Nobody has opened it, so whether a number was actually issued is unknown rather than confirmed — Drive cleanup observed only that the thread exists, and is not tracking the outcome.

It is worth an item here because [play-store-release] runs into this question sooner or later, and the two answers lead to very different work. If a number was issued, most of that job is already done. If the application died, that is better known early than discovered halfway through a store listing — and either answer costs less than starting a second application that duplicates the first.

It is user work because it needs a sign-in to an account Claude has no access to, and a judgment about what the reply actually says.

**The walkthrough.**
1. Sign in to the separate business Google account — the one set up on 2026-05-05, not the everyday personal account — and open its mail.
2. Search that mailbox for `D-U-N-S` and open the thread from CSC Australia dated 2026-05-17. Look for the reply that follows the original request.
3. Read the reply and say which of three it is: a number was issued, more information is being asked for, or the application was refused or abandoned. That answer is the observable, and it decides what [play-store-release] has to do about business identity.

Whether Hexboard publishes under a company name at all is a separate question this item does not settle; it only establishes what is available if the answer turns out to be yes.

Filed 2026-09-17 09:20, stamped by the queue tool.

#### Three cleared items still say the instrumented suite cannot be trusted, which stopped being true [stale-instrumented-caveat]
[accent-row-top-row], [panel-seam-gap] and [landscape-reveal-neighbours] each carry a sentence written while the instrumented suite was failing — that the suite is not available as a check, or that its half of an observation cannot be trusted, while [instrumented-tests-no-composition] stands. That item was closed by hand on 2026-09-17 and the suite now returns 28 of 28 passing on the Pixel 6, so the condition those sentences name has lifted and the item they name is no longer in the queue.

The reason this is worth an item rather than a tidy-up is what the sentences do to a build that reads them. Each of the three names an instrumented test as its own observation — the thing that shows the work landed. A build reading the caveat is being told not to trust the very check the same entry asks it to run, and the likely outcome is an item shipped with its observation skipped and nobody noticing, because the entry gave permission.

All three sit above the cleared-to-run line, so a build run reaches them before a planning session does. That ordering is why this was filed rather than left to be found.

What each sentence should become is a judgment per item rather than one substitution: [panel-seam-gap]'s caveat sits inside a sentence about a judgment on the phone that is genuinely still outstanding, while [accent-row-top-row]'s travels with a lift note recording why the item was released. Removing the clause wholesale would take some of that with it.

Filed at this close, which noticed it while removing [instrumented-tests-no-composition] from Processed.
Filed 2026-09-17 09:23, stamped by the queue tool.

