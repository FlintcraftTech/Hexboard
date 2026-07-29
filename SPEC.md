# SPEC — Hexboard

## What this is

Hexboard (working name) is a virtual keyboard for Android. It's for people who are frustrated with the traditional keyboard layout, but it resembles that layout as closely as possible so the learning curve stays low.

## Who it's for

QWERTY users who want a better on-screen typing experience without having to relearn where the letters are. Familiar enough to adopt without a full relearning event — "open minds, but not so far open the brain falls out."

## How it works

The keyboard sacrifices row-straightness and uses circular keys in a neat hexagonal configuration. Rows zig-zag — odd columns sit half a key lower than even columns — and that trade-off is what yields greater key size than competing QWERTY-style virtual keyboards. Larger keys are the whole point.

The core differentiator is a **perceptual claim**: users feel more confident aiming at circular targets than at hexagons with visible corners. They aim more centrally, contact patches land closer to the key centre, and accuracy improves. This is what distinguishes Hexboard from existing tessellation-based keyboards (Typewise, MessagEase, Thumb-Key). The differentiator is settled — not to be re-litigated. (Note: the claim is perceptual, not functional — Hexboard does not claim that hex keyboards mis-route taps.)

Layout details carried from the prototype:
- Otherwise plain QWERTY. Three letter panels (RARE / QWERTY / SYMBOLS) reached by horizontal swipe, with QWERTY as home. Emoji panels reached by vertical swipe down.
- Two space bars in row 3, one per thumb — a deliberate compromise that preserves layout familiarity at the cost of one mild oddity.
- The visible circle is intentionally smaller than the touch target — a bigger targeting zone, and less crowded aesthetics.
- Long-press accents cover diacritics for common letters plus alternate punctuation forms.
- Uppercase glyphs render at 0.92× the lowercase font size, so capitals no longer overfill the circle relative to lowercase. This is a scaling factor, not a shared font size; chosen by eye against the layout preview.
- Intended hit-testing for Android: nearest-centre (Voronoi) assignment — full coverage, no gaps, no z-order tie-breaks. (The prototype's overlapping square boxes are incidental, not the design.)
- Accessibility: each key must expose its own accessibility node — an individually-focusable element with a text label and correct bounds — so screen readers and switch access work. Accessibility services largely bypass raw-touch routing, so these nodes are a separate requirement from the nearest-centre hit-testing above; both must be correct.

Target stack: Kotlin / Jetpack Compose. The Android build is a fresh effort, not a line-by-line port. The browser prototype (`hexboard17.html`) is the canonical reference for layout, gestures, and key inventory.

## Project docs

Three project docs structure each project:
- `SPEC.md` — product truth. What the project is, who it's for, how it works.
- `QUEUE.md` — work batches and captured ideas.
- `LOG/` — per-session records of what was built, tested, and decided.

## Principles

- **The perceptual wedge is inviolable.** Any suggestion that compromises it (e.g. "just use rounded squares") should be questioned, not quietly accepted.
- **Predictive text is planned but deferred.** The eventual version won't fail every time a typo has the wrong first letter, because it will search the letters backwards too. Held until after the first working model — get the keyboard right first.
- **Familiarity over radical departure.** Keep it close enough to QWERTY that users adopt it without relearning.
- Single dark theme is enough for v0; theming and customisation come later.
- Out of scope for early iterations: IME service polish (settings screen, language switching) — type first, polish after.
