# 37384e4 — The top-row accent overlap answered by the row above the keys, which restores the bar the prototype clamped into

Holding a top-row letter draws its accent alternatives over the keys either side of it. The cause is exact: the accent row is drawn inside the board's bounds and clamped to the top edge, which is what `hexboard17.html` does — but the prototype had a bar above its keys to clamp *into*, and the board had nothing above row 0.

[suggestion-strip], designed earlier in the same session, restores that bar: a row one vertical step tall, slightly more than a key's diameter, so an accent row for a top-row key fits in it. This is not a workaround but the arrangement the prototype's clamping was written against.

Three alternatives lost, each with its reason. Drawing the row below the key on the top row would put accents above on three rows and below on one, and merely moves the overlap onto row 1. Drawing it in a window that may extend above the input view is the genuinely general answer and what commercial keyboards do, but it is a second window with its own lifecycle and dismissal — a great deal of machinery for a case the row already answers, and worth revisiting only if the row is ever removed. Shrinking the accent row degrades the feature on every row to fix it on one.

The cost is stated rather than left to be discovered: while a top-row key is held, its accent row covers the suggestion row and the microphone. Transient, and nobody reads suggestions mid-hold, but real and worth seeing on the phone.

**Queue changes:** moved from Unprocessed into Processed below the readiness line with `Blocked by: [suggestion-strip]`, the row it clamps into. The ordering is written on both items.

**Work processed:** kept — [accent-row-top-row].
