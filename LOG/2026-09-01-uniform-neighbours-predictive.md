# be3516e — Predictive text gains saved words and loses proper nouns, moving the red flag from designed-out to informed consent, and its word-list question answered

Two decisions of the user's changed this item, one of them reversing something already in SPEC.

**No proper nouns in the shipped dictionary**, their reason being that a word corrected into a name is a particularly irritating failure. This is a property of the word list rather than the engine, so it became a criterion the dictionary research had to satisfy.

**Saved words, which reverses "stores nothing".** SPEC said the engine learns nothing and stores nothing — a fixed dictionary chosen on 2026-08-20 precisely so the engine never holds a record of the user's writing, which is what had cleared this item's red flag. The reversal was surfaced before anything was written, with the prior decision cited and the exposure stated plainly: saving words means a file of words they typed sitting on the device. They chose the saved-word store, the reason being the obvious one — a keyboard that cannot learn a surname or a street name is worse to use.

So the flag stays cleared but on a different footing, and the item says so: cleared by designing the risk out has become cleared by informed consent to a smaller risk. Three constraints came with that consent and bind the build — the list grows only on a deliberate save, it never leaves the device, and the user can read and delete it. The item warns a later session not to read the flag as designed-out and quietly drop them.

The word-list lookup this item had left for a later session was done instead, filed as `workshop/resources/research/word-list-licence-and-frequency.md`. SCOWL answers both halves at once: its licence is permissive enough to ship in a public repository under PolyForm Noncommercial, and it separates proper names into categories that can simply be left out at build time, so the no-names rule needs no filter written. What SCOWL lacks is frequency data; its size levels are a coarse commonness ranking that may serve, and if a real table is wanted, Leipzig (CC BY) raises no ShareAlike question where wordfreq (CC BY-SA) does. One licence trap is named so nobody hits it twice: a well-known repository presents as MIT, but that covers the generator code while the data carries the source corpus's own terms.

The item was held in Unprocessed against [install-and-enable-on-pixel] — there is nothing to correct into until there is something to type on.

**Queue changes:** [uniform-neighbours-predictive] gained both decisions, the consent trail and a research citation, plus a `Blocked by:` line naming [install-and-enable-on-pixel]. SPEC's predictive text principle rewritten.

**Work processed:** kept in Unprocessed, held — [uniform-neighbours-predictive]. Red flag re-examined and left cleared, on the informed-consent route.
