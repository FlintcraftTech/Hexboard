# 88b95f2 — Three completion slots designed for the row above the keys, after a long-word slot was designed and then removed

Recorded 2026-09-17 13:59. The reasoning behind the commonness work this sits inside is in the record under [uniform-neighbours-predictive]; this entry carries what is particular to the display half.

While a word is being typed the middle of the row above the keys carries three slots: the raw string entered so far, and the two commonest completions of it. Tapping one puts that word in, and nothing is applied unless tapped. Completion is prefix-directed by nature, which is the mechanism the correction engine refused — the two coexist because SPEC already permits offering what it forbids applying, so a completion that misses because the first letter was mistyped simply shows nothing and the space bar's correction still catches the word.

**The wrong turn is the part worth keeping.** The design first had fixed roles for all three slots, the third being an "expansion lane" offering a long completion — carried across from the specification the session started from, to answer the keystroke-savings problem. The owner questioned it twice: first why length had anything to do with it, and earlier whether bounding it to common words would leave long rare words uncompletable. The second question produced a genuinely better rule on the way — relax the commonness bound as the prefix narrows the field — and the third killed the slot outright. The honest position, said at the time: the case for fixed roles over ranked candidates rested almost entirely on the long-word argument, so removing it removed the recommendation too. Length as a gate with commonness as the choice, and the relaxing bound, both fell with it.

What replaced it is what the owner asked for in the first place — commonest first — with the left slot keeping the one role that is not a suggestion at all.

Two things settled alongside: the left slot commits without saving, because a slot tapped while typing fast would make the saved-word store's "deliberate save" claim doubtful and that claim is what cleared its privacy flag; and the row's three claimants — homophones on a finished word, the save offer after an undo, completions while typing — are at different moments and so never compete, which is now written on all three items.

A privacy risk was designed out rather than accepted: the source specification populated its top tier from contacts, calendar entries and nearby place names. Refused outright — it would make the keyboard a reader of two of the most sensitive stores on the phone and reverse the engine's promise that nothing enters its word store except by a deliberate save. The same shape was declined once before, on 2026-09-12, when [dictation-biasing-saved-words] left Android's device-context switch off. The item carries `Red flag · State: cleared` recording that.

**Queue changes:** [completion-slots] written and placed in Processed, cleared to run, immediately after [uniform-neighbours-predictive]; cross-references written onto [tap-word-alternatives] and [predictive-saved-words]; SPEC's predictive principle gained the three-slot sentence.

**Work processed:** kept — [completion-slots].
