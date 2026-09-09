<!-- GENERATED FILE — DO NOT HAND-EDIT.
     Layout: QWERTY (Spanish) (qwerty-es), language es.
     Generated from resources/key-layout-es.json by scripts/generate-key-manifest.py.
     Edit the JSON and re-run the script; any edit made here will be overwritten. -->

# Key manifest — QWERTY (Spanish)

This document is a readable view of one Hexboard layout's key inventory: which characters this layout offers, which panel they live on, and where on that panel they sit. The layout is QWERTY (Spanish), identified as `qwerty-es`. It is generated from `resources/key-layout-es.json`, which is the actual source of truth. Edit the JSON, then re-run `scripts/generate-key-manifest.py`.

Its scope is the three letter and symbol panels. The emoji panels reached by vertical swipe down are system-supplied content rather than a hand-curated inventory, so they are deliberately outside this manifest and the four rules below do not apply to them — their absence here is correct, not a gap.

Geometry is deliberately not here and not in the config: zag offsets, circle radius, touch-target sizing, uppercase scaling and nearest-centre hit-testing live in Kotlin, because they are the perceptual claim SPEC calls inviolable. A variant may change which characters you get, never how it feels to aim at them.

## The four inviolable rules

1. **NO KEY MAY BE LOST.** Every output character must appear in this manifest. Before shipping any build, run audit_keys.js and confirm the output set matches exactly.
2. **NO UNRESOLVED DUPLICATES.** Each output character should appear on exactly one panel. Cross-panel duplicates must be resolved before shipping — either remove one instance and fill the vacated slot with something new, or explicitly justify why both are needed (e.g. a convenience copy that is clearly labelled).
3. **NO SILENT CHANGES.** Any key move, addition, or removal must be reflected in this manifest and noted in the commit comment.
4. **EMPTY SLOTS ARE OPPORTUNITIES, NOT ACCEPTABLE GAPS.** If a duplicate is removed, the freed slot must be filled with a character that has no other home, agreed with the user first.

Rule 1 names `audit_keys.js`, which is a browser-prototype script. The wording is kept as written because it describes the prototype. The underlying principle — verify the shipped key set against this manifest before shipping — is what carries over to Android, where the mechanism will be different.

## Files that do not read this config

These hold their own copies of key data by design. They are not generated from the config and are not kept in step with it automatically.

- hexboard17.html — the browser prototype. Frozen: reference only, does not read this config.
- planning/layout-preview.html — the standing layout preview fixture. Does not read this config either; it stays hand-edited per CLAUDE.md.

## Panel 0 — RARE

Three rows, ten keys each.

- **row0** (cols 0–9): `~` `` ` `` `|` `<` `>` `¬` `∞` `√` `∑` `π` — `{` `}` `[` `]` `\` were moved from here to SYMBOLS.
- **row1** (cols 0–9): `«` `»` `°` `€` `£` `¥` `©` `®` `§` `¶`
- **row2** (cols 0–9): `™` `…` `–` `—` `×` `÷` `±` `≠` `≤` `≥` — Was offset +1 into cols 1-10, carried over from the prototype, whose board could half-snap sideways so a key past the right edge stayed reachable. Hexboard's board does not, so the eleventh column was an artefact that cost the whole panel about nine per cent of its key size.

## Panel 1 — QWERTY

The home panel. The middle row is eleven wide because Spanish adds Ñ to it; the other rows keep ten columns. Row 3 carries shift, common punctuation and the two space bars.

- **row0** (cols 0–9): `Q` `W` `E` `R` `T` `Y` `U` `I` `O` `P` — All insert keys: Q–P.
- **row1** (cols 0–10): `A` `S` `D` `F` `G` `H` `J` `K` `L` `Ñ` `⌫` — A–L plus Ñ at cols 0–9, and `⌫` backspace at col 10. Ñ takes the slot backspace holds on the English layout, so backspace moves one column right and this row alone is eleven wide.
- **row2** (cols 0–9): `⇤` `Z` `X` `C` `V` `B` `N` `M` `⇥` `↵` — `⇤` cursor-left at col 0; Z–M at cols 1–7; `⇥` cursor-right at col 8; `↵` enter at col 9. Unchanged from the English layout.
- **row3** (cols 0–9): `⇧` `?` `,` `!` `␣` `'` `␣` `"` `.` `-` — Full zag row: two space bars, one per thumb.

Non-output keys on this panel: `⌫` backspace, `⇤` cursor-left, `⇥` cursor-right, `↵` enter (outputs a newline), `⇧` shift.

## Panel 2 — SYMBOLS

Left block (cols 0–6) is punctuation; right block (cols 7–9) is numpad plus math.

`·` marks an intentionally empty slot.

- **row0** (cols 0–9): `[` `·` `@` `·` `#` `·` `$` `1` `2` `3`
- **row1** (cols 0–9): `\` `*` `·` `(` `·` `)` `·` `4` `5` `6`
- **row2** (cols 0–9): `%` `/` `^` `{` `&` `·` `_` `7` `8` `9`
- **row3** (cols 0–9): `·` `]` `·` `}` `·` `:` `;` `0` `=` `+`

Freed slots filled with `[` `]` `{` `}` `\`, moved here from RARE for closer access. Removed from SYMBOLS, and now only on QWERTY row3: `?` `!` `'` `"` `-`.

Why each empty slot is empty:

- row0 col1 — Spacing gap in the punctuation block — keeps the sparse left-hand symbols from reading as a dense grid. An opportunity slot per manifest rule 4.
- row0 col3 — Spacing gap in the punctuation block. An opportunity slot per manifest rule 4.
- row0 col5 — Spacing gap in the punctuation block. An opportunity slot per manifest rule 4.
- row1 col2 — Spacing gap in the punctuation block. An opportunity slot per manifest rule 4.
- row1 col4 — Spacing gap in the punctuation block, keeping `(` and `)` visually paired and separated. An opportunity slot per manifest rule 4.
- row1 col6 — Spacing gap in the punctuation block. An opportunity slot per manifest rule 4.
- row2 col5 — Spacing gap in the punctuation block. An opportunity slot per manifest rule 4.
- row3 col0 — Spacing gap on the bottom zag row. An opportunity slot per manifest rule 4.
- row3 col2 — Spacing gap on the bottom zag row, keeping `]` and `}` visually paired and separated. An opportunity slot per manifest rule 4.
- row3 col4 — Spacing gap on the bottom zag row. An opportunity slot per manifest rule 4.

## Long-press map — letters

- a → `á` `å` `ą` `æ` `ā` `ª` `à` `ä`
- c → `č` `ç` `ć`
- e → `é` `ē` `ę` `ė` `ë` `è` `ê`
- i → `í` `ī` `î` `į` `ì` `ï`
- n → `ń`
- o → `ó` `º` `ō` `ø` `œ` `õ` `ô` `ö`
- s → `ß`
- u → `ú` `ū` `ù` `ü` `û`

## Long-press map — punctuation

- `?` → `¿`
- `,` → `;` `…`
- `!` → `¡`
- `'` → `‘` `’` `` ` ``
- `"` → `“` `”` `«` `»`
- `.` → `…` `:`
- `-` → `–` `—`
