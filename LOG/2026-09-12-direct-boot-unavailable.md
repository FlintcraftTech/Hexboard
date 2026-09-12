# [HASH] — [direct-boot-unavailable] documented in SPEC and left as a capture, with the accessibility argument recorded against the do-nothing case

Session of 2026-09-12, 12:54.

Android warns, when the keyboard is switched on, that after a reboot the app cannot start until the phone is unlocked — so the unlock itself is typed on whatever keyboard the system falls back to. It was filed on 2026-09-09 as a permanent property no document mentioned, rather than as something known to need fixing.

**Half of it closed here: SPEC now states the behaviour and why**, the keyboard's state living in storage readable only after the first unlock, and the clipboard deliberately so.

**The design half is not designable yet.** The question is what Hexboard must read before unlock, and two of its three answers belong to features nobody has built: the saved-word list would have to move to device-protected storage, and the clipboard history must not, being encrypted at rest precisely so it does not survive into a locked phone's keyboard.

**One argument was added against the do-nothing case**, because the entry's own weighing — one unfamiliar keyboard per reboot against a storage split — looks stronger than it is. It is a fair trade for most people and not for the person this keyboard is for: someone who chose Hexboard because larger targets make them accurate is denied that accommodation at exactly the moment a mistyped unlock costs most, and SPEC treats accessibility as a requirement rather than a nicety. So it is a reason to revisit rather than to close.

The trigger for taking it up again — [persistent-clipboard] and [predictive-saved-words] being built, since between them they settle what the keyboard reads at startup — is named in prose rather than as a blocker, both already being in Processed where the field would hold nothing back.

**Queue changes:** SPEC gained the direct-boot sentence; [direct-boot-unavailable] kept in Unprocessed with the design question and its trigger written in.

**Work processed:** kept in Unprocessed — [direct-boot-unavailable].
