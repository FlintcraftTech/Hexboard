# 8f705a3 — [panel-switch-gestures] built: a horizontal pager over the three letter panels, opening on QWERTY, with the board's non-consuming press tracker underneath

Closed 2026-09-02 16:18. Fifth item of the run, last of the four `KeyboardPanel.kt` items, because its swipe must cancel everything the other three started.

The architecture was settled at planning by reading the code: nearest-centre routing puts the tap handler on the board, so panel switching wraps the whole board — a pager of three pages opening on QWERTY, with the drag-follows-finger feel the prototype has. The known trap was the consumed pointer-down: Compose's standard tap detector consumes it and starves the pager. The research named two remedies, a non-consuming tap detector or parent detection in the Initial pass, and said which was needed would be discovered by running it.

What the build found is that the first remedy was already in place: the press tracker [key-press-feedback] wrote consumes nothing on the way down, so the pager sees every event and no Initial-pass detection was added. The one case that needed care went the other way — a slide along the accent row is horizontal movement, exactly what a pager reads as a swipe, so the board consumes movement while an accent row is open and at no other time. A pointer the pager does take over shows up to the board as consumed and cancels its key, its repeat and its popup, which is the contract the four items agreed.

The new `HexboardBoard` composable holds the pager and panel state at the surface, sized to the tallest panel so the board does not change height between pages, and both the app screen and the input method service now host it instead of one panel. The service edit was one line to a file already in the run's list, made because the surface the pager wraps is the service's input view; it is recorded here rather than treated as scope growth.

Two alternatives the item recorded as never investigated stay that way: one continuously offset surface with a single detector, and switching on a discrete fling. Neither was needed to build this.

Tick: done, UNCONFIRMED: needs the app run on the Pixel 6; the thing to watch is whether the pager swipes at all (the consumed-down hazard) and whether a slide along the accent row stays a slide rather than becoming a page turn.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt`.

**Routed to Captures:** [panel-key-size-consistency], filed by the post-run rescan — each panel sizes its own keys, which shows once a layout has one eleven-wide panel and two ten-wide ones.
