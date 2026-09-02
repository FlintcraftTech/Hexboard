# PLACEHOLDER — [long-press-accent-popup] found by the rescan and processed the same session: the config's eighteen accent lists and SPEC's promise had no Kotlin and no queue item behind them

Planning record for [long-press-accent-popup], 2026-09-02.

Surfaced by the rescan run at the end of the planning session: while designing [backspace-key-repeat], reading `KeyboardPanel.kt` had shown a tap-only detector, and nothing in the queue built the accent popup that `key-layout.json` declares on eighteen keys and SPEC lists as a feature. Processed immediately on the user's choice.

The gesture is the prototype's, which SPEC names canonical: hold, a row of alternatives appears above the key with the first highlighted, slide sideways to move the highlight, release to type it; the base character is not in the row. The one decision put to the user was release-without-sliding, where the prototype types the first alternative and Gboard would type the base letter; the prototype's behaviour was kept on Claude's recommendation, as it saves a slide for the commonest case. Timing follows the phone's touch-and-hold setting under the SPEC sentence written earlier in the session, replacing the prototype's fixed 320 ms. A key never both repeats and pops up.

Cleared to run third of the four items editing `KeyboardPanel.kt` — after feedback and repeat, whose press state and hold detection it reuses, and before the swipe, which must cancel it. The order is written on all four.
