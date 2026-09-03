# 37384e4 — The layout picker held against the Gradle change that puts the configs in the app, after the code showed only one of its two open questions ever needed a keyboard

Two questions kept this in Unprocessed, both recorded as wanting a running keyboard: how the app enumerates available layouts, and where it remembers the choice. Reading the code showed that only one of them did.

Enumeration has a real dependency, and it is not the keyboard. `android/app/build.gradle.kts` copies exactly one hard-coded file, `key-layout.json`, into the app's assets, so `key-layout-ru.json` exists in the repository and never reaches the app at all. There is nothing for a picker to enumerate until the copy task ships the folder rather than the file, which is [ship-all-layout-configs]. Remembering the choice turns out to need nothing: it is an ordinary stored preference read by both the app and the input method service, a desk decision whenever this is taken up.

The item was not designed out today, and the judgment is worth recording because it was Claude's recommendation and the user took it: a picker is only worth having once there is more than one layout *in the app*, and there is one. Designing a chooser for a list of one invites decisions that would be revisited the moment the second layout arrives, so the small Gradle change goes first and this follows it.

Two stale references were cleared while the item was open. It named [first-installable-build] as its blocker and described [variant-language-fields] as cleared to run; both shipped earlier the same day.

**Queue changes:** stays in Unprocessed, its `Blocked by:` moved from the shipped [first-installable-build] to [ship-all-layout-configs], with the Gradle finding and the storage answer written into its prose. The ordering is written on both items.

**Work processed:** kept in Unprocessed, held — [layout-switching].
