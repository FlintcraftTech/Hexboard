# [HASH] — [stale-android-build-dir] the folder inspected, the user's go recorded, and the deletion left for a build run

Session of 2026-09-12, 12:54.

`android/app/build/` holds Gradle output dated 3 September, inside the Google Drive folder this project lives in, so it is being synced — one of the two reasons [build-output-off-drive] moved the build output elsewhere. It cannot corrupt a build any more; it is bulk in a synced folder for no purpose.

The entry asked for a person to say go rather than a rule, on the method's line that a folder Claude did not create this session is never presumed rubbish. The look it wanted was done here: four top-level entries, every one a canonical Gradle output directory — `generated`, `intermediates`, `kotlin`, `outputs` — nothing hand-placed among them or at the level below, 51 MB in total. `android/app/build` is the only stale build directory in the tree, and `android/local.properties` still carries the `hexboard.buildDir` setting, so deleting it does not invite Gradle to recreate it.

**The user agreed to the deletion and then chose to leave it for a build run rather than have it done in the planning session.** The route that would have allowed it there — the scope-lock's door, which records the path in a scope file and the close as handmade work — was offered and declined. So the item is cleared to run with the inspection and the go-ahead written into it, so the build does not re-ask.

**Queue changes:** [stale-android-build-dir] cleared to run.

**Work processed:** kept — [stale-android-build-dir].
