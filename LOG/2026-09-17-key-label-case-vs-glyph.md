# 88b95f2 — Key announcements settled to track the drawn glyph, after the code turned out to contradict its own doc comment

Recorded 2026-09-17 13:59.

The entry framed this as a product question: a key draws 'q' but announces 'Q' to a screen reader, and whether that is wrong was open — the announcement might reasonably be case-neutral, or might track the glyph.

Reading the code narrowed it to almost nothing. `accessibilityLabel` in `KeyboardPanel.kt` carries a doc comment saying "A letter or punctuation key announces its own glyph, which is what a reader expects", and then returns `key.label`, the config's uppercase form, while the drawing two lines above it calls `key.glyph(shiftState)`. The function states the intent and does something else, which is an unfinished implementation rather than an undecided design.

The product argument agreed and is what decided it. SPEC makes a point of the board showing which shift state it is in — shift lit differently for the two, every letter drawn as a capital — and a screen-reader user gets none of that. The label is the only channel where they could, so a description that never changes leaves them unable to know whether the next letter arrives capitalised.

Most of the work is a ripple rather than the fix: four instrumented tests find keys by their accessibility description, so changing what keys announce changes what those tests ask for. That is named in the item's own file list, so a build meets it inside its scope rather than being refused by the safety check partway.

Refused, with reasons carried: a case-neutral announcement, which removes the one channel telling a screen-reader user the board's state; leaving `key.label`, which disagrees with the drawn key in the state the board spends most of its time in; and putting the word "capital" into the label string, since how a capital is spoken is the screen reader's business.

**Queue changes:** [key-label-case-vs-glyph] rewritten with its design and moved from Unprocessed into Processed, cleared to run, placed before the `[user]` items; SPEC's accessibility bullet gained the sentence that a key announces what it will type.

**Work processed:** kept — [key-label-case-vs-glyph].
