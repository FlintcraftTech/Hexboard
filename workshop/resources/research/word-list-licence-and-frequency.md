# A word list for predictive text: licence, frequencies, and keeping proper nouns out

Researched 2026-09-01 for `[uniform-neighbours-predictive]`. Three things had to
hold at once: a licence that can ship inside a public repository under PolyForm
Noncommercial, frequency information to break ties between equally close
candidates, and no proper nouns, which the project's owner ruled out on
2026-09-01 because a word corrected into a name is the most irritating failure
autocorrect has.

## The finding

**SCOWL — the Spell Checking Oriented Word Lists — satisfies the licence and the
proper-noun requirement outright.** Its licence grants permission to use, copy,
modify, distribute and sell the word lists for any purpose without fee, the only
condition being that the copyright notice and licence text travel with derived
works. That is permissive in the sense this project needs: it imposes nothing on
Hexboard's own licence and does not care that PolyForm Noncommercial restricts
commercial use.

**SCOWL separates proper nouns into their own categories, which is the part no
other candidate offered.** It divides its material into `words` (ordinary English
words), `upper` (uppercase words that appear in dictionaries) and `proper-names`
(further uppercase words), and its `mk-list` tool builds a custom list from
chosen categories. So excluding names is a build-time selection rather than a
filtering pass someone has to write and maintain.

**SCOWL carries no frequency data, and that is its one gap.** What it has instead
is size levels, numbered 10 through 95, and those levels were themselves assigned
using frequency classifications from other corpora — so the level a word sits at
is a coarse commonness ranking already. Whether that is enough to break ties
between two equally-close candidates is a design question for the build, but it
means a first version may need no second source at all.

**If a real frequency table is wanted, two candidates, and they differ in a way
that matters here.** The `wordfreq` export of 25,000 English words with
frequencies is CC BY-SA 4.0; the Leipzig Corpora Collection lists are reported as
CC BY 4.0. ShareAlike is the difference. CC BY-SA obliges adaptations of the data
to be shared under the same terms, which raises a question about a derived,
filtered, repackaged table shipped inside an app under a different licence —
answerable, but it is a question. CC BY 4.0 asks only for attribution and raises
none. **On licence grounds alone, Leipzig is the cleaner pick and wordfreq is the
one that needs a decision first.**

**One trap worth naming.** `hermitdave/FrequencyWords` presents as MIT, and it is
a common first hit. The MIT licence there covers the generator code; the data is
derived from OpenSubtitles, which carries its own terms. A permissive licence on
a repository that ships derived data is not the same as a permissive licence on
the data, and this is the shape that catches people.

**The Leipzig licence was read at source on 2026-09-17, and the paragraph below is
superseded on that one point.** The Anubis proof-of-work challenge is still in
front of the site — a plain fetch of `wortschatz.uni-leipzig.de/en/download`
returned the challenge page and nothing else on 2026-09-17 — but a real browser
solves it and reaches the content, which is how this was read. The project's Terms
of Usage page at `wortschatz-leipzig.de/en/usage` says, in full: *"The data and
applications provided by the project are protected by copyright. They are made
available free of charge for private and scientific use under the Creative
Commons licence CC BY-NC. Any use beyond the query options provided on the WWW,
automated queries (except via our web services) and commercial use of the data are
prohibited without the written consent of the project management. The text corpora
offered for download are made available under the Creative Commons licence CC BY."*

Three things follow, and the second is the one to carry.

- **The downloadable corpora are CC BY, confirming the secondary sources.** What
  those sources added and the page does not is a version number: the page says
  "CC BY" with no version, where they said CC BY 4.0. The version is therefore
  still second-hand.
- **The project's other data is CC BY-NC, which is a distinction the secondary
  sources did not carry.** So the licence depends on which artifact is taken, and
  a frequency list is only CC BY if it comes inside a corpus download. The
  Downloads page states that co-occurrence information is precomputed and included
  in the corpus packages, so a frequency list taken from a package is covered —
  that is a reading of the two pages together rather than a sentence either one
  states, and it is the thing to confirm before shipping anything.
- **Automated queries are prohibited except through the project's web services.**
  So a script that scrapes the site is out; the download packages are the route.

The paragraph below stands as the record of the 2026-09-04 attempt.

**The Leipzig check was attempted on 2026-09-04 and could not be completed, which
is a result worth recording rather than a gap.** Leipzig's own download and
frequency-dictionary pages both sit behind an Anubis proof-of-work bot challenge,
so neither could be read from here. Secondary sources agree on CC BY 4.0 for the
downloadable word lists, with CC BY 3.0 named for the printed Frequency
Dictionaries series — consistent with what this file already said, and still
second-hand. So the licence is better attested than it was and is **not** read off
Leipzig's own terms, and an item that bundles Leipzig data still carries an
unverified rest.

**What follows for the engine, decided on 2026-09-04.** The first version uses
SCOWL's own size levels as the tie-break and bundles no second source, so nothing
depends on the unread licence. A real frequency table becomes a later question
with its own item, and whoever takes it up needs a route past the bot challenge —
a browser, or the corpora download form — rather than a plain fetch.

**What the size levels actually mean, read from SCOWL's own README on 2026-09-17
and not known here before.** The project had chosen level 60 by reasoning about a
trade rather than from any statement of what the levels contain. SCOWL's author
names them directly: 35 is small, 50 medium, **60 "the size used for default
spell checking dictionary"**, 70 "the size used for large spell checking
dictionary". Sizes 70 and below hold words found in most dictionaries; **80 holds
"all the strange and unusual words people like to use in word games such as
Scrabble"**, and words at 85 "may no longer be used in modern English". The
recommendation is explicit: *"For spell checking I recommend using size 60. This
size is the largest size that I am fairly confident does not contain any
misspellings or invalid words."* Sizes 80 and above are named as not reasonable
for spell checking at all.

**What follows for this project.** The real choice is 60 or 70 and nothing above
is defensible, so the range is far narrower than the entry that asked the question
assumed. The level already shipped is the one SCOWL recommends, and the reason it
recommends it — the largest size confidently free of misspellings and invalid
words — is a stronger reason than the one this project reached on its own. Level
70 remains available where a specific everyday word turns out to be missing, but
the standing answer to that is adding the word by name, which the generator
already does for the pronoun `I`.

Read from `https://raw.githubusercontent.com/en-wl/wordlist/v2/README.md`.

## What this does not settle

Whether SCOWL's size levels are a good enough frequency proxy for tie-breaking —
that needs the engine to exist and be tried against real typing. The level
question above is settled; this one is not, and they are different questions.
Whether the en_GB variant or the -ise/-ize split matters for this project. And the
exact Leipzig licence, still unread at source for the reason above.

## Frame assessment

- **TIME RANGE** — Not applicable: the question is which word list to ship, and
  the product addresses no particular period. What does have a range is the data
  itself — a frequency table built from subtitles or news reflects the language
  of that corpus and that decade, which will show up as unfamiliar slang ranking
  above ordinary words. Nobody has stated a target for this, and that is a gap in
  SPEC rather than in the research.
- **PEOPLE** — Applies to English typists, which is who the shipped default
  layout serves. It says nothing about the contributed variants SPEC anticipates:
  every other language needs this question asked again, and SCOWL is
  English-only.
- **FRESHNESS** — Licences change rarely and SCOWL's has been stable for years,
  so the licence half ages slowly. The available-corpora half ages faster.
- **RISK IF WRONG** — Being wrong about a licence is the expensive case, because
  the repository is public and a wrongly-licensed data file is a distribution
  problem rather than a bug. It does not warrant a red flag today, since nothing
  has been bundled — but it does warrant reading the actual licence file of
  whatever ships, at the moment it ships, rather than trusting this summary.
- **ALTERNATIVES** — Considered and set aside: `wordfreq` and Leipzig, both
  above; `hermitdave/FrequencyWords`, named as a trap. Never investigated: the
  word lists Android's own keyboards ship, which may be licensed in a way that
  rules them out entirely; and Wiktionary's frequency lists, which are
  CC BY-SA and would raise the same ShareAlike question as wordfreq.
