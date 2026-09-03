# [HASH] — Installing Hexboard on the Pixel 6 and switching it on as a keyboard

Walk-through record for [install-and-enable-on-pixel], closed 2026-09-03 14:14. Opened as the drive started so nothing is lost if the session ends mid-way, and appended step by step. This session ran across 2026-09-02 and 2026-09-03.

The run held one cleared item and it was this walkthrough, so nothing was built in this run.

The light capability check was run before handing the first step over: every step is a click inside Android Studio or a tap on the user's own handset. There is no `adb` on this machine and Gradle cannot reach its own daemon here, both established by attempt in earlier sessions and recorded on the item. Confirmed user work.

What is unverified going into this drive: the four changes to `KeyboardPanel.kt` made on 2026-09-02, the IME service registration from [first-installable-build], and [assets-srcdir-deprecation]'s rewiring of the Gradle assets copy. Nothing has compiled since any of them. Eight queued items are held below the readiness line waiting on this run.

## What happened

- Drive opened.
- Steps 1–3 were already satisfied when the drive started: the project was open in Android Studio, the run configuration read `app`, and the Pixel 6 showed in the device dropdown in normal text. The user had gone straight to step 4.
- **Step 4 failed.** `Build android: failed ... with 5 errors`, two each on `:app:packageDebugResources` and `:app:mergeDebugResources`, all of them `java.nio.file.AccessDeniedException` raised from `Files.deleteIfExists` against paths under `android/app/build/intermediates/incremental/debug/`. Gradle could not delete its own output, which is a file-locking failure rather than anything wrong with the Kotlin.
- Two file-holding processes were found running on this machine: Google Drive's own sync process, and Windows Defender. The project lives inside `My Drive`, so every file Gradle writes under `build/` is being synced as it churns, and Drive holding a handle open while Gradle tries to delete is the likeliest cause. Windows Defender is the second candidate and the less likely one. Neither was proved — this is the diagnosis the evidence supports, not a confirmed cause.
- `android/app/build/` was deleted from Claude's shell rather than handed over as a Build → Clean Project step. It went cleanly, 80MB, confirmed gitignored first. That it deleted without complaint says no lock was held at that moment, which fits a transient sync lock and rules nothing out.
- The user paused Google Drive syncing and ran again. **Install successfully finished.** So the whole of 2026-09-02's Kotlin compiles, the Gradle rewiring from [assets-srcdir-deprecation] syncs, and the app installs. The build half of what this item was verifying is done.
- Step 5's Settings path was wrong for this handset. The user found the screen themselves — it is titled **Keyboard apps** — and switched Hexboard on. Which path reaches it was not recorded. Filed as [install-walkthrough-settings-path] with the observation that a Settings search is version-proof where a menu path is not.
- Both dialogs Android showed were the expected ones: the input-method collection warning the step names, and the standard note that an app cannot start after a reboot until the phone is unlocked.
- **Step 6 done: the keyboard draws.** Circular keys in zig-zag rows, in a real text field, as an input method rather than a test activity. First time any of this has run as a keyboard.
- One fault found and filed rather than fixed: the board's bottom row is drawn underneath Android's navigation bar, so row 3's keys — the two space bars worst of all — sit behind the system buttons and lose their lower halves to them. Filed as [board-under-navigation-bar]. Not seen before because `MainActivity`'s test screen is an ordinary activity and gets an activity's insets; the input view does not.
- The Google Drive lock that failed the first Run was filed as [drive-sync-locks-gradle-build], since it will recur at every build.
- **Step 7 answered, and answered by use rather than by inspection: the user typed their report to Claude on Hexboard.** Keys respond, characters arrive where they were aimed, and the horizontal swipe changes panel. All three of the item's questions are yes.
- A second observation from that swipe — the next panel does not run continuously from the last. Looked into rather than filed on sight, and it is not a rendering fault. `HorizontalPager` sets no `pageSpacing`, so the pages abut; what looks like a gap is the symbols panel's own empty slots, sixteen of them, clustered on its left half. SPEC's manifest rules already call an empty slot an opportunity to be filled by agreement, so the work is choosing characters rather than fixing a bug. Filed as [symbols-panel-empty-slots].
- Looking into that turned up a factual error in a queued item. [panel-key-size-consistency] says panels of differing width today means Russian alone; the English RARE panel's row 2 reaches column 10, so it is eleven wide against QWERTY's ten and the mismatch is live on the default layout. Filed as [rare-panel-eleven-wide] so the correction is made in front of the user rather than silently.

**Outcome: done.** Every step was reached and the item's three questions are answered yes. Nine builds recorded as unconfirmed are now confirmed to compile, install and run, which is what eight held items were waiting on.

**Files touched:** none by this item.

**Routed to Captures:** [install-walkthrough-settings-path], [drive-sync-locks-gradle-build], [board-under-navigation-bar], [symbols-panel-empty-slots], [rare-panel-eleven-wide], [uppercase-labels-vs-shift-state].

Advisory: filed — [forward-advisory], replacing the spent one that pointed at this item. It names the thirteen items whose hold has just gone, and the three captures filed today that bear on them.

**Also in this chat:**

The user asked why one item was cleared and fourteen held, and whether the held work was genuinely blocked or merely ordered after it. Answering it produced a distinction worth keeping: four were literally blocked, three waited on other queued work rather than on the install, six were held only so that further changes would not stack on files nothing had compiled, and one was held by a date. The user then argued that the six needed no enforcement — an ordering plus the forward advisory the close already files would have done. That is right about the outcome and wrong about the mechanism, and the correction is the interesting half: /next builds every Claude-work item in a run before it walks any `[user]` item, so queue position alone cannot put a walkthrough first, and the hold is what makes "first" mean first.

On the user's instruction, that was reported to the project that maintains this method, as mail rather than as a public issue, with their two proposals carried as theirs: that a `[user]` item be walked in its queue position, and that the run's off-ramp offer to reorder be questioned. They chose not to track a reply, so nothing was filed to check for one. The outbound line is in the mailbox register.

One correction Claude made to its own work: `android/app/build/` was deleted from the shell rather than handed over as an Android Studio menu step, which is the standing preference for reaching a tool instead of walking the user through a click path. The deletion was checked against `.gitignore` first.

The wind-down re-scan is covered by the rescan just run.
