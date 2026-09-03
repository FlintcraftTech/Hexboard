# 37384e4 — A physical-keyboard test filed after the standard turned out to be Android's and already inherited

The user asked whether interactivity with wireless or wired keyboards needs testing at all, and what the standard even is — guessing it might be about the keyboard going away when typing starts elsewhere.

Looked up rather than answered from memory. `InputMethodService` decides whether to draw its input view in `onEvaluateInputViewShown()`, and its default answer is to show the keyboard unless a hard keyboard is available. `HexboardImeService` extends `InputMethodService` and does not override that method — checked — so Hexboard already behaves as every other keyboard does, with nobody having written a line for it. The trigger is the keyboard connecting, not typing starting somewhere else: the platform re-evaluates on the configuration change a pairing produces.

So the question narrowed, and what is left is not the hiding but the surviving. Hexboard's input view is Compose, hosted with lifecycle, view-model and saved-state owners installed by hand — the arrangement [first-installable-build] had to design around because it is the known crash trap. The platform destroys and recreates that view when a keyboard connects and disconnects, and nothing has ever exercised that path. It is untested rather than suspected.

The item was worth filing only because the user confirmed they have a keyboard to pair; without one it would have been a test nobody could run, and Claude said so before asking.

One feature is deliberately not in it: keyboards like Gboard offer a setting to keep the on-screen keyboard visible while a physical one is attached, by overriding the same method. That is a feature rather than a standard and nobody has asked for it.

**Queue changes:** filed into Unprocessed as `[user]` work and moved into Processed below the readiness line with `Blocked by: [install-and-enable-on-pixel]`, since there is nothing to hide until Hexboard is switched on. Placed among the `[user]` items batched at the end of the held region. SPEC's layout details gained the hide-and-return behaviour.

**Work processed:** kept — [physical-keyboard-handover].
