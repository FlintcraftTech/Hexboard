# [HASH] — [picker-language-order] the layout picker sorts languages by their own names rather than by a code the reader never sees

Session of 2026-09-12, 12:54.

`LayoutCatalogue.group` ended in a sorted map over the BCP 47 tag, so the seven shipped layouts read German, English, Spanish, French, Italian, Portuguese, Russian — alphabetical by `de, en, es, fr, it, pt, ru`. SPEC settled the order *within* a language and said nothing about the order between them, so this was a gap rather than a departure.

Sorting by each language's display name in the device's own locale wins on maintenance rather than on taste: the order comes from data Android already holds, so nothing has to be set correctly on every future config. The entry listed the order changing with the device language as a drawback; it is the behaviour working — a reader whose phone is in Russian sees the languages named in Russian and sorted as Russian sorts them.

Two alternatives lost. Putting the current layout's language first pins the entry the reader is least likely to want, the picker being opened in order to change layout, and rearranges the list between visits. A declared position per language is stable and correct and is one more field to get right on every layout added forever, to solve what the platform solves for free.

**Queue changes:** [picker-language-order] designed out and cleared to run; SPEC's layouts principle gained the between-languages ordering.

**Work processed:** kept — [picker-language-order].
