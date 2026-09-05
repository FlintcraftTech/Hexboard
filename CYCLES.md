# CYCLES

Work that comes round again, rather than being done once. Each definition names the
artifact it works on, the steps of one turn, how often a turn is due, and the observable
that says a turn has been completed. Due-ness is recomputed from that observable every
time it is checked — nothing here stores a position, so a missed check costs nothing and
no state file can be wrong about where things stand.

Created 2026-09-05, with this project's first cycle.

---

## Refresh the bundled emoji list [emoji-data-refresh]

**Artifact:** `resources/emoji-test.txt` — Unicode's own published emoji list, bundled and
shipped into the app's assets, from which the five emoji panels and the whole-list browser
are filled.

**Why it is a cycle rather than a one-off.** Unicode publishes a new emoji version on a
roughly annual rhythm, and a keyboard quietly offering a stale emoji set is the kind of
failure that shows up as a bug report years later. [emoji-panels] named the refresh as an
accepted cost when bundling Unicode's list was chosen over adopting Jetpack's picker, and
that item has shipped and left the queue, so without this definition the obligation lives
only in a log entry nobody reads on a schedule.

**Cadence: annually.** Derived from Unicode's own publication rhythm rather than declared —
its emoji releases have come at roughly yearly intervals, read from the directory listing at
`unicode.org/Public/emoji/` on 2026-09-04, where the highest published version was 16.0 and
the bundled file is that same version.

**The steps of one turn:**

1. Read the directory listing at `unicode.org/Public/emoji/` and note the highest published
   version.
2. Where that version is the one already bundled — read from the header of
   `resources/emoji-test.txt`, which records its version and date — the turn is complete
   with nothing changed. Record it as a completed turn and stop. **A turn that finds nothing
   is still a turn**, and recording it is what stops the next check re-deriving the same
   answer from scratch.
3. Otherwise download that version's `emoji-test.txt` and replace `resources/emoji-test.txt`
   with it.
4. Run the emoji parse guard — the unit test asserting the parse still yields enough entries
   to fill the panels and still finds its group headings. A format change shows up here
   rather than as panels quietly emptying.
5. Check whether the first 250 entries have shifted far enough that the panels rearrange
   under a user who knew where things were, and say so in the record either way. This is a
   judgment to report rather than a condition to satisfy.
6. Re-read the copyright year in the Unicode notice carried in `README.md`'s Notices
   section, which moves with the file. The licence permits the redistribution and the notice
   is already there — see `workshop/resources/research/unicode-data-file-licence.md` — so the
   year is the one part to correct rather than assume.
7. Record the turn, opening the record by saying that it records a completed turn of this
   cycle.

**The observable that marks a completed turn:** the most recent record in `LOG/` filed under
the slug `emoji-data-refresh` **whose opening line says it records a completed turn of this
cycle**. That wording is load-bearing: a planning session writes one record per item it
processes, named by that item's slug — including the record for authoring this definition —
so an observable reading "the most recent record under this slug" would count the authoring
record as a turn and report the cycle as run before it ever has been.

The file's own header date is deliberately **not** the observable, because it records when
*Unicode* published, not when this project last looked. It would never move on a turn that
found nothing.

**Writes:** `resources/emoji-test.txt`, `README.md`
