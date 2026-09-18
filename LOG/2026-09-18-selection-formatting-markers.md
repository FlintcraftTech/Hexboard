# [HASH] — [selection-formatting-markers] split out and then designed the same session, once "can the keyboard detect which app it is in?" turned out to have a verified yes and a firm no

Session of 2026-09-18, 15:42.

Split out of [selection-formatting-spans] as the half for apps that strip styled text and read markers instead — `*bold*` and its relatives. It was filed as a capture and processed at the next checkpoint, which is where it stopped being undesignable.

**Two findings changed its shape before any design was proposed, and both are facts rather than judgments.** The markers contradict each other: a single asterisk means bold in WhatsApp, Telegram and Slack, and italic in Discord, which follows ordinary Markdown and needs two for bold. So no single convention can be adopted and applied everywhere — the same tap would embolden a message in one app and italicise it in another. And underline is mostly absent: three of the four carry markers for bold, italic, strikethrough and monospace and none for underline, so the marker route cannot offer the same three controls the styling route does.

**A point in the item's favour that its first framing missed**, and it is why the entry survived rather than being dropped as a maintenance burden: the apps where styling fails are largely rich web editors, and those carry their own formatting toolbars, so the keyboard is not the only route there. The apps where markers work are the messaging ones, which offer nothing at all. The half reaches a real gap.

**The user's question was whether the keyboard could detect the convention and adapt, and the answer split cleanly.** It can identify the *app*: an input method is handed an `EditorInfo` carrying the package name, and from API 23 the system verifies that name against the application's real UID and refuses an input connection where the two disagree, so it cannot be spoofed — and this project's minimum is API 26. It cannot observe the *convention*, because it never sees text rendered; a sent message is drawn in a view no input method can read. Inferring it would need an accessibility service reading other apps' screens, which is the exact shape of the malicious keyboard this project designs against. So "detect and adapt" resolves to "identify the app and look it up", and the table does not disappear — it stops being something the user picks.

**The user then added the requirement that made the design better.** A new app needs an easy way in. Rather than a list of installed applications — which needs a broad package-query permission and would be indefensible on a keyboard — holding any of the three controls opens a chooser for the app currently attached, so the affordance sits in the app where the problem was noticed. That also unified the two halves: the controls are always present, and the table decides only what a tap does, so there is no state where they vanish with nothing to explain it, and an unconfigured app gets the harmless behaviour rather than a guess that leaves a stray asterisk in a sent message.

The table lives in a config file rather than in source, which is SPEC's existing rule for the key inventory applied to the same shape of thing: adding an app becomes a data edit.

**The privacy risk, and how the flag cleared.** The list of configured apps is a record of apps the user uses, held on the phone, where none existed before. Claude raised it plainly; the user accepted it in the same exchange. Four constraints came with that consent and are written into the item: the list grows only by a deliberate configuration, nothing is recorded about an unconfigured app, it never leaves the device, and it is readable and deletable on the settings screen. That is deliberately the same footing [predictive-saved-words] was put on, so the project carries one pattern rather than two. The flag is therefore cleared **by informed consent**, not by design-out.

Placed straight after [selection-formatting-spans] rather than held against it: the two share the same controls and files, so one run builds them in order, and a blocking line would only hide this entry from the run that should build it next.

**Queue changes:** [selection-formatting-markers] filed as a capture and rewritten whole into Processed, cleared, placed after [selection-formatting-spans]; that item's own text corrected where it had recorded this half as a refused option; SPEC's styling sentence widened to cover the marker case and the configured-apps list.

**Work processed:** kept — [selection-formatting-markers].
