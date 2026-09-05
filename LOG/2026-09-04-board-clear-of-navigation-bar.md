# 987cb5c — Input view padded by the navigation-bar inset, so row 3 stops sharing a band with the system buttons

Written 2026-09-04 at 15:53.

Raised by the user on 2026-09-03, from the first time Hexboard drew as a real keyboard rather than a test screen: row 3 — shift, `?`, `,`, `!`, the two space bars, `'`, `"`, `.`, `-` — sat across the navigation bar with the lower part of each circle behind it. That is worse than a cosmetic overlap, because the navigation bar takes touches in its own region: a tap aimed at the lower half of a row 3 key reaches the system instead of the keyboard, and the space bars are worst hit, sitting in the middle where the three system buttons are.

The cause was read from the code rather than guessed. There was no inset handling anywhere in the app, and `android/app/build.gradle.kts` sets `targetSdk = 36`, where apps are laid out edge-to-edge behind the system bars with the opt-out gone.

What the research could not settle is worth keeping, because it is the one soft spot in the diagnosis: Android's behaviour-changes page discusses Activities throughout and says nothing about `InputMethodService` input views either way, so whether the enforcement formally reaches an IME window is undocumented as far as that search reached. The fix survives the gap, which is what let it be settled anyway — `navigationBarsPadding()` pads by an inset that is zero where there is no bar, so it lifts the board clear if the window does extend behind the bar and costs nothing if it does not. Correct under either answer.

The reserved space is added rather than taken. An input method's window is sized to its view, so padding the view makes it taller and the keys keep their size — the right direction for a keyboard whose argument is large keys. The background is applied before the padding so it fills the bar's band rather than leaving a bare strip.

Refused: padding by the wider safe-drawing inset, which also covers gesture areas and display cutouts and would spend height beyond the defect seen; and shrinking the board to fit, which takes the fix out of the keys, the one place this project will not take it from. Overriding `onComputeInsets` on the service remains the thing to look at if the padding proves insufficient.

**Confirmed:** nothing.

**Unconfirmed, transcribed from the tick:** nothing here can compile or run Android tests. `NavigationBarInsetTest` is Android Studio's to run on the Pixel 6, and the row-3 keys should be tapped by hand as well, since the test reads bounds rather than where touches actually go.

**Files touched:** `HexboardImeService.kt` (`navigationBarsPadding()` on the board's modifier chain after the background, plus import and comment, 7 lines), `NavigationBarInsetTest.kt` (created, 2 tests, 127 lines).

**Routed to Captures:** none from this item.

**Depth:** short.

Cites `workshop/resources/research/edge-to-edge-and-ime-input-views.md`. Interacts with [suggestion-strip], which grew the same view at the top in this same run: the board now carries both, so its total height is up by both amounts and that combined height is what to look at on the phone rather than either alone.
