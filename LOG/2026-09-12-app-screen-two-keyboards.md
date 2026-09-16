# 770b778 — [app-screen-two-keyboards] the app screen's board becomes a picture, because deleting it would have taken the layout preview with it

Session of 2026-09-12, 12:54.

The app screen draws a board at the bottom and, since 2026-09-05, carries the problem report's own text field. They behave differently: the board appends to a scratch line, the field raises whichever keyboard the phone has selected. The user typed on the board on 2026-09-09 expecting the report box to fill.

The board is development scaffolding — `MainActivity`'s own comment says so — and both its original jobs are now done by typing on the real keyboard. The entry offered deleting it as the cheapest way out, and that would have been wrong: the layout picker built on 2026-09-05 gave it a second job nobody noticed it had acquired, since it is the only place a layout can be looked at before it is chosen, and there are seven of them.

So it becomes what it now is — a picture of the chosen layout, sitting with the picker, taking no input, with the scratch line going too. A board that never responds reads as a picture rather than as a broken keyboard, which is the same call [landscape-reveal-neighbours] took the same day for the same reason: a key you cannot properly use should not type.

Wiring the board to whatever field has focus was refused as the most work and the oddest result, two keyboards on screen at once. A screen of its own for the report was refused for adding navigation to an app that has none.

**Queue changes:** [app-screen-two-keyboards] designed out and cleared to run; SPEC's layouts principle gained the sentence about the settings screen's picture.

**Work processed:** kept — [app-screen-two-keyboards].
