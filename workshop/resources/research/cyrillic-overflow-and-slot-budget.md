# Fitting a Cyrillic alphabet into Hexboard's slot budget

Researched 2026-09-02, for the question of whether Hexboard can carry layouts copied from other languages' standard keyboards, using Russian as the hard case.

## Hexboard's budget

Read from `resources/key-layout.json` on 2026-09-02. The QWERTY panel is four rows of ten slots, all forty occupied. Row 3 holds the two space bars and punctuation, so a language has **30 letter slots** and no spare.

## The standard Russian layout

ЙЦУКЕН is the standard Cyrillic layout for Russian on computers and typewriters, named for the first six letters of its top row. Its three letter rows are:

- Й Ц У К Е Н Г Ш Щ З Х Ъ — 12
- Ф Ы В А П Р О Л Д Ж Э — 11
- Я Ч С М И Т Ь Б Ю — 9

That is 32. The 33rd letter, Ё, is not in those rows: on the desktop layout it sits alone in the top-left corner, where the backtick key is on QWERTY, well away from Е.

## What mobile keyboards already do about it

**On a phone, Ё is not given a key at all — it is reached by holding Е.** This is the finding that matters, and it is not a workaround anyone invented for Hexboard: it is what Russian keyboards on Android already do, because a phone row cannot carry the desktop layout's spare key either.

So the overflow problem is not Hexboard's alone, and the mechanism for solving it is the one Hexboard already has. Long-press accents are in the config format and in SPEC.

## The size of the actual shortfall

32 letters wanted, 30 slots available. **Two letters must move behind a long-press**, on top of Ё which has already moved there on every mobile keyboard.

The proposed rule, which generalises past Russian: an overflow letter goes behind a long-press on the letter it is already paired with in the writing system — Ъ under Ь, the hard sign under the soft sign; Щ under Ш, which differ by a tail. Pairs that already exist to a reader of the language are the ones a user will guess without being taught, and they are exactly what Hexboard's long-press accents already model for Latin diacritics.

**Which specific pairs each language uses is not settled here and must not be guessed from this file.** The rule is what this establishes; the pairing is a judgment for someone who reads the language, checked against a real keyboard for that language.

## Alternatives considered

**Widening the rows to eleven or twelve.** Rejected. Row width is not the perceptual wedge and could be changed, but more keys per row means smaller keys, and larger keys are the whole point of the project. A layout that shrinks the keys to fit an alphabet has given away the thing being varied around.

**Spilling overflow letters onto the RARE panel.** Rejected. The panel is one horizontal swipe away and is named for what it holds; a letter of the alphabet is not rare in its own language, and putting one there makes the layout worse for every word that uses it.

**The phonetic Russian layout (ЯВЕРТЫ and its relatives)**, which maps Cyrillic onto QWERTY positions by sound. Not researched — named here so nobody records it as ruled out. It is a genuine second option for Russian and would raise a different question, namely which of two standards a variant should copy.

## Frame assessment

- **TIME RANGE** — not applicable in the usual sense, but the gap is in SPEC rather than here: SPEC states no time range for how long a shipped layout is expected to stand. ЙЦУКЕН has been the standard since Soviet-era typewriters, so the answer is stable on any horizon this project has.
- **PEOPLE** — this is about Russian typists, and the sources are English-language explainers of the Russian layout rather than Russian typists' own accounts. The row contents are consistent across sources and are a matter of record; the *mobile* convention for Ё is reported rather than observed on a device. That limit is real and is why the pairing decision is reserved for someone who reads the language.
- **FRESHNESS** — high. The layout is decades stable and the mobile long-press convention is long established.
- **RISK IF WRONG** — a wrongly placed letter ships a layout Russian users find broken, and a shipped layout gets copied rather than read. It warrants a native-speaker check before any Cyrillic layout ships, not a red flag: nothing here exposes anyone's data.
- **ALTERNATIVES** — three were weighed and are recorded above with why each lost or was left open. The phonetic layout is the one that was never investigated.

## Sources

- [JCUKEN — Wikipedia](https://en.wikipedia.org/wiki/JCUKEN)
- [Russian ЙЦУКЕН Layout — Visual Guide](https://anykeyboard.io/blog/russian-keyboard-layout-explained)
- [How to Add Russian Language Keyboard on Android](https://electronics.alibaba.com/question/russian-keyboard-on-android-setup,-layouts-troubleshooting)
- [How do you type ё on the standard Russian keyboard? — italki](https://www.italki.com/en/post/question-491146)
