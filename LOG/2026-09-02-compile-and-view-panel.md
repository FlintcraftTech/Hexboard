# 1979343 — First run on the Pixel 6: it compiles, the panel draws, and taps land where they were aimed

Walk-through record for [compile-and-view-panel], opened as the drive started so nothing is lost if the session ends mid-way. Appended step by step.

Three things are unverified going in: that the Kotlin compiles, that Gson parses `key-layout.json` out of the app's assets at runtime, and that the panel draws. Gradle cannot run from Claude's shell on this machine, so all three need the phone.

The light capability check was run before handing over: every step is a click inside Android Studio or a tap on the user's own handset, and there is no `adb` on this machine to reach a device with. Confirmed user work.

Two build-config faults were already found and fixed earlier in this same session, while driving [run-key-config-validator] — a Provider passed to the Android SourceSet API, and a `compileSdk` below what three libraries require. Both would have stopped this item too.

## What happened

- Drive opened. The item's steps 1–4 pair the Pixel 6 over Wi-Fi, but a screenshot earlier in this session already showed "Google Pixel 6" in Android Studio's device dropdown, so the pairing looks done. Asked rather than assumed, on this item's own turn. The pairing was indeed already done, so steps 1–4 were skipped.
- The Pixel 6 showed greyed out in the device dropdown, which read as a problem and was not one: the selected run configuration was still the unit test, which needs no device. Switching the configuration to `app` un-greyed it.
- The Run button then stayed disabled. The cause was a pending Gradle sync, prompted by the `compileSdk` edit earlier in the session and announced in a banner reading Sync Now that was hidden behind the open dropdown. Syncing enabled the button. Worth writing into the walkthrough: after any change to a Gradle file, sync before expecting Run to work.
- The app installed and opened. All three of the item's questions answered yes: it compiled, the keys drew in their zig-zag rows, and the characters arriving at the top matched the keys the user aimed at. Multi-touch worked too, which nothing had asked about.
- One fault found and filed rather than fixed: holding the delete key deletes a single character instead of repeating. Filed as [backspace-key-repeat] in Unprocessed, with the design question it opens — which keys repeat at all, given that a repeating letter would collide with the long-press accent menu — and its collision with [key-press-feedback] and [panel-switch-gestures], which edit the same file.

**Outcome: done.** This is the first time any of this Kotlin has run. Two build-config faults had to be fixed to get here, both recorded in the [run-key-config-validator] entry of the same date.

**Files touched:** none by this item.

**Routed to Captures:** [backspace-key-repeat], [claude-md-phase-ran].
