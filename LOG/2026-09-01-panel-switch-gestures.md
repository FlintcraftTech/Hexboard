# be3516e — Panel switching designed out as a horizontal pager, with the consumed-down gesture trap researched and filed before the build meets it

The item's open question was architectural: is a panel a screen the keyboard navigates between, or a slice of one continuously drawn surface? Reading `KeyboardPanel.kt` answered it without appeal to preference. The tap handler sits on the board rather than on individual keys, and it has to — SPEC requires nearest-centre routing, so a tap goes to the closest key centre rather than to whichever circle contains it. Panel switching therefore cannot be a per-key concern and must wrap the whole board, which is a horizontal pager holding three pages, opening on QWERTY, keeping the drag-follows-finger feel the browser prototype has.

That raised a question about the framework rather than about Hexboard, so it was researched with the user present rather than guessed at. `detectTapGestures` — which the board already runs — consumes the pointer-down event, and a consumed down starves whatever else contends for the gesture. So the obvious construction, wrapping the board in a pager, is exactly the shape reported to leave the pager unable to swipe. Two established remedies exist: a hand-written tap detector that detects without consuming, or having the parent detect its drag in Compose's Initial pass, ahead of the child's Main-pass detector. Both live in the same file, so which one is needed does not change the file list — it is discovered by running the thing.

Filed as `workshop/resources/research/compose-pager-vs-board-tap-gesture.md` with an index line, stating plainly that none of it was executed, since Gradle cannot run here. Its frame assessment records two alternatives nobody investigated rather than ruled out: one continuously offset surface with a single detector handling both tap and drag, and discrete fling-to-switch, which would sidestep the contention entirely.

The item's old release condition was the vague "waits on a rendering surface that exists". The surface exists as code, but nothing has compiled or run it, and the item's observation is a thing seen on a phone — so it now names [compile-and-view-panel] as its blocker, which is that condition made into an item that can actually resolve.

A correction on process, recorded because it recurs: the research index should have been read before offering the search, not after. Nothing was duplicated, but the check ran in the wrong order.

**Queue changes:** [panel-switch-gestures] moved from Unprocessed to Processed, held below the line against [compile-and-view-panel]; gained the pager design, the hazard, a research citation and a cross-reference to [key-press-feedback], which shares its file.

**Work processed:** kept — [panel-switch-gestures].
