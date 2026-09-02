# 6f66348 — [assets-srcdir-deprecation] kept: the Variant API route, cleared last among the builds, checked at the next Android Studio run

Planning record for [assets-srcdir-deprecation], 2026-09-02.

Filed the same morning from the Build panel: the line that copies `key-layout.json` into the app's assets uses a Gradle call now deprecated on Android Gradle Plugin 9.2.1 (read from the version file), and the other half of the same API already broke the build that day. Two routes were open. The Variant API route was chosen — `addGeneratedSourceDirectory` — because it is what the plugin's own error message recommended and it restores the task-dependency wiring the morning's plain-`File` fix gave up, rather than only silencing the warning; the `directories` set is recorded as the fallback if the copy task cannot expose the output the Variant API wants.

Gradle cannot run on this machine, so the item says plainly that the change is unconfirmed until the next Android Studio run — [install-and-enable-on-pixel] is the next item that compiles, and its walkthrough now says to report the Build panel if the sync complains. Placed last of the builds in the cleared region, since nothing depends on it and a build-file change is the kind that can stop a sync.
