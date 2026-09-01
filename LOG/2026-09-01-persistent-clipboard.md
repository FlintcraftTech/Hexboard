# [HASH] — A persistent clipboard designed with a threat model, and no clear-all button on purpose

The user raised this against Gboard, whose clipboard clears and loses something copied twenty minutes ago. Their retention rule, restated as a build can implement it: a clip is kept if it is younger than an hour, or if it is among the twenty most recent, and dropped only when both fail.

The privacy risk was raised at the moment the idea landed rather than after designing it. A clipboard history is a file holding the last hour of everything copied, and what passes through a clipboard includes passwords, one-time codes and card numbers.

Android's marker for that turned out weaker than it sounds, and was read rather than assumed. `ClipDescription.EXTRA_IS_SENSITIVE` exists from API 33, with a string constant usable below it — which matters, since this project's minimum is API 26. But the documentation is explicit that it adds no security: it is a rendering hint the *source application* chooses to set, so it protects only where that app bothered.

Two design decisions are the user's and both are recorded with their reasons. **No clear-all button**, because a cleared clipboard is itself suspicious — someone looking over your shoulder wonders why it is empty — and per-item deletion removes what you want gone while the rest still looks ordinary. That is a threat model rather than a simplification, and it is written down because a clear-all is the obvious thing to add and would otherwise read as an omission. **Deletion by holding an item and dragging it to a bin** that appears during the drag, on a clipboard screen in the manner of Gboard's.

One alternative was proposed by Claude and beaten by the user. Claude suggested refusing to store anything copied from a password field. The user replaced it with a five-minute expiry, and the reason it lost is that exclusion breaks the case people actually hit — you deliberately copy a password to paste it, and it is not there.

Two limits are stated in the item rather than solved: a clip copied while the keyboard is not attached to the field cannot be classified and gets the ordinary rule, and a persistent clipboard remains a larger target than one that clears whatever is done to it.

**Queue changes:** [persistent-clipboard] filed in Unprocessed, held against [first-installable-build], carrying a cleared red flag. SPEC gained the clipboard principle.

**Work processed:** kept in Unprocessed, held — [persistent-clipboard]. Red flag cleared by design plus informed consent.
