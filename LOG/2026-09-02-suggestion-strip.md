# 37384e4 — A row above the keys split into its own item once a second feature needed it, reversing a recommendation made an hour earlier

The board gains a row above the keys, one vertical step tall, holding nothing. Three later features each add their own control to it: the microphone at its right end, the clipboard button at its left, and predictive text's candidates in the middle.

The reversal is the part worth keeping. When the row was first designed — inside [in-keyboard-voice-input], to hold the microphone — Claude recommended against splitting it out, on the ground that exactly one feature needed it and a separate item would be a hop with nothing in it. The user agreed. Within the hour [persistent-clipboard]'s screen was settled as reachable from a button at the row's left end, and the two features turned out to be held by different things: voice input waits on [recogniser-gap-comparison], a dictation test on the phone, while the clipboard waits on nothing. Leaving the container inside the voice item would have parked the clipboard behind a test that has nothing to do with clipboards. Claude recommended the split on those grounds and the user took it. The premise that changed was the number of features needing the row, not a change of mind about hops.

The row's height is derived rather than picked: one row's vertical pitch, computed from the same radius the board already solved for the panel on screen, floored so it never falls below Android's 48dp minimum touch target — read from Google's own accessibility guidance during the session rather than asserted from memory, after Claude flagged that its first statement of the figure was recalled. A fixed dp value was the alternative and lost because it drifts out of proportion on any board that is not ten columns wide, which the Russian layout already is.

One structural finding, established by reasoning over the code rather than by running it: an input method's window is sized to its view and Compose clips to bounds, so a control cannot actually be painted outside the keyboard's rectangle. The microphone standing proud of the row therefore means the row reserves that overspill as real height and leaves it empty. Visually identical, structurally different, and worth recording because the natural reading of "spills over the edge" is the one that cannot work.

Two costs were weighed and accepted. The row takes one row's worth of height from the keys, on a keyboard whose argument is large keys — the ordinary cost every phone keyboard pays, but paid against SPEC's geometry principle and to be looked at on the phone. And it may ship briefly as a visibly empty band, since each of the three features that fill it is held by something of its own; that was judged obviously-unfinished rather than wrong, the alternative being the arrangement the split was undoing.

Held below the cleared line against [install-and-enable-on-pixel], because `KeyboardPanel.kt` took four changes earlier the same day and none has compiled.

**Queue changes:** filed into Unprocessed and moved into Processed below the readiness line, placed first in the held region as the item that releases the most other work. [in-keyboard-voice-input] and [persistent-clipboard] now name it as a blocker; [accent-row-top-row] does too.

**Work processed:** kept — [suggestion-strip].
