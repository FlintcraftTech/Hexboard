# [HASH] — [tap-word-alternatives] designed out around homophones derived from CMUdict, after research closed the two obvious routes

The item had one open half — where the alternatives come from — and it could not be designed at a desk. The research settled it in three findings, filed as `word-alternative-sources.md`.

Key geometry cannot supply them, established before searching: autocorrect fires on the space bar, so a word still sitting finished in the text was either already corrected or is a dictionary word, and the motivating case — "rose" where "rows" was meant — is two edits apart between two real words. An on-device language model is unavailable: Android's route is Gemini Nano through the ML Kit GenAI APIs, whose supported-device list starts at the Pixel 9, while this project's handset is a Pixel 6 and SPEC's minimum is API 26, so it fails for the developer and most of the audience at once. And `pimentel/homophones`, the obvious curated list, states no licence at all, so it cannot go into a public repository.

What works is not a list. CMUdict maps words to phoneme sequences, so words with identical sequences are homophones by construction — a grouping rather than a curation, with nothing to license and nothing to trust. "rose" and "rows" are both `R OW1 Z`. Its licence makes any research or commercial use unrestricted, asking only that its origin be acknowledged, which is the shape README's Notices section already uses twice.

The affordance was checked rather than assumed: `onUpdateSelection()` fires when the user taps to move the cursor, and `InputConnection` reads the text either side of it. The item is held against [predictive-dictionary-bundle], whose word list the homophone table is filtered against, and against [verify-this-runs-build-on-device], because the row it draws into was built yesterday and nothing has compiled it. SPEC's "nothing correctly spelled" principle gained a sentence naming the feature.

**Queue changes:** [tap-word-alternatives] rewritten and moved into Processed below the line, blocked by [predictive-dictionary-bundle] and [verify-this-runs-build-on-device]; SPEC's correctly-spelled principle extended; `word-alternative-sources.md` filed with an index line.

**Work processed:** kept — [tap-word-alternatives].
