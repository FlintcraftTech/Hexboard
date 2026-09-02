# [HASH] — Language and ordering fields added to the key config, with a validator that refuses a config missing them

The layout picker groups layouts by language and orders them within a language, and the config could express neither. Until now the language was only implied by an id reading `qwerty-en`, which is a convention rather than data — and every layout written before these fields existed would have had to be edited afterwards, which is why this went first.

`resources/key-layout.json` moves from schemaVersion 2 to 3, gaining `language` (a BCP 47 tag, `"en"` here, required) and `order` (an integer giving the position within that language, lower first, with a missing value sorting last). Its `about` prose gained a sentence on each, as it already carried for `isDefault`.

The generator gained real validation rather than only reading the new fields. `scripts/generate-key-manifest.py` now runs a `validate()` before it generates anything, refusing a missing or blank `language` and a non-integer `order`, and the manifest header names the language alongside the layout. The validation was exercised against mutated copies of the config held in memory — missing language, blank language and a non-integer order each rejected, an omitted order accepted, the real config accepted — which is what makes the refusals a tested claim rather than an intended one.

A display name for the language stays deliberately absent, and the reason is worth keeping because the field looks obviously useful: Android resolves a tag with `Locale.forLanguageTag(...).displayLanguage` and a browser with `Intl.DisplayNames`, both naming the language in the *reader's* own language. A hand-written name would arrive in twenty spellings and be wrong for everyone but its author.

No Kotlin changed. Gson ignores JSON fields the data classes do not declare, so `KeyLayout.kt` parses the config unchanged — the same reasoning [variant-schema] recorded, and untested here for the same reason it was untested there.

**Files touched:** `resources/key-layout.json`, `scripts/generate-key-manifest.py`, `resources/key-manifest.md`.

**Routed to Captures:** none.

Tick: done, confirmed — `--check` reports the manifest up to date, the header names the language, and `git diff` on the manifest shows one changed line, so no key data moved.

Depth: short.

Rule gate: not needed — no rule was authored or amended.
