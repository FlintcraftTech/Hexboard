# [HASH] — Both candidate row-3 space arrangements put into the layout preview, with the parity arithmetic recorded beside them

QWERTY row 3 carries the two space bars, one per thumb, and today they sit at columns 4 and 6 — adjacent-but-one rather than symmetric, so the left-thumb key is further from the left thumb than the right one is from the right. The fix looked like a one-line change until planning found what actually constrains it: the zag rule keys on column parity, so even columns sit half a key higher than odd ones, and a pair genuinely symmetric about a ten-column row means columns `c` and `9−c`. Nine is odd, so one of any such pair is always even and the other always odd. Two space bars cannot be both edge-symmetric and at the same height. That is arithmetic rather than taste, and it is why this became a choice to look at rather than a change to make.

So the fixture now shows three rows: today's 4-and-6, candidate A at 2-and-6 (both even, matched height, but two columns from the left edge and three from the right), and candidate B at 3-and-6 (equal reach from each edge, at the cost of the two spaces sitting at different heights). Each is labelled with what it gives up, and the arithmetic that forces the trade-off is written into the block's comment, so the next person to open the file does not have to rediscover it.

`planning/layout-preview.html` is the standing fixture CLAUDE.md requires maintaining rather than replacing, and this is exactly its stated purpose. The rows it had been left on — a lowercase-versus-uppercase font-size comparison — were retired to make room, and they had drifted anyway: they compared a 0.90× uppercase scale while SPEC has since settled on 0.92×. A small `mkRow3` helper builds a row from a spaced string, so adding a further arrangement is one line.

Nothing here touches `resources/key-layout.json`. The character set is untouched in every candidate, and both candidates turn out to be swaps rather than vacancies — the key displaced by the moved space takes the freed column — so SPEC's rule about filling a freed slot never fires. Applying the winner is [left-space-relocation]; choosing between them is [row3-space-choice], which is genuinely the user's, since the trade-off is about how the keyboard feels under a thumb rather than anything computable.

**Files touched:**
- `planning/layout-preview.html` — the `LAYOUTS` block reworked to the three row-3 arrangements, with a `mkRow3` helper and the parity reasoning in a comment above it.

Built, and confirmed: the page was opened in a browser and its rendered key positions read back. All three rows draw ten keys, the zag holds with odd columns lower, candidate A's two spaces share a height, and candidate B's do not while sitting three columns from each edge.

**Routed to Captures:** none.
