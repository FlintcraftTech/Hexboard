# Panel switching: a horizontal pager over a board-level tap detector

Researched 2026-09-01 for `[panel-switch-gestures]`. The question: Hexboard's
`KeyboardPanel` puts its tap detector on the board rather than on each key,
because SPEC requires nearest-centre routing — a tap goes to the closest key
centre, not to whichever circle contains it. Panel switching therefore has to
wrap the whole board. Does a horizontal swipe still reach a pager wrapped around
a board that is already detecting taps?

## What the search settles

**`detectTapGestures` consumes the pointer-down event.** Compose's standard
gesture modifiers mark the down as consumed, and a consumed down means anything
else contending for that gesture is skipped. This is documented behaviour and
widely reported, not an edge case.

**So a naive wrap is a known hazard, not a safe default.** Putting a
`HorizontalPager` around a `KeyboardPanel` that already runs
`detectTapGestures` on its board is the obvious construction and is exactly the
shape people report failing: the child's detector takes the down, and the
parent's drag detection can be starved of the events it needs to recognise a
swipe.

**Two established workarounds exist, and they are different in kind.** One is a
non-consuming tap detector — a hand-written `PointerInputScope` extension that
detects a tap without calling the consume, so the event survives for the parent.
The other is pass separation: Compose delivers each pointer event in an Initial
pass and then a Main pass, so a parent detecting its drag in the Initial pass
sees the event before the child's Main-pass tap detector does. The second is the
mechanism Compose itself uses for nested scrolling.

## What it does not settle

Which workaround Hexboard needs, and whether either is required at all with the
current pager implementation. The sources disagree in detail, some describe
older versions, and the behaviour depends on the exact modifier order and
Compose version in this project. **Nothing here has been run.** Gradle cannot
execute on the machine this was researched from — it needs a loopback connection
to its own daemon and every available route is blocked from making one, recorded
across four attempts in `[run-key-config-validator]` — so this is reading, not
testing.

It also says nothing about the second half of the item's question: whether an
in-flight drag may cross a panel boundary, and how a swipe near a panel edge is
told from a key press there. Touch slop is the expected answer — movement below
a threshold is a tap, above it a drag — but the interaction between slop and a
consumed down is the same unsettled question as above.

## Frame assessment

- **TIME RANGE** — Not applicable in the usual sense: the subject is a
  framework's current behaviour rather than a period the product addresses. The
  relevant range is "the Compose version this project builds against", which the
  finding does not pin down and which is a real gap in it.
- **PEOPLE** — Applies to whoever builds panel switching, which is Claude in a
  later run. It is developer-facing and reaches no Hexboard user directly,
  though a wrong answer reaches every user as a keyboard that swipes when they
  meant to type.
- **FRESHNESS** — Amended on a cycle. Compose gesture handling changes between
  releases, and at least one source read here describes older behaviour. Treat
  this as stale the moment the project's Compose version moves.
- **RISK IF WRONG** — Bounded and cheap to discover. Being wrong means the first
  attempt at panel switching does not swipe, which fails loudly in front of
  whoever runs it rather than shipping silently. It warrants no red flag. What
  it does warrant is the item saying "try it and expect this to bite" rather
  than "wrap it in a pager".
- **ALTERNATIVES** — Only the pager shape was researched. Two others were never
  investigated: drawing all three panels on one continuously offset surface with
  a single gesture detector handling both tap and drag, and switching panels on
  a discrete fling with no drag-follows-finger at all. Neither was ruled out;
  both were simply not looked at, and the second would sidestep this whole
  question by never contending for the same gesture.
