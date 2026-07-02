# Hexboard — Planning Hand-off

**Status:** extended planning phase, no implementation yet.
**Target platform:** Android (Kotlin / Jetpack Compose).
**Working name:** Hexboard.
**Existing prototype:** `hexboard17.html` (browser proof of concept). The Android build is a fresh effort, not a port — the prototype's job is to demonstrate the layout, gestures, and key inventory, not to be ported line-by-line.

---

## What it is

A QWERTY keyboard for Android where the keys are circles arranged in a hexagonal tessellation pattern. Rows zig-zag (odd columns sit half a key lower than even columns). This trade-off — sacrificing row straightness — is what allows the circles to be larger than they would be in a traditional rectangular grid. Larger keys are the whole point.

---

## The load-bearing claim

The reason Hexboard exists rather than reusing an existing tessellated keyboard (Typewise, MessagEase, Thumb-Key) is a **perceptual** one:

> Users feel more confident aiming at circular targets than at hexagons with visible corners. They aim more centrally, contact patches land closer to the key centre, accuracy improves.

This is a testable claim. It is the differentiator. If user research shows it doesn't hold or the effect is too small to matter, the project loses its wedge.

Note: the *functional* version of this argument — that hex keyboards actually mis-route taps — is not the claim being made. Don't let it creep into docs or marketing.

---

## Design decisions already made

**Visible vs. touch target.** The visible circle is intentionally smaller than the touch target. Two reasons:
1. Targeting: gives the user a bigger zone, including the gaps between visible circles.
2. Aesthetics: keys look crowded if visible circles almost kiss.

**Hit-testing strategy.** The HTML prototype uses overlapping square bounding boxes resolved by DOM order — this is incidental, not designed. For Android, **nearest-centre (Voronoi) assignment** should be the default. Full coverage of the keyboard surface, no gaps, no z-order tie-breaks.

**Layout philosophy.** "Open minds, but not so far open the brain falls out." Familiar enough that QWERTY users adopt it without a full relearning event. Radical departures avoided unless necessary.

**Two space bars.** Row 3 has two space keys, one reachable by each thumb. This is a deliberate compromise — a single traditional space bar would force more disruption elsewhere in the layout. Two spaces preserves the rest of the layout's familiarity at the cost of one mild oddity.

**Panels.** Three letter panels (RARE / QWERTY / SYMBOLS) navigated by horizontal swipe; QWERTY is home. Five emoji panels reached by vertical swipe down.

**Long-press accents.** Map covers diacritics for common letters plus alternate forms for punctuation. Implemented in prototype; carries forward.

---

## Open questions / pending decisions

**Left-space relocation.** Left space currently lives at row 3 col 4. It should move to row 3 col 2 (currently the comma) so the two spaces sit at roughly symmetric distances from the screen edges, one per thumb. Open: where does the comma go, and what (if anything) fills the now-vacant col 4? This is a layout puzzle for early planning.

**Hit-test verification.** Nearest-centre is the intended strategy but should be confirmed against accessibility services (TalkBack, switch access) before being cemented.

**Capitalisation glyph sizing.** Currently uppercase letters share font size with lowercase, so capitals look bigger than lowercase. Confirm whether this is desired or whether glyph metrics should be normalised.

---

## Seeding the guardrail docs

**CLAUDE.md** — should include the perceptual wedge above as the project's reason for existing. Any suggestion that compromises it (e.g. "let's just use rounded squares") should be questioned.

**MANIFEST.md** — port the canonical key manifest comment block from the top of `hexboard17.html`'s script section. Preserve the four inviolable rules verbatim (no lost keys, no unresolved duplicates, no silent changes, empty slots are opportunities).

**UX.md** — the perceptual claim, the visible-vs-touch-target decision, the nearest-centre hit-testing decision, the two-spaces compromise, the layout philosophy quote.

**BACKLOG.md** — the open questions above. Whatever else surfaces as planning continues.

---

## Out of scope for early iterations

- IME service polish (settings screen, language switching). Type first, polish after.
- Theming / customisation — single dark theme is enough for v0.
- Predictive text / autocorrect — explicitly deferred. The wedge is targeting accuracy, not prediction.

---

## Competitive landscape — already reviewed

Typewise, MessagEase, Thumb-Key and other tessellation-based keyboards have been used by the project owner. They share the tessellation idea but use polygonal keys with visible corners. **Do not re-litigate this in the next session.** The differentiator decision is settled. The perceptual claim above is what makes Hexboard distinct.
