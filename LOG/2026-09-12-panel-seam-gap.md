# [HASH] — [panel-seam-gap] the seam closed by making a pager page a whole number of column pitches, replacing the negative-spacing answer taken hours earlier

Session of 2026-09-12, 12:54.

The reasoning is carried by `2026-09-12-landscape-reveal-neighbours.md`, which records why the first answer was replaced and what the replacement costs. What belongs here is the arithmetic that made it a design question rather than a bug: within a panel neighbouring columns sit `horizontalStep` apart, about 1.81 radii, while across a seam the two centres are `2 * radius + 2 * EDGE` apart — about 2.55 radii at the Pixel 6's solved size, roughly 40% wider than every other gap on the board.

Cleared to run, with `SeamSpacingTest` as its observation and the instrumented suite unavailable as a check while [instrumented-tests-no-composition] stands.

**Queue changes:** [panel-seam-gap] designed out, rewritten once, and cleared to run.

**Work processed:** kept — [panel-seam-gap].
