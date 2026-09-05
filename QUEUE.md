# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Raise the key's solid fraction and let the fade reach its neighbours [soft-edge-fraction-values]
The two numbers that decide how much of a key is solid and how far its fade reaches are changed, so a key reads as bigger again and its label grows back with it.

**What was found, and it came from you on 2026-09-04.** Your account of what the soft edge did: the gradient was built inside what was the boundary of the old circles, instead of around them where there is a little space. Reading `KeyGeometry.kt` the same day confirmed it. A key is painted as a radial gradient using two fractions of the touch radius — solid out to `SOLID_FRACTION = 0.55`, faded to nothing at `FADE_FRACTION = 1.0` — so the fade stops exactly at the edge of the touch target and puts nothing into the gap between keys. The old hard-edged disc was `radius - 3dp`, which is 0.86 of the touch radius at the Pixel 6's solved 22dp. So the part of a key that reads as definitely there fell from 0.86 of the radius to 0.55, which is the shrinkage rather than anything about the fade being soft.

**One cause, two complaints.** [soft-key-edge] was built to answer your complaint that the keys look too small, and it made them read smaller. Label size is a fixed fraction of the solid radius, so the glyphs shrank by about a third at the same moment and for the same reason. [label-size-after-soft-edge] reported that half and is merged into this item, its three options carried below.

**The values, chosen rather than compared, on your instruction on 2026-09-04.** You were offered a comparison affordance on the phone — two or three settings to flick between — and chose to have a value picked and shipped with the install instead. Last session had already tried to get a comparison page in front of you and could not, which is recorded on [soft-key-edge].

- `SOLID_FRACTION` 0.55 to **0.75**. The definite part of the key comes back to three quarters of the touch radius, against 0.86 for the old hard disc — most of what was lost, while staying clear of an edge so late it reads as hard again.
- `FADE_FRACTION` 1.0 to **1.045**. Neighbouring key centres sit 2.09 radii apart, so half that distance is 1.045 radii: adjacent fades meet exactly and there is no visibly dead space between keys, which is what your own diagnosis asked for.

**Why 1.045 can never overlap, which is the part worth writing down.** The gap between touch targets is `max(1.5, radius * 0.09)`, so at any solved radius at or above about 16.7dp the gap is 0.09 of the radius and the centre spacing is exactly 2.09 radii. Below that the floor of 1.5dp takes over and the spacing is proportionally wider, so the fades fall a little short of meeting rather than running into each other. The value is therefore a ceiling at every radius the board can solve.

**What the labels do.** They are sized against the solid radius, so raising it to 0.75 grows every glyph by about a third from where this run left it, landing roughly 13% below the size they were on 2026-09-03. That residual is deliberate: a glyph belongs inside the solid part of the key rather than out on the fade, which is the rule [soft-key-edge] introduced and this item keeps. Raising `LABEL_FRACTION` to close the last 13% would need it above 1.0, which would put glyphs onto the fade.

**A wrong figure in the code, corrected as part of this.** `KeyGeometry.kt`'s own comment names the generous end worth trying as "solid to 0.45 and transparent at about 1.18, where neighbouring fades just meet". At 1.18 radii the fades overlap substantially; they meet at 1.045. The figure was written on 2026-09-04 and would have been trusted by whoever tuned these next, so it is corrected with the arithmetic beside it rather than merely replaced.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — `SOLID_FRACTION` becomes 0.75 and `FADE_FRACTION` becomes 1.045; the doc comment's "about 1.18" is replaced by 1.045 with the 2.09-radii spacing and the small-radius floor written out, so the next tuning session has the derivation rather than a number.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyEdgeTest.kt` — carries the observation below, added to the assertions already there.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`, to confirm the gradient's solid stop is still derived as `SOLID_FRACTION / FADE_FRACTION` and needs no edit of its own.

**The observation that shows it landed:** `KeyEdgeTest` passes with a new assertion that across the whole solvable radius range the drawn fade never exceeds half the distance between neighbouring key centres — so fades meet and never overlap — and that the solid radius is three quarters of the touch radius. The existing assertions that the solid fraction is strictly below the outer one and that the outer one is at least 1.0 both still hold.

**Options already refused, each with what defeated it.** Restoring the old 0.86 solid fraction — that is where the hard edge effectively was, so it reinstates the boundary the fade exists to dissolve. Pushing only the fade outward and leaving the solid fraction alone — the gap is 9% of the radius, so it buys about 1dp and answers neither complaint. Sizing labels against the fade radius rather than the solid one, which is [label-size-after-soft-edge]'s first option — it puts glyphs out over the fading part, which [soft-key-edge] explicitly did not want. Raising `LABEL_FRACTION` to compensate, that item's second option — it would have to exceed 1.0 and lands in the same place. Leaving it alone, that item's third option — defeated by your instruction to pick a value and ship it. Building a comparison affordance to choose between values on the phone — offered and declined by you on 2026-09-04 in favour of shipping one value.

Rests on: `SOLID_FRACTION = 0.55`, `FADE_FRACTION = 1.0`, `LABEL_FRACTION = 0.90` and the `max(1.5, radius * 0.09)` gap rule, all read from `KeyGeometry.kt` on 2026-09-04; the old `VISIBLE_INSET = 3f` and the 22dp radius solved for a 411dp width, both recorded in [soft-key-edge]'s own text; that the centre spacing is `2 * radius + gap` in every direction, which is the hexagonal packing `KeyGeometry` computes and was checked against its own arithmetic on 2026-09-04 rather than run; that neither value has been seen on a screen, which is what [verify-this-runs-build-on-device] is for.

Placed immediately above [verify-this-runs-build-on-device] so it compiles and installs in the same sitting, which is what shipping a picked value rather than comparing values requires. That ordering is written on both items.

#### Work out which keys neighbour which, from the config and the zag rule [predictive-neighbour-table]
The app learns, for every key on a panel, which keys sit next to it. That table is what the planned autocorrect measures distance with, and nothing computes it today.

**Why it is its own item, and why it can be built before anything else.** [uniform-neighbours-predictive] rests on a claim about geometry: in a hexagonal tessellation each interior key has exactly six equidistant neighbours, so the set of keys a user might have meant is uniform. Nothing in the app knows that set. The item's own note, added 2026-08-07, is that the neighbour sets are derivable from `resources/key-layout.json` but not from it alone — the config carries each key's `row` and `col` and no geometry, while which six keys touch a given key depends on the zag parity that lives in `KeyGeometry.kt`. So this is arithmetic over data the app already has, with no dependency on a dictionary, on the correction behaviour, or on anything being verified on a phone.

**Three questions the parent item said to settle first, all answered on 2026-09-04 and all desk answers.**

- **Keys at a panel's edge have fewer than six neighbours, and that is fine.** A key's neighbours are the keys one step away from it; six is what an interior key happens to have. The distance measure needs a neighbour set, not a set of exactly six, so an edge key simply has a smaller one. Uniformity is a property interior keys have rather than a requirement the measure imposes.
- **The two space bars in row 3 are excluded from the neighbour model.** A tap that lands on a space ends the word rather than substituting a letter, and ending the word is the correction moment itself, so a space is never one of the things the user might have meant instead. Row 3's space keys are therefore left out of every neighbour set and given none of their own.
- **Neighbour sets are computed per panel and never span two.** A mis-tap cannot cross a swipe boundary, so a key on QWERTY has no neighbours on RARE or SYMBOLS.

**How the neighbours are found.** Every key's centre is already computed by `KeyGeometry.centre(row, col, radius)`, and in the hexagonal packing the distance from a key to each of its six neighbours is exactly one vertical step — `2 * radius + gap(radius)` — in every direction. So a key's neighbours are the keys on the same panel whose centre lies within a small tolerance of one vertical step away. The tolerance exists because the packing is computed in floating point rather than because the spacing is approximate. The result is scale-free: the same table comes out at any solved radius, since every distance is a multiple of the radius.

**Why this is not a lookup the config could hold**, recorded because putting it in the config is the obvious suggestion. SPEC's key-inventory principle keeps geometry out of the config deliberately — the zag rule is the perceptual wedge and lives in Kotlin — so a neighbour table written into `key-layout.json` would put derived geometry into the one file that is supposed to carry none, and would have to be re-derived by hand for every new language. Computing it at runtime costs a pass over the keys once per layout.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — a new function returning, for one panel's keys, each key's neighbour indices: the keys whose centre sits within tolerance of one vertical step away, with row 3's space keys excluded both as neighbours and as holders of a neighbour set.
- `android/app/src/test/java/tech/flintcraft/hexboard/NeighbourTableTest.kt` — new, carrying the observation below.

Reads but does not change: `resources/key-layout.json` and `resources/key-layout-ru.json` for real key positions to test against, and `android/app/src/main/java/tech/flintcraft/hexboard/KeyLayout.kt` for the shape the keys arrive in.

**The observation that shows it landed:** `NeighbourTableTest` passes, asserting on both shipped configs that a key well inside QWERTY has exactly six neighbours, that a key at the left edge of a row has fewer, that every neighbour relationship is mutual, that neither space key appears in any neighbour set, and that the table computed at a 12dp radius is identical to the one computed at 34dp — the two ends of the solvable range, which is what shows the result is scale-free.

**Options already refused, each with what defeated it.** Writing the neighbour table into `key-layout.json` — puts geometry into the file SPEC keeps geometry out of, and makes every new language a hand-derivation. Hard-coding a table for QWERTY — there are two shipped layouts already and the Russian one is eleven columns wide, so a hard-coded table is wrong for it on the day it is written. Treating adjacency as "row and column differ by at most one" — that is rectangular adjacency and gets the zag wrong, which is the whole thing this project's geometry does differently.

Rests on: `KeyGeometry.centre`, `verticalStep` and the `max(1.5, radius * 0.09)` gap rule, read from `KeyGeometry.kt` on 2026-09-04; that every one of a key's six neighbours sits exactly one vertical step away, checked against that file's own arithmetic on 2026-09-04 rather than run; the config carrying `row` and `col` and no geometry, read from `resources/key-layout.json` on 2026-09-04.

[uniform-neighbours-predictive] is held against this item and [predictive-dictionary-bundle], being the correction engine that uses both. That ordering is written on all three entries.

#### Ship an English word list with the app, with the proper names left out [predictive-dictionary-bundle]
The word list the planned autocorrect compares finished words against, generated from SCOWL, committed to this repository, copied into the app at build time and readable from Kotlin.

**Why it is its own item.** [uniform-neighbours-predictive] cannot correct anything without a dictionary, and choosing and packaging one is independent of the correction behaviour, of the neighbour geometry and of anything being verified on a phone. It is also the half with a licence question attached, which is a different kind of care from the arithmetic.

**Which list, and why it is already settled.** SCOWL, established in `workshop/resources/research/word-list-licence-and-frequency.md` on 2026-09-01. Its licence is permissive enough to ship inside this repository under PolyForm Noncommercial, and — the part that matters most here — it separates proper names into categories of their own, so SPEC's no-proper-nouns rule is satisfied by leaving those categories out at generation time rather than by writing a filter that guesses at what is a name. That rule exists because a word corrected into a name is the most irritating failure autocorrect has.

**How ties get broken, decided on 2026-09-04, and it is a narrowing.** SCOWL carries no frequency data. What it has is size levels — a coarse commonness ranking, since a word's level reflects how common it is — and the first version uses that and bundles no second source. The research finding named `wordfreq` (CC BY-SA 4.0, ShareAlike raising a question about a repackaged table shipped under a different licence) and the Leipzig Corpora Collection (reported CC BY 4.0) as the real frequency tables. Leipzig was the cleaner of the two on licence grounds and its terms could not be read: on 2026-09-04 both its download page and its frequency-dictionary page turned out to sit behind a proof-of-work bot challenge, so the CC BY 4.0 figure remains second-hand. Rather than bundle data on an unread licence, the first version does without. A real frequency table is a later question with its own item, and whoever takes it up needs a route past that challenge.

**What a build actually produces**, stated because "ship a word list" hides the work. A generation script run once, by hand, producing a plain text file of one word per line that is committed like the emoji data already is — not a build-time download, which would make the build depend on a network and on someone else's server staying up. The script records in the file's own header which SCOWL release and which size level it was generated from, so the list can be regenerated identically.

**What the build changes.**
- `scripts/generate-word-list.py` — new. Reads a SCOWL distribution, takes the English word files at the chosen size level, leaves out the proper-name categories, and writes the list with a header naming the SCOWL version, the size level and the categories included and excluded.
- `resources/wordlist-en.txt` — new, generated by that script and committed, alongside the key configs and the emoji data.
- `android/app/build.gradle.kts` — the existing copy task, which already takes `resources/key-layout*.json` and `emoji-test.txt` into the app's assets, also takes the word list.
- `android/app/src/main/java/tech/flintcraft/hexboard/WordList.kt` — new. Reads the list from the assets and answers two questions: is this exact word in the list, and what is its size level.
- `README.md` — the Notices section gains SCOWL's attribution beside the FlorisBoard and Unicode ones already there.
- `android/app/src/test/java/tech/flintcraft/hexboard/WordListTest.kt` — new, carrying the observation below.

Reads but does not change: `workshop/resources/research/word-list-licence-and-frequency.md`, for the licence terms and the trap it names.

**The observation that shows it landed:** `WordListTest` passes, asserting that the committed list parses, that a sample of ordinary words is present, that a sample of common first names and place names is absent — which is the no-proper-nouns rule made checkable rather than asserted — and that the file's header names a SCOWL version and a size level. Whether the list actually reaches the app's assets is checked by the app running, so it belongs to the next install rather than to this test.

**Options already refused, each with what defeated it.** `hermitdave/FrequencyWords` — presents as MIT, but the MIT licence covers the generator and the data is derived from OpenSubtitles with its own terms; named as a trap in the research finding. `wordfreq` — CC BY-SA, so a filtered repackaged table shipped inside an app under a different licence raises a ShareAlike question that would have to be answered first. Leipzig — the licence could not be read at source, above. Downloading the list at build time — makes every build depend on a network and someone else's uptime. Writing a proper-noun filter — SCOWL already separates them, so a filter would be guessing where the data already knows.

Rests on: SCOWL's licence and its separable proper-name categories, read from its own readme on 2026-09-01; that SCOWL ships no frequency data and that its size levels are a commonness ranking, from the same read; that Leipzig's own terms are unreadable from here behind a bot challenge, attempted 2026-09-04; the existing copy task taking a filtered set of files from `resources/`, read from `android/app/build.gradle.kts` on 2026-09-04.

[uniform-neighbours-predictive] is held against this item and [predictive-neighbour-table], being the correction engine that uses both. That ordering is written on all three entries.

#### Report a wrong key on a layout, from inside the app [layout-error-report]
Red flag · State: cleared
A "Report a problem" entry in the app that composes a short report about the layout in use — which layout, which app and Android version, which handset, and whatever the person types about what is wrong — shows it, and hands it to their own mail app to send.

**Why it exists, decided by you on 2026-09-04.** SPEC required each language's layout to be confirmed by someone who reads the language before it ships. You judged that unrealistic, and the record already agreed in advance: [language-list-choice] was held against [russian-layout-check] on exactly the ground that the reader check is the bottleneck, scaling with whether a reader can be found rather than with how many people speak the language, and [russian-layout-check] itself has sat dated a month out because no Russian typist was to hand. Your replacement is easy reporting, and this is it.

**Why the swap is safe, which turns on what the requirement was guarding.** [russian-layout-check] gives the reason for the confirmation rule: a shipped layout gets copied and imitated, so an error propagates rather than sitting still. But layouts are not invented here — each is transcribed from FlorisBoard's own layout and popup files, which are already in use by speakers of that language. So the risk is a bad *transcription*, not a bad *design*: a letter in the wrong slot. That is what a user notices at once and what a report fixes in one line. The cost, stated rather than glossed: the first user of each new layout becomes the person who finds the errors, where a reader would have found them before anyone typed.

**Your wider aim, and the part of it this item is.** You want to catch people before they go to the Play Store and leave a review, accepting that this may mean a lot of email and that screening it is a later problem worth having. That general funnel is filed as [feedback-funnel-before-store]; this item is the narrow piece the SPEC change actually needs, and it is deliberately small because Hexboard has no Play Store listing and no keyboard yet verified on a handset.

**One policy line this stays clear of, read on 2026-09-04.** Google's in-app review guidance forbids asking the user any question before or while presenting the rating prompt — including "are you enjoying the app?" — because filtering for happy reviewers inflates ratings. So a report route must never be attached to a rating flow. This one is not: it is a standing entry that is always there, unattached to any prompt, and it makes no judgment about anyone's mood.

**The privacy risk, raised by Claude and designed out, which is what clears the flag above.** A keyboard that sends reports is one keystroke of carelessness away from being a keyboard that sends your text. Four things answer it, and three are structural rather than promises:

- **The report is built from a fixed list of fields and there is no path from the text field into it.** Nothing from `InputConnection`, the clipboard or any key history is available to the report builder; it takes the layout id, the app version, the Android version, the device model and the person's own typed description, and nothing else exists for it to take.
- **Nothing is sent by the app.** It opens the person's own mail app with the text prefilled, so they read it and press send themselves. Hexboard makes no network request and needs no internet permission for this.
- **The composed text is shown before it goes**, so the claim above is checkable by the person making the report rather than taken on trust.
- **The address is not in the repository.** It is read from `android/local.properties`, which is gitignored, into a build config field; where that field is empty the entry does not appear at all. This repository is public and the scrub rules bar a personal contact detail from a tracked file.

**Why the app rather than the keyboard.** The keyboard surface spends gestures, and horizontal swipe already means "change panel" — the same reasoning that put the layout picker in the app's own settings on 2026-09-01. A report is also not something anyone does mid-sentence.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/ProblemReport.kt` — new. Builds the report text from the five fields above and nothing else. A pure function, so what it can and cannot contain is checkable without a running app.
- `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt` — a "Report a problem" entry: a description box, the composed report shown in full beneath it, and a send button that opens an `ACTION_SENDTO` mail intent with the text prefilled. Hidden entirely when the address is unset.
- `android/app/build.gradle.kts` — `buildFeatures` gains `buildConfig = true` beside the existing `compose = true`, and an optional `hexboard.reportAddress` is read from `local.properties` into a build config field, defaulting to empty. This is the same pattern `hexboard.buildDir` already uses, added by [build-output-off-drive].
- `android/app/src/test/java/tech/flintcraft/hexboard/ProblemReportTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/KeyLayout.kt`, for the layout id and language the report names.

**The observation that shows it landed:** `ProblemReportTest` passes, asserting that a composed report contains the layout id, the app version, the Android version and the device model; that it contains the description it was given and no other free text; and that `ProblemReport` exposes no way to pass it text from an input connection or a clipboard — the last being asserted by the function's own signature taking the five fields and nothing else, which is what makes "it cannot carry what you typed" a property of the code rather than a claim about it. Whether the mail app actually opens is a check for the phone and belongs to whoever next installs.

**Options already refused, each with what defeated it.** A rating prompt that asks how it's going and routes unhappy users to a form — prohibited by Google's own guidance, above. Sending the report from the app over the network — needs an internet permission on a keyboard, needs somewhere to send it, and removes the person's own sight of what leaves; the mail intent gives the same reach for none of that. A GitHub issue link — public, and it asks for an account before anyone can report a wrong letter. Including recent text for context — the one thing this must never do. Putting the entry on the keyboard surface — spends a gesture the panels already use.

Rests on: `android/local.properties` being gitignored, checked with `git check-ignore` on 2026-09-04; `buildFeatures` currently declaring only `compose = true`, read from `android/app/build.gradle.kts` on 2026-09-04; Google's in-app review guidance forbidding a pre-screening question in the rating flow, read on 2026-09-04 from its own documentation and secondary summaries; that layouts are transcribed from FlorisBoard rather than designed here, recorded in `workshop/resources/research/open-source-layout-sources.md` on 2026-09-02.

[feedback-funnel-before-store] is the general version of this and is deliberately not built here. [russian-layout-check] and SPEC's layout principle both change with this item; those changes were made in the planning session of 2026-09-04 rather than by a build.

#### Let a user choose which layout variant they are typing on [layout-switching]
The app's own settings offer the layouts Hexboard ships, grouped by language; the one chosen is what the keyboard draws from then on.

Filed by /rescan on 2026-08-20, from a requirement SPEC created the same day and nothing in the queue held: if one app ships several layouts, the user has to be able to pick theirs. It was named as an accepted cost when the platform decision was made and then not filed, which is how a feature dies in SPEC. **Designed out on 2026-09-04.**

**Where the picker lives** was settled by you on 2026-09-01 and is in SPEC: the app's own settings, not the keyboard surface. The keyboard-surface option — a long-press or a gesture, reachable without leaving what you are typing — lost because horizontal swipe already means "change panel", so the picker would have had to find a gesture the layout has not already spent. Recorded because it is the obvious idea and will otherwise be re-proposed.

**What the picker shows**, settled with you on 2026-09-01 and narrowed on 2026-09-02: layouts grouped by language, ordered within each language by a set position rather than by anything counted. Usage telemetry was the alternative for ordering and lost outright — it is the only true measure of popularity and it would have been the first thing in Hexboard to report what a user does back to a server, against every other feature's posture. Nothing counts anything.

Two things this picker was going to show and now will not, recorded because both were designed and then removed rather than forgotten: a link to whoever made the layout, and layouts the user built on the device sitting alongside the shipped ones. Both went on 2026-09-02 with the layout editor, when you settled that Hexboard ships layouts copied from each language's own standard and nobody builds their own.

**Both open questions closed on 2026-09-04, and neither needed a running keyboard after all.**

- **Enumeration.** `AssetManager.list()` returns the names of every asset at a given path, and [ship-all-layout-configs] made the assets folder hold every `key-layout*.json` rather than one hard-coded file. So the app lists the assets, keeps the ones matching that pattern, and reads each config's `language` and `order` — the two fields [variant-language-fields] added at schema version 3 precisely so a picker could read them. This was the half genuinely waiting on something, and what it was waiting on has shipped.
- **Where the choice is remembered.** An ordinary stored preference. The settings screen and the input method service are the same application, so both read the same preferences with no cross-process machinery; the service re-reads it when the keyboard is next shown, so a change takes effect without a restart.

**Why it lands right after [layout-error-report], and the ordering matters rather than being tidy.** This picker is what makes the Russian layout reachable at all — the config ships into the assets today and nothing can select it, so it is inert. SPEC no longer holds a layout back for a native reader's confirmation, on your decision of 2026-09-04; what catches an error instead is the in-app report route. So the safety net should land in the same build as the thing that needs it, rather than after it. That is placement and a sentence rather than a `Blocked by:` line, because a hold would push this below the readiness line and out of the run that should build both.

**Why it was not designed out on 2026-09-02, which is a judgment worth keeping.** The picker is only worth having once there is more than one layout *in the app*, and there was one — the Gradle task copied a single hard-coded file. Designing a chooser for a list of one invites decisions that would be revisited the moment the second layout arrived, so the small Gradle change went first. Claude's recommendation, your agreement. That change shipped on 2026-09-04.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/LayoutCatalogue.kt` — new. Lists the assets, keeps the `key-layout*.json` entries, reads each one's `id`, `name`, `language` and `order`, and returns them grouped by language and ordered within each group, with a missing `order` sorting last.
- `android/app/src/main/java/tech/flintcraft/hexboard/LayoutPreference.kt` — new. Reads and writes the chosen layout's `id` in the app's shared preferences, falling back to the config marked `isDefault` where nothing is stored or the stored id no longer exists.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyLayout.kt` — the loader takes the asset filename to open rather than assuming `key-layout.json`.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — loads the chosen layout when the input view is created, so a change made in the app is in effect the next time the keyboard appears.
- `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt` — the picker itself: the layouts grouped by language with the current one marked, and a tap storing the choice.
- `android/app/src/test/java/tech/flintcraft/hexboard/LayoutCatalogueTest.kt` — new, carrying the first half of the observation.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/LayoutSwitchingUiTest.kt` — new, carrying the second half.

Reads but does not change: `resources/key-layout.json` and `resources/key-layout-ru.json`, for the fields the catalogue reads; `android/app/build.gradle.kts`, to confirm the copy task already puts both into a flat assets folder.

**The observation that shows it landed:** `LayoutCatalogueTest` passes, asserting that a set of configs is grouped by language, ordered by `order` within a language, that a config with no `order` sorts last, and that a stored id naming a layout that is not present falls back to the default rather than failing. `LayoutSwitchingUiTest` picks the Russian layout in the app, brings the keyboard up and asserts Cyrillic keys are drawn, then picks English again and asserts the Latin ones are back. Nothing here can run either, so running them is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** A picker on the keyboard surface — horizontal swipe is already spent on panels. Ordering by usage telemetry — the first thing that would report user behaviour to a server. A creator link, and device-made layouts alongside the shipped ones — both went with the layout editor on 2026-09-02. Hard-coding the list of layouts in Kotlin — a further language would then be two changes rather than the one file SPEC promises. Holding this against [layout-error-report] with a `Blocked by:` line — it would drop below the readiness line and out of the run that should build both.

Rests on: `AssetManager.list()` returning the names of all assets at a path, read from Android's class reference on 2026-09-04 and not run; the assets folder being flat with each config keeping its own filename, read from `android/app/build.gradle.kts` on 2026-09-04; `language` and `order` existing at schema version 3, read from `resources/key-layout.json` on 2026-09-04; that the settings screen and the input method service share one preferences store, which follows from their being one application and has not been run.

SPEC's out-of-scope line still lists "IME service polish (settings screen, language switching)" among things deferred for early iterations. That line was not read as holding this back, because the reason to build it now is [layout-error-report] and the Russian layout becoming reachable, not a judgment about which iteration this is. Worth a look next time SPEC is read end to end.

#### Transcribe the first five layouts: French, German, Spanish, Portuguese, Italian [first-batch-layouts]
Five new layout configs, each transcribed from FlorisBoard's own files, so Hexboard ships seven layouts rather than two.

**Chosen by you on 2026-09-05**, from the plan settled the same day in [language-list-choice]. All five are Latin script, so nothing here touches the geometry, the zag rule, right-to-left text or anything the Russian layout had to solve. The order is deliberate: French and German first because they are genuine key-order changes and therefore the ones that prove the route, then the three that are QWERTY with different long-press accents.

**Two levels of cheapness, found in FlorisBoard's own file listing on 2026-09-05 and worth knowing before starting.** French has `azerty.json` and German has its own file, both real re-orderings of the letters. Portuguese and Italian have **no layout file at all** — they use the shared `qwerty.json` and differ only in their popup mappings, `pt.json` and `it.json`. Spanish has `spanish.json`, which is QWERTY plus Ñ. So three of the five are the same board with different accents, and the work there is the popup mapping rather than the rows.

**Which German file, to be settled by reading rather than guessed.** The listing carries both `german.json` and `qwertz.json`, and which one matches what a German phone keyboard actually shows was not established. Read both, take the one matching, and record in the config's `about` field which file was transcribed — the same discipline [language-starter-layouts] used for Russian.

**Row widths.** Spanish adds Ñ, which makes its middle row eleven keys wide. That case is already solved: SPEC allows rows wider than ten with correspondingly smaller keys, and [language-starter-layouts] shipped an eleven-wide Russian board on exactly that rule. No letter is hidden or dropped to fit ten columns.

**Every letter reachable, visible or by long-press**, per SPEC. For these five that mostly means the accented forms sit under their base letters, which is what the popup mapping files record.

**What the build changes.**
- `resources/key-layout-fr.json` — new. French, transcribed from FlorisBoard's `azerty.json` with popups from `fr.json`.
- `resources/key-layout-de.json` — new. German, from whichever of `german.json` and `qwertz.json` matches, with popups from `de.json`.
- `resources/key-layout-es.json` — new. Spanish, from `spanish.json` with popups from `es.json`, its middle row eleven wide.
- `resources/key-layout-pt.json` — new. Portuguese: the shared `qwerty.json` rows with popups from `pt.json`.
- `resources/key-layout-it.json` — new. Italian: the shared `qwerty.json` rows with popups from `it.json`.
- `resources/key-manifest-fr.md`, `-de.md`, `-es.md`, `-pt.md`, `-it.md` — generated by `scripts/generate-key-manifest.py`, never hand-edited.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/ShippedConfigsTest.kt` — extended to cover the five new configs.
- `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` — extended likewise.

Reads but does not change: `resources/key-layout.json` and `resources/key-layout-ru.json` as the shape to follow; `android/app/build.gradle.kts`, to confirm the copy task already takes every `key-layout*.json` and needs no edit — which is what [ship-all-layout-configs] built.

Each config carries `language` as a BCP 47 tag and an integer `order`, both required at schema version 3, and `isDefault` false — English stays the default.

**The observation that shows it landed:** `KeyLayoutValidationTest` and `ShippedConfigsTest` pass over seven configs rather than two, each new one having a non-blank BCP 47 language, an integer order, and its generated manifest present and matching. The generator's own `validate()` refuses a missing or blank language and a non-integer order, so a config that fails those does not reach the assets.

**Options already refused, each with what defeated it.** Picking the batch by where Android users are rather than by what the config model supports — it puts Hindi near the top, which is a Devanagari job of its own rather than a transcription, and was offered and declined on 2026-09-05. Waiting for someone to ask for each of these — the demand-driven policy stands for languages beyond this batch, but with no users there is nobody to ask, which is the reason a first batch exists at all. Hiding letters to fit ten columns — barred by SPEC and already settled against for Russian.

Rests on: FlorisBoard's layout and popup file listing, read on 2026-09-05; its Apache 2.0 licence and the attribution convention, recorded in `workshop/resources/research/open-source-layout-sources.md` on 2026-09-02; `language`, `order` and `isDefault` existing at schema version 3 and the generator validating the first two, read from `resources/key-layout.json` and recorded in [variant-language-fields]; that the copy task already ships every `key-layout*.json`, built by [ship-all-layout-configs] on 2026-09-04 and not yet compiled.

Placed after [layout-switching], because until the picker exists these five ship into the assets and cannot be selected — exactly the state the Russian layout has been in. That ordering is written on both entries. Dictionaries for these languages are not this item's: a layout with no word list is usable and simply offers no correction, which SPEC now states.

#### Bound the board's height so a sideways phone is not all keyboard [landscape-board-height]
The solved key radius is limited by the available height as well as the width, so the keyboard cannot grow to fill a landscape screen.

**Found on 2026-09-05 while designing [split-layout-wide-screens]**, by arithmetic over `KeyGeometry.kt` rather than by seeing it. `solveRadius` takes a width and a column count and returns the largest radius that fits, capped at `MAX_RADIUS = 34`. Nothing consults the height. On a Pixel 6 turned sideways the width is large enough to hit that cap, and four rows at a 34dp radius give a board about 323dp tall, plus about 71dp for the row above the keys — roughly 394dp of keyboard on a screen about 411dp tall. The text field being typed into would be a sliver.

**Why it is filed apart from the split.** [split-layout-wide-screens] moves the two halves apart horizontally and does nothing about height, so on its own it would deliver thumb-reachable halves on a keyboard that has swallowed the screen. The split is a feature and this is closer to a defect, so bundling them would hold the feature behind the fix. That item is held against this one.

**The fix, and it is a second constraint rather than a new rule.** The radius is solved from the width as it is today, then reduced where necessary so the board plus the row above it fits within **half the available height** — the smaller of the two answers wins, with `MIN_RADIUS` still flooring it. A proportion of the height rather than a fixed number of dp, which is how every other size in this file is derived, and half is the share a phone keyboard conventionally takes rather than a figure invented here.

**What it must not do: change the portrait board.** In portrait the current board comes to roughly 259dp including the strip, against a screen around 915dp tall — well inside half — so the height constraint should never bind there, and the observation below asserts exactly that. A fix that quietly shrinks the keys everyone already uses would be a worse bug than the one it repairs.

**Where the available height comes from.** Compose's `LocalConfiguration` carries the screen size in dp. `KeyboardPanel.kt` already uses `BoxWithConstraints` for width, but an input method's view wraps its own content vertically, so `maxHeight` there is not the screen and cannot be used for this.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — the radius solver gains an available height and a row count, and returns the smaller of the width-derived radius and the largest radius whose board plus strip fits half that height, floored at `MIN_RADIUS`. The existing width-only behaviour stays reachable for callers with no height to give.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `HexboardBoard` reads the screen height from the configuration and passes it, with the row count it already computes.
- `android/app/src/test/java/tech/flintcraft/hexboard/BoardHeightBoundTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt`, to confirm nothing else sizes the view.

**The observation that shows it landed:** `BoardHeightBoundTest` passes, asserting that at a portrait viewport of about 411 by 915dp the solved radius is exactly what the width-only solver returns today — so portrait is untouched — and that at a landscape viewport of about 915 by 411dp the board plus strip comes to no more than half the height, and the radius is above `MIN_RADIUS`. Both are arithmetic over `KeyGeometry`, so neither needs a device.

**Options already refused, each with what defeated it.** Lowering `MAX_RADIUS` — it would shrink the keys on large portrait screens, which is the opposite of this project's whole argument. A fixed maximum board height in dp — a bare number with no derivation, and wrong on the next screen size. Letting the keyboard scroll — a keyboard is aimed at from muscle memory, so a board that can be scrolled is a board whose keys are not where they were. Solving the split first and treating height as a later polish — the split does not reduce height at all, which is the finding that produced this item.

Rests on: `solveRadius` taking width and columns only, `MAX_RADIUS = 34`, `MIN_RADIUS = 12`, `gap(radius) = max(1.5, radius * 0.09)` and `boardHeight`'s formula, all read from `KeyGeometry.kt` on 2026-09-05; `stripHeight` being one vertical step floored at 48dp, read from `KeyboardPanel.kt` on 2026-09-05; the Pixel 6's own dimensions of roughly 411 by 915dp, taken from the 411dp width this project has already solved against and recorded in [soft-key-edge]; that Compose exposes the screen size through `LocalConfiguration` in `androidx.compose.ui.platform`, confirmed present in that package's index on 2026-09-05 but with its field names not read at source — to be confirmed at the start of the build, where the compiler settles it in one attempt.

**The whole finding is arithmetic and has not been seen**, which is worth saying plainly: nobody has turned the keyboard sideways. [verify-this-runs-build-on-device] is the first sitting where anyone could, and turning the phone is one gesture — but this item does not wait on it, because the arithmetic is checkable here and a wrong keyboard in landscape is not something to ship while waiting for confirmation.

#### Phrase-at-a-time prompting, tried on a throwaway page before anything is built on it [rsvp-dictation-prompter]
A page in `planning/` that shows a passage one phrase at a time, large, advancing only when the speaker says so. Its job is to settle whether the idea works before anything is built on it.

Captured by you on 2026-09-04 at 13:02, during the drive of [recogniser-gap-comparison], from noticing that dictation performed far better in the test than it does for you ordinarily. Refined by you at 13:09. **Reframed on 2026-09-05, when you said what you had actually been thinking of: not dictation tests, but the sitting where someone trains the speech model.** The design is unchanged by that; where it eventually belongs is not, and the real destination is now [enrolment-prompter].

**The problem, in your account.** Handed a written passage, you can read ahead, so you know what you are about to say — and knowing that is exactly what removes the hesitation. Your ordinary use runs to at least three corrections per short sentence; the test run was, in your words, "way, way, way, better than normal". You narrate fluently when reading and hesitate constantly when speaking off the cuff, and it is the hesitation a recogniser cannot follow.

**And it bites hardest on enrolment, which is your point.** A model trained on someone reading aloud adapts to their reading voice, which is not the voice they dictate in — so the training would fit the wrong target. There is a fairness problem alongside it: a method that requires fluent reading aloud gives anyone with dyslexia or a reading disability a worse personal model, which is worse than giving them a worse test score.

**The design, and the two refinements are what make it work.**

- **One piece at a time, large, in the manner of Spritz or Spreeder** — no eye scanning, and, the load-bearing part, no seeing ahead. Foreknowledge is removed by construction rather than by asking someone not to read ahead.
- **Phrases rather than single words.** Your correction at 13:09: one word at a time is its own unusual act and would elicit its own stilted delivery, which is a different unnatural speech rather than the natural speech wanted. A phrase is the unit people speak in.
- **The speaker sets the pace**, not the display. Also yours at 13:09, and explicitly not Spreeder's forced march, which makes the speaker chase the prompt and would fail worst for the slower readers this is meant to include.
- **A rotating set** big enough that repeated runs do not teach the content, which would reintroduce foreknowledge on a slower clock.

**Why this is a page rather than the real thing, decided on 2026-09-05.** The claim the whole design rests on — that a phrase-at-a-time, speaker-paced prompt actually produces ordinary hesitant speech — has never been tried by anyone. A page you can open and speak into settles that in an evening. If people slip into reading-aloud voice anyway, the idea dies for a few hours' work rather than after being built into an enrolment flow that is months away, behind [personal-voice-model] and [in-keyboard-voice-input]. **This is deliberately throwaway**: it is a trial instrument, not the feature.

**One practical constraint that decides the file shape.** The page is opened by double-clicking it, with no server, so it cannot fetch a sibling JSON file — browsers refuse that for a local file. The passages therefore live in a block inside the HTML, exactly as `layout-preview.html` carries its `LAYOUTS` block.

**The residual, stated because the design does not remove it.** This is still reading aloud, just without look-ahead. It removes foreknowledge, which is the identified cause, and it does not turn reading into speaking.

**What the build changes.**
- `planning/dictation-prompter.html` — new. A `PASSAGES` block near the top holding at least three passages, each an array of phrases as authored. The page picks one, shows a single phrase very large and centred with nothing else on screen, and advances on the space bar or a click anywhere. Only the current phrase is ever visible. The end of a passage is announced, and starting again picks a passage other than the one just finished. It stores nothing and makes no network request.

Reads but does not change: `planning/layout-preview.html`, for the shape of an editable data block in a standing fixture.

**The observation that shows it landed:** opening `planning/dictation-prompter.html` by double-clicking shows exactly one phrase and no other passage text; pressing space replaces it with the next phrase and the previous one is no longer on screen; reaching the last phrase says the passage has ended; and starting again offers a different passage from the one just finished. Checked by opening the file, which needs no build and no device. **Whether the idea works is not this item's observation** — that is [prompter-elicits-natural-speech], which needs a person to speak into it.

**Options already refused, each with what defeated it.** One word at a time, as Spritz does — elicits its own word-by-word delivery, your correction of 2026-09-04. A forced advance rate, as Spreeder does — makes the speaker chase the prompt. Building the real thing into the app first — the enrolment session it belongs in does not exist and is not designable, so the trial would wait months to test a claim a page can test now. Detecting the end of a phrase automatically — needs a recogniser, which a page has none of, and it was the harder half. A separate JSON file of passages — a locally opened page cannot fetch one.

Rests on: `planning/` holding standing HTML fixtures with editable data blocks, read from that folder on 2026-09-05; that a locally opened page cannot fetch a sibling file, which is long-standing browser behaviour and is the reason for the embedded block.

[prompter-elicits-natural-speech] is the trial this exists for. [enrolment-prompter] is where the idea goes if the trial passes. Those orderings are written on all three entries.

#### Phase line and README status turn over once the keyboard switches on [status-lines-after-install]
Three sentences that stopped being true when Hexboard was installed as a working keyboard, in the two documents a newcomer reads first.

Filed by /rescan on 2026-09-02, held against the install that would falsify them, and **released on 2026-09-03** when that install put Hexboard on the Pixel 6 as a switched-on input method. Designed out on 2026-09-05.

**What is false, and since when.** `CLAUDE.md`'s phase paragraph — written on 2026-09-02 by [claude-md-phase-ran] and one build behind by the end of the run that wrote it — says there is no input method service yet, so Hexboard is an app rather than a keyboard. `README.md`'s Status section says there is no working keyboard and nothing to install. Both were true when written. The install of 2026-09-03 registered Hexboard as an input method and typed the keys it was aimed at, recorded in [install-and-enable-on-pixel].

**A third instance the item did not name, found on 2026-09-05.** README's "Try the prototype" section says `hexboard17.html` is the only part of Hexboard you can actually type on today. Same fact, same staleness, same file — and fixing two sentences while a third contradicts them would leave the document arguing with itself.

**One distinction to keep rather than smooth over.** "Nothing to install" is false in one sense and true in another: the project builds from source and runs on a phone, and there is no packaged release to download. A Status section saying there is a working keyboard, without that distinction, sends a reader looking for a download that does not exist. So Status says both things.

**The phase paragraph's closing instruction is reworded, not dropped.** It ends "Very little exists, so keep designing before coding rather than rushing new work into the app" — written when three Kotlin files existed, and now resting on a premise that has gone. The force behind it has not: the design record still runs deliberately ahead of the build, which is how this project works rather than an accident of how little exists. So it is rewritten as intent. Your call, taken on 2026-09-05.

**What the build changes.**
- `CLAUDE.md` — the phase paragraph in the project rules: Hexboard runs on a Pixel 6 as a registered input method and types what it is aimed at; the feature set is early; the design record deliberately runs ahead of the code, so new work is designed before it is built. No sentence claiming there is no input method service, and no sentence resting on how little exists.
- `README.md` — the Status section: it builds, installs and types, with predictive text, clipboard history and voice input all still unbuilt, and no packaged release to download. And the "Try the prototype" section: the browser prototype stops being described as the only part you can type on, and is described as what it is — the frozen reference for layout, gestures and key inventory, runnable with no build step.

**The observation that shows it landed:** a grep across `CLAUDE.md` and `README.md` for "no input method service", "no working keyboard", "nothing to install" and "the only part of Hexboard you can actually type on" returns nothing, and `README.md`'s Status section contains both a statement that it builds and runs and a statement that there is no release.

**Options already refused, each with what defeated it.** Fixing only the two sentences the item named — leaves the third contradicting them in the same file. Saying simply that there is a working keyboard — sends a reader looking for a download that does not exist. Dropping the design-before-coding instruction because its premise has gone — the premise has gone and the intent has not, and dropping it would quietly remove a working rule for a wording reason. Waiting until [verify-this-runs-build-on-device] proves the newest eleven items — the claims here rest on the install of 2026-09-03, which is already confirmed, not on anything built since.

Rests on: the install of 2026-09-03 registering Hexboard as an input method and typing correctly, recorded in [install-and-enable-on-pixel]; the three stale sentences, read from `CLAUDE.md` and `README.md` on 2026-09-05; that no packaged release exists, which follows from there being no release process anywhere in the project.

The phase line states the phase, and the phase changed at exactly this point, so it turns over once rather than at every run.

Sits alongside [remove-superseded-editor-page], which removes a different paragraph from the same README section. Neither depends on the other and both may run in either order; whichever runs second should re-read the section rather than assuming its shape. That is written on both entries.

#### Remove the layout editor page, whose reason for being kept was deleted [remove-superseded-editor-page]
`planning/hexboard-editor.html` and the README paragraph describing it are removed, since the future work they were held for no longer exists.

Found by /rescan on 2026-09-05 and processed with you the same day.

**It was kept deliberately, and deletion was specifically weighed and rejected.** On 2026-08-21 the page was moved from the repository root into `planning/` with `git mv`, and README gained a paragraph describing it. That session's record states plainly that deletion lost, on the ground that "its drag-and-drop interaction design is the expensive half of that future work". The future work it names is [variant-editor].

**[variant-editor] was deleted on 2026-09-02**, when you replaced the contributor-facing editor with layouts copied from each language's own standard. So the one recorded reason for keeping the page went with it, and nobody returned to the keeping decision. That is the same shape as two other things caught this session — a decision resting on a premise the project later removed — and the reason it is worth writing down rather than treating as tidying.

**What is actually at stake, since it is small either way.** This repository is public. A stranger browsing it finds a page titled "HexBoard Layout Editor" with a working drag-and-drop board and an Export button, and a README paragraph saying it is "kept as prior art for a future contributor-facing editor" — a future the project decided against. Against that, keeping the file costs nothing but a stale paragraph, and **git retains the drag-and-drop design either way**, so nothing is lost that anyone could want back.

**What the page actually is**, read on 2026-09-05: 557 lines, tracked in git, drawing the real zag geometry with the structural keys locked, dragging keys between slots, and exporting JavaScript fragments for pasting into the prototype — which is where key data lived before `resources/key-layout.json` existed. It cannot produce a config in any format the project now uses.

**What the build changes.**
- `planning/hexboard-editor.html` — deleted, with `git rm` so the removal is recorded as a move away rather than an untracked disappearance.
- `README.md` — the paragraph beginning "One more page is kept in the repo without being maintained" is removed entirely, along with its link. Nothing replaces it: the section's remaining content is about the browser prototype, which is unaffected.

Reads but does not change: `planning/layout-preview.html`, to confirm it is a different page and stays — it is the standing layout-preview fixture `CLAUDE.md` names, and nothing here touches it.

**The observation that shows it landed:** `planning/` contains `layout-preview.html` and `dictation-prompter.html` and no editor page; a grep across the repository for `hexboard-editor` returns only LOG entries and queue prose describing its history, and nothing in `README.md`; and `git log -- planning/hexboard-editor.html` still returns its history, which is what shows the design was archived rather than destroyed.

**Options already refused, each with what defeated it.** Keeping the file and rewriting the README paragraph to say it is prior art with no live successor — offered on 2026-09-05 and not taken: it leaves a public repository advertising a working editor for a cancelled feature, and the paragraph would then exist only to explain why the file exists. Leaving both as they are — README states a plan the project has abandoned, which is the same defect [status-lines-after-install] is fixing elsewhere in the same file. Deleting the file without touching README — leaves a dead link in a public document.

Rests on: the 2026-08-21 keep decision and its stated reason, read from `LOG/index-2026-08.md` and `LOG/2026-08-21-hexboard-editor-status.md` on 2026-09-05; [variant-editor]'s deletion on 2026-09-02, read from `LOG/2026-09-02-variant-editor.md`; the page's own content and its 557 lines, read on 2026-09-05; README's paragraph at the end of its prototype section, read the same day; that git retains a deleted file's history, which is what makes the removal reversible.

Sits alongside [status-lines-after-install], which edits a different part of the same README section. Neither depends on the other and both may run in either order; whichever runs second should re-read the section rather than assuming its shape. That is written on both entries.

#### Symbols panel's empty slots read as a gap when you swipe into it [symbols-panel-empty-slots]
The English symbols panel's ten empty positions are filled with ten characters Hexboard currently cannot type at all.

Raised by you on 2026-09-03, from the first swipe between panels on the real keyboard: the next panel does not run continuously from the last one. **Designed out on 2026-09-05.**

**What it actually is: the config's own empty slots, not a rendering fault.** The symbols panel is four rows of ten and carries thirty keys, so ten positions are empty — and they are all on the left, which is why swiping into the panel shows blank board where keys are expected. The pager was checked on 2026-09-03 and is not the cause: `HorizontalPager` in `KeyboardPanel.kt` sets no `pageSpacing`, so pages abut with nothing between them.

**A correction to this item's own arithmetic, made on 2026-09-05.** It previously said thirty keys and sixteen empty slots in the same paragraph, which cannot both be true of a forty-position panel, and it enumerated nine of the empties while missing row 2 column 5. Counted from the config: **ten**, at rows and columns (0,1) (0,3) (0,5) (1,2) (1,4) (1,6) (2,5) (3,0) (3,2) (3,4).

**Why this decision matters more than it looks.** The whole English inventory was checked on 2026-09-05: RARE is full at thirty of thirty, and QWERTY has no spare position either. **These ten are the only free slots in the English layout.** Whatever goes here is what Hexboard has room for, and the next character after that evicts something.

**Every printable ASCII character already has a home**, checked one by one on 2026-09-05 across the three panels. So the question is only what is missing beyond ASCII.

**The ten, agreed with you on 2026-09-05: “ ” ‘ ’ • ← → ½ ¢ ≈.** The four curly quotes because a keyboard that produces only straight quotes is one people work around every day, and they are the largest single gap; the bullet because there is no way to start a list; two arrows because the layout has none anywhere; and ½, ¢ and ≈ as the remaining most-wanted singles.

**No two empty slots are adjacent**, on any row — the gaps alternate with filled positions throughout. So the four quotes cannot sit side by side, and "grouped" here can only mean placed in the same region of the panel. They take the four earliest empty positions, (0,1) (0,3) (1,2) (1,4), which puts them in the top-left near `[` and `@` where the other typographic marks already are. The bullet follows at (1,6), ≈ at (2,5) among the operators, ¢ at (3,0), and the two arrows at (3,2) and (3,4) so the left arrow sits left of the right one, with `}` between them because nothing else is possible.

**Deliberately not included: ¿ and ¡.** They are the obvious missing punctuation, and they belong on the Spanish layout's own config rather than the English one — SPEC's manifest rules bind each layout individually, and [first-batch-layouts] adds Spanish.

**Deliberately not included: the Russian layout.** Its symbols panel has the identical ten gaps, found on 2026-09-05, but Russian uses « » as its primary quotation marks and „ " as its secondary, so four English curly quotes are probably the wrong fill for it. That is [russian-panel-gaps], which also carries three further empties on the Russian RARE panel.

**What the build changes.**
- `resources/key-layout.json` — ten keys added to the symbols panel at the positions above, each with its label and output, leaving every existing key exactly where it is.
- `resources/key-manifest.md` — regenerated by `scripts/generate-key-manifest.py`, never hand-edited.

Reads but does not change: `resources/key-layout-ru.json`, to confirm it is untouched by this item; `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`, to confirm no code change is needed — the panel draws whatever the config carries.

**The observation that shows it landed:** the English symbols panel has a key at every one of its forty positions and none elsewhere has moved; `resources/key-manifest.md` lists all ten new characters; and the existing key-audit test still passes, so no key was lost while ten were added.

**Options already refused, each with what defeated it.** Treating it as a rendering fault and adding page spacing — the pager was checked and is not the cause; the panel really is missing keys. Rearranging the panel so the gaps sit at the right-hand edge instead — SPEC's manifest rules say an empty slot is an opportunity rather than an acceptable gap, so moving the gap is not fixing it, and it would move every key a user had learned. Filling the slots with duplicates of `!` and `?` from QWERTY — SPEC bars unresolved duplicates, and neither is hard to reach. Including ¿ and ¡, or filling the Russian panel to match, both above.

Rests on: the symbols panel's thirty keys and ten empty positions, and the whole English inventory being ASCII-complete with RARE and QWERTY full, all counted from `resources/key-layout.json` on 2026-09-05; the Russian symbols panel having the same ten gaps, counted from `resources/key-layout-ru.json` the same day; `HorizontalPager` setting no `pageSpacing`, read from `KeyboardPanel.kt` on 2026-09-03; SPEC's rule that a freed slot is filled with a character agreed first, which is what the agreement above satisfies.

`resources/key-manifest.md` is the generated view of what is where, and is the place to read what the three panels carry.

#### Guard the emoji file's parse so a format change fails loudly [emoji-parse-guard]
A unit test over the bundled emoji list, so a future Unicode file that parses differently is caught by a failing test rather than by the panels quietly emptying.

Filed on 2026-09-05, split out of [emoji-data-refresh] when that item's recurring half became the project's first cycle. This is the standing guard rather than the recurring work: it runs on every build, not once a year.

**What it protects.** `EmojiCatalogue.parse` reads the codepoints before the first semicolon and keeps only entries whose status column says `fully-qualified`. Both are stable in the format as it stands. But a format change — a moved column, a renamed status, a different comment convention — would not fail: the parse would simply return few entries or none, and the symptom would be emoji panels arriving empty on a phone, long after the file was swapped.

**Why the assertions are derived rather than invented.** A test asserting "at least 3,781 emoji" would be a bare number that goes wrong the next time Unicode adds any, and one asserting an exact count would fail on every legitimate refresh. The three assertions below are each a proportion or a requirement the app already has:

- the parse yields **at least as many entries as the panels display** — five panels of fifty, a figure `EmojiCatalogue` already computes from its own `ROWS` and `COLS` — since fewer than that means the board cannot be filled;
- the parse finds **more than one group heading**, since the groups are what the whole-list browser is organised by and a format change would collapse them;
- the parsed count is **at least half the file's non-comment lines**, which is the assertion that actually catches a moved status column: a wrong column reads as an unrecognised status and drops nearly everything, taking the ratio to near zero, while the ordinary mix of fully-qualified and lesser-qualified entries stays well above half.

**Where it runs.** A plain unit test on the JVM, reading `resources/emoji-test.txt` from the repository the way `KeyLayoutValidationTest` already reads the shipped key config. `EmojiCatalogue.parse` is `internal`, so it is reachable from the test source set without anything being opened up for it.

**What the build changes.**
- `android/app/src/test/java/tech/flintcraft/hexboard/EmojiCatalogueTest.kt` — new, carrying the three assertions above.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/EmojiCatalogue.kt`, for `parse`, `ROWS` and `COLS`; `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt`, for how a unit test reaches a file in `resources/`; `resources/emoji-test.txt`, which is the file under test.

**The observation that shows it landed:** `EmojiCatalogueTest` passes against the bundled Unicode 16.0 file on all three assertions. It is a JVM test, so it runs without a device — but Gradle cannot run on this machine, so running it belongs to [verify-this-runs-build-on-device]'s sitting or the next one after it.

**Options already refused, each with what defeated it.** Asserting an exact emoji count — fails on every legitimate refresh, which trains people to edit the test rather than read it. Asserting a fixed minimum like 3,000 — a bare number with no derivation. Checking the file's header version instead — a header can be right while the body's format has moved, which is the case this exists for. Leaving the check as a step inside the cycle's turn — it would then run once a year, where a standing test runs on every build and catches a bad file the day it lands.

Rests on: `EmojiCatalogue.parse` filtering on `fully-qualified` and reading codepoints before the semicolon, and `ROWS` and `COLS` giving the panel capacity, all read from `EmojiCatalogue.kt` on 2026-09-05; `KeyLayoutValidationTest` existing as the precedent for a unit test reading a repository file, read from the test source set on 2026-09-05.

Step 4 of the `emoji-data-refresh` cycle in `CYCLES.md` runs this test as part of every turn. That ordering is written in both places.

#### Walkthrough steps quote on-screen text exactly, as a CLAUDE.md rule [walkthrough-steps-quote-screen-text]
One sentence added to `CLAUDE.md`'s project rules: where a walkthrough step asks the user to read something on screen, it quotes the exact text the app displays, and where that text varies it quotes each form — read from the source rather than recalled.

Filed by /rescan on 2026-09-04 at 15:30, from a step that misfired while driving [recogniser-gap-comparison] the same day. **Designed out on 2026-09-05.**

**What happened.** A step asked the user to report what "the availability line" said. That phrase came from the queue item's own wording and named nothing on his screen, so he asked what an availability line was. Reading `MainActivity.kt` showed the screen carries one line reading either "On-device recognition: available" or "On-device recognition: not available on this phone". Quoting those two strings in the first place would have cost nothing and the step would have worked.

**Why the source-reading half matters as much as the quoting half.** A quoted string composed from memory reads exactly like one read from the code, and is wrong in a way the reader cannot detect — the person following the step is the one person who cannot check the translation, because they cannot see the source. The failure here was cheap because he asked; a user who assumed the wording was approximate and reported the wrong line would have produced a wrong result nobody would have questioned.

**Why a rule rather than a correction.** `CLAUDE.md` already carries the sentence, added 2026-09-02, that a GUI step names something visible to click or a menu path with any shortcut as an aside. This is the same instinct failing at a third site: not where to go, but what the screen will say once you are there. A step that paraphrases on-screen text hands the reader a translation problem on top of the task.

**Not held against its sibling, and the reasoning is worth keeping.** [settings-steps-name-a-search] narrows the same paragraph of the same file, so building them separately means editing `CLAUDE.md` twice for one instinct — which was this item's own stated worry. But that item is held on an unverified fact, whether searching Android Settings reaches those screens on the Pixel 6, and that hold now sits behind [physical-keyboard-handover], which sits behind [verify-this-runs-build-on-device]. This rule rests on nothing unverified: quoting text read from the source is correct by construction. Holding a sound rule behind an unsound one to save a second small edit is the wrong trade, so the second edit is accepted. The sentence is worded generally enough that the Settings rule later reads as a narrowing of it rather than a third unrelated rule. That reasoning is written on both entries.

**What the build changes.**
- `CLAUDE.md` — one sentence in the project rules, beside the existing GUI-step sentence: where a walkthrough step asks the user to read something on screen, it quotes the exact text the app displays, quoting each form where the text varies, and that text is read from the source rather than recalled. The existing sentence stays as it is.

**The observation that shows it landed:** `CLAUDE.md`'s project rules contain a sentence naming both the quoting and the reading-from-source halves, and a grep for the existing GUI-step sentence about naming something visible to click still returns it, so the new rule sits beside it rather than replacing it.

**Options already refused, each with what defeated it.** Rewriting the existing GUI-step sentence to absorb this — that sentence governs where to go and this governs what the screen says; folding them makes one long rule that is harder to apply than two short ones. Correcting the one step that misfired and writing no rule — the instinct that produced it survives, and two walkthroughs had already inherited the same instinct before anyone noticed. Waiting to make one edit together with [settings-steps-name-a-search] — above. Requiring only the quoting and not the source-reading — a remembered quotation is the failure mode, not an unquoted paraphrase.

Rests on: `CLAUDE.md` carrying the 2026-09-02 GUI-step sentence in its project rules, read on 2026-09-05; the two exact strings in `MainActivity.kt`, read on 2026-09-04 when the user asked; [settings-steps-name-a-search]'s hold and the chain behind it, read from the queue on 2026-09-05.

This is arguably a gap in the method itself, whose own walkthrough rule says a step names the thing to click and the thing to look for without saying to quote the screen's words. This project reported a rule of the same shape to the method's own project on 2026-09-02. Nothing was sent about this one; the project rule is what makes it bind here, and sending a report remains open.

#### Dead border colour left on every key after the soft edge removed the border [dead-key-border-colour]
The `border` colour in `KeyColors` and the five values `colorsFor` supplies for it are removed, since nothing has read any of them since the key border went.

Filed by /rescan on 2026-09-04 at 15:30, from the run that built [soft-key-edge]. **Confirmed against the source and designed out on 2026-09-05.**

[soft-key-edge] removed the 1.5dp border from every key, because a border in a lighter colour than the fill was the hardest edge on the key and would have reinstated exactly the boundary the fade exists to dissolve. What it did not remove is the colour that fed it: `KeyColors` still declares the field and `colorsFor` still supplies a value for each of the five key kinds — `0xFF2C3358` for special, `0xFF0A6D44` for space, `0xFF404055` for punctuation, `0xFF2C2F44` for symbol and rare, and `0xFF484858` for the default — and nothing reads any of them. Checked by grep across the Kotlin on 2026-09-05.

**What the build must not also remove, found in that same grep.** There is one live `.border(1.5.dp, Color(0xFF3A3D5C), RoundedCornerShape(12.dp))` in `KeyboardPanel.kt`, and it is on the **accent popup's container** — the rounded panel that floats above the board while a key is held — not on a key. It is correct and stays: it separates a floating panel from the board behind it, where the no-border rule is about keys. Written down because whoever does this tidy-up greps for `border`, finds two things and is one careless moment from removing both.

**It is dead rather than reserved.** SPEC says a single dark theme is enough for v0 with theming later, so a spare colour field could plausibly be held for a future theme — except SPEC also now says a key is drawn as a soft-edged circle with no border, so no theme will want one.

**Why it is worth doing at all**, given nothing depends on it either way: this repository is public, and a colour named `border` on a keyboard that draws no borders reads to a stranger as a leftover rather than a decision — which is exactly what it is.

**The original reason for deferring has expired.** The item was filed rather than done because it was five lines in a file that run had already changed heavily and which cannot be compiled on this machine. That run is closed, and this now rides in the same run as the other work in `KeyboardPanel.kt`.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `KeyColors` loses its `border` property and its documentation comment loses the word; `colorsFor`'s five constructions each drop their middle argument. The accent popup's own `.border(...)` is untouched.

**The observation that shows it landed:** a grep for `border` across the Kotlin returns the accent popup's modifier and its import and nothing else — no field, no constructor argument, and no mention in a comment describing a key. The project compiles, which is what proves no construction site was missed, so that half belongs to the next Android Studio sitting.

**Options already refused, each with what defeated it.** Keeping the field for a future theme — SPEC says keys have no border, so no theme wants it. Removing the accent popup's border too — a different surface with a different reason, above. Leaving it as a harmless leftover — it is harmless and it is read by strangers, which is the whole of the reason to spend five lines on it.

Rests on: `KeyColors` declaring `border` and `colorsFor` supplying five values none of which is read, and the accent popup's own border being the only live one, both from a grep across the Kotlin on 2026-09-05; SPEC's no-border sentence and its single-dark-theme line, read from `SPEC.md` on 2026-09-05.

#### [user] Build and install this run's eleven items, and run the thirteen test files [verify-this-runs-build-on-device]
Filed by /rescan on 2026-09-04 at 15:30. The /next run of 2026-09-04 built eleven items and could compile none of them, and three other `[user]` items are now waiting on this without anything in the queue saying so.

**Why it exists.** Gradle cannot run on this machine and there is no `adb` here, both established by attempt and recorded in [run-key-config-validator]. So every item that run built ends in a check only Android Studio can perform on the Pixel 6, and until someone performs it the eleven are written rather than working. The eleven: [build-output-off-drive], [board-clear-of-navigation-bar], [suggestion-strip], [ship-all-layout-configs], [soft-key-edge], [row-tint], [panel-key-size-consistency], [rare-row2-unindent], [declare-savedstate-viewmodel-deps], [shift-behaviour] and [emoji-panels].

**Why it is filed rather than left in the conversation.** [verify-a11y-ondevice], [verify-switch-access-ondevice] and [physical-keyboard-handover] were each presented during that run and each deferred by you until today's build is on the phone — a decision made in a chat, about a thing no queue item named. Without this item those three sit in the queue apparently waiting on nothing, and the next session re-presents them as ready.

**What is new since the install of 2026-09-03, so the build is not assumed to be routine.** Two Gradle changes want a sync rather than a compile: `hexboard.buildDir` moves the whole build output to `C:\builds\hexboard`, and two AndroidX artifacts are newly declared. Six test files are new and did not exist at the last install.

Why it cannot be Claude's: it needs Gradle and a handset, neither reachable from here.

The walkthrough:
1. Open the Hexboard project in Android Studio and let it finish its Gradle sync. Look for: the sync completing without an error banner. Two things could fail here specifically — the `savedstate` version, and the build-output relocation — so if it fails, the message text is the thing to report rather than the fact of failing.
2. Check where the output went. Look for: `android/app/build/` empty or absent, and a new `C:\builds\hexboard` folder holding the build output. That is [build-output-off-drive]'s own observation and this is the only chance to make it.
3. Run the app on the Pixel 6 with the Run button. Look for: it installing and opening, and the keyboard drawing when you tap into a text field.
4. With the board up, check three of the six visible changes:
   - keys with no border, fading softly outward instead of ending at a hard edge;
   - the rows shaded in two alternating tones, close enough that neither looks pressed;
   - an empty band above the top row of keys.
5. Check the other three:
   - letters resting in lowercase and turning to capitals when you tap shift;
   - emoji panels arriving on a downward swipe;
   - RARE's bottom row starting at the left edge rather than indented by one key.
6. Judge the size of the keys and their labels, which is what [soft-edge-fraction-values] changed in this same build. Look for: whether a key now reads as big enough, and whether its letter is comfortable to read at a glance. This is the judgment that item was shipped blind to make, so an opinion either way is the result — "still too small" is as useful as "right now".
7. Run the unit tests: right-click `android/app/src/test/java/tech/flintcraft/hexboard` and choose Run. Look for: three new files among them — `KeyEdgeTest`, `RowTintTest`, `SharedRadiusTest` — and which of them pass.
8. Run the instrumented tests with the phone connected: right-click `android/app/src/androidTest/java/tech/flintcraft/hexboard` and choose Run. Look for: the new `NavigationBarInsetTest`, `StripLayoutTest`, `ShippedConfigsTest`, `ShiftStateUiTest` and `EmojiPanelsUiTest`, and which pass. `EmojiPanelsUiTest`'s last test is the one worth watching — it checks a key still types after a second pager was wrapped around the board.
9. Report: whether the sync succeeded, where the build output landed, which of the six visible changes you found, what you thought of the key and label size, which tests failed and with what message, and anything on screen that looked wrong whether or not a test caught it.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

[verify-a11y-ondevice], [verify-switch-access-ondevice] and [physical-keyboard-handover] are each held until this has run. Those orderings are written on all four entries.

**The label-size question is no longer a separate item.** [label-size-after-soft-edge] wanted the same look at the board and was merged into [soft-edge-fraction-values] on 2026-09-04, which changes the two fill fractions and ships in this same build. So this walkthrough asks whether the keys and their labels look right at the new values, rather than whether the labels look too small at the old ones. That ordering is written on both entries.

--- Cleared to run above this line ---

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Blocked by: [predictive-neighbour-table], [predictive-dictionary-bundle]
Autocorrect for Hexboard: when the user presses space, the word just finished is compared against the shipped dictionary and, if it is not already a word, replaced by the closest match.

Captured by you, sharpened on 2026-08-06, reshaped by your decisions of 2026-08-20 and 2026-09-01, and designed out on 2026-09-04. The full decision history is in the records under this slug; what follows is what a build needs.

**Rewritten on 2026-09-04, when you decided to design this now.** It had sat in Unprocessed saying it could not be designed because SPEC held predictive text until the first working keyboard existed. It does exist — Hexboard was installed on the Pixel 6 and typed what it was aimed at on 2026-09-03 — so the gate had been met and nobody had looked at the item in that light. Two foundations were split out ([predictive-neighbour-table], [predictive-dictionary-bundle]) and the saved-word store was split out as [predictive-saved-words]; this item is now the correction engine alone.

**Why the engine is built rather than borrowed, which is the whole argument.** Closeness is measured with the key geometry: substituting a key for one of its six neighbours is a near-miss and costs little, any other substitution is a real difference and costs a lot. That is a neighbour-weighted edit distance, and no general-purpose library can compute it, because none of them know which keys touch which. In a hexagonal tessellation every interior key sits the same distance from each of its six neighbours, so the confusion set is uniform in a way a rectangular keyboard's is not — horizontal neighbours there are closer than diagonal ones.

**Your reason for wanting it, which the design answers.** Your complaint about ordinary autocomplete is that getting the first letter wrong is far worse than getting a later one wrong, because a prefix lookup reads words forwards and so treats the first letter as certain. Correcting at the word boundary dissolves that: the whole word is in hand, so no letter is trusted more than any other and the first-letter case stops being special rather than being compensated for.

**Three guards, all in SPEC.** A word already in the dictionary exactly is never corrected, or the engine mangles deliberate spellings. A correction is undone by pressing backspace immediately after it lands, because silent unrevertable autocorrect is the most resented behaviour keyboards have. And nothing correctly spelled is ever changed on the user's behalf — your principle of 2026-09-03, raised from Gboard adding an apostrophe to every "its"; on this engine it was already true twice over, since the dictionary guard covers "its" and an apostrophe insertion is not a neighbour substitution.

**Ties are broken by the word list's own size level**, which is a coarse commonness ranking, and no second frequency source is bundled — settled with [predictive-dictionary-bundle] on 2026-09-04, where the reasoning and the rejected sources live.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/Autocorrect.kt` — new. The neighbour-weighted edit distance between a typed word and a candidate, and the choice of closest match with the size level breaking ties. Pure functions over a word, a neighbour table and the word list, so they can be checked without a running input method.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — accumulates the current word as keys are committed, fires the correction when a space arrives, replaces the committed text where a correction is made, and undoes it when the very next key is backspace. A word already in the list is committed untouched.
- `android/app/src/test/java/tech/flintcraft/hexboard/AutocorrectTest.kt` — new, carrying the first half of the observation below.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/AutocorrectUiTest.kt` — new, carrying the second half.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` for the neighbour table and `WordList.kt` for lookups, both built by this item's blockers.

**The observation that shows it landed:** `AutocorrectTest` passes, asserting that a word one neighbour-substitution from a dictionary word is corrected to it, that a word one non-neighbour substitution away is left alone, that a word already in the list is never changed, and that between two equally-near candidates the one at the commoner size level wins. `AutocorrectUiTest` types a near-miss followed by a space and asserts the corrected word is in the field, then presses backspace and asserts the original is back. Nothing on this machine can run either, so running them is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** Correcting per keystroke, searching forwards from the pressed key and its six neighbours and ranking by plausibility times frequency — this was the 2026-08-06 design and it lost to word-boundary correction on 2026-08-20, because it exists to work around reading a word before it is finished, which correcting at the boundary simply removes; it was also far more machinery, needing a per-keystroke candidate interface. Adopting an existing library — none knows the key geometry, which is the entire source of the advantage. Learning from what the user types — designed out on 2026-08-20 and now [predictive-saved-words]' business rather than this item's. Correcting grammar or punctuation — barred by SPEC.

**A reasoning this item is the home of, recorded because it is the standing defence of the board's routing.** You asked on 2026-09-02 whether the touch targets should be circles that kiss, leaving dead space between them. They must not. This engine repairs a *wrong* character cheaply, because the six-neighbour geometry says which letter was probably meant; it cannot repair a *missing* one, because a deletion carries no signal about which letter failed to arrive or where. Dead space would produce exactly the failure this engine is worst at in order to avoid the one it is best at. Full-coverage nearest-centre routing is what avoids it, and that reasoning now sits in `KeyGeometry.nearestCentre`'s own documentation as well.

Cites research: `workshop/resources/research/word-list-licence-and-frequency.md`.

Rests on: the neighbour table and the word list, both built by this item's two blockers rather than assumed; SPEC's three guards, read from `SPEC.md` on 2026-09-04; that this project's own predictive-text deferral has been met, established by the install of 2026-09-03 recorded in [install-and-enable-on-pixel].

[predictive-saved-words] is held against this item, being the store the engine would consult once it exists. [tap-word-alternatives] is a different mechanism and this engine cannot reach it: it would never touch "rose" for "rows", both being real words several keys apart. Those orderings are written on all three entries.

#### Tapping a finished word to see alternatives, which nothing here can do [tap-word-alternatives]
Blocked by: [predictive-dictionary-bundle], [verify-this-runs-build-on-device]
Tap a word already sitting in the text and the row above the keys offers replacements for it; tap one and it takes the word's place.

Captured by you on 2026-09-02, from your own phone: you tapped a mistyped word and Gboard offered the right one. The word was "rose" where you had meant "rows". Designed out on 2026-09-04 after the research this had been waiting on.

**Why the predictive engine cannot cover this**, which is why it is a separate feature rather than a corner of one. [uniform-neighbours-predictive] corrects on the space bar using a neighbour-weighted edit distance, and its first guard is that a word already in the dictionary is never corrected. "rose" is a real word, and it is two edits from "rows" rather than one neighbour substitution. So by the time a word sits finished in the text, either autocorrect already fixed it or it is exactly the kind of word geometry cannot reach.

**Where the alternatives come from, settled on 2026-09-04: homophones derived from the CMU Pronouncing Dictionary.** CMUdict maps words to phoneme sequences, so words with identical sequences are homophones by construction — a grouping rather than a curated list, which means there is no list to trust and none to license. "rose" and "rows" are both `R OW1 Z`. Its licence makes use for any research or commercial purpose completely unrestricted and asks that its origin be acknowledged where it is redistributed, which is the same shape as the Unicode and FlorisBoard notices `README.md` already carries.

**Two routes were closed by the same research, and both are the obvious thing to try again.** A language model over the sentence is unavailable: Android reaches one through Gemini Nano and the ML Kit GenAI APIs, whose supported-device list starts at the Pixel 9 — this project's handset is a Pixel 6 and SPEC's minimum is API 26, so it fails for the developer and for most of the audience at once, and bundling a model of our own is not a fallback at roughly two billion parameters. And `pimentel/homophones`, the curated list that is the first search hit, states no licence at all, so it cannot go into a public repository.

**How the keyboard knows which word was tapped**, checked rather than assumed on 2026-09-04: `onUpdateSelection()` fires when the user taps to move the cursor, and `InputConnection.getTextBeforeCursor` and `getTextAfterCursor` read the text either side of it, which is enough to recover the word the cursor landed in. Read from Android's reference documentation, not run.

**This is the offering side of your own SPEC principle**, added 2026-09-03 from Gboard adding an apostrophe to every "its": nothing correctly spelled is ever changed on the user's behalf, and offering an alternative the user can tap is permitted where applying one is not. Nothing here changes a word unless a candidate is tapped, and no word is marked as wrong.

**What the build changes.**
- `scripts/generate-homophones.py` — new. Reads a CMUdict release, groups entries by phoneme sequence, keeps only groups whose members all appear in the shipped word list, and writes the table with a header naming the CMUdict version it came from.
- `resources/homophones-en.txt` — new, generated by that script and committed alongside the key configs, the emoji data and the word list.
- `android/app/build.gradle.kts` — the existing copy task also takes the homophone table into the app's assets.
- `android/app/src/main/java/tech/flintcraft/hexboard/Homophones.kt` — new. Loads the table and answers one question: what are the alternatives for this word, if any.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — on a selection change, reads the word the cursor landed in, asks for its alternatives and publishes them; replaces the word when one is chosen.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the row above the keys draws the alternatives and reports which was tapped. The row's height is unchanged: this fills the space [suggestion-strip] shipped empty.
- `README.md` — the Notices section gains CMUdict's acknowledgment.
- `android/app/src/test/java/tech/flintcraft/hexboard/HomophonesTest.kt` — new, carrying the first half of the observation.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/TapWordAlternativesUiTest.kt` — new, carrying the second half.

Reads but does not change: `resources/wordlist-en.txt`, which [predictive-dictionary-bundle] ships and which the generation script filters against.

**The observation that shows it landed:** `HomophonesTest` passes, asserting that "rose" offers "rows", that a word with no homophone offers nothing, and that every word in the table appears in the shipped word list — which is the filter made checkable rather than asserted. `TapWordAlternativesUiTest` types a sentence containing a homophone, taps that word, asserts its alternative appears in the row above the keys, taps the alternative and asserts the text now reads with the replacement and nothing else changed. Nothing here can run either, so running them is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** A language model over the sentence — no on-device route below the Pixel 9. `pimentel/homophones` — no licence. Reusing the neighbour-weighted distance over the word list — autocorrect has already fired by the time a word is finished, so what is left is what geometry cannot reach. Wiktionary's pronunciation data — CC BY-SA, raising the same ShareAlike question `wordfreq` raised for frequencies. Highlighting a word the keyboard thinks is wrong — barred by SPEC, and it would make the feature something that acts rather than offers.

**What is deliberately not answered.** Whether homophones alone are enough: a user tapping a word may have meant something merely similar rather than something identical-sounding, and nothing measures how often. That is a question for after this is used, not a reason to hold it — the motivating case is a homophone.

Cites research: `workshop/resources/research/word-alternative-sources.md`, which carries the licences, the device list and what it does not settle.

Rests on: CMUdict's licence and its phoneme-sequence content, read from the cmusphinx/cmudict repository on 2026-09-04; the ML Kit GenAI supported-device list starting at the Pixel 9, read from Google's own documentation on 2026-09-04, which that file flags as amended on a cycle; `onUpdateSelection()` firing on a cursor tap and `InputConnection` reading the surrounding text, read from Android's reference on 2026-09-04 and not run; that the row above the keys exists and works, which [suggestion-strip] built on 2026-09-04 and nothing has yet compiled — which is why [verify-this-runs-build-on-device] is the second blocker.

[speech-output-correction] records what is known about how Gboard does the equivalent, and found that Google describes its proofreading as origin-blind — checking typed, pasted and dictated text alike — which is why this feature needs no record of what arrived by voice. [uniform-neighbours-predictive] is a different mechanism and cannot reach these cases. Those orderings are written on all three entries.

#### [user] Speak into the prompter and say whether it made you hesitate normally [prompter-elicits-natural-speech]
Blocked by: [rsvp-dictation-prompter]
A short sitting with the prompter page open, to find out whether phrase-at-a-time speaker-paced prompting actually elicits ordinary speech — the claim everything else about it rests on.

**Why it exists.** [rsvp-dictation-prompter] builds the page; nothing about building it shows whether the idea works. That question needs a person speaking and noticing how they sounded, which is the whole of what this item is. It is the reason the page is being built cheaply rather than as part of the enrolment flow it is really for.

Why it cannot be Claude's: it needs someone to speak aloud and report how their own speech felt, which is a judgment only the speaker can make.

The walkthrough:
1. Open `planning/dictation-prompter.html` by double-clicking it. Look for: one short phrase, large, in the middle of an otherwise empty page.
2. Get a text field ready on your phone with dictation running — whichever keyboard you like, since this is about the prompting rather than the recogniser. Look for: the microphone listening and words arriving when you speak.
3. Work through one whole passage, pressing space for each new phrase and speaking it. Look for: whether you hesitated the way you do in ordinary speech, or slipped into a reading-aloud voice.
4. Do a second passage, this time deliberately trying to go fast. Look for: whether hurrying makes you read rather than speak — which is what would tell us the pacing has to stay unhurried.
5. Report three things: whether you sounded like yourself talking or like yourself reading, how many corrections the dictation needed compared with a normal message, and anything about the page itself that got in the way.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

**What each answer would mean, written now so two impressions are not data nobody knows how to read.** If it produced ordinary hesitant speech, the design carries over to [enrolment-prompter] intact and the remaining questions there are about passage content rather than about the mechanism. If it produced reading-aloud speech anyway, the mechanism does not do what it was designed to do, and the honest response is to say so and stop rather than to add refinements — the whole point of a cheap trial is that it is allowed to fail. If it landed somewhere between, the thing to record is what made the difference, since that is what a later design would have to control for.

[rsvp-dictation-prompter] builds the page; [enrolment-prompter] is what this trial is protecting from being built on a wrong premise. Those orderings are written on all three entries.

#### Split the board into two halves on wider screens [split-layout-wide-screens]
Blocked by: [landscape-board-height]
The board splits into two halves with a gap between them, one under each thumb, whenever the screen is wider than it is tall.

Captured by you on 2026-09-02, immediately after choosing to keep the space bars at columns 4 and 6 in [row3-space-choice]. The two decisions belong together and the connection is the point: your reason for keeping today's arrangement is that in normal portrait handling the thumbs sit naturally near the middle of the screen, so the space keys do not need moving outward. On a wider screen that stops being true — the thumbs move to the edges and the middle becomes the part neither can reach. **Designed out on 2026-09-05.**

**Settled with you on 2026-09-02: the split is automatic, and the trigger is width exceeding height.** Not a setting, and not a device class — a phone turned to landscape gets it, a tablet in portrait does not. Gboard's split-as-a-toggle was named as the comparison and not chosen; your rule needs no menu and follows directly from the reason the feature exists, which is where the thumbs are. This is in SPEC.

**Three of the four open questions were desk answers after all, settled on 2026-09-05.**

- **The space bars need no rule of their own.** Ten columns split into 0–4 and 5–9 put the column-4 space bar in the left half and the column-6 one in the right. One each, forced by where they already sit. This was recorded on 2026-09-02 as the likely answer; it turns out to be the only answer the existing columns allow.
- **An odd column count splits with the extra column on the left.** The Russian board is eleven wide, so this case is live rather than hypothetical.
- **The gap is a proportion of the solved radius**, like the key gap and the strip height, so it stays in proportion at any width and on any board. A fixed dp gap was the alternative and loses on exactly the ground the strip height already settled: it drifts out of proportion on a board that is not ten wide.

**The fourth question dissolved rather than being answered.** A horizontal swipe crossing the gap was named as something gesture work would have to handle. It does not: [panel-switch-gestures] wraps the whole board in one pager, so a split is a change to where the keys are drawn inside a page rather than two pages side by side, and a swipe anywhere across the board still changes panel. Hit-testing is untouched for the same reason — `nearestCentre` runs over every key on the panel, so a tap landing in the gap resolves to the nearest centre on one side or the other and there is no dead space.

**What this does not do, and it matters for SPEC's inviolable geometry:** a split separates the halves, it does not rearrange keys within them or change the zag rule, the circle size, or nearest-centre routing. Each half keeps its own columns. So this is a question about where the board is drawn rather than about the key inventory, which puts it in the code that lays the panel out rather than in a layout config.

**Held against [landscape-board-height], and the reason is that this feature alone would not rescue landscape.** Found on 2026-09-05: the radius is solved from width alone, so a sideways Pixel 6 hits the 34dp cap and the board plus the row above it comes to roughly 394dp on a screen about 411dp tall. Splitting that board horizontally puts two halves under the thumbs on a keyboard that has swallowed the screen. The height bound has to land first for this to be worth having. That ordering is written on both entries.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` — a function giving each key's horizontal offset when the board is split: the columns below the split point shift left by half the gap, those at or above it shift right by half the gap, with the gap derived as a proportion of the solved radius and the split point being the midpoint with the extra column going left on an odd count.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the board applies that offset when the available width exceeds the available height, and applies nothing otherwise. The pager, the tap handling and the accent popups are untouched.
- `android/app/src/test/java/tech/flintcraft/hexboard/SplitLayoutTest.kt` — new, carrying the first half of the observation.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/SplitLayoutUiTest.kt` — new, carrying the second half.

Reads but does not change: `resources/key-layout.json` and `resources/key-layout-ru.json`, for a ten-wide and an eleven-wide board to test against.

**The observation that shows it landed:** `SplitLayoutTest` passes, asserting that on a ten-wide board columns 0–4 move left and 5–9 move right by equal amounts, that on the eleven-wide Russian board the left half takes six columns and the right five, that the two space bars end in different halves, and that the offsets are zero when the height is not exceeded by the width. `SplitLayoutUiTest` puts the device in landscape, asserts a gap appears between the halves, taps a key either side of it and asserts each types what it shows, then swipes horizontally and asserts the panel changes. Nothing here can run either, so running them is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** A split toggle in settings, as Gboard has — rejected on 2026-09-02; the rule needs no menu and follows from where the thumbs are. A device-class trigger — a phone in landscape wants it and a tablet in portrait does not, which is orientation rather than class. A fixed dp gap — drifts out of proportion on a board that is not ten wide. Two pagers, one per half — the pager is board-level and a swipe should cross the whole board; two would make a swipe mean different things on different sides. Rearranging keys within a half to bring them closer to the thumb — breaks the zag and the hexagonal packing SPEC calls inviolable.

Rests on: the space bars sitting at columns 4 and 6, settled in [row3-space-choice] and read from `resources/key-layout.json` on 2026-09-05; the pager being board-level rather than per-panel, built by [panel-switch-gestures] and read from `KeyboardPanel.kt` on 2026-09-05; `nearestCentre` running over every key on the panel, read from `KeyGeometry.kt` on 2026-09-05; that the Russian board is eleven columns wide, recorded in [language-starter-layouts]; the landscape height arithmetic recorded in [landscape-board-height], which is why this is held.

Interacts with [key-press-feedback] and [panel-switch-gestures], both of which have shipped and both of which edit the same panel code.

#### Voice input inside the keyboard, held open by the thumb [in-keyboard-voice-input]
Red flag · State: cleared
Blocked by: [verify-this-runs-build-on-device]
**The hold was repointed on 2026-09-04, and both of the old blockers are answered rather than dropped.** [recogniser-gap-comparison] was driven to its end that day: the platform's on-device recogniser and Gboard's produced basically identical transcripts, differing only in punctuation, so the premise this item was held against — that the recognition is good enough to build a thumb-held control around — is established. Its remaining formatting gap is [speech-output-correction], which now waits on this item rather than the other way round. [suggestion-strip] was built the same day and shipped the empty row this item's microphone sits in, but nothing has compiled it, so what stands in for it here is [verify-this-runs-build-on-device], the walkthrough that installs and checks that build. The one caveat from the comparison, recorded rather than buried: it was run on read passages, which the user established mid-drive is the easy case, so it says the two engines match and does not say either is good in ordinary hesitant speech.

**The strip left this item on 2026-09-02, later the same session.** This item no longer builds the row above the keys — [suggestion-strip] does, and this one adds the microphone to its right end. The reason is that [persistent-clipboard] turned out to need the same row for its own button and is held by nothing, while this item is held by a test on the phone; leaving the container here would have parked the clipboard behind a dictation comparison that has nothing to do with clipboards. The row's height derivation, its reserved overspill and the cost in key size moved to that item with it and are not repeated here.

Hold moved on 2026-09-02. [first-installable-build] shipped that day, and the design was completed the same session, so what remains is not a missing keyboard but an unmeasured recogniser: [recogniser-gap-comparison] establishes whether the platform's on-device recognition is good enough to build a thumb-held control around. That is a premise this whole item rests on rather than a detail, so it holds the build.

Captured by you on 2026-09-01 and designed with you the same session. Your requirement, and the reason the feature exists at all: people should not have to switch to another keyboard to dictate, because they switch away and never switch back.

**That requirement is satisfiable, and this was checked rather than assumed.** An input method can run recognition itself — `SpeechRecognizer.isOnDeviceRecognitionAvailable()` and `createOnDeviceSpeechRecognizer()` exist from API 31, with on-device recognition forced from API 33 — and it needs the `RECORD_AUDIO` permission. Two existing keyboards, WhisperInput and Transcribro, are built this way, so this is a trodden path rather than a hopeful one. Read from Android's documentation and those projects on 2026-09-01; nothing was run.

**The privacy risk, raised by Claude and settled with you in the same exchange, which is what clears the flag above.** A keyboard requesting microphone permission is on its face indistinguishable from a keyboard that listens to you, and this repository is public and will be read by people deciding whether to trust it. `RECORD_AUDIO` on an input method is exactly what a malicious keyboard would ask for. Four things answer that, and three of them are design rather than assurance:

- **Recognition is on-device and audio never leaves the phone.**
- **Where a device cannot recognise on-device, voice input is simply unavailable.** This is the conservative arm of a real fork and it costs something: the project's minimum is API 26, and the on-device recogniser starts at 31, so devices between those levels get no voice input at all. The alternative was falling back to the network recogniser, which would send audio off the device — rejected, because a keyboard that quietly ships your voice somewhere on older hardware makes every other claim here worthless.
- **The microphone is open only while the control is held down.** Your choice, from a straight comparison with tap-to-start-tap-to-stop. The reason it won: "the microphone is open only while you are holding the button" is a claim a stranger can verify by using the keyboard, rather than one they have to take on trust from an indicator and a timeout. The cost was named and accepted — press-and-hold is tiring for anything longer than a sentence and you cannot dictate while looking away, so comfortable long-form dictation is a later question, not this item's.
- **No audio is retained once the words are transcribed**, and the mic is visibly indicated while open.

**The control grows while held and shrinks when fully released.** Yours, on 2026-09-01, and it answers the objection to press-and-hold rather than restating it: the button is hard to keep hold of while you are moving about, and a thumb drifting slightly off it loses the message halfway through. Enlarging the control the moment it is held makes the target forgiving exactly when forgiveness is needed, and shrinking it back on release keeps the resting keyboard uncluttered. It is the project's own thesis — a generous touch target — applied to the one control where losing your grip costs a whole sentence rather than one character. What still needs settling at build time is what counts as "fully released", since the point of the growth is that small movements must not end the recording.

**Where the mic control lives, settled by you on 2026-09-02.** It sits in a row above the board — the strip where autocorrect suggestions go — at the right-hand end of that strip. So it is a control belonging to the board, not a key declared in the layout config: `resources/key-layout.json`, the manifest rules and every language's layout are untouched by it, which is what a per-language config should be untouched by.

Three alternatives were read out of the code and lost, recorded so none is re-proposed. A key in QWERTY's row 3 loses outright: columns 0–9 are all occupied (shift, `?`, `,`, `!`, space, `'`, space, `"`, `.`, `-`), so a mic key there evicts a punctuation key, which SPEC's manifest rules make a deliberate loss rather than a placement. A key on RARE or SYMBOLS, where row 3 does have gaps, defeats the feature's own purpose — a microphone you must swipe to reach is most of the way back to switching keyboards. And a long-press on an existing key spends no pixels but has no free hold to spend: holds already carry accent menus and backspace repeat.

**What the choice costs, stated rather than glossed.** `HexboardBoard` today sizes itself to exactly the tallest panel and draws nothing else, so the strip is new vertical space on a keyboard whose whole argument is larger keys. The strip is the standard place a phone keyboard puts this row, so the cost is the ordinary one every keyboard pays, but it is a cost and SPEC's geometry principle is what it is paid against.

**This item builds the strip, settled by you on 2026-09-02.** Nothing has built it, and the feature that would normally own it — predictive text — is deferred by SPEC until after the first working keyboard, so waiting for it means waiting behind something with no date. This item therefore adds the strip as a board-level row above the pager, carrying the mic control at its right end and nothing else; predictive text later fills the rest of it with candidates rather than creating it. The alternative was filing the strip as its own item and holding this one against it — rejected as a hop with nothing in it, the strip being layout with no design left once its height is chosen.

**How tall the strip is, settled with you on 2026-09-02.** The strip's drawn band is one row's vertical pitch — `KeyGeometry.verticalStep` of the same radius the board already solved for that panel — floored so it never falls below 48dp. Derived rather than fixed, so it stays in proportion on the eleven-wide Russian layout and on any screen width without a second rule, and so the mic can never end up a different size from the keys under it. A fixed dp value was the alternative and lost on exactly that: it drifts out of proportion on every board that is not ten wide.

**The mic is drawn larger than a key and sits proud of the strip**, your call in the same exchange — a stylistic overspill past the strip's edge rather than a control confined to the band. One structural consequence, which changes what the build does rather than how it looks: an input method's window is sized to its view and Compose clips to bounds, so nothing can be painted outside the keyboard's own rectangle. The view therefore reserves the overspill as real height at the top and leaves it empty except where the mic occupies it. So the strip's reserved height is one row's pitch plus the overspill, while its drawn band is the pitch alone.

**What counts as "fully released", settled by you on 2026-09-02: the finger leaving the screen, with no distance threshold.** Once the hold has begun that pointer owns the recording until it lifts, anywhere on the screen, whether or not it has slid off the control. Drift cannot cut a sentence short at any distance, which is what the growing control existed to prevent, and it removes the magic number rather than choosing a value for it. The one case needing its own answer is Android cancelling the pointer outright — a system gesture, the notification shade — where recording stops and whatever was transcribed is kept rather than discarded.

**A defeated alternative, recorded so it is not revived.** An accent picker — offering the English varieties a device supports, presented as accents rather than locales — was designed and then dropped by you on 2026-09-01. It reached only speakers who happen to match one of a short list organised by where a variety of English is spoken natively, and missed the case you actually raised: a second-language English speaker in a multicultural country, who matches no entry. Your verdict was that it compares poorly with training on the user's own voice, which is [personal-voice-model]. Recognition here therefore uses the language the phone is set to, with no accent setting of its own.

Cites research: `workshop/resources/research/android-voice-input-and-accents.md`, which carries the API levels, the API 26–30 gap, the recognisable English varieties and why that list cannot answer an accent complaint.

Two things this item inherits from work filed on 2026-09-02. [ondevice-recogniser-test] puts a temporary dictation screen and the `RECORD_AUDIO` permission into `MainActivity` to measure the recogniser early; **removing both belongs here**, since this item is what introduces the microphone properly, and a test screen carrying that permission must not survive into anything published. And [recogniser-gap-comparison] measures how the platform recogniser compares with Gboard's, which tells this item whether the recognition it gets is good enough to build the press-and-hold control around at all. That ordering is written on all three items.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — `HexboardBoard` gains the strip above the pager and adds its reserved height to the board's own; the mic is drawn at the strip's right-hand end as a circle larger than the board's key radius, standing proud of the strip's band; press-and-hold grows it, a lift anywhere on the screen ends the recording, and a cancelled pointer stops it while keeping what was transcribed.
- `android/app/src/main/java/tech/flintcraft/hexboard/VoiceInput.kt` — new. Wraps the on-device recogniser: the API 31 and `isOnDeviceRecognitionAvailable()` gate, one utterance per hold, no network fallback, no audio retained. Where the gate fails the mic is absent rather than degraded.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — commits the recognised words into the field the keyboard is attached to, and routes the runtime `RECORD_AUDIO` request.
- `android/app/src/main/AndroidManifest.xml` — the `RECORD_AUDIO` permission's TEMPORARY marker removed; the permission becomes permanent and is the microphone's own rather than the test screen's.
- `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt` — the temporary dictation screen from [ondevice-recogniser-test] removed, availability line and Dictate button with it.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/VoiceControlUiTest.kt` — new. Carries the observation below.

Reads but does not change: `resources/key-layout.json` (to confirm the mic is not in it), `android/app/src/main/java/tech/flintcraft/hexboard/KeyGeometry.kt` (for `verticalStep` and the solved radius).

**The observation that shows it landed:** the instrumented test asserts the strip is present above the keys, the mic's accessibility node sits at its right-hand end with a bounds at least 48dp square and larger than a key's, `MainActivity` no longer exposes a Dictate button, and a hold-then-lift on the mic opens and closes exactly one recognition session. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** A mic key in QWERTY's row 3 — no free column, so it evicts punctuation. A mic key on RARE or SYMBOLS — a microphone you must swipe to reach is most of the way back to switching keyboards. A long-press on an existing key — no free hold, accents and repeat already hold them. A fixed dp strip height — drifts out of proportion on any board that is not ten wide. A distance threshold for release — a magic number where none is needed. Filing the strip as its own item — a hop with nothing in it. The accent picker and the network fallback, both recorded above.

Rests on: the on-device recogniser's API levels and the `RECORD_AUDIO` requirement, read from Android's documentation on 2026-09-01; that on-device recognition genuinely keeps audio on the device, which is what the API claims and what a build should confirm before the README repeats it; Android's 48dp minimum touch target, read from Google's own accessibility guidance on 2026-09-02, with 24dp the WCAG 2.5.8 level-AA floor; that an input method's window is sized to its view and Compose clips to bounds, so an overspill must be reserved as real height — reasoned from how the board is drawn today, not run; that the platform recogniser is good enough to build a press-and-hold control around, which is unverified and is exactly what [recogniser-gap-comparison] measures.

[uniform-neighbours-predictive] inherits the strip rather than creating it: predictive text fills the row this item introduces. That ordering is written on both items.

#### Ask the recogniser to punctuate what it hears, rather than correcting it afterwards [speech-output-correction]
Blocked by: [in-keyboard-voice-input]
Dictated text arrives with punctuation and capitalisation, because the recognition request asks the platform for them. There is no correction stage on the finished transcript at all.

Captured by you on 2026-09-02 as "correcting what the speech recogniser returns", separate from the keyboard's own autocorrect by your instruction, with the target being dictation as good as Gboard's. **Rewritten on 2026-09-04**, when the measurement it was waiting on came back and turned a large open question into a flag on a request.

**What the comparison found, and what it removed from this item.** [recogniser-gap-comparison] was driven on 2026-09-04. The transcripts through Gboard and through this project's own on-device recogniser were basically identical, and the single difference was that **Gboard adds punctuation**. So the recognition-quality half of this item has nothing in it: there is no accuracy gap to close, and the whole of what is left is formatting. The item had been written expecting the opposite and carried three routes for closing a gap; two of them are gone with the gap.

**The remaining question was whether punctuation is something a finished transcript can recover, and it turns out not to be the question at all.** Android's speech recogniser takes `EXTRA_ENABLE_FORMATTING` from API 33, with `FORMATTING_OPTIMIZE_QUALITY` and `FORMATTING_OPTIMIZE_LATENCY` to choose between, and `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION` to stop punctuation appearing after the last word of a partial result. So the punctuation happens inside recognition, where the research said it has to — `workshop/resources/research/gboard-speech-correction.md` established that Gboard has no separate autocorrect stage either, correction happening inside an all-neural recogniser. This project asks for the same thing through a documented flag rather than rebuilding it.

**Quality rather than latency, chosen on 2026-09-04.** [in-keyboard-voice-input] makes the microphone press-and-hold with one utterance per hold, so the text lands when the thumb lifts rather than streaming in word by word. Nobody is watching for the next word to appear, which is what latency optimisation buys. If partial results are ever shown while the hold is in progress, `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION` is what stops a comma flickering at the end of every partial.

**The cost, stated rather than glossed.** These extras are API 33. On-device recognition itself starts at API 31, so voice input is already unavailable below that; this adds a narrower band — devices on API 31 and 32 get dictation with no punctuation. The keyboard does not fail there and does not fall back to anything: it simply does not ask for formatting, and the words arrive unpunctuated. That is a smaller gap than the API 26–30 one already accepted for voice input itself, and it is accepted on the same reasoning.

**Why SPEC's correctly-spelled principle is not in tension with this**, which is worth stating because it looks as though it should be. That principle bars reaching into a finished word the user spelled — an apostrophe added to "its" is the case it came from. Asking the recogniser to punctuate is not that: nothing the user typed is touched, and the punctuation is part of what recognition produces rather than an edit applied to it afterwards. The principle's own wording already anticipated this, permitting punctuation and capitalisation of a *dictated* passage as something the recogniser did not supply rather than a correction of anything typed.

**The storage tension this item recorded has gone, and the reason should not be lost.** The item held that matching Gboard might mean keeping a per-user record of what someone says and how they fix it, which is the class of storage this project refuses everywhere else. Nothing in this design stores anything: a flag on a request holds no transcript. What would have needed storage is biasing recognition toward the user's own vocabulary, and that is split out as [dictation-biasing-saved-words] — where it turns out not to need a transcript either, since Android takes an explicit list of strings.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/VoiceInput.kt` — the recognition request sets `EXTRA_ENABLE_FORMATTING` to `FORMATTING_OPTIMIZE_QUALITY` on API 33 and above, and `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION` wherever partial results are shown. Below API 33 neither is set and the request is otherwise unchanged.
- `android/app/src/test/java/tech/flintcraft/hexboard/VoiceFormattingTest.kt` — new, carrying the observation below.

Reads but does not change: `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt`, to confirm the committed text needs no post-processing once the recogniser is formatting.

**The observation that shows it landed:** `VoiceFormattingTest` passes, asserting that a request built for API 33 or above carries the formatting extra set to the quality value, and that a request built for a lower API level carries neither formatting extra — so the gate is checkable rather than asserted. Whether the punctuation is actually any good is a judgment on the phone, and belongs to whoever next dictates through the keyboard rather than to a test.

**Options already refused, each with what defeated it.** A corrector applied to the finished transcript — the audio and every rejected alternative are gone by then, so it can tidy output but cannot close a recognition gap; and there is no gap to close. Correcting the transcript against the saved-word list — biasing does the same job inside recognition, where it is worth more. Optimising formatting for latency — nobody is watching partial text under a press-and-hold microphone. Adding punctuation ourselves with rules over the finished text — that is the corrector above wearing a smaller hat, and it would reach into text the recogniser produced without knowing where the speaker paused.

Cites research: `workshop/resources/research/gboard-speech-correction.md`, which states plainly that its architecture papers are from 2019 and 2020 — sound on where correction happens, not a description of what Gboard ships today.

Rests on: `EXTRA_ENABLE_FORMATTING`, `EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION`, `FORMATTING_OPTIMIZE_QUALITY` and `FORMATTING_OPTIMIZE_LATENCY` all arriving in API 33, read from Android's own API 33 difference report on 2026-09-04 and not run; the two recognisers being equivalent but for punctuation, established by the drive of [recogniser-gap-comparison] on 2026-09-04 — on read passages, which that drive itself established is the easy case; on-device recognition starting at API 31, read from Android's documentation on 2026-09-01.

Held against [in-keyboard-voice-input], which creates the recognition request this item configures — there is nothing to set a flag on until that exists. That ordering is written on both entries.

**An observation of yours from 2026-09-02 that this item no longer carries.** You noticed "rose" where you had meant "rows" in dictated text, tapped it, and were offered "rows" — and asked whether Gboard tags which words arrived by voice. The lookup found Google describing its proofreading as checking typed, pasted and dictated text alike, so an origin-blind proofreader explains it with no provenance tracking at all; nothing found describes Gboard tagging words by origin, recorded as *not found* rather than established absent, on your own caution that the observation is ambiguous between the two explanations. That affordance is [tap-word-alternatives], which was designed out on 2026-09-04 around homophones from CMUdict and needs no record of what was dictated. That ordering is written on both entries.

#### Clipboard history that persists, with a screen and drag-to-bin deletion [persistent-clipboard]
Red flag · State: cleared
Blocked by: [verify-this-runs-build-on-device]
**The hold was repointed on 2026-09-04.** [suggestion-strip] built the row this item's clipboard button lives in on 2026-09-04, so it is no longer in the queue — but nothing has compiled it, and this item's whole route in depends on that row working. What stands in for it is [verify-this-runs-build-on-device], the walkthrough that installs and checks that build.

Captured by you on 2026-09-01, and designed with you in the same session. Your complaint is with Gboard: its clipboard clears, and losing something you copied twenty minutes ago is the annoyance this removes.

**The retention rule, in your words and then in mine.** You asked for "the last 20 items or hour of content" — more than twenty items under an hour old is fine, and at over an hour old everything past twenty falls off. Restated as the rule a build implements: a clip is kept if it is younger than an hour, or if it is among the twenty most recent; it is dropped only when both fail. So fifty clips copied in one hour are all kept while that hour lasts, and once they have all aged past it, the newest twenty remain.

**The screen and the deletion gesture, also yours.** A clipboard screen in the manner of Gboard's. An item is deleted by holding it and dragging it to a bin that appears while the drag is in progress.

**No clear-all, and the reason is a threat model rather than a simplification.** Your reason: a cleared clipboard is itself suspicious — someone looking over your shoulder wonders why it is empty. Per-item deletion removes what you want gone while the rest still looks ordinary, and the retention rule handles the bulk case by itself. This is a defeated alternative worth recording, because a clear-all button is the obvious thing to add and looks like an omission rather than a decision.

**The privacy risk, raised by Claude at the moment the idea landed and settled with you in the same exchange, which is what clears the flag above.** A clipboard history is a file on the phone holding the last hour of everything copied, and what passes through a clipboard includes passwords, one-time codes and card numbers. Persisting it is a real increase in exposure over a clipboard that clears. You were told that plainly, and the design below is what you chose rather than dropping the feature.

The protections, and what each is worth:
- **Clips from a password field are kept five minutes, not an hour.** Password fields are detectable: an input method receives an `EditorInfo` when it attaches to a field, and its `inputType` carries the password variations — this is how keyboards suppress learning in password boxes. Verified against Android's input-method documentation on 2026-09-01.
- **Clips the source app marks sensitive get the same five minutes.** Android's flag is `ClipDescription.EXTRA_IS_SENSITIVE` from API 33, with the string constant `"android.content.extra.IS_SENSITIVE"` usable below it — which matters, since this project's minimum is API 26. Its documented worth is limited and the item must not overstate it: Android states the flag adds no security and is a rendering hint the *source application* chooses to set, so it protects only where that app bothered.
- **The store is encrypted at rest and never leaves the device.**
- **The five-minute rule is stated in the app where the user can read it**, because Android's own guidance for input methods is that a password is not saved anywhere without explicitly informing the user.

**Two limits that are stated rather than solved.** A clip copied while the keyboard is not attached to the field cannot be classified, so it gets the ordinary hour-and-twenty rule — the common case is covered, since copying usually happens through the app's selection toolbar while the keyboard is up, but the gap is real. And none of this makes a persistent clipboard safe; it makes it safer, and it remains a larger target than one that clears. That is the trade you accepted knowingly.

**A defeated alternative, recorded so it is not re-proposed.** Claude first proposed refusing to store anything copied from a password field at all. You replaced it with the five-minute expiry, and the reason it lost is that exclusion breaks the case people actually hit — you deliberately copy a password to paste it, and it is not there. Five minutes is long enough to paste and short enough not to sit in an hour of history.

**How the clipboard screen is reached, settled by you on 2026-09-02: a button at the left end of the strip above the keys.** That strip is introduced by [in-keyboard-voice-input], which puts the microphone at its right end; the clipboard takes the left. The reason this route wins is that it spends nothing — no gesture, no key slot, and no vertical space beyond what the strip already costs — where every other route spends something. It is also where Gboard puts its own clipboard entry, so it is the familiar place to look.

Two routes were ruled out the same day. Reaching it by a swipe is not available: horizontal swipe already moves between the three letter panels, which [panel-switch-gestures] shipped that morning as a pager. And reaching it the way emoji are reached does not exist — a grep of the Kotlin on 2026-09-02 found no vertical swipe and no emoji panel anywhere, so SPEC's promise of five emoji panels is unimplemented and cannot be built on.

**What the clipboard screen replaces, settled by you on 2026-09-02: the board area only, with the strip staying put.** The row above the keys does not move or change height; the panels beneath it are swapped for the clip list, and the clipboard button at the strip's left end is a toggle — tap to open, tap again to return to the keys.

Three reasons, recorded because the alternatives look reasonable. The keyboard's total height never changes, so nothing under the thumb jumps when the list opens or closes, where a full-screen or taller panel would shift the very text field being pasted into. The way back is the control you came in by, so there is nothing new to learn. And it keeps the clipboard inside the keyboard rather than sending the user to an app screen, which would mean leaving the text field — the failure this whole project designs against. An app-screen clipboard and a taller overlay are the two defeated alternatives.

The cost, accepted knowingly: the board area is four rows tall, so three or four clips are visible at a time and the rest are reached by scrolling. Gboard makes the same trade and it is not avoidable without changing the keyboard's height.

The original hold, [first-installable-build], shipped on 2026-09-02, and the reasons this sat in Unprocessed have gone with it: the panel structure is settled, and the screen now has a home. What it waits on instead is [suggestion-strip], the row its button lives in.

**Tapping a clip pastes it and returns to the keys**, settled by you on 2026-09-02 — one action for the thing the list was opened for, where a second tap on the toggle would make the common case two gestures. A tap and a hold therefore do different things and neither can be mistaken for the other. What this rules out, recorded rather than left implicit: there is no way to select a clip without pasting it, and no multi-paste. Neither was wanted — the retention rule already handles keeping things.

**The bin appears across the bottom of the board area while a drag lasts**, settled by you the same day, and vanishes when the drag ends. The bottom edge because that is where a thumb naturally pulls something it wants rid of, and because the list scrolls vertically, so a bin at the top would sit where the next clip arrives from. Releasing anywhere other than on the bin returns the clip to the list, making an aborted drag the default rather than something to aim for. A permanently visible bin was the alternative and lost twice over: it spends space in an area already showing only three or four clips, and it can receive an accidental tap that a drag-only target cannot.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/ClipboardStore.kt` — new. Holds the history and the retention rule (kept if under an hour old or among the twenty most recent, dropped only when both fail; five minutes instead for password-field and source-marked-sensitive clips), encrypted at rest, with read and per-item delete. Nothing leaves the device.
- `android/app/src/main/java/tech/flintcraft/hexboard/ClipboardScreen.kt` — new. The scrolling clip list drawn into the board area, tap-to-paste-and-return, hold-and-drag with the bin across the bottom, and the sentence stating the five-minute rule where the user can read it.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the clipboard button at the left end of the row [suggestion-strip] builds, and the swap of the board area between the panels and the clip list. The row itself is untouched: this item adds a control to it and does not change its height.
- `android/app/src/main/java/tech/flintcraft/hexboard/HexboardImeService.kt` — watches the clipboard, classifies each clip from the attached field's `EditorInfo.inputType` and the clip's own sensitive flag, and commits a pasted clip into the field.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/ClipboardUiTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** the instrumented test asserts that the strip's left-end button swaps the board area for the clip list and back without the keyboard's total height changing; that a tap on a clip commits its text and returns to the keys; that a hold shows the bin across the bottom, a release on it removes that clip and a release elsewhere does not; and that a clip stored from a password-typed field is gone after five minutes while an ordinary one is not. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

Held below the line against [suggestion-strip], which builds the row this item's button lives in. That ordering is written on both items.

Rests on: `EditorInfo.inputType` exposing password variations to an input method, and `ClipDescription.EXTRA_IS_SENSITIVE` existing from API 33 with a usable string constant below it — both read from Android's documentation on 2026-09-01, neither run; that an encrypted-at-rest store is available to an input method without further permission, which is assumed rather than checked and should be confirmed at the start of the build.

#### Reach the whole emoji list, not just the 250 the panels hold [emoji-panel-reach]
Blocked by: [persistent-clipboard]
One of the emoji panels' 250 slots becomes a control that swaps the board area for a scrolling grid of every emoji Unicode publishes, in its own groups; tapping one types it and returns to the keys.

Filed by the build of [emoji-panels] on 2026-09-04 at 12:44, which found the reach and could not write the SPEC sentence a build may not write. **Designed out on 2026-09-05, and SPEC's sentence written the same day.**

**What the build found.** `resources/emoji-test.txt` at Unicode 16.0 carries 3,781 fully-qualified emoji. The board has five panels of fifty slots — ten columns by five rows, the prototype's arrangement — so 250 are reachable and the remaining 3,531 sit in the bundled file and on no panel.

**Why that was not a defect in the build.** The item said to slice the parsed list into five panels in the file's own order and named the prototype as the reference for the arrangement. The prototype fills 250 slots from the front of the list, centre panel first and the outer two reversed, so that moving outward from home in either direction moves further down the list. The build did exactly that, and CLDR order is roughly commonest-first, so the 250 shown are the ones most likely to be wanted.

**Why it is still worth fixing.** A keyboard offering a fifteenth of the emoji that exist is a limitation a user meets the first time they want a flag, a less common animal or a food that is not in the top 250 — and today there is no way to type it at all.

**One of the three feared costs turned out not to exist.** This item was filed saying scrolling panels would break the fixed-height promise the clipboard screen was designed around. They would not: [persistent-clipboard] settled precisely this shape on 2026-09-02 — the board area is replaced by a scrolling list while the keyboard's total height never changes, and the same control returns to the keys. That is a browser inside a fixed-height board rather than a scrolling panel, and the mechanism this needs was already designed for another feature.

**So the answer costs one emoji slot.** The five panels stay exactly as they are — 250 commonest, no scrolling, fixed positions a user can learn — and one of those slots carries a control that opens the browser. The two alternatives this item feared both cost more: more than five panels spends a swipe, and a search field spends space in the row above the keys, which already has the microphone at its right end, the clipboard button at its left and suggestions in the middle.

**Nothing here is governed by the manifest rules**, which is worth stating because it looks as though it should be. SPEC puts the emoji panels outside the manifest's scope deliberately: their content comes from Unicode's published list and display order rather than an inventory curated here, so the four rules — no key lost, no unresolved duplicates, no silent changes, empty slots filled — do not bind them, and spending one slot on a control is not an eviction under any rule.

**Held against [persistent-clipboard], and it is a real hold rather than tidiness.** That item builds the swap of the board area for a scrolling view, and this reuses it. Two independent implementations of the same swap is how a keyboard ends up with two slightly different ways of covering its own keys, which a user notices and cannot name.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/EmojiCatalogue.kt` — exposes the whole parsed list with Unicode's own group and subgroup for each entry, rather than only the 250 sliced into panels.
- `android/app/src/main/java/tech/flintcraft/hexboard/EmojiBrowser.kt` — new. The scrolling grid drawn into the board area, grouped by Unicode's groups with their names as headings, reusing the board-area swap [persistent-clipboard] builds. A tap types the emoji and returns to the keys.
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — one slot on an emoji panel draws the control instead of an emoji, and opens the browser.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/EmojiBrowserUiTest.kt` — new, carrying the observation below.

Reads but does not change: `resources/emoji-test.txt`, for the group structure the browser headings come from.

**The observation that shows it landed:** the instrumented test asserts that the control opens the browser without the keyboard's total height changing, that the browser can be scrolled to an emoji outside the first 250 and tapping it commits that character and returns to the keys, and that the five panels still carry 249 emoji plus the control. Nothing on this machine can see the keyboard, so the test is the check; running it is Android Studio's, on the Pixel 6.

**Options already refused, each with what defeated it.** More than five panels — spends a swipe, on a board whose horizontal swipe already means "change letter panel" and whose vertical swipe already means "emoji". A search field in the row above the keys — that row has three claimants already. Scrolling the panels themselves — would change where a learned emoji sits, which is the thing fixed panels buy. Shipping only 250 and saying so in SPEC — considered on 2026-09-05 and rejected: a user who wants the 251st has no route at all, and the browser costs one slot.

Rests on: 3,781 fully-qualified emoji at Unicode 16.0 and the five-panel arrangement, both established by the build of [emoji-panels] on 2026-09-04; the board-area swap keeping the keyboard's height constant, designed in [persistent-clipboard] on 2026-09-02 and not yet built; SPEC placing the emoji panels outside the manifest rules, read from `SPEC.md` on 2026-09-05; that `emoji-test.txt` carries group and subgroup structure, read by the [emoji-panels] build on 2026-09-04.

Interacts with the `emoji-data-refresh` cycle in `CYCLES.md`, which replaces the bundled file when Unicode ships a version — a browser over the whole list makes a stale file more visible rather than less, since every emoji is now reachable. That connection is written in both places.

#### Accent row on the top row overlaps the neighbouring keys [accent-row-top-row]
Blocked by: [verify-this-runs-build-on-device]
The accent row for a top-row key draws into the strip above the keys instead of over its own row's neighbours.

**The hold was repointed on 2026-09-04.** [suggestion-strip] built the row this clamps into on 2026-09-04 and has left the queue, but nothing has compiled it. [verify-this-runs-build-on-device] is the walkthrough that installs and checks that build, and it stands in here.

**Settled with you on 2026-09-02, and the fix arrived the same session.** The accent row is drawn inside the board's bounds and clamped to the top edge, which is what `hexboard17.html` does — but the prototype had a bar above its keys to clamp *into*, and the board had nothing above row 0. [suggestion-strip], designed earlier in this session, restores exactly that bar: a row one vertical step tall, which is slightly more than a key's diameter, so an accent row for a top-row key fits in it. This is not a workaround; it is the arrangement the prototype's clamping was written against.

**The cost, stated rather than discovered.** While a top-row key is held, its accent row covers the suggestion row and the microphone. That is transient and nobody reads suggestions mid-hold, but it is a real overlap and worth seeing on the phone.

**Three options refused, each with what defeated it.** Drawing the row below the key on the top row — accents would appear above on three rows and below on one, and the overlap merely moves onto row 1. Drawing it in a window that may extend above the input view — the genuinely general answer, and what commercial keyboards do, but it is a second window with its own lifecycle and dismissal, which is a great deal of machinery for a case the strip already answers; worth revisiting only if the strip is ever removed. Shrinking the row — degrades the feature on every row to fix it on one.

**What the build changes.**
- `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt` — the accent row's clamp takes the strip's reserved height as its ceiling rather than the board's top edge, so a top-row key's alternatives rise into the strip.
- `android/app/src/androidTest/java/tech/flintcraft/hexboard/AccentRowTopRowTest.kt` — new, carrying the observation below.

**The observation that shows it landed:** the instrumented test holds a top-row key and asserts that the accent row's bounds sit entirely above the top key row — overlapping the strip rather than any key — and that holding a key on a lower row is unchanged.

Held below the line against [suggestion-strip], which builds the row this clamps into. That ordering is written on both items.

Rests on: the accent row's current clamping, read from `KeyboardPanel.kt` on 2026-09-02, not run; that one vertical step of strip height is enough to hold an accent row, which follows from the arithmetic and has not been seen.

Filed by /rescan on 2026-09-02 from the run that built [long-press-accent-popup]. The row is drawn by the board, inside the board's bounds, clamped to the top edge as the prototype clamps it — but the prototype had a bar above its keys to clamp into, and the board has nothing above row 0. So holding Е or E on the top row draws the alternatives over the neighbouring keys of that same row. Options not yet weighed: draw the row below the key on the top row, draw it in a window that may extend above the input view, or shrink the row. Seen only in reasoning; the first run on the Pixel 6 will show how bad it looks.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Blocked by: [verify-this-runs-build-on-device]
**Held again on 2026-09-04.** You deferred this during the build run of 2026-09-04 because that run's work is not on the phone, and nothing in the queue said so — so it kept being offered as ready. It now waits on [verify-this-runs-build-on-device], which installs that build. That ordering is written on both entries.

Captured by you. A check that Hexboard's keys behave properly for someone using a screen reader or switch access, run on the phone with those services turned on.

Two things to confirm: that nearest-centre routing still selects the key you aimed at while an accessibility service is intercepting touches, and that each key exposes an accessibility node with the right label and bounds. SPEC treats these as separate requirements, because accessibility services largely bypass raw-touch routing — so both must be correct and one passing says nothing about the other.

**Cleared to run on 2026-09-03**, when the install run put Hexboard on the Pixel 6 as a switched-on keyboard — the lift-condition this item had carried since 2026-08-20, TalkBack being untestable against a keyboard nobody has switched on. [first-installable-build] had been dropped from the hold on 2026-09-02, having shipped that day.

Why it cannot be Claude's: it needs accessibility services running on a real handset, and there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator].

**The walkthrough was written on 2026-09-03**, this item having had none — it said only "confirm two things with accessibility services active" and never said how to switch them on. It reaches Settings by searching rather than by a menu path, for the reason recorded in [physical-keyboard-handover]: the path this project wrote down for the install did not match the handset, and Android moves these screens between versions. Whether the search works on this handset is itself unverified, so step 1 keeps Accessibility as its stated fallback and says which route it establishes. [settings-steps-name-a-search] waits on that answer, from whichever of this item and [physical-keyboard-handover] you run first.

The walkthrough:
1. Open Settings and type `talkback` into its search box. Look for: a result that opens a screen with a TalkBack on/off switch — turn it on. If the search finds nothing useful, TalkBack lives under Accessibility. Nobody has run this search on the Pixel 6, so which of the two works is the first thing this step establishes. TalkBack will start speaking, and a tutorial may appear; dismiss it.
2. With TalkBack on, remember the gesture change: one tap now selects and announces, and a double-tap activates. Open anything with a text field and double-tap into it. Look for: Hexboard's keys appearing, and TalkBack announcing something when you touch one.
3. Drag one finger slowly across a row of keys without lifting. Look for: TalkBack naming each key as you cross it, and the name changing at roughly the point the circles meet rather than early or late.
4. Pick three keys spread across the board and, for each, tap it and then double-tap to enter it. Look for: the character that arrives matching the key TalkBack named.
5. Turn TalkBack off the same way you turned it on. Look for: normal tapping returning.
6. Report three things: any key TalkBack named wrongly or did not name at all, any place where the name changed noticeably before or after your finger crossed between two keys, and whether every character you entered matched what was announced.

Switch access is deliberately not in this walkthrough and is now [verify-switch-access-ondevice], filed on 2026-09-03. It needs its own setup, and it turned out to have a question of its own worth asking — whether the scan order makes sense across the zigzag — so bundling it here would have buried that. TalkBack and Switch Access exercise the same accessibility nodes, so this item covers the labelling and bounds half and that one covers reachability, order and scanning speed. That ordering is written on both entries.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

#### [user] Check the board under Switch Access, for reachability, scan order and how long a key takes [verify-switch-access-ondevice]
Blocked by: [verify-this-runs-build-on-device]
**Held on 2026-09-04.** You deferred this during the build run of 2026-09-04 because that run's work is not on the phone, and nothing in the queue said so. It now waits on [verify-this-runs-build-on-device], which installs that build. That ordering is written on both entries.

Switch Access is Android's way of using a phone without touching the screen: a physical button moves a highlight from one on-screen element to the next, and a second press selects whatever is highlighted. The switch can be a purpose-built button, a keyboard key, one of the phone's own volume keys, or a facial gesture read by the camera. This item checks that Hexboard's keys work under it.

**Why it exists.** SPEC's accessibility bullet names screen readers **and switch access** together, and says the per-key nodes are a separate requirement from nearest-centre routing. So a TalkBack pass proves half of that promise and this proves the other half. It was cut from [verify-a11y-ondevice] on 2026-09-03, where it had been named and left out for needing its own setup; that item's own note points here.

**The route to drive it, settled on 2026-09-03 after the obvious one turned out to defeat itself.** The usual cheap switch is a Bluetooth keyboard, and you have one — but Hexboard inherits `onEvaluateInputViewShown()` and hides whenever a hard keyboard connects, so that route would leave you scanning a board that is not on screen. Android's Switch Access takes three switch sources: an external USB or Bluetooth keyboard, the phone's own buttons including the volume keys, or Camera Switches driven by facial gestures. The volume keys need no hardware and connect nothing, so nothing hides. Read from Google's accessibility help pages on 2026-09-03.

**What this tests, and what it does not.** The switch source sits upstream of everything Hexboard does: whatever is pressed, Android walks the same accessibility tree with the same scanning machinery, and Hexboard never sees the switch at all. So reachability, order and labelling are the same whatever someone presses, and the volume keys stand in for the pressing rather than for the scanning. It tests nothing about anyone's actual switch hardware, and that limit is stated rather than glossed.

**The part with real information in it: scan order across the zigzag.** Odd columns sit half a key lower than even ones, so the board has no straight rows. A scan that traverses by position may hop down-and-up-and-down instead of running along each row, which would be tiring and confusing to use. Nobody has looked, and it is a property of this project's own geometry rather than a generic accessibility question.

**And the harsh case, which the two-switch test hides.** Switch Access can run on one switch, where the highlight advances on a timer and the press only selects. A letter panel carries around thirty keys, so on a timer the highlight could take a very long time to reach a key near the end. That is a problem a keyboard has and most apps do not. Raised by you on 2026-09-03, from asking what happens for a user whose switch is not set up the way this walkthrough assumes.

Why it cannot be Claude's: it needs accessibility services running on a real handset and a person watching a highlight move, and there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator].

The walkthrough:
1. Open Settings and type `switch access` into its search box. Look for: a result that opens a screen with a Switch Access on/off switch. If the search finds nothing useful, it lives under Accessibility. Nobody has run this search on the Pixel 6, so which of the two works is the first thing this step establishes.
2. Turn Switch Access on and follow its setup as far as assigning switches. Assign **volume up** to Next and **volume down** to Select. Look for: the setup accepting both keys, and a highlight box appearing on screen when you press volume up.
3. Open anything with a text field and bring Hexboard up. Press volume up repeatedly to walk the highlight across the board. Look for: every key taking the highlight in turn, and each one announced or shown with the character it types.
4. Watch the path the highlight takes across a row of keys. Look for: whether it runs along the row, or jumps between the higher and lower columns — that zigzag hop is the thing this step exists to catch, so describe the path rather than answering yes or no.
5. Press volume down on a highlighted key. Look for: that character arriving in the text field, and not a different one.
6. Go back into Switch Access settings and turn on auto-scan, so the highlight advances on a timer and only one switch is needed. Look for: the highlight moving by itself.
7. From the start of the board, time how long it takes the highlight to reach a key near the end of the bottom row. Look for: the number of seconds, which is the whole point of this step.
8. Turn Switch Access off. Look for: normal tapping returning.
9. Report five things: any key the highlight never reached or named wrongly, what the path looked like across the zigzag, whether the selected character was the one shown, the auto-scan time to a far key, and anything that made it awkward that these steps did not ask about.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

**What a bad result means, so the report is worth making.** A confusing scan order or an unacceptable auto-scan time is a design question about grouping the board for scanning — rows as groups, or panels as groups, so the highlight descends into a row rather than crossing every key. That would be its own work and is deliberately not designed here.

Rests on: Switch Access accepting the phone's own volume keys as switches, and offering auto-scan on a single switch, both read from Google's accessibility help pages on 2026-09-03 and not tried; `onEvaluateInputViewShown()`'s inherited hiding, read from Android's documentation on 2026-09-02, which is why the keyboard route was rejected; that the scan traverses by position rather than by declaration order, which is unverified and is exactly what step 4 looks at.

[verify-a11y-ondevice] covers the TalkBack half and names this item as the other; [physical-keyboard-handover] must not be run in the same sitting, since its Bluetooth keyboard hides the board this one scans. Those orderings are written on all three entries.

#### [user] Check Hexboard hides for a physical keyboard and comes back intact [physical-keyboard-handover]
Blocked by: [verify-this-runs-build-on-device]
**Held again on 2026-09-04.** You deferred this during the build run of 2026-09-04 because that run's work is not on the phone, and nothing in the queue said so. It now waits on [verify-this-runs-build-on-device], which installs that build. That ordering is written on both entries.

**Cleared to run on 2026-09-03**, when the install run put Hexboard on the Pixel 6 as a switched-on keyboard, which is what step 1 below needs.

A test with a Bluetooth or USB keyboard paired to the Pixel 6: Hexboard should get out of the way while it is connected, and come back working when it is not.

Raised by you on 2026-09-02, asking whether interactivity with physical keyboards needs testing at all and what the standard even is.

**The standard is Android's and Hexboard already has it, which is what narrowed this to one check.** `InputMethodService` decides whether to draw its input view in `onEvaluateInputViewShown()`, and the default answer is to show the keyboard unless a hard keyboard is available. `HexboardImeService` extends `InputMethodService` and does not override that method, so the hiding is inherited rather than written. The trigger is the keyboard connecting, not typing starting somewhere else — the platform re-evaluates on the configuration change a pairing produces. Read from Android's documentation on 2026-09-02, not run.

**So what is worth testing is not the hiding but the surviving.** Hexboard's input view is Compose, hosted with lifecycle, view-model and saved-state owners installed by hand on the view and its decor view — the arrangement [first-installable-build] had to design around because it is the known crash trap. The platform destroys and recreates that view when a keyboard connects and disconnects, and nothing has ever exercised that path. Nothing here makes a failure likely; it is untested rather than suspected.

**A feature this test deliberately does not ask for.** Keyboards like Gboard offer a setting to keep the on-screen keyboard visible while a physical one is attached, by overriding that same method to return true on the user's say-so. That is a feature rather than a standard, nobody has asked for it, and this item is not it.

Why it cannot be Claude's: it needs a physical keyboard paired to a phone, and there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator] and [install-and-enable-on-pixel]. You confirmed on 2026-09-02 that you have a keyboard to pair, which is what made this worth filing at all.

The walkthrough:
1. With Hexboard switched on and showing, open anything with a text field and tap into it. Look for: the circular keys in zig-zag rows, as usual.
2. Put the physical keyboard into pairing mode, then open Settings and type `bluetooth` into its search box. Look for: a result that opens the Bluetooth device list, with your keyboard offered as something to pair with. If the search finds nothing useful, the list lives under Connected devices. Nobody has run this search on the Pixel 6, so which of the two works is the first thing this step establishes.
3. Pair it, then return to the text field. Look for: Hexboard's keys disappearing from the screen, leaving the text field with more room.
4. Type a few characters on the physical keyboard. Look for: the characters arriving in the text field.
5. Disconnect the keyboard — switch it off, or unpair it on the same Bluetooth screen. Look for: Hexboard's keys returning by themselves when you next tap into a text field.
6. With the board back, tap several keys and swipe sideways between panels. Look for: characters arriving as before, and the panels changing — a board that draws but does not respond is the failure this step is for.
7. Report four things: whether the board hid, whether it came back, whether it still worked afterwards, and anything the Run panel's log printed while you were doing it.

**Step 2 was rewritten on 2026-09-03 to reach Settings by searching rather than by a path.** It named Settings → Connected devices, which nobody had checked against the handset — and the install walkthrough's own written path turned out not to match the Pixel 6 when it was driven that day. Android moves these screens between versions and the search box does not, so a step naming a term to search and a screen to look for should survive what a menu path does not. That is reasoning rather than a checked fact: whether the search actually reaches the Bluetooth list on this handset is unverified, which is why the step keeps Connected devices as its stated fallback.

[settings-steps-name-a-search] is held against this item for exactly that reason: it would write the search-box instruction into `CLAUDE.md` as the standing rule for every future walkthrough, and driving this one is the cheapest thing that shows whether the search works. So say in your report which of the two routes got you there. That ordering is written on both entries.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

Do not run this in the same sitting as [verify-switch-access-ondevice]. The hiding this item tests is exactly what would defeat that one: a Bluetooth keyboard connected here takes Hexboard off the screen, and that item needs the board visible to scan it. That is why it drives Switch Access from the volume keys instead. That ordering is written on both entries.

Rests on: `onEvaluateInputViewShown()`'s default behaviour, read from Android's documentation on 2026-09-02 and not run; that the platform destroys and recreates the input view on a keyboard connection rather than merely hiding it, which is the reason for the test and is itself what the test would establish.

#### Settings steps name a search term rather than a menu path, as a CLAUDE.md rule [settings-steps-name-a-search]
Blocked by: [physical-keyboard-handover]
One sentence added to `CLAUDE.md`'s project rules, saying how a walkthrough step should send someone into Android's Settings.

**Why it exists.** The install walkthrough of 2026-09-02 wrote a five-level menu path — Settings → System → Languages & input → On-screen keyboard → Manage on-screen keyboards — and when it was driven on 2026-09-03 the Pixel 6 had no such path. The destination screen was titled **Keyboard apps**. The step's outcome was reached anyway, because the person driving it went and found the screen, which is exactly the kind of rescue a walkthrough must not depend on. Android moves these screens between versions, so a path written down is stale on a schedule nobody here controls.

**Why a rule rather than three corrections.** Two further walkthroughs had already inherited the same instinct — [physical-keyboard-handover] named Settings → Connected devices, and [verify-a11y-ondevice] asked for accessibility services to be on without saying how — and both were rewritten by hand on 2026-09-03. The instinct is what produces them, so the fix belongs at the level that governs how walkthrough steps are written rather than at each instance. `CLAUDE.md` already carries the neighbouring rule, added 2026-09-02, that a GUI step names something visible to click or a menu path with any shortcut only as an aside; this narrows that rule for the one surface where written paths have actually failed.

**Held against [physical-keyboard-handover], and the reason is that this rule rests on a fact nobody has checked.** Whether searching Android's Settings for a term actually reaches these screens on the Pixel 6 is unverified — no `adb` on this machine, no way to read that phone's Settings app, and the search was proposed rather than tried. Writing it into `CLAUDE.md` as the standing rule would make it govern every future walkthrough on the strength of a guess, which is the same mistake at one level up. [physical-keyboard-handover] and [verify-a11y-ondevice] both now open by searching, each carrying the older menu path as its stated fallback, so the first of them to be driven settles it. That ordering is written on both entries.

**What the build changes.**
- `CLAUDE.md` — one sentence in the project rules, beside the existing GUI-step sentence: a step sending the user into Android Settings names a term to type into the Settings search box and the screen title to look for, rather than a menu path.

**The observation that shows it landed:** `CLAUDE.md`'s project rules contain a sentence naming the Settings search box, and the existing GUI-step sentence is still there beside it rather than replaced.

**Options already refused.** Correcting the three walkthroughs and writing no rule — the instinct that produced them survives, and the next walkthrough repeats it; two of the three were already written before anyone noticed. Writing the rule now, ahead of the check — makes an unverified route binding on everything written afterwards. Replacing the existing GUI-step sentence rather than sitting beside it — that sentence governs every GUI surface, not just Settings, and Android Studio has no search box of this kind.

Rests on: the install walkthrough's path failing against the Pixel 6, and the destination screen being titled Keyboard apps, both reported by the user on 2026-09-03 from the drive itself; that Android's Settings search reaches these screens, which is **unverified as of 2026-09-03** and is what the hold exists for.

**A second edit to the same paragraph was accepted on 2026-09-05, deliberately.** [walkthrough-steps-quote-screen-text] adds a rule about quoting on-screen text exactly, in this same part of `CLAUDE.md`, and was cleared to run rather than merged with this one. It rests on nothing unverified — quoting text read from the source is correct by construction — where this item rests on a Settings-search route nobody has tried, and holding a sound rule behind an unsound one to save a second small edit is the wrong trade. That sentence is worded generally enough that this one will read as a narrowing of it rather than a third unrelated rule. That reasoning is written on both entries.

Filed on 2026-09-03 at 16:31, split out of the capture that reported the wrong Settings path because a planning session may not write `CLAUDE.md`. That capture's other half — correcting the two live walkthroughs — was done in the same session, and the capture was then deleted.

## Unprocessed

> Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Speech recognition that adapts to its own user's voice, trained on the phone [personal-voice-model]
Red flag · State: cleared
Blocked by: [in-keyboard-voice-input]
Captured by you on 2026-09-01 and designed with you the same session. Your complaint is concrete: Gboard requires you to talk in an American accent to be understood.

**This is the answer to that complaint, and an accent picker is not.** An accent list can only offer the English varieties a device supports, organised by where a variety is spoken natively — Indian English, Nigerian English, Singaporean English. It reaches whoever matches an entry and misses everyone else, and your objection was that Australia is a multicultural country: a second-language English speaker matches no entry at all. Adapting to the individual voice is the only approach that does not care what the speaker's first language was. You dropped the picker on those grounds, and that decision is recorded on [in-keyboard-voice-input] as well.

**Training happens on the phone.** Your choice, from three: on the phone, on a server, or on the user's own computer. The server route was rejected because uploading recordings of someone's voice is exactly what the rest of this keyboard refuses to do, and the user's-own-computer route because it asks a phone user to run a training job on a laptop, which most of the audience will not do. The cost accepted with the on-phone choice is a job heavy enough to want charge and idle time.

**Enrolment audio is destroyed once adaptation has run.** Your choice, over keeping it on the device. The phone ends up holding an adapted model and no recordings, which is the strongest position available and the one consistent with everything else here. What it costs is stated below rather than glossed.

**The cost of destroying the audio, which defeats an earlier idea and must not be quietly restored.** Accumulating enrolment across short sittings — two or three minutes at a time, quality improving over weeks — was proposed and is not available under this choice, because accumulating requires keeping the recordings between sittings. So either one sitting must be enough on its own, or adaptation runs incrementally after each sitting on material that is then destroyed, and nobody here has checked whether incremental adaptation without the earlier audio degrades what was already learned. That is a real open question for the build, not a detail.

**How much audio this actually needs, which is where an earlier claim was wrong.** A previous turn of this session put it at about 30 minutes; you challenged that, and the challenge was right. The research is filed as `workshop/resources/research/speaker-adaptation-cost.md`. Its answer: 30 minutes describes full fine-tuning, which at low data is the *worst* technique available because it overfits a single speaker. LoRA adaptation updates roughly 3% of parameters and beats full fine-tuning in exactly the low-data regime a phone user is in; speaker-embedding conditioning needs seconds and no training job; and one 2026 method adapts while decoding with no enrolment at all. So the design should reach for the cheap rungs and treat full fine-tuning as the last resort rather than the default.

**The privacy risk, raised by Claude and settled with you across this session's exchange, which is what clears the flag above.** Enrolment means recording the user's voice, which is more sensitive than anything else this keyboard holds. Three things answer it: the audio never leaves the device, it is destroyed once adaptation has run, and enrolment is a deliberate session the user starts rather than anything that happens in the background. The residual, accepted knowingly: while an enrolment session is running the microphone is open and recordings exist on disk, so the window is real even though it is short and user-initiated.

**Not designable yet, and this is why it sits in Unprocessed rather than below the line.** Three things are unknown and none of them is a decision anyone can make at a desk. No packaged Android implementation of any rung was found, so the engineering distance between the research and a Pixel is unmeasured. Whisper does not stream, so words would arrive in a block rather than as spoken, while the models that do stream are less accurate to begin with — which is the thing adaptation is meant to fix, so the two constraints pull against each other and nothing here resolves them. And the published gains are modest and measured on elderly and pathological speech, not on the multilingual speakers this exists for, so the size of the win is genuinely unknown.

Cites research: `workshop/resources/research/speaker-adaptation-cost.md`.

Rests on: that research file's ladder, read from published work on 2026-09-01 and nothing run; the field's own speed, which that file flags — the zero-shot work is from June 2026, so the ladder is to be re-checked before any build rather than trusted as written.

An article proposing this as a piece for the site project was sent on 2026-09-01, carrying the same caveats and an explicit warning that novelty is unverified. That send is in `INBOX/sent.md`.

#### Words the user deliberately saves, kept on the phone and readable by them [predictive-saved-words]
Red flag · State: cleared
Blocked by: [uniform-neighbours-predictive]
A store of words the user has explicitly chosen to add, which the autocorrect treats as dictionary words so it stops correcting them.

**Split out of [uniform-neighbours-predictive] on 2026-09-04**, when that item was decomposed into buildable work. It is the one part of the engine that stores anything, so it carries its own risk and its own consent trail rather than riding inside the correction item.

**Why it exists, in your framing.** A keyboard that cannot learn a surname or a street name is worse to use. Without this the engine mangles a name every time it is typed, since a word not in the shipped list is exactly what the correction machinery reaches for.

**The privacy position, and it changed footing on 2026-09-01 — this is the part a later session must not misread.** The original decision of 2026-08-20 was a fixed dictionary learning from nothing and recording nothing, chosen so the engine never holds a record of your writing on the device, which is the exposure Android's own keyboard warning is about. That designed the risk out. On 2026-09-01 you were told plainly that saving words means the device holds a file of words you typed, that this is smaller than silent learning because each word is a deliberate act rather than a harvest, and that it stops the "stores nothing" claim being true. You chose the saved-word store knowingly. So the flag is cleared **by your informed consent to a smaller risk**, not by design-out, and three constraints came with that consent and bind the build:

- the list grows only when you deliberately save a word — nothing is added by observing what is typed;
- it never leaves the device;
- you can read it and delete from it.

**What is not settled, and it is a real design question rather than a detail.** Whether the list needs anything beyond read and delete — an export, a lock, a way to clear it wholesale. It was left open on 2026-09-01 as a question for whoever builds this rather than something to guess at, and it is still open. The clipboard's own reasoning bears on one of those: [persistent-clipboard] deliberately has no clear-all, because an emptied history is itself a disclosure to anyone looking over your shoulder. Whether the same argument applies to a word list is a different question — a word list is not a record of what you copied — and it is worth asking rather than assuming either way.

**Also unsettled: how a word gets saved.** There is no affordance for it anywhere in the design, and the row above the keys already has three claimants ([suggestion-strip]'s own contents, the microphone and the clipboard button). That is why this stays in Unprocessed rather than being kept as buildable work: what a build changes cannot be stated until both the saving gesture and the read-and-delete surface are chosen. The `Blocked by:` line above therefore means *do not offer this again while the engine is still open* — it returns by itself once that item is built.

**Held against [uniform-neighbours-predictive]** because the store's whole purpose is to be consulted by the correction engine, and there is nothing to consult it from until that exists. That ordering is written on both entries.

Rests on: SPEC's predictive-text principle, which already states that the engine does not learn and that the saved-word list grows only by a deliberate save, stays on the device and is the user's to read and delete — read from `SPEC.md` on 2026-09-04; the consent exchange of 2026-09-01, recorded in that session's record under [uniform-neighbours-predictive].

#### Bias dictation toward the words the user has saved, and nothing else [dictation-biasing-saved-words]
Blocked by: [predictive-saved-words]
Hand the speech recogniser the user's own saved words so it is likelier to hear them — a surname, a street name, a term from their work — without storing anything about what they have said.

Split out of [speech-output-correction] on 2026-09-04, when that item narrowed to asking the platform to punctuate. Biasing was one of the three routes it carried and is the only one still live, and it is a different feature rather than a detail of that one.

**What makes it possible, and it answers a question the original item recorded as unresearched.** That item asked whether the on-device API permits biasing at all. It does: Android added `EXTRA_BIASING_STRINGS` in API 33 — "optional list of strings, towards which the recognizer should bias the recognition results". So the influence happens inside recognition, which is where `workshop/resources/research/gboard-speech-correction.md` established that correction has to happen if it is to be worth anything.

**Why this is the version of personalised dictation this project can have.** Gboard's advantage here comes partly from keeping a per-user record of what someone says and how they correct it, which is the class of storage refused everywhere else here — predictive text keeps only words deliberately saved, and enrolment audio for [personal-voice-model] is destroyed after adaptation. Biasing needs none of that: it needs a list of words, and [predictive-saved-words] is already a list of words the user chose to keep. Nothing new is stored and nothing about speech is recorded.

**A second switch that is deliberately left alone.** API 33 also carries `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT`, "optional boolean to enable biasing towards device context" — which would plausibly bring the phone's contacts into recognition. It is not set, so it is not asked for. This is a privacy choice rather than a rule: SPEC's no-proper-nouns rule governs the autocorrect dictionary, where the harm is a typed word being turned into a name, and dictating a friend's name correctly is a benefit rather than that harm. So nothing forbids the device-context switch; it is left off because the conservative default matches everything else here, and turning it on would be the user's decision to make knowingly. Android's documentation never says what "device context" contains, which is a further reason not to opt into it blind.

**Why it stays a capture rather than becoming work.** [predictive-saved-words] does not exist and is itself undesigned — nobody has settled how a word gets saved or where the list is read. There is no list to hand the recogniser, so what a build changes cannot be stated. The `Blocked by:` line means *do not offer this again while that item is open*; it returns by itself once the list is built.

**What is not established.** Whether the extras' defaults are what the documentation implies — they are described as optional and as enabling a behaviour, which reads as off unless set, without the default being stated outright. That is checkable on the phone once there is a microphone to check with, and it is worth checking rather than assuming, since being wrong about the device-context default would mean contacts influencing recognition without anyone choosing it.

Rests on: `EXTRA_BIASING_STRINGS` and `EXTRA_ENABLE_BIASING_DEVICE_CONTEXT` arriving in API 33, read from Android's API 33 difference report and the class reference on 2026-09-04 and not run; the recogniser being reachable at all on this handset, established by the drive of [recogniser-gap-comparison] on 2026-09-04.

[speech-output-correction] configures the same recognition request for punctuation and is the sibling of this one; [predictive-saved-words] is the list it would draw on. Those orderings are written on all three entries.

#### Catch a complaint inside the app before it becomes a Play Store review [feedback-funnel-before-store]
Blocked by: [layout-error-report]
A general route for telling Hexboard something is wrong — not just a wrong key on a layout — aimed at reaching people while the complaint is still fixable rather than after it has been left as a one-star review.

Captured by you on 2026-09-04, while settling how layout errors get found once the reader-confirmation requirement goes. Your aim in your own framing: capture people before they go to the Play Store, accepting that this may mean a lot of email, and that agent screening is what you would add if the load got heavy — a small price for a better listing.

**Why it is filed rather than built now.** [layout-error-report] is the narrow piece the SPEC change needed and is buildable today. This is the wider funnel, and Hexboard has no Play Store listing, no verified keyboard on a handset, and no users — so designing the shape of a complaint channel now would be designing months ahead of the thing it serves, against a volume nobody can estimate. It returns by itself once the narrow report exists and there is something to generalise from.

**What is already settled and carries over, so this is not started from nothing.** Three constraints come from [layout-error-report] and are not up for rediscovery: the report must be built from a fixed list of fields with no path from the text field into it; nothing is sent by the app itself, the person sending it from their own mail app after seeing the text; and the address lives in gitignored `local.properties` rather than in this public repository.

**And one policy line, read on 2026-09-04.** Google's in-app review guidance forbids asking the user any question before or while presenting the rating prompt, including "are you enjoying the app?" — filtering for happy reviewers inflates ratings and is prohibited. So the funnel may never be a mood check that routes unhappy people away from the rating flow. What is permitted is a standing report route that is always available and attached to no prompt. Any design here starts from that.

**What is genuinely open.** Whether the funnel is one entry or several by kind of problem; whether it lives only in the app or also somewhere reachable from the keyboard without spending a gesture; whether a report can carry a screenshot, and what that would mean for the no-typed-text rule when the screenshot is of a text field; and what screening looks like when volume arrives — your own suggestion of an agent is the starting point rather than a decided answer.

Rests on: Google's in-app review guidance, read on 2026-09-04, which is the kind of policy amended on a cycle and should be re-read before this is designed; the three constraints from [layout-error-report], which are design decisions of this project rather than external facts.

#### Deliver each language's word list after install rather than inside the app [dictionary-asset-packs]
Blocked by: [predictive-dictionary-bundle]
A language's word list reaches the phone when that language is used, rather than every word list being carried by every install.

Filed on 2026-09-05, from the plan settled with you the same day in [language-list-choice]. Your question is what produced it: whether a language brings its whole dictionary with it, and whether languages should be downloaded and activated separately. For layouts the answer was no — 17 KB each is not worth a mechanism. For dictionaries it is yes.

**The mechanism, read from Android's own documentation on 2026-09-05.** Google Play Asset Delivery. An app bundle declares asset packs — containers of assets with no executable code — in one of three delivery modes: install-time, which counts toward the download size shown on the listing; fast-follow, downloaded automatically after installation; and on-demand, downloaded while the app runs. Neither of the last two counts toward the listed size.

**The property that makes it usable here at all: Google Play performs the delivery and the app makes no network request of its own.** No server, no CDN, and no internet permission on a keyboard — which is the thing this project's whole posture refuses. An app that fetched its own dictionaries would be a keyboard with network access, and no amount of explaining would make that a good look in a public repository.

**What it costs.** Publishing as an Android App Bundle through Google Play. A build distributed any other way — a sideloaded APK, another store — cannot use it, so those routes would need every dictionary shipped inside the app or none at all. Whether Hexboard ever distributes outside Play is undecided and this item does not decide it.

**Stated rather than glossed: Android's documentation frames asset packs as a feature for games**, describing them as containers of game assets replacing legacy expansion files. Nothing read says a non-game app may not use them and the mechanism is an app-bundle feature rather than a games programme, but no non-game example was read, and the size limits were noted as existing rather than read. Both want confirming before this is built.

**Why it waits.** [predictive-dictionary-bundle] ships the English word list inside the app, which is the right shape while there is one. This item only earns its complexity at the second dictionary, and there is no second dictionary until a language's word list has been found and its licence read — one hunt per language, per `workshop/resources/research/language-cost-layouts-versus-dictionaries.md`. Building the delivery mechanism before there is anything to deliver would be machinery ahead of its cargo.

**What is not settled.** Which delivery mode fits — on-demand when a layout is first chosen is the obvious shape, but fast-follow for the phone's own system language may be better, and nobody has weighed them. What the keyboard does while a dictionary is downloading, which is a real moment: the layout works and correction is simply absent, so the honest answer may be that nothing needs to happen. And whether a failed or refused download leaves any state worth reporting to the user.

Cites research: `workshop/resources/research/language-cost-layouts-versus-dictionaries.md`.

Rests on: Play Asset Delivery's three modes, its no-network-request property and its App Bundle requirement, all read from Android's documentation on 2026-09-05 and nothing run; that a word list is a megabyte or two per language, which is an estimate rather than a measurement and is flagged as such in the research file.

[language-list-choice] carries the plan this implements. [predictive-dictionary-bundle] ships the first word list the old way. Those orderings are written on all three entries.

#### What Hexboard does where one language has two standard layouts [competing-layout-standards]
Blocked by: [first-batch-layouts]
Some languages have more than one keyboard layout in ordinary use. This decides whether Hexboard picks one, ships both, or asks.

Split out of [language-list-choice] on 2026-09-05, when the plan for adding languages was settled and this was the one part of it left open. It had been sitting inside that item since 2026-09-02, raised by Russian.

**The cases, from FlorisBoard's own file listing read on 2026-09-05.** Russian has the standard ЙЦУКЕН and a phonetic ЯВЕРТЫ arrangement, the second never investigated here. Turkish ships as both F and Q. Persian has three files. Bulgarian has both the BDS standard and a phonetic layout. Serbian has Cyrillic and Latin. So this is a recurring shape rather than a Russian oddity, and it will arrive again with every second or third language.

**Why it is not simply "ship both".** Each extra layout is another config to transcribe and another entry in the picker, and a picker listing three Persian layouts asks a user to know which one they want before they have typed anything. Against that, picking one for them means guessing, and guessing wrong makes the keyboard unusable for whoever uses the other.

**What already exists to build on, so this is a decision rather than a design.** The picker orders layouts by a set position within a language, settled on 2026-09-02 and built by [layout-switching] — so shipping two variants of one language is already representable, and the ordering field already decides which appears first. Nothing new is needed mechanically. What is missing is the policy.

**Three answers are visible and none is chosen.** Ship only the one a language's phones most commonly show, and add a second on request under the demand-driven policy. Ship every variant the source offers, and let the picker's order carry the recommendation. Or decide per language when it is transcribed, which is the honest description of what happens if nothing is settled.

**Held against [first-batch-layouts]** because that batch is five Latin-script languages with one standard layout each, so it will not exercise this question at all. The next batch after it plausibly will, and by then there will be a real picker with real layouts in it to reason about rather than a hypothetical one.

Rests on: FlorisBoard's layout file listing showing multiple standards for Russian, Turkish, Persian, Bulgarian and Serbian, read on 2026-09-05; the picker's within-language ordering, settled 2026-09-02 and specified in [layout-switching], not yet built.

#### Prompt the speaker phrase by phrase while the voice model is being trained [enrolment-prompter]
Blocked by: [personal-voice-model]
The enrolment session shows what to say one phrase at a time, at the speaker's own pace, so the model adapts to how they actually talk rather than to how they read aloud.

**This is what you were actually thinking of**, said on 2026-09-05 when the idea was being filed as a testing instrument. You raised it on 2026-09-04 during a dictation test, so it was written up as a way of testing recognisers; the use you had in mind was the sitting where someone trains the model.

**Why it matters more here than in a test.** A test measured on reading-aloud speech gives a flattering number. A *model* trained on reading-aloud speech adapts to the wrong voice entirely — it fits the speech you produce when reading, and is then asked to recognise the speech you produce when talking. The mismatch is not a measurement error; it is baked into what the model learned. [personal-voice-model] already records that enrolment must be a deliberate session the user starts, and this says what happens inside it.

**And the fairness point lands harder here too.** A method that requires fluent reading aloud gives anyone with dyslexia or a reading disability a worse *personal model* — a permanently worse keyboard — rather than a worse test result. That is the same argument you made on 2026-09-04, applied to the case where the consequence sticks.

**The design carries over from [rsvp-dictation-prompter] intact**: one phrase at a time, large, nothing visible ahead of it; the speaker advancing it rather than a timer; a rotating set large enough that repeated sittings do not teach the content. All of that was settled with you on 2026-09-04 and none of it changes by moving into the app.

**What does change, and it is a genuine design question rather than a detail.** A dictation test wants speech representative of how someone talks. Enrolment may instead want **phonetic coverage** — passages chosen so the model hears every sound it needs, which is a different criterion for what fills the rotating set and can pull directly against naturalness. Nobody has looked at which wins, or whether a set can satisfy both. That question is this item's and is not answered here.

**Two more things it inherits from [personal-voice-model] rather than deciding.** How long a sitting has to be, which that item records as genuinely unknown and dependent on which adaptation technique is used. And that enrolment audio is destroyed once adaptation has run, so a sitting cannot be accumulated across days — which makes it matter more that a single sitting produces usable speech.

**Why it stays a capture.** [personal-voice-model] is itself undesignable — three unknowns recorded on it, none a desk decision — and there is no enrolment session for a prompter to live inside. What a build changes cannot be stated until that exists. The `Blocked by:` line means do not offer this again while that item is open; it returns by itself.

**And it may not survive its own trial.** [prompter-elicits-natural-speech] tests, cheaply and long before any of this, whether phrase-at-a-time speaker-paced prompting produces ordinary speech at all. If it does not, this item's premise is gone and it should be re-examined rather than built.

Rests on: [personal-voice-model]'s record of enrolment being a user-started session with audio destroyed after adaptation, and of the sitting length being unknown, read from that entry on 2026-09-05; the prompter design settled with you on 2026-09-04; that reading-aloud speech and ordinary speech differ enough to matter to an adapted model, which is your own observation from the drive of [recogniser-gap-comparison] and has not been measured.

#### Russian layout has thirteen empty slots and its own view of quotation marks [russian-panel-gaps]
Blocked by: [symbols-panel-empty-slots]
The Russian layout's symbols panel has ten empty positions and its RARE panel three more, and what should fill them is not the same answer as English.

Found on 2026-09-05 while filling the English symbols panel, by counting both configs rather than by anything failing. The Russian symbols panel is four rows of ten with thirty keys, empty at exactly the same positions as the English one — (0,1) (0,3) (0,5) (1,2) (1,4) (1,6) (2,5) (3,0) (3,2) (3,4) — which is unsurprising, since it was transcribed from the English config's shape. Its RARE panel is three rows of eleven with thirty keys, empty at (0,10) (1,10) and (2,0).

**Why it is not simply "copy the English answer".** [symbols-panel-empty-slots] fills the English slots with four curly quotes among other things, and Russian punctuation does not work that way: « » are the primary quotation marks and „ " the secondary pair. So the four characters doing the most work in the English fill are the wrong four here. SPEC's manifest rules bind each layout individually, which is exactly the case this is.

**What is genuinely open.** Which characters fill the thirteen; whether the Russian symbols panel should carry the same non-quote choices as English (the bullet, the arrows, ½, ¢ and ≈ have no obvious language dependence); and whether the three RARE gaps are a transcription artefact of fitting an eleven-column panel or a deliberate space.

**And a prior question the answer depends on.** Hexboard no longer holds a layout back for a native reader's confirmation — you removed that requirement on 2026-09-04 in favour of the in-app report route, [layout-error-report]. So this is a judgment made from sources rather than from a reader, and the honest route is to transcribe what Russian keyboards actually offer rather than to reason about it, the way [language-starter-layouts] transcribed the letters. Where that data lives for symbol panels specifically was not established: FlorisBoard's character layouts cover letters, and its symbol arrangements were not read.

**Held against [symbols-panel-empty-slots]** so the English decision lands first and this one can follow its shape where the shape transfers, rather than two panels being designed at once from scratch. That ordering is written on both entries.

Rests on: the two configs' key counts and empty positions, counted from `resources/key-layout.json` and `resources/key-layout-ru.json` on 2026-09-05; Russian using « » as primary and „ " as secondary quotation marks, which is a claim about the language recorded here as the reason not to copy English and worth checking against a source before it is built on; that FlorisBoard's files cover character layouts rather than symbol panels, read from its directory listing on 2026-09-05.

#### Run the first turn of the emoji-list refresh cycle [emoji-data-refresh]
Cycle: emoji-data-refresh
Filed by the close of 2026-09-05 at the cycles due-ness check, the cycle having been authored earlier the same session and having no completed turn yet.

The definition is in `CYCLES.md`. Its observable is the most recent record under this slug whose opening line says it records a completed turn, and no such record exists — the only record under this slug is the one about authoring the cycle, which the definition deliberately excludes. So the cycle is due by construction on its first check.

**The turn will probably find nothing to do, and that is a completed turn.** The directory listing at `unicode.org/Public/emoji/` was read on 2026-09-04 and its highest published version was 16.0, which is what `resources/emoji-test.txt` already carries. If that is still true when the turn runs, step 2 of the definition applies: nothing changes, and the turn is recorded as complete. Doing that once is what gives the observable something to read, so the next check computes due-ness from a real turn rather than from an absence.

