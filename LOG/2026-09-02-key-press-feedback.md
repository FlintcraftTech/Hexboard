# PLACEHOLDER — [key-press-feedback] built: the pressed key lightens on touch and fades back on release, tracked per finger at board level

Closed 2026-09-02 16:18. Second item of the twelve-item run, and the first of four that edit `KeyboardPanel.kt`, in the order settled at planning: feedback first, because it introduces the per-key press state the other three hang on.

The design was the user's, narrowed at planning from touches-and-highlight to the key highlight alone: the pressed key lightens instantly, holds, then fades back, and nothing marks where the finger landed, because a marker would draw the eye to near-misses that nearest-centre routing already absorbed. That reasoning is on the planning record and was not reopened.

What the build changed underneath is bigger than the effect: the board's tap detector was replaced with a pointer loop that follows every finger from down to up, resolving each to a key at the moment it lands. That is what lets several keys fade at once — a fast typist's presses overlap — and it is also what the repeat, popup and swipe items needed, so the loop was written to take an `onCancel` from the start: a pointer some other handler has consumed fades its key with nothing committed. Key commit moved from tap to release as part of this, which the accent popup requires (release is what chooses).

Timing is two constants — a 40 ms hold after release and a 150 ms fade — written as opening values to adjust by eye on the phone, exactly as the item said.

Tick: done, UNCONFIRMED: needs the app run on the Pixel 6; the timing constants (40 ms hold, 150 ms fade) are opening values to adjust by eye.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

**Routed to Captures:** none.
