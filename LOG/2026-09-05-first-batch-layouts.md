# 5f9d97e — [first-batch-layouts] created with the five the user chose: French, German, Spanish, Portuguese, Italian

Chosen by the user on 2026-09-05 from the plan settled the same day; [language-list-choice]'s record carries that reasoning. The alternative offered was picking by where Android users are rather than by what the config model supports, which would put Hindi near the top — a Devanagari job of its own rather than a transcription. He took the five.

The ordering is deliberate: French and German first because they are genuine key-order changes and therefore the ones that prove the route, then the three that are QWERTY with different accents. That distinction came out of FlorisBoard's own file listing — Portuguese and Italian have no layout file at all and differ only in their popup mappings, which is a second and cheaper level of cheapness nobody had noticed before.

Two things are left to reading rather than guessing at build time: which of `german.json` and `qwertz.json` matches what a German phone keyboard shows, recorded in the config's `about` field as [language-starter-layouts] did for Russian; and Spanish's eleven-wide middle row, which is the case SPEC already allows and Russian already shipped.
