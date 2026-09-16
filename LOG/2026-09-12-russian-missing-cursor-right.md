# 770b778 — [russian-missing-cursor-right] cursor-right takes the apostrophe's slot, and the validator learns to check for the five structural keys

Session of 2026-09-12, 12:54.

`key-layout-ru.json` had cursor-left and no cursor-right; every other shipped layout has both. The Russian board is eleven columns wide because Cyrillic needs the letter slots, so row 2 runs cursor-left, nine letters, backspace, and enter moved down to row 3 — cursor-right had nowhere left and was dropped rather than rehoused.

**Reading the configs settled where it goes and corrected the obvious explanation.** Eleven-wide is not itself the reason: Spanish is also eleven wide and carries three empty letter-panel positions. The Russian letter panel is completely full, so the key had to come from somewhere, and the apostrophe at row 3 col 5 is the one doing least work for a Russian typist — Russian orthography does not use it, and it is there because the row was transcribed wholesale from English. Everything else on that row earns its place: the two space bars are SPEC's, shift and enter are structural, and `? , ! " . -` are all used in Russian.

No key is lost, which SPEC requires: the apostrophe rehouses onto a Russian symbol panel, and [russian-panel-gaps] now reserves one of its thirteen slots for it — which is why this item waits on that one rather than as a formality, since the fill would otherwise take all thirteen.

**The second half is worth more than the Russian fix.** `KeyLayoutValidationTest` checked slots, bounds, duplicates and accent lists and nothing about a layout carrying the keys a keyboard needs — a layout missing backspace, enter or shift would have passed every rule the project had. It now asserts all five structural actions across every shipped config, which is what would have caught this on the day the file landed. SPEC gained the matching sentence at the /done gate.

**Queue changes:** [russian-missing-cursor-right] designed out and held against [russian-panel-gaps]; SPEC gained the five-structural-keys sentence.

**Work processed:** kept — [russian-missing-cursor-right].
