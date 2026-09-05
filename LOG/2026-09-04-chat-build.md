# 987cb5c — Build run of 2026-09-04: eleven items shipped uncompiled, four user items addressed, seven captures filed

Written 2026-09-04 at 16:05. This is the chat-level record for the /next run of 2026-09-04, which wrote eleven entries of its own; what belongs to no single work item is here.

The run built [build-output-off-drive], [board-clear-of-navigation-bar], [suggestion-strip], [ship-all-layout-configs], [soft-key-edge], [row-tint], [panel-key-size-consistency], [rare-row2-unindent], [declare-savedstate-viewmodel-deps], [shift-behaviour] and [emoji-panels], each with its own entry.

**The run's defining constraint, and it shapes how everything above should be read.** Gradle cannot run on this machine and there is no `adb` here, so nine of the eleven items are written and unproven. Two were genuinely confirmed: [rare-row2-unindent], whose observation is about the config and the generated manifest and could be checked here, and the parse half of [emoji-panels]. [declare-savedstate-viewmodel-deps] and [shift-behaviour] were partly confirmed by reads that did not need a compiler. Thirteen test files now wait on Android Studio, six of them new today. That verification had no home in the queue at all until this session's rescan filed [verify-this-runs-build-on-device], and three other `[user]` items were silently waiting on it.

## The `[user]` items

- **[recogniser-gap-comparison] — done.** Walked to its end this session. The Pixel 6 reports on-device recognition available, and the two sets of transcripts came back basically identical with Gboard adding punctuation the test screen did not. That is the outcome the item said would mean the same engine behind both doors, with the remaining work being punctuation rather than accuracy — so the premise [in-keyboard-voice-input] was held against is answered. Its own drive record is `2026-09-04-recogniser-gap-comparison-drive.md`.
- **[verify-a11y-ondevice] — deferred**, on the user's word, until today's build is installed: this run moved every key's accessibility bounds.
- **[verify-switch-access-ondevice] — deferred**, on the user's word, same reason plus one of its own: the scan-order question it exists to answer is a property of the geometry this run changed.
- **[physical-keyboard-handover] — deferred**, on the user's word. What it tests is the Compose input view surviving destroy-and-recreate, and this run gave that view a second pager, a row above the keys and the emoji panels.

## Also in this chat

**A limit in a test's own design, found by the user while running it.** Dictating the written passages performed far better than his ordinary experience — his account is at least three corrections per short sentence normally. He narrates fluently when reading and hesitates constantly when speaking off the cuff, and it is the hesitation the recogniser cannot follow. So handing someone a script destroys the thing the test is trying to measure. It does not spoil the comparison, both engines having been fed the same read passages, but it means neither engine's absolute standard has been tested. His proposed answer, filed as [rsvp-dictation-prompter]: flash phrases rather than single words, so the prompt carries a natural cadence; let the speaker set the pace by button press or end-of-phrase detection rather than forcing a rate as Spreeder does; and draw from a rotating set large enough that repeated runs do not teach the content. He also connected it to reading skill — a test requiring fluent reading aloud excludes people with dyslexia or a reading disability from running it at all, and one phrase at a time in large type removes the eye-scanning as well as the foreknowledge.

**A vocabulary failure in a walkthrough step, and its correction.** A step asked the user what "the availability line" said. That phrase came from the queue item and named nothing on his screen; he asked what it was. Reading `MainActivity.kt` showed the screen carries one line reading either "On-device recognition: available" or "On-device recognition: not available on this phone". Quoting those strings would have cost nothing. Filed as [walkthrough-steps-quote-screen-text], which sits beside [settings-steps-name-a-search] as the same instinct failing at a different site.

**One deviation from an item's file list, deliberate.** [panel-key-size-consistency] named `KeyGeometry.kt` read-only and put the centring in `KeyboardPanel.kt`; the offset went into `KeyGeometry.kt` because that file's standing rule is that every position in the app is computed there and nowhere else. The rule won over the file list, and the item's entry records it.

**One heading reworded after the queue lint flagged it** — a capture filed as "The five emoji panels reach..." led with an article, which the outline view truncates, and was refiled as "Emoji panels reach...".

**Two scoped-out things worth naming.** `KeyConfigUiTest.kt` was added to the run's file list mid-build: removing `visibleRadius` broke its one caller, which is the described work of [soft-key-edge] rather than growth past it. And a scripted `sed` write was correctly blocked by the safety check and redone with the editing tools.

**Routed to Captures:** [emoji-panel-reach], [rsvp-dictation-prompter], [label-size-after-soft-edge], [verify-this-runs-build-on-device], [emoji-data-refresh], [walkthrough-steps-quote-screen-text], [dead-key-border-colour].

**Research filed:** `workshop/resources/research/unicode-data-file-licence.md`, with its index line — the Unicode Terms of Use that [emoji-panels] flagged as unread.

**SPEC lags one sentence** until the next planning run: how much of Unicode's list the five panels reach. It is filed as [emoji-panel-reach] rather than written, because the session that made a choice does not certify it as product truth.

**Advisory:** filed — forward-advisory.

**Wind-down re-scan:** covered by the rescan just run.
