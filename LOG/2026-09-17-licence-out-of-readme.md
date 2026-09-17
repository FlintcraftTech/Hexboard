# [HASH] — [licence-out-of-readme] created and cleared second in the region: the README's licence summary and its three third-party notices move out to a new LICENSING.md, with the two queue entries and the emoji cycle that cite the Notices section repointed in the same move

Date: 2026-09-17 15:50

A planning session that opened on a queue with nothing left to process — all seven
waiting captures passed over, five behind other entries and two behind dates — and
which therefore did its only work on something the user raised mid-session: moving
the licence out of `README.md` into a file of its own.

**What the section turned out to be, which is what shaped the item.** It is not one
thing. Lines 78 to 148 of a 148-line README are licensing, and they divide into a
plain-terms summary of the PolyForm Noncommercial licence and a `### Notices`
subsection carrying three third-party attributions — FlorisBoard for the Russian
layout, SCOWL for the word list, Unicode for the emoji list, two of them quoting a
copyright block verbatim. The notices are the bulkier half and they are load-bearing:
SCOWL's licence requires its notice to travel with derived works, and the Unicode
License v3 requires its notice to appear either with the data files or in the
associated documentation, which is the limb this project chose and recorded in
`workshop/resources/research/unicode-data-file-licence.md`. A separate document in
the same repository is still that documentation, so the condition survives the move
provided the blocks move verbatim. The user was asked whether the move took the
notices too and recommended it should; that was the one genuinely open question and
it is why the interview closed on it rather than on a disposition.

**Why `LICENSING.md` and not `LICENCE.md`.** The authoritative licence text already
sits in `LICENSE` at the root and is untouched by any of this. A second file one
letter away from it is a file a reader opens the wrong one of, and "licensing" also
covers the third-party notices, which "licence" alone does not.

**What stays in the README is two sentences, and the second is the point.** That
Hexboard is source-available under PolyForm Noncommercial 1.0.0, linking `LICENSE`;
and that this is deliberately not open source under the OSI definition. The second is
the fact a reader is most likely to be surprised by later, so it stays in front of
them rather than behind a link.

**The ripple was most of the work, and finding it was a grep rather than a judgment.**
Three documents send a reader or a build to "`README.md`'s Notices section". Two are
queue entries — [tap-word-alternatives], which adds CMUdict's acknowledgment, and
[russian-panel-gaps], which adds CLDR's — and amending a queue entry's own wording is
planning work rather than a build's, so both were repointed at the decision step in
this session instead of being left for the build. The third is the `emoji-data-refresh`
cycle in `CYCLES.md`, whose step 6 sends a future turn to re-read Unicode's copyright
year there and whose `Writes:` field names `README.md`; that one went into the item's
own Files line, alongside the Unicode research file and its index line, so those
documents change in the same move as the file they will point at rather than naming it
before it exists. The queue entries could not be handled that way, which leaves a
window in which they name a file that does not yet exist — closed by ordering rather
than by a field, the item sitting second in the cleared region so it lands before
either of them can be built.

**Why second rather than first, and why nothing was added to [nested-wrap].** That
item is `[freeform]` and `Runs alone`, is done by hand in a chat of its own, and moves
every path the queue names. Placing this one after it means `LICENSING.md` does not
exist when the wrap runs, so the wrap's file-by-file split needs no new entry, and
this item's own paths are rewritten by the wrap along with everything else.

**Queue changes:** [licence-out-of-readme] written and moved into Processed, cleared
to run, second in the region after [nested-wrap]; [tap-word-alternatives] and
[russian-panel-gaps] each had their `README.md` Files line repointed to `LICENSING.md`
with the ordering written into both; the spent forward-advisory naming [nested-wrap]
deleted from Unprocessed at the opening, having oriented this session. Twenty-three
items now cleared to run, nine held below the line, seven captures waiting and every
one of them passed over.

**Work processed:** kept — [licence-out-of-readme]. Deleted — none.

**Advisory:** filed — nested-wrap

**Also in this chat:** the opening narration used the word "today" with no source
behind it; the safety check stopped it and the sentence was re-sent naming the two
dates the flags actually rest on. The below-the-line revisit lifted nothing, every
held item's blocker being cleared to run rather than shipped. The digest's seven
placement flags were reported and left standing: all seven are captures naming an item
now cleared to run, and all seven are themselves passed over, so they are a prompt to
re-read a premise before a build reaches them rather than anything actionable. The
`emoji-data-refresh` cycle is not due — its first turn completed on 2026-09-12 against
an annual cadence. The issue channel was checked in all three limbs and nothing has
moved since the anchor; the mailbox is empty.
