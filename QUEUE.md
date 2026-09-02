# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

--- Cleared to run above this line ---

#### [user] Install Hexboard on the Pixel 6 and switch it on as a keyboard [install-and-enable-on-pixel]
Blocked by: [first-installable-build]
Split out of [first-installable-build] during the /plan session of 2026-08-20. Claude writes the IME service; producing the APK and putting it on the phone is yours, and burying that in the build item's prose would have left it invisible as next-work.

Confirmed by attempt rather than assumed, at the keep-step. There is no `adb` on this machine — not on PATH, and no Android SDK platform-tools directory in either of the two places it installs to — so Claude has no route to a device even if one were connected. And Gradle cannot run here at all: it needs a loopback connection to its own daemon, which every route Claude has is blocked from making, established across four attempts on 2026-08-06 and recorded in [run-key-config-validator]. So Claude cannot build the APK either. Android Studio has neither restriction and does both in one click.

**Walkthrough rewritten on 2026-09-02 from what the first real run taught** (the drive of [compile-and-view-panel] the same day). The phone was already paired before that run started, so pairing is now the exception rather than the opening; the two things that actually stalled the drive — a greyed-out device caused by the wrong run configuration, and a Run button disabled by a pending sync hidden behind a dropdown — are steps now; and every step names something to click or a menu path, never a keyboard shortcut, since a shortcut that fails leaves nothing on screen to report.

The walkthrough:
1. In Android Studio, open the `android` folder inside the Hexboard project and wait for the sync. Look for: the progress bar along the bottom finishing, and the Sync tab there reporting **finished** with a green tick. If instead a banner across the top of the editor reads **Sync Now**, click it — or use the menu **File → Sync Project with Gradle Files** — and wait for that tick.
2. In the top toolbar, open the run-configuration dropdown (the one left of the device name) and make sure it reads **app**, not a test name. Look for: the device dropdown beside it showing **Google Pixel 6** in normal text. Greyed out means the run configuration is still a test; missing altogether means the phone is not paired — go to step 3, otherwise skip to step 4.
3. Only if the phone is missing: on the Pixel 6, open Settings → System → Developer options → **Wireless debugging**, turn it on, tap its name to open it, and tap **Pair device with QR code**. In Android Studio, open the device dropdown and choose **Pair Devices Using Wi-Fi**, then point the phone's camera at the code. Look for: the Pixel 6 appearing by name in the device dropdown.
4. With the Pixel 6 selected, click the green ▶ **Run** button. If it is greyed out, a sync is pending — **File → Sync Project with Gradle Files**, wait for the tick, then click Run. Look for: the Run panel along the bottom reading **Install successfully finished**. An error there is a build failure, not a phone problem; report the text.
5. On the phone, open Settings → System → Languages & input → On-screen keyboard → **Manage on-screen keyboards**, and switch **Hexboard** on. Look for: Android's warning that a keyboard can collect what you type — expected for any keyboard, and you have to accept it to continue.
6. Open anything with a text field and tap into it, then tap the small keyboard icon at the bottom right of the navigation bar and choose **Hexboard**. Look for: circular keys in zig-zag rows.
7. Report three things: whether it appears at all, whether keys respond to a tap, and whether the characters that arrive in the text field are the ones you aimed at.

Once this is done, [split-layout-wide-screens] returns for design by itself — it waits on seeing the keyboard in landscape on this phone. This run is also where [assets-srcdir-deprecation]'s Gradle change is first seen to sync and the app still found its config, so report the Build panel if the sync complains. Those orderings are written on both items.

If step 6 shows no keyboard icon, the service is registered but crashing on first show; the likely cause is the Compose lifecycle-owner trap named in [first-installable-build], and the Run panel's log will say so.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Blocked by: [first-installable-build], [install-and-enable-on-pixel]
Captured by you. The second blocker was added on 2026-08-20: TalkBack cannot be tested against a keyboard nobody has switched on, so this waits on the install as well as on the code that makes it installable. Once Hexboard is running on the Pixel 6, confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

#### [user] Compare dictation through Gboard and through the on-device recogniser [recogniser-gap-comparison]
Blocked by: [ondevice-recogniser-test]
Filed on 2026-09-02. The measurement half of the recogniser question, split from the build because only you have the phone and only you can judge what came out.

Why it cannot be Claude's: there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator] and [install-and-enable-on-pixel]. Beyond that, the test is someone speaking, which nothing here can do.

The walkthrough:
1. Pick three passages of about thirty seconds each and write them down: one of ordinary conversational sentences, one with names and places in it, and one you will speak at your natural speed rather than dictation speed. Look for: having the text in front of you to compare against afterwards.
2. Open any messaging app, tap the microphone on Gboard's keyboard, and dictate all three. Save what comes out. Look for: three transcripts you can read back.
3. Open Hexboard's test screen and dictate the same three passages through it. Save what comes out. Look for: whether the availability line said on-device recognition was available at all, which is the first thing to report.
4. Report three things: whether the two sets of transcripts differ noticeably, where each went wrong, and whether Gboard added punctuation and capitals that the test screen did not.

What a result means, so the report is worth making: near-identical output suggests the same engine behind both doors, and the remaining work is punctuation and capitalisation rather than accuracy. A clear Gboard advantage means the gap is inside recognition, which correcting a finished transcript cannot fix.

## Unprocessed

> Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Last session advises processing install-and-enable-on-pixel next [forward-advisory]
Filed at the close of the build run of 2026-09-02. The run built the input method service, the four gesture changes to the panel, the dictation test screen, the key audit test and the Gradle rewiring, and none of them has compiled: every one is ticked unconfirmed and waits on one Android Studio session on the Pixel 6. The install walkthrough [install-and-enable-on-pixel] is that session, and it sits just below the cleared line with its blocker shipped, so lifting it is what turns nine unconfirmed builds into seen ones. A /next run before that lift would find only the three held user steps and build nothing. The overlap scan found two captures from the same close bearing on it: [declare-savedstate-viewmodel-deps], which names the one compile risk to expect if the build fails, and [status-lines-after-install], which is held against the install and turns over the phase line and README once it succeeds. Neither blocks the lift. Six captures are waiting to be sorted in total.

#### Let a user choose which layout variant they are typing on [layout-switching]
Blocked by: [first-installable-build]
Filed by /rescan on 2026-08-20, from a requirement SPEC created the same day and nothing in the queue held: if one app ships several layouts, the user has to be able to pick theirs. It was named as an accepted cost when the platform decision was made and then not filed, which is how a feature dies in SPEC.

**Where the picker lives** was settled by you on 2026-09-01 and is in SPEC: the app's own settings, not the keyboard surface. The keyboard-surface option — a long-press or a gesture, reachable without leaving what you are typing — lost because horizontal swipe already means "change panel", so the picker would have had to find a gesture the layout has not already spent. Recorded because it is the obvious idea and will otherwise be re-proposed.

**What the picker shows**, settled with you on 2026-09-01 and narrowed on 2026-09-02: layouts grouped by language, ordered within each language by a set position rather than by anything counted. Usage telemetry was the alternative for ordering and lost outright — it is the only true measure of popularity and it would have been the first thing in Hexboard to report what a user does back to a server, against every other feature's posture. Nothing counts anything.

Two things this picker was going to show and now will not, recorded because both were designed and then removed rather than forgotten: a link to whoever made the layout, and layouts the user built on the device sitting alongside the shipped ones. Both went on 2026-09-02 with the layout editor, when you settled that Hexboard ships layouts copied from each language's own standard and nobody builds their own.

**The data it needs is [variant-language-fields]**, split out on 2026-09-01 and cleared to run, so every layout written carries the fields before there is a picker to read them. That ordering is written on both items.

What is still open, and none of it is a desk decision: how the app enumerates the available layouts at runtime, and where it remembers the choice. Both want a keyboard that runs.

Now held against [first-installable-build] rather than against [variant-schema], which shipped on 2026-09-01. The hold is the same in substance and more honest about what it waits for: there is no settings screen to add a picker to, because the app has no IME service at all today. It stays in Unprocessed rather than below the cleared-to-run line for that reason — what a build would change still cannot be stated. Nothing here is waiting on you.

Interacts with [panel-switch-gestures]: horizontal swipe already moves between the three letter panels, so whatever switches layouts must not collide with a gesture that already means something.

SPEC puts language switching out of scope for early iterations while the platform principle says the app must *eventually* let a user switch layouts, so the deferral is what SPEC asks for rather than a delay against it.

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Red flag · State: cleared
Blocked by: [install-and-enable-on-pixel]
Captured by you, and sharpened during the /plan session of 2026-08-06.

Two decisions of yours on 2026-09-01 supersede the storage half of the 2026-08-20 design. Both are now in SPEC.

**No proper nouns in the shipped dictionary.** Your reason, in your own framing: a word being corrected into a name is a particularly irritating failure. This is a property of the word list rather than of the engine, so it becomes a criterion the dictionary research has to satisfy — most frequency-ranked lists include names, so one that excludes them or can be filtered is what the search is for.

**Saved words, which reverses "stores nothing".** The 2026-08-20 decision was a fixed dictionary learning from nothing and recording nothing, chosen so that the engine never holds a record of your writing on the device — the exposure Android's own keyboard warning is about — and that choice is what cleared the flag above. You were told plainly on 2026-09-01 that saving words means the device holds a file of words you typed, that this is smaller than silent learning because each word is a deliberate act rather than a harvest, and that it stops the "stores nothing" sentence being true. You chose the saved-word store, and the reason is the obvious one: a keyboard that cannot learn a surname or a street name is worse to use.

So the flag stays **cleared**, but on a different footing, and the change of footing is the thing to notice. It was cleared by designing the risk out; it is now cleared by your informed consent to a smaller risk, recorded above. Three constraints came with that consent and bound the build: the list grows only when you deliberately save a word, it never leaves the device, and you can read and delete it. A later session must not read this flag as cleared-by-design-out and quietly drop those.

The one thing this does not settle is whether a saved-word list needs anything further — an export, a lock, a way to clear it wholesale. That is a design question for the session that builds this, not something to guess at now.

Reshaped on 2026-08-20 by your design decision, which supersedes the mechanism described further down rather than adding to it. Correction fires **on the space bar**, not per keystroke: at that moment the whole word is in hand, it is compared against the dictionary, and it is replaced by the closest match — ties broken by which word is more common.

Why this is the stronger design, recorded because the earlier one reads persuasively and would otherwise be restored. The forwards-from-seven-candidates mechanism below exists to work around reading a word before it is finished; it treats the first letter as the hard case because a prefix lookup has nothing else to go on. Correcting at the word boundary dissolves that: no letter is trusted more than any other, so the first-letter problem — your original complaint about autocomplete — stops being a special case rather than being compensated for. It is also far simpler, needing no per-keystroke candidate UI.

"Closest match" is where the geometry pays off, and it needs stating precisely: substituting a key for one of its six neighbours is a near-miss and costs little, any other substitution is a real difference and costs a lot. That is a neighbour-weighted edit distance, and it is exactly what no general-purpose library can compute, because none of them know which keys touch which. The argument for building rather than borrowing survives the change of mechanism intact.

Two guards, agreed at the same time. A word already in the dictionary exactly is never corrected, or the engine mangles deliberate spellings. And a correction must be revertable by pressing backspace immediately after it lands, because silent unrevertable autocorrect is the most resented behaviour keyboards have.

The privacy question, raised by Claude in that session and settled by you in the same exchange, which is what clears the flag above. A predictive engine improves by learning from what its user types, and that means storing a record of your writing on the device — the thing Android's own warning dialog cautions about whenever a keyboard is enabled. Your decision: a fixed dictionary only, learning from nothing and recording nothing. The risk is designed out rather than accepted, at a known cost in accuracy.

Still not designable, and left in Unprocessed for that reason alone. What settles it is a build — SPEC holds predictive text until the keyboard works, and there is nothing to correct into until there is something to type on.

The word-list lookup this item named as a later session's job was done on 2026-09-01 instead, and is filed as `workshop/resources/research/word-list-licence-and-frequency.md`. Its answer in one line: SCOWL's licence is permissive enough to ship inside this public repository under PolyForm Noncommercial, and it separates proper names into a category that can simply be left out at build time, which satisfies the no-proper-nouns rule without anyone writing a filter. What SCOWL lacks is frequency data; its size levels are a coarse commonness ranking that may serve for tie-breaking, and if a real table is wanted, Leipzig (CC BY) raises no ShareAlike question where wordfreq (CC BY-SA) does. The finding also names one trap by name, so nobody walks into it twice.

Rests on: SCOWL's licence and its proper-names category, read from its own readme on 2026-09-01; the Leipzig licence, reported second-hand in that finding rather than read off Leipzig's terms, and to be checked before anything is bundled.

In a hexagonal tessellation every key sits the same distance from each of its neighbours, and each interior key has exactly six of them. A standard rectangular keyboard doesn't have this property: horizontal neighbours are closer than diagonal ones, so the set of plausible mis-taps is uneven and direction-dependent. On Hexboard, for any key pressed there are exactly six other keys the user might have meant, equally likely by distance alone — a clean, uniform confusion set.

The user's reason for caring about this: their biggest complaint about autocomplete is that getting the first letter wrong is far worse than getting any later letter wrong, because ordinary autocomplete is a prefix lookup that only reads words forwards and so treats the first letter as certain. The neighbour set dissolves that. Rather than trusting the pressed key, the engine searches forwards from all seven candidates — the pressed key and its six neighbours — and ranks results by geometric plausibility times word frequency. Prefix-trie walks are cheap enough that seven of them cost nothing noticeable. Searching the letters backwards, the fix SPEC previously recorded, remains useful for typos later in a word, but is no longer what rescues the first letter.

This is the argument for building the engine rather than adopting a library: no general-purpose library knows the key geometry, so none can exploit any of it. Neighbour sets come from `resources/key-layout.json` rather than being hand-maintained.

Three things to check before relying on it, recorded so they aren't discovered late. Keys at a panel's edge have fewer than six neighbours, so the uniform case is the interior one. The two space bars in row 3 are not ordinary circles and won't fit the neighbour model cleanly. And the property holds within a panel, not across panels — a mis-tap can't cross a swipe boundary.

A fourth thing to know before designing this, added on 2026-08-07. The neighbour sets are derivable from `resources/key-layout.json`, but not from it alone. The config carries each key's `row` and `col` and nothing more — geometry is deliberately excluded from it, because the zag rule (odd columns sit half a key lower) is the perceptual wedge SPEC calls inviolable and so lives in Kotlin. Which six keys neighbour a given key depends on that zag parity, not just on row and column. So the neighbour table is computed by the app at runtime from the config plus the zag rule; it is not a lookup the config can hold, and nothing should be designed on the assumption that reading the config is sufficient.

Not yet designed enough to build — what exists is the insight and the approach, not a description of what any build would change. It needs a later /plan to turn into buildable work, and SPEC holds predictive text until after the first working keyboard anyway. The three caveats above are the known starting points for that design session.

Filed after `a42cd01`.

#### Clipboard history that persists, with a screen and drag-to-bin deletion [persistent-clipboard]
Red flag · State: cleared
Blocked by: [first-installable-build]
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

Held in Unprocessed against [first-installable-build] because a keyboard with no input method service cannot read the clipboard at all, and because what a build would change cannot be stated yet: there is no clipboard screen to add to, no panel structure settled to reach it from, and the screen's relationship to the panels that [panel-switch-gestures] governs is undesigned. Nothing here is waiting on you.

Rests on: `EditorInfo.inputType` exposing password variations to an input method, and `ClipDescription.EXTRA_IS_SENSITIVE` existing from API 33 with a usable string constant below it — both read from Android's documentation on 2026-09-01, neither run.

#### Voice input inside the keyboard, held open by the thumb [in-keyboard-voice-input]
Red flag · State: cleared
Blocked by: [first-installable-build]
Captured by you on 2026-09-01 and designed with you the same session. Your requirement, and the reason the feature exists at all: people should not have to switch to another keyboard to dictate, because they switch away and never switch back.

**That requirement is satisfiable, and this was checked rather than assumed.** An input method can run recognition itself — `SpeechRecognizer.isOnDeviceRecognitionAvailable()` and `createOnDeviceSpeechRecognizer()` exist from API 31, with on-device recognition forced from API 33 — and it needs the `RECORD_AUDIO` permission. Two existing keyboards, WhisperInput and Transcribro, are built this way, so this is a trodden path rather than a hopeful one. Read from Android's documentation and those projects on 2026-09-01; nothing was run.

**The privacy risk, raised by Claude and settled with you in the same exchange, which is what clears the flag above.** A keyboard requesting microphone permission is on its face indistinguishable from a keyboard that listens to you, and this repository is public and will be read by people deciding whether to trust it. `RECORD_AUDIO` on an input method is exactly what a malicious keyboard would ask for. Four things answer that, and three of them are design rather than assurance:

- **Recognition is on-device and audio never leaves the phone.**
- **Where a device cannot recognise on-device, voice input is simply unavailable.** This is the conservative arm of a real fork and it costs something: the project's minimum is API 26, and the on-device recogniser starts at 31, so devices between those levels get no voice input at all. The alternative was falling back to the network recogniser, which would send audio off the device — rejected, because a keyboard that quietly ships your voice somewhere on older hardware makes every other claim here worthless.
- **The microphone is open only while the control is held down.** Your choice, from a straight comparison with tap-to-start-tap-to-stop. The reason it won: "the microphone is open only while you are holding the button" is a claim a stranger can verify by using the keyboard, rather than one they have to take on trust from an indicator and a timeout. The cost was named and accepted — press-and-hold is tiring for anything longer than a sentence and you cannot dictate while looking away, so comfortable long-form dictation is a later question, not this item's.
- **No audio is retained once the words are transcribed**, and the mic is visibly indicated while open.

**The control grows while held and shrinks when fully released.** Yours, on 2026-09-01, and it answers the objection to press-and-hold rather than restating it: the button is hard to keep hold of while you are moving about, and a thumb drifting slightly off it loses the message halfway through. Enlarging the control the moment it is held makes the target forgiving exactly when forgiveness is needed, and shrinking it back on release keeps the resting keyboard uncluttered. It is the project's own thesis — a generous touch target — applied to the one control where losing your grip costs a whole sentence rather than one character. What still needs settling at build time is what counts as "fully released", since the point of the growth is that small movements must not end the recording.

**What is not designed, and why this sits in Unprocessed rather than below the line.** Where the mic control lives is unsettled: whether it is a key declared in the layout config like any other, or a control belonging to the board rather than the key inventory. That reaches `resources/key-layout.json` and the manifest rules if it is a key, and reaches nothing if it is not, so no honest file list can be written yet. It also touches whatever [panel-switch-gestures] settles, since a press-and-hold on the board has to be told apart from a swipe and from an ordinary tap.

**A defeated alternative, recorded so it is not revived.** An accent picker — offering the English varieties a device supports, presented as accents rather than locales — was designed and then dropped by you on 2026-09-01. It reached only speakers who happen to match one of a short list organised by where a variety of English is spoken natively, and missed the case you actually raised: a second-language English speaker in a multicultural country, who matches no entry. Your verdict was that it compares poorly with training on the user's own voice, which is [personal-voice-model]. Recognition here therefore uses the language the phone is set to, with no accent setting of its own.

Cites research: `workshop/resources/research/android-voice-input-and-accents.md`, which carries the API levels, the API 26–30 gap, the recognisable English varieties and why that list cannot answer an accent complaint.

Two things this item inherits from work filed on 2026-09-02. [ondevice-recogniser-test] puts a temporary dictation screen and the `RECORD_AUDIO` permission into `MainActivity` to measure the recogniser early; **removing both belongs here**, since this item is what introduces the microphone properly, and a test screen carrying that permission must not survive into anything published. And [recogniser-gap-comparison] measures how the platform recogniser compares with Gboard's, which tells this item whether the recognition it gets is good enough to build the press-and-hold control around at all. That ordering is written on all three items.

Rests on: the on-device recogniser's API levels and the `RECORD_AUDIO` requirement, read from Android's documentation on 2026-09-01; that on-device recognition genuinely keeps audio on the device, which is what the API claims and what a build should confirm before the README repeats it.

#### Speech recognition that adapts to its own user's voice, trained on the phone [personal-voice-model]
Red flag · State: cleared
Blocked by: [in-keyboard-voice-input]
Captured by you on 2026-09-01 and designed with you the same session. Your complaint is concrete: Gboard requires you to talk in an American accent to be understood.

**This is the answer to that complaint, and an accent picker is not.** An accent list can only offer the English varieties a device supports, organised by where a variety is spoken natively — Indian English, Nigerian English, Singaporean English. It reaches whoever matches an entry and misses everyone else, and your objection was that Australia is a multicultural country: a second-language English speaker matches no entry at all. Adapting to the individual voice is the only approach that does not care what the speaker's first language was. You dropped the picker on those grounds, and that decision is recorded on [in-keyboard-voice-input] as well.

**Training happens on the phone.** Your choice, from three: on the phone, on a server, or on the user's own computer. The server route was rejected because uploading recordings of someone's voice is exactly what the rest of this keyboard refuses to do, and the user's-own-computer route because it asks a phone user to run a training job on a laptop, which most of the audience will not do. The cost accepted with the on-phone choice is a job heavy enough to want charge and idle time.

**Enrolment audio is destroyed once adaptation has run.** Your choice, over keeping it on the device. The phone ends up holding an adapted model and no recordings, which is the strongest position available and the one consistent with everything else here. What it costs is stated below rather than glossed.

**The cost of destroying the audio, which defeats an earlier idea and must not be quietly restored.** Accumulating enrolment across short sittings — two or three minutes at a time, quality improving over weeks — was proposed and is not available under this choice, because accumulating requires keeping the recordings between sittings. So either one sitting must be enough on its own, or adaptation runs incrementally after each sitting on material that is then destroyed, and nobody here has checked whether incremental adaptation without the earlier audio degrades what was already learned. That is a real open question for the build, not a detail.

**How much audio this actually needs, which is where an earlier claim was wrong.** A previous turn of this session put it at about 30 minutes; you challenged that, and the challenge was right. The research is filed as `workshop/resources/research/speaker-adaptation-cost.md`. Its answer: 30 minutes describes full fine-tuning, which at low data is the *worst* technique available because it overfits a single speaker. LoRA adaptation updates roughly 3% of parameters and beats full fine-tuning in exactly the low-data regime a phone user is in; speaker-embedding conditioning needs seconds and no training job; and one 2026 method adapts while decoding with no enrolment at all. So the design should reach for the cheap rungs and treat full fine-tuning as the last resort rather than the default.

**The privacy risk, raised by Claude and settled with you across this session's exchange, which is what clears the flag above.** Enrolment means recording the user's voice, which is more sensitive than anything else this keyboard holds. Three things answer it: the audio never leaves the device, it is destroyed once adaptation has run, and enrolment is a deliberate session the user starts rather than anything that happens in the background. The residual, accepted knowingly: while an enrolment session is running the microphone is open and recordings exist on disk, so the window is real even though it is short and user-initiated.

**Not designable yet, and this is why it sits in Unprocessed rather than below the line.** Three things are unknown and none of them is a decision anyone can make at a desk. No packaged Android implementation of any rung was found, so the engineering distance between the research and a Pixel is unmeasured. Whisper does not stream, so words would arrive in a block rather than as spoken, while the models that do stream are less accurate to begin with — which is the thing adaptation is meant to fix, so the two constraints pull against each other and nothing here resolves them. And the published gains are modest and measured on elderly and pathological speech, not on the multilingual speakers this exists for, so the size of the win is genuinely unknown.

Cites research: `workshop/resources/research/speaker-adaptation-cost.md`.

Rests on: that research file's ladder, read from published work on 2026-09-01 and nothing run; the field's own speed, which that file flags — the zero-shot work is from June 2026, so the ladder is to be re-checked before any build rather than trusted as written.

An article proposing this as a piece for the site project was sent on 2026-09-01, carrying the same caveats and an explicit warning that novelty is unverified. That send is in `INBOX/sent.md`.

#### [user] Have a Russian reader check the ЙЦУКЕН layout before it ships [russian-layout-check]
Blocked by: [language-starter-layouts]
A check of the Russian layout by someone who types Russian, before it ships.

Filed on 2026-09-02, at the moment the Russian layout was designed. **Narrowed the same evening:** the layout is now transcribed from FlorisBoard's Russian layout file (see `workshop/resources/research/open-source-layout-sources.md`), with only Ъ and Ё hidden (under Ь and Е, read from that project's popup file), so there are no pairings of Claude's to judge — Щ has its own key. What the reader confirms is that the copy matches the keyboard they already use, and step 3 below is what stays of the original question.

Why this cannot be Claude's. Whether a pairing is guessable rather than merely defensible is a judgment only a reader of the language can make, and the research this rests on came from English-language explainers of the Russian layout rather than from Russian typists — a limit recorded in `workshop/resources/research/cyrillic-overflow-and-slot-budget.md` rather than glossed over.

Why it matters more than it looks. A shipped layout is copied rather than read: it becomes the thing later layouts and later contributors imitate, so an error in it propagates rather than sitting still.

**Narrowed on 2026-09-02.** This walkthrough opened by having you repoint the preview page's `LAYOUTS` block at the Russian config — an edit to JavaScript, which Claude can make and which therefore should never have been yours. It moved into [language-starter-layouts], so by the time you reach this the board is already there to open.

The walkthrough:
1. Open `planning/layout-preview.html` by double-clicking it. Look for: a board of Cyrillic letters in the usual zag rows, with the Russian layout among those the page offers.
2. Show it to someone who types Russian. Ask them one question — is every letter where they expect it? Look for: hesitation over any particular key, which tells you more than a yes does.
3. Ask them about the two hidden letters: would they think to hold Ь to get Ъ, and Е to get Ё? Look for: whether they guessed before you explained.
4. Report what they said. A rejected pairing is ordinary planning work filed from your report, not something to fix while you are with them.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

[language-list-choice] is held against this item, and the reason is worth knowing while you run it: how much trouble it is to find a reader and get an answer is the fact that decides how many further languages are worth committing to. So note what the arranging cost, not only what they said. That ordering is written on both items.

#### Which languages get a layout, and in what order [language-list-choice]
Blocked by: [russian-layout-check]
Filed on 2026-09-02, when you replaced the contributor-facing editor with layouts copied from each language's own standard. Russian is being done first as the hard case, in [language-starter-layouts]; this is the decision about what follows it.

**Half of it is already answered and is in SPEC:** layouts are added as they are asked for. You settled that on 2026-09-02, and the standing policy is demand-driven rather than a planned rollout. What stays open is only whether there is a first batch beyond Russian.

**Held against [russian-layout-check] on 2026-09-02, and the reason is the thing this decision is missing.** Each language costs three things: Claude reading FlorisBoard's layout and popup files for it (the source settled on 2026-09-02 for Russian, recorded in `workshop/resources/research/open-source-layout-sources.md`), Claude transcribing them into a config with rows as wide as the source's, and a check by someone who reads it. The first two are cheap and yours to trigger rather than to do. Where FlorisBoard offers more than one layout for a language, which to copy is a question for this item — it was not researched. The third is the bottleneck, and it does not scale with how many people speak a language — it scales with whether a reader of it can be found to look. A language with a hundred million speakers and nobody to check it costs more than one with five million and someone willing. Nothing has tested that yet: [russian-layout-check] is the first attempt this project makes at getting a native reader to look at a layout, and how much trouble it is to arrange is what should set the size of any list. So the item returns by itself carrying the one fact it lacks.

An alternative was offered and not taken up now: researching which alphabets fit 30 slots and which overflow — Greek at 24 letters and Hebrew at 22 being transcription jobs, while Arabic, Vietnamese and the Indic scripts each raise their own version of the Cyrillic problem. It is useful whenever this is taken up, and it answers what is cheap rather than what is wanted, so it was not a reason to decide now.

Worth settling in the same conversation: what happens where a language has two competing standards. Russian already raises it — the phonetic ЯВЕРТЫ layout sits alongside ЙЦУКЕН and was never investigated — and Hexboard would have to either pick one or ship both as separate layouts, which the picker's ordering within a language is capable of holding.

#### Correcting what the speech recogniser returns [speech-output-correction]
Blocked by: [recogniser-gap-comparison]
Captured by you on 2026-09-02, and separate from the keyboard's own autocorrect by your instruction. Your target: dictation as good as Gboard's. Your reason for raising it: the correction Gboard does looks like AI to you, and you wanted that checked rather than assumed.

It is AI, and the check changed the shape of the feature. Gboard's on-device recogniser has been an all-neural end-to-end model since 2019, and there is no separate autocorrect stage at all: correction happens inside recognition, with an optional second pass where a language model re-ranks the recogniser's own N best guesses at the whole utterance. So a corrector applied to a finished transcript has strictly less to work with than the recogniser had — the audio is gone, and with it every alternative that was considered and rejected. It can tidy output; it cannot close a gap in recognition quality. That is why [recogniser-gap-comparison] comes first: it establishes whether there is a gap to close.

**The tension with the rest of this project, which is a decision for you and not an implementation detail.** Google's own documentation says transcripts of what the user says and types are saved on the device, and that corrections are used to improve dictation for that user. Part of Gboard's advantage is a per-user record of how someone speaks and how they fix it. Hexboard refuses that class of storage everywhere else: predictive text keeps only words deliberately saved, and enrolment audio for [personal-voice-model] is destroyed after adaptation. Matching Gboard by the same means would reverse that. Nothing is decided here, and no design should assume either answer.

Three routes are visible and none is chosen: rely on the platform recogniser and add only punctuation and capitalisation; bias recognition toward the user's saved words and contacts, if the on-device API permits biasing at all, which was not researched; or correct the transcript against the saved-word list the predictive engine already holds. Which of them are open depends on what the comparison finds.

Cites research: `workshop/resources/research/gboard-speech-correction.md`, which carries the sources and states plainly that the architecture papers are from 2019 and 2020 — sound on where correction happens, and not a description of what Gboard ships today.

#### Split the board into two halves on wider screens [split-layout-wide-screens]
Blocked by: [install-and-enable-on-pixel]
The board splits into two halves, one under each thumb, whenever the screen is wider than it is tall.

**Settled with you on 2026-09-02: the split is automatic, and the trigger is width exceeding height.** Not a setting, and not a device class — a phone turned to landscape gets it, a tablet in portrait does not. Gboard's split-as-a-toggle was named as the comparison and not chosen; your rule needs no menu and follows directly from the reason the feature exists, which is where the thumbs are.

**Held against [install-and-enable-on-pixel] on 2026-09-02**, as a capture rather than below the line, because what remains open is seen rather than reasoned: the gap width and what each half does with the space bars want a real keyboard in landscape on your phone, and only the installed input method gives that — the test screen in `MainActivity` is portrait-only in practice. The likely answer for the space bars, recorded so it is the starting point and not a discovery: each half keeps the one nearer its thumb. That ordering is written on both items.

Captured by you on 2026-09-02, immediately after choosing to keep the space bars at columns 4 and 6 in [row3-space-choice]. The two decisions belong together and the connection is the point: your reason for keeping today's arrangement is that in normal portrait handling the thumbs sit naturally near the middle of the screen, so the space keys do not need moving outward. On a wider screen that stops being true — the thumbs move to the edges and the middle becomes the part neither can reach.

**Your proposal: split the board into two halves on wide screens.** Your observation is that the layout has a clean enough middle to split at, and that splitting naturally brings the space keys back under the thumbs in that case, which is the same problem [row3-space-choice] settled for portrait, answered for the case where it actually bites.

What this does not do, and it matters for SPEC's inviolable geometry: a split separates the halves, it does not rearrange keys within them or change the zag rule, the circle size, or nearest-centre routing. Each half keeps its own columns. So this is a question about where the board is drawn rather than about the key inventory, which puts it in the code that lays the panel out rather than in a layout config.

What is not settled, and none of it is a desk decision: at what width the split appears, whether it is automatic or a setting, how wide the gap is, and what happens to the two space bars — whether each half keeps one, which is the arrangement that most obviously puts one under each thumb.

Interacts with [key-press-feedback] and [panel-switch-gestures], both of which edit the same panel code and are held against [compile-and-view-panel], now done. A horizontal swipe crossing the gap between two halves is a case that gesture work will have to answer.

#### Shift behaviour: one-shot today, caps lock and double-tap undesigned [shift-behaviour]
Filed by /rescan on 2026-09-02 from the run that built [first-installable-build]. That item left shift to the service, and the build chose the simplest thing that gives capitals at all: tapping shift uppercases the next inserted character, then clears. Nothing in SPEC or the queue says what shift does. The prototype `hexboard17.html` has a double-tap-for-caps-lock (its `caps` state with a 320 ms double-tap window), and whether Hexboard keeps that, and whether a shifted state should be visible on the key, are the questions. The timing rule in SPEC — every hold and repeat follows the phone's settings — may reach a double-tap window too. Ordinary planning work; nothing here is waiting on you.

#### Every layout config must reach the app's assets, not only the English one [ship-all-layout-configs]
Filed by /rescan on 2026-09-02 from the run that built [language-starter-layouts]. The Gradle copy task ships `resources/key-layout.json` alone, so `key-layout-ru.json` exists in the repository and not in the app. A layout picker has nothing to list until every config is copied and the app can enumerate them at runtime, which is exactly the open question [layout-switching] records. This belongs with that item and should be built with or before it; that ordering is written here and should be written there when it is processed. The copy task now takes a `DirectoryProperty` output, so copying a folder of configs rather than one file is a small change to `android/app/build.gradle.kts`.

#### On an eleven-wide layout, the other two panels draw bigger keys than QWERTY [panel-key-size-consistency]
Filed by /rescan on 2026-09-02 from the run that built [language-starter-layouts]. `KeyboardPanel` sizes its keys from its own panel's widest row, so on the Russian layout QWERTY is eleven wide and RARE and SYMBOLS stay ten wide with correspondingly larger circles. Swiping between panels would change key size. Whether all three panels should share the radius of the widest (so the narrower panels sit centred with margins), or the difference is acceptable, is a design question for the session that first sees it on the phone. Geometry lives in Kotlin, so the fix is in `KeyboardPanel.kt` or `HexboardBoard`, not in any config.

#### Two support libraries the service uses are not declared directly [declare-savedstate-viewmodel-deps]
Filed by /rescan on 2026-09-02 from the run that built [first-installable-build]. `HexboardImeService.kt` imports `androidx.savedstate` and `androidx.lifecycle.ViewModelStore` classes, which are not in `android/gradle/libs.versions.toml`; the build relies on them arriving through Compose UI and Activity, which declare them as API dependencies. If the next Android Studio build fails on unresolved references in the service, the fix is to add both to the version catalogue and the app's dependencies. If it builds, this is nothing and can be deleted at the next planning session. Nothing to do until that build runs.

#### Accent row on the top row overlaps the neighbouring keys [accent-row-top-row]
Filed by /rescan on 2026-09-02 from the run that built [long-press-accent-popup]. The row is drawn by the board, inside the board's bounds, clamped to the top edge as the prototype clamps it — but the prototype had a bar above its keys to clamp into, and the board has nothing above row 0. So holding Е or E on the top row draws the alternatives over the neighbouring keys of that same row. Options not yet weighed: draw the row below the key on the top row, draw it in a window that may extend above the input view, or shrink the row. Seen only in reasoning; the first run on the Pixel 6 will show how bad it looks.

#### Phase line and README status turn over once the keyboard switches on [status-lines-after-install]
Blocked by: [install-and-enable-on-pixel]
Filed by /rescan on 2026-09-02. Two sentences will be false the moment the install walkthrough shows Hexboard switched on as a keyboard: the phase paragraph in `CLAUDE.md`, written this same day by [claude-md-phase-ran], says there is no input method service yet; and `README.md`'s Status section says there is no working keyboard and nothing to install. Both were true when written and one build behind by the end of the run that wrote them. The phase line states the phase, and the phase changes at exactly this point, so it turns over once rather than at every run. Held against the install because the sentences stay true until it succeeds.

