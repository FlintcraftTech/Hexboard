# 770b778 — [landscape-reveal-neighbours] the sideways screen answered by revealing the neighbouring panels, which cost the split layout and rewrote the seam fix

Session of 2026-09-12, 12:54.

Turning the phone sideways on 2026-09-09 showed the board in the left third of the screen with the rest empty, because [landscape-board-height] had made the radius the smaller of the width's answer and the height's. The user's answer was that whatever of the RARE and SYMBOLS panels fits in that spare width should be showing.

**Settling it took three items rather than one, and the order they fell in matters.** The reveal needs pager pages to be the width of the *board* rather than of the screen — with screen-width pages the spare width sits inside the current page and no neighbour can appear in it. That is the same change [panel-seam-gap] needed, arrived at from the other side: making each page a whole number of column pitches puts the first and last columns half a pitch from their page edges, so the seam becomes one pitch by construction.

**So the seam decision taken earlier the same session was reopened and replaced, and the reason it lost is recorded rather than dropped.** The first answer was a negative page spacing overlapping full-width pages by exactly the surplus. The arithmetic was sound and nothing was wrong with it, but a screen-width page can never reveal anything, so the reveal would have needed a second mechanism doing the same job. It also carried an unread risk — a Google issue titled "HorizontalPager with negative pageSpacing causes…" behind a sign-in — which the page-width route removes rather than accepts. The cost accepted in exchange: a sliver of the neighbouring panel shows in portrait too, a few dp at each edge, permanently rather than only mid-swipe.

**[split-layout-wide-screens] was deleted over this**, and the decision is recorded under its own slug. It answered the same screen by separating the board into two halves under the thumbs, and the two cannot both hold.

**A revealed key is drawn and inert, which was the user's call against Claude's recommendation.** Claude proposed that tapping one move to its panel; the user refused, and his reasons are in the entry: an accidental press must not scroll the screen about, a key that cannot be seen in full should not type, those keys are the rarer characters so typing from them serves uses that probably do not exist, and inertness is what entrains the swipe and pushes the gesture's discoverability. The cost he accepted knowingly is that a tap doing nothing is the thing people retry.

**Queue changes:** [landscape-reveal-neighbours] designed out and held against [panel-seam-gap]; [panel-seam-gap] rewritten to the pitch-width mechanism; [split-layout-wide-screens] returned to Unprocessed and then deleted; SPEC's layout bullet rewritten from the split to the reveal.

**Work processed:** kept — [landscape-reveal-neighbours], [panel-seam-gap]; deleted — [split-layout-wide-screens].
