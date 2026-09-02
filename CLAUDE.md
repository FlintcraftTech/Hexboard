# CLAUDE.md

<!-- ▼ PLUGIN-MANAGED — do not edit between these markers. Refreshed by /setup's migration, which reports what it replaces. Your own rules belong below the end marker. ▼ -->

This project uses the Throughliner method.

## Project docs

- **SPEC.md** — product truth. What it is, who it's for, how it works.
- **QUEUE.md** — your work, in two sections. **Processed** work is vetted and ready to build, worked top-to-bottom; a `--- Cleared to run above this line ---` line marks how far down is greenlit (below it is decided but not ready yet). **Unprocessed** work is captured ideas and tasks not yet fully processed. Each piece of work is one line: a `#### ` heading naming the work, with a `[slug]` at the end of that heading line and a short rationale beneath it, plus a `captured by you` credit on items you personally raised (anything else is unmarked — Claude is the default author). A work item can carry a leading flavor tag: none means a build (Claude edits files), `[audit]` a review pass (Claude reads and reports), `[user]` a step only you can run. A security or privacy risk Claude surfaces becomes a work item carrying a `Red flag · State: cleared/uncleared` marker — surfaced first each session while uncleared, until it's cleared (either designed out, or you're told the risk plainly and choose to accept it).
- **LOG/** — session records: what was built, tested, decided. One file per session entry, plus index.md one-line summaries naming each entry file.
- **FAQ/** — workflow FAQ. Index loaded at session start; details in FAQ/faq.md.
- **INBOX/** — messages from other projects you run. Anything waiting is mentioned at session start; handled messages move to `INBOX/archive/`. A message going out to another project is always shown to you for approval first.

## Workflow

- `/setup` — scaffold project docs (done if you're reading this).
- `/plan` — queue management, captures, design questions.
- `/next` — execute the top piece of ready work (a build or an audit, by its flavor tag). It can work several cleared pieces of work back-to-back, top-down, stopping at the readiness line or when something genuinely needs you.
- `/rescan` — read back over the conversation and file anything decided or noticed but never written down. Run it whenever, as often as you like; it only looks back as far as the last time you ran it. It files things and leaves the deciding to /plan.
- `/done` — record, update docs, commit.

## Rules for Claude

- SPEC.md is a normal doc, and there's no separate spec-edit step — but **it changes during planning, not during a build**. When a planning decision changes what SPEC says, Claude writes that sentence in the /plan session, with you there. A build never writes product truth: if a build discovers SPEC is missing a sentence, it writes the sentence down as a new queue item and carries on, so SPEC is behind by at most that one sentence until your next planning session — and it's behind visibly, as an item you can see, rather than quietly. The reason is that the session which made a choice shouldn't be the one that certifies it as product truth. A large SPEC rework is ordinary build work that lists SPEC.md among its files, and the safety check still blocks a build from editing SPEC unless it does. Note spec issues for /plan as they come up.

## Visibility

<!-- Set at setup: which repository holds this project's documents, and whether
     they are published anywhere. Left blank until that is settled. -->

Visibility: this folder's own git repository holds the project's documents. `SPEC.md`, `QUEUE.md`, `CLAUDE.md` and `LOG/` are tracked and go public with the repo; `FAQ/` and `INBOX/` stay out of it.

## Language

Language: English

<!-- ▲ PLUGIN-MANAGED — do not edit above this line. ▲ -->

## Project rules

Hexboard (working name) is an Android keyboard app being designed around circular keys arranged in a hexagonal tessellation. Larger keys are achieved by zig-zagging rows — odd columns sit half a key lower than even columns — sacrificing row straightness in exchange for bigger touch targets. The layout is otherwise plain QWERTY, with three letter panels (RARE / QWERTY / SYMBOLS) reached by horizontal swipe and emoji panels reached by vertical swipe down.

The core differentiator is a perceptual claim: users feel more confident aiming at circles than at hexagons with visible corners, aim more centrally, and tap more accurately as a result. This distinguishes Hexboard from existing tessellation-based keyboards (Typewise, MessagEase, Thumb-Key), all of which the project owner has used and rejected. Don't re-litigate this — the differentiator is settled.

Current phase: Android implementation has begun, and none of it has been run. `MainActivity.kt`, `KeyboardPanel.kt` and `KeyLayout.kt` exist, and the Compose keyboard already draws its keys from the config. Nothing written has been compiled or seen on a device — [compile-and-view-panel] is the queued step that changes that — so treat the existing Kotlin as real code of unknown correctness, and keep designing before coding rather than rushing new work into the app. Target stack is Kotlin / Jetpack Compose. A working browser prototype (hexboard17.html) demonstrates the layout, gestures, and key inventory and should be treated as the canonical reference for current design intent.

Layout previews during planning use `planning/layout-preview.html` — the standing fixture. Edit its `LAYOUTS` block (real zag + circle geometry, lifted from hexboard17.html) and reload to preview any row config. Maintain this rather than rebuilding a previewer.

The repo is going public, and the planning record goes with it — `LOG/`, `QUEUE.md`, `CLAUDE.md` and `SPEC.md` are tracked, so anyone will be able to read them. This was decided deliberately on 2026-08-06: the planning record is a worked example of the Throughliner method, which is itself public, so the demonstration value is the point. `FAQ/` is the exception — it stays on disk so both Alex and Claude can consult how the method works mid-session, but it is untracked and does not ship with the repo.

Write the tracked docs accordingly. Be candid about the work; never candid about the person. An unresolved question is framed as a property of the question — what makes it hard, what it depends on — never as a limitation of Alex's. Never write personal contact details into a tracked file, including in text *about* those details.
