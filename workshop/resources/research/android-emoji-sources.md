# Where an Android keyboard gets its emoji from

Researched 2026-09-02, for `[emoji-panels]` — the question being what SPEC's phrase "the emoji panels are system-supplied content" can actually mean on Android.

## What Android offers

`androidx.emoji2:emoji2-emojipicker` is the Jetpack library for emoji input. It supplies an up-to-date emoji list, skin-tone variants, recently-used tracking, and consistent rendering across OEM devices and older Android versions, and it works alongside `emoji2`/EmojiCompat, which exists to stop newer emoji rendering as tofu on Android 11 and below.

**It supplies all of that only through its own view.** `EmojiPickerView` is a vertical scrolling grid with a clickable horizontal category header, configured by `emojiGridColumns` and `emojiGridRows`.

**The emoji list is not public data.** Read from the library's declared API surface on 2026-09-02 (`emoji2/emoji2-emojipicker/api/current.txt` on the androidx repository): the public types are `EmojiPickerView`, `EmojiViewItem`, `RecentEmojiProvider`, `RecentEmojiAsyncProvider` and `RecentEmojiProviderAdapter`. `EmojiViewItem` carries one emoji and its variants; the recent-emoji interfaces handle only what the user has picked. The catalogue and its categories are internal to the view. So the library is take-it-or-leave-it: Google's list arrives with Google's user interface, or not at all.

## The consequence for Hexboard

SPEC promises five emoji panels reached by a downward swipe — Hexboard's own arrangement — and separately says emoji content is system-supplied rather than hand-curated. Those cannot both be satisfied by this library: adopting it means adopting a scrolling grid and abandoning the panels.

## The third source, which is what was chosen

Unicode publishes the emoji list itself. `emoji-test.txt`, under `unicode.org/Public/emoji/<version>/`, lists every emoji in CLDR display order — the order keyboards actually use, not codepoint order — grouped into groups and subgroups, with a qualification status per entry. Its own header describes it as data for testing which emoji forms should be in keyboards, which is the use here. `emoji-sequences.txt` and `emoji-zwj-sequences.txt` alongside it carry the sequence types (keycaps, flags, tag sequences, modifier sequences).

This gives the content without the user interface, so Hexboard keeps its five panels and its geometry. It is the same move the project already makes for layouts, which SPEC says are transcribed from open data rather than invented here.

**What it does not give**, and these become work rather than gifts: skin-tone variants, recently-used tracking, and the rendering guarantee EmojiCompat provides on older Android versions. That last one is separable — `emoji2` can be used for rendering without the picker.

## Licence

Unicode's data files are covered by the Unicode Terms of Use at `unicode.org/terms_of_use.html`. **Not read in full on 2026-09-02** — flagged here for the same reason the Leipzig licence was flagged in `word-list-licence-and-frequency.md`: a licence reported second-hand is not a licence checked, and this repository is public.

## Assessment of this finding's frame

- **TIME RANGE** — the emoji list is versioned and Unicode revises it roughly annually; this finding describes the sources rather than a snapshot of the content, so it ages slowly. The androidx API surface was read on the day.
- **PEOPLE** — applies to anyone typing emoji on Hexboard, which is the general user. Nothing here turns on who they are.
- **FRESHNESS** — the androidx API could gain a public data accessor in a later release, which would reopen the choice. Worth re-checking if the emoji work is ever revisited rather than trusted as permanent.
- **RISK IF WRONG** — if the API surface was misread, Hexboard bundles data it could have got from the platform: wasted work and a list to maintain, not a broken product. If the licence turns out to restrict redistribution, a public repository is shipping data it should not, which is the serious one and is why it is flagged unread.
- **ALTERNATIVES** — three were considered and named: the Jetpack picker (rejected, brings its own UI), Unicode's own data (chosen), and a hand-written list like the prototype's 250 (rejected, contradicts SPEC and goes stale). Parsing the system emoji font to discover what a device can render was not investigated.
