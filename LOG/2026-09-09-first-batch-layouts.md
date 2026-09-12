# f83f5d9 — [first-batch-layouts] five layouts transcribed by a machine reading FlorisBoard's own files, because the failure mode is mistyping

This session ran across 2026-09-05 and 2026-09-09.

Hexboard now ships seven layouts rather than two. The five are French, German, Spanish, Portuguese and Italian, chosen because all are Latin script, so nothing here touches the geometry, the zag rule or anything the Russian layout had to solve — and ordered so the two genuine re-orderings come first and prove the route.

**The transcription was performed by a one-off script reading FlorisBoard's layout and popup files, not by retyping them, and that is a deliberate reading of what the item asked for.** The item exists because layouts are not invented here: each is copied from an open-source keyboard's own data, so what can go wrong is a transcription error rather than a bad design. A machine reading the source files cannot mistype a letter. The script lived in the session scratchpad and is not committed; the five configs and their generated manifests are the artifact. Each config's `about` field names the exact FlorisBoard files it came from and the date they were read.

**Which German file, which the item said to settle by reading rather than guessing.** FlorisBoard carries both `german.json` and `qwertz.json`. `german.json` shows Ü, Ö, Ä and ß on the letter rows; `qwertz.json` is a bare ten-wide QWERTZ without them. What a German phone keyboard shows is the former, so that is the one transcribed, and the reading is written into the config rather than only into this record. Its rows are eleven wide because that layout is; nothing is hidden or dropped to fit ten columns, per SPEC.

**A correction to the item's own arithmetic on Spanish.** It said the middle row is eleven keys wide. That is true once backspace is counted — the letters alone are ten, a–l plus Ñ — and it is how the layout was built: Ñ takes the slot backspace holds on the English board, so backspace moves one column right and that row alone is eleven. The other three rows keep the English arrangement exactly, expressed through per-row bounds rather than by padding the panel.

Two smaller decisions the files forced. French's third row ends in an apostrophe in FlorisBoard, and Hexboard's row 3 already carries one, so keeping it would have been a duplicate output on one panel — barred by the manifest rules. Only the letters were taken from that row and the freed slots hold the structural keys. And Python's own `upper()` turns ß into the two letters SS, which is a word rather than a key label, so the one letter whose capital it cannot give is named explicitly: U+1E9E, the capital sharp s. An accent that already exists as its own key on a panel is stripped from every popup list, for the same duplicate rule.

Both test files were widened rather than duplicated. `KeyLayoutValidationTest` now validates every shipped config instead of only the English one, with the cross-panel duplicate rule scoped per config — the same character on the English and French home panels is two layouts offering the same letter, not a duplicate — and three new rules: language and order present, exactly one default across the set, and each config's generated manifest actually beside it. All seven pass.

**Files touched:** `resources/key-layout-fr.json`, `-de.json`, `-es.json`, `-pt.json`, `-it.json`; `resources/key-manifest-fr.md`, `-de.md`, `-es.md`, `-pt.md`, `-it.md`; `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt`; `android/app/src/androidTest/java/tech/flintcraft/hexboard/ShippedConfigsTest.kt`.

**Routed to Captures:** [russian-missing-cursor-right], [latin-panel-gaps].
