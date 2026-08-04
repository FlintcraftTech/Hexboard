# Canonical key manifest

This document is the canonical source of truth for Hexboard's key inventory: which characters exist, which panel they live on, and where on that panel they sit. It is lifted verbatim from the manifest block at the top of `hexboard17.html`, which was the canonical copy until this file took over.

## The four inviolable rules

1. **NO KEY MAY BE LOST.** Every output character must appear in this manifest. Before shipping any build, run audit_keys.js and confirm the output set matches exactly.
2. **NO UNRESOLVED DUPLICATES.** Each output character should appear on exactly one panel. Cross-panel duplicates must be resolved before shipping — either remove one instance and fill the vacated slot with something new, or explicitly justify why both are needed (e.g. a convenience copy that is clearly labelled).
3. **NO SILENT CHANGES.** Any key move, addition, or removal must be reflected in this manifest and noted in the commit comment.
4. **EMPTY SLOTS ARE OPPORTUNITIES, NOT ACCEPTABLE GAPS.** If a duplicate is removed, the freed slot must be filled with a character that has no other home, agreed with the user first.

Rule 1 names `audit_keys.js`, which is a browser-prototype script. The wording is kept as written because it describes the prototype. The underlying principle — verify the shipped key set against this manifest before shipping — is what carries over to Android, where the mechanism will be different.

## Known copies to reconcile

This manifest is the source; the following are copies that must be brought back into line whenever it changes. Rule 3 points here.

- `hexboard17.html` — the browser prototype. Holds the manifest comment block, the `LK` / `SK` / `RAR_ROWS` key definitions, and `LP_MAP`.
- `planning/layout-preview.html` — the standing layout preview fixture. Holds its own `LAYOUTS` block and circle geometry.

A future Android build, and any key-audit check written for it, become consumers of this manifest too.

## Panel 0 — RARE

Three rows, ten keys each.

- **row0** (cols 0–9): `~` `` ` `` `|` `<` `>` `¬` `∞` `√` `∑` `π` — `{` `}` `[` `]` `\` were moved from here to SYMBOLS.
- **row1** (cols 0–9): `«` `»` `°` `€` `£` `¥` `©` `®` `§` `¶`
- **row2** (cols 1–10, offset +1 to align with the QWERTY middle row; col 10 is visible on the half-snap): `™` `…` `–` `—` `×` `÷` `±` `≠` `≤` `≥`

## Panel 1 — QWERTY

- **row0** (cols 0–9): `Q` `W` `E` `R` `T` `Y` `U` `I` `O` `P` — all insert keys.
- **row1** (cols 0–9): `A` `S` `D` `F` `G` `H` `J` `K` `L` `⌫` — `⌫` at col 9 is backspace, not an output character.
- **row2**: `⇤` cursor-left at col 0; `Z` `X` `C` `V` `B` `N` `M` at cols 1–7; `⇥` cursor-right at col 8; `↵` enter at col 9.
- **row3** (cols 0–9): `⇧` shift, `?`, `,`, `!`, `␣` space, `'`, `␣` space, `"`, `.`, `-` — two space bars, one per thumb.

Non-output keys on this panel: `⇧` shift, `⌫` backspace, `⇤` and `⇥` cursor movement, `↵` enter (outputs a newline).

## Panel 2 — SYMBOLS

Left block (cols 0–6) is punctuation; right block (cols 7–9) is numpad plus math. `·` marks an intentionally empty slot.

- **row0**: `[` `·` `@` `·` `#` `·` `$` `1` `2` `3`
- **row1**: `\` `*` `·` `(` `·` `)` `·` `4` `5` `6`
- **row2**: `%` `/` `^` `{` `&` `·` `_` `7` `8` `9`
- **row3**: `·` `]` `·` `}` `·` `:` `;` `0` `=` `+`

Freed slots filled with `[` `]` `{` `}` `\`, moved here from RARE for closer access. Removed from SYMBOLS, and now only on QWERTY row3: `?` `!` `'` `"` `-`.

## Long-press map — letters

- a → `à á â ä ã å æ`
- c → `ç`
- d → `ð`
- e → `è é ê ë ē`
- i → `ì í î ï ī`
- n → `ñ`
- o → `ò ó ô ö õ ø œ`
- s → `ß š`
- u → `ù ú û ü ū`
- y → `ý ÿ`
- z → `ž`

## Long-press map — punctuation

- `,` → `;` `…`
- `.` → `…` `:`
- `?` → `¿`
- `!` → `¡`
- `-` → `–` `—`
- `'` → `'` `'` `` ` ``
- `"` → `"` `"` `«` `»`
