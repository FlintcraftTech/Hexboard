# [HASH] — [install-walkthrough-settings-path] fixed where it was live and split where it was not: two walkthroughs rewritten to search Settings, the standing rule filed and held because the search itself is unverified

Recorded 2026-09-04 at 17:00. This session ran across 2026-09-03 and 2026-09-04.

The capture reported that the install walkthrough of 2026-09-02 sent the user to Settings → System → Languages & input → On-screen keyboard → Manage on-screen keyboards, and that no such path exists on the Pixel 6. They found the screen themselves — titled **Keyboard apps** — and switched Hexboard on, so the step's outcome was reached in spite of its directions. That kind of rescue is exactly what a walkthrough must not depend on.

The live damage was not in the item that failed, which had already been walked to its end and removed, but in two items cleared to run earlier the same session. [physical-keyboard-handover] named Settings → Connected devices, which nobody had checked against the handset. [verify-a11y-ondevice] was worse in a different way: it asked for accessibility services to be active and never said how to turn them on, so it had no path to be wrong about yet. Both were rewritten by hand here, which is planning work rather than a build — the capture said so itself, and rewriting a queue item's steps is what a planning session does.

The route chosen is the Settings search box rather than a corrected menu path, because Android moves these screens between versions and a path written down goes stale on the next one while a search term does not.

**A correction was made to that work mid-session, after a plugin update changed the governing rules.** The rewritten steps had asserted what the search would return — "look for a result named **TalkBack**" — as though it had been checked. It had not, and could not be: this machine cannot read the phone's Settings. The updated rules require a claim about an outside surface handed to the user to be given as a guess with its fallback in the same sentence. Both steps now say what to look for, name the older menu path as the fallback, and say plainly that which route works is the first thing the step establishes.

That same limitation is why the standing rule was filed held rather than cleared. Writing "search Settings rather than name a path" into `CLAUDE.md` would make an unverified route binding on every walkthrough written afterwards — the same mistake one level up. It is held against [physical-keyboard-handover] because driving that item is the cheapest thing that settles whether the search works, and both walkthroughs now ask the user to report which route got them there.

**Queue changes:** [physical-keyboard-handover]'s step 2 rewritten to reach Bluetooth by search with Connected devices as the stated fallback, and pairing split from the check that follows it so each step carries one action and one thing to look for; [verify-a11y-ondevice] given the walkthrough it never had, opening on a TalkBack search with Accessibility as the fallback. [settings-steps-name-a-search] created and placed below the line against [physical-keyboard-handover].

**Work processed:** deleted [install-walkthrough-settings-path], its two live fixes made in-session and its standing-rule half split into [settings-steps-name-a-search].

**Advisory:** not needed — the close's recommendation names no single item to start from.
