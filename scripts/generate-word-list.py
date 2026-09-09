#!/usr/bin/env python3
"""Generate resources/wordlist-en.txt from a SCOWL distribution.

Run once by hand, not at build time. The generated list is committed alongside the
key configs and Unicode's emoji data, so a build never depends on a network or on
somebody else's server staying up.

    python scripts/generate-word-list.py --scowl <path to unpacked SCOWL>

Why SCOWL, recorded in workshop/resources/research/word-list-licence-and-frequency.md:
its licence permits redistribution inside this repository, and — the part no other
candidate offered — it separates proper names into categories of their own. SPEC's
rule that the shipped dictionary carries no proper nouns is therefore satisfied by
leaving those categories out here, rather than by a filter that guesses at what is a
name.

That separation turned out to leak in both directions, found on 2026-09-05 by reading
the generated list rather than trusting the categories, and the two corrections below
are backstops behind SCOWL's own categories rather than a replacement for them:

  - ten American place-name possessives sit in `american-words.50`, the ordinary-words
    file, rather than in `proper-names`. So an entry whose first letter is a capital
    followed by a lowercase letter is dropped. That is a shape rather than a judgment
    about what a name is: in a list built from lowercase word categories, nothing else
    takes that shape. Checked against the generated list, it removes exactly those ten
    and leaves I'd, I'll, I'm, I've, OK and OK's forms alone.
  - the pronoun `I` is in `english-upper.10`, filed with American, England and John,
    because that category is words that are capitalised. Excluding names excludes it.
    It is added back by name. A dictionary without `I` would leave the engine free to
    change a typed `I` into something else.

SCOWL carries no frequency data. What it has is size levels, and a word's level is a
coarse commonness ranking, so the level travels into the list beside each word and is
what the engine breaks ties with. A real frequency table is a later question with an
item of its own.
"""

from __future__ import annotations

import argparse
import datetime
import re
import sys
from pathlib import Path

# SCOWL's own category names. `words` is ordinary English; `contractions` carries
# don't, we'll and the rest, which must be present or the keyboard would try to
# correct them. `upper` and `proper-names` are the two that hold names, and they are
# the reason this project chose SCOWL — they are simply not listed here.
CATEGORIES = ("words", "contractions")

# The spelling variants to take. Both national spellings ship, so the keyboard never
# corrects colour to color or the other way about.
SPELLINGS = ("english", "american", "british")

# A SCOWL final/ filename: <spelling>-<category>.<level>
FILENAME = re.compile(r"^(?P<spelling>[a-z_0-9]+)-(?P<category>[a-z-]+)\.(?P<level>\d+)$")

# An initial capital followed by a lowercase letter — the shape a name takes in a list
# built from lowercase word categories. See the module docstring for why this exists.
CAPITALISED = re.compile(r"^[A-ZÀ-Þ][a-zß-ÿ]")

# Words that are always capitalised and are not names, so the rule above must not eat
# them and the excluded `upper` category must not take them away. One entry, and it is
# the one word in English this applies to.
ALWAYS_CAPITALISED = ("I",)

DEFAULT_LEVEL = 60


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--scowl",
        required=True,
        type=Path,
        help="Path to an unpacked SCOWL distribution (the folder holding final/).",
    )
    parser.add_argument(
        "--out",
        type=Path,
        default=Path("resources/wordlist-en.txt"),
        help="Where to write the list. Defaults to resources/wordlist-en.txt.",
    )
    parser.add_argument(
        "--level",
        type=int,
        default=DEFAULT_LEVEL,
        help=(
            "Highest SCOWL size level to include, 10 to 95. Every level at or below it "
            f"is taken. Defaults to {DEFAULT_LEVEL}, SCOWL's own 'large' size: small "
            "enough that the obscure entries the higher levels add are not offered as "
            "corrections, large enough that ordinary words are in the list and so are "
            "left alone."
        ),
    )
    return parser.parse_args()


def find_final_dir(scowl: Path) -> Path:
    """The folder holding the per-category word files."""
    for candidate in (scowl / "final", scowl):
        if candidate.is_dir() and any(FILENAME.match(f.name) for f in candidate.iterdir()):
            return candidate
    sys.exit(
        f"No SCOWL word files found under {scowl}. Expected a final/ folder holding "
        "files named like english-words.60."
    )


def read_scowl_version(scowl: Path) -> str:
    """SCOWL's release, read from its own files rather than supplied on the command line."""
    for name in ("VERSION", "version"):
        path = scowl / name
        if path.is_file():
            return path.read_text(encoding="utf-8", errors="replace").strip().splitlines()[0]
    readme = scowl / "README"
    if readme.is_file():
        for line in readme.read_text(encoding="utf-8", errors="replace").splitlines():
            match = re.search(r"SCOWL\s+\(?(?:version\s+)?([0-9][0-9._-]+)", line)
            if match:
                return match.group(1)
    return "unknown"


def read_words(path: Path) -> tuple[list[str], str]:
    """One SCOWL file's words, with the encoding it turned out to be in.

    SCOWL ships its lists in ISO-8859-1 in some releases and UTF-8 in others, and the
    file itself does not say which. UTF-8 is tried first because a file that decodes
    as UTF-8 almost never is anything else.
    """
    raw = path.read_bytes()
    for encoding in ("utf-8", "iso-8859-1"):
        try:
            text = raw.decode(encoding)
        except UnicodeDecodeError:
            continue
        return [line.strip() for line in text.splitlines() if line.strip()], encoding
    sys.exit(f"Could not decode {path} as UTF-8 or ISO-8859-1.")


def collect(final_dir: Path, level: int) -> tuple[dict[str, int], list[str], set[str], list[str]]:
    """Every word at or below `level`, with the lowest level it appears at.

    The lowest level is the one kept because that is the commonness ranking: a word
    present at level 10 is far commoner than one that first appears at 60.
    """
    words: dict[str, int] = {}
    used: list[str] = []
    encodings: set[str] = set()
    dropped: list[str] = []
    for path in sorted(final_dir.iterdir()):
        match = FILENAME.match(path.name)
        if not match:
            continue
        if match.group("spelling") not in SPELLINGS:
            continue
        if match.group("category") not in CATEGORIES:
            continue
        file_level = int(match.group("level"))
        if file_level > level:
            continue
        entries, encoding = read_words(path)
        encodings.add(encoding)
        used.append(path.name)
        for word in entries:
            if CAPITALISED.match(word) and word not in ALWAYS_CAPITALISED:
                dropped.append(word)
                continue
            if word not in words or file_level < words[word]:
                words[word] = file_level
    for word in ALWAYS_CAPITALISED:
        words.setdefault(word, 10)
    return words, used, encodings, sorted(set(dropped))


def main() -> None:
    args = parse_args()
    final_dir = find_final_dir(args.scowl)
    version = read_scowl_version(args.scowl)
    words, used, encodings, dropped = collect(final_dir, args.level)
    if not words:
        sys.exit(f"No words collected from {final_dir} at level {args.level} or below.")

    today = datetime.date.today().isoformat()
    header = [
        "# Hexboard's English word list, for word-boundary correction.",
        "#",
        f"# Generated {today} by scripts/generate-word-list.py from SCOWL {version},",
        f"# size level {args.level} and below.",
        "#",
        f"# Categories included: {', '.join(CATEGORIES)}.",
        "# Categories EXCLUDED: upper, proper-names — SPEC's rule that the shipped",
        "# dictionary carries no proper nouns, satisfied by leaving SCOWL's own name",
        "# categories out rather than by filtering.",
        "#",
        "# Two backstops behind those categories, because the separation leaks both ways.",
        f"# Dropped from the ordinary-words files as names SCOWL filed outside its own name",
        f"# categories ({len(dropped)}): {', '.join(dropped) if dropped else 'none'}.",
        f"# Added back as always capitalised and not a name: {', '.join(ALWAYS_CAPITALISED)}.",
        f"# Spellings included: {', '.join(SPELLINGS)}.",
        f"# Source encoding: {', '.join(sorted(encodings))}. This file is UTF-8.",
        "#",
        "# One entry per line: the word, a tab, and the lowest SCOWL size level it appears",
        "# at. That level is a coarse commonness ranking and is what breaks ties between",
        "# two equally close corrections. SCOWL ships no frequency data.",
        "#",
        f"# Files read: {len(used)}. Words: {len(words)}.",
        "#",
        "# SCOWL is Copyright 2000-2018 Kevin Atkinson. Its licence travels with this file;",
        "# see the Notices section of README.md.",
        "",
    ]

    lines = [f"{word}\t{level}" for word, level in sorted(words.items())]
    args.out.parent.mkdir(parents=True, exist_ok=True)
    args.out.write_text("\n".join(header + lines) + "\n", encoding="utf-8", newline="\n")
    print(f"Wrote {args.out} — {len(words)} words from {len(used)} SCOWL files.")


if __name__ == "__main__":
    main()
