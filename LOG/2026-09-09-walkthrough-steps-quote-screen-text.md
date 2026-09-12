# f83f5d9 — [walkthrough-steps-quote-screen-text] a project rule that a step quotes what the screen says, read from the source rather than recalled

This session ran across 2026-09-05 and 2026-09-09.

Filed by /rescan on 2026-09-04 from a step that misfired the same day. A walkthrough step asked the user to report what "the availability line" said. That phrase came from the queue item's own wording and named nothing on his screen, so he asked what an availability line was. Reading `MainActivity.kt` showed the screen carries one line reading either "On-device recognition: available" or "On-device recognition: not available on this phone". Quoting those two strings would have cost nothing and the step would have worked.

**The source-reading half matters as much as the quoting half**, and that is why the rule names both. A quoted string composed from memory reads exactly like one read from the code, and is wrong in a way the reader cannot detect — the person following the step is the one person who cannot check the translation, because they cannot see the source. That failure was cheap because he asked; a user who assumed the wording was approximate and reported the wrong line would have produced a wrong result nobody would have questioned.

A rule rather than a correction, because `CLAUDE.md` already carries a sentence about naming something visible to click, added 2026-09-02, and this is the same instinct failing at a third site: not where to go, but what the screen will say once you are there. The new paragraph sits beside that one rather than absorbing it — folding them would make one long rule that is harder to apply than two short ones.

**It was not held against its sibling**, and the reasoning is worth keeping: [settings-steps-name-a-search] narrows the same paragraph of the same file, so building them separately means editing `CLAUDE.md` twice for one instinct. But that item is held on an unverified fact, behind a chain of two other items. This rule rests on nothing unverified — quoting text read from the source is correct by construction — and holding a sound rule behind an unsound one to save a second small edit is the wrong trade.

**The rule was then used and broke the same day it shipped.** Driving the walkthrough on 2026-09-09, a step told the user to right-click `app/src/test/java/tech/flintcraft/hexboard` in Android Studio's project tree — a filesystem path, which the Android view does not show. The correct target, read from his own screenshot, is `tech.flintcraft.hexboard (test)` under `app → kotlin+java`. Naming a path that exists on disk but not on the reader's screen is precisely what this rule bars, one paragraph after writing it.

**Files touched:** `CLAUDE.md`.

**Routed to Captures:** none.
