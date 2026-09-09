# [HASH] — [symbols-panel-empty-slots] the last ten free slots in the English layout filled, and the original complaint turned out to have a second cause

This session ran across 2026-09-05 and 2026-09-09.

The user raised this on 2026-09-03 from the first swipe between panels on the real keyboard: the next panel does not run continuously from the last one. Designing it out found the symbols panel carrying thirty keys in forty positions, with all ten gaps on the left — which is why swiping into it showed blank board where keys were expected.

**These ten were the only free slots in the English layout.** RARE is full at thirty of thirty and QWERTY has no spare position either, checked one by one. So whatever went here is what Hexboard has room for, and the next character after these evicts something — which is what made the choice worth agreeing rather than filling.

The ten, agreed with the user on 2026-09-05: **“ ” ‘ ’ • ← → ½ ¢ ≈**. The four curly quotes because a keyboard producing only straight quotes is one people work around every day, and they are the largest single gap; the bullet because there was no way to start a list; two arrows because the layout had none anywhere; and ½, ¢ and ≈ as the remaining most-wanted singles. Every printable ASCII character already had a home, checked across the three panels, so the question was only what was missing beyond ASCII.

No two empty slots are adjacent on any row, so the four quotes cannot sit side by side and "grouped" could only mean the same region: they take (0,1) (0,3) (1,2) (1,4), in the top-left near `[` and `@` where the other typographic marks are. **The item's own placement text was internally inconsistent** — it called those "the four earliest empty positions", which skips (0,5), the third earliest, and it placed only nine of the ten characters. The explicit list was taken over the "earliest" gloss, leaving ½ for (0,5); that is the only assignment putting all ten characters in all ten slots.

Two exclusions kept from the item. ¿ and ¡ are the obvious missing punctuation and belong on the Spanish layout's own config, since the manifest rules bind each layout individually. And the Russian panel has the identical ten gaps but uses « » as its primary quotation marks, so four English curly quotes are probably the wrong fill for it — that is [russian-panel-gaps]. The five Latin layouts built in the same run inherit the same gaps for the same reason, filed as [latin-panel-gaps].

**Proved on the phone on 2026-09-09**: forty keys, no gaps, the characters where described.

**And the original complaint survives, with a different cause.** This item had checked the pager, found it sets no `pageSpacing`, and concluded the pager was not the cause. The empty slots were real and are now filled — and the seam is still visible, because across a page boundary two column centres sit `2 * radius + 2 * EDGE` apart, about 2.55 radii, against `horizontalStep` of about 1.81 radii within a panel. Roughly 40% wider. Filed as [panel-seam-gap], with the arithmetic.

**Worth knowing about what shipped:** the four curly quotes are now keys on SYMBOLS *and* remain long-press accents on QWERTY's `'` and `"`. That is two routes to the same character rather than a duplicate under the manifest rules, which speak of keys on panels, and no test objects — but it is true of the board that was agreed and nobody has decided whether the long-press copies should now go.

**Files touched:** `resources/key-layout.json`, `resources/key-manifest.md`.

**Routed to Captures:** [panel-seam-gap], [latin-panel-gaps], [curly-quote-double-route].
