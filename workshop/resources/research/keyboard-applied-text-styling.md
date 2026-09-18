# Whether an Android keyboard can apply bold, italic and underline to text

Researched 2026-09-18 for `[selection-formatting-spans]`, from the user's complaint that
most places support bold, underline and italic and yet no keyboard offers them, so
formatting means copying text out, styling it elsewhere and pasting it back.

## What was asked

Two questions. Can an input method apply styling to text at all? And if it can, why does
no shipping keyboard do it?

## What was found

**The plumbing exists and is documented.** `InputConnection` carries a flag,
`GET_TEXT_WITH_STYLES`, described as being "for use with `getTextAfterCursor(int, int)` and
`getTextBeforeCursor(int, int)` to have style information returned along with the text".
`getSelectedText` takes the same flag — its `flags` parameter "May be either 0 or
`GET_TEXT_WITH_STYLES`" — so a keyboard can read the current selection with whatever
styling it already carries. In the other direction, `commitText` is documented as taking
"the composing text with styles if necessary", pointing at `Spanned` "for how to attach
style object to the text", with `SpannableString` and `SpannableStringBuilder` named as the
implementations. So the read-toggle-commit round trip a formatting control needs is a
supported path rather than a trick.

Read from an archived copy of the official Android SDK reference rather than from
`developer.android.com`, whose live page returned only its navigation index when fetched on
2026-09-18. The flag and both method signatures are long-standing rather than recent, so the
age of the mirror is not the risk it would be for a new API — but it is a mirror, and that
is stated rather than glossed.

**Why nobody ships it is a position rather than an obstacle.** The consistent answer, in
Google's own support threads and in how the platform is described, is that bold, italic and
underline belong to the app and the field rather than to the keyboard: the keyboard supplies
characters and each app invents its own way of styling them. A keyboard offering a bold
button would therefore be offering something an unknown proportion of fields would silently
discard.

**And that discarding is real, not hypothetical.** A ProseMirror issue records Gboard and
rich web editors already disagreeing about spans applied inside a word on Android — style
not sticking at all in one direction, and applying only after the following space in the
other. Web-based editors are the expected failure case, which matters because email and
document editors on a phone are frequently exactly that.

**The second route, not taken here.** Where styling will not survive, the same selection
could be wrapped in the characters an app reads as formatting — the asterisks WhatsApp and
Telegram accept. That is a per-app table and it fails visibly where the table is wrong. It is
`[selection-formatting-markers]`, which was split out on 2026-09-18 and designed
the same day: the conventions contradict each other — a single asterisk is bold
in WhatsApp, Telegram and Slack and italic in Discord — so the table is keyed by
the package name `EditorInfo` supplies, verified against the application's UID
from API 23 and therefore trustworthy at this project's API 26 minimum. Underline
has no marker in three of the four, so that control is absent rather than inert
where an app lacks it.

## What this does not settle

Which specific Android apps keep committed styling. Nothing here was run: there is no `adb`
on this machine and no route from it to the handset. The evidence standing in for it is the
user's own account of 2026-09-18 — his workaround ends by pasting styled text back into the
app he took it from, so that app keeps pasted styling, which is the same path a commit
takes. That is one app's worth of evidence and it is not a survey.

## Frame

- **TIME RANGE** — not applicable; the product addresses no particular period.
- **PEOPLE** — applies to anyone typing on the keyboard, which is the whole audience. The
  finding is about platform behaviour rather than about a population, so nothing here is
  narrower than the users it serves.
- **FRESHNESS** — the `InputConnection` half is stable API surface and unlikely to move. The
  which-apps-honour-it half is not: apps change their editors, and a table of them would go
  stale continuously. That asymmetry is the reason the marker route was split off.
- **RISK IF WRONG** — if ordinary Android fields turn out not to keep committed styling, the
  feature does nothing anywhere and the build is wasted. The cost is bounded — three
  controls and one pure-function file — and the failure is silent rather than damaging, so
  this warrants the user's own look at where his workaround works rather than a red flag or
  a cycle.
- **ALTERNATIVES** — two were considered and one ruled out here: markers wrapping the
  selection, split out as its own entry; and an armed toggle applied before typing rather
  than to a selection, refused because the user named the selection case as the one he hits.
  A per-app rich-text engine of Hexboard's own was never considered and is named as such.
