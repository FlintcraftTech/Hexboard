# [HASH] — `hexboard-editor.html` moved into `planning/` and recorded in the README as superseded prior art

A third HTML file had been sitting at the repo root since the project was adopted, tracked, untouched, and named in nothing — not SPEC, not CLAUDE.md, not the README, not any queue item. With the repo about to go public, the root becomes permanent reading for anyone who arrives, so an unexplained file there is a question every visitor has to answer for themselves.

Its fate was settled at planning by reading it rather than guessing: it is a working drag-and-drop key-arrangement editor on the real zag geometry, with panel tabs and structural keys locked, and its export button is what dates it — it emits JavaScript source fragments for pasting into the prototype, which is where key data lived before `resources/key-layout.json` existed. So it is superseded. Deleting it was the obvious move and it lost, because the interaction design is the expensive half of a contributor-facing layout editor and it is already built; [variant-editor] would otherwise start from nothing. It is moved rather than removed, on Claude's recommendation and the user's agreement.

The move used `git mv`, so the file's history follows it and the go-public record stays readable at its new path.

The README gained a paragraph saying what the page is, why it is superseded, and why it is kept — enough that a visitor meets it as a deliberate decision rather than as clutter. One sentence above it needed adjusting as a consequence: the prototype was described as "the only part of Hexboard you can actually use today", which stopped being true the moment a second usable page was named, so it now reads "the only part of Hexboard you can actually type on today".

**Files touched:**
- `hexboard-editor.html` → `planning/hexboard-editor.html` — moved with `git mv`.
- `README.md` — new paragraph on the moved editor; one sentence reworded above it.

Built, and confirmed: the file is in `planning/`, absent from the root, recorded by git as a rename rather than a delete-plus-add, and the README's new link path resolves against the moved file.

**Routed to Captures:** none.
