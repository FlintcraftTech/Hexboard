# Hexboard

An Android keyboard built around circular keys in a hexagonal tessellation.

![The Hexboard QWERTY panel: circular keys packed in a hexagonal grid, with odd columns sitting half a key lower than even columns](resources/images/hexboard-layout.png)

## What it is, and why

Hexboard is plain QWERTY with one change: the rows zig-zag. Odd columns sit half a key
lower than even columns, which packs the keys into a hexagonal grid instead of a
rectangular one. Hexagonal packing fits more area into the same space, so every key is
bigger than it would be on a conventional layout of the same size. The letters stay
exactly where you expect them — you give up straight rows, and you get larger keys in
exchange.

The keys are drawn as **circles**, and that is the part that matters most.

Several keyboards already use hexagonal tessellation — Typewise, MessagEase, Thumb-Key —
and they draw the keys as hexagons, corners and all. Hexboard's claim is about
perception, not routing: a person aiming at a circle aims more confidently than a person
aiming at a shape with visible corners, so they aim closer to the centre, and their taps
land more accurately as a result. A corner is a place your eye can aim at that isn't the
middle of the key.

That is a claim about people, so it is testable rather than proven. If it turned out that
the effect wasn't there, or was too small to matter, the idea wouldn't survive it. It is
worth being precise about what is *not* being claimed: hex-shaped keyboards do not
mis-route taps, and Hexboard doesn't say they do. The difference is in where you aim, not
in what the software does with the tap.

## Status

In development, and honestly early. There is **no working keyboard yet** — the Android
app is a fresh Kotlin / Jetpack Compose build that doesn't type anything so far. There's
nothing to install.

## Try the prototype

`hexboard17.html` is a browser prototype. Download or clone the repo and open that file in
any browser — no build step, no dependencies. It's the only part of Hexboard you can
actually type on today.

It demonstrates the layout, the swipe gestures between panels, and the full key inventory:
three letter panels (RARE / QWERTY / SYMBOLS) reached by swiping horizontally, emoji
panels by swiping down, and long-press accents on letters that need them.

The prototype is **frozen** — a reference for what the design intends, not the product and
not a maintained app. The real key data now lives in
[`resources/key-layout.json`](resources/key-layout.json), which the Android build reads.

One more page is kept in the repo without being maintained.
[`planning/hexboard-editor.html`](planning/hexboard-editor.html) is a prototype-era layout
editor: keys are dragged between slots on the real zag geometry, with the structural keys
locked. It is superseded — it exports JavaScript fragments for pasting into the prototype,
which is where key data lived before the config existed — and it is kept as prior art for
a future contributor-facing editor, since the drag-and-drop half of that job is already
worked out here.

## Building the Android app

The app in `android/` builds with Android Studio and needs no setup beyond opening it. One
optional setting: adding a `hexboard.buildDir` line to `android/local.properties` — for
example `hexboard.buildDir=C:/builds/hexboard` — puts Gradle's build output at that path
instead of inside the project, which is worth doing where the project folder sits in a
synced drive or deep enough to run into Windows' path length limit. `local.properties` is
never checked in, so the setting stays on your own machine, and leaving the line out builds
exactly as before.

## The planning record is public on purpose

[`LOG/`](LOG/) and [`QUEUE.md`](QUEUE.md) are the working record of how this project is
being designed — every session, every decision, and the reasoning behind each one,
including the ideas that were considered and rejected.

They're tracked in this repo deliberately. Hexboard is built using
[Throughliner](https://flintcraft.tech), a method for building software with Claude Code,
and this repo is a worked example of it: a real project with the planning left in rather
than tidied away. If you're more interested in how software gets decided than in how it
gets typed, start there.

## Licence

Hexboard is source-available under the [PolyForm Noncommercial License 1.0.0](LICENSE).

In plain terms:

- **You may read the source.** It's here to be read.
- **You may fork it to build Hexboard for another language or key set.** That's an explicitly welcome use, and the key inventory lives in `resources/key-layout.json` precisely so it can be varied without touching the code.
- **You may not use it commercially.** Selling Hexboard, or anything built from it, is not permitted. Personal use, hobby projects, study, and use by charities, schools, and public institutions all are.

Source-available is not the same as open source: this licence restricts commercial use, so Hexboard does not qualify as open source under the OSI definition. That restriction is deliberate.

The licence text in [LICENSE](LICENSE) is the authoritative version; this summary is not a substitute for it.

### Notices

The Russian layout in `resources/key-layout-ru.json` is transcribed from the layout and
popup data of [FlorisBoard](https://github.com/florisboard/florisboard), which is licensed
under the Apache License, Version 2.0. The file names the two source files it was read from.

`resources/emoji-test.txt` is Unicode's own published emoji list, redistributed unmodified,
and the emoji panels are filled from it. It carries Unicode's copyright notice in its own
header. The Unicode License v3 permits redistribution on the condition that its copyright and
permission notice appear either with the file or in the documentation, so:

> Copyright © 1991-2024 Unicode, Inc. All rights reserved.
>
> Permission is hereby granted, free of charge, to any person obtaining a copy of data files
> and any associated documentation (the "Data Files") or Unicode software and any associated
> documentation (the "Software") to deal in the Data Files or Software without restriction,
> including without limitation the rights to use, copy, modify, merge, publish, distribute,
> and/or sell copies of the Data Files or Software, and to permit persons to whom the Data
> Files or Software are furnished to do so, provided that either (a) this copyright and
> permission notice appear with all copies of the Data Files or Software, or (b) this
> copyright and permission notice appear in associated Documentation.
>
> THE DATA FILES AND SOFTWARE ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
> IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
> PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE
> COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL
> INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE,
> DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
> ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THE DATA FILES OR SOFTWARE.
>
> Except as contained in this notice, the name of a copyright holder shall not be used in
> advertising or otherwise to promote the sale, use or other dealings in these Data Files or
> Software without prior written authorization of the copyright holder.

The full terms are at [unicode.org/license.txt](https://www.unicode.org/license.txt).
