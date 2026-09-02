# [HASH] — Two undeclared AndroidX imports made explicit rather than left conditional on what the next build says

As filed, this item was a wager: add the dependencies if the Android Studio run fails on unresolved references, delete the item if it succeeds. Claude recommended against that and the user agreed.

Relying on a transitive dependency for something the source `import`s directly is a latent break rather than a working arrangement. It holds only while Compose UI and Activity keep exposing those artifacts as API rather than implementation, which is their decision and not this project's; a routine version bump can withdraw it, and the failure then arrives at a moment unrelated to anything changed here. Declaring what you import is the ordinary discipline and costs nothing in the packaged app, the artifacts shipping either way.

It also takes a conditional out of the queue. As written the item had to be re-examined after the install run whichever way that run went — either to do the work or to delete itself.

One thing is written as a premise rather than an assumption: the exact artifact coordinates, and whether they need explicit versions or ride an existing bill of materials, were not read from this project's catalogue. The libraries exist; which coordinates to use is a lookup the build does first.

**Queue changes:** moved from Unprocessed into Processed below the readiness line with `Blocked by: [install-and-enable-on-pixel]`, [assets-srcdir-deprecation] having changed the app's Gradle file that day with no sync since.

**Work processed:** kept — [declare-savedstate-viewmodel-deps].
