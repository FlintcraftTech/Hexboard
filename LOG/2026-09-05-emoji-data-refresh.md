# 5f9d97e — [emoji-data-refresh] deleted into the project's first cycle, and a standing parse guard split out of it

The bundled `emoji-test.txt` is pinned at Unicode 16.0, dated 2024-08-14 in its own header, and nothing said when or how it gets replaced. [emoji-panels] named the refresh as an accepted cost when bundling Unicode's list was chosen, but that item shipped and left the queue, so the obligation survived only in a log entry nobody reads on a schedule. The item's own three options were a cycle, a dated capture each time a release lands, or whoever notices — and it was blunt that today's answer was the third, chosen by nobody.

The cycle was taken, creating `CYCLES.md`. The other two both depend on someone happening to notice a release, which is the failure rather than the fix. The one-time cost is a cycles line at every session opening, which is what makes the check impossible to forget. This also became more visible during the same session: [emoji-panel-reach] makes every emoji in the file reachable rather than only the first 250, so a stale file now means every gap is somewhere a user can look.

One trap was designed around rather than discovered. The obvious marker for a completed turn is the date in the file's own header — but that is *Unicode's* publication date, not this project's, so it would never move on a turn that found nothing. The observable is instead a log record under the cycle's slug whose opening line says it records a completed turn, which is also what stops a planning record about the cycle being counted as the cycle having run.

[emoji-parse-guard] was split out as a standing test rather than a step inside the turn: the item warned that a newer file must not be assumed to parse the same way, and a format change would show up as panels quietly emptying. Inside the turn that check runs once a year; as a test it runs on every build.

**Queue changes:** [emoji-data-refresh] deleted from Unprocessed and re-authored as a cycle definition in a new `CYCLES.md`; [emoji-parse-guard] created and cleared to run.

**Work processed:** kept — [emoji-parse-guard]. Deleted — [emoji-data-refresh], into the cycle.
