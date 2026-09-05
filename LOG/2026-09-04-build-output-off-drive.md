# 987cb5c — Gradle's build output made relocatable per machine, and pointed off Drive here

Written 2026-09-04 at 15:52.

The first Run of 2026-09-03 died with five `AccessDeniedException` errors out of `Files.deleteIfExists` — Gradle unable to delete its own output. Three suspects were named and none proved: Google Drive syncing files Gradle was still writing, Windows Defender, and Windows' 260-character path ceiling, the last brought over from the Taskflow project where it had bitten at 279 characters. Measurement here found the deepest path at 234 characters, 26 short of the ceiling, so path length did not cause that failure — but the deepest paths are Kotlin-generated synthetic class names, which grow with the code, so it is a hazard waiting rather than an innocent number.

The design's whole point is that it answers all three without identifying the guilty one. A short path outside `My Drive` takes the output away from Drive's sync, away from whatever else held it, and roughly doubles the headroom under the ceiling: `C:\builds\hexboard` is 18 characters against the project root's 57, so every build path loses 39 and the 234 becomes about 195.

Nothing is hard-coded, because this repository is public and an absolute Windows path in a tracked build file would break every clone. `android/app/build.gradle.kts` reads an optional `hexboard.buildDir` key from `android/local.properties` — already gitignored, and already the conventional home for per-machine Android settings — and falls back to Gradle's own default when the key is absent or unreadable, so a fresh clone builds unchanged. `README.md` gained a section naming the key, since a setting invisible in the repository is a setting nobody finds.

It doubles as an experiment at no extra cost: if builds stop failing, Drive or Defender was the holder; if they still fail, neither was, and the cause is something nobody has named.

**Confirmed:** nothing. Gradle cannot run on this machine.

**Unconfirmed, transcribed from the tick:** Gradle cannot run on this machine. Android Studio must build and confirm that `android/app/build/` is empty afterwards, the output appears under `C:\builds\hexboard`, and the app still installs and runs on the Pixel 6; and that removing the `hexboard.buildDir` line restores the default location.

**Files touched:** `android/app/build.gradle.kts` (an optional per-machine build-output block, 17 lines), `android/local.properties` (the key itself, untracked, 4 lines), `README.md` (a Building the Android app section, 10 lines).

**Routed to Captures:** none from this item. The verification it needs is covered by [verify-this-runs-build-on-device], filed at this session's rescan.

**Depth:** short.
