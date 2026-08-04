# [HASH] — Canonical key manifest written to resources/key-manifest.md, with a SPEC principle governing the key inventory

The key inventory — which characters exist, which panel they sit on, where in the grid — lived only as a comment block at the top of `hexboard17.html`. That made a prototype file the source of truth for something the Android build, the layout preview, and any future key-audit check all need. Moving it to `resources/key-manifest.md` gives it a home that outlives the prototype.

The four inviolable rules are kept verbatim as written in the prototype, including rule 1's reference to `audit_keys.js`. That script is browser-prototype-only, so the doc keeps the original wording but adds a note that the principle carrying to Android is "verify the shipped key set against the manifest before shipping" — the mechanism there will be different. The SPEC principle is worded around that principle rather than the script, for the same reason.

The doc names its known copies — `hexboard17.html` and `planning/layout-preview.html` — as consumers to reconcile whenever it changes, which gives rule 3 (no silent changes) a concrete target instead of an instruction with nowhere to point. That reconcile-by-hand arrangement is deliberately an interim step: the queued `[layout-config-source]` item proposes one machine-readable config all three consume, which would remove the hand-copying entirely.

**Files touched:** `resources/key-manifest.md` (created — four rules, consumers list, RARE/QWERTY/SYMBOLS panel maps with row and column, both long-press maps); `SPEC.md` (added "The key inventory is governed by a canonical manifest" to Principles).
**Routed to Captures:** none.
