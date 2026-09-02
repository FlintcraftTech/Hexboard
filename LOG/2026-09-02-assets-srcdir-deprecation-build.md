# 8f705a3 — [assets-srcdir-deprecation] built: the key-config copy rewired through the Variant API, removing the deprecated srcDir call

Closed 2026-09-02 16:18. Ninth item of the run, placed last of the code changes because a build-file change is the kind that can stop a sync.

The route was settled at planning: `addGeneratedSourceDirectory` on the variant's assets, which is what the plugin's own error message recommended and which restores the task dependency the earlier plain-file fix gave up. The item asked the build to read the plugin's documentation for the exact method shape, since neither route had been looked up. The reference page returned only its navigation, so the shape was read from Google's own gradle-recipes sample for this plugin version instead: a task exposing a `DirectoryProperty` output, passed with a property reference, and the plugin — not the build file — choosing the generated directory. The `directories` mutable set stays the recorded fallback, unused.

The Copy-typed task, the preBuild dependency and the `srcDir` line are gone; a small task class copies the config into whatever directory the plugin assigns.

Tick: done, UNCONFIRMED: needs the next Android Studio sync (no `srcDir` deprecation in the Build panel) and the app still finding key-layout.json in its assets on the Pixel 6; the method shape was read from Google's gradle-recipes `addGeneratedSourceFolder` sample for AGP 9.2 rather than the reference page, which returned only its navigation.

**Files touched:** `android/app/build.gradle.kts`.

**Routed to Captures:** none.
