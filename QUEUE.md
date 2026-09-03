# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Gradle's build output moves to a short path outside Drive, set per machine [build-output-off-drive]
The build directory becomes configurable per machine, and on this machine it points at `C:\builds\hexboard` — short, and outside `My Drive`.

**Raised by you on 2026-09-03**, from the first Run of that day failing with five `java.nio.file.AccessDeniedException` errors out of `Files.deleteIfExists` against `android/app/build/intermediates/incremental/debug/` — Gradle unable to delete its own output because something else held it open. Deleting the build folder and pausing Google Drive got the install through.

**Three suspects, none proved, and that is the point of this design.** Google Drive syncing files Gradle is still writing is the likeliest holder, because the project folder sits inside `My Drive`. Windows Defender is the other candidate. Neither was demonstrated: pausing Drive and deleting the folder happened together, so the successful run is evidence rather than proof. The third suspect came from the Taskflow project on 2026-09-03, brought over by you: Windows' 260-character path ceiling, which had bitten there at 279 characters.

**What the path measurement found here, which is a near-miss rather than a cause.** The project root is 57 characters. The deepest file Gradle currently writes is 234 — `.../intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes/tech/flintcraft/hexboard/MainActivityKt$DictationTest$lambda$22$lambda$21$$inlined$onDispose$1.class`, a Kotlin-generated synthetic class name. That is under the ceiling by 26 characters, and an over-length path raises a different error than the one seen, so path length did **not** cause the 2026-09-03 failure. It is a hazard rather than a diagnosis: those synthetic names grow with the code, so one more nested lambda inside a longer-named composable crosses it, and the failure then arrives intermittently. Measured on 2026-09-03.

**Why one change answers all three.** A short path outside `My Drive` takes the output away from Drive's sync, away from whatever else was holding it, and roughly doubles the headroom under the path ceiling. `C:\builds\hexboard` is 18 characters against the project root's 57, so every build path loses 39 and the 234 becomes about 195. Nothing needs to know which suspect was guilty.

**Why nothing is lost by moving it.** `android/.gitignore` ignores `/build` and `.gradle`, so everything Drive is fighting Gradle over is disposable output that git deliberately excludes — all of the cost of syncing, none of the benefit.

**Why the path is not hard-coded into a tracked file.** This repository is public. An absolute `C:\builds\hexboard` written into `android/app/build.gradle.kts` would break the build for anyone who clones it and would be wrong on any other machine. So the build file reads an optional property and falls back to Gradle's default when it is absent, and the machine-specific value lives in `android/local.properties`, which `android/.gitignore` already excludes and which Android projects already use for exactly this kind of per-machine setting.

**What the build changes.**
- `android/app/build.gradle.kts` — reads an optional `hexboard.buildDir` key from `android/local.properties`; where present, sets `layout.buildDirectory` to that path; where absent or unreadable, leaves Gradle's default alone so a fresh clone builds unchanged.
- `android/local.properties` — gains `hexboard.buildDir=C:/builds/hexboard`. Untracked, so it stays on this machine and reaches nobody who clones the repository.
- `README.md` — one sentence under the build instructions naming the optional key and what it is for, since a setting nobody can see in the repository is a setting nobody will find.

Reads but does not change: `android/.gitignore`, to confirm `/build` and `local.properties` are both excluded.

**The observation that shows it landed:** after a build, `android/app/build/` contains nothing — the output appears under `C:\builds\hexboard` instead — and the app still installs and runs on the Pixel 6. And with the `hexboard.buildDir` line removed from `local.properties`, a build writes to `android/app/build/` as before, so a fresh clone is unaffected. Gradle cannot run on this machine, so both halves are Android Studio's to check, on the Pixel 6.

**The experiment this doubles as, worth recording because it costs nothing extra.** If builds stop failing after this, Drive or Defender was the holder and the collision is gone. If they still fail, neither was, and the cause is something nobody has named yet. Either way the next build says something the previous evidence could not.

**Options already refused.** A walkthrough step telling you to pause Drive before every build — costs a step in every item that compiles, forever, and leaves the path ceiling untouched. Moving the whole project out of `My Drive` — changes where the repository lives and what backs it up, which is a bigger decision than a build obstacle warrants, and this makes it unnecessary rather than deferring it. Relocating the `.gradle` project cache as well — `--project-cache-dir` is documented as a command-line flag and the matching `gradle.properties` key was not confirmed, and builds here go through Android Studio's Run button rather than a terminal, so the certain half is worth taking alone. The session scratchpad or the local temp folder as the target — outside Drive, but long, so it would relieve the sync collision and leave the path hazard roughly where it is.

Rests on: `layout.buildDirectory` being how Gradle's build output location is set, read from Gradle's own directory-layout documentation on 2026-09-03; `--project-cache-dir` being a command-line flag with no confirmed property equivalent, read from Gradle's command-line reference the same day; the 234-character deepest path and the 57-character project root, measured on this machine on 2026-09-03; that `android/local.properties` is gitignored, read from `android/.gitignore` the same day; **that Drive or Defender was the holder, which is unproved** — the fix does not depend on knowing, per the paragraph above.

The path-ceiling half came from the Taskflow project, where it was the proven cause at 279 characters; here it is a hazard at 234 and not the cause. Recorded so the two are not confused later.

#### Board padded clear of the navigation bar, which currently takes taps meant for row 3 [board-clear-of-navigation-bar]
The input view is padded by the navigation-bar inset, so the bottom key row stops sharing a band with Android's back, home and recents buttons.

**Raised by you on 2026-09-03**, from the first time Hexboard drew as a real keyboard rather than a test screen: row 3 — shift, `?`, `,`, `!`, the two space bars, `'`, `"`, `.`, `-` — sits across the navigation bar, with the lower part of each circle behind it.

**Why it is worse than a cosmetic overlap.** The navigation bar takes touches in its own region, so a tap aimed at the lower half of a row 3 key reaches the system rather than the keyboard. The bottom row is partly unusable rather than partly hidden, and it is the space bars that are worst affected, because they sit in the middle where the three system buttons are.

**The cause, read from the code on 2026-09-03 rather than guessed.** There is no inset handling anywhere in the app: a grep across all five Kotlin files for window insets, navigation bars or safe-drawing padding returns nothing but an unrelated comment about the visible circle being smaller than its touch target. `HexboardImeService.onCreateInputView` builds a bare `ComposeView`, installs the three owners, and sets its content to `HexboardBoard` with `fillMaxWidth()` and a background colour and nothing else. Meanwhile `android/app/build.gradle.kts` sets `targetSdk = 36`, and apps targeting 35 and above are laid out edge-to-edge behind the system bars on Android 15 and above, with the opt-out gone by 36. So the board is handed the full height including the bar's band and fills it.

**What the research could not settle, recorded because the item should not read as more certain than it is.** Android's behaviour-changes page discusses Activities throughout and says nothing about `InputMethodService` input views in relation to edge-to-edge or insets, either way. So whether the enforcement formally reaches an IME window is undocumented as far as that search reached. What is not in doubt is the observation on the Pixel 6 and the absence of any inset handling to explain it away.

**Why the fix survives that gap, which is what let it be settled anyway.** `Modifier.navigationBarsPadding()` pads by the navigation-bar inset, and that inset is zero where there is no bar to avoid. If the IME window does extend behind the bar, the padding lifts the board clear; if it turns out not to, the padding is zero and no height is spent. The remedy is correct under either answer.

**Where the reserved space comes from, which the capture left open.** It is added rather than taken. An input method's window is sized to its view, so padding the view makes it taller and the window grows to fit: the keys keep their size and the keyboard occupies slightly more of the screen. That is the right direction for a keyboard whose argument is large keys, and it is the same mechanism [suggestion-strip] uses at the top of the same view — the two stack, and a board carrying both is taller by both amounts.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — the modifier chain on `HexboardBoard` inside `onCreateInputView` gains `navigationBarsPadding()` alongside the existing `fillMaxWidth()` and background. The background is applied so that it fills the padded region too, rather than leaving a bare strip where the bar sits.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/NavigationBarInsetTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/build.gradle.kts`, for the target SDK that makes this bite.

**The observation that shows it landed:** the instrumented test asserts that the bottom of the lowest key row's accessibility bounds sits at or above the top of the navigation-bar inset, and that where that inset is zero the board's height is unchanged from before — so the fix cannot silently spend height on a device with no bar. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6, and the row-3 keys should be tapped by hand as well, since the test reads bounds rather than where touches actually go.

**Options already refused.** Padding by the wider safe-drawing inset — it also covers gesture areas and display cutouts, spending height beyond the defect actually seen, on a keyboard arguing for large keys. Shrinking the board to fit the remaining space — takes the fix out of the keys, which is the one place this project will not take it from. Overriding `onComputeInsets` on the service — the framework route, not researched, and the thing to look at only if the padding proves insufficient.

Cites research: `workshop/resources/research/edge-to-edge-and-ime-input-views.md`.

Rests on: the absence of inset handling and the shape of `onCreateInputView`, both read from the Kotlin on 2026-09-03; `targetSdk = 36`, read from `android/app/build.gradle.kts` the same day; the edge-to-edge enforcement for apps targeting SDK 35 and above, read from Android's behaviour-changes page on 2026-09-03; **that the enforcement reaches an `InputMethodService` input view, which is undocumented and unverified as of 2026-09-03** — the fix does not depend on it, per the paragraph above, but the diagnosis does.

Interacts with [suggestion-strip], which adds reserved height at the top of the same view: both grow the keyboard's total height and neither depends on the other. That ordering is written on both entries.

#### Row above the keys, holding the keyboard's own controls [suggestion-strip]
The board gains a row above the keys. This item builds the row itself and puts nothing in it; three later features each add their own control to it.

**Split out of [in-keyboard-voice-input] on 2026-09-02, reversing a recommendation made earlier the same session.** When the strip was first designed, exactly one feature needed it, so a separate item was judged a hop with nothing in it and the voice item was to build it. Within the hour a second feature needed it — [persistent-clipboard], whose screen you settled is reached by a button at the strip's left end — and the two are held by different things: voice input waits on [recogniser-gap-comparison], your dictation test on the phone, while the clipboard waits on nothing. Leaving the container inside the voice item would have parked the clipboard behind a test that has nothing to do with clipboards. Claude's recommendation both times, and the premise that changed is the count of features needing it, not a change of mind about hops.

**What the row is.** Its drawn band is one row's vertical pitch — `KeyGeometry.verticalStep` of the same radius the board already solved for that panel — floored so it never falls below 48dp. Derived rather than fixed, so it stays in proportion on the eleven-wide Russian layout and on any screen width without a second rule, and so a control in it can never end up a different size from the keys beneath it. A fixed dp value was the alternative and lost on exactly that.

**It reserves more height than it draws.** Controls in this row may be drawn larger than a key and stand proud of the band — the microphone does. An input method's window is sized to its view and Compose clips to bounds, so nothing can be painted outside the keyboard's own rectangle; the row therefore reserves the overspill as real height and leaves it empty except where a control occupies it.

**What it costs.** One row's worth of height the keys no longer get, on a keyboard whose whole argument is larger keys. It is the standard place a phone keyboard puts this row, so the cost is the ordinary one, but it is paid against SPEC's geometry principle and should be looked at on the phone rather than accepted on paper.

**And it may ship empty for a while, which was accepted rather than overlooked.** This item builds the row and puts nothing in it; the three features that fill it are each held by something of their own, so if this is built first the keyboard carries a visibly empty band above the keys until one of them lands. That was weighed on 2026-09-02 and accepted: it is obviously unfinished rather than wrong, and the alternative — holding the container until a control exists for it — is what putting the row inside [in-keyboard-voice-input] would have done, which is the arrangement this item was split out of.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `HexboardBoard` draws the row above the pager and adds its reserved height to the board's own. The row is empty: no control, no divider beyond what makes it read as a band.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/StripLayoutTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt`, for `verticalStep` and the solved radius.

**The observation that shows it landed:** the instrumented test asserts the board's total height exceeds the tallest panel's by the row's reserved height, that the row's drawn band equals one vertical step for the panel on screen and is never under 48dp, and that the top key row still begins below the band rather than under it. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

**Options already refused.** Building the row inside [in-keyboard-voice-input] — parks the clipboard behind a dictation test. Giving each feature its own row — three bands above the keys, which the geometry cannot afford. A fixed dp height — drifts out of proportion on any board that is not ten wide.

**Cleared to run on 2026-09-03.** It was held against [install-and-enable-on-pixel] because it edits `KeyboardPanel.kt`, which four builds of 2026-09-02 changed and none had compiled. That run has now happened: the file compiled, the board drew and taps landed on the keys aimed at, so the unverified-foundation reason has gone.

Rests on: the row's height derivation and the clipping behaviour, reasoned from `KeyGeometry.kt` and `KeyboardPanel.kt` as they stand on 2026-09-02, not run; Android's 48dp minimum touch target, read from Google's own accessibility guidance on 2026-09-02.

[in-keyboard-voice-input] adds the microphone at this row's right end, [persistent-clipboard] a button at its left end, and [uniform-neighbours-predictive] fills the middle with candidates. Those orderings are written on all four items.

Interacts with [board-clear-of-navigation-bar], which pads the same view at the bottom by the navigation-bar inset. Both grow the keyboard's total height by the same mechanism — an input method's window is sized to its view — so a board carrying both is taller by both amounts, and whichever ships second should be looked at on the phone for the combined height rather than its own. Neither depends on the other. That ordering is written on both entries.

#### Every layout config must reach the app's assets, not only the English one [ship-all-layout-configs]
The Gradle task that copies the key config into the app ships one hard-coded file, so every layout but the English one is invisible to the app. This makes it copy them all.

**Kept on 2026-09-02, after reading the build file rather than trusting the capture.** `CopyKeyLayoutConfig` takes a single `RegularFileProperty` pointed at `resources/key-layout.json` and writes one hard-coded name into the generated assets directory. The output side needs nothing — [assets-srcdir-deprecation] made it a `DirectoryProperty` that same morning.

**The copy is filtered rather than wholesale, settled with you the same day.** `resources/` also holds `key-manifest.md`, `key-manifest-ru.md` and an `images/` folder. Copying the folder would put files in the APK that no code reads, and the manifest in particular is generated *from* the config for people to read — the app carrying it would be carrying a second copy of what it already parses. So the task takes `key-layout*.json` and leaves the rest. Claude's recommendation, your agreement.

**What the build changes.**
- `android/app/build.gradle.kts` — `CopyKeyLayoutConfig`'s input becomes a directory rather than a single file, and its action copies every `key-layout*.json` found there into the generated assets directory under its own name. The task description and the file's opening comment, both of which name the single file today, are reworded to match.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/ShippedConfigsTest.kt` — new, carrying the observation below.

Reads but does not change: `resources/`, for what is there to copy.

**The observation that shows it landed:** the instrumented test lists the app's own assets and asserts that `key-layout.json` and `key-layout-ru.json` are both present, that every asset matching `key-layout*.json` parses as a layout with at least one panel, and that no `key-manifest` file and no image has been shipped.

**Options already refused.** Copying the whole `resources/` folder — ships manifests and images the app never reads. Adding the Russian config as a second hard-coded name — leaves the same defect for the next language.

**Cleared to run on 2026-09-03.** It was held against [install-and-enable-on-pixel] because [assets-srcdir-deprecation] rewired this exact task on 2026-09-02 with no Gradle sync since. That sync has now run — the app built and installed — so the rewiring is proven and a second change to the file no longer stacks on an unverified one.

Rests on: the task's current shape, read from `android/app/build.gradle.kts` on 2026-09-02; that the generated assets directory is flat, so filenames alone distinguish the configs — read from the same file, not run.

[layout-switching] is held against this item: a picker has nothing to enumerate until the configs are in the app. That ordering is written on both items.

Filed by /rescan on 2026-09-02 from the run that built [language-starter-layouts]. The Gradle copy task ships `resources/key-layout.json` alone, so `key-layout-ru.json` exists in the repository and not in the app. A layout picker has nothing to list until every config is copied and the app can enumerate them at runtime, which is exactly the open question [layout-switching] records. This belongs with that item and should be built with or before it; that ordering is written here and should be written there when it is processed. The copy task now takes a `DirectoryProperty` output, so copying a folder of configs rather than one file is a small change to `android/app/build.gradle.kts`.

#### Keys drawn with a soft edge instead of a hard disc and a border [soft-key-edge]
Each key is drawn as a radial fade — solid through the middle, transparent at the edge of its touch target — with the 1.5dp border removed.

**Raised by you on 2026-09-03, from using the keyboard rather than looking at it.** Your account: the circles appear a little small still, but enlarging them would make the board look too crowded; the problem is *perceived* key size, which lowers press confidence and so slows typing; and a soft edge might make it as clear as possible that there is no hard circular boundary, just a soft circular target.

**The measurement behind that, taken the same day.** `KeyGeometry.VISIBLE_INSET = 3f` shaves 3dp off every key's radius before it is drawn. Solving `solveRadius` for the Pixel 6's 411dp width gives a touch radius of about 22dp, so the drawn circle is about 19dp — roughly 86% of the radius and **about 74% of the area**. A quarter of every key is deliberately invisible, which is what you are seeing.

**And your crowding prediction is arithmetic rather than a hunch.** Neighbouring centres sit about 46dp apart. Drawn at 19dp radius they leave an 8dp gap; take the inset to zero and the gap falls to about 2dp — visibly nearly touching. So simply drawing them bigger really does trade one complaint for the other.

**A second thing the code showed, which strengthens the case.** `KeyboardPanel` draws each key with a 1.5dp border in a *lighter* colour than the fill. So the boundary today is not merely visible, it is emphasised twice — once by the fill stopping and once by a brighter ring drawn exactly where it stops. Any soft-edge treatment has to remove the border, or the border reinstates the hard edge the fade exists to dissolve.

**Why this serves the perceptual wedge rather than threatening it, which SPEC requires be questioned rather than waved through.** The wedge is a claim about *corners*: people aim more confidently at circular targets than at shapes with visible corners. A radial fade has no corners, so the claim is untouched. Beyond that, a radial gradient is a centre-emphasising cue by construction, and aiming centrally is precisely the behaviour the wedge predicts and wants — so the expectation is that it helps. The opposite risk is real and is recorded rather than dismissed: fade too far and there is nothing definite to aim at, which would cost confidence instead of building it. That is what the falloff values below are for, and why they are yours to adjust on the phone.

**A defect this exposes, worth fixing in the same change.** `VISIBLE_INSET` is an absolute 3dp where every other quantity in `KeyGeometry` — the gap, the vertical step, the label size — is derived from the radius. So the give-away is a larger share of a smaller key: worse on the eleven-wide Russian layout, and at `MIN_RADIUS = 12` the drawn circle would be down to about 56% of the touch area. Expressing the edge as fractions of the radius removes that by construction, which is a reason to prefer fractions over a second dp constant.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — `VISIBLE_INSET` is replaced by two fractions of the radius: where the fill is still fully solid, and where it has faded to nothing. `visibleRadius` goes with it, and the label sizing that used it takes the solid fraction instead, so glyphs stay inside the solid part rather than sitting on the fade.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — a key is drawn as a radial gradient from its fill colour at the centre to fully transparent at the outer fraction, in place of the clipped circle plus `border`. The border is removed. The press glow keeps working as it does today: `lerp(fill, lit, glow)` now supplies the gradient's colour rather than a flat fill, so a pressed key lightens across its whole extent.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyEdgeTest.kt` — new, carrying the observation below.

Reads but does not change: `resources/key-layout.json`, since this changes nothing about the key inventory.

**The two values are tunable constants, not decisions, and this is deliberate.** Settled with you on 2026-09-03: whether the edge is soft at all is the decision, and it is made here; how steep the fade is is one curve inside otherwise fully described work. Start at **solid to 55% of the radius, transparent at 100%** — the conservative option, which lights more area than today's 19dp disc while putting nothing new into the gaps, so it cannot read as more crowded than the current board. A more generous variant — solid to 45%, transparent at about 118% of the radius, so neighbouring fades just meet — is the other end worth trying. Both are one constant each and are yours to move once you have seen them on the phone. Your standing instruction, given the same day: small visual adjustments of this kind are made during the build rather than pinned in advance.

**The observation that shows it landed:** the unit test asserts that the solid fraction is strictly less than the outer fraction, that the outer fraction is at least 1.0 so the drawn key reaches its whole touch target, that the label fits inside the solid fraction, and that no border width is drawn. Whether it *looks* better is yours, on the phone — the test protects the relationships, not the taste.

**Options already refused.** Reducing `VISIBLE_INSET` toward zero — your own objection, confirmed by the arithmetic: it leaves a 2dp gap between hard edges and looks crowded. Keeping the border and softening only the fill — the brighter ring is the hardest edge on the key, so this changes least where it matters most. A second absolute dp constant for the fade — reproduces the scaling defect that made the inset worse on narrow keys. Deciding the falloff here from a rendered comparison — a comparison page was built in the session scratchpad and could not be got in front of you, and on your instruction the values ride into the build as tunables instead.

Rests on: `VISIBLE_INSET = 3f`, the `solveRadius` solver, `gap`, `verticalStep`, `LABEL_FRACTION` and the 1.5dp border, all read from `KeyGeometry.kt` and `KeyboardPanel.kt` on 2026-09-03; the 22dp touch radius and 74% area figures, computed from that solver for a 411dp width the same day, not measured on the device; that a soft edge raises perceived size and press confidence, which is **unverified** — it is the reason the feature exists, and only using it on the phone tests it.

Placed immediately before [row-tint], which chose its one-fifth tint against a flat disc: the tint is a resting *fill*, and once the fill fades at the edge the same fraction may read differently, so it should be judged after this rather than before. Both edit `KeyboardPanel.kt`. That ordering is written on both entries. [key-press-feedback] shipped on 2026-09-02 and makes the pressed key's lightening the only press feedback there is; this preserves it by lerping the gradient's colour rather than a flat fill.

#### Rows tinted alternately, so the zigzag still reads as QWERTY rows [row-tint]
Every key in a row shares a resting fill, and the fills alternate down the board, so the rows are visible as rows.

**Raised by you on 2026-09-02, and the reason is the wedge itself.** Your point: the rows are still there, they are only zigzagging, and people read left to right — so the row is the unit that carries recognition, and QWERTYUIOP is a string almost everyone knows on sight. Highlighting rows says *this is the keyboard you already use*; highlighting columns would say nothing, because nobody reads a keyboard downwards. SPEC's familiarity principle is what this defends: the zigzag is the one thing that makes Hexboard look unfamiliar, and this is what tells a new user it is not.

**Tinting the keys, not banding behind them, settled by you the same day.** A band drawn behind a zigzagging row is not a stripe but a wave, and it would be the first non-circular shape on the board — rejected on that ground. Tinting the circles themselves adds no shape at all. Your words for how far to take it: nothing crazy, just enough to notice.

**How far, derived rather than picked.** `KeyboardPanel` already draws each key as `lerp(fill, lit, glow)` — resting at one colour, fully lit at another when pressed. Even rows rest at the existing fill; odd rows rest one-fifth of the way along that same line. So the tint introduces no new colour, cannot clash with the theme, and its distance from a pressed key is measurable rather than a matter of taste: a press travels the whole way, so a pressed key stays four-fifths brighter than its unpressed neighbours even in a tinted row. That is what keeps this clear of [key-press-feedback], which shipped on 2026-09-02 and makes the pressed key's lightening the only press feedback there is.

The one-fifth is Claude's proposal and is not derived from anything — it is the smallest step expected to read as deliberate rather than as a rendering artefact. It is one constant, so it is adjustable once it has been seen on the phone, and seeing it is the point at which to judge it.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — gains the tint fraction as a named constant and a pure `rowTint(row: Int): Float` returning 0 for even rows and that fraction for odd ones. It sits with the other appearance constants already there: the visible inset, the label fractions, the uppercase scale. **And `nearestCentre`'s comment gains why full coverage matters, not only that it holds**: a tap that lands nowhere is a deletion, and the neighbour-weighted correction in [uniform-neighbours-predictive] can only fix substitutions, so dead space between circles would produce the one failure that engine cannot repair. Added here rather than as an item of its own, because this is the work already going to this file — the reasoning itself lives in [uniform-neighbours-predictive], where a reader proposing kissing circles would land.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — each key's resting fill becomes `lerp(fill, lit, rowTint(row))` instead of `fill`, with the press glow still lerping the whole way to `lit` from wherever the key rests.
- `android/app/src/test/java/tech/flintcraft/hexboard/RowTintTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** the unit test asserts that `rowTint` returns zero for even rows and the same non-zero fraction for odd ones, that the fraction is one-fifth, and that it is strictly less than the full press travel — so a later change that let the tint approach the press colour fails rather than merely looking wrong.

**Options already refused.** A band drawn behind each row — introduces a wave-shaped non-circular element. Tinting by column — nobody reads a keyboard downwards, so it carries no recognition. A stepped tint, one shade per row rather than two alternating — the difference accumulates down the board, so the bottom row would sit far from the top instead of reading as banding. A hand-picked shade — clashes with the theme and makes the distance from the press colour a matter of taste.

**Cleared to run on 2026-09-03**, when the install run compiled `KeyboardPanel.kt` and showed it drawing on the Pixel 6. It had been held because that file took four changes on 2026-09-02 and none had compiled.

[soft-key-edge] is placed immediately before this one and should be judged first. The one-fifth above was chosen against a flat disc with a hard border; once a key's fill fades out at its edge, the same fraction covers a different amount of visible area and may read differently, so the value is worth looking at again after that lands rather than before. Both edit `KeyboardPanel.kt`. That ordering is written on both entries.

Rests on: the fill/lit lerp being how a key's colour is produced today, read from `KeyboardPanel.kt` on 2026-09-02, not run; that one-fifth is visible on a real screen in a dark theme, which is unverified and is what the install run first shows.

#### On an eleven-wide layout, the other two panels draw bigger keys than QWERTY [panel-key-size-consistency]
All three letter panels of a layout draw at one key size, the largest that fits the widest of them, with narrower panels centred.

**Settled with you on 2026-09-02.** Today each panel solves its own radius from its own widest row, so a layout whose panels differ in width resizes every key by about ten per cent on each swipe and moves every centre. Nothing is broken by that — taps still route, nodes still land, accent rows still draw — it is a visual and motor change rather than a fault.

**Two factual corrections, made on 2026-09-03 after every panel in both layouts was measured key by key.** This item originally said the mismatch "appears only on layouts whose panels differ in width, which today means Russian alone", and that on Russian "QWERTY divides the width by eleven while RARE and SYMBOLS divide it by ten". Both are wrong, and the second is what made the first look safe. RARE is eleven columns wide on *both* layouts — its rows 0 and 1 sit in columns 0-9 while row 2 is indented into columns 1-10 — so the English layout has had the mismatch all along, with RARE's keys about nine per cent smaller than QWERTY's on the config that ships. On Russian it is SYMBOLS that is the narrow panel at ten columns, QWERTY and RARE both being eleven. The claims were reasoned from the Russian layout's arithmetic on 2026-09-02 without the English config being read against them, and the error surfaced only when the board was first seen running on the Pixel 6.

**What that changes here, and what it does not.** The fix is unchanged — one radius solved from the widest panel across the layout, narrower panels centred — and so is everything below. What changes is the scope: [rare-row2-unindent] removes the English instance by moving RARE's row 2 back to columns 0-9, so by the time this is built the mismatch may be Russian-only after all, which is what this item wrongly assumed on 2026-09-02. It is still needed either way, because Russian's own QWERTY genuinely is eleven columns against SYMBOLS' ten. That ordering is written on both entries; either may be built first and neither depends on the other.

**The argument that decided it came from the code's own existing choice.** `HexboardBoard` already computes the board's height as the tallest panel's and uses it for all three, so a swipe never resizes the board vertically. Sizing the radius per panel answers the same question the other way. Making them consistent is what this item does.

**The trade, stated in the terms it was decided on.** Per-panel sizing costs a ten per cent resize on every swipe; shared sizing costs RARE and SYMBOLS ten per cent of their key size permanently, on a keyboard whose headline is large keys. You chose shared.

**A clarification worth keeping, because the same confusion will recur.** This shares a radius only between the three panels *within one layout*. Nothing is shared across languages: SPEC already says a wider alphabet gets correspondingly smaller keys, so Russian's keys are smaller than English's whatever happens here. Your first answer was given on the reading that this was a cross-language rule, and reversed once the scope was clear — recorded so a later reader does not take the reversal for indecision. Claude also described per-panel sizing as making the board "unstable" in the exchange before that, which overstated it; the honest description is the resize above, and it is what the decision was finally made on.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `HexboardBoard` solves the radius once, from the widest panel across the whole layout, and passes it to each panel; `KeyboardPanel` takes the radius as a parameter instead of solving its own, and offsets its keys horizontally to centre where its own board width is narrower than the widest panel's.
- `android/app/src/test/java/tech/flintcraft/hexboard/SharedRadiusTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt`, for `solveRadius` and `boardWidth`.

**The observation that shows it landed:** the unit test asserts that for a layout whose panels differ in width, the radius handed to every panel equals what the widest panel alone would have solved, and that a narrower panel's leftmost key centre sits further right than the widest panel's by half the difference in board width — a centred panel rather than a left-aligned one.

**Options already refused.** Each panel sizing its own — the current behaviour, which resizes the board on every swipe and contradicts the height decision already made. Spreading a narrower panel's keys to fill the width instead of centring — breaks the hexagonal packing, which SPEC calls inviolable.

SPEC gained the sentence on 2026-09-02, in the same planning session.

**Cleared to run on 2026-09-03**, when the install run compiled `KeyboardPanel.kt` and showed it drawing on the Pixel 6. It had been held because that file took four changes on 2026-09-02 and none had compiled.

Rests on: the per-panel radius solve and the shared height, both read from `KeyboardPanel.kt` on 2026-09-02, not run; the panel widths of both layouts, measured key by key from `resources/key-layout.json` and `resources/key-layout-ru.json` on 2026-09-03 — which is the read that produced the two corrections above, and which the 2026-09-02 version of this item never did.

Filed by /rescan on 2026-09-02 from the run that built [language-starter-layouts]. `KeyboardPanel` sizes its keys from its own panel's widest row, so a panel wider than its siblings gets correspondingly smaller circles and swiping between them changes key size. Whether all three panels should share the radius of the widest (so the narrower panels sit centred with margins), or the difference is acceptable, was the design question, and it was settled the same day. Geometry lives in Kotlin, so the fix is in `KeyboardPanel.kt` or `HexboardBoard`, not in any config — which is what distinguishes it from [rare-row2-unindent], where the panel widths themselves are the config's business.

#### RARE's row 2 moves to columns 0-9, making the English layout ten columns wide [rare-row2-unindent]
The English RARE panel's third row is shifted one column right, which makes the whole panel eleven columns wide. This moves it back.

**Settled with you on 2026-09-03, from measuring every panel in both layouts.** English RARE runs rows 0 and 1 in columns 0-9 and row 2 in columns 1-10 — ten keys either way, the last row simply indented. That indent is what makes the panel eleven wide, so `KeyboardPanel` solves its radius by dividing the width by eleven while QWERTY and SYMBOLS divide by ten. RARE's keys are therefore already about nine per cent smaller than QWERTY's on the layout that ships, and every swipe between them resizes the keys. Nobody had noticed because nothing had drawn the board as a real keyboard until 2026-09-03.

**Why the indent is not worth keeping.** It comes from `hexboard17.html`, whose own comment on that row reads `cols 1-10 (offset +1; col 10 visible on half-snap)`. The prototype's board could half-snap sideways, so a key hanging past the right edge was still reachable. Hexboard's board does not do that and nothing plans to. The eleventh column is an artefact of a mechanism this project did not reproduce, and it currently costs the whole board nine per cent of its key size.

**What this is not.** Not a change to the key inventory: all ten characters stay on RARE's row 2, in the same order, and nothing moves between panels. SPEC keeps geometry in code and the key inventory in config, and a row's horizontal offset is the config's — which is why this is a config edit rather than Kotlin. SPEC says nothing about RARE's offset, so no sentence there goes wrong.

**What the build changes.**
- `resources/key-layout.json` — the ten keys of the RARE panel's row 2 have their `col` reduced by one, from 1-10 to 0-9. Nothing else in the file changes.
- `resources/key-manifest.md` — regenerated from the edited config by `scripts/generate-key-manifest.py`, never hand-edited, per SPEC's rule that each config generates its own manifest.

Reads but does not change: `hexboard17.html`, for the half-snap comment that explains the offset; `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt`, for how a panel's width is derived from its widest row.

**The observation that shows it landed:** every panel in `resources/key-layout.json` has a widest row of ten columns, so the three English panels all report the same width; RARE's row 2 still carries the same ten characters in the same order; and the regenerated manifest matches the config with no drift.

**Options already refused.** Leaving it and letting [panel-key-size-consistency] shrink QWERTY and SYMBOLS to match — makes the default panel of the default layout about nine per cent smaller, on a keyboard whose headline is large keys, to accommodate one indented row of maths symbols. Dropping a character from row 2 to fit ten columns — SPEC's manifest rules make losing a key a deliberate act, and nothing needs losing when the row already holds exactly ten. Keeping the offset for its own sake — it exists for a half-snap this project does not have.

[panel-key-size-consistency] is unaffected as work and still needed: the Russian layout's QWERTY genuinely is eleven columns wide against SYMBOLS' ten, so a layout whose panels differ in width still exists and still wants one shared radius. What changes is that after this item, English is no longer such a layout. That ordering is written on both entries — either may be built first, and neither depends on the other.

Rests on: the column assignments of every panel in `resources/key-layout.json` and `resources/key-layout-ru.json`, measured key by key on 2026-09-03; the prototype's half-snap comment on that row, read from `hexboard17.html` the same day; that the offset is not load-bearing for the zag — the zag rule keys on column parity, so shifting a whole row by one inverts which of its keys sit low, which is a visible change nobody has seen on the phone and is the one thing to look at when it lands.

Filed on 2026-09-03, split out of the capture reporting that English already had the panel key-size mismatch.

#### Two support libraries the service uses are not declared directly [declare-savedstate-viewmodel-deps]
The input method service imports two AndroidX libraries the project never declares. This declares them.

**Settled with you on 2026-09-02: declare them outright rather than waiting to see whether the build complains.** As filed, this item was conditional — add them if the Android Studio run fails on unresolved references, delete this if it succeeds. That was rejected for two reasons.

Relying on a transitive dependency for something the source `import`s directly is a latent break rather than a working arrangement. It holds only while Compose UI and Activity keep exposing those artifacts as API rather than implementation, which is their decision and not this project's; a routine version bump can withdraw it, and the failure then arrives at a moment unrelated to anything changed here. Declaring what you import is the ordinary discipline, and it costs nothing in the packaged app because the artifacts ship either way.

And it takes a conditional out of the queue. As written, the item had to be re-examined after the install run whichever way that run went — either to do the work or to delete itself. Declared outright, the build's outcome stops being something this item waits on.

**The coordinates, settled on 2026-09-03 rather than left to the build.** As filed, this item said the exact artifact coordinates were "a lookup the build does first". Reading the catalogue showed that half of that needed no lookup at all, and the other half needed one narrow read:

- **`androidx.lifecycle:lifecycle-viewmodel` rides the version already there.** The catalogue carries `androidx-lifecycle-runtime-ktx` at `lifecycleRuntimeKtx = "2.11.0"`; same group, same release train, so the new entry takes `version.ref = "lifecycleRuntimeKtx"` and no new version is added.
- **`androidx.savedstate:savedstate` needs a version of its own**, since nothing in the catalogue covers it and the Compose bill of materials reaches only `androidx.compose` artifacts. The current stable release is **1.5.0**, published 2026-05-19, read from Android's own savedstate release page on 2026-09-03. So a `savedstate = "1.5.0"` version entry is added alongside the others.

The base `savedstate` artifact is the one wanted rather than `savedstate-ktx`: the service imports `SavedStateRegistry`, `SavedStateRegistryController`, `SavedStateRegistryOwner` and `setViewTreeSavedStateRegistryOwner`, which the base artifact carries.

**What the build changes.**
- `android/gradle/libs.versions.toml` — a `savedstate = "1.5.0"` line under `[versions]`; under `[libraries]`, `androidx-savedstate = { group = "androidx.savedstate", name = "savedstate", version.ref = "savedstate" }` and `androidx-lifecycle-viewmodel = { group = "androidx.lifecycle", name = "lifecycle-viewmodel", version.ref = "lifecycleRuntimeKtx" }`.
- `android/app/build.gradle.kts` — `implementation(libs.androidx.savedstate)` and `implementation(libs.androidx.lifecycle.viewmodel)` added to the dependency block, beside the existing `implementation(libs.androidx.lifecycle.runtime.ktx)`.

**The observation that shows it landed:** `HexboardImeService.kt`'s imports of `androidx.savedstate` and `androidx.lifecycle.ViewModelStore` classes each resolve to an artifact the catalogue names, so a grep of the catalogue finds both, and the app's dependency block lists both.

**Options already refused.** Leaving it conditional on the build's outcome — leaves a queue item whose whole content is "find out", and leaves a real import undeclared meanwhile. Leaving the coordinates as a lookup for the build — the paragraph doing that was removed on 2026-09-03; half of it needed no lookup at all, being readable from the catalogue in the repository, and the other half was one version read that cost a minute with the user present. `savedstate-ktx` in place of the base artifact — the four imported classes live in the base one.

Rests on: the catalogue's existing `lifecycleRuntimeKtx = "2.11.0"` entry and the app's dependency block, both read from this repository on 2026-09-03; `androidx.savedstate:savedstate` being at 1.5.0, published 2026-05-19, read from Android's savedstate release page on 2026-09-03 — a version moves, so a build finding 1.5.0 superseded should take the current stable release and say so rather than treating this line as binding; that the Compose bill of materials covers only `androidx.compose` artifacts and so cannot supply savedstate, reasoned from the catalogue's own entries the same day.

**Cleared to run on 2026-09-03**, when the install run performed the Gradle sync that [assets-srcdir-deprecation]'s change to the app's Gradle file had been waiting for. One thing the run also settled: the build succeeded with these two artifacts arriving transitively, so this is the latent break described above rather than a compile failure to repair.

Filed by /rescan on 2026-09-02 from the run that built [first-installable-build]. `HexboardImeService.kt` imports `androidx.savedstate` and `androidx.lifecycle.ViewModelStore` classes, which are not in `android/gradle/libs.versions.toml`; the build relies on them arriving through Compose UI and Activity, which declare them as API dependencies. If the next Android Studio build fails on unresolved references in the service, the fix is to add both to the version catalogue and the app's dependencies. If it builds, this is nothing and can be deleted at the next planning session. Nothing to do until that build runs.

#### Shift behaviour: one-shot today, caps lock and double-tap undesigned [shift-behaviour]
What the shift key does, which nothing had ever decided until 2026-09-02.

**Where the unshifted letter comes from, settled with you on 2026-09-03 — and cleared to run in the same move.** Seeing the keyboard run on the Pixel 6 on 2026-09-03 showed every letter drawn as a capital, and raised a question this item had been assuming an answer to: `resources/key-layout.json` gives each key one `label`, already a capital, so on the face of it there is no lowercase form for a shift state to switch to. Reading the prototype settled it. `hexboard17.html` redraws each label uppercase when shift or caps is on and lowercase when neither is (line 429), and does the same for accent alternatives (line 639) — so letters resting in lowercase is a decision the canonical reference already made and nobody had written down. And no config change is needed to honour it: across all 27 English letter keys and all 31 Russian ones, `output` is exactly the lowercase of `label`, checked key by key rather than sampled. The board therefore draws `output` at rest and `label` while a shift state is on. No layout file changes, and no new field is added to the schema.

The alternative was capitals always, with the shift state signalled by the shift key's own lighting alone — which is what the code does today and what several phone keyboards do. It lost because the prototype had already chosen otherwise and SPEC names that prototype as canonical, so keeping today's behaviour would have been a silent departure from the reference rather than a decision.

SPEC gained the matching sentence on 2026-09-03: it already said letters draw as capitals while a shift state is on, and said nothing about the resting state, which is how the gap survived. It now says letters rest in lowercase.

**Hexboard keeps the prototype's double-tap caps lock, settled by you on 2026-09-02.** `hexboard17.html` runs a three-state cycle on the shift key: one tap gives shift, a second tap inside the double-tap window gives caps lock, and a further tap clears both. That is what Hexboard does.

**The double-tap window is read from the phone rather than hard-coded.** The prototype uses a literal 320 ms. SPEC's rule that every hold and repeat follows the phone's own settings does not literally reach a double tap, which is not a hold, but the same instinct does, and this project has twice preferred the system's value to a number of its own. Android's `ViewConfiguration.getDoubleTapTimeout()` is believed to be that value at 300 ms; it is written here as a value to confirm at the start of the build rather than as a checked fact, because nobody has read it.

**The shift state is visible, settled by you on 2026-09-02.** The shift key lights while either state is on, with shift and caps lit differently as the prototype does, and every letter label — including the alternatives in an accent row — draws in uppercase while either is on. Today none of that happens: `KeyboardPanel` is never told about the state at all, so pressing shift changes nothing on screen. That is a defect rather than an unfinished feature, and it would have been true even had caps lock been refused.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — the single `shifted` flag becomes three states, off / shift / caps, cycled by the shift action on the rule above and cleared on the first inserted character in the shift state only. The state is passed down to the board.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — takes that state, lights the shift key by it, and draws every letter label and accent alternative from the key's `label` while a state is on and from its `output` while neither is.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/ShiftStateUiTest.kt` — new, carrying the observation below.

Reads but does not change: `hexboard17.html`, for the cycle it defines and its label switching; `resources/key-layout.json`, for the `label` and `output` pair each letter key already carries.

**The observation that shows it landed:** the instrumented test asserts that the letter labels read lowercase with neither state on; that one tap on shift redraws them in uppercase and marks the shift key's node as active; that a second tap inside the double-tap window reaches the caps state and a third clears both; that in the shift state one inserted character arrives capitalised and the labels revert immediately after; and that in the caps state they do not revert.

**Options already refused.** Keeping one-shot shift alone — leaves the prototype's caps lock unreplaced with nothing chosen in its place. The prototype's hard-coded 320 ms — a number of our own where the system publishes one. Capitals always, with only the shift key lit — a silent departure from the prototype SPEC names as canonical. A second config field for the unshifted form — unnecessary, `output` already being exactly that for every letter key in both shipped layouts.

Cleared to run on 2026-09-03: the install hold was spent by that day's run, and the labels question that briefly replaced it was settled in the same session.

Rests on: the prototype's three-state cycle, its 320 ms window and its label switching, read from `hexboard17.html` on 2026-09-02 and 2026-09-03; `output` being the lowercase of `label` for every letter key, read from `resources/key-layout.json` and `resources/key-layout-ru.json` on 2026-09-03 — which a further layout could break, so the build should fall back to lowercasing the label rather than assuming it; `ViewConfiguration.getDoubleTapTimeout()` existing and being the right system value, believed but unread.

Filed by /rescan on 2026-09-02 from the run that built [first-installable-build]. That item left shift to the service, and the build chose the simplest thing that gives capitals at all: tapping shift uppercases the next inserted character, then clears. Nothing in SPEC or the queue says what shift does. The prototype `hexboard17.html` has a double-tap-for-caps-lock (its `caps` state with a 320 ms double-tap window), and whether Hexboard keeps that, and whether a shifted state should be visible on the key, are the questions. The timing rule in SPEC — every hold and repeat follows the phone's settings — may reach a double-tap window too. Ordinary planning work; nothing here is waiting on you.

#### Emoji panels, which SPEC promises and no Kotlin implements [emoji-panels]
Five emoji panels reached by swiping down from the letters, filled from Unicode's own published emoji list.

**Found by /rescan on 2026-09-02, mid-session.** A grep of the Kotlin during the clipboard discussion found no vertical swipe and no emoji panel anywhere: `KeyboardPanel.kt` has a horizontal pager over the three letter panels and no vertical dimension at all. SPEC has promised five emoji panels reached by a downward swipe throughout, and no queue item has ever covered them — the "dies in SPEC" case the seeding rule exists for. Your steer on where it sits: it is needed, and the earlier it is taken up the longer there is to resolve it.

**The question at its centre, answered the same day.** SPEC said the emoji panels were "system-supplied content", and nobody had established what that could mean on Android. `androidx.emoji2:emoji2-emojipicker` supplies an up-to-date list, skin-tone variants and recents — but only through `EmojiPickerView`, a vertical scrolling grid with a category header, and its catalogue is internal: the library's declared public API is the view, an item class and the recent-emoji providers, and nothing exposes the list as data. So the platform's content arrives with the platform's user interface or not at all, and SPEC's five-panel promise and its system-supplied promise could not both stand.

**The road chosen, settled by you on 2026-09-02: bundle Unicode's own list and keep Hexboard's panels.** `emoji-test.txt`, published per emoji version under `unicode.org/Public/emoji/`, lists every emoji in CLDR display order — the order keyboards use — grouped into groups and subgroups, and its own header describes it as data for keyboards. That gives the content without the interface. It is the same division SPEC already applies to layouts, which are transcribed from open data rather than invented here, and SPEC's wording was sharpened to say so in the same session.

What it costs, accepted knowingly: skin-tone variants and recently-used tracking come free from the Jetpack picker and become work here instead, and somebody has to refresh the list when Unicode moves. The rendering guarantee is separable — `emoji2` can be used for rendering on older Android versions without taking the picker.

**The alternative and why it lost.** Adopting `EmojiPickerView` is less work and gets skin tones and recents for nothing, but it replaces Hexboard's emoji surface with Google's, and SPEC's five-panel sentence would have had to be rewritten to promise the platform's picker. Rejected as less work and less Hexboard. A hand-written list like the prototype's 250 characters was the third option, rejected for contradicting SPEC's own wording and going stale with every Unicode release.

**The gesture collision to expect.** [panel-switch-gestures] shipped a `HorizontalPager` on 2026-09-02. The prototype's arrangement is a vertical track containing the horizontal one, so a `VerticalPager` wraps it — and the pointer-consumption trap that item had to solve, where a pager consuming the pointer-down kills the key press, arrives again on the second axis. The remedy recorded in `workshop/resources/research/compose-pager-vs-board-tap-gesture.md` was worked out for the horizontal case and is the starting point rather than a known answer.

**What the build changes.**
- `resources/emoji-test.txt` — new. Unicode's published list, bundled as data, with its version recorded in the file's own header as published.
- `android/app/build.gradle.kts` — the assets copy takes the emoji data alongside the layout configs.
- `android/app/src/main/java/tech/flintcraft/hexboard/EmojiCatalogue.kt` — new. Parses the bundled file into groups and their emoji, ignoring the non-fully-qualified entries, and slices the result into five panels in the file's own order.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `HexboardBoard` gains a `VerticalPager` with the letter board above and the emoji panels below, the horizontal pager continuing to run inside each; an emoji tap commits its characters like a key.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/EmojiPanelsUiTest.kt` — new, carrying the observation below.

Reads but does not change: `hexboard17.html`, for the panel arrangement and which emoji sit in the centre panel.

**The observation that shows it landed:** the instrumented test asserts that a downward swipe from the letter board reaches an emoji panel and an upward swipe returns; that five emoji panels exist and a horizontal swipe moves between them; that tapping an emoji commits its characters into the field; and that a key press on the letter board still commits after both pagers are in place — the pointer-consumption regression, checked rather than assumed.

**What is deliberately not in this item.** Skin-tone variants and recently-used tracking, both of which the Jetpack picker would have supplied and which are their own work. Whether the row above the keys stays visible on the emoji panels is undesigned and belongs with whichever of the two ships second.

**Cleared to run on 2026-09-03**, when the install run compiled `KeyboardPanel.kt` and its horizontal pager was seen working on the Pixel 6 — so the second pager this item adds now goes onto a proven surface rather than an untested one.

Cites research: `workshop/resources/research/android-emoji-sources.md`.

Rests on: the Jetpack picker's public API surface, read from the androidx repository on 2026-09-02, so a later release exposing the list as data would reopen the choice; `emoji-test.txt`'s existence, its CLDR ordering and its group structure, read from Unicode's own publication the same day; **the Unicode Terms of Use, which were NOT read** — this repository is public, so the licence is confirmed before the file is bundled, exactly as the Leipzig licence is flagged in `word-list-licence-and-frequency.md`.

#### [user] Compare dictation through Gboard and through the on-device recogniser [recogniser-gap-comparison]
**Cleared to run on 2026-09-03.** The hold was repointed onto the install on 2026-09-02, because step 3 below needs the dictation screen actually on the phone and [ondevice-recogniser-test] had never been compiled. The install run of 2026-09-03 compiled and installed the app, so the screen is there and this is yours to run.

Filed on 2026-09-02. The measurement half of the recogniser question, split from the build because only you have the phone and only you can judge what came out.

Why it cannot be Claude's: there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator] and [install-and-enable-on-pixel]. Beyond that, the test is someone speaking, which nothing here can do.

The walkthrough:
1. Pick three passages of about thirty seconds each and write them down: one of ordinary conversational sentences, one with names and places in it, and one you will speak at your natural speed rather than dictation speed. Look for: having the text in front of you to compare against afterwards.
2. Open any messaging app, tap the microphone on Gboard's keyboard, and dictate all three. Save what comes out. Look for: three transcripts you can read back.
3. Open Hexboard's test screen and dictate the same three passages through it. Save what comes out. Look for: whether the availability line said on-device recognition was available at all, which is the first thing to report.
4. Report three things: whether the two sets of transcripts differ noticeably, where each went wrong, and whether Gboard added punctuation and capitals that the test screen did not.

What a result means, so the report is worth making: near-identical output suggests the same engine behind both doors, and the remaining work is punctuation and capitalisation rather than accuracy. A clear Gboard advantage means the gap is inside recognition, which correcting a finished transcript cannot fix.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
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

Filed on 2026-09-03 at 16:31, split out of the capture that reported the wrong Settings path because a planning session may not write `CLAUDE.md`. That capture's other half — correcting the two live walkthroughs — was done in the same session, and the capture was then deleted.

#### Voice input inside the keyboard, held open by the thumb [in-keyboard-voice-input]
Red flag · State: cleared
Blocked by: [recogniser-gap-comparison], [suggestion-strip]
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
Blocked by: [suggestion-strip]
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
Blocked by: [suggestion-strip]
The accent row for a top-row key draws into the strip above the keys instead of over its own row's neighbours.

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

#### [user] Have a Russian reader check the ЙЦУКЕН layout before it ships [russian-layout-check]
Not before: 2026-10-02
A check of the Russian layout by someone who types Russian, before it ships.

**Kept and dated on 2026-09-02.** [language-starter-layouts] shipped that morning and its record says built *and confirmed*, so nothing in the queue holds this any longer — the walkthrough's first step was tried and works, `planning/layout-preview.html` carrying the Russian board. What holds it is outside the project entirely: you have no Russian typist to hand, and nothing here can produce one. Without the date it would return to the top of the queue every session and be set aside again, which is the failure a date exists to stop. A month was chosen because nothing ships before the keyboard works and it is not yet installable, so the delay costs the project nothing, and a month is long enough that a reader may simply turn up rather than having to be hunted. The span is Claude's proposal and yours to approve, which you did.

**Which half of the superseded research this still leans on.** `workshop/resources/research/cyrillic-overflow-and-slot-budget.md` is marked superseded by [language-starter-layouts] as rewritten the same day: its slot arithmetic was wrong — 26 letter positions rather than 30 — and the hide-six-letters approach built on it was abandoned for FlorisBoard's eleven-wide rows. What still stands, and what this item rests on, is the rest: the ЙЦУКЕН row contents, Ё living under Е on phones, the note that the phonetic ЯВЕРТЫ layout was never investigated, and the file's own admission that its sources were English-language explainers rather than Russian typists — which is the reason this item exists at all.

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

## Unprocessed

> Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Symbols panel's empty slots read as a gap when you swipe into it [symbols-panel-empty-slots]
Raised by you on 2026-09-03, from the first swipe between panels on the real keyboard: the next panel does not run continuously from the last one.

**What it actually is: the config's own empty slots, not a rendering fault.** `resources/key-layout.json` gives the symbols panel 30 keys across four rows of ten columns, and they are not evenly spread. Row 0 has keys at columns 0, 2, 4, 6, 7, 8, 9 and nothing at 1, 3, 5. Row 1 skips 2, 4 and 6. Row 3 skips 0, 2 and 4. So the panel's left half is largely empty while its right half is solid, and a swipe that brings it in shows blank board where keys are expected. The pager was checked and is not the cause — `HorizontalPager` in `KeyboardPanel.kt` sets no `pageSpacing`, so pages abut with nothing between them.

**SPEC already says what to do about this**, which is why the fix is a design conversation rather than a defect report. Its manifest rules hold that empty slots are opportunities rather than acceptable gaps: a freed slot gets filled with a character that has no other home, agreed first. Sixteen slots on the symbols panel currently have no character in them.

So the work is choosing what goes in them — and the choice is yours under that rule, not something a build decides. `resources/key-manifest.md` is the generated view of what is where and is the place to read what the three panels already carry before deciding what is missing.

Read from `resources/key-layout.json` and `KeyboardPanel.kt` on 2026-09-03. What the panel looks like once filled has not been previewed; `planning/layout-preview.html` is the fixture that would show it.

#### Phase line and README status turn over once the keyboard switches on [status-lines-after-install]
**Released on 2026-09-03**, when the install run put Hexboard on the Pixel 6 as a switched-on keyboard. This was held against that run because the two sentences below stayed true until it succeeded; they are now false, so this is live work rather than something pending.

Filed by /rescan on 2026-09-02. Two sentences will be false the moment the install walkthrough shows Hexboard switched on as a keyboard: the phase paragraph in `CLAUDE.md`, written this same day by [claude-md-phase-ran], says there is no input method service yet; and `README.md`'s Status section says there is no working keyboard and nothing to install. Both were true when written and one build behind by the end of the run that wrote them. The phase line states the phase, and the phase changes at exactly this point, so it turns over once rather than at every run. Held against the install because the sentences stay true until it succeeds.

#### Split the board into two halves on wider screens [split-layout-wide-screens]
The board splits into two halves, one under each thumb, whenever the screen is wider than it is tall.

**Released on 2026-09-03**, when the install run put the keyboard on the phone. It was held against that run because what remains open here is seen rather than reasoned, and only an installed input method shows it — which is now available. What is still open is unchanged: the gap width and what each half does with the space bars want the board looked at in landscape.

**Settled with you on 2026-09-02: the split is automatic, and the trigger is width exceeding height.** Not a setting, and not a device class — a phone turned to landscape gets it, a tablet in portrait does not. Gboard's split-as-a-toggle was named as the comparison and not chosen; your rule needs no menu and follows directly from the reason the feature exists, which is where the thumbs are.

**Held against [install-and-enable-on-pixel] on 2026-09-02**, as a capture rather than below the line, because what remains open is seen rather than reasoned: the gap width and what each half does with the space bars want a real keyboard in landscape on your phone, and only the installed input method gives that — the test screen in `MainActivity` is portrait-only in practice. The likely answer for the space bars, recorded so it is the starting point and not a discovery: each half keeps the one nearer its thumb. That ordering is written on both items.

Captured by you on 2026-09-02, immediately after choosing to keep the space bars at columns 4 and 6 in [row3-space-choice]. The two decisions belong together and the connection is the point: your reason for keeping today's arrangement is that in normal portrait handling the thumbs sit naturally near the middle of the screen, so the space keys do not need moving outward. On a wider screen that stops being true — the thumbs move to the edges and the middle becomes the part neither can reach.

**Your proposal: split the board into two halves on wide screens.** Your observation is that the layout has a clean enough middle to split at, and that splitting naturally brings the space keys back under the thumbs in that case, which is the same problem [row3-space-choice] settled for portrait, answered for the case where it actually bites.

What this does not do, and it matters for SPEC's inviolable geometry: a split separates the halves, it does not rearrange keys within them or change the zag rule, the circle size, or nearest-centre routing. Each half keeps its own columns. So this is a question about where the board is drawn rather than about the key inventory, which puts it in the code that lays the panel out rather than in a layout config.

What is not settled, and none of it is a desk decision: at what width the split appears, whether it is automatic or a setting, how wide the gap is, and what happens to the two space bars — whether each half keeps one, which is the arrangement that most obviously puts one under each thumb.

Interacts with [key-press-feedback] and [panel-switch-gestures], both of which edit the same panel code and are held against [compile-and-view-panel], now done. A horizontal swipe crossing the gap between two halves is a case that gesture work will have to answer.

#### Tapping a finished word to see alternatives, which nothing here can do [tap-word-alternatives]
A way to tap a word already in the text and be offered replacements for it, rather than only correcting at the moment of typing.

Captured by you on 2026-09-02, from something you noticed on your own phone: you tapped a mistyped word and Gboard offered the right one from the suggestion bar. Hexboard has no equivalent, and the gap is structural rather than an oversight.

**Why the predictive engine cannot cover this.** [uniform-neighbours-predictive] corrects at the word boundary using a neighbour-weighted edit distance, where a near-miss is a substitution for one of the six adjacent keys. The case that prompted this was "rose" for "rows" — several keys apart, and both real words, so the engine's own first guard, that a word already in the dictionary is never corrected, means it would never touch it. Homophones and wrong-but-real words are a different mechanism from mis-taps, and the geometry that makes this project's correction good says nothing about them.

**Two halves, and only one of them is this item's.** The affordance — select a word already typed, show alternatives somewhere, replace it on a tap — is a keyboard interaction and belongs here. Where the alternatives come from is the harder half and is not settled anywhere: a homophone list, a language model over the sentence, or the saved-word list the predictive engine holds. [speech-output-correction] records what is known about how Gboard does the equivalent.

**A SPEC principle added on 2026-09-03 confirms this item rather than constraining it, and the distinction is the whole reason it survives.** Nothing correctly spelled is ever changed on the user's behalf: Hexboard does not correct grammar or punctuation, and *offering* an alternative the user can tap is permitted where *applying* one is not. Raised by you from Gboard adding an apostrophe to every "its" whether or not it belonged. This item is the offering side of that line — the user asks by tapping a word, and nothing happens unless they choose a replacement — so it is exactly the shape the principle allows. What the principle does rule out is any version of this that pre-emptively substitutes, or that highlights a correctly-spelled word as wrong. A language model over the sentence therefore remains available as a *source of candidates*, and may never become a trigger for changing anything by itself.

**Where the alternatives would appear is likely already answered.** [suggestion-strip] builds a row above the keys for exactly this class of thing, and predictive text is already expected to fill its middle. A word tapped in the text putting its alternatives there needs no new surface.

Not designed enough to build, which is why it stays in Unprocessed: what a build would change cannot be stated until the source of the alternatives is chosen, and that choice wants the strip in existence and the predictive engine's shape settled.

[speech-output-correction] and [uniform-neighbours-predictive] both bear on this and neither contains it. Those orderings are written on the first of them.

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Red flag · State: cleared
Captured by you, and sharpened during the /plan session of 2026-08-06.

**Released from the install hold on 2026-09-03**, when that run put the keyboard on the phone. That changes nothing about readiness: the reason this stays in Unprocessed is the one below rather than the install — SPEC defers predictive text until the keyboard works, and what a build would change still cannot be stated.

Two decisions of yours on 2026-09-01 supersede the storage half of the 2026-08-20 design. Both are now in SPEC.

**No proper nouns in the shipped dictionary.** Your reason, in your own framing: a word being corrected into a name is a particularly irritating failure. This is a property of the word list rather than of the engine, so it becomes a criterion the dictionary research has to satisfy — most frequency-ranked lists include names, so one that excludes them or can be filtered is what the search is for.

**Saved words, which reverses "stores nothing".** The 2026-08-20 decision was a fixed dictionary learning from nothing and recording nothing, chosen so that the engine never holds a record of your writing on the device — the exposure Android's own keyboard warning is about — and that choice is what cleared the flag above. You were told plainly on 2026-09-01 that saving words means the device holds a file of words you typed, that this is smaller than silent learning because each word is a deliberate act rather than a harvest, and that it stops the "stores nothing" sentence being true. You chose the saved-word store, and the reason is the obvious one: a keyboard that cannot learn a surname or a street name is worse to use.

So the flag stays **cleared**, but on a different footing, and the change of footing is the thing to notice. It was cleared by designing the risk out; it is now cleared by your informed consent to a smaller risk, recorded above. Three constraints came with that consent and bound the build: the list grows only when you deliberately save a word, it never leaves the device, and you can read and delete it. A later session must not read this flag as cleared-by-design-out and quietly drop those.

The one thing this does not settle is whether a saved-word list needs anything further — an export, a lock, a way to clear it wholesale. That is a design question for the session that builds this, not something to guess at now.

Reshaped on 2026-08-20 by your design decision, which supersedes the mechanism described further down rather than adding to it. Correction fires **on the space bar**, not per keystroke: at that moment the whole word is in hand, it is compared against the dictionary, and it is replaced by the closest match — ties broken by which word is more common.

Why this is the stronger design, recorded because the earlier one reads persuasively and would otherwise be restored. The forwards-from-seven-candidates mechanism below exists to work around reading a word before it is finished; it treats the first letter as the hard case because a prefix lookup has nothing else to go on. Correcting at the word boundary dissolves that: no letter is trusted more than any other, so the first-letter problem — your original complaint about autocomplete — stops being a special case rather than being compensated for. It is also far simpler, needing no per-keystroke candidate UI.

"Closest match" is where the geometry pays off, and it needs stating precisely: substituting a key for one of its six neighbours is a near-miss and costs little, any other substitution is a real difference and costs a lot. That is a neighbour-weighted edit distance, and it is exactly what no general-purpose library can compute, because none of them know which keys touch which. The argument for building rather than borrowing survives the change of mechanism intact.

Two guards, agreed at the same time. A word already in the dictionary exactly is never corrected, or the engine mangles deliberate spellings. And a correction must be revertable by pressing backspace immediately after it lands, because silent unrevertable autocorrect is the most resented behaviour keyboards have.

**A third guard arrived on 2026-09-03, and it is now a SPEC principle of its own.** Raised by you from using Gboard, which corrected every "its" to "it's" whether or not the apostrophe belonged there. Nothing correctly spelled is ever changed on the user's behalf: Hexboard corrects mis-taps and does not correct grammar or punctuation, and offering an alternative the user can tap is permitted where applying one is not. On this engine's own design that was already true twice over — the dictionary guard above covers "its", and an apostrophe insertion is not a neighbour substitution so the geometry would never propose it — so the principle constrains the routes that had no such guard rather than changing anything here. It is recorded on this item because this is where a later session would look for it.

The privacy question, raised by Claude in that session and settled by you in the same exchange, which is what clears the flag above. A predictive engine improves by learning from what its user types, and that means storing a record of your writing on the device — the thing Android's own warning dialog cautions about whenever a keyboard is enabled. Your decision: a fixed dictionary only, learning from nothing and recording nothing. The risk is designed out rather than accepted, at a known cost in accuracy.

Still not designable, and left in Unprocessed for that reason alone. What settles it is a build — SPEC holds predictive text until the keyboard works, and there is nothing to correct into until there is something to type on.

The word-list lookup this item named as a later session's job was done on 2026-09-01 instead, and is filed as `workshop/resources/research/word-list-licence-and-frequency.md`. Its answer in one line: SCOWL's licence is permissive enough to ship inside this public repository under PolyForm Noncommercial, and it separates proper names into a category that can simply be left out at build time, which satisfies the no-proper-nouns rule without anyone writing a filter. What SCOWL lacks is frequency data; its size levels are a coarse commonness ranking that may serve for tie-breaking, and if a real table is wanted, Leipzig (CC BY) raises no ShareAlike question where wordfreq (CC BY-SA) does. The finding also names one trap by name, so nobody walks into it twice.

**Why the board's routing must never produce a nothing-tap, raised by you on 2026-09-02 and reasoned out together.** You asked whether the touch targets should be circles that kiss, on the ground that a hexagonal cell has corners that could be pressed by accident, and then asked whether a wrong keystroke is harder to correct than a missing one. The second question answers the first, and it answers it from this engine rather than from geometry.

This engine corrects at the word boundary with a neighbour-weighted edit distance: substituting a key for one of its six neighbours is a near-miss and costs almost nothing. A wrong character is therefore the failure the whole design is built around. A *missing* character is a different edit — a deletion — and the six-neighbour geometry carries no signal for it: nothing says which letter failed to arrive, or where in the word. The engine would be guessing blind.

So touch regions that leave dead space would produce exactly the failure this engine is worst at, in order to avoid the one it is best at. That is the standing argument for the board's full-coverage routing, and it is recorded here rather than in SPEC because it is reasoning rather than product truth.

Two things were established at the same time and belong with it. A Voronoi corner is a boundary and not a target — it is the point equidistant from three centres, so a tap there resolves to one of the three rather than triggering anything of its own. And making the drawn circles kiss would not remove the hexagonal cells anyway: with the gap at zero the circles merely grow, and the space between them is still assigned to the nearest centre. Geometry allows only three arrangements — full coverage with hexagonal cells, overlapping circles needing an arbitrary draw-order tie-break, or circles with dead interstices — and only the third has no corners. The perceptual wedge is unaffected either way, because the cells are never drawn: SPEC's own note says the claim is perceptual rather than functional.

The suggestion row this engine's candidates appear in is not this item's to create: [in-keyboard-voice-input] introduces a strip above the keys for its microphone on 2026-09-02, and predictive text fills the rest of that row rather than adding one. That ordering is written on both items.

Rests on: SCOWL's licence and its proper-names category, read from its own readme on 2026-09-01; the Leipzig licence, reported second-hand in that finding rather than read off Leipzig's terms, and to be checked before anything is bundled.

In a hexagonal tessellation every key sits the same distance from each of its neighbours, and each interior key has exactly six of them. A standard rectangular keyboard doesn't have this property: horizontal neighbours are closer than diagonal ones, so the set of plausible mis-taps is uneven and direction-dependent. On Hexboard, for any key pressed there are exactly six other keys the user might have meant, equally likely by distance alone — a clean, uniform confusion set.

The user's reason for caring about this: their biggest complaint about autocomplete is that getting the first letter wrong is far worse than getting any later letter wrong, because ordinary autocomplete is a prefix lookup that only reads words forwards and so treats the first letter as certain. The neighbour set dissolves that. Rather than trusting the pressed key, the engine searches forwards from all seven candidates — the pressed key and its six neighbours — and ranks results by geometric plausibility times word frequency. Prefix-trie walks are cheap enough that seven of them cost nothing noticeable. Searching the letters backwards, the fix SPEC previously recorded, remains useful for typos later in a word, but is no longer what rescues the first letter.

This is the argument for building the engine rather than adopting a library: no general-purpose library knows the key geometry, so none can exploit any of it. Neighbour sets come from `resources/key-layout.json` rather than being hand-maintained.

Three things to check before relying on it, recorded so they aren't discovered late. Keys at a panel's edge have fewer than six neighbours, so the uniform case is the interior one. The two space bars in row 3 are not ordinary circles and won't fit the neighbour model cleanly. And the property holds within a panel, not across panels — a mis-tap can't cross a swipe boundary.

A fourth thing to know before designing this, added on 2026-08-07. The neighbour sets are derivable from `resources/key-layout.json`, but not from it alone. The config carries each key's `row` and `col` and nothing more — geometry is deliberately excluded from it, because the zag rule (odd columns sit half a key lower) is the perceptual wedge SPEC calls inviolable and so lives in Kotlin. Which six keys neighbour a given key depends on that zag parity, not just on row and column. So the neighbour table is computed by the app at runtime from the config plus the zag rule; it is not a lookup the config can hold, and nothing should be designed on the assumption that reading the config is sufficient.

Not yet designed enough to build — what exists is the insight and the approach, not a description of what any build would change. It needs a later /plan to turn into buildable work, and SPEC holds predictive text until after the first working keyboard anyway. The three caveats above are the known starting points for that design session.

Filed after `a42cd01`.

#### Let a user choose which layout variant they are typing on [layout-switching]
Blocked by: [ship-all-layout-configs]
Filed by /rescan on 2026-08-20, from a requirement SPEC created the same day and nothing in the queue held: if one app ships several layouts, the user has to be able to pick theirs. It was named as an accepted cost when the platform decision was made and then not filed, which is how a feature dies in SPEC.

**Where the picker lives** was settled by you on 2026-09-01 and is in SPEC: the app's own settings, not the keyboard surface. The keyboard-surface option — a long-press or a gesture, reachable without leaving what you are typing — lost because horizontal swipe already means "change panel", so the picker would have had to find a gesture the layout has not already spent. Recorded because it is the obvious idea and will otherwise be re-proposed.

**What the picker shows**, settled with you on 2026-09-01 and narrowed on 2026-09-02: layouts grouped by language, ordered within each language by a set position rather than by anything counted. Usage telemetry was the alternative for ordering and lost outright — it is the only true measure of popularity and it would have been the first thing in Hexboard to report what a user does back to a server, against every other feature's posture. Nothing counts anything.

Two things this picker was going to show and now will not, recorded because both were designed and then removed rather than forgotten: a link to whoever made the layout, and layouts the user built on the device sitting alongside the shipped ones. Both went on 2026-09-02 with the layout editor, when you settled that Hexboard ships layouts copied from each language's own standard and nobody builds their own.

**The data it needs is [variant-language-fields]**, split out on 2026-09-01 so every layout written carries the fields before there is a picker to read them. That ordering is written on both items.

What is still open, and none of it is a desk decision: how the app enumerates the available layouts at runtime, and where it remembers the choice. Both want a keyboard that runs.

**Hold moved again on 2026-09-02, and this time onto something the code shows.** [first-installable-build] and [variant-language-fields] both shipped that day, so neither holds this. What does is [ship-all-layout-configs]: `android/app/build.gradle.kts` copies exactly one hard-coded file, `key-layout.json`, into the app's assets, so `key-layout-ru.json` exists in the repository and never reaches the app. There is nothing for a picker to enumerate until the copy task ships the folder rather than the file, and that is what that item does. Read from the Gradle file and `KeyLayout.kt` on 2026-09-02.

**The other open question turns out not to need a running keyboard.** Where the app remembers the choice is an ordinary stored preference read by both the app and the input method service — a desk decision whenever this is taken up, with no dependency on anything. Only enumeration was ever genuinely waiting.

**Why it was not designed out on 2026-09-02, which is a judgment worth recording.** The picker is only worth having once there is more than one layout *in the app*, and there is one. Designing a chooser for a list of one invites decisions that would be revisited the moment the second layout actually arrives, so the small Gradle change goes first and this follows it. Claude's recommendation, your agreement. It stays in Unprocessed rather than below the cleared-to-run line because what a build would change still cannot be fully stated — the enumeration half depends on a shape that item has not yet built. Nothing here is waiting on you.

Interacts with [panel-switch-gestures]: horizontal swipe already moves between the three letter panels, so whatever switches layouts must not collide with a gesture that already means something.

SPEC puts language switching out of scope for early iterations while the platform principle says the app must *eventually* let a user switch layouts, so the deferral is what SPEC asks for rather than a delay against it.

#### Correcting what the speech recogniser returns [speech-output-correction]
Blocked by: [recogniser-gap-comparison]
Captured by you on 2026-09-02, and separate from the keyboard's own autocorrect by your instruction. Your target: dictation as good as Gboard's. Your reason for raising it: the correction Gboard does looks like AI to you, and you wanted that checked rather than assumed.

It is AI, and the check changed the shape of the feature. Gboard's on-device recogniser has been an all-neural end-to-end model since 2019, and there is no separate autocorrect stage at all: correction happens inside recognition, with an optional second pass where a language model re-ranks the recogniser's own N best guesses at the whole utterance. So a corrector applied to a finished transcript has strictly less to work with than the recogniser had — the audio is gone, and with it every alternative that was considered and rejected. It can tidy output; it cannot close a gap in recognition quality. That is why [recogniser-gap-comparison] comes first: it establishes whether there is a gap to close.

**The tension with the rest of this project, which is a decision for you and not an implementation detail.** Google's own documentation says transcripts of what the user says and types are saved on the device, and that corrections are used to improve dictation for that user. Part of Gboard's advantage is a per-user record of how someone speaks and how they fix it. Hexboard refuses that class of storage everywhere else: predictive text keeps only words deliberately saved, and enrolment audio for [personal-voice-model] is destroyed after adaptation. Matching Gboard by the same means would reverse that. Nothing is decided here, and no design should assume either answer.

Three routes are visible and none is chosen: rely on the platform recogniser and add only punctuation and capitalisation; bias recognition toward the user's saved words and contacts, if the on-device API permits biasing at all, which was not researched; or correct the transcript against the saved-word list the predictive engine already holds. Which of them are open depends on what the comparison finds.

**A SPEC principle now bounds all three, added on 2026-09-03.** Nothing correctly spelled is ever changed on the user's behalf — Hexboard corrects mis-taps and does not correct grammar or punctuation, and offering an alternative the user can tap is permitted where applying one is not. Raised by you from Gboard adding an apostrophe to every "its" whether or not it belonged. This bites hardest on the first route above: adding punctuation and capitalisation to a *dictated* passage is what the recogniser did not supply and is not a correction of anything the user typed, so it stays available — but reaching into a finished word to add an apostrophe does not, however confident the sentence model is. Whichever route is taken, this is a constraint on it rather than a question for it.

Cites research: `workshop/resources/research/gboard-speech-correction.md`, which carries the sources and states plainly that the architecture papers are from 2019 and 2020 — sound on where correction happens, and not a description of what Gboard ships today.

**An observation of yours from 2026-09-02, and what it narrows.** You dictated a passage, switched to typing by hand, and later noticed "rose" where you had meant "rows". Tapping the word offered "rows" — a homophone correction, on text dictated during a session that had since become an ordinary typing session with the microphone off. Your question was whether Gboard tags which words arrived by voice and keeps that tag afterwards, or whether it would have offered the same correction on typed text.

Looked up the same day, and it does not settle it. What was found points the second way: Google's own description of the proofread feature says it checks "any typed, pasted or voice-dictated text", naming all three origins together, so an origin-blind proofreader explains what you saw without any provenance tracking at all. Nothing found describes Gboard tagging words by how they arrived — recorded as *not found* rather than as established absent. **Your own caution is the reason this is worded that way**: you pointed out that the observation is ambiguous between the two explanations, against Claude having first called it evidence for one.

**Why it matters here.** An origin-blind corrector needs no record of what was dictated, which is exactly the storage this project refuses everywhere else. So the tension this item records — that matching Gboard might mean keeping a per-user transcript — is narrower than it looked: at least the homophone half of the job needs no such record. It does not touch the recognition-quality half, which is still what [recogniser-gap-comparison] has to measure.

The affordance the observation depends on — tapping a finished word to see alternatives — is filed separately as [tap-word-alternatives], because Hexboard has no design for it and the predictive engine cannot reach homophones. That ordering is written on both items.

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

#### Which languages get a layout, and in what order [language-list-choice]
Blocked by: [russian-layout-check]
Filed on 2026-09-02, when you replaced the contributor-facing editor with layouts copied from each language's own standard. Russian is being done first as the hard case, in [language-starter-layouts]; this is the decision about what follows it.

**Half of it is already answered and is in SPEC:** layouts are added as they are asked for. You settled that on 2026-09-02, and the standing policy is demand-driven rather than a planned rollout. What stays open is only whether there is a first batch beyond Russian.

**Held against [russian-layout-check] on 2026-09-02, and the reason is the thing this decision is missing.** Each language costs three things: Claude reading FlorisBoard's layout and popup files for it (the source settled on 2026-09-02 for Russian, recorded in `workshop/resources/research/open-source-layout-sources.md`), Claude transcribing them into a config with rows as wide as the source's, and a check by someone who reads it. The first two are cheap and yours to trigger rather than to do. Where FlorisBoard offers more than one layout for a language, which to copy is a question for this item — it was not researched. The third is the bottleneck, and it does not scale with how many people speak a language — it scales with whether a reader of it can be found to look. A language with a hundred million speakers and nobody to check it costs more than one with five million and someone willing. Nothing has tested that yet: [russian-layout-check] is the first attempt this project makes at getting a native reader to look at a layout, and how much trouble it is to arrange is what should set the size of any list. So the item returns by itself carrying the one fact it lacks.

An alternative was offered and not taken up now: researching which alphabets fit 30 slots and which overflow — Greek at 24 letters and Hebrew at 22 being transcription jobs, while Arabic, Vietnamese and the Indic scripts each raise their own version of the Cyrillic problem. It is useful whenever this is taken up, and it answers what is cheap rather than what is wanted, so it was not a reason to decide now.

Worth settling in the same conversation: what happens where a language has two competing standards. Russian already raises it — the phonetic ЯВЕРТЫ layout sits alongside ЙЦУКЕН and was never investigated — and Hexboard would have to either pick one or ship both as separate layouts, which the picker's ordering within a language is capable of holding.

