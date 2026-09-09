# [HASH] — [status-lines-after-install] three sentences that stopped being true when the keyboard switched on, and a fourth found in the same file

This session ran across 2026-09-05 and 2026-09-09.

`CLAUDE.md` said there was no input method service yet, so Hexboard was an app rather than a keyboard. `README.md` said there was no working keyboard and nothing to install. Both were true when written and both were falsified by the install of 2026-09-03, which registered Hexboard as an input method and typed the keys it was aimed at. The item was filed by /rescan on 2026-09-02, held against the install that would falsify it, and released when that install happened — which is the mechanism working exactly as intended.

Designing it out turned up a fourth instance the item had not named: README's prototype section said `hexboard17.html` is the only part of Hexboard you can actually type on today. Same fact, same staleness, same file. Fixing two sentences while a third contradicted them would have left the document arguing with itself.

**One distinction was kept rather than smoothed over.** "Nothing to install" is false in one sense and true in another: the project builds from source and runs on a phone, and there is no packaged release to download. A Status section saying there is a working keyboard, without that, sends a reader looking for a download that does not exist. So Status says both — it builds, installs and types, with predictive text, clipboard history and voice input all still unbuilt, and no packaged release.

**The phase paragraph's closing instruction was reworded rather than dropped**, on the user's call. It ended "Very little exists, so keep designing before coding rather than rushing new work into the app" — written when three Kotlin files existed, resting on a premise that has gone. The force behind it has not: the design record runs deliberately ahead of the build, which is how this project works rather than an accident of how little exists. Dropping a working rule for a wording reason would have been the wrong trade, so it is rewritten as intent.

The item's own grep was run and returns nothing for all four stale phrases across both files.

**Files touched:** `CLAUDE.md`, `README.md`.

**Routed to Captures:** none.
