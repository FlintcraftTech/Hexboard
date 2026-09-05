# [HASH] — [predictive-neighbour-table] created as the first buildable foundation of the predictive engine

Computing each key's neighbour set at runtime from `key-layout.json` plus the zag rule, in `KeyGeometry.kt`. Split out of [uniform-neighbours-predictive] on 2026-09-05; the reasoning, including the three caveats this item answers, is in that item's record for the same date.

Buildable ahead of everything else because it depends on no dictionary, no correction behaviour and nothing being verified on a phone — a key's neighbours are the keys whose centre sits one vertical step away, and the result is scale-free.
