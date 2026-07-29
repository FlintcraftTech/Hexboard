# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### [user] Set up the Android Studio project — Claude guides, you drive [android-studio-setup]
Captured by you. Stand up the Kotlin / Jetpack Compose project in Android Studio. Claude guides you through it step by step so we don't scaffold the whole project by hand. This is the first concrete build step; the Android build is a fresh effort, not a port of the browser prototype.

--- Cleared to run above this line ---

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Captured by you. Once an Android build exists, install it on the Pixel 6 (wireless debugging) and confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Pull the canonical key manifest and its four inviolable rules into the project docs [key-manifest-rules]
Captured by you. Pull the canonical key manifest from the top of hexboard17.html's script section into the project docs, verbatim. The four rules — no lost keys, no unresolved duplicates, no silent changes, empty slots are opportunities. The old plan hand-off wanted these preserved as a guardrail for the Android build; they're currently only in the prototype file, not in SPEC or the queue. Decide during /plan where they should live (likely SPEC, as product truth).

#### Preview harness recorded as a standing planning fixture [preview-harness]
Captured by you. `planning/layout-preview.html` is a reusable layout-preview tool built this session. It renders any row config with the prototype's real zag + circle geometry (lifted verbatim from hexboard17.html); edit the `LAYOUTS` block at the top and reload. Standard fixture for previewing layout changes during /plan — use and maintain this rather than rebuilding one. Committed this session.
