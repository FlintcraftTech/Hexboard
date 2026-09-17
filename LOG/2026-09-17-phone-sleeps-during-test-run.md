# 88b95f2 — The screen-sleep item deleted, its work already done by a route its own walkthrough did not propose

Recorded 2026-09-17 13:59.

The Pixel 6's screen going dark mid-run is what failed eight of twelve instrumented tests on 2026-09-09 and recurred on 2026-09-12 — a Compose test needs a resumed activity, and a dark screen leaves none. The item's walkthrough proposed turning on Android's "Stay awake" developer option, found by searching Settings.

That search returned nothing on the handset, so the screen timeout was raised to thirty minutes instead, which outlasts the fifty-second suite comfortably. The suite now returns 28 of 28. All of that was recorded by the session that did it, which left the item in Unprocessed awaiting a disposition.

One thing was noticed here that changes what a follow-up would look like, and is worth keeping even though no follow-up was filed: Settings search surfaces entries from sections that are switched on, and Developer options is hidden until deliberately enabled. So the search finding nothing is more consistent with Developer options never having been turned on than with Android having moved the setting — the sturdier fix is probably still available, just not by searching for it.

It was dropped anyway, and the decision is "the timeout is good enough" rather than "the caveat is ignored". The entry's caveat is fair — a display timeout is a plain setting anyone could shorten, where the developer switch is tied to charging — but the fix works against a wide margin, and the diagnosis is now written down, so a recurrence costs minutes rather than a run spent rediagnosing. An item chasing the sturdier switch would sit in the queue being set aside every session for a durability gain nobody is currently paying for.

**Queue changes:** [phone-sleeps-during-test-run] deleted from Unprocessed. Nothing relocated — the session record of 2026-09-17 under [instrumented-tests-no-composition] already carries the outcome, the failed walkthrough and the caveat in full.

**Work processed:** deleted — [phone-sleeps-during-test-run].
