# [HASH] — Today's 4-and-6 space bars kept, on a reason the preview could not have shown

Walk-through record for [row3-space-choice], opened as the drive started so nothing is lost if the session ends mid-way. Appended step by step.

The choice is between a matched-looking pair of space bars and equal thumb reach, and it cannot be computed: the zag rule puts odd columns half a key lower, so two space bars cannot be both edge-symmetric and at the same height. What the decision rests on is `planning/layout-preview.html`, which [row3-space-candidates] rendered and confirmed on 2026-08-21 and which nothing has touched since.

The light capability check was run before handing over: the step is looking at three arrangements with the user's own thumbs where they would sit holding a phone. There is no tool that substitutes for that.

Whatever is chosen releases [left-space-relocation], which writes the winner into the config — or, if today's arrangement wins, that item is deleted rather than built.

## What happened

- Drive opened. Step 1 given: open the preview page and look for the three labelled rows.
- The user answered before opening it: **today's 4-and-6 arrangement stays.** Their reason, in their own words, is that the thumbs are "naturally closer to the middle of the screen in normal vertical portrait mode handling of phone" — so the asymmetry the two candidates were designed to remove is not a cost. The whole premise of the move was that col 4 and col 6 are not symmetric about the row, which leaves the left-thumb key further from the left thumb than the right one is from the right; that premise assumes the thumbs start at the edges, and in portrait they do not.
- Steps 2 and 3 were therefore not driven. The answer the walkthrough exists to get was given, and the item names that answer as one of its three valid outcomes.

**Outcome: done.** No file changed. The consequence recorded on the item is that [left-space-relocation] is now deleted rather than built, since it exists only to apply a move that is not happening — but deleting a queue item is the user's call at a planning session, not something a run does, so it stays in the queue for now.

**Files touched:** none.

**Routed to Captures:** [split-layout-wide-screens], which the user raised immediately after making this choice and which answers the same reach problem for the case where it does bite.
