# [HASH] — A native-reader check filed as the one thing standing between the Russian layout and shipping

The Russian layout's rows are copied from the published standard and are checkable against sources, so they are not the risk. Two things in it are Claude's own reading and are copied from nowhere: that the hard sign Ъ belongs behind a long-press on the soft sign Ь, and that Щ belongs behind Ш. Both are plausible pairings from the shape of the writing system, and plausible is precisely the failure this project keeps guarding against, because a shipped layout is copied rather than read and an error in it propagates.

It cannot be Claude's, and the reason is narrower than "Claude does not read Russian". Whether a pairing is *guessable* rather than merely defensible is a judgment only someone who types the language can make. The research this rests on came from English-language explainers of the Russian layout rather than from Russian typists, which is recorded in the finding file rather than glossed.

The walkthrough was corrected later in the same session, after /rescan caught it. Its first step had the user repoint the preview page's `LAYOUTS` block at the Russian config — an edit to JavaScript, which Claude can make, so `[user]` was the wrong home for it. Preparing the preview moved into [language-starter-layouts], and this item now starts at opening a file and showing it to someone.

It names no observable, which is stated on the item so a later session asks rather than checks.

**Queue changes:** filed into Unprocessed with `Blocked by: [language-starter-layouts]`; step 1 removed after the rescan; a note added about recording what the check cost to arrange, because [language-list-choice] is held against it for that fact.

**Work processed:** kept in Unprocessed, held — [russian-layout-check].
