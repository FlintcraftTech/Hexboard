# 3838e52 — [board-under-navigation-bar] diagnosed to edge-to-edge enforcement at targetSdk 36, and fixed with a padding that is correct whether or not the diagnosis is

Recorded 2026-09-04 at 17:00. This session ran across 2026-09-03 and 2026-09-04.

The capture reported, from the first time Hexboard drew as a real keyboard, that row 3 sits in the same band as Android's back, home and recents buttons — and that this is worse than a cosmetic overlap, because the navigation bar takes touches in its own region, so a tap aimed at the lower half of a row 3 key reaches the system rather than the keyboard. The two space bars are worst affected, sitting where the three system buttons are. The capture said plainly that it was reasoned from a screenshot and that nothing in the Kotlin had been read.

Reading it produced a firmer diagnosis. A grep across all five Kotlin files for window insets, navigation bars or safe-drawing padding returns nothing but an unrelated comment; `HexboardImeService.onCreateInputView` builds a bare `ComposeView`, installs the three owners and hands it back with `fillMaxWidth()` and a background colour and nothing else. And `android/app/build.gradle.kts` sets `targetSdk = 36`. Apps targeting 35 and above are laid out edge-to-edge behind the system bars on Android 15 and above, with the opt-out gone by 36 — so the board is handed the full height including the bar's band and fills it. That also explains why nothing looked wrong in `MainActivity`'s test screen, which is an ordinary activity.

**The research could not close the question, and the item says so.** Android's behaviour-changes page discusses Activities throughout and says nothing about `InputMethodService` input views in relation to edge-to-edge or insets, either way — it was fetched specifically to check. The Compose material found is written from the other side of the problem, for apps whose text fields must stay clear of a keyboard. So whether the enforcement formally reaches an IME window is undocumented as far as that search reached. What is not in doubt is the symptom on the phone and the absence of any inset handling to explain it away.

**What let the fix be settled anyway is a property of the remedy rather than of the evidence.** `Modifier.navigationBarsPadding()` pads by the navigation-bar inset, and that inset is zero where there is no bar to avoid. So if the IME window does extend behind the bar the padding lifts the board clear, and if it does not the padding is zero and no height is spent. Correct under either answer to the open question — which is why the item could clear while the platform question stays open, and why its observation asserts that a zero inset leaves the board's height unchanged.

It also answered the design question the capture left open. An input method's window is sized to its view, so padding the view makes it taller and the window grows to fit: the keys keep their size and the keyboard occupies slightly more screen. That is the right direction for a keyboard arguing for large keys, and it is the same mechanism [suggestion-strip] uses at the top of the same view, so a board carrying both is taller by both amounts.

Padding by the wider safe-drawing inset was refused: it also covers gesture areas and display cutouts, spending height beyond the defect actually seen.

**Queue changes:** [board-clear-of-navigation-bar] created and placed at the top of the cleared region — an environment fault that makes the most-pressed keys partly unusable outranks the visual work behind it. The research is filed as `workshop/resources/research/edge-to-edge-and-ime-input-views.md` with its index line, carrying the five-criteria frame assessment and stating the undocumented half plainly.

**Work processed:** deleted [board-under-navigation-bar], its content having moved into the new item and the research file.

**Advisory:** not needed — the close's recommendation names no single item to start from.
