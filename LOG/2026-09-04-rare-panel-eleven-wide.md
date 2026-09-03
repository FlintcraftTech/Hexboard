# [HASH] — [rare-panel-eleven-wide] confirmed and found to correct two claims rather than one, with the eleventh column traced to a prototype affordance this project never reproduced

Recorded 2026-09-04 at 17:00. This session ran across 2026-09-03 and 2026-09-04.

The capture said [panel-key-size-consistency] was wrong to call the panel key-size mismatch Russian-only, because the English RARE panel is eleven columns wide. Measuring every panel in both layouts key by key confirmed that and turned up a second false claim in the same item: it said that on Russian, QWERTY divides the width by eleven while RARE and SYMBOLS divide it by ten. RARE is eleven wide on *both* layouts, so on Russian it is SYMBOLS alone that is narrow. The second error is what made the first look safe — believing RARE was ten wide everywhere is exactly what would let someone conclude English had no mismatch.

Both claims were reasoned from the Russian layout's arithmetic on 2026-09-02 without the English config being read against them, and neither surfaced until the board was seen running.

**Why the eleventh column exists, which changed what the fix should be.** RARE's rows 0 and 1 sit in columns 0–9; row 2 holds ten keys too, but in columns 1–10 — indented by one. The prototype's own comment on that row says why: `cols 1-10 (offset +1; col 10 visible on half-snap)`. Its board could half-snap sideways, so a key hanging past the right edge stayed reachable. Hexboard's board does not do that and nothing plans to, so the eleventh column is an artefact of a mechanism this project never reproduced — and it currently costs the whole board about nine per cent of its key size.

That mattered because of what the queued fix would otherwise have cost. Sizing every panel from the widest would, on English, shrink QWERTY and SYMBOLS to match RARE: the default panel of the default layout made smaller, on a keyboard whose headline is large keys, to accommodate one indented row of maths symbols. Moving the row back is a config edit that removes the cause instead, and no key is lost or moved between panels — only the row's horizontal offset changes, which SPEC leaves to the config since geometry alone stays in code.

The two items do not replace one another. [panel-key-size-consistency] is still needed, because Russian's own QWERTY genuinely is eleven columns against SYMBOLS' ten; what changes is that after [rare-row2-unindent] the mismatch really will be Russian-only, which is what that item wrongly assumed on 2026-09-02.

One consequence is recorded on the new item rather than discovered later: the zag keys on column parity, so shifting a whole row by one inverts which of its keys sit low. That is a visible change nobody has seen, and it is the thing to look at when the work lands.

**Queue changes:** [rare-row2-unindent] created and cleared to run, editing `resources/key-layout.json` and regenerating the manifest. [panel-key-size-consistency] rewritten to carry both corrections, the read that produced them and a dated rests-on line; its build, its refused alternatives and its observation are unchanged.

**Work processed:** deleted [rare-panel-eleven-wide], its content having moved into [rare-row2-unindent] and [panel-key-size-consistency].

**Advisory:** not needed — the close's recommendation names no single item to start from.
