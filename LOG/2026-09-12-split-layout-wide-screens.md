# [HASH] — [split-layout-wide-screens] deleted: the unreachable middle it was designed against stopped existing when its own blocker shipped

Session of 2026-09-12, 12:54.

Lifted off the held region at this session's opening because [landscape-board-height] had shipped, and deleted by its end. The reasoning it was built on — that on a wide screen the thumbs move to the edges and the middle becomes the part neither can reach — was true while the board filled the screen's width, which is how it behaved when the item was designed on 2026-09-02. The height bound of 2026-09-09 made the radius the smaller of the width's answer and the height's, so a sideways Pixel 6 now draws a board about a third of the screen's width, centred and whole, with no unreachable middle in it. The item was held against that very change and nobody re-read it afterwards.

**This is the fourth time this project has caught a decision resting on a premise it had itself removed**, and the third since 2026-09-05. The pattern is the same each time: the thing that removes the premise is the item's own blocker, so the moment the hold lifts is exactly the moment the reasoning needs re-reading, and lifting is the one step that does not re-read it.

The tablet case was weighed rather than assumed away: a tablet in landscape solves against the 34dp radius cap and comes out centred and whole in the same way, and a tablet held for thumb-typing is not the posture the split was reasoned for. Nobody has asked for a split and there are no users to ask. Its full design — the column split, the gap as a proportion of the solved radius, the odd-count rule, and five refused alternatives — stays in git and in the record, so a tablet case arriving later restarts from the design rather than from nothing. [landscape-reveal-neighbours] carries the pointer.

**Queue changes:** [split-layout-wide-screens] lifted at the opening, returned to Unprocessed mid-session, then deleted.

**Work processed:** deleted — [split-layout-wide-screens].
