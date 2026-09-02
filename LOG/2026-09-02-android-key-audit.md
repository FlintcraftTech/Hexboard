# PLACEHOLDER — [android-key-audit] built: a Compose UI test that walks every key and accent in the config and proves each reaches the screen and emits its own character

Closed 2026-09-02 16:18. Seventh item of the run.

Captured by the user; reframed at planning once the config became authoritative. The app's key set cannot disagree with the config, so what is worth checking is wiring: a key that renders nothing, renders in the wrong slot, or emits the wrong character. The test does three things per key on every panel — asserts an accessibility node with the key's label is centred where the geometry puts its row and column, taps that centre and asserts the emitted output and action match the config, and for every accent holds past the phone's long-press timeout, slides to that accent's cell and asserts it was emitted. Failures name the character and its panel position. This is manifest rule 1 made mechanical.

Two things the build settled for itself: a Compose UI test under `androidTest` rather than a plain instrumented one, because the panel is Compose and the test rule drives its clock; and one test switching a state-held panel rather than one test per panel, because the test rule allows content to be set once. To let the test aim at an accent cell, the popup geometry and the accessibility label in `KeyboardPanel.kt` went from private to internal.

Running it is the user's, in Android Studio, since Gradle cannot run here; the tick says so. It is not a `[user]` item of its own because the run of it rides the next Android Studio session the install walkthrough already opens.

Tick: done, UNCONFIRMED: the test is written but not run; it runs on the Pixel 6 from Android Studio (right-click `KeyConfigUiTest.kt`, Run), which is yours since Gradle cannot run here.

**Files touched:** `android/app/src/androidTest/java/tech/flintcraft/hexboard/KeyConfigUiTest.kt` (new), `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

**Routed to Captures:** none.
