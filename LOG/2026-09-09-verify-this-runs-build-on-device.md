# f83f5d9 — [verify-this-runs-build-on-device] the Android Studio sitting, driven live: seven defects found in code three runs had written and none had compiled

This session ran across 2026-09-05 and 2026-09-09.

Opened at the start of the walk-through on 2026-09-05, and appended to as each step happened,
so a session that died mid-drive would leave a record of what was actually done rather than
nothing. It was driven to its end on 2026-09-09.

**Why it is the user's.** Checked again on 2026-09-05 rather than taken from the item: `java`
is not on PATH and `JAVA_HOME` is unset, so `android/gradlew` fails before it starts; `gradle`
and `adb` are both absent too. Nothing here can compile the project or reach the handset.
Those facts are now in `TOOLS.md`.

**What has changed since the item's walkthrough was written.** The item was written on
2026-09-04 against a run of eleven items and thirteen test files. The run of 2026-09-05 built
fourteen more items on top of them, so the sitting now covers both runs at once: seven
layouts rather than two, a layout picker, a "Report a problem" entry, a bundled word list, a
height bound for landscape, and six new or extended test files. The steps below are the
item's own, re-cut to what is actually in the build.

## Steps, as driven

**Step 1 — Gradle sync. Failed on 2026-09-09 at 08:53, then fixed.** Nine errors, all in
`android/app/build.gradle.kts` and all cascading from one: `Unresolved reference 'util'` at the
line reading `java.util.Properties()`. Inside a Gradle Kotlin build script `java` names the Java
plugin's extension rather than the package, so the qualified form does not resolve; `load`,
`getProperty` and the `configs` list all failed downstream of it.

**It was not a defect of this run.** That line arrived with the build-output relocation on
2026-09-04, in a run that could compile nothing, and the 2026-09-05 run only moved it from
inside a `run { }` block to the top of the file. This sitting is the first time any Gradle has
read it, which is what the item existed for.

Fixed by importing `java.util.Properties` at the top of the script and calling `Properties()`.
Re-driven from step 1.

**Step 1, second attempt — nine errors down to four, on 2026-09-09 at 09:00.** The remaining
four all came from the expression building the list of files to copy into the assets:
`source.listFiles { ... }.orEmpty() + listOfNotNull(...)`. `listFiles` returns an array,
`orEmpty()` hands back an out-projected one, and neither `plus` overload resolves against an
out-projection — so `configs` took an error type and the `forEach` over it failed too.

**Also not a defect of this run.** That expression was written on 2026-09-04 in the same
uncompiled run; the 2026-09-05 work only added a second filename to the `listOfNotNull`.

Rebuilt as a plain `MutableList<File>`: the configs matched by name in one loop, then the two
named data files — `emoji-test.txt` and `wordlist-en.txt` — appended in another. No array
arithmetic and no variance question. Re-driven from step 1 again.

**Step 1, third attempt — the sync finished cleanly on 2026-09-09 at 09:02.** So the two
defects above were the whole of what the previous run's uncompiled Gradle changes carried.

**Step 2 — where the build output goes: not yet answerable, and that is the honest state.**
Claude checked rather than handing this over. `android/app/build/` still holds output, but all
of it is dated 3 September, from the install of that day, and nothing was written there by
today's sync. `C:\builds` exists and holds a folder belonging to another project, with no
`hexboard` folder in it. A sync alone need not create a module's build folder, so the question
only settles once something is actually built; re-checked after the run.

**Before the run — the report address was set, on the user's instruction.** The "Report a
problem" entry reads its destination from `android/local.properties`, and the entry is absent
from the app entirely where no address is set, so the feature could not otherwise be seen this
sitting. The user was offered the choice of adding the line himself and asked Claude to do it.
The file is gitignored, re-confirmed with `git check-ignore`, so no address reaches the public
repository. `android/local.properties` was outside the run's file list and the safety check
refused the edit; the path was added to the working file's Files section first, which is the
route that exists for exactly this.

**Step 3 — the first compile of the app's own Kotlin failed on one error, on 2026-09-09 at
09:15.** `MainActivity.kt`: *Argument type mismatch: actual type is 'String', but 'Key' was
expected.*

**A third defect from the same uncompiled run, and the most interesting of the three.**
[emoji-panels] gave `HexboardBoard` an `onEmoji: (String) -> Unit` parameter on 2026-09-04, and
placed it after `onKey`. A trailing lambda in Kotlin binds to the *last* parameter, so the app
screen's `{ key -> typed = apply(key, typed) }` stopped attaching to `onKey` and began
attaching to `onEmoji` — where the parameter is a String. The call still read correctly and
meant something entirely different.

Fixed by passing `onKey` as a named argument rather than as a trailing lambda, with the reason
written beside it. Every other call site was checked in the same pass: the input method service
and all four instrumented tests already name their arguments, so `MainActivity` was the only
one exposed.

**Step 3, second attempt — it built and installed on 2026-09-09.** So the app's own Kotlin
compiles across all fourteen items of this run and the eleven before them.

**Step 2, revisited and passed.** `C:\builds\hexboard` now holds `generated`, `intermediates`,
`kotlin`, `outputs` and `tmp`, all written by that build, and nothing was written into
`android/app/build` at all — everything still in that folder is dated 3 September, from before
the relocation existed. So [build-output-off-drive]'s own observation is made. The stale folder
is a leftover rather than a live one.

**Step 4 — the layout picker: all seven layouts present, English selected.** Reported by the
user. The seven read QWERTZ (German), QWERTY (English), QWERTY (Spanish), AZERTY (French),
QWERTY (Italian), QWERTY (Portuguese), ЙЦУКЕН (Russian), grouped under their languages.

**And a defect this run created, found in the same breath and fixed on the user's approval.**
The app screen stacked its controls in a fixed column with no scrolling. That held when the
screen carried one button and the dictation test; the layout picker and the report entry made
it taller than the phone, so the dictation availability line was cut off at the bottom edge and
the report entry could not be reached at all — which would have made two of this sitting's own
checks impossible to perform.

The controls column now scrolls and carries a weight, so the board keeps its own space at the
bottom rather than being pushed off by the controls above it. `Arrangement.SpaceBetween` went
with it, since the weight does that job now.

**Step 5 — the report entry works, on 2026-09-09.** With a description typed in, the composed
report read `Layout: qwerty-en`, `Hexboard: 1.0`, `Android: 17`, `Device: Pixel 6` and the
typed description, and nothing else; the button enabled; the mail app opened with that text in
the body and the configured address in the To field, unsent. That is
[layout-error-report]'s own observation made on the handset, including the half it could only
be checked for on a phone.

**And a second finding on the way there, filed as [app-screen-two-keyboards].** The user typed
on the preview board at the bottom of the app screen expecting the report box to fill, and the
characters appeared in the scratch line below the button instead. The preview board is
scaffolding with no input connection; the report box is a real field that opens whichever
keyboard the phone has selected. Nothing was wrong with the board until a screen with a real
field grew under it.

**Steps 6 and 7 — Hexboard switched on and selected.** It was off in the phone's keyboard list
on 2026-09-09, and whether the reinstall cleared it or the user switched it off after the last
sitting is not known; the instrumented tests later in this sitting reinstall the app, which
answers it for free. Switching it on produced Android's two standard dialogs, the second of
which stated a fact no document held — filed as [direct-boot-unavailable].

Selecting it then took a second finding: the user reported Hexboard absent from the
"Change Keyboard" card, and it was on it, third, with its own name as the small line. Filed as
[switcher-subtype-label].

**Step 8 — three visual checks, one pass and two findings.** The empty band above the top row
is there. The keys have no outline, and the user's judgment on the fade and on the label size
is filed as [key-drawing-second-pass]; the row banding does not read as banding, filed as
[row-banding-too-weak]. Both were captured rather than fixed, on the user's decision: the three
constants involved are coupled, each change wants a look on the phone, and one of the changes
reverses a decision [soft-key-edge] made deliberately. That reversal was put in front of him
rather than absorbed quietly.

This is [soft-edge-fraction-values]'s own observation being made. Its walkthrough step said an
opinion either way was the result, and the opinion is that the values are wrong — which is a
successful check rather than a failed one.

**Step 9 — shift works, and two more findings.** Letters rest in lowercase and turn to capitals
on a shift tap, and RARE's bottom row starts at the left edge, so [shift-behaviour] and
[rare-row2-unindent] are both confirmed on the handset.

The emoji panels arrive on a finger-*up* swipe, not down. The user confirmed up is correct, so
nothing is wrong with the build; SPEC's phrase "reached by vertical swipe down" describes where
the panels sit rather than what the hand does, and the walkthrough step written from it asked
for the wrong gesture. Filed as [spec-emoji-swipe-wording].

And the seam between panels is still visible, which is the user's complaint of 2026-09-03
returning. [symbols-panel-empty-slots] diagnosed that complaint as the config's empty slots and
cleared the pager after finding it sets no `pageSpacing`. The empty slots were real and are now
filled; the seam is a second cause and survives them. Across a page boundary two column centres
sit `2 * radius + 2 * EDGE` apart, about 2.55 radii, against `horizontalStep` of about 1.81
radii within a panel — roughly 40% wider. Filed as [panel-seam-gap], with the arithmetic.

**Step 10 — the symbols panel is full.** Forty keys, no gaps, with the four curly quotes near
`[` and `@`, the bullet, `≈` among the operators, and `¢` `←` `→` along the bottom row. That is
[symbols-panel-empty-slots]'s own observation made on the phone.

**The key and label size judgment [soft-edge-fraction-values] was shipped blind to make was
given at step 8** — "way too small" — and is carried in [key-drawing-second-pass], so it is not
asked again here.

**Step 11 — sideways, the height bound holds and exposes something else.** The board plus the
row above it fits inside half the screen, which is [landscape-board-height]'s own observation
made. What it revealed is that the board is now narrower than the screen for the first time,
with the spare width sitting empty on the right.

Claude first filed that as a choice between centring the board and splitting it. The user's
answer was neither: however much of the neighbouring panels fits in the spare width should be
showing. That is a better answer than either — it uses the space for the thing the space is
next to and costs no gesture — so the first capture was deleted and refiled as
[landscape-reveal-neighbours] with his framing. Recorded because the correction is the useful
part: the run had reached for the two obvious moves and the person looking at the screen saw a
third.

**Step 12 — the unit tests: 50 passed, none failed.** That confirms, on the desk rather than by
reasoning: `KeyEdgeTest`, `NeighbourTableTest` over both shipped configs, `WordListTest`,
`ProblemReportTest`, `LayoutCatalogueTest`, `BoardHeightBoundTest`, `EmojiCatalogueTest`, and
`KeyLayoutValidationTest` now running over all seven layouts, alongside `RowTintTest` and
`SharedRadiusTest` from the previous run. Every UNCONFIRMED tick this run wrote for a JVM test
is now confirmed.

**Step 13 — the instrumented tests, and three more compile defects before they would run.** The
`androidTest` source set had never been compiled either. `click` is an extension on
`TouchInjectionScope` rather than a member, and was used without importing it in
`EmojiPanelsUiTest` and `KeyConfigUiTest`; `onAllNodes` is the reverse case — a member of the
test rule, with nothing at that name to import — and `NavigationBarInsetTest` and
`StripLayoutTest` both imported it anyway. Four files, all from the 2026-09-04 run, all outside
this run's file list; the user approved adding them to scope and they were fixed.

**Then it ran: 25 tests, 13 passed, 12 failed.** Every failure is a test that renders the
board, and every pass is one that does not. The message is `No compose hierarchies found in the
app`, thrown out of `fetchSemanticsNodes` before any gesture happens. The board itself is not
broken — it was typed on by hand the same morning — so the fault is in how the harness stands
it up. Filed as [instrumented-tests-no-composition], with the three hypotheses worth trying
first and the reason it wants a run of its own rather than another round here: each one costs a
build, an install and a device run, and none of that is reachable from Claude's shell.

## Outcome

**Done — walked to its end.** The item's observable was the report itself, and the report is
this record. What it set out to do it did: it found seven defects that no amount of reading
would have found, in code three separate runs had written and none had compiled.

Three in the Gradle script and the app's own Kotlin, all from the run of 2026-09-04:
`java.util.Properties` unresolvable in a build script, an array-plus-list expression that
resolves against neither overload, and a trailing lambda that had silently rebound from `onKey`
to `onEmoji`. Three more in the instrumented tests, the same shape. And one this run created,
found by the user rather than by a test: the app screen had grown taller than the phone with no
way to scroll to the bottom of it.

Eight captures came out of the sitting as well, six of them from looking at the thing rather
than from anything failing.

**What is proved, and what is not.** Proved on the handset: it builds, installs, runs, types;
seven layouts are shipped and selectable; the symbols panel is full; shift, the emoji panels,
RARE's unindented row and the navigation-bar band all behave; a problem report composes and
reaches the mail app; the build output lands off the synced drive; and the landscape height
bound holds. Proved on the desk: 50 unit tests. Not proved: the instrumented suite, which now
compiles and has its own item.

**One thing the sitting could not settle, recorded so nobody re-derives it.** Hexboard was
switched off in the phone's keyboard list at the start of the day, and whether the reinstall
cleared it or the user switched it off after the previous sitting is unknown. The instrumented
run later reinstalled the app, which would have answered it for free — but that run failed to
compile before installing anything, so the question is still open.

**Files touched:** none by this item directly. The fixes it produced are recorded against
`android/app/build.gradle.kts`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`
and the four instrumented test files, in the chat-level record for this session.

**Routed to Captures:** [instrumented-tests-no-composition], [landscape-reveal-neighbours],
[panel-seam-gap], [spec-emoji-swipe-wording], [key-drawing-second-pass], [row-banding-too-weak],
[direct-boot-unavailable], [switcher-subtype-label], [app-screen-two-keyboards],
[picker-language-order].


