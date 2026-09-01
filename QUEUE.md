# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Flip the Hexboard repo from private to public on GitHub [repo-go-public]
Captured by you. Split out of [licence-and-go-public] during planning.
Runs alone

Retagged from `[user]` to Claude-work on 2026-08-14, on your instruction, after the original premise turned out to be false. The item had said only you could do this because it was "an account action on github.com that Claude can't perform". That is wrong: the `gh` command-line tool on this machine is signed in with admin rights over the FlintcraftTech organisation, so the flip is a single command. The browser walkthrough this replaces is preserved in git history if it is ever needed again.

The work is one command:

```
gh repo edit FlintcraftTech/Hexboard --visibility public --accept-visibility-change-consequences
```

The consequences flag is not optional — gh refuses the command without it whenever `--visibility` is used. The owner name carries a lowercase `c`, GitHub's current spelling, confirmed against the GitHub API on 2026-08-14.

Then verify two ways, because the authenticated view cannot tell you what a stranger sees. `gh repo view FlintcraftTech/Hexboard --json isPrivate` should report `false`; and an unauthenticated request to `https://api.github.com/repos/FlintcraftTech/Hexboard` should return 200 rather than the 404 it returns today.

**Ask before running it, and get an explicit yes in that same session.** Retagging changed who types the command, not who decides. Going public is irreversible in the way that matters — once the history is public it can be cloned, so making the repo private again does not un-expose anything. Whoever runs this must first say plainly what is about to become readable, and stop for an answer.

Lift-condition, now met and recorded so the clearing can be checked rather than trusted: [add-licence] (done), [git-history-audit] with its findings dealt with (processed in the /plan session of 2026-08-06), [untrack-faq], [git-noreply-email], and [public-readme] have all landed. The licence should be in place before anyone can read the code; the audit was the red-flag mitigation; the FAQ should leave the tree before the tree is public; and the noreply address should be set before any further commit is published. Cleared above the readiness line on 2026-08-14 on that basis.

Marked `Runs alone` because it is irreversible and outward-facing. A run that reaches it should stop there rather than carry the same session's momentum into the ask.

Placement, decided on 2026-08-14 and recorded so a later re-sort does not undo it. This sits last among the build items and ahead of [run-key-config-validator], which is the only cleared `[user]` item. It goes after [throughliner-doc-drift] and [emoji-panels-missing-from-config] because both of those correct text that this item publishes — CLAUDE.md still names the old method, and the config still claims a scope SPEC has since narrowed. It goes *before* the validator test because that test is not a gate for going public, and a run halts at a `[user]` item: left in the usual end-preferred position, the validator would have blocked the flip from ever being reached in an unattended run.

What the irreversibility means for the three limitations recorded elsewhere, and which the ask above must name — the personal address in commit metadata and in file content at `a42cd01`, the reworded candid line at `6e09dad`, and the FAQ in every commit so far — all remain readable in history after this runs, and each was consciously accepted rather than overlooked.

#### [user] Run the key-config validator test in Android Studio [run-key-config-validator]
Captured by you. Confirmed as user-work during planning on 2026-08-06, after Claude exhausted every route it had.

Claude wrote `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` during the build of [key-config-validator] but has never been able to execute it. What is unverified is the Kotlin, not the key data: the config itself was independently checked when it was built, by reimplementing all six checks in a throwaway Python script, and every one passed against `resources/key-layout.json`. What running the test proves is that the test compiles, that Gson resolves as a test dependency, and that the test finds the config file at runtime.

Why this can't be Claude's to run, established by attempt rather than assumption. Gradle needs a loopback network connection to reach its own daemon, and every route Claude has is blocked from making one. Tried on 2026-08-06: Gradle via the Bash tool (failed — no Java on PATH); the same with Android Studio's bundled JDK 21 at `C:\Program Files\Android\Android Studio\jbr`, which Java-wise works fine (failed — "Unable to establish loopback connection"); the same again with Claude's sandbox disabled (identical failure, so the sandbox is not the cause); and via PowerShell with `--no-daemon`, which still forks a single-use daemon (identical failure). The block is below the level Claude can reach. Don't spend another session re-testing this — run it in Android Studio, which has no such restriction.

The walkthrough:
1. Open Android Studio and open the `android` folder inside the Hexboard project. Wait for the Gradle sync to finish — there's a progress bar along the bottom, and a notification strip across the top if it wants anything.
2. In the Project pane on the left, navigate to `app/src/test/java/tech/flintcraft/hexboard/` and open `KeyLayoutValidationTest.kt`.
3. In the narrow gutter immediately left of the code, next to the line declaring `class KeyLayoutValidationTest`, there's a small green triangle. Click it and choose **Run 'KeyLayoutValidationTest'**.
4. Results appear in a panel at the bottom. Report what you see: all six tests green, a compile error, or a test failure. A test failure names the offending character and its panel, so the message itself tells us what's wrong.

If it reports that it can't find the config file, the likely cause is the `hexboard.repoRoot` system property set in `android/app/build.gradle.kts`; the test also walks up from the working directory as a fallback, so both paths would have to fail.

Nothing blocks this — it can run whenever you next have Android Studio open.

#### [user] Compile the app in Android Studio and look at the QWERTY panel [compile-and-view-panel]
Filed at the close of 2026-08-21, when [compose-keyboard-renders-config] shipped code that nothing has run. Gradle cannot run on this machine — it needs a loopback connection to its own daemon and every route Claude has is blocked from making one, established across four attempts and recorded in [run-key-config-validator] — so three things are unverified: that the new Kotlin compiles, that Gson parses `key-layout.json` out of the app's assets at runtime, and that the panel draws.

Nothing in the queue already covers this. [run-key-config-validator] runs a unit test against the config and never touches the app; [install-and-enable-on-pixel] does cover a real build, but it is held behind [first-installable-build], so the first compile of this code would otherwise wait on an IME service that has not been written. Checking it now is what stops a broken foundation being built on.

Where it runs, settled on 2026-09-01. The original step 2 said "with any device or emulator selected", which assumed a device target that is not set up. Asked directly, you chose the Pixel 6 over Wi-Fi rather than an emulator or a compile-only Build → Make Project. The compile-only option was the one rejected with a reason worth keeping: it proves the Kotlin compiles and proves nothing about whether Gson finds the config in the assets at runtime or whether the panel draws, which is two thirds of what this item exists to answer.

The pairing in steps 1–4 below is the same pairing as steps 1–2 of [install-and-enable-on-pixel]. Once this item is done, that one starts at its own step 3.

The walkthrough:
1. On the Pixel 6, check whether **Developer options** is listed under Settings → System. If it is not, go to Settings → About phone and tap **Build number** seven times — a message counts down and then says you are a developer. Look for: Developer options now appearing under Settings → System.
2. Open Developer options, turn **Wireless debugging** on, then tap its name rather than its toggle to open it. Look for: an entry reading **Pair device with QR code**.
3. In Android Studio, open the `android` folder inside the Hexboard project and wait for the Gradle sync to finish — a progress bar runs along the bottom. Then open the device dropdown in the top toolbar and choose **Pair Devices Using Wi-Fi**. Look for: a window showing a QR code.
4. On the phone, tap **Pair device with QR code** and point the camera at that code. Look for: the Pixel 6 appearing by name in Android Studio's device dropdown.
5. With the Pixel 6 selected in the dropdown, click the green ▶ Run button. Look for: the Build panel along the bottom. A compile error names a file and a line — report that text if it comes.
6. If it runs, the phone shows the words **Tap the keys** at the top and the QWERTY panel at the bottom: circular keys in zig-zag rows, with the two space bars in the bottom row. Tap a few keys and check that the text at the top matches what you aimed at.
7. Report three things: whether it compiled, whether the keys drew, and whether the characters that arrived were the right ones.

What step 6 describes was confirmed against `MainActivity.kt` on 2026-09-01 rather than assumed: the screen holds a `Text` reading "Tap the keys" until something is typed, with `KeyboardPanel` beneath it, and each key press appends its output character.

If it compiles but crashes on launch, the likely cause is the config not being found in the assets — the Gradle copy task that puts it there is in `android/app/build.gradle.kts` and the app reads it by the filename `key-layout.json`.

Placed after [run-key-config-validator] in the cleared region because both are Android Studio jobs done in one sitting.

#### [user] Look at the three row-3 arrangements and pick one [row3-space-choice]
Cleared on 2026-09-01. [row3-space-candidates] shipped on 2026-08-21 and its record says the three arrangements were rendered and confirmed in a browser, so the thing this step looks at exists and has been seen to draw. What the choice rests on is that preview page — `planning/layout-preview.html`, verified on 2026-08-21 — and nothing since then has touched it.
Split out of [left-space-relocation] on 2026-08-20. This is the deciding step, and it is genuinely yours: the trade-off is between a matched-looking pair and equal thumb reach, which is a judgment about how the keyboard feels rather than anything that can be computed.

The walkthrough:
1. Open `planning/layout-preview.html` by double-clicking it — it is a plain page and needs no server. Look for three rows stacked down the page, each labelled with its column numbers.
2. Compare them with your thumbs where they would actually sit holding a phone. The current arrangement is the 4-and-6 row; the other two are the candidates.
3. Say which you want: 2-and-6 (matched height, off-centre by one) or 3-and-6 (even reach, spaces at different heights) — or that today's 4-and-6 is fine after all, which is a real answer and closes the whole thread.

Your choice releases [left-space-relocation], which applies it to the config.

--- Cleared to run above this line ---

#### Register Hexboard as an Android input method [first-installable-build]
Blocked by: [compose-keyboard-renders-config]
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

#### Verify every key in the config actually renders and emits its character [android-key-audit]
Blocked by: [compose-keyboard-renders-config]
Captured by you. Rewritten during planning as the second half of a split; the validator half is [key-config-validator].

Original framing was to confirm the Kotlin keyboard's character set matches the manifest. The [layout-config-source] decision removes that need: the app reads `resources/key-layout.json` directly, so its key set *is* the config and the two cannot disagree. What remains worth checking is wiring — a key correctly declared in the config can still render nothing, render in the wrong slot, or emit the wrong character.

The build: an instrumented or Compose UI test that walks every key in the config, asserts a key node exists at the expected panel, row and column, and asserts that activating it emits exactly the character the config declares. Long-press accents get the same treatment — each accent in a key's list is reachable and emits its own character. Failures name the character and its panel position.

This is what manifest rule 1 — verify the shipped key set before shipping — actually means once the config is authoritative: not a comparison of two lists, but proof that the one list reaches the screen intact.

Lift-condition: cleared to run once the Compose keyboard renders keys from the config, since there is nothing to drive until keys exist on screen.

#### Apply the chosen row-3 space arrangement to the key config [left-space-relocation]
Blocked by: [row3-space-choice]
Reduced to the applying step on 2026-08-20, when the item was split three ways. Putting the candidates on screen is [row3-space-candidates] and choosing between them is [row3-space-choice]; what stays here is writing the winner into the config. The finding that forced the split — that two space bars cannot be both edge-symmetric and at the same height, because the zag rule keys on column parity — is recorded in full on [row3-space-candidates].

One of the item's two original open questions answers itself, and the answer is worth having before the choice comes back. Both candidates are **swaps, not vacancies**. Moving the left space to col 2 displaces the comma and frees col 4, so the comma takes col 4. Moving it to col 3 displaces `!` and frees col 4, so `!` takes col 4. Either way nothing is left empty, so SPEC's rule that a freed slot must be filled with a character that has no other home never fires — there is no freed slot. If you pick today's 4-and-6 after all, this item is deleted rather than built.

The build: edit row 3 of the `qwerty` panel in `resources/key-layout.json` to the chosen arrangement, then regenerate `resources/key-manifest.md` by running `scripts/generate-key-manifest.py` rather than hand-editing it, as the config's own `about` field requires. The character set is unchanged — every key that exists still exists, in a different column — so SPEC's no-key-may-be-lost and no-unresolved-duplicates rules are satisfied by construction, and the commit message carries the move per the no-silent-changes rule.

Files: `resources/key-layout.json`, `resources/key-manifest.md`.

Original framing, kept for the record. Carried out of `hexboard-plan.md` on 2026-08-07, when that document was folded into SPEC and deleted. It was the one open question in it that nothing else records, so deleting the file as-is would have lost it.

The two space bars in QWERTY row 3 currently sit at col 4 and col 6. Confirmed against `resources/key-layout.json` during this planning session: left space is still at col 4, and the comma is still at col 2. Because col 4 and col 6 are adjacent-but-one rather than symmetric about the row, the two spaces are not equidistant from the screen edges, so the left-thumb key is further from the left thumb than the right-thumb key is from the right thumb. Moving the left space to col 2 makes the pair roughly symmetric, one comfortable per thumb.

Two questions the move opens, and they are the reason this is not a one-line change. The comma currently at col 2 has to go somewhere. And col 4 is then vacant — which SPEC's manifest rules say is an opportunity to be filled deliberately with a character that has no other home, not a gap to leave. Both need agreeing before any edit to the config.

Not designed enough to build. It needs a layout-preview session: edit the `LAYOUTS` block in `planning/layout-preview.html` to try candidate arrangements and look at them, rather than reasoning about column numbers in the abstract.

Filed after `e6742fc`.

#### Horizontal swipe between the three letter panels [panel-switch-gestures]
Blocked by: [compile-and-view-panel]
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

Shares `KeyboardPanel.kt` with [key-press-feedback], which is held against the same item. Whichever is built second should expect the other's changes to be there already. They also meet in one place rather than merely coexisting: a gesture that turns into a panel swipe must not leave a key stuck lit, which is stated on both.

Held below the line against [compile-and-view-panel] rather than against the rendering item, which shipped. The old wording said this waits on "a rendering surface that exists" — the surface exists as code, but nothing has compiled or run it, and the observation above is a thing seen on a phone. The compile check is that release condition made into an item that can actually resolve.

#### Key press feedback — the pressed key lightens and fades back [key-press-feedback]
Blocked by: [compile-and-view-panel]
Captured by you on 2026-09-01 and designed with you in the same session. Your words for the problem: the keyboard as it stands is a bit inert.

**What was decided, and it is narrower than the original ask.** You first described touches showing and fading on each press alongside the key highlight. Presented with the choice, you chose the key highlight alone: the pressed key lightens instantly, holds, then fades back, and nothing marks where the finger actually landed.

**Why the touch-point marker lost, recorded because it is the more obvious of the two and will look like an oversight.** The board's tap handler resolves a tap by nearest centre, so the touch target is larger than the drawn circle and a tap landing between two circles still goes somewhere definite. A marker at the real touch point would therefore show the gap between where you aimed and which key won. That is honest feedback, and it is exactly the feedback SPEC's perceptual wedge does not want: the wedge is about aiming *confidently* at circles, and a display that draws the eye to near-misses the routing already absorbed works against it. The highlight alone still answers "did that register, and on which key".

**The build:** in `KeyboardPanel.kt`, hold per-key press state at the board level — which is where the tap is already resolved — and render the resolved key with a lighter fill that animates back to its normal colour. Concurrent presses each animate independently, because a fast typist starts the next press before the last has finished fading. A gesture that turns into a panel swipe rather than a tap must not leave a key stuck lit.

**Timing, to be set by eye rather than by argument.** Start with the highlight instant, a brief hold, and a fade of about 150ms. These are opening values; you will have the keyboard in front of you when you run [compile-and-view-panel], and adjusting them on sight is the point rather than a fallback.

The observation that shows it landed: on the Pixel 6, tapping a key visibly lightens that key and it fades back, and typing quickly shows several keys fading at once rather than one cancelling another.

Held below the line against [compile-and-view-panel] rather than cleared. This changes code that has never been compiled, and stacking a second unverified change on the first means debugging both together if the compile fails. The cost was named to you and accepted: the effect appears on the phone a sitting later than it otherwise would. Placement interacts with [panel-switch-gestures], which is held against the same item and touches the same file — whichever is built second should expect the other's changes to `KeyboardPanel.kt` to be there already.

Rests on: `KeyboardPanel.kt` resolving taps at board level by nearest centre, read 2026-09-01; SPEC's perceptual wedge, which is what rules out the touch-point marker.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

#### [user] Install Hexboard on the Pixel 6 and switch it on as a keyboard [install-and-enable-on-pixel]
Blocked by: [first-installable-build]
Split out of [first-installable-build] during the /plan session of 2026-08-20. Claude writes the IME service; producing the APK and putting it on the phone is yours, and burying that in the build item's prose would have left it invisible as next-work.

Confirmed by attempt rather than assumed, at the keep-step. There is no `adb` on this machine — not on PATH, and no Android SDK platform-tools directory in either of the two places it installs to — so Claude has no route to a device even if one were connected. And Gradle cannot run here at all: it needs a loopback connection to its own daemon, which every route Claude has is blocked from making, established across four attempts on 2026-08-06 and recorded in [run-key-config-validator]. So Claude cannot build the APK either. Android Studio has neither restriction and does both in one click.

Steps 1 and 2 below are the same pairing as steps 1–4 of [compile-and-view-panel], which was cleared to run on 2026-09-01 and does the pairing first. If that item has been done, the phone is already paired and this one starts at step 3.

The walkthrough:
1. On the Pixel 6, open Settings → System → Developer options → Wireless debugging and turn it on. Look for the entry **Pair device with QR code**.
2. In Android Studio, open the device dropdown in the top toolbar and choose **Pair Devices Using Wi-Fi**, then scan the QR code with the phone. You'll know it worked when the Pixel 6 appears by name in that same dropdown.
3. With the Pixel 6 selected in the dropdown, click the green ▶ Run button. Watch the Run panel along the bottom — success reads **Install successfully finished**. An error there is a build failure, not a phone problem; report the text.
4. On the phone, open Settings → System → Languages & input → On-screen keyboard → **Manage on-screen keyboards**, and switch **Hexboard** on. Android shows a warning that a keyboard can collect what you type — that dialog is expected for any keyboard, and you have to accept it to continue.
5. Open anything with a text field and tap into it. A small keyboard icon appears in the navigation bar at the bottom right — tap it and choose **Hexboard**. You should see circular keys in zig-zag rows.
6. Report three things: whether it appears at all, whether keys respond to a tap, and whether the characters that arrive in the text field are the ones you aimed at.

If step 5 shows no keyboard icon, the service is registered but crashing on first show; the likely cause is the Compose lifecycle-owner trap named in [first-installable-build], and the Run panel's log will say so.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Blocked by: [first-installable-build], [install-and-enable-on-pixel]
Captured by you. The second blocker was added on 2026-08-20: TalkBack cannot be tested against a keyboard nobody has switched on, so this waits on the install as well as on the code that makes it installable. Once Hexboard is running on the Pixel 6, confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

> Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Last session advises processing repo-go-public next [forward-advisory]
Filed at the close of 2026-09-01, replacing the spent advisory that pointed at [compile-and-view-panel]. That one has done its job: it was written to say the Compose keyboard was code nobody had run, and it still is, but it is no longer the first thing a run meets.

[variant-schema] shipped this session and left the queue, so [repo-go-public] is now the top cleared item. It is marked `Runs alone` and it sits first, so a /next run will build it and then end — and it is irreversible in the way that matters, since once the history is public it can be cloned and making the repository private again un-exposes nothing. Whoever runs it must say plainly what becomes readable and stop for an explicit yes in that same session. The three limitations already recorded on the item — a personal address in commit metadata and in file content, a reworded candid line, and the FAQ in every commit so far — all stay readable in history afterwards, and each was consciously accepted rather than overlooked. Read the item before starting it, not after.

Everything cleared below it is `[user]` work, so nothing else in the queue can be built without you present. Of those, [compile-and-view-panel] is still the one that unlocks the most: [panel-switch-gestures] and [key-press-feedback] are both held against it, and both change `KeyboardPanel.kt`, which has never been compiled.

The overlap scan was run against the unprocessed work, and one thing came out of it rather than nothing. [variant-editor] and [layout-switching] were both held by [variant-schema], which has now shipped — so their blocker is resolved and they are candidates to lift, not items whose premise failed. Neither contradicts or invalidates [repo-go-public]; they are simply the work that this session's build released. Nothing else waiting in Unprocessed touches the top cleared item.

#### A contributor-facing layout editor for building language / key-set variants [variant-editor]
Blocked by: [variant-schema]
Split on 2026-09-01, which changes what holds this item and how it comes back. The one thing that stopped it designing out — that a contributed config has no way to say which variant it is — is now its own piece of work, [variant-schema], cleared to run. Half of the 2026-08-20 reasoning did not survive the re-reading: that entry said the schema "follows from a build, not from a decision anyone can make now", and only part of it does. How the app enumerates layouts and stores the user's pick waits on a working keyboard; whether a variant is its own file, what identifies it, and what happens to the generated manifest are desk decisions, and they are what [variant-schema] settles. The recommendation to split was Claude's; you agreed to it.

Kept in Unprocessed rather than filed below the cleared-to-run line, deliberately. Below the line means designed and buildable but held, and this is not yet buildable even with the schema in hand — what the editor is (a page in `planning/`, a rework of `hexboard-editor.html`, something else) and what it writes are still undescribed. The `Blocked by:` line above therefore does what it does on a capture: this stops being offered until [variant-schema] resolves, then returns by itself. Nothing here is waiting on you.

Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. (That decision was taken on 2026-08-20 and is recorded further down; the two sentences before this one describe the fork framing the same session rejected, and are kept as the original capture rather than as current design.) Prerequisite, now met: [layout-config-source] has landed. `resources/key-layout.json` exists and SPEC names it the canonical manifest, so the config this editor would produce and the Android build would consume is real rather than hypothetical. Confirmed during the /plan session of 2026-08-07. Far downstream of a first Android build; filed as a design thread, not near-term work.

Two things were settled on 2026-08-20, and they change what this item is.

**Hexboard is a platform for variants.** The scope decision underneath this item — one opinionated keyboard, or a platform other people build on — had been deferred three times as better made with a working keyboard in hand, and was never actually put to you. Asked directly, you chose the platform. It is now written into SPEC as a principle, so this item no longer carries a scope question.

**Variants arrive by contribution, not by fork, which dissolves this item's hardest open question rather than answering it.** The fork-sync problem below exists only because a fork copies the code; since the geometry is inviolable and only the key inventory varies, a variant is one JSON file and needs no copy of the code at all. So layouts are contributed into this repository and one app ships them all. A fix to the rendering code then reaches every variant at once and nothing has to be re-synchronised. Your reason for the choice was that you were unsure where the fork sat best in the picture; the recommendation was Claude's, on the grounds that it makes that uncertainty stop existing. The costs were named and accepted: reviewing contributed key data, and the app eventually needing layout switching, which SPEC currently defers.

The paragraph below is kept as the record of the fork question and why it lost, not as live design.

Carries an open question relocated from [licence-and-go-public] during planning: once language forks exist, how do they stay in step with canonical Hexboard as it changes? This is the part of the item that most needs outside input — it's a question about distribution and project governance rather than about the keyboard itself, and nothing already decided here settles it. It's an upstream/downstream design question — fork-and-cherry-pick, a shared config, or contribution-back terms written into the licence itself — and it interacts with [layout-config-source], since a shared machine-readable key config is one way forks track upstream without merging code. Not answerable until the config lands and a real fork exists.

Sharpened in the /plan session of 2026-08-04, then deliberately deferred rather than designed. Two things are now settled that narrow it: the editor's target format is `resources/key-layout.json`, no longer TBD; and because the config carries the key inventory only, with geometry staying in Kotlin, the editor's scope is hard-bounded to key data and structurally cannot touch the perceptual wedge. What still blocks design is that none of its subjects exist yet — no keyboard, so nothing to vary; no fork, so the fork-sync question has no real case to reason about; and the SPEC-level scope decision underneath it (whether Hexboard becomes a platform for variants rather than one opinionated keyboard) is better made with a working keyboard in hand than in the abstract.

Prior art exists and should be read before designing this. `hexboard-editor.html` — relocated to `planning/` by [hexboard-editor-status] — is a working drag-and-drop key-arrangement editor on the real zag geometry, with panel tabs and locked structural keys. Only its export is obsolete: it emits JavaScript fragments for the prototype rather than `resources/key-layout.json`. The interaction design is the expensive half and it is already built.

Re-weighed on 2026-08-07 and deferred again, with the prerequisite above now cleared. What still blocks design is unchanged and is not something a planning session can resolve by thinking harder: there is no keyboard yet, so there is nothing to vary; there is no fork, so the upstream/downstream sync question has no concrete case to reason about; and the scope decision underneath it is better made with a working keyboard in hand.

Skipped again on 2026-08-20, but for one precise reason rather than three vague ones, so the next session knows exactly what it is waiting for. Two of the three old reasons are gone: the scope decision is made, and the fork-sync question is dissolved. What remains is a schema question the second limb fails on. An editor has to write a config that identifies *which variant it is* — a language or key-set identifier, and whatever else distinguishes one contributed layout from another — and `resources/key-layout.json` has no such field today, because it was designed when there was exactly one layout. That shape cannot be settled here, because it depends on how the app holds and switches between several layouts, which is undesigned. Until then the editor's output format is unknown and no honest file list can be written.

Who owns what: the schema follows from a build, not from a decision anyone can make now. Nothing here is waiting on you.

[layout-switching] is held by this same unknown and was skipped alongside it on 2026-08-20. The two are worth processing together whenever the schema question comes up, since one answer releases both: an editor has to write a variant identifier and a picker has to read one.

#### Let a user choose which layout variant they are typing on [layout-switching]
Blocked by: [variant-schema]
Where the picker lives was settled by you on 2026-09-01 and is now written into SPEC's platform principle: the choice is made in the app's own settings, not on the keyboard surface. The keyboard-surface option — a long-press or a gesture, reachable without leaving what you are typing — lost because horizontal swipe already means "change panel", so putting the picker on the keyboard would have to find a gesture that does not collide with one the layout already spends. Recording the defeated option here so it is not re-proposed as the obvious idea it looks like.

What remains open beyond the schema, and none of it is a decision anyone can make at a desk: how the app finds the available layouts at runtime, and where it remembers the choice. Both wait on a keyboard that runs.

Held on the same terms as [variant-editor], and for the same single reason — a picker has to read a variant identifier and no config carries one yet. The `Blocked by:` line above keeps this out of the offering until [variant-schema] resolves, then it returns by itself. It stays in Unprocessed rather than below the cleared-to-run line because what a build would change cannot yet be stated: there is no settings screen to add to, the app having no IME service at all today. Nothing here is waiting on you.

Filed by /rescan on 2026-08-20. SPEC gained a principle that day — Hexboard is a platform for layout variants, contributed into this repository, with one app shipping them all. That principle carries a requirement nothing in the queue holds: if one app ships several layouts, the user has to be able to pick theirs.

It was named as an accepted cost when the decision was made and then not filed, which is exactly how a feature dies in SPEC. SPEC also still puts language switching out of scope for early iterations, so this is deliberately not urgent — what it must not be is absent.

Interacts with [panel-switch-gestures]: horizontal swipe already moves between the three letter panels, so whatever switches layouts must not collide with a gesture that already means something.

Skipped on 2026-08-20 rather than kept, and held by the same single unknown as [variant-editor]. `resources/key-layout.json` has no notion of more than one layout — it was written when there was exactly one, so it carries no variant identifier and no way to say which layout a set of keys belongs to. Until that shape exists there is nothing to say about what a picker reads or writes. The shape follows from a build rather than from any decision available now, so nothing here is waiting on you.

SPEC also puts language switching out of scope for early iterations and the platform principle says the app must *eventually* let a user switch layouts, so the deferral is what SPEC asks for rather than a delay against it.

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

#### [user] Read SPEC end to end and say whether it still sounds like your project [spec-coherence-readthrough]
Filed by /rescan on 2026-09-01, from a suggestion made in passing during that session's planning and never written down.

SPEC took seven changes in one session on 2026-09-01: `key-layout.json` reworded from the canonical layout to the default layout the app ships with; a sentence saying the layout picker lives in the app's own settings; press feedback as the key's own highlight with no touch-point marker; the persistent clipboard with its hour-and-twenty rule, drag-to-bin deletion and five-minute rule for password clips; voice input held open by the thumb; voice recognition adapting to its own user with enrolment audio destroyed after adaptation; and the predictive text principle rewritten around saved words and no proper nouns. The stale "Project docs" section was removed the same day.

Each of those sentences was written against its own discussion and is correct on its own terms. Nobody has read the result end to end. The risk is a document that is accurate line by line and no longer reads as a description of a keyboard — a pile of rulings rather than product truth — and that is a judgment about tone and coherence rather than about facts, which is why it is yours.

The walkthrough:
1. Open `SPEC.md` and read it start to finish in one sitting, without stopping to fix anything. Look for: whether it still describes one product with a point of view, or has become a list of decisions.
2. Note anything that reads as machinery rather than product truth — a sentence explaining *how* something is implemented rather than what the keyboard does. Look for: internal field names, file formats, or steps a component runs through.
3. Note anything that now says too much for its importance, particularly in the Principles list, where a minor feature can end up with more words than the perceptual wedge.
4. Report what you found. Any rewording is ordinary planning work filed from what you report, not something to do while reading.

This names no observable that a later session could check — nothing in the world changes when it is done — so it waits until you mention it rather than being verified.

