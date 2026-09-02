# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Register Hexboard as an Android input method [first-installable-build]
Lifted above the line on 2026-09-02: its blocker [compose-keyboard-renders-config] was built on 2026-08-21 and verified on the Pixel 6 by [compile-and-view-panel] on 2026-09-02 — the panel compiled, drew and typed. The premise below (no service in the manifest, a panel to host) was last checked against the files on 2026-08-20 and nothing since has touched the manifest.

Filed on 2026-08-14 during planning, alongside [compose-keyboard-renders-config], for the same reason: [verify-a11y-ondevice] waited on an installable build with no queue item to name as its blocker.

Designed out on 2026-08-20, and narrowed by a split. The threshold is not keys on screen — that is the rendering item — but Android accepting Hexboard as an input method the user can pick. Confirmed by reading `android/app/src/main/AndroidManifest.xml` during that session: there is no service of any kind declared today, only the launcher activity. SPEC puts IME polish (settings screen, language switching) out of scope for early iterations, so the target is the minimum that lets you type on the thing.

The build:
- `HexboardImeService.kt` (new) — an `InputMethodService` whose input view is a `ComposeView` hosting the panel from [compose-keyboard-renders-config], committing each key's output character through the current input connection. **Set the lifecycle, saved-state and view-model owners on that view explicitly**: Compose inside an `InputMethodService` has no owners by default and crashes the first time the keyboard is shown. This is the known trap and it is written here so the build does not rediscover it.
- `AndroidManifest.xml` — a `<service>` declaration with `BIND_INPUT_METHOD`, an intent filter for `android.view.InputMethod`, and meta-data pointing at the descriptor below.
- `res/xml/method.xml` (new) — the input-method descriptor and its subtype.
- `res/values/strings.xml` — the label Android shows in the keyboard picker.
- `MainActivity.kt` — a button opening the system's input-method settings, so switching Hexboard on is findable rather than hunted for.

Held below the line because there is nothing to host before keys render. Building and installing the APK is not Claude's — no `adb` on this machine and Gradle cannot run here — so that half is split out as [install-and-enable-on-pixel], which this item releases.

Files: `android/app/src/main/AndroidManifest.xml`, `android/app/src/main/res/values/strings.xml`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, and two new files (`HexboardImeService.kt`, `res/xml/method.xml`).

#### Key press feedback — the pressed key lightens and fades back [key-press-feedback]
Lifted above the line on 2026-09-02: [compile-and-view-panel] ran the app on the Pixel 6 that day and the panel compiled, drew and typed, so the code this edits is no longer unverified. Its premise — the board-level nearest-centre tap handler in `KeyboardPanel.kt` — was read on 2026-09-01 and that file has not changed since.

Captured by you on 2026-09-01 and designed with you in the same session. Your words for the problem: the keyboard as it stands is a bit inert.

**What was decided, and it is narrower than the original ask.** You first described touches showing and fading on each press alongside the key highlight. Presented with the choice, you chose the key highlight alone: the pressed key lightens instantly, holds, then fades back, and nothing marks where the finger actually landed.

**Why the touch-point marker lost, recorded because it is the more obvious of the two and will look like an oversight.** The board's tap handler resolves a tap by nearest centre, so the touch target is larger than the drawn circle and a tap landing between two circles still goes somewhere definite. A marker at the real touch point would therefore show the gap between where you aimed and which key won. That is honest feedback, and it is exactly the feedback SPEC's perceptual wedge does not want: the wedge is about aiming *confidently* at circles, and a display that draws the eye to near-misses the routing already absorbed works against it. The highlight alone still answers "did that register, and on which key".

**The build:** in `KeyboardPanel.kt`, hold per-key press state at the board level — which is where the tap is already resolved — and render the resolved key with a lighter fill that animates back to its normal colour. Concurrent presses each animate independently, because a fast typist starts the next press before the last has finished fading. A gesture that turns into a panel swipe rather than a tap must not leave a key stuck lit.

**Timing, to be set by eye rather than by argument.** Start with the highlight instant, a brief hold, and a fade of about 150ms. These are opening values; you will have the keyboard in front of you when you run [compile-and-view-panel], and adjusting them on sight is the point rather than a fallback.

The observation that shows it landed: on the Pixel 6, tapping a key visibly lightens that key and it fades back, and typing quickly shows several keys fading at once rather than one cancelling another.

Held below the line against [compile-and-view-panel] rather than cleared. This changes code that has never been compiled, and stacking a second unverified change on the first means debugging both together if the compile fails. The cost was named to you and accepted: the effect appears on the phone a sitting later than it otherwise would. Placement interacts with [panel-switch-gestures], which is held against the same item and touches the same file — whichever is built second should expect the other's changes to `KeyboardPanel.kt` to be there already. Settled on 2026-09-02: this is built first of the three items editing that file, then [backspace-key-repeat], which hangs a hold timer on the per-key press state this item introduces, then the swipe. That ordering is written on all three.

Rests on: `KeyboardPanel.kt` resolving taps at board level by nearest centre, read 2026-09-01; SPEC's perceptual wedge, which is what rules out the touch-point marker.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

#### Backspace and the cursor keys repeat while held, at the phone's own timing [backspace-key-repeat]
Held backspace and cursor keys repeat, the way every keyboard's do; today holding delete removes one character and stops.

Captured by you on 2026-09-02, from the first time the app was run on the Pixel 6 during [compile-and-view-panel], and designed with you the same day. The absence reads as the keyboard being broken rather than as a missing feature. Everything else in that first run was right.

**Which keys repeat: backspace, cursor-left and cursor-right, and nothing else.** A held letter is the accent menu the config already declares, so letters cannot repeat. Enter, shift and space do not repeat either; on every mainstream keyboard a held space does something else or nothing.

**Keyed off the key's `action` in code, not a new config field.** Repeat is what an action does, not which keys exist, and SPEC gives behaviour to code and inventory to config. The Russian layout and every later one get it with no schema change. A `repeat` field in the config was the alternative and lost on that ground.

**Timing follows the phone's settings, never a number of ours.** Your requirement, from setting up phones for people slower than you: the hold delay must scale with Android's accessibility "Touch & hold delay" setting. Android's `ViewConfiguration` reports exactly that — `getLongPressTimeout()` is the value that setting changes, and `getKeyRepeatDelay()` is the system's own repeat interval — so the build reads both at runtime and hard-codes neither. AOSP's keyboard used fixed 400 ms / 50 ms; that was the first proposal here and it lost to yours. The same rule binds every hold on this keyboard, the accent menus included, and SPEC carries it.

**What the board lacks today is any notion of a key being held**, read from `KeyboardPanel.kt` on 2026-09-02: it runs `detectTapGestures` and nothing else, so neither repeat nor the long-press accents can work yet. The press-and-release tracking this needs is the same per-key press state [key-press-feedback] introduces, so this is built second of the four items editing that file: feedback first, then this, then [long-press-accent-popup] (which reuses this item's hold detection for keys that have alternatives — a key never both repeats and pops up), then [panel-switch-gestures]. A repeating key stays lit for as long as it repeats; a press that becomes a panel swipe stops the repeat. That ordering is written on all four.

The build: in `KeyboardPanel.kt`, on a press resolved to a key whose action is backspace, cursor-left or cursor-right, start a timer at the system long-press timeout that re-fires the action every system key-repeat interval until release or until the gesture becomes a swipe.

The observation that shows it landed: on the Pixel 6, holding delete removes characters continuously after a short pause and stops on release; holding a letter does not repeat it; and changing Settings → Accessibility → Timing controls → Touch & hold delay from Short to Long visibly lengthens the pause before repeating starts.

Rests on: `KeyboardPanel.kt` using `detectTapGestures` only, read 2026-09-02; `ViewConfiguration.getLongPressTimeout()` tracking the accessibility touch-and-hold setting and `getKeyRepeatDelay()` existing, from memory of Android's API on 2026-09-02 and not looked up — the build should confirm both against the platform documentation before relying on them.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

#### Long-press accent popup, as the prototype draws it [long-press-accent-popup]
Holding a key that has alternatives shows them in a row above it; slide to choose, release to type. The config already declares every list and SPEC already promises the feature; nothing in the Kotlin shows one.

Found on 2026-09-02 while designing [backspace-key-repeat] and processed the same day: `resources/key-layout.json` carries eighteen `longPress` lists (and the Russian layout adds Ё under Е and Ъ under Ь), SPEC lists long-press accents among the layout details, and `KeyboardPanel.kt` runs a tap detector only — read 2026-09-02 — so no hold reaches the config's lists. Nobody's work until now.

**The gesture is the prototype's, which SPEC names canonical for gestures**, read from `hexboard17.html` on 2026-09-02: on a hold, a row of the key's alternatives appears above it with the first highlighted; sliding sideways moves the highlight along the row; releasing types the highlighted one. The base character is not in the row — a tap gives that. **On release without sliding, the first alternative is typed**, which is the prototype's behaviour and was kept over Gboard's (where release gives the base letter) because it saves a slide for the commonest case; settled with you on 2026-09-02.

**Timing is the phone's own touch-and-hold delay**, per the SPEC sentence added the same day, replacing the prototype's fixed 320 ms. Keys with no alternatives do nothing on a hold unless they repeat ([backspace-key-repeat]); a key never both repeats and pops up.

The build: in `KeyboardPanel.kt`, on a hold resolved to a key whose `accents` list is non-empty, draw the row above that key (clamped inside the board edges as the prototype does), track the pointer's horizontal position against the row to move the highlight, and commit the highlighted alternative on release. A press that becomes a panel swipe closes the row and types nothing. The row is drawn by the board, not by the key, since the board already owns press state.

Built after [backspace-key-repeat] and before [panel-switch-gestures]: it uses the hold detection that item introduces on the press state [key-press-feedback] introduces, and the swipe item is the one that resolves all contention last. That ordering is written on those items too.

The observation that shows it landed: on the Pixel 6, holding **e** shows è é ê ë ē above it, sliding highlights each in turn, releasing types the highlighted one, and releasing without sliding types è; holding a key with no list shows nothing.

Rests on: the `accents` accessor in `KeyLayout.kt` and the tap-only detector in `KeyboardPanel.kt`, both read 2026-09-02; the prototype's popup logic (`openLP`, `updateLPSel`, `commitLP`), read 2026-09-02.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

#### Horizontal swipe between the three letter panels [panel-switch-gestures]
Lifted above the line on 2026-09-02: [compile-and-view-panel] ran the app on the Pixel 6 that day and the panel compiled, drew and typed. The premise — the board-level tap detector and the consumed-down behaviour of `detectTapGestures` — was read on 2026-09-01 and nothing has executed the gesture code since; the rests-on line below still stands as written.

Split out of [compose-keyboard-renders-config] during the /plan session of 2026-08-20, which kept the rendering half and returned this half here. The rendering item draws one panel; this is what makes RARE, QWERTY and SYMBOLS reachable from each other, with QWERTY as home per SPEC.

Design progress made before the split, so the next session starts further along. Two of the three questions the original item raised are settled and belong to the rendering half: the zag rule gets one Kotlin home ported from `planning/layout-preview.html`, and `hexboard17.html` is treated as specification for layout, gestures and key inventory exactly as SPEC already says. What was not answerable in that session is this one — where panel switching lives.

That question is genuinely architectural rather than a detail. It decides whether a panel is a screen the keyboard navigates between or a slice of one continuously drawn surface, and that choice reaches how state is held, how a swipe is disambiguated from a key press near a panel edge, and whether an in-flight drag can cross a boundary. SPEC already rules out one thing: a mis-tap cannot cross a swipe boundary, recorded in [uniform-neighbours-predictive].

What would settle it: a rendering surface that exists, so the gesture can be tried against real keys rather than reasoned about. So this waits on the rendering half rather than on a decision anyone can make now.

Designed out on 2026-09-01, and the architectural question is answered by reading the code rather than by preference. `KeyboardPanel.kt` puts its tap detector on the board, not on the individual keys, and it has to: SPEC requires nearest-centre routing, so a tap goes to the closest key centre rather than to whichever circle contains it. Panel switching therefore cannot be a per-key concern and must wrap the whole board. That settles the screens-versus-one-surface question in favour of a horizontal pager holding three pages, one `KeyboardPanel` each, opening on QWERTY — which also keeps the drag-follows-finger feel `hexboard17.html` has, rather than a hard cut between panels.

The known trap, which is why this item exists in this shape. `detectTapGestures`, which the board already runs, consumes the pointer-down event, and a consumed down starves whatever else is contending for the gesture — so the obvious construction is exactly the one reported to leave the pager unable to swipe. Two established remedies: a hand-written tap detector that detects without consuming, or having the parent detect its drag in Compose's Initial pass, which runs ahead of the child's Main-pass detector. Both live in `KeyboardPanel.kt`, so which one is needed does not change the file list — it is discovered by running the thing, not decided at a desk.

Cites research: `workshop/resources/research/compose-pager-vs-board-tap-gesture.md`, which carries the sources and states plainly that none of it was executed.

Two alternatives were never investigated rather than ruled out, and are recorded so nobody assumes they were weighed: drawing all three panels on one continuously offset surface with a single detector handling both tap and drag, and switching on a discrete fling with no drag-follows-finger, which would sidestep the gesture contention entirely.

The build: wrap the three panels in a horizontal pager opening on QWERTY, with panel state held at the surface rather than inside a panel, and apply whichever of the two remedies the run shows is needed.

The observation that shows it landed: on the Pixel 6, a horizontal swipe moves between RARE, QWERTY and SYMBOLS, and a tap that does not travel still types its key — including near a panel edge, which is where the two behaviours compete.

Rests on: `KeyboardPanel.kt`'s board-level tap detector, read 2026-09-01; the consumed-down behaviour of `detectTapGestures`, read from documentation on 2026-09-01 and not executed; the project's Compose version, which the research does not pin down and which the build should read off `android/app/build.gradle.kts` before trusting any of it.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`.

Shares `KeyboardPanel.kt` with [key-press-feedback], [backspace-key-repeat] and [long-press-accent-popup], and is built last of the four (settled 2026-09-02): feedback introduces per-key press state, repeat hangs a hold timer on it, the accent popup reuses that hold, and this item's swipe must cancel all three — a gesture that turns into a panel swipe leaves no key lit, no key repeating and no popup open. That ordering is written on all four.

Held below the line against [compile-and-view-panel] rather than against the rendering item, which shipped. The old wording said this waits on "a rendering surface that exists" — the surface exists as code, but nothing has compiled or run it, and the observation above is a thing seen on a phone. The compile check is that release condition made into an item that can actually resolve.

#### Dictation test screen calling Android's on-device recogniser [ondevice-recogniser-test]
Lifted above the line on 2026-09-02: [compile-and-view-panel] ran the app on the Pixel 6 that day, so the app this edits has compiled and the phone is paired. Nothing has verified the recogniser API premise since it was read on 2026-09-01; the rests-on line below still stands as written.

Filed on 2026-09-02, out of the question of whether Hexboard's dictation can match Gboard's. The research is `workshop/resources/research/gboard-speech-correction.md`, and its central unknown is this: Gboard uses Google's own recogniser, Hexboard would call Android's public on-device one, and nothing found establishes whether those are the same engine on a Pixel. If they are, Hexboard starts at Gboard's recognition quality for nothing; if they are not, the gap is inside recognition where no transcript-level correction can reach it. That difference decides whether a correction feature is worth designing at all, so it is settled by measurement before any design.

It rides the existing app rather than waiting for the keyboard. This needs no input method service and no keyboard: `MainActivity` is an ordinary app screen that already exists, so a button and a text area are enough to exercise the recogniser. That is what makes the answer available now rather than several items downstream.

The build:
- `MainActivity.kt` — a test screen: a button that starts on-device recognition and shows the returned text, and a line reporting what `SpeechRecognizer.isOnDeviceRecognitionAvailable()` returned, which also answers whether this handset has on-device recognition at all.
- `AndroidManifest.xml` — the `RECORD_AUDIO` permission and the runtime request for it.

**This scaffolding must not reach a shipped build.** The microphone permission belongs to [in-keyboard-voice-input], which introduces it properly with the press-and-hold control and the guarantees around it; a test screen carrying it is fine on your own handset and is not something to publish. Removing both is part of that item's work rather than a trailing step here.

The observation that shows it landed: on the Pixel 6, the screen reports whether on-device recognition is available, and pressing the button and speaking a sentence puts recognised text on screen.

Cites research: `workshop/resources/research/gboard-speech-correction.md`.

Rests on: `SpeechRecognizer.isOnDeviceRecognitionAvailable()` and `createOnDeviceSpeechRecognizer()` existing from API 31 and requiring `RECORD_AUDIO`, read from Android's documentation on 2026-09-01 and recorded in `workshop/resources/research/android-voice-input-and-accents.md`; that `MainActivity` is a plain Compose activity, read on 2026-09-01.

Held against [compile-and-view-panel] because it changes the same untried app and wants the Pixel already paired, which that item does.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, `android/app/src/main/AndroidManifest.xml`.

#### Verify every key in the config actually renders and emits its character [android-key-audit]
Lifted above the line on 2026-09-02: its blocker [compose-keyboard-renders-config] was built on 2026-08-21 and verified on the Pixel 6 by [compile-and-view-panel] on 2026-09-02, so keys now exist on screen to drive. Running the test it writes is not Claude's — Gradle cannot run here, recorded on [install-and-enable-on-pixel] — so the build writes the test and the run of it is yours, in Android Studio.

Captured by you. Rewritten during planning as the second half of a split; the validator half is [key-config-validator].

Original framing was to confirm the Kotlin keyboard's character set matches the manifest. The [layout-config-source] decision removes that need: the app reads `resources/key-layout.json` directly, so its key set *is* the config and the two cannot disagree. What remains worth checking is wiring — a key correctly declared in the config can still render nothing, render in the wrong slot, or emit the wrong character.

The build: an instrumented or Compose UI test that walks every key in the config, asserts a key node exists at the expected panel, row and column, and asserts that activating it emits exactly the character the config declares. Long-press accents get the same treatment — each accent in a key's list is reachable and emits its own character. Failures name the character and its panel position.

This is what manifest rule 1 — verify the shipped key set before shipping — actually means once the config is authoritative: not a comparison of two lists, but proof that the one list reaches the screen intact.

Lift-condition: cleared to run once the Compose keyboard renders keys from the config, since there is nothing to drive until keys exist on screen.

#### Russian layout, copied from Gboard's Russian keyboard [language-starter-layouts]
A second layout config for the app: Russian, transcribed from FlorisBoard's Russian layout file. It is the first layout after English and the first to need rows wider than ten.

**Rewritten whole on 2026-09-02, the second rewrite that day.** The morning's version assumed the QWERTY panel gives 30 letter slots and hid two Russian letters behind long-presses; a /next run halted on it because four of those slots are backspace, cursor-left, cursor-right and enter, leaving 26. Rather than hide six letters, you settled the method that now governs every layout: **copy the standard phone keyboard for the language as far as Hexboard's layout allows, and improvise nothing.** Your one condition, in your words: "as long as we don't have missing keys, that's the main thing." SPEC's layout principle carries this.

**The source is a file, not a screenshot.** You first proposed screenshotting Gboard in each language, then withdrew it the same hour: Gboard offers several Russian layouts and reading them by eye is work you would rather not do. The replacement, found by web search with you present, is FlorisBoard's layout data — Apache 2.0, one JSON per layout and one popup file per language, read directly by Claude with nobody squinting at a phone. The finding is `workshop/resources/research/open-source-layout-sources.md`. Its Russian file, `jcuken_russian.json`, gives three letter rows of eleven, eleven and nine — Й Ц У К Е Н Г Ш Щ З Х · Ф Ы В А П Р О Л Д Ж Э · Я Ч С М И Т Ь Б Ю — and its `ru.json` popups put **Ё under Е and Ъ under Ь** and nothing else. Your Gboard screenshot of the default Russian keyboard, taken the same day, shows the identical rows, so the two sources agree.

**Why widening won, having lost the day before.** The morning's item refused wider rows because narrower keys cut against the big-keys thesis. That reasoning was Claude's, and it lost to copying the standard: there is no ten-column Russian phone keyboard to copy (Gboard and iOS both use 11-11-9), so any six-letter hiding scheme is an invention, and the tenth-narrower key is a trade every Russian typist already takes on Gboard. It applies to the Russian layout alone; English is untouched. Defeated with it: six invented pairings, and dropping the cursor keys to make 28 slots and four pairings — both improvisation. Spilling letters onto the RARE panel stays refused: a letter is not rare in its own language.

**The arrangement, all four rows eleven wide**, because `KeyboardPanel.kt` sizes keys from the panel's widest row (read 2026-09-02: `solveRadius(maxWidth, panel.maxCol + 1)`), so a ten-wide row 3 would sit narrower than the board rather than keep bigger keys:
- Rows 0 and 1: the eleven letters each, as shown. No backspace in row 1.
- Row 2: cursor-left at col 0, the nine letters at cols 1–9, backspace at col 10 — backspace where Gboard has it.
- Row 3: Hexboard's own row, shift through hyphen at cols 0–9 as in the English layout, plus enter at col 10 — enter on the right, where Gboard's is. The two space bars at cols 4 and 6 sit symmetric about the centre column.
- Cursor-right has no slot and is not on this layout. Gboard has no cursor keys at all, so this is the one key English has that Russian does not; named here so it reads as a decision rather than a loss.

The build:
- `resources/key-layout-ru.json` (new) — a schemaVersion 3 config: `id` `jcuken-ru`, `name` `ЙЦУКЕН (Russian)`, `isDefault: false`, `language` `ru`, `order`, `generates` naming its own manifest; the QWERTY panel's rows 0–3 with `colMax` 10 laid out as above, Ё as a long-press entry on Е and Ъ on Ь. Its `about` names FlorisBoard and the two file paths it was transcribed from, with the read date, and a `NOTICE` line for FlorisBoard's Apache 2.0 licence goes in the repository's `README.md` (the attribution rule the research file states). The RARE and SYMBOLS panels are copied from the English layout unchanged, since neither is language-specific.
- `resources/key-manifest-ru.md` (new) — generated by `python scripts/generate-key-manifest.py --config resources/key-layout-ru.json`, never hand-written.
- `planning/layout-preview.html` — its `LAYOUTS` block gains the Russian arrangement, so the board can be looked at without anyone editing JavaScript. The preview's geometry is fixed-radius, so an eleven-wide board simply renders wider.

The observation that shows it landed: the generator runs against the new config without error and writes the manifest to the path the config names; `--check` reports no drift; the manifest lists all 33 Russian letters, 31 as keys and Ё and Ъ as long-press entries, which is the no-missing-keys condition made checkable; and the layout renders in `planning/layout-preview.html` with the 31 visible letters in the rows above. Reads but does not change: `resources/key-layout.json`, for the RARE and SYMBOLS panels and row 3. Runs but does not change: `scripts/generate-key-manifest.py`. No Kotlin changes: the panel already sizes to the widest row.

**A check by a Russian reader before this ships is [russian-layout-check], and this item does not clear it.** It is now a confirmation of a copy rather than a judgment on Claude's pairings, but a shipped layout is copied rather than read, so it stays.

Cites research: `workshop/resources/research/open-source-layout-sources.md`, which carries the file paths, the licence and the row contents; and `workshop/resources/research/cyrillic-overflow-and-slot-budget.md`, superseded on 2026-09-02 and marked so at its top.

Rests on: the row contents and the two popups, read from FlorisBoard's `jcuken_russian.json` and `ru.json` on 2026-09-02; the function-key positions, read from your Gboard screenshot on 2026-09-02; `KeyboardPanel.kt` sizing keys from `maxCol`, read 2026-09-02; the `language` and `order` fields from [variant-language-fields], built 2026-09-02.

Which further languages follow is [language-list-choice], and each follows this same method: Claude reads FlorisBoard's layout and popup files for the language and transcribes them. The phonetic Russian layout (ЯВЕРТЫ) exists as a Gboard option and was not investigated; not ruled out.

Files: `resources/key-layout-ru.json`, `resources/key-manifest-ru.md`, `planning/layout-preview.html`, `README.md`.

#### Deprecated `srcDir` call in the app's Gradle build file [assets-srcdir-deprecation]
Noticed on 2026-09-02 during [run-key-config-validator], in the same Build panel output as the error that halted the sync, and filed rather than fixed because it stops nothing today.

The warning: `'fun srcDir(srcDir: Any): Any' is deprecated. Use 'directories' mutable set instead`. It is on `sourceSets["main"].assets.srcDir(generatedAssetsDir)`, the line that puts `key-layout.json` into the app's assets so the app can read the key inventory at runtime.

Why it is worth filing rather than leaving. This is the same API whose Provider-taking overload became a hard error in the plugin version now in use, which is what stopped the build in that session. A deprecation on the surviving overload is the same thing happening again more slowly, and the failure lands the next time the Android Gradle Plugin is upgraded — which is to say, at a moment chosen by somebody else.

**Settled on 2026-09-02: the Variant API route.** Wire the copy task's output through `androidComponents { onVariants { it.sources.assets?.addGeneratedSourceDirectory(task, { it.outputDir }) } }`, giving the copy task a `DirectoryProperty` output to hand over. That is the route the plugin's own error message recommended, and it restores the task-dependency wiring the plain-`File` fix gave up rather than only silencing the warning. The `directories` mutable set is the recorded fallback if the copy task cannot be made to expose that output on this plugin version — the build reads the AGP 9.2 documentation for the exact method shapes before editing, since neither was looked up during planning.

Gradle cannot run on this machine, so nothing here is verified by the build itself. The observation that shows it landed: at the next Android Studio run — [install-and-enable-on-pixel] is the next item that compiles — the Gradle sync passes without the `srcDir` deprecation in the Build panel, and the app still finds `key-layout.json` in its assets at runtime on the Pixel 6, which [compile-and-view-panel] established on 2026-09-02 and a regression here would break. Until that run, the change is unconfirmed; the item is cleared anyway because the alternative is a build file that breaks at a moment nobody here chooses.

Placed last in the cleared region: nothing depends on it, and a build-file change is the one kind that can stop a sync, so it should follow the Kotlin work rather than precede it.

Rests on: the deprecation text and the error message, read from Android Studio's Build panel on 2026-09-02; AGP 9.2.1, read from `android/gradle/libs.versions.toml` on 2026-09-02.

Files: `android/app/build.gradle.kts`.

#### Android Studio steps should name something visible, not a keyboard shortcut [android-studio-step-authoring]
A one-sentence rule added to `CLAUDE.md`: a walkthrough step for a GUI app names something visible to click or a menu path, and a keyboard shortcut may ride alongside as an aside but is never the instruction.

Filed on 2026-09-02, from driving two Android Studio items in one session, and kept the same day. It is a rule about how steps are written rather than work on the keyboard, so it lives in `CLAUDE.md`, which governs how Claude works on this project; SPEC is what the product is, and a queue item cannot hold a standing rule. A planning session may not edit `CLAUDE.md`, which is why this is a build rather than done on the spot.

The build: one sentence under **Project rules** in `CLAUDE.md`, in the wording above. The observation that shows it landed: a grep of `CLAUDE.md` for "never the instruction" finds it. Placed beside [claude-md-phase-ran], which edits the same file, so one run does both.

The same instance was also reported to the plugin's own project by mail on 2026-09-02, since the method's walkthrough rule already asks each step to name the thing to click and this is a sharper version of it; that send is in `INBOX/sent.md`.

Rule gate: one rule is added, and it earns its place because the failure it prevents was seen twice in one session and leaves no evidence when it happens — nothing to report, nothing to diagnose. It sharpens an existing project practice rather than opening a new subject, and it evicts nothing: no current `CLAUDE.md` sentence covers how a GUI step is worded.

What happened. A step said to press Shift twice to open the search box and type the test's name. Nothing opened, and the reported symptom was "I did it but nothing much seemed to happen" — the failure of a double keypress is silent and leaves nothing on screen to react to. Re-issued as a click path down the Project pane's tree — expand `kotlin+java`, then the entry suffixed `(test)` — it worked first time, with each expansion confirming itself.

The rule that follows: a step names something on screen that can be clicked, and a menu path where a menu will do, rather than a keyboard shortcut. A shortcut that fails produces no evidence, so there is nothing for the person following it to report and nothing for the person writing it to diagnose. The same session saw this twice — the sync was given as `File → Sync Project with Gradle Files` only after the toolbar button and the banner link had both been offered and neither found.

The one thing to weigh before adopting it wholesale: shortcuts are faster once known, and a rule written too strictly would ban naming one at all. The useful form is probably that the click path is the step and a shortcut rides alongside it as an aside, never as the instruction.

This generalises a fix filed the same day for one item, [install-walkthrough-refresh].

#### CLAUDE.md's phase line says the Kotlin has never run, and it has [claude-md-phase-ran]
Filed at the close of 2026-09-02, from a stale sentence this same session created. [claude-md-phase-stale] replaced "extended planning, no implementation" with wording saying that Android implementation has begun and that none of it has been compiled or run on a device, naming [compile-and-view-panel] as the item that would change that. Later in the same session [compile-and-view-panel] was driven to its end: the app compiled, installed on the Pixel 6, drew its keys, and typed the characters it was aimed at.

So the second half of that sentence is now false, and it points at an item that has since been done. It is the first thing a fresh session reads about the state of the code, which is exactly why the previous version was worth correcting.

The build: replace the has-not-been-compiled clause in `CLAUDE.md`'s project-rules section with what is now true — the app has been built and run on a Pixel 6, the QWERTY panel draws from the config, and taps land on the keys they were aimed at — and drop the reference to [compile-and-view-panel]. What should survive from the old wording is the caution it carried in both versions: very little of the app exists, so designing before coding still holds.

**Decided on 2026-09-02: the line states the phase, not a checklist of what has run.** A phase changes rarely — the next one is "a keyboard you can switch on", when [first-installable-build] lands — so a phase line goes stale at moments worth a sentence anyway, where a what-has-run line goes stale at every run. The build writes this replacement for the first two sentences of the phase paragraph in `CLAUDE.md`'s project-rules section, leaving the rest of the paragraph (target stack, the browser prototype as reference) as it is:

> Current phase: Android implementation is under way and has run on a Pixel 6 — the QWERTY panel draws from the config and types the keys it is aimed at. There is no input method service yet, so it is an app rather than a keyboard. Very little exists, so keep designing before coding rather than rushing new work into the app.

The observation that shows it landed: a grep of `CLAUDE.md` for "none of it has been run" and for "compile-and-view-panel" returns nothing, and one for "has run on a Pixel 6" finds the new sentence. Placed beside [android-studio-step-authoring], which edits the same file, so one run does both.

Why this was not simply fixed when it was noticed: the session that made a choice is not the session that certifies it, and a build does not write project rules twice in the session that wrote them once.

Rule gate: not needed — no rule is added or removed. This replaces a stale statement of fact in the phase paragraph; the design-before-coding caution it carries is kept as an amendment to that same sentence, as the previous version did, so nothing new competes for a reader's attention.

Files: `CLAUDE.md`.

#### [user] Read SPEC end to end and say whether it still sounds like your project [spec-coherence-readthrough]
Lifted above the line on 2026-09-02: [spec-principles-rework] was built that day and confirmed by the greps its record names, so the rebalanced SPEC this reads is the one on disk.

Filed by /rescan on 2026-09-01, from a suggestion made in passing during that session's planning and never written down.

SPEC took seven changes in one session on 2026-09-01: `key-layout.json` reworded from the canonical layout to the default layout the app ships with; a sentence saying the layout picker lives in the app's own settings; press feedback as the key's own highlight with no touch-point marker; the persistent clipboard with its hour-and-twenty rule, drag-to-bin deletion and five-minute rule for password clips; voice input held open by the thumb; voice recognition adapting to its own user with enrolment audio destroyed after adaptation; and the predictive text principle rewritten around saved words and no proper nouns. The stale "Project docs" section was removed the same day.

It changed again on 2026-09-02: the platform principle was rewritten from variants arriving by outside contribution to Hexboard shipping a layout per language copied from that language's own standard, and the key-inventory principle reworded to match. That rewrite replaced sentences written the day before, which is itself a reason to read the whole thing.

Each of those sentences was written against its own discussion and is correct on its own terms. Nobody has read the result end to end. The risk is a document that is accurate line by line and no longer reads as a description of a keyboard — a pile of rulings rather than product truth — and that is a judgment about tone and coherence rather than about facts, which is why it is yours.

**Narrowed to one question on 2026-09-02, and this is the whole change.** The item originally asked for four things. Two of them — spotting sentences that describe machinery rather than product truth, and spotting a feature that has outgrown its importance — turned out to need no judgment about what you meant, only a careful read, so they were done in that session's planning and became [spec-principles-rework]. What is left is the one thing Claude genuinely cannot do: whether SPEC still sounds like *your* project. Claude wrote most of those sentences, so it would be marking its own homework.

The walkthrough:
1. Open `SPEC.md` and read it start to finish in one sitting, without stopping to fix anything. Look for: whether it still describes one product with a point of view, or reads as a list of decisions that happen to sit together.
2. Report what you found, in whatever words come. Any rewording is ordinary planning work filed from what you report, not something to do while reading.

This names no observable that a later session could check — nothing in the world changes when it is done — so it waits until you mention it rather than being verified.

Held below the line against [spec-principles-rework], which rebalances the Principles list. Reading the document to judge its coherence is worth doing once, after that rewrite, rather than twice. That ordering is written on both items.

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

