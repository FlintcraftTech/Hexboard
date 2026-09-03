# Edge-to-edge enforcement, and what it does not say about input methods

Researched 2026-09-03, for `[board-under-navigation-bar]`, after Hexboard's bottom key row was seen drawn across Android's navigation bar on a Pixel 6.

## What is established

**Apps targeting SDK 35 or above are laid out edge-to-edge on Android 15 and above.** The window spans the full width and height of the display and draws behind the system bars — status bar, caption bar and navigation bar — and the application is responsible for insetting its own content. This is a behaviour change rather than an option; the opt-out available at 35 is gone by 36. Read from Android's own behaviour-changes page for apps targeting Android 15 (`developer.android.com/about/versions/15/behavior-changes-15`) and its edge-to-edge guides.

Hexboard's `android/app/build.gradle.kts` sets `targetSdk = 36`, read the same day.

**System-bar insets are the right ones for tappable content.** Android's guidance is explicit that views which are tappable and must not be visually obscured should use the system-bar insets rather than a narrower set. A keyboard is the extreme case of tappable content.

## What is NOT established, and this is the point of the file

**Android's documentation does not say whether the enforcement reaches an `InputMethodService` input view.** The behaviour-changes page was fetched and searched specifically for this: it discusses Activities throughout, lists the affected UI as the gesture-handle and three-button navigation bars, the status bar and the display cutout, and gives its examples in terms of Activities, Compose `Scaffold` and ordinary Views. Input methods, IMEs and keyboards appear nowhere in relation to edge-to-edge or to window insets.

The Compose insets material is written from the other side of the same problem — an *app* whose text field must stay clear of the keyboard — and so answers `imePadding()` questions rather than "how does a keyboard inset itself".

**So the platform-level question is open**, and nothing found in this search closes it. What is not open is the observation: Hexboard's board was drawn across the navigation bar on a real Pixel 6, and the app contains no inset handling of any kind.

## The consequence for the design

`Modifier.navigationBarsPadding()` pads by the navigation-bar inset, and that inset is **zero when there is no bar to avoid**. So the remedy is safe under either answer to the open question: where the IME window does extend behind the bar, the padding lifts the board clear; where it does not, the padding is zero and no height is spent. That property is why the fix could be settled without settling the platform question.

## Frame assessment

- **TIME RANGE** — covers Android 15 and 16 (API 35 and 36), which is the range that matters: the enforcement begins at 35 and Hexboard targets 36. Behaviour on API 26–34, which Hexboard also supports, was not researched, and the padding being zero-valued there makes it a non-issue rather than an answered question.
- **PEOPLE** — applies to anyone running Hexboard on a device with a navigation bar, which is the whole audience. It says nothing about devices with gesture navigation and no bar, where the inset is small or zero.
- **FRESHNESS** — the enforcement is a fixed platform behaviour tied to a target SDK level, so it does not drift. The undocumented half could change at any time by Google simply documenting it, which would settle the open question rather than invalidate anything here.
- **RISK IF WRONG** — low, and this is unusual. The remedy is self-cancelling: wrong about the platform question means the padding is zero and the board is unchanged. The residual risk is that the overlap has some *other* cause entirely, in which case the fix does nothing and the defect survives to be seen again on the phone — which the build's own on-device check would catch.
- **ALTERNATIVES** — padding by the wider safe-drawing inset was considered and not chosen: it also covers gesture areas and display cutouts, which would spend height on a keyboard whose argument is large keys, and the observed defect is specifically the navigation bar. Overriding `onComputeInsets` on the service was not researched and remains the route to look at if the padding proves insufficient.
