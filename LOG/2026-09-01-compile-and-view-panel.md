# [HASH] — The compile-and-look step given a real device story, pairing the Pixel 6 over Wi-Fi, and cleared to run

This item existed because the Compose keyboard shipped on 2026-08-21 with nothing having compiled it. Its walkthrough said "with any device or emulator selected", which quietly assumed a device target that is not set up — so the step could not actually be performed as written. Asked which of three routes matched the real setup, the user chose the Pixel 6 over Wi-Fi, over an emulator and over a compile-only Build → Make Project.

The compile-only option is recorded on the item as the rejected one with its reason: it proves the Kotlin compiles and proves nothing about whether Gson finds the config in the app's assets at runtime or whether the panel draws, which is two thirds of what the item exists to answer.

The walkthrough was rewritten from four steps to seven, splitting the pairing so no step carries more than three instructions, and each step gained the thing to look for. The claim its final step makes about what appears on screen was checked against `MainActivity.kt` rather than assumed — a `Text` reading "Tap the keys" above `KeyboardPanel`, with each press appending its output character.

Two ordering facts were written into both items rather than one: the pairing here is the same pairing as steps 1–2 of [install-and-enable-on-pixel], so that item now starts at its step 3 once this is done. Placed after [run-key-config-validator] in the cleared region because both are Android Studio jobs done in one sitting.

**Queue changes:** [compile-and-view-panel] moved from Unprocessed to Processed, cleared to run, after [run-key-config-validator]. [install-and-enable-on-pixel] amended to name the shared pairing.

**Work processed:** kept — [compile-and-view-panel].

**Advisory:** filed — see the chat-level entry for this session.
