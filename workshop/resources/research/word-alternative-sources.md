# Where alternatives for an already-typed word could come from

Read on 2026-09-04, for [tap-word-alternatives] — tapping a finished word and being
offered replacements. The question the item could not answer at a desk: what
supplies the alternatives, given that key geometry cannot.

## Why geometry is out, established before the search

Autocorrect fires on the space bar, so by the time a word sits finished in the
text it was either corrected or is already a dictionary word. What survives is
precisely what the neighbour-weighted distance cannot reach. The motivating case
— "rose" where "rows" was meant — is two edits apart and both are real words.

## An on-device language model is out, and the reason is concrete

Android's route to a bundled small model is Gemini Nano, reached through AICore
and the ML Kit GenAI APIs. Google's own supported-device list for those APIs
starts at the **Pixel 9**; the Pixel 6 is not on it, and neither is anything
older. This project's test handset is a Pixel 6 and SPEC's minimum is API 26, so
the route is unavailable to the developer and to most of the audience at once.
Read from Google's ML Kit GenAI overview on 2026-09-04.

Shipping a model of one's own is the other form of this and is not seriously
available: the smallest model Google names for latency-sensitive keyboard work is
Gemma E2B at roughly two billion parameters, which is orders of magnitude beyond
what a keyboard APK can carry.

## A curated homophone list is out on licence

`pimentel/homophones` is the obvious first hit and carries a usable CSV — one
line per homophone group, comma-separated, ASCII only. Its repository states no
licence at all, which means the default of all rights reserved rather than
anything permissive. It cannot be redistributed inside this repository as things
stand. Read on 2026-09-04.

## What works: derive the homophones from CMUdict

The CMU Pronouncing Dictionary maps English words to phoneme sequences. Words
whose phoneme sequences are identical are homophones by construction, so the
table is a grouping rather than a curation — no list to trust and no list to
license. Checked on the motivating case: "rose" and "rows" are both `R OW1 Z`.

Its licence is the permissive one this needs: **"Use of this dictionary for any
research or commercial purpose is completely unrestricted"**, with acknowledgment
of origin requested where the material is redistributed. That is the same shape
as the Unicode and FlorisBoard notices README already carries. Read from the
cmusphinx/cmudict repository on 2026-09-04.

The practical form: a generation script groups CMUdict by phoneme sequence, keeps
only groups whose members all appear in the shipped word list, and writes a small
table. Restricting to shipped words is what keeps it small and stops the feature
offering words the keyboard does not otherwise know.

## What this does not settle

Whether homophones alone are enough. The motivating case is a homophone, but a
user tapping a word may equally have meant something merely similar, and nothing
here says how often that happens. It also says nothing about the affordance —
how a word is selected in the text, and what happens when a tapped word has no
alternatives to offer.

## Frame assessment

- **TIME RANGE** — Not applicable in the usual sense: CMUdict's licence and
  content are stable and the dictionary is decades old. The Gemini Nano device
  list is the opposite and is dated below under freshness.
- **PEOPLE** — Applies to English typists, which is who [tap-word-alternatives]
  is for today. A homophone table derived from CMUdict is English-only, so this
  finding says nothing about the Russian layout or any language added later —
  each would need its own pronunciation source.
- **FRESHNESS** — The device-support half is amended on a cycle: Google's list
  named the Pixel 9 as its oldest Google device on 2026-09-04 and expands over
  time, so the LM route may open for newer handsets later. It does not reopen for
  the Pixel 6 or for API 26. The CMUdict half is not on a cycle.
- **RISK IF WRONG** — Being wrong about the CMUdict licence would mean shipping
  data this repository has no right to redistribute, in a repository that is
  public. That is the one claim here worth re-reading at build time rather than
  trusting this file. Being wrong about the device list costs nothing: it would
  only mean a route was ruled out that was in fact available.
- **ALTERNATIVES** — Ruled out with reasons: neighbour-weighted distance,
  on-device LM, `pimentel/homophones`. Never considered: a confusion set built
  from real typing data, which nobody here has and which would mean collecting
  what users type; and Wiktionary's pronunciation data, which is CC BY-SA and
  would raise the same ShareAlike question `wordfreq` raised for frequencies.
