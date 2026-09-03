# [HASH] — [uppercase-labels-vs-shift-state] answered by the prototype, which had already decided it: labels rest lowercase and rise to capitals with the shift state, needing no config change

Recorded 2026-09-04 at 17:00. This session ran across 2026-09-03 and 2026-09-04.

The capture came out of watching Hexboard run on the Pixel 6 on 2026-09-03: every letter drew as a capital while lowercase characters arrived in the field. It found the cause — `resources/key-layout.json` gives each letter key one `label`, already capitalised, alongside a separate `output` — and concluded that [shift-behaviour] assumed something the config could not do, since there was no lowercase form for a shift state to switch to. It framed that as an open design question with two answers available.

Reading the two files it rests on showed the question was not open. `hexboard17.html`, which SPEC names as the canonical reference for layout and key inventory, redraws each label uppercase when shift or caps is on and lowercase when neither is (line 429), and does the same for the alternatives in an accent row (line 639). So letters resting in lowercase is a decision the prototype already made and nobody had written down. The capture was right that the question was unanswered *in this project's documents* and wrong that it was unanswered.

The second half was cheaper still. Across all 27 English letter keys and all 31 Russian ones, `output` is exactly the lowercase of `label` — checked key by key rather than sampled. So the unshifted form is already in the config under a different name, and the board can draw `output` at rest and `label` while a state is on. No layout file changes and no schema field is added, which matters because a new field would have to be written into every future language's config.

The alternative — capitals always, with the shift state signalled by the shift key's own lighting alone — is what the code does today and what several phone keyboards do. It lost because the prototype had already chosen otherwise and SPEC names that prototype canonical, so keeping today's behaviour would have been a silent departure from the reference rather than a decision.

The user was told plainly that this makes the resting keyboard read in lowercase, which is a visible change from what they had seen on the phone the day before, and agreed to it on that basis.

**Queue changes:** [shift-behaviour] rewritten to carry where the unshifted letter comes from, with the defeated alternatives and a rests-on line noting that a further layout could break the `output`-is-lowercase assumption, so the build should fall back to lowercasing rather than assume it; its hold, which had been repointed onto this capture earlier in the session, dropped and the item cleared to run. SPEC's shift sentence gained a clause saying letters rest in lowercase, having previously described only the shifted state — which is how the gap survived.

**Work processed:** deleted [uppercase-labels-vs-shift-state], its content having moved into [shift-behaviour] and SPEC.

**Advisory:** not needed — the close's recommendation names no single item to start from.
