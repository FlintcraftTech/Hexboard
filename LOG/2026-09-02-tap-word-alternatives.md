# 37384e4 — Tapping a finished word to see alternatives filed as a gap the predictive engine cannot cover

Captured from the user's own observation: they tapped a mistyped word on their phone and Gboard offered the right one from its suggestion bar. Hexboard has no equivalent, and the gap is structural rather than an oversight.

[uniform-neighbours-predictive] corrects at the word boundary using a neighbour-weighted edit distance, where a near-miss is a substitution for one of six adjacent keys. The case that prompted this was "rose" for "rows" — several keys apart, and both real words, so the engine's own first guard, that a word already in the dictionary is never corrected, means it would never touch it. Homophones and wrong-but-real words are a different mechanism from mis-taps, and the geometry that makes this project's correction good says nothing about them.

Two halves, and only one is this item's. The affordance — select a word already typed, show alternatives, replace on a tap — is a keyboard interaction and belongs here. Where the alternatives come from is the harder half and is settled nowhere: a homophone list, a language model over the sentence, or the saved-word list the predictive engine holds.

Where they would appear is likely already answered: [suggestion-strip] builds a row above the keys for exactly this class of thing, and predictive text is already expected to fill its middle.

Left in Unprocessed because what a build would change cannot be stated until the source of the alternatives is chosen, and that choice wants the row in existence and the engine's shape settled.

**Queue changes:** filed as a capture at the bottom of Unprocessed. [speech-output-correction] names it.

**Work processed:** filed, not processed — [tap-word-alternatives].
