# 37384e4 — The Gradle copy task made to ship every layout config, filtered so the manifests and images stay out of the app

Read from the build file rather than trusted from the capture: `CopyKeyLayoutConfig` takes a single `RegularFileProperty` pointed at `resources/key-layout.json` and writes one hard-coded name into the generated assets directory. The output side needs nothing, [assets-srcdir-deprecation] having made it a `DirectoryProperty` the same morning. So the Russian layout built that day exists in the repository and cannot reach the phone.

The one decision was whether to copy the folder or filter it. `resources/` also holds `key-manifest.md`, `key-manifest-ru.md` and an `images/` folder; copying wholesale would put files in the app that no code reads, and the manifest in particular is generated *from* the config for people to read, so the app carrying it would be carrying a second copy of what it already parses. Claude recommended the `key-layout*.json` filter and the user agreed.

Held below the line against [install-and-enable-on-pixel] rather than cleared, because [assets-srcdir-deprecation] rewired this exact task earlier the same day and no Gradle sync has run since — a second unverified change stacking on an unproven one is what this project has held work back for before.

**Queue changes:** moved from Unprocessed into Processed below the readiness line with `Blocked by: [install-and-enable-on-pixel]`, and the files, the filter and the test written into it. [layout-switching] is held against it.

**Work processed:** kept — [ship-all-layout-configs].
