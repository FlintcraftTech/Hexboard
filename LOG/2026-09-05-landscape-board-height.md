# [HASH] — [landscape-board-height] created from arithmetic nobody had done, while designing the split it turns out to precede

Split out of [split-layout-wide-screens] on 2026-09-05; that item's record for the same date carries the finding.

The fix is a second constraint rather than a new rule: the radius is solved from width as it is today, then reduced where necessary so the board plus the row above it fits within half the available height, floored at `MIN_RADIUS`. A proportion of the height rather than a number of dp, which is how every other size in that file is derived.

What it must not do is change the portrait board, and the observation asserts exactly that — at a portrait viewport the solved radius must equal what the width-only solver returns today. A fix that quietly shrank the keys everyone already uses would be a worse bug than the one it repairs.
