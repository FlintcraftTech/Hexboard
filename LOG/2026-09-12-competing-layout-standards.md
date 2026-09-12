# [HASH] — [competing-layout-standards] one layout per language, both where the scripts differ, more on request — written into SPEC and the item deleted

Session of 2026-09-12, 12:54.

Some languages have more than one keyboard in ordinary use: Russian has ЙЦУКЕН and a phonetic arrangement, Turkish ships as F and Q, Persian has three files, Bulgarian has BDS and phonetic, Serbian has Cyrillic and Latin. So it recurs every second or third language.

Nothing was needed mechanically — [layout-switching] already orders layouts within a language by a declared position, so two variants are representable. What was missing was the policy, and the policy is the existing one applied a level down: SPEC already adds languages as they are asked for, and [layout-error-report] built the route by which someone says a layout is wrong for them.

**The exception is principled rather than a matter of taste.** Where the variants are different scripts, both ship: a Serbian typist who writes in Latin cannot type at all on a Cyrillic board, while a Turkish typist given Q instead of F can type, slower and crosser, and can ask for the other.

Shipping every variant lost on the Persian case — three layouts in the picker asks a user to know which they want before they have typed anything, and each is another transcription with its own error risk and nobody checking it. Deciding per language as each is transcribed is the honest description of doing nothing.

The item was deleted rather than kept, because with the policy in SPEC nothing was left to build: Russian already ships the arrangement the rule names, Serbian is not shipped yet, and the rule governs the next transcription rather than any file today. The cases themselves were already in `workshop/resources/research/open-source-layout-sources.md` from the catalogue read of 2026-09-05.

**Queue changes:** SPEC's layouts principle gained the multiple-standards rule; [competing-layout-standards] deleted.

**Work processed:** deleted — [competing-layout-standards].
