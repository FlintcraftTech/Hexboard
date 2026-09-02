# 8f705a3 — [backspace-key-repeat] built: backspace and the cursor keys repeat while held, at timings read from the phone's own settings

Closed 2026-09-02 16:18. Third item of the run, second of the four `KeyboardPanel.kt` items, hanging a hold timer on the press state [key-press-feedback] introduced.

The item was designed with the user that morning after the first real run showed a held delete removing one character and stopping. Two decisions of theirs bind the build and were followed: which keys repeat is keyed off the key's action in code, not a config field, so every layout gets it with no schema change; and the timing is the phone's, never a number of ours — the hold delay must scale with Android's accessibility touch-and-hold setting, the user's requirement from setting up phones for people slower than themselves, which defeated the fixed values AOSP's keyboard uses.

The item asked the build to confirm the two platform calls before relying on them, and it did: the AOSP source of `ViewConfiguration` shows `getLongPressTimeout()` reading `Settings.Secure.LONG_PRESS_TIMEOUT`, which is the setting that accessibility control changes, and `getKeyRepeatDelay()` reading the system's repeat interval. Both are read at press time, so a settings change applies to the next press without a restart.

One thing the build settled by itself: a repeating key acts on the press and then repeats, where every other key acts on release. That is how physical and on-screen keyboards behave, and it is what "re-fires" in the item's own wording implies.

Tick: done, UNCONFIRMED: needs the app run on the Pixel 6, including the Touch & hold delay check the item names; the API premise (getLongPressTimeout reads Settings.Secure.LONG_PRESS_TIMEOUT, getKeyRepeatDelay exists) was confirmed from the AOSP ViewConfiguration source this session.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

**Routed to Captures:** none.
