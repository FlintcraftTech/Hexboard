# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Define resources/key-layout.json as the source of truth for key data, with the manifest generated from it [layout-config-source]
Captured by you. Key data currently lives as prose hand-copied across `hexboard17.html`, `planning/layout-preview.html`, and `resources/key-manifest.md` — three copies that can silently drift, which manifest rule 3 forbids.

The build: create `resources/key-layout.json` holding the key inventory — every character, its panel, its row and column, its label, and its long-press accent list — as the single source of truth. Add `scripts/generate-key-manifest.py`, which regenerates `resources/key-manifest.md` from that JSON; the manifest keeps its current readable shape and its four inviolable rules verbatim, and gains a header line saying it is generated and must not be hand-edited. Add a Gradle task in `android/` copying the JSON into the app's assets at build time, so no second copy is checked in. Add a "frozen — reference only, does not consume the config" note to `hexboard17.html`. Add a separate note to `planning/layout-preview.html` saying it does not read the config either — but that file stays the live layout fixture per CLAUDE.md and is not frozen; it is edited and reloaded to preview row configurations as before.

Decided in this session: the JSON is the truth and the manifest is generated, not the reverse — a generated view cannot drift by construction, whereas a hand-authored manifest parsed into JSON would just swap the old drift risk for a markdown-parser one. Neither file is hand-edited in practice; Alex reviews the generated manifest in the same readable form it has today, and the eventual [variant-editor] writes JSON rather than markdown tables. `hexboard17.html` is confirmed frozen as a historical design reference, which is what reduced this from three consumers to one.

Also decided: the config carries the key inventory only — geometry stays in Kotlin (zag offsets, circle radius, touch-target-larger-than-circle, 0.92× uppercase scaling, nearest-centre hit-testing). The reason is the perceptual wedge SPEC calls inviolable: if geometry were configurable, a fork could flatten the zag or square the keys and Hexboard would stop meaning anything. A variant may change which letters you get, never how it feels to aim at them. Accepted cost: a future per-language geometry change (a fourth row, say) is a code change, not a config change — judged the right trade, since that's a deliberate design decision anyway.

Unblocks [android-key-audit], which verifies the shipped key set against this config, and [variant-editor], which reads and writes it.

#### Validate key-layout.json against the manifest rules as a unit test [key-config-validator]
Captured by you. Split out of [android-key-audit] during planning. The config being the single source of truth means the app's key set can't drift from it — but it says nothing about whether the config itself is sound, and that's where the manifest's inviolable rules actually bite.

The build: a JVM unit test in the Android project that loads `resources/key-layout.json` and asserts it holds. Checks: no character appears on more than one panel unless the entry carries an explicit justification field; no two keys share a row and column within a panel; every row/column falls inside the panel's declared bounds; every long-press accent list is well-formed and its characters are distinct; and every declared slot in a panel is either filled or carries a note explaining why it's empty. A failure names the offending character and panel, so the message is actionable without reading the JSON.

This covers manifest rules 2 (no unresolved duplicates) and 4 (empty slots are opportunities, not acceptable gaps), and enforces the "no silent changes" rule by failing the build when a change breaks them. It needs no keyboard and no device — it runs against the config file alone, so it can be built as soon as [layout-config-source] lands.

#### Add the PolyForm Noncommercial 1.0.0 licence to the repo [add-licence]
Captured by you. Decided in the /plan session of 2026-08-04, after researching the options — the full comparison is at `resources/research/licence-options.md`.

The build: add `LICENSE` at the repo root containing the verbatim text of PolyForm Noncommercial 1.0.0, fetched from the PolyForm project rather than reproduced from memory, with Alex named as licensor. Add a short licence section to `README.md` (creating it if absent) saying the project is source-available under that licence, that forks to build Hexboard for other languages are welcome, and that commercial use is not permitted.

Why this licence: Alex's intent is that people may read the source and fork it to build Hexboard in another language, but get no general right to copy it. That's a purpose-limited derivative right, and nothing standard grants exactly that. PolyForm Noncommercial is the closest fit — a hobbyist building a German Hexboard is squarely permitted, and anyone selling Hexboard is not. It's plain-language, lawyer-drafted, and recognised, which a custom licence would not be.

Known and accepted gap: the licence also permits noncommercial forks that weren't the intent, so someone could publish a free rival keyboard built on this code. Judged acceptable — the realistic threat is commercial appropriation. Also accepted: source-available terms exclude the project from some open-source ecosystems and can deter contributors.

Independent of the repo going public — the licence should be in place first either way.

#### [audit] Review the full git history for anything that shouldn't go public [git-history-audit]
Red flag · State: cleared
Captured by you. Split out of [licence-and-go-public] during planning, because it must complete before the repo goes public and nothing else in that item gated it.

The audit: read the repo's entire commit history — not just the current files — for content that shouldn't leave the machine. Look for absolute machine paths containing the user's name, email addresses and account identifiers, anything personal in commit messages or in the planning docs' history, and any credentials or tokens. Report findings as fresh captures naming each occurrence and the commits it appears in.

If findings appear, cleaning them is a separate build — rewriting history is destructive and needs its own decision, so this pass reports and does not fix.

Red flag: making the repo public exposes everything in its git history, not just the current files, and this repo's history includes machine paths and could include personal detail. The flag is cleared by design rather than by acceptance: the risk is removed from the go-public path by making this audit a prerequisite of it, and [repo-go-public] records that gate as its lift-condition. Nothing is exposed until the audit has run and any findings are dealt with.

--- Cleared to run above this line ---

#### Verify every key in the config actually renders and emits its character [android-key-audit]
Captured by you. Rewritten during planning as the second half of a split; the validator half is [key-config-validator].

Original framing was to confirm the Kotlin keyboard's character set matches the manifest. The [layout-config-source] decision removes that need: the app reads `resources/key-layout.json` directly, so its key set *is* the config and the two cannot disagree. What remains worth checking is wiring — a key correctly declared in the config can still render nothing, render in the wrong slot, or emit the wrong character.

The build: an instrumented or Compose UI test that walks every key in the config, asserts a key node exists at the expected panel, row and column, and asserts that activating it emits exactly the character the config declares. Long-press accents get the same treatment — each accent in a key's list is reachable and emits its own character. Failures name the character and its panel position.

This is what manifest rule 1 — verify the shipped key set before shipping — actually means once the config is authoritative: not a comparison of two lists, but proof that the one list reaches the screen intact.

Lift-condition: cleared to run once the Compose keyboard renders keys from the config, since there is nothing to drive until keys exist on screen.

#### [user] Flip the Hexboard repo from private to public on GitHub [repo-go-public]
Captured by you. Split out of [licence-and-go-public] during planning. Only you can do this — it's an account action on github.com that Claude can't perform.

The walkthrough, once the lift-condition below has cleared:
1. Open `https://github.com/FlintCraftTech/Hexboard` in a browser, signed in as the account that owns it.
2. Go to **Settings** (the tab across the top of the repo, not your account settings).
3. Scroll to the bottom of that page, to the red-bordered **Danger Zone** section.
4. Find **Change repository visibility** and click **Change visibility**.
5. Choose **Make public**, then confirm. GitHub asks you to type the repository name — `FlintCraftTech/Hexboard` — to prove it's deliberate.
6. Reload the repo while signed out, or in a private browsing window, to confirm it's genuinely visible.

Lift-condition: cleared to run once [add-licence] has landed *and* [git-history-audit] has run with any findings dealt with. Both gates matter — the licence should be in place before anyone can read the code, and the history audit is the red-flag mitigation. Going public is effectively irreversible: once the history is public it can be cloned, so taking the repo private again does not un-expose it.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Captured by you. Once an Android build exists, install it on the Pixel 6 (wireless debugging) and confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Write a proper README before the repo goes public [public-readme]
Surfaced in the wind-down re-scan of the /plan session on 2026-08-04. [add-licence] creates a README only as a home for a licence section, which is not enough for a repo strangers will land on. A public README should say what Hexboard is, state the perceptual claim that distinguishes it from other tessellation keyboards, make clear it is an in-progress Android build rather than a shipping app, and spell out plainly what a fork may and may not do under PolyForm Noncommercial. Should be ordered before [repo-go-public]. Filed after `dca16ac`.

#### Consider a legal read of the licence choice before going public [licence-legal-read]
Surfaced in the wind-down re-scan of the /plan session on 2026-08-04. The PolyForm Noncommercial choice was made from a research summary written by Claude, which is not legal advice. The risk is low — the licence is standard and lawyer-drafted, and it was not modified — but the decision is effectively irreversible once forks exist under it, so a short professional read is worth weighing. Filed so the option is consciously taken or declined rather than never raised. Filed after `dca16ac`.

#### A contributor-facing layout editor for building language / key-set variants via fork [variant-editor]
Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. Prerequisite: [layout-config-source] — the editor is a downstream consumer of that single machine-readable config and can't sensibly exist before it. Far downstream of a first Android build; filed as a design thread, not near-term work.

Carries an open question relocated from [licence-and-go-public] during planning: once language forks exist, how do they stay in step with canonical Hexboard as it changes? Alex has flagged this as beyond him. It's an upstream/downstream design question — fork-and-cherry-pick, a shared config, or contribution-back terms written into the licence itself — and it interacts with [layout-config-source], since a shared machine-readable key config is one way forks track upstream without merging code. Not answerable until the config lands and a real fork exists.

Sharpened in the /plan session of 2026-08-04, then deliberately deferred rather than designed. Two things are now settled that narrow it: the editor's target format is `resources/key-layout.json`, no longer TBD; and because the config carries the key inventory only, with geometry staying in Kotlin, the editor's scope is hard-bounded to key data and structurally cannot touch the perceptual wedge. What still blocks design is that none of its subjects exist yet — no keyboard, so nothing to vary; no fork, so the fork-sync question has no real case to reason about; and the SPEC-level scope decision underneath it (whether Hexboard becomes a platform for variants rather than one opinionated keyboard) is better made with a working keyboard in hand than in the abstract.

