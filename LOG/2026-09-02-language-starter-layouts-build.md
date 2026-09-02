# PLACEHOLDER — [language-starter-layouts] built: the Russian ЙЦУКЕН layout transcribed from FlorisBoard into its own config, manifest, preview and licence notice

Closed 2026-09-02 16:18. Eighth item of the run, and the one build this run could fully confirm.

The item had been rewritten twice that day and its method is now SPEC's: copy the standard phone keyboard for the language as far as Hexboard's layout allows, and improvise nothing. The user's one condition was no missing keys. The source is FlorisBoard's layout and popup files, Apache 2.0, read directly, giving 11-11-9 letter rows with Ё under Е and Ъ under Ь; the user's Gboard screenshot agreed.

The build generated the config from a script rather than retyping it, so the RARE and SYMBOLS panels and row 3 are copied from the English file byte for byte — a check confirmed both panels identical — and only the QWERTY rows are new: the eleven letters each on rows 0 and 1, cursor-left, nine letters and backspace on row 2, and Hexboard's own row 3 with enter added at col 10. Cursor-right has no slot, as the item decided. All four rows are eleven wide because the panel sizes keys from its widest row.

The no-missing-keys condition was made checkable and checked: 31 visible letters plus ё and ъ as long-presses cover all 33, no slot in the four-by-eleven grid is empty, the generator wrote the manifest without error and its drift check passes for both layouts. The preview page needed multi-row support, which it did not have — it had only ever drawn one row — so it gained an optional row per key and the Russian board as its first entry, and was opened in a browser to confirm the board draws. The README gained the FlorisBoard notice the research asked for.

Two things this does not do, recorded so they are not assumed: the Russian config is not copied into the app's assets (the copy task ships one file), and the reader's check [russian-layout-check] stays open.

Tick: done, confirmed: generator wrote the manifest and `--check` reports no drift for both layouts; a script check found 31 visible letters, ё and ъ as long-presses, no letter of the 33 missing, no empty slot in the 4×11 grid, and RARE and SYMBOLS byte-identical to the English config; the preview page renders the board.

**Files touched:** `resources/key-layout-ru.json` (new), `resources/key-manifest-ru.md` (new, generated), `planning/layout-preview.html`, `README.md`.

**Routed to Captures:** [ship-all-layout-configs], filed by the post-run rescan.
