# Whether Unicode's own data files can be bundled in a public repository

Read on 2026-09-04 from `unicode.org/copyright.html` and the licence text it points at,
`unicode.org/license.txt`, for `[emoji-panels]`. That item's own "Rests on" line flagged the
Unicode Terms of Use as **not read**, and made reading them the condition of bundling
`emoji-test.txt` — this is that reading.

## What it says

Unicode Data Files are governed by the **Unicode License v3**, described on Unicode's own
terms page as a free and open-source licence. Its grant is permissive in the MIT mould:
permission is granted free of charge, to any person obtaining a copy of the data files, to
deal in them without restriction — including to use, copy, modify, merge, publish,
distribute and sell copies.

The condition attached is a notice condition, and it offers a choice of where the notice
goes: the copyright **and** permission notice must appear either with all copies of the data
files, or in the associated documentation.

## What that means for Hexboard

Bundling `emoji-test.txt` in this public repository, and shipping it inside the app, is
permitted. What it costs is a notice: the Unicode copyright and permission notice must
travel either alongside the file or in the project's own documentation. `README.md` already
carries a Notices section, which is the "associated documentation" limb of the choice and
the cheaper of the two to keep correct.

This is a narrower finding than the licence question `licence-options.md` answers: that one
is about the licence Hexboard itself is published under, and this one is about a third
party's data travelling inside it. Neither constrains the other.

## Assessment of this finding's own frame

- **Time range** — not applicable in the usual sense. A licence governs a version of a file,
  and the current Unicode License v3 governs the data files published under it now. What is
  not covered is whether an *older* archived data file carries different terms; nothing here
  proposes using one.
- **People** — the people this bears on are anyone who clones or forks this repository, and
  anyone who ships the app. The notice condition is what reaches them, and it is satisfied
  in the repository rather than by each of them individually.
- **Freshness** — Unicode's licence has been revised before (v3 is the current numbering),
  so this is a subject that does change, on no announced cycle. Worth re-reading if the
  bundled data file is ever refreshed to a newer Unicode release, which is the same moment
  the file itself is replaced.
- **Risk if wrong** — redistributing a third party's data in a public repository without the
  notice its licence requires. The remedy is cheap and the failure is quiet, which is the
  combination worth being deliberate about: the notice goes in as part of bundling the file,
  not afterwards.
- **Alternatives** — the alternatives to bundling Unicode's file were researched and ruled
  out in `android-emoji-sources.md`: Jetpack's `emoji2-emojipicker`, whose catalogue is
  internal so its content arrives only with its own scrolling-grid interface, and a
  hand-written list like the prototype's, which contradicts SPEC's own wording and goes stale
  with every Unicode release. This finding does not reopen that choice; it clears the one
  condition the chosen road was left waiting on.
