# [HASH] — [predictive-saved-words] a word is saved by tapping the offer that appears when autocorrect is undone, which is the affordance the design never had

Session of 2026-09-12, 12:54.

The saved-word store had sat in Unprocessed since 2026-09-04 for one reason: nothing in the design said *how* a word gets saved. The row above the keys already has three claimants — suggestions, the microphone, the clipboard button — and the entry would not clear the buildability check without an answer.

**The answer was already in SPEC and nobody had looked at it that way.** SPEC says a correction is reverted by pressing backspace immediately after it lands. That press is the user telling the keyboard the word was right and it was wrong, which is exactly the information a save carries. So the offer to save rides that undo: the row above the keys is empty at that instant, and the affordance costs no key, no gesture and no space.

Three alternatives lost with their reasons. A long-press on a word in the text collides with Android's own text selection, and a plain tap is taken by [tap-word-alternatives]. A dedicated key evicts punctuation, which SPEC's manifest rules make a real loss. The settings screen alone means leaving the text field to save a word, which is the failure this project designs against — so it became the second route rather than the only one.

**What the list offers was the other open question: read, delete, add by hand, and a clear-all.** The clear-all needed its reasoning stated because [persistent-clipboard] deliberately refuses one and copying that across would have been wrong. The clipboard's reason is a threat model — an emptied clipboard is itself a disclosure to someone looking over your shoulder — and a saved-word list is not a record of what you did but a list you curated, whose empty state is what every install starts in. An export was refused as a route off the device, which the store's consent conditions rule out; a lock as security theatre, the keyboard having no notion of who is using it.

The red flag stays cleared on its original footing — the user's informed consent of 2026-09-01 to a smaller risk than silent learning, with the three constraints that came with it. Nothing in this session's design touches that, and the store still grows only by deliberate save, never leaves the device, and is the user's to read and delete.

Held below the line against [uniform-neighbours-predictive], because the undo the offer hangs off does not exist until the correction engine does.

**Queue changes:** [predictive-saved-words] designed out and moved into Processed, held; SPEC's predictive-text principle gained the sentence saying what a deliberate save is.

**Work processed:** kept — [predictive-saved-words].
