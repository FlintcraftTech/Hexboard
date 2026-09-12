# f83f5d9 — [emoji-parse-guard] three assertions that are proportions rather than counts, so a legitimate refresh cannot break them

This session ran across 2026-09-05 and 2026-09-09.

`EmojiCatalogue.parse` reads the codepoints before the first semicolon and keeps only entries whose status column says `fully-qualified`. Both are stable in the format as it stands, and a format change would not fail anything: a moved column, a renamed status or a different comment convention would simply make the parse return few entries or none, and the symptom would be emoji panels arriving empty on somebody's phone, long after the file was swapped. This is the standing guard against that, split out of the refresh cycle so it runs on every build rather than once a year.

**Why the assertions are derived rather than invented** is the part worth keeping. "At least 3,781 emoji" is a bare number that goes wrong the next time Unicode adds any; an exact count fails on every legitimate refresh, which trains people to edit the test instead of reading it. So each of the three is a proportion or a requirement the app already has. The parse yields at least as many entries as the panels display — a figure `EmojiCatalogue` computes from its own `ROWS` and `COLS`, since fewer than that means the board cannot be filled. The file carries more than one group heading, since the groups are what the whole-list browser is organised by. And the parsed count is at least half the file's non-comment lines: that is the one that catches a moved status column, because a wrong column reads as an unrecognised status and drops nearly everything, taking the ratio to near zero, while the ordinary mix of fully-qualified and lesser-qualified spellings sits well above half.

Against the bundled Unicode 16.0 file all three hold with room: 3,781 entries against a panel capacity of 250, ten group headings, and a recognised share of 0.750 against a floor of a half.

One small departure from the item's wording. It said "the parse finds more than one group heading"; `parse` returns emoji and no groups, so the test counts the headings in the file's own lines instead — which is what a format change would break and what the assertion was after.

Step 4 of the `emoji-data-refresh` cycle runs this test as part of every turn, and that ordering is written in both places.

**Files touched:** `android/app/src/test/java/tech/flintcraft/hexboard/EmojiCatalogueTest.kt`.

**Routed to Captures:** none.
