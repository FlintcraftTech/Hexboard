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

## What FlorisBoard actually offers, read 2026-09-05

The character-layout directory holds **76 files**. Filed here because the list is
what any future language decision starts from, and re-reading a GitHub directory
listing is not free.

Latin-script national layouts: `azerty` and `canadian_french`; `german` and
`german2`; `qwertz`; `swiss_french`, `swiss_german`, `swiss_italian`; `spanish`;
`danish`, `norwegian`, `swedish_finnish`, `icelandic`, `faroese`; `estonian`,
`hungarian`, `slovenian`, `catalan` (and `catalan_accents`); `turkish_f` and
`turkish_q`; `azerbaijani`; `kurdish`, `kurdish_kurmanci`, `kurdish_standard`;
`igbo`; `esperanto` (and `esperanto_with_hx`).

Non-Latin: `greek`; `hebrew`; `jcuken_russian`, `jcuken_ukrainian`,
`jcuken_interslavic`; `serbian_cyrillic` and `serbian_latin`; `bulgarian_bds` and
`bulgarian_phonetic`; `rusyn`, `rusyn_us`; `udmurt_compact`, `udmurt_extended`;
`eastern_armenian`, `western_armenian`, `armenian_alt_phonetic`;
`georgian_standard`; `arabic`; `persian`, `persian2`, `persian3`; `urdu_phonetic`;
`hindi_in`; `bengali_unijoy`; `tamil`; `thai_kedmanee`, `thai_manoonchai`;
`korean`, `korean_phonetic`; `jis`; `warang_citi`.

Alternative English arrangements, which are a different kind of thing entirely —
not a language, a preference: `dvorak` (and `dvorak_de`, `dvorak_es`,
`dvorak_se`), `colemak`, `colemak_dh`, `colemak_dhm`, `workman`, `bepo`, `bone`,
`neo2`, `halmak`, `nalmy`, `diktor`, `sangaline`, `ipa`.

**Several common languages have no layout file**, because they use the shared
`qwerty.json` and differ only in their long-press accents — Portuguese and
Italian among them, whose popup mappings `pt.json`, `pt-BR.json` and `it.json`
exist while no layout file does. So there are two levels of cheapness: a
different key order, and the same key order with different accents.

The popup-mapping directory holds per-language files including `ar`, `ast`, `bg`,
`bn-unijoy`, `ca`, `cjk`, `ckb`, `cs`, `da`, `de`, `el`, `en`, `eo`, `es`, `et`,
`fa`, `fi`, `fo`, `fr`, `hi-IN`, `hr`, `hu`, `hy`, `id`, `is`, `it`, `iw`, `ka-std`,
`kab`, `ko`, `ku`, `lt`, `lv`, `nb`, `nn`, `pl`, `pt`, `pt-BR`, `ro`, `ru`, `rue`,
`sk`, `sl-SI`, `sr`, `sv`, `tr`, `udm`, `uk`, `ur-PK`, `vi-VN`, plus `default`.

## Which languages are cheap, and which are not — the tiering agreed 2026-09-05

Ordering by speaker numbers gets this wrong, because the cost of a language is
decided by whether Hexboard's config model can express its script at all.

1. **Latin-script variants.** The same 26 letters in a different order plus a few
   extra keys, with long-press accents already built. Pure transcription, and the
   subset sharing `qwerty.json` is cheaper still.
2. **Alphabetic non-Latin scripts of similar size.** Greek at 24 letters, Hebrew
   at 22, the further Cyrillic languages. Transcription plus a check that the
   alphabet fits the row widths — the work Russian cost a session to settle.
   Hebrew adds right-to-left text, which is rendering work rather than key
   inventory.
3. **Scripts needing work of their own.** Arabic has contextual letter forms, the
   Indic scripts have conjuncts, Thai has 44 consonants and stacked vowels. Each
   is its own piece of work rather than a config file.
4. **Out of scope entirely: Chinese, Japanese and Korean.** They do not select
   characters from a key inventory; they need candidate conversion, which is an
   engine rather than a layout. Decided rather than discovered, and now stated in
   SPEC — note that FlorisBoard nonetheless ships `korean` and `jis` files, so
   their presence in the list above is not an invitation.

## Symbol panels are not covered by this source, and CLDR is where the quotes come from

**Added 2026-09-12, for `[russian-panel-gaps]` and `[latin-panel-gaps]`.** This finding
settles where a language's *letters* come from and says nothing about its symbol panel.
Read on 2026-09-12, FlorisBoard organises symbol layouts by script and region rather than
by language: the directory holds `western.json`, `western_additional_symbols.json`,
`western_samsung.json`, `eastern.json`, `cjk.json`, `persian.json`, `armenian.json` and
`ipa.json`, plus `neo2.json` for that layout. There is no Russian symbol layout, and
French, German, Spanish, Portuguese and Italian would all share `western.json`. So the
source that settles the letters cannot settle which quotation marks a language uses, and
transcription is not available for this question.

**Unicode's CLDR carries it instead.** CLDR's `delimiters` element gives every locale a
`quotationStart`, `quotationEnd`, `alternateQuotationStart` and `alternateQuotationEnd` —
four characters, which is exactly the four quote slots a symbols panel has to fill, across
574 locales. French returns « and » with a curly alternate pair; German uses the low-high
„ pairing. Read from CLDR's own LDML specification and from library documentation quoting
the data on 2026-09-12; the specific per-locale values were not each read, so a build
takes them from the data rather than from this file.

That puts the quote slots on the same footing as the emoji list: published Unicode data
rather than a judgment made here, under a licence already read in
`unicode-data-file-licence.md`. The other six symbol slots — `• ← → ½ ¢ ≈` — are
punctuation rather than orthography and carry across unchanged.

## What copying means here

The transcription copies which letters sit where, into Hexboard's own config format. That is the factual content of a layout rather than FlorisBoard's expression of it; even so, Apache 2.0 permits copying the files outright with attribution, so the safe course is to name FlorisBoard and the file path in the config's `about` field and carry the licence notice in the repository. Not a legal opinion — a reason to attribute rather than to worry.

## Frame assessment

- **TIME RANGE** — layouts are stable over years; FlorisBoard's file could change, so the path and the read date are recorded and a later transcription re-reads the file.
- **PEOPLE** — this is data written by FlorisBoard's contributors, many of them native typists of the languages concerned (the Rusyn layout, for instance, was contributed with its popups). Better provenance than English-language explainers, and still not a substitute for a reader's check before a layout ships.
- **FRESHNESS** — high; the files were read this day.
- **RISK IF WRONG** — a wrong transcription ships a broken layout. This line originally named the reader check in [russian-layout-check] as the mitigation; that requirement was removed from SPEC on 2026-09-04 and the item deleted with it, so the mitigation is now the in-app report route built by [layout-error-report] — errors are caught by the people using the layout rather than before it ships. Corrected 2026-09-05. No data exposure either way.
- **ALTERNATIVES** — the user's Gboard screenshots (agree with the file for Russian, but cost the user visual work and cannot show hidden letters); AOSP (harder to read); the GPL forks (licence). Each weighed above.

## Sources

- FlorisBoard licence: https://raw.githubusercontent.com/florisboard/florisboard/main/LICENSE
- FlorisBoard Russian layout: https://github.com/florisboard/florisboard/blob/main/app/src/main/assets/ime/keyboard/org.florisboard.layouts/layouts/characters/jcuken_russian.json
- FlorisBoard Russian popups: https://github.com/florisboard/florisboard/blob/main/app/src/main/assets/ime/keyboard/org.florisboard.localization/popupMappings/ru.json
- AOSP LatinIME rows: https://android.googlesource.com/platform/packages/inputmethods/LatinIME/+/refs/heads/main/java/res/xml/
- FlorisBoard's popup convention, from its contributing notes: https://github.com/florisboard/florisboard/commit/b30e3b809357a425effdb4f12c901657dcf6abf5
