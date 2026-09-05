# [HASH] — [layout-switching] cleared after both its open questions turned out to be desk answers, and after the reason to hold it was removed the same day

The picker's home and contents were settled on 2026-09-01 and 2026-09-02 — the app's own settings, layouts grouped by language and ordered by a set position, usage telemetry rejected outright as the first thing that would report user behaviour to a server. What stayed open was how the app enumerates layouts at runtime.

That closed on two facts. `AssetManager.list()` returns the names of every asset at a path, and [ship-all-layout-configs] built the copy task that puts every `key-layout*.json` into a flat assets folder on 2026-09-04 — so the app lists the assets and reads each config's `language` and `order`, the two fields [variant-language-fields] added at schema version 3 precisely so a picker could read them. Where the choice is remembered is an ordinary stored preference, the settings screen and the input method service being one application.

A stronger reason to hold it was raised and then removed within the same session. SPEC required a layout to be confirmed by a native reader before shipping, and the Russian config is inert today because nothing can select it — so the picker is what ships it. That would have held this item behind a check nobody could arrange. The user then removed the requirement, and the hold went with it.

It sits immediately after [layout-error-report] in the cleared region rather than carrying a `Blocked by:` line, because a hold would push it below the readiness line and out of the run that should build both — the picker makes the Russian layout reachable and the report route is what catches an error in it, so the safety net should land in the same build.

SPEC's out-of-scope line still lists "IME service polish (settings screen, language switching)" among things deferred for early iterations. It was deliberately not read as holding this back, and that is noted on the item for the next end-to-end read of SPEC.

**Queue changes:** [layout-switching] rewritten and moved into Processed, cleared to run, placed after [layout-error-report].

**Work processed:** kept — [layout-switching].
