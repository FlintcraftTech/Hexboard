# QUEUE

## Red flags

Security, privacy, and data-exposure risks Claude has surfaced — kept at the top so they're the first thing seen each session. Each carries a state: open, resolved, or accepted. Empty until a risk comes up.

## Batches

Worked top to bottom. Each batch is one /next session. Subheadings name the kind of work (Build, Test, Audit).

### Build

- Set up the project in Android Studio (you guide me to do it so we don't have to scaffold the whole project by hand).

### Parked

## Deferred tests

Verification waiting on an event — not a parallel to-do list. A planned test lands here when it can't run in the session that planned it: the behaviour only goes live after the plugin updates, a person has to do something first, or an outside event hasn't happened yet. Each line records what to verify, what will confirm it, and two things about the wait — the deferral reason (why it waits: host-side / needs-user / external) and the runnability once the wait clears (who runs it then: Claude-runnable / user-run). Claude writes lines here and clears them; each /plan asks which waits have cleared and rolls the now-runnable ones into a test batch. You don't maintain this section.

## Captures

Captured outside /plan. Picked up and routed during the next /plan session.

- Left-space relocation: left space currently at row 3 col 4; should move to row 3 col 2 (currently the comma) so both spaces sit symmetric from the screen edges, one per thumb. Open: where does the comma go, and what fills the vacated col 4?
- Hit-test verification: nearest-centre is the intended strategy but should be confirmed against accessibility services (TalkBack, switch access) before being cemented.
- Capitalisation glyph sizing: uppercase currently shares font size with lowercase, so capitals look bigger. Confirm whether that's desired or whether glyph metrics should be normalised.
- Key manifest and its four inviolable rules: pull the canonical key manifest from the top of hexboard17.html's script section into the project docs, verbatim. The four rules — no lost keys, no unresolved duplicates, no silent changes, empty slots are opportunities. The old plan hand-off wanted these preserved as a guardrail for the Android build; they're currently only in the prototype file, not in SPEC or the queue. Decide during /plan where they should live (likely SPEC, as product truth).

---

### Parked
