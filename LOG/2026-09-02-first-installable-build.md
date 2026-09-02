# PLACEHOLDER — [first-installable-build] built: Hexboard registered as an Android input method, hosting the Compose board in a service that owns its own lifecycle

Closed 2026-09-02 16:18. Built in the twelve-item run of that afternoon, first of the run because five held items name it as their blocker.

The item's threshold was Android accepting Hexboard as a keyboard the user can pick, not keys on screen — the rendering half shipped earlier and was seen on the Pixel 6 that morning. The design was already in the item: an `InputMethodService` whose input view is a `ComposeView` hosting the panel, a manifest `<service>` with `BIND_INPUT_METHOD` and the `android.view.im` descriptor, a `method.xml` with one subtype, picker strings, and a button on the app screen opening the system's input-method settings so switching Hexboard on is findable.

The known trap the item wrote down was honoured: Compose inside an input method service has no lifecycle, view-model or saved-state owners, so the service is all three itself and installs them on the Compose view and on the window's decor view, since Compose looks them up from the root. Lifecycle state follows the service's own callbacks (created, started on view creation, resumed on input start).

One choice the item left open and the build made: shift. The service does one-shot shift — tapping it uppercases the next inserted character, then clears — because a keyboard with no capitals at all is not typeable and the old app-screen comment already said shift was settled with the service. Whether that is right, and whether double-tap gives caps lock as the prototype does, is filed as [shift-behaviour] rather than decided here. Backspace, enter and the cursor keys are sent as key events rather than text edits, so backspace respects a selection and enter performs a single-line field's action.

Later in the same run [panel-switch-gestures] changed the service's input view from one panel to the whole board; that one-line change is recorded on that item's entry.

Tick: done, UNCONFIRMED: needs a Gradle build in Android Studio, then the install walkthrough [install-and-enable-on-pixel]; the compile risk to watch is whether `androidx.savedstate` and `lifecycle-viewmodel` resolve transitively (they are not declared directly in `libs.versions.toml`).

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` (new), `android/app/src/main/AndroidManifest.xml`, `android/app/src/main/res/xml/method.xml` (new), `android/app/src/main/res/values/strings.xml`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`.

**Routed to Captures:** [shift-behaviour], [declare-savedstate-viewmodel-deps] — both filed by the post-run rescan.
