# 5f9d97e — [symbols-panel-empty-slots] filled with ten characters, after the item's own arithmetic turned out to be wrong

The item said the symbols panel carries thirty keys *and* sixteen empty slots, which cannot both be true of a forty-position panel, and it enumerated nine of the empties while missing row 2 column 5. Counted from the config: ten, and all on the left half, which is why swiping into the panel looks like arriving at something half-built.

One constraint made the choice matter more than it looked. The whole English inventory was checked: RARE is full at thirty of thirty and QWERTY has no spare position, so these ten are the **only** free slots in the English layout, and whatever goes here is what Hexboard has room for. Every printable ASCII character was found to have a home already, so the question was only what is missing beyond it.

The ten agreed with the user: “ ” ‘ ’ • ← → ½ ¢ ≈. The four curly quotes because a keyboard producing only straight quotes is one people work around daily; the bullet because there is no way to start a list; two arrows because the layout has none anywhere.

Two things were deliberately left out. ¿ and ¡ belong on the Spanish layout's own config, since the manifest rules bind each layout individually and [first-batch-layouts] is about to add Spanish. And the Russian layout, whose symbols panel has the identical ten gaps but whose primary quotation marks are « » rather than curly ones — filed as [russian-panel-gaps], along with three further empties on its RARE panel.

A placement constraint was found and written down: no two empty slots are adjacent anywhere on the panel, so the four quotes cannot sit side by side and "grouped" can only mean placed in the same region.

**Queue changes:** [symbols-panel-empty-slots] rewritten and moved into Processed, cleared to run; [russian-panel-gaps] created as a capture behind it.

**Work processed:** kept — [symbols-panel-empty-slots]. Filed — [russian-panel-gaps].
