# [HASH] — Every key-layout config now copied into the app, so a further language is one new file

Written 2026-09-04 at 15:55.

Filed by /rescan on 2026-09-02 from the run that built [language-starter-layouts]: the Gradle copy task took a single `RegularFileProperty` pointed at `resources/key-layout.json` and wrote one hard-coded name into the generated assets, so `key-layout-ru.json` existed in the repository and not in the app. A layout picker has nothing to enumerate until that is fixed, which is the open question [layout-switching] records.

The copy is filtered rather than wholesale, settled with the user on 2026-09-02 — Claude's recommendation, his agreement. `resources/` also holds the generated manifests and an images folder; copying the folder would put files in the APK that no code reads, and the manifest in particular is generated *from* the config for people to read, so shipping it would mean the app carrying a second copy of what it already parses. So the task takes `key-layout*.json` and leaves the rest.

The task's input became a `DirectoryProperty` and its action now copies every match under its own filename, the generated assets directory being flat so filenames alone distinguish the configs. Refused: copying the whole folder, and adding the Russian config as a second hard-coded name, which leaves the same defect for the next language.

**Confirmed:** nothing.

**Unconfirmed, transcribed from the tick:** Gradle cannot run here, so `ShippedConfigsTest` is Android Studio's to run on the Pixel 6.

**Files touched:** `android/app/build.gradle.kts` (CopyKeyLayoutConfig takes a directory and copies every `key-layout*.json`; task description and the file's opening comment rewritten, ~20 lines), `ShippedConfigsTest.kt` (created, 3 tests, 78 lines).

**Routed to Captures:** none from this item.

**Depth:** short.

[layout-switching] was held against this item and its blocker is now answered — a picker has something to enumerate. Lifting it is planning's call. The same copy task was extended again later in this run, by [emoji-panels], to carry Unicode's emoji data alongside the configs.
