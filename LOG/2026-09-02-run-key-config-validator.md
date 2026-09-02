# [HASH] — Key-config validator run green, after two build-config failures on the way

Walk-through record for [run-key-config-validator], opened as the drive started so nothing is lost if the session ends mid-way. Appended to step by step.

The item exists because Gradle cannot run from Claude's shell on this machine: it needs a loopback connection to its own daemon, and four routes were tried and blocked on 2026-08-06. Android Studio has no such restriction. What running the test proves is that the test compiles, that Gson resolves as a test dependency, and that the test finds the config file at runtime — the key data itself was already checked independently when it was built.

The light capability check was run before handing over: the steps are a Gradle sync and a run-configuration click inside Android Studio, and there is no tool on this machine that would let Claude do either. Confirmed user work.

## What happened

- Drive opened. Step 1 given: open the `android` folder in Android Studio and wait for the Gradle sync to finish.
- Android Studio asked which SDK to use — its own default, or the one `local.properties` names. Both were checked from the shell and both carry platform android-36.1, which is what the module's `compileSdk` asks for, so either would build. Studio's own was recommended and chosen, on the grounds that it is the one Studio's SDK Manager keeps current; `local.properties` is git-ignored, so nothing about the choice reaches the repository.
- The sync then failed with one error, at `android/app/build.gradle.kts` line 56: "You cannot add Provider instances to the Android SourceSet API." The line is `sourceSets["main"].assets.srcDir(generatedAssetsDir)`, which is what puts `key-layout.json` into the app's assets, and `generatedAssetsDir` was a `Provider<Directory>` from `layout.buildDirectory.dir(...)`. The current Android Gradle Plugin rejects a Provider there, so nothing could compile and both this item and [compile-and-view-panel] were blocked behind it.
- Fixed with the user's agreement, `android/app/build.gradle.kts` being added to the run's file list first: `generatedAssetsDir` is now resolved to a plain `File` where it is declared, which both the copy task and the assets wiring accept. The one-line change is unverified from here — Gradle cannot run from Claude's shell on this machine — so the re-sync is what confirms it.
- Step 1 re-issued: sync again and report. The sync passed — the Sync tab reported `android: finished` with a green tick — so the build-config fix is confirmed by the thing it was blocking. The user reported not knowing how to sync, and was given File → Sync Project with Gradle Files rather than the toolbar button or the banner link; that is the route worth keeping in any later walkthrough, since it names a menu item rather than an icon.
- Step 2 given: open `KeyLayoutValidationTest.kt` by pressing Shift twice and searching for it by name. That did not work — the search-everywhere box is opened by a double keypress and the file never opened. Re-given as a click path down the Project pane's tree instead: expand `kotlin+java`, then the entry suffixed `(test)`, which holds `ExampleUnitTest` and `KeyLayoutValidationTest`. That route worked, and is the one to write into this walkthrough: it is visible and clickable, with no timing in it.
- Step 3 given: right-click `KeyLayoutValidationTest` and choose Run. The test did not execute — the build failed first at `:app:checkDebugAarMetadata`, reporting that three libraries require the app to be compiled against SDK 37 while the project was on 36.1.
- Fixed with the user's agreement: `compileSdk` raised from 36.1 to 37 in `android/app/build.gradle.kts`, which was already in the run's file list. `minSdk` (26) and `targetSdk` (36) are untouched, so which devices the app runs on is unchanged. The alternative — downgrading the three libraries — was named and not taken, because it is more churn and pins the project to older Compose. The SDK chosen earlier, Android Studio's own, carries platforms 36 and 36.1 but not 37, so Studio has to download platform 37; the other SDK on this machine already had android-37.0.
- Step 3 re-issued after that change. **Seven tests passed.** That is what the item existed to establish: the test compiles, Gson resolves as a test dependency, and the test finds the config at runtime. The item's walkthrough says six, written when the test had six; the file now carries seven `@Test` methods, so seven is right and the walkthrough's figure is stale rather than the result being wrong.
- Android Studio also raised a "build performance issues" suggestion in the margin. That is an IDE recommendation about build speed, not a failure, and nothing was done about it.

**Outcome: done.** The compileSdk change is confirmed by the same run, since the metadata check it was made for is what the build had to get past.

**Files touched:** `android/app/build.gradle.kts` — two fixes, both added to the run's scope with the user's agreement as they were found.

**Routed to Captures:** [assets-srcdir-deprecation], [install-walkthrough-refresh], [android-studio-step-authoring].
