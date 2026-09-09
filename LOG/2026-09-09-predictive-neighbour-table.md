# [HASH] — [predictive-neighbour-table] which keys touch which, computed from the zag packing rather than stored anywhere

This session ran across 2026-09-05 and 2026-09-09.

SPEC's predictive engine measures a mis-tap by whether the key pressed is one of the six neighbours of the key meant — a near-miss — or anything else, a real difference. Nothing in the app knew which keys touch which. The config could not say: it carries each key's `row` and `col` and no geometry at all, deliberately, because the zag is the perceptual wedge and lives in Kotlin. So the table had to be derived, and deriving it depends on nothing else — not a dictionary, not the correction behaviour, not a phone — which is why it was worth doing before any of them.

`KeyGeometry` gains `neighbourTable`, taking one panel's slots and returning each key's neighbours. A key's neighbours are the keys whose centre lies one vertical step away, because in this packing that single distance covers all six directions: a diagonal neighbour is `horizontalStep` across and half a vertical step down, and `horizontalStep` is `verticalStep * √3/2`, so the diagonal comes out at exactly one vertical step too. Every distance is a multiple of the radius, so the table is scale-free — the test asserts the table computed at 12dp is identical to the one at 34dp, the two ends of the solvable range, which is what makes that claim checkable rather than asserted.

Three things the item settled beforehand, all of which survived the build. An edge key has fewer than six neighbours and that is correct: six is what an interior key happens to have, not a requirement the measure imposes. The two space bars take no part, because a tap on a space ends the word and ending the word *is* the correction moment. And sets never span panels, because a mis-tap cannot cross a swipe boundary.

The build refused the obvious alternative for the reason the item recorded: writing the table into `key-layout.json` would put derived geometry into the one file SPEC keeps geometry out of, and would have to be re-derived by hand for every new language. It also declined "row and column differ by at most one", which is rectangular adjacency and gets the zag wrong — the one thing this project's geometry does differently from everyone else's.

The tick was written UNCONFIRMED because Gradle cannot run on this machine; the same algorithm was run independently over both shipped configs first, and every assertion held. `NeighbourTableTest` then passed for real in the walkthrough of 2026-09-09, among the 50.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt`, `android/app/src/test/java/tech/flintcraft/hexboard/NeighbourTableTest.kt`.

**Routed to Captures:** none.
