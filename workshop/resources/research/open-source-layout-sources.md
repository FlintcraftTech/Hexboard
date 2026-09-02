# Open-source keyboard layout data Hexboard can transcribe from

Researched 2026-09-02, for [language-starter-layouts] and every layout after it. The question: is there a machine-readable, permissively licensed source for each language's standard phone keyboard layout, including which letters hide behind a long-press, so that a layout is transcribed from a file rather than from a screenshot anyone has to read by eye?

## Answer: FlorisBoard's layout files, Apache 2.0

FlorisBoard (github.com/florisboard/florisboard) is licensed Apache 2.0 — read from its `LICENSE` file on 2026-09-02. Its layouts are JSON, one file per layout, and its long-press popups are a separate JSON per language. Both read directly.

**The Russian layout**, `app/src/main/assets/ime/keyboard/org.florisboard.layouts/layouts/characters/jcuken_russian.json`, read in full on 2026-09-02:

- Row 1: й ц у к е н г ш щ з х — 11
- Row 2: ф ы в а п р о л д ж э — 11
- Row 3: я ч с м и т ь б ю — 9

That is exactly the arrangement the user's Gboard screenshot showed the same day, so the two sources agree.

**The Russian popups**, `app/src/main/assets/ime/keyboard/org.florisboard.localization/popupMappings/ru.json`, read in full on 2026-09-02: under `all`, **е → ё** and **ь → ъ**, and nothing else letter-wise (the rest is punctuation on the comma key and `.ru`-style domain suffixes in URL fields). So the two hidden Russian letters are Ё under Е and Ъ under Ь, confirmed from the file rather than guessed.

The repository also carries `serbian_cyrillic.json` in the same folder; other languages were not enumerated.

## AOSP LatinIME, also Apache 2.0, but harder to read

Android's own open-source keyboard keeps its Russian rows in `java/res/xml/rowkeys_east_slavic1.xml`, `…2.xml`, `…3.xml` (read on 2026-09-02, via android.googlesource.com). The letters match FlorisBoard's, but several keys and every long-press list are indirections (`!text/morekeys_cyrillic_ie`, `!text/keyspec_east_slavic_row1_9`) resolved per locale in a generated Java table, so reading a complete layout means chasing three or four files. Usable as a cross-check; FlorisBoard is the one to transcribe from.

## Not chosen

HeliBoard and OpenBoard, both forks of AOSP LatinIME, carry the same data under GPL v3, which sits awkwardly beside Hexboard's PolyForm Noncommercial licence for copied data files. Not needed while an Apache source has the same content.

## What copying means here

The transcription copies which letters sit where, into Hexboard's own config format. That is the factual content of a layout rather than FlorisBoard's expression of it; even so, Apache 2.0 permits copying the files outright with attribution, so the safe course is to name FlorisBoard and the file path in the config's `about` field and carry the licence notice in the repository. Not a legal opinion — a reason to attribute rather than to worry.

## Frame assessment

- **TIME RANGE** — layouts are stable over years; FlorisBoard's file could change, so the path and the read date are recorded and a later transcription re-reads the file.
- **PEOPLE** — this is data written by FlorisBoard's contributors, many of them native typists of the languages concerned (the Rusyn layout, for instance, was contributed with its popups). Better provenance than English-language explainers, and still not a substitute for a reader's check before a layout ships.
- **FRESHNESS** — high; the files were read this day.
- **RISK IF WRONG** — a wrong transcription ships a broken layout; mitigated by the reader check in [russian-layout-check]. No data exposure.
- **ALTERNATIVES** — the user's Gboard screenshots (agree with the file for Russian, but cost the user visual work and cannot show hidden letters); AOSP (harder to read); the GPL forks (licence). Each weighed above.

## Sources

- FlorisBoard licence: https://raw.githubusercontent.com/florisboard/florisboard/main/LICENSE
- FlorisBoard Russian layout: https://github.com/florisboard/florisboard/blob/main/app/src/main/assets/ime/keyboard/org.florisboard.layouts/layouts/characters/jcuken_russian.json
- FlorisBoard Russian popups: https://github.com/florisboard/florisboard/blob/main/app/src/main/assets/ime/keyboard/org.florisboard.localization/popupMappings/ru.json
- AOSP LatinIME rows: https://android.googlesource.com/platform/packages/inputmethods/LatinIME/+/refs/heads/main/java/res/xml/
- FlorisBoard's popup convention, from its contributing notes: https://github.com/florisboard/florisboard/commit/b30e3b809357a425effdb4f12c901657dcf6abf5
