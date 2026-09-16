# 770b778 — [spec-emoji-swipe-wording] SPEC now says what the finger does, and the same phrase in CLAUDE.md is filed as its own item

Session of 2026-09-12, 12:54.

SPEC said the emoji panels are "reached by vertical swipe down" and the finger goes up. Both descriptions were true of different things, which is why it survived: the panels sit below the letters in the vertical stack, so a reader moves *down the stack*, and moving down a stack means dragging the content up. SPEC described where they sit and read as an instruction for the hand — and a walkthrough written from it on 2026-09-09 asked for the wrong gesture, which is how it was found.

No code changes: `VerticalPager` holds the letters at page 0 and the emoji at page 1, the arrangement the prototype has and the user confirmed by hand.

**The horizontal phrase was checked in the same pass, as the entry asked.** "Three letter panels reached by horizontal swipe" names no direction, so it could not mislead the way the vertical one did — but it also never said that RARE sits left of QWERTY and SYMBOLS right, which is what a reader or a walkthrough needs. The rewrite says both directions and what each reaches.

**The same wrong phrase is in `CLAUDE.md`, and a planning session may not write it** — the same reason [settings-steps-name-a-search] exists as its own item. That half is filed as [claude-md-swipe-wording] and cleared to run.

**Queue changes:** SPEC's layout bullet rewritten in both axes; [claude-md-swipe-wording] filed and cleared; [spec-emoji-swipe-wording] deleted.

**Work processed:** kept — [claude-md-swipe-wording]; deleted — [spec-emoji-swipe-wording].
