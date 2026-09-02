# [HASH] — The picker's ordering settled as curated rather than counted, telemetry rejected outright, and the hold moved onto an honest blocker

The user's design for the picker was layouts chosen by language and then by popularity, with a link to whoever made each one. Popularity had to be read from something, and Claude put three candidates: nothing automated, a GitHub signal, or usage telemetry from the app. Telemetry is the only true measure of popularity and it lost outright — it would have been the first thing in Hexboard to report what a user does back to a server, against the posture of every other feature in the project. The user took Claude's recommendation of a curated position set in the repository. Nothing counts anything, and no red flag arose from any of it as a result.

Two things designed here were removed later the same session, and both are recorded on the item so neither returns as an obvious omission: the creator link, and device-made layouts appearing alongside the shipped ones. Both went with the layout editor when layouts became copies of each language's own standard.

The hold was also corrected. The item still named [variant-schema] as its blocker, which shipped on 2026-09-01, so the reference pointed at nothing. It now names [first-installable-build], which is the honest obstacle: there is no settings screen to put a picker in, because the app has no input method service at all. It stays in Unprocessed rather than below the cleared line because what a build would change still cannot be stated.

Where the picker lives was settled on 2026-09-01 and is unchanged: the app's own settings, not the keyboard surface, because horizontal swipe already means "change panel".

The reasoning behind the removals is in the [variant-editor] entry of the same date.

**Queue changes:** rewritten; `Blocked by:` changed from [variant-schema] to [first-installable-build]; SPEC gained a sentence on how layouts are offered.

**Work processed:** kept in Unprocessed, held — [layout-switching].
