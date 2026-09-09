# [HASH] — [layout-error-report] a report route built so it structurally cannot carry what you typed, and proved on the handset

This session ran across 2026-09-05 and 2026-09-09.

This item is what SPEC accepted in place of holding every layout back until a native reader had confirmed it — a rule the user judged unrealistic, and which the record had already agreed was the bottleneck. Layouts are transcribed from another keyboard's own data rather than designed here, so what goes wrong is a transcription error, a letter in the wrong slot: the thing a user notices at once and a one-line report fixes.

**It carried a red flag, raised by Claude at planning and designed out, and this build is where the design became code.** A keyboard that sends reports is one careless keystroke from being a keyboard that sends your text. Three of the four answers are structural rather than promises. `ProblemReport.compose` takes five values — layout id, app version, Android version, device model, and what the person typed into the box — and there is no parameter through which an input connection, a clipboard or any key history could reach it; `ProblemReportTest` asserts that by reflection, so "it cannot carry what you typed" is a property of the signature rather than a claim about it. Nothing is sent by the app: it opens the person's own mail app with the text prefilled, so Hexboard makes no network request and needs no internet permission. The composed text is shown in full before it goes, so the person can check the first claim themselves. And the address is not in the repository — it is read from the gitignored `android/local.properties` into a build config field, and where that field is empty the entry does not appear at all. `git check-ignore` was re-run at build time rather than trusted.

The flag is carried into this record as **cleared**.

One departure from the item's own wording: it said it would read `KeyLayout.kt` without changing it, but a report names the layout id and `KeyLayout` had no id field — the config carries `id`, `name`, `language`, `order` and `isDefault` and the data class declared none of them. All five were added. The file was already in the run's scope for [layout-switching], which needs the same fields.

**Proved on the phone on 2026-09-09.** With the address set in `local.properties` on the user's instruction, the report composed as `Layout: qwerty-en`, `Hexboard: 1.0`, `Android: 17`, `Device: Pixel 6` and the typed description and nothing else; the button enabled only once the box had text; the mail app opened with that text in the body and the address in the To field, unsent. That is the half of the observation that could only ever be made on a handset.

Getting there turned up something the item could not have anticipated: the app screen also draws Hexboard's own preview board, which has no input connection and appends to a scratch line, so typing on it does not fill the report box. Filed as [app-screen-two-keyboards]. Nothing was wrong with that board until a screen with a real field grew underneath it.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/ProblemReport.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/KeyLayout.kt`, `android/app/build.gradle.kts`, `android/app/src/test/java/tech/flintcraft/hexboard/ProblemReportTest.kt`, `android/local.properties` (untracked).

**Routed to Captures:** [app-screen-two-keyboards].
