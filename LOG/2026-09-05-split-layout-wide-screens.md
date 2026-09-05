# [HASH] — [split-layout-wide-screens] designed out, and found not to solve the landscape problem it was filed for

Three of the item's four open questions turned out to be desk answers. The space bars need no rule: ten columns splitting 0–4 and 5–9 put the column-4 bar in the left half and the column-6 one in the right, forced by where they already sit. An odd column count — the Russian board is eleven wide — takes the extra column on the left. The gap is a proportion of the solved radius, like the key gap and the strip height, rather than a fixed dp value that drifts on a board that is not ten wide.

The fourth dissolved rather than being answered: a horizontal swipe crossing the gap needs nothing, because [panel-switch-gestures] wraps the whole board in one pager, so splitting changes where keys are drawn inside a page rather than making two pages. Hit-testing is untouched for the same reason — `nearestCentre` runs over every key, so a tap in the gap resolves to the nearest centre and there is no dead space.

What the item does not do is the finding. The split moves the halves apart horizontally and does nothing about height, and height is what breaks in landscape: the radius is solved from width alone and capped at 34dp, so a sideways Pixel 6 gives about 323dp of board plus about 71dp of strip on a screen about 411dp tall. Splitting that board puts two halves under the thumbs on a keyboard that has swallowed the screen. That is arithmetic off `KeyGeometry.kt` and nobody has turned the phone sideways.

So [landscape-board-height] was split out and cleared, and this item held against it — the split being a feature and the height bound closer to a defect, where bundling them would hold the feature behind the fix. At the close, the spec-sync gate caught that SPEC's split sentence promises the halves keep their key size, which the height bound makes false; the sentence was rewritten.

**Queue changes:** [split-layout-wide-screens] rewritten and moved into Processed below the line, blocked by [landscape-board-height]; [landscape-board-height] created and cleared to run; SPEC's split sentence rewritten at the close.

**Work processed:** kept — [split-layout-wide-screens], [landscape-board-height].
