# [HASH] — savedstate and lifecycle-viewmodel declared outright rather than inherited transitively

Written 2026-09-04 at 16:00.

Settled with the user on 2026-09-02: declare them rather than waiting to see whether the build complains. As filed the item was conditional — add them if the Android Studio run fails on unresolved references, delete this if it succeeds — and that was rejected for two reasons.

Relying on a transitive dependency for something the source `import`s directly is a latent break rather than a working arrangement. It holds only while Compose UI and Activity keep exposing those artifacts as API rather than implementation, which is their decision and not this project's; a routine version bump can withdraw it, and the failure then arrives at a moment unrelated to anything changed here. Declaring what you import is the ordinary discipline, and it costs nothing in the packaged app because the artifacts ship either way. It also takes a conditional out of the queue: as written, the item had to be re-examined after the install run whichever way that run went.

The install run of 2026-09-03 settled which case this is — the build succeeded with both artifacts arriving transitively, so this is the latent break described above rather than a compile failure to repair.

The coordinates were settled on 2026-09-03 rather than left to the build. Half needed no lookup: `androidx.lifecycle:lifecycle-viewmodel` rides the `lifecycleRuntimeKtx = "2.11.0"` already in the catalogue, same group and same release train. The other half needed one narrow read: nothing in the catalogue covers `androidx.savedstate` and the Compose bill of materials reaches only `androidx.compose` artifacts, so a version entry of its own was added at 1.5.0, read from Android's savedstate release page that day. The base artifact is the one wanted rather than `savedstate-ktx`, since the four imported classes live in the base one.

Refused: leaving it conditional on the build's outcome, which leaves a queue item whose whole content is "find out" and a real import undeclared meanwhile; and leaving the coordinates as a lookup for the build, when half was readable from the repository and the other half cost a minute with the user present.

**Confirmed, transcribed from the tick:** a grep of the catalogue finds both entries, the app's dependency block lists both, and the service's six imports they cover are `androidx.lifecycle.ViewModelStore`/`ViewModelStoreOwner` and the four `androidx.savedstate` ones.

**Not confirmed:** that the versions resolve, which needs a Gradle sync in Android Studio. If a sync finds 1.5.0 superseded, take the current stable release rather than treating this record as binding — a version moves, and the item said so.

**Files touched:** `android/gradle/libs.versions.toml` (a savedstate version and two library entries, with a comment on why they are declared rather than inherited, 14 lines), `android/app/build.gradle.kts` (two implementation lines beside the existing lifecycle one, 2 lines).

**Routed to Captures:** none from this item.

**Depth:** short.
