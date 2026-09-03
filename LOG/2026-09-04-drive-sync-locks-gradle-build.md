# [HASH] — [drive-sync-locks-gradle-build] answered by moving Gradle's output to a short path outside Drive, which addresses three suspects at once — the third brought over from another project by the user

Recorded 2026-09-04 at 17:00. This session ran across 2026-09-03 and 2026-09-04.

The capture recorded that the first Run of 2026-09-03 failed with five `java.nio.file.AccessDeniedException` errors raised from `Files.deleteIfExists` against `android/app/build/intermediates/incremental/debug/` — Gradle unable to delete its own output. Deleting the build folder and pausing Google Drive got the install through, but the two changes happened together, so the success was evidence rather than proof, and Windows Defender was never ruled out.

Two things were established here before any fix was considered. Everything under the churning folders is already gitignored — `android/.gitignore` covers `/build` and `.gradle` — so Drive is syncing, versioning and uploading files git deliberately excludes: all of the cost, none of the benefit. And Gradle's own documentation gives `layout.buildDirectory` for relocating build output, while `--project-cache-dir` for the `.gradle` cache is documented as a command-line flag with no confirmed `gradle.properties` key. Since builds here go through Android Studio's Run button rather than a terminal, only the certain half was taken.

**The third suspect came from the Taskflow project, carried across by the user**, who showed how that project had dealt with the same class of failure: Windows' 260-character path ceiling, which had bitten there at 279 characters. Measuring here found the project root at 57 characters and the deepest build path at 234 — under the ceiling by 26, with the deepest paths being Kotlin-generated synthetic class names that grow as the code does. So path length did **not** cause this failure, an over-length path raising a different error than the one seen; it is a hazard sitting 26 characters away, which would arrive intermittently and confusingly.

That contribution materially improved the answer. The target had been about to be the local temp folder, which is outside Drive but long — relieving the sync collision and leaving the path hazard roughly where it is. A short path addresses all three suspects at once: `C:\builds\hexboard` is 18 characters against the root's 57, so every build path loses 39 and the 234 becomes about 195.

The path is not hard-coded into a tracked file, because this repository is public and an absolute Windows path would break a fresh clone. The build file reads an optional key from the gitignored `android/local.properties` and falls back to Gradle's default when it is absent, so the observation has two halves: output appears under the new path, and removing the key restores the old behaviour.

Two options were refused. A walkthrough step telling the user to pause Drive before every build costs a step in every item that compiles, forever, and leaves the path ceiling untouched. Moving the whole project out of `My Drive` changes where the repository lives and what backs it up, which is a bigger decision than a build obstacle warrants — and this makes it unnecessary rather than deferring it.

The fix doubles as the experiment, which is recorded on the item because it costs nothing extra: if builds stop failing, Drive or Defender was the holder; if they still fail, neither was.

**Queue changes:** [build-output-off-drive] created and placed at the top of the cleared region, ahead of the feature work, since it blocks compiling at all.

**Work processed:** deleted [drive-sync-locks-gradle-build], its content having moved into the new item.

**Advisory:** not needed — the close's recommendation names no single item to start from.
