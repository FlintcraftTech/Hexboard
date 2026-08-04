# [HASH] — resources/key-layout.json is now the single source of truth for key data, with key-manifest.md generated from it

Key data had been living as prose hand-copied across three files — the browser prototype, the layout preview fixture, and the manifest — which is exactly the silent drift manifest rule 3 forbids. This build collapses that to one machine-readable file and one generated view.

The direction of generation was the decision that mattered. JSON is the truth and the manifest is generated from it, not the reverse. A generated view cannot drift by construction; hand-authoring the manifest and parsing it into JSON would only have swapped the drift risk for a markdown-parser one. Neither file is hand-edited in practice, and Alex still reviews the manifest in the same readable form it had before.

Two things were deliberately kept out of the config. Geometry stays in Kotlin — zag offsets, circle radius, touch-target sizing, uppercase scaling, nearest-centre hit-testing — because it is the perceptual claim SPEC calls inviolable, and a configurable geometry would let a fork flatten the zag and stop Hexboard meaning anything. And the two HTML files stay non-consumers: the prototype is frozen as a historical reference, and the preview fixture keeps its own throwaway key data on purpose, so it can sketch arrangements that are not yet the real layout. Both now say so in a header comment, which matters because a reader would otherwise take a difference between them and the config as drift needing a fix.

The generator gained a `--check` mode that fails if the manifest on disk has drifted from the config, and it refuses to generate at all if any slot inside a row's declared bounds is neither a key nor a declared empty slot. Writing that constraint surfaced something the old prose manifest had hidden: the ten empty SYMBOLS slots had no recorded reason for being empty. Manifest rule 4 says an empty slot is an opportunity, not an acceptable gap — which is uncheckable if nobody wrote down why each gap exists. Each now carries a note, and the manifest prints them.

**Files touched:** `resources/key-layout.json` (created), `resources/key-manifest.md` (now generated), `scripts/generate-key-manifest.py` (created), `android/app/build.gradle.kts` (assets-copy task), `hexboard17.html` (frozen notice), `planning/layout-preview.html` (live-fixture notice)

**Routed to Captures:** none
