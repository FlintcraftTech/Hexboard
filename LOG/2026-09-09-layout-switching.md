# f83f5d9 — [layout-switching] the picker that made the Russian layout reachable at all, discovered from the assets rather than listed in code

This session ran across 2026-09-05 and 2026-09-09.

The Russian config had shipped into the app's assets since [ship-all-layout-configs] and nothing could select it, so it was present and inert. This is the picker that changes that, and its placement was settled long before the build: the app's own settings, not the keyboard surface, because horizontal swipe already means "change panel" and a picker there would have had to find a gesture the layout has not already spent.

The list is discovered rather than declared. `LayoutCatalogue` enumerates the assets, keeps every `key-layout*.json`, and reads each one's `id`, `name`, `language` and `order` — the two last added at schema version 3 precisely so a picker could read them. That is what keeps SPEC's promise that a further language is one new file and nothing else; a hard-coded list in Kotlin would have made it two changes. Ordering within a language is each config's declared `order`, with a config that declares none sorting last. Ordering by how often a layout gets used was refused outright and the reason is worth keeping: it is the only true measure of popularity, and it would be the first thing in Hexboard to report what a user does back to a server, against every other feature's posture. Nothing counts anything.

`LayoutPreference` stores the chosen id and resolves it against what is actually installed, falling back to the config marked `isDefault` — a stored id naming a layout removed between versions must not leave the keyboard with nothing to draw.

The item named two open questions and both were answered in the build as designed: `AssetManager.list("")` enumerates, and the choice is an ordinary shared preference that the settings screen and the input method service both read, being one application.

**One departure, and the item's own stated intent is what forced it.** The item said the service loads the chosen layout when the input view is created. The build also re-reads it in `onStartInputView`, because the system reuses one input view across appearances — so `onCreateInputView` may not run again after a change in settings, and the item's intent, that a change takes effect the next time the keyboard appears, would not otherwise hold. `keyLayout` was made observable so the board redraws when it changes.

**Proved on the phone on 2026-09-09**: all seven layouts present under their language headings with English selected. The picker also revealed something SPEC never settled — the language *groups* are sorted by BCP 47 tag, so they read German, English, Spanish, French, Italian, Portuguese, Russian, which is alphabetical by a code the reader never sees. SPEC settled the order within a language and never between them. Filed as [picker-language-order].

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/LayoutCatalogue.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/LayoutPreference.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt`, `android/app/src/test/java/tech/flintcraft/hexboard/LayoutCatalogueTest.kt`, `android/app/src/androidTest/java/tech/flintcraft/hexboard/LayoutSwitchingUiTest.kt`.

**Routed to Captures:** [picker-language-order], [spec-out-of-scope-stale].
