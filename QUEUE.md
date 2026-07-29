# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Write the canonical key manifest to a reference doc and add a SPEC principle pointing at it [key-manifest-rules]
Captured by you. Create `resources/key-manifest.md` holding, verbatim from the top of hexboard17.html, the panel-by-panel key map (RARE / QWERTY / SYMBOLS with each character's row and column) and the two long-press maps (letters and punctuation), plus the four inviolable rules: no key may be lost, no unresolved duplicates, no silent changes, empty slots are opportunities not gaps. The doc declares itself the canonical source of truth and lists its known copies — `hexboard17.html` and `planning/layout-preview.html`'s `LAYOUTS`/geometry block — as consumers to reconcile whenever the manifest changes, so rule 3 has a concrete target. Add a short "canonical key manifest" principle to SPEC.md's Principles stating the four rules in product terms and pointing to the reference doc as the canonical key inventory. One nuance: rule 1 names `audit_keys.js`, a prototype-only script — keep that verbatim wording inside the reference doc (it describes the prototype), but word the SPEC principle around the underlying principle (verify the shipped key set matches the manifest before shipping), since the Android mechanism will differ. Files: `resources/key-manifest.md` (new), `SPEC.md`.

#### [user] Set up the Android Studio project — Claude guides, you drive [android-studio-setup]
Captured by you. Stand up the Kotlin / Jetpack Compose project in Android Studio. Claude guides you through it step by step so we don't scaffold the whole project by hand. This is the first concrete build step; the Android build is a fresh effort, not a port of the browser prototype.

--- Cleared to run above this line ---

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Captured by you. Once an Android build exists, install it on the Pixel 6 (wireless debugging) and confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Define a single machine-readable layout config as the source of truth for key data [layout-config-source]
Captured by you. Today the key data (panel key maps, long-press accent maps, geometry) lives as prose hand-copied across hexboard17.html, planning/layout-preview.html, and the forthcoming resources/key-manifest.md — three copies that can silently drift, the exact thing manifest rule 3 forbids. This item: define one machine-readable config (format TBD, e.g. JSON) as the single source of truth — the Android build consumes it, the key-audit check verifies against it, and the eventual variant editor reads and writes it. resources/key-manifest.md becomes the human-readable view of that config, or is generated from it. Unblocks [android-key-audit] and [variant-editor]. Needs design on format and on migrating existing consumers; sequencing likely around the first Android build.

#### Design an Android key-audit check to replace the prototype's audit_keys.js [android-key-audit]
Captured by you. The canonical key manifest's rule 1 — verify the shipped key set matches the manifest before shipping — names `audit_keys.js`, a browser-prototype-only script. The Android build will need its own equivalent: a check (automated test or tooling) confirming the Kotlin/Compose keyboard's actual output character set matches `resources/key-manifest.md` exactly, with no lost keys and no unresolved duplicates. Premature to build until an Android build exists; filed now so the guardrail isn't lost. Relates to [key-manifest-rules].

#### A contributor-facing layout editor for building language / key-set variants via fork [variant-editor]
Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. Prerequisite: [layout-config-source] — the editor is a downstream consumer of that single machine-readable config and can't sensibly exist before it. Far downstream of a first Android build; filed as a design thread, not near-term work.

