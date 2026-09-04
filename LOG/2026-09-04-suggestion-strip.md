# [HASH] — Row above the keys built and shipped empty, its height derived from the key radius

Written 2026-09-04 at 15:54.

The row exists because three separate features need one and none of them owns it. It was split out of [in-keyboard-voice-input] on 2026-09-02, reversing a recommendation made earlier that same session: when the strip was first designed exactly one feature needed it, so a separate item looked like a hop with nothing in it — and within the hour [persistent-clipboard] needed the same row for its own button. The two are held by different things, voice input waiting on a dictation test and the clipboard on nothing, so leaving the container inside the voice item would have parked the clipboard behind a test that has nothing to do with clipboards. Claude's recommendation both times; what changed was the count of features needing it, not a view about hops.

The height is derived rather than fixed: one row's vertical pitch of the radius the board already solved, floored at Android's 48dp minimum touch target. That keeps it in proportion on an eleven-wide layout and at any screen width without a second rule, and means a control in it can never end up a different size from the keys beneath it. A fixed dp value was the alternative and lost on exactly that.

It ships empty, which was weighed and accepted rather than overlooked: the three features that fill it are each held by something of their own, so the keyboard carries a visibly empty band until one lands. That is obviously unfinished rather than wrong, and the alternative — holding the container until a control exists — is the arrangement this item was split out of.

One design note the build resolved rather than invented. The item describes the row as reserving more height than it draws, because a control may stand proud of the band and an input method's window is sized to its view, so nothing can be painted outside the keyboard's rectangle. With the row empty nothing stands proud, so reserved height equals the band; the overspill mechanism is documented in the code for whichever feature first needs it.

**Confirmed:** nothing.

**Unconfirmed, transcribed from the tick:** `StripLayoutTest` is Android Studio's to run on the Pixel 6. The row ships empty, so the cost in key size is also worth looking at on the phone rather than accepting on paper, as the item asks.

**Files touched:** `KeyboardPanel.kt` (HexboardBoard wraps the pager in a Column under a new empty ControlStrip; `stripHeight`, `MIN_STRIP_HEIGHT`, `STRIP_TAG` and three imports, ~55 lines), `StripLayoutTest.kt` (created, 3 tests, 130 lines).

**Routed to Captures:** none from this item.

**Depth:** short.

[in-keyboard-voice-input], [persistent-clipboard] and [accent-row-top-row] were each held against this item and their blockers are now answered; lifting them is planning's call, not this close's. [accent-row-top-row] is the one whose premise this most directly satisfies — it needs a bar above row 0 to clamp an accent row into, which is what the prototype had and the board did not.
