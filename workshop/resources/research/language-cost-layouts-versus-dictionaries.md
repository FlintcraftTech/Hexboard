# What a further language actually costs, and how optional assets reach a phone

Researched 2026-09-05, for [language-list-choice]. The question came from this
project's owner: does a language bring its whole dictionary with it, and should
languages be downloaded and activated separately rather than shipped in the app?

## The two halves of a language, and they differ by two orders of magnitude

- **The layout** — which letters sit where. Measured in this repository on
  2026-09-05: `key-layout.json` is 17,520 bytes and `key-layout-ru.json` is
  17,039. For comparison the bundled `emoji-test.txt` is 640,215 bytes, thirty-
  seven times one layout. Fifty layouts would add under a megabyte before
  compression.
- **The dictionary** — the word list the predictive engine corrects against.
  Order of a megabyte or two per language before compression. **This figure is
  an estimate from typical word-list sizes and has not been measured against a
  distribution**; it is stated as an estimate deliberately, because the decision
  it supports only needs the two halves to differ by orders of magnitude, which
  they plainly do.

So a layout is free and a dictionary is not, and treating "supporting a
language" as one unit hides the whole cost.

## There is no SCOWL for other languages: it is one licence hunt per language

SCOWL, already cleared for English in `word-list-licence-and-frequency.md`, is
English-only. The standard multilingual source is the Hunspell dictionary set,
and its structure is the problem rather than its quality: **Hunspell the library
is tri-licensed LGPL/GPL/MPL, while each language's dictionary is a separate
work with its own licence**, and Hunspell's own documentation tells integrators
to contact the wordlist author to confirm the licence is acceptable. Read on
2026-09-05.

That is the answer to "is this cheap or expensive": adding a language's
**layout** is cheap and repeatable; adding its **dictionary** is a fresh licence
question each time, of the kind that has already consumed a session apiece for
the Unicode emoji data and for SCOWL.

**A layout without a dictionary is still useful**, which is what makes the split
worth having: the keys work and you can type; only the correction is absent.

## Optional assets can reach a phone without the app making a network request

Google Play Asset Delivery. Read from Android's own documentation on 2026-09-05.

- Asset packs are containers of assets with **no executable code**.
- Three delivery modes: **install-time** (counts toward the app's download
  size), **fast-follow** (downloaded automatically after install, does not count),
  and **on-demand** (downloaded while the app runs, does not count).
- **Google Play performs the delivery; the app makes no network requests of its
  own** and no server or CDN is needed. That is the property that matters here:
  a keyboard that ships no internet permission can still gain assets after
  install.
- It requires publishing as an **Android App Bundle through Google Play**. An
  APK distributed any other way cannot use it.

**Stated rather than glossed: the documentation frames asset packs as a feature
for games**, describing them as containers of game assets replacing legacy
expansion files. Nothing read says a non-game app may not use them, and the
mechanism is an app-bundle feature rather than a games programme, but the
framing is what the documentation carries and no non-game example was read.

## What this does not settle

The actual size of a real word list for a real language, which nobody has
measured. Whether any single multilingual source exists under one permissive
licence — the search found the Hunspell set and its per-language licensing, not
an exhaustive survey. And whether a keyboard shipped outside Google Play, which
this project has not decided about, would lose the delivery route entirely.

## Frame assessment

- **TIME RANGE** — Not applicable to the size measurements, which are facts about
  files as they stand. The delivery mechanism is a current platform feature and
  is dated below.
- **PEOPLE** — Applies to anyone typing a language other than English, which is
  the audience the question is about. It says nothing about which languages those
  people actually want, which is the separate question [language-list-choice]
  still holds.
- **FRESHNESS** — Play Asset Delivery is amended on a cycle: modes, size limits
  and programme terms have changed before and the size limits were not read here,
  only that they exist. Re-read before anything is built on it. Hunspell's
  per-language licensing is structural rather than versioned and is unlikely to
  move.
- **RISK IF WRONG** — Being wrong about a dictionary's licence means
  redistributing data this public repository has no right to, which is the same
  exposure the emoji and SCOWL reads were done to avoid; that is the claim to
  re-read per language rather than trust here. Being wrong about asset packs
  costs a design that has to be replaced by shipping everything in the app —
  recoverable, since that is the fallback anyway.
- **ALTERNATIVES** — Considered and set aside: shipping every dictionary in the
  app (works, and grows the download by megabytes per language); the app
  downloading dictionaries itself (needs an internet permission on a keyboard and
  a server, against this project's whole posture). Never investigated: whether a
  dictionary can be generated rather than sourced, and whether the predictive
  engine could work usefully from a much smaller list than a full spell-check
  dictionary.
