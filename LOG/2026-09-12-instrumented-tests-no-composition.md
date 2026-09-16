# 770b778 — [instrumented-tests-no-composition] tagged freeform and placed first, with one of its three hypotheses eliminated at the desk

Session of 2026-09-12, 12:54.

Twelve of twenty-five instrumented tests fail on the Pixel 6, every one that renders the board, with `No compose hierarchies found in the app`. The board itself is fine — the keyboard was typed on by hand the same morning — so the fault is in how the tests stand it up.

**One hypothesis was eliminated by reading the files.** The entry suspected a helper measuring the root before `setContent`, which produces exactly that message. It does not happen: in `EmojiPanelsUiTest` and `StripLayoutTest` every failing test calls `showBoard()` first, and `showBoard` sets the content and waits for idle before anything measures; `KeyConfigUiTest` has the same shape. The two survivors both need a device: whether `createComposeRule()` suits a board stood up outside an activity, and whether something inside `HexboardBoard` throws under the harness after the first frame — the height read through `LocalConfiguration` and the height-bounded radius solver being the most recently changed things on that path.

**Tagged `[freeform]` after re-reading both uncommon markers.** It names work that characteristically cannot run inside an unattended build run, which is this: each remaining hypothesis needs the user to press Run in Android Studio and report, and Gradle, Java and adb are all absent from this machine. `Runs alone` was considered and rejected — it marks work the method does build, in an isolated run.

**Placed first in the cleared region**, because four cleared items change what a rendering test would see. Sorting the harness first means they are built against tests that can be trusted; sorting it last ships each of them with the blind spot this session has been working around.

**Queue changes:** [instrumented-tests-no-composition] tagged `[freeform]`, cleared to run, and placed at the top of Processed.

**Work processed:** kept — [instrumented-tests-no-composition].
