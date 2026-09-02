# QUEUE

## Processed

> Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Flip the Hexboard repo from private to public on GitHub [repo-go-public]
Captured by you. Split out of [licence-and-go-public] during planning.
Runs alone

Retagged from `[user]` to Claude-work on 2026-08-14, on your instruction, after the original premise turned out to be false. The item had said only you could do this because it was "an account action on github.com that Claude can't perform". That is wrong: the `gh` command-line tool on this machine is signed in with admin rights over the FlintcraftTech organisation, so the flip is a single command. The browser walkthrough this replaces is preserved in git history if it is ever needed again.

The work is one command:

```
gh repo edit FlintcraftTech/Hexboard --visibility public --accept-visibility-change-consequences
```

The consequences flag is not optional — gh refuses the command without it whenever `--visibility` is used. The owner name carries a lowercase `c`, GitHub's current spelling, confirmed against the GitHub API on 2026-08-14.

Then verify two ways, because the authenticated view cannot tell you what a stranger sees. `gh repo view FlintcraftTech/Hexboard --json isPrivate` should report `false`; and an unauthenticated request to `https://api.github.com/repos/FlintcraftTech/Hexboard` should return 200 rather than the 404 it returns today.

**Ask before running it, and get an explicit yes in that same session.** Retagging changed who types the command, not who decides. Going public is irreversible in the way that matters — once the history is public it can be cloned, so making the repo private again does not un-expose anything. Whoever runs this must first say plainly what is about to become readable, and stop for an answer.

Lift-condition, now met and recorded so the clearing can be checked rather than trusted: [add-licence] (done), [git-history-audit] with its findings dealt with (processed in the /plan session of 2026-08-06), [untrack-faq], [git-noreply-email], and [public-readme] have all landed. The licence should be in place before anyone can read the code; the audit was the red-flag mitigation; the FAQ should leave the tree before the tree is public; and the noreply address should be set before any further commit is published. Cleared above the readiness line on 2026-08-14 on that basis.

Marked `Runs alone` because it is irreversible and outward-facing. A run that reaches it should stop there rather than carry the same session's momentum into the ask.

Placement, decided on 2026-08-14 and recorded so a later re-sort does not undo it. This sits last among the build items and ahead of [run-key-config-validator], which is the only cleared `[user]` item. It goes after [throughliner-doc-drift] and [emoji-panels-missing-from-config] because both of those correct text that this item publishes — CLAUDE.md still names the old method, and the config still claims a scope SPEC has since narrowed. It goes *before* the validator test because that test is not a gate for going public, and a run halts at a `[user]` item: left in the usual end-preferred position, the validator would have blocked the flip from ever being reached in an unattended run.

What the irreversibility means for the three limitations recorded elsewhere, and which the ask above must name — the personal address in commit metadata and in file content at `a42cd01`, the reworded candid line at `6e09dad`, and the FAQ in every commit so far — all remain readable in history after this runs, and each was consciously accepted rather than overlooked.

#### Language and ordering fields added to the key config [variant-language-fields]
Split out of [layout-switching] on 2026-09-01 and narrowed on 2026-09-02. The layout picker groups layouts by language and orders them within a language, and the config can express neither: schemaVersion 2 carries `id`, `name`, `isDefault` and `generates`, and the language is only implied by the id reading `qwerty-en`, which is a convention rather than data. Every layout written before these fields exist would have to be edited afterwards, so they come first.

The same shape of work as [variant-schema], and buildable for the same reason: what a field is called and what it holds is a desk decision, while reading it at runtime waits on a keyboard.

The build:
- `resources/key-layout.json` — schemaVersion 2 to 3, adding `language`, a BCP 47 tag (`"en"` here), required; and `order`, an integer giving the position within that language, lower first, with a missing value sorting last. The `about` prose gains a sentence saying what each is for, as it already does for `isDefault`.
- `scripts/generate-key-manifest.py` — validate both fields, and name the language in the manifest header alongside the layout.
- `resources/key-manifest.md` — regenerated by running the script, never hand-edited.

A display name for the language is deliberately not a field. The tag is enough: Android resolves it with `Locale.forLanguageTag(...).displayLanguage` and a browser with `Intl.DisplayNames`, both of which name the language in the reader's own language rather than in the author's. A hand-written name would arrive in twenty spellings and be wrong for everyone else.

A `creator` field was designed on 2026-09-01 and dropped on 2026-09-02, and the reason is worth keeping because the field looks obviously useful. It existed to credit outside contributors and link to them from the picker. Layouts are now authored in this project by copying each language's own standard, so there is no outside author to credit and a field naming this project on every layout says nothing.

No Kotlin changes. Gson ignores JSON fields the data classes do not declare, so `KeyLayout.kt` parses the config unchanged — the same reasoning [variant-schema] recorded, and untested here for the same reason, that Gradle cannot run on this machine.

The observation that shows it landed: `python scripts/generate-key-manifest.py --check` reports no drift, the regenerated manifest header names the language as well as the layout, and the manifest's key data is unchanged from the previous commit. Runs but does not change: `scripts/generate-key-manifest.py` is edited by this item and then run by its own check.

Rests on: the schemaVersion 2 field set, read from `resources/key-layout.json` on 2026-09-01; the generator's `--config` and `generates` handling, recorded in the [variant-schema] build record of 2026-09-01; Gson's tolerance of undeclared fields, carried from that item's reasoning and not executed.

Runs before [language-starter-layouts], which writes a config carrying these fields — that ordering is written on both items. Placed after [repo-go-public] because that item is marked `Runs alone` and a run stops there regardless.

Files: `resources/key-layout.json`, `scripts/generate-key-manifest.py`, `resources/key-manifest.md`.

#### Russian layout, copied from the standard ЙЦУКЕН arrangement [language-starter-layouts]
Filed on 2026-09-01 as starter layouts for a contributor-facing editor, and rewritten on 2026-09-02 when you replaced that whole approach. Your reasoning: an editor is a lot of machinery built for contributors who do not exist yet, and the layouts people actually want already exist as national standards, so copying them is cheaper and more likely to be right than any tool for authoring them. [variant-editor] and the phone-side editor were deleted on that basis.

**Copying a published standard is what makes this checkable rather than invented.** The earlier version of this item refused to let Claude author key sets for languages it does not read, and that objection stands for invention — it does not apply to transcribing an arrangement that is a matter of record and can be checked against sources.

Russian first, because it is the hard case and it is now researched. Standard ЙЦУКЕН has three letter rows of twelve, eleven and nine — Й Ц У К Е Н Г Ш Щ З Х Ъ · Ф Ы В А П Р О Л Д Ж Э · Я Ч С М И Т Ь Б Ю — which is 32. The 33rd letter, Ё, sits alone in the desktop layout's top-left corner and on a phone has no key at all: Russian keyboards on Android put it behind a long-press on Е.

**The overflow rule, established from that and applying to every later language.** Hexboard's QWERTY panel gives 30 letter slots, so two more letters move behind a long-press on the letter they are already paired with in the writing system — the hard sign Ъ under the soft sign Ь, and Щ under Ш, which differ by a tail. Pairs a reader already knows are the ones they will guess untaught, and it is what Hexboard's long-press accents do for Latin diacritics. Widening the rows was rejected because more keys per row means smaller keys and larger keys are the point; spilling onto the RARE panel was rejected because a letter is not rare in its own language.

**The two pairings above are Claude's reading and are the one thing here that is not copied**, so they are what the check below is for.

The build:
- `resources/key-layout-ru.json` (new) — a schemaVersion 3 config: `id` `jcuken-ru`, `name` `ЙЦУКЕН (Russian)`, `isDefault: false`, `language` `ru`, `order`, `generates` naming its own manifest, and the three letter rows above mapped onto the QWERTY panel's rows 0–2 with the two overflow letters as long-press entries. Row 3 — the two space bars and punctuation — is unchanged from the English layout. The RARE and SYMBOLS panels are copied from the English layout unchanged, since neither is language-specific.
- `resources/key-manifest-ru.md` (new) — generated by `python scripts/generate-key-manifest.py --config resources/key-layout-ru.json`, never hand-written.

- `planning/layout-preview.html` — its `LAYOUTS` block gains the Russian arrangement, so the board can be looked at without anyone editing JavaScript afterwards. This was moved here from [russian-layout-check]'s walkthrough on 2026-09-02: that item had asked you to repoint the preview yourself, which is an edit Claude can make, and `[user]` is reserved for what Claude genuinely cannot do. The work was happening here regardless, since the landing observation below already requires the layout to be rendered — writing it into the build is what makes the result persist for the person who has to look at it.

The observation that shows it landed: the generator runs against the new config without error and writes the manifest to the path the config names; `--check` reports no drift; and the layout renders in `planning/layout-preview.html` with every one of the 32 letters present, which is what catches a board that is wrong at a glance before anyone can type on it. Reads but does not change: `resources/key-layout.json`, for the unchanged panels and row 3. Runs but does not change: `scripts/generate-key-manifest.py`.

**A native-speaker check before this ships is a `[user]` item, [russian-layout-check], and this one does not clear it.** A shipped layout is copied rather than read, so an error in it propagates.

Cites research: `workshop/resources/research/cyrillic-overflow-and-slot-budget.md`, which carries the sources, the rejected alternatives, and the fact that its sources are English-language explainers of the Russian layout rather than Russian typists.

Rests on: the ЙЦУКЕН row contents and Ё's placement, read from published descriptions on 2026-09-02 and not observed on a device; Hexboard's 30-letter-slot budget, computed from `resources/key-layout.json` on 2026-09-02; the `language` and `order` fields, which [variant-language-fields] adds and which this config carries — that ordering is written on both items.

Which further languages follow, and in what order, is [language-list-choice]. The phonetic Russian layout (ЯВЕРТЫ) was never investigated and is not ruled out; it raises the general question of which standard to copy where a language has two.

Files: `resources/key-layout-ru.json`, `resources/key-manifest-ru.md`, `planning/layout-preview.html`.

#### SPEC's principles rebalanced and their machinery moved out [spec-principles-rework]
Filed on 2026-09-02 from a verification pass run in that session's planning, which is where the measurements below come from. It is the Claude-doable half of [spec-coherence-readthrough]; the judgment about whether SPEC still reads as one product stays there and stays yours.

**The proportion problem, measured rather than asserted.** Word counts of the Principles bullets: the perceptual wedge, which SPEC calls inviolable and which is the project's whole differentiator, runs 22 words. Predictive text, deferred and unbuilt, runs 309. The key inventory runs 269 and the layout-per-language principle 198. Clipboard, voice input and voice adaptation run 119, 116 and 89. Every remaining bullet is under 30. So within the list a reader skims, a deferred feature outweighs the reason the project exists by fourteen to one.

The count alone overstates it and the fix must not overcorrect: the wedge is also described at length in the "How it works" section, so the bullet is not its only home. What is wrong is the balance inside one list, not the total coverage of the wedge.

**The machinery to move out, each checked against where it already lives.** SPEC's own admission rule is that a sentence describing internal fields, file formats or the steps a component runs through belongs in the doc owning that mechanism, with SPEC naming the behaviour instead:
- the predictive-text principle's three sentences on deriving the neighbour table from the config plus the zag rule, and computing it at runtime rather than storing it — near-identical text already sits on [uniform-neighbours-predictive], confirmed on 2026-09-02, so this is a deletion rather than a relocation;
- the key-inventory principle's config filenames and its list of the fields a layout config carries — both already in that config's own `about` text and in README.md;
- the uppercase-glyph line's closing "This is a scaling factor, not a shared font size; chosen by eye against the layout preview" — the first clause is implementation and the second is how the decision was made, which belongs in the record;
- "The prototype's overlapping square boxes are incidental, not the design" and "The Android build is a fresh effort, not a line-by-line port" — history about the prototype rather than truth about the product;
- the wedge paragraph's "not to be re-litigated" and "not re-argued from the armchair" — guidance about how to work on the project, which is CLAUDE.md's job and which CLAUDE.md already carries.

**What this must not touch, checked in the same pass and recorded so the rework does not go further than it should.** The clipboard, voice-input and voice-adaptation principles are behavioural throughout — they say what the keyboard does, not how — and are left alone. The accessibility line justifies itself, and that justification prevents a real error about accessibility services bypassing touch routing, so it stays.

**This is a rewrite for balance and admission, never for length.** A true sentence about a live feature is not evicted for being long: what comes out is machinery that lives elsewhere, history, and rationale. Nothing here is a word budget, and no target figure is set.

The build: rewrite the three over-long principles in `SPEC.md` — key inventory, layout-per-language, predictive text — so each states the behaviour and drops the mechanism listed above; remove the five machinery and history fragments named; leave every other bullet untouched.

The observation that shows it landed: a grep of `SPEC.md` for `key-layout.json`, `key-manifest.md`, `re-litigated` and `line-by-line` returns nothing; the predictive-text bullet no longer describes when the neighbour table is computed; and every feature the file described before it still has a sentence describing it, so nothing was lost rather than shortened.

Runs before [spec-coherence-readthrough], because reading the document to judge its coherence is worth doing once the rebalancing has happened rather than twice. That ordering is written on both items.

Files: `SPEC.md`.

#### CLAUDE.md's phase line corrected — implementation has started [claude-md-phase-stale]
Rule gate: claude-md-phase-stale — not needed, no rule is added. This replaces a stale statement of fact about what exists in the repository. The one rule-like force the old sentence carried, that sessions should not rush into code, is kept as an amendment to that same sentence — its parent — rather than as anything freestanding, so nothing new competes for a reader's attention.
Filed on 2026-09-02 by /rescan, and processed in the same session. The project rules in `CLAUDE.md` say "Current phase: extended planning, no implementation." That has not been true for some time: `MainActivity.kt`, `KeyboardPanel.kt` and `KeyLayout.kt` exist, the Compose keyboard already draws its keys from the config, and four build items are cleared to run.

Why it matters more than a stale sentence usually does. `CLAUDE.md` is loaded at the start of every session on this project, so it is the first thing that shapes what a fresh session believes exists. One reading "no implementation" starts from a wrong picture — the failure mode being a session that proposes building what is already built, or treats the existing Kotlin as hypothetical.

**The line is doing a real job as well as a wrong one, and the replacement must keep it.** "Extended planning" told sessions not to rush into code, and that instinct still holds, because nothing written has been compiled or run: [compile-and-view-panel] is the cleared `[user]` item that would establish it. So the new wording carries both facts — there is real Kotlin in the repository, and none of it has been seen to run on a device. Replacing the line with "implementation has started" alone would trade one wrong picture for another.

This belongs in `CLAUDE.md` rather than `SPEC.md` because it describes how to work on the project rather than what the project is — the distinction those two files are most often confused across, and in this direction specifically.

The build: replace the phase sentence in `CLAUDE.md`'s project-rules section with wording stating that Android implementation has begun, naming what exists, and stating that none of it has been compiled or run on a device, with [compile-and-view-panel] named as the item that changes that. Everything else in that section stays: the layout-preview fixture and the frozen `hexboard17.html` prototype are both still described correctly.

The observation that shows it landed: a grep of `CLAUDE.md` for "no implementation" returns nothing, and the replacement names both that Kotlin exists and that it has not been run.

Rests on: the three Kotlin files existing, read from the repository on 2026-09-02; that nothing has been compiled, which is what [compile-and-view-panel] and [run-key-config-validator] both exist to establish and which remains true while they are unrun.

Files: `CLAUDE.md`.

#### [user] Run the key-config validator test in Android Studio [run-key-config-validator]
Captured by you. Confirmed as user-work during planning on 2026-08-06, after Claude exhausted every route it had.

Claude wrote `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` during the build of [key-config-validator] but has never been able to execute it. What is unverified is the Kotlin, not the key data: the config itself was independently checked when it was built, by reimplementing all six checks in a throwaway Python script, and every one passed against `resources/key-layout.json`. What running the test proves is that the test compiles, that Gson resolves as a test dependency, and that the test finds the config file at runtime.

Why this can't be Claude's to run, established by attempt rather than assumption. Gradle needs a loopback network connection to reach its own daemon, and every route Claude has is blocked from making one. Tried on 2026-08-06: Gradle via the Bash tool (failed — no Java on PATH); the same with Android Studio's bundled JDK 21 at `C:\Program Files\Android\Android Studio\jbr`, which Java-wise works fine (failed — "Unable to establish loopback connection"); the same again with Claude's sandbox disabled (identical failure, so the sandbox is not the cause); and via PowerShell with `--no-daemon`, which still forks a single-use daemon (identical failure). The block is below the level Claude can reach. Don't spend another session re-testing this — run it in Android Studio, which has no such restriction.

The walkthrough:
1. Open Android Studio and open the `android` folder inside the Hexboard project. Wait for the Gradle sync to finish — there's a progress bar along the bottom, and a notification strip across the top if it wants anything.
2. In the Project pane on the left, navigate to `app/src/test/java/tech/flintcraft/hexboard/` and open `KeyLayoutValidationTest.kt`.
3. In the narrow gutter immediately left of the code, next to the line declaring `class KeyLayoutValidationTest`, there's a small green triangle. Click it and choose **Run 'KeyLayoutValidationTest'**.
4. Results appear in a panel at the bottom. Report what you see: all six tests green, a compile error, or a test failure. A test failure names the offending character and its panel, so the message itself tells us what's wrong.

If it reports that it can't find the config file, the likely cause is the `hexboard.repoRoot` system property set in `android/app/build.gradle.kts`; the test also walks up from the working directory as a fallback, so both paths would have to fail.

Nothing blocks this — it can run whenever you next have Android Studio open.

#### [user] Compile the app in Android Studio and look at the QWERTY panel [compile-and-view-panel]
Filed at the close of 2026-08-21, when [compose-keyboard-renders-config] shipped code that nothing has run. Gradle cannot run on this machine — it needs a loopback connection to its own daemon and every route Claude has is blocked from making one, established across four attempts and recorded in [run-key-config-validator] — so three things are unverified: that the new Kotlin compiles, that Gson parses `key-layout.json` out of the app's assets at runtime, and that the panel draws.

Nothing in the queue already covers this. [run-key-config-validator] runs a unit test against the config and never touches the app; [install-and-enable-on-pixel] does cover a real build, but it is held behind [first-installable-build], so the first compile of this code would otherwise wait on an IME service that has not been written. Checking it now is what stops a broken foundation being built on.

Where it runs, settled on 2026-09-01. The original step 2 said "with any device or emulator selected", which assumed a device target that is not set up. Asked directly, you chose the Pixel 6 over Wi-Fi rather than an emulator or a compile-only Build → Make Project. The compile-only option was the one rejected with a reason worth keeping: it proves the Kotlin compiles and proves nothing about whether Gson finds the config in the assets at runtime or whether the panel draws, which is two thirds of what this item exists to answer.

The pairing in steps 1–4 below is the same pairing as steps 1–2 of [install-and-enable-on-pixel]. Once this item is done, that one starts at its own step 3.

The walkthrough:
1. On the Pixel 6, check whether **Developer options** is listed under Settings → System. If it is not, go to Settings → About phone and tap **Build number** seven times — a message counts down and then says you are a developer. Look for: Developer options now appearing under Settings → System.
2. Open Developer options, turn **Wireless debugging** on, then tap its name rather than its toggle to open it. Look for: an entry reading **Pair device with QR code**.
3. In Android Studio, open the `android` folder inside the Hexboard project and wait for the Gradle sync to finish — a progress bar runs along the bottom. Then open the device dropdown in the top toolbar and choose **Pair Devices Using Wi-Fi**. Look for: a window showing a QR code.
4. On the phone, tap **Pair device with QR code** and point the camera at that code. Look for: the Pixel 6 appearing by name in Android Studio's device dropdown.
5. With the Pixel 6 selected in the dropdown, click the green ▶ Run button. Look for: the Build panel along the bottom. A compile error names a file and a line — report that text if it comes.
6. If it runs, the phone shows the words **Tap the keys** at the top and the QWERTY panel at the bottom: circular keys in zig-zag rows, with the two space bars in the bottom row. Tap a few keys and check that the text at the top matches what you aimed at.
7. Report three things: whether it compiled, whether the keys drew, and whether the characters that arrived were the right ones.

What step 6 describes was confirmed against `MainActivity.kt` on 2026-09-01 rather than assumed: the screen holds a `Text` reading "Tap the keys" until something is typed, with `KeyboardPanel` beneath it, and each key press appends its output character.

If it compiles but crashes on launch, the likely cause is the config not being found in the assets — the Gradle copy task that puts it there is in `android/app/build.gradle.kts` and the app reads it by the filename `key-layout.json`.

Placed after [run-key-config-validator] in the cleared region because both are Android Studio jobs done in one sitting.

#### [user] Look at the three row-3 arrangements and pick one [row3-space-choice]
Cleared on 2026-09-01. [row3-space-candidates] shipped on 2026-08-21 and its record says the three arrangements were rendered and confirmed in a browser, so the thing this step looks at exists and has been seen to draw. What the choice rests on is that preview page — `planning/layout-preview.html`, verified on 2026-08-21 — and nothing since then has touched it.
Split out of [left-space-relocation] on 2026-08-20. This is the deciding step, and it is genuinely yours: the trade-off is between a matched-looking pair and equal thumb reach, which is a judgment about how the keyboard feels rather than anything that can be computed.

The walkthrough:
1. Open `planning/layout-preview.html` by double-clicking it — it is a plain page and needs no server. Look for three rows stacked down the page, each labelled with its column numbers.
2. Compare them with your thumbs where they would actually sit holding a phone. The current arrangement is the 4-and-6 row; the other two are the candidates.
3. Say which you want: 2-and-6 (matched height, off-centre by one) or 3-and-6 (even reach, spaces at different heights) — or that today's 4-and-6 is fine after all, which is a real answer and closes the whole thread.

Your choice releases [left-space-relocation], which applies it to the config.

--- Cleared to run above this line ---

#### Register Hexboard as an Android input method [first-installable-build]
Blocked by: [compose-keyboard-renders-config]
Filed on 2026-08-14 during planning, alongside [compose-keyboard-renders-config], for the same reason: [verify-a11y-ondevice] waited on an installable build with no queue item to name as its blocker.

Designed out on 2026-08-20, and narrowed by a split. The threshold is not keys on screen — that is the rendering item — but Android accepting Hexboard as an input method the user can pick. Confirmed by reading `android/app/src/main/AndroidManifest.xml` during that session: there is no service of any kind declared today, only the launcher activity. SPEC puts IME polish (settings screen, language switching) out of scope for early iterations, so the target is the minimum that lets you type on the thing.

The build:
- `HexboardImeService.kt` (new) — an `InputMethodService` whose input view is a `ComposeView` hosting the panel from [compose-keyboard-renders-config], committing each key's output character through the current input connection. **Set the lifecycle, saved-state and view-model owners on that view explicitly**: Compose inside an `InputMethodService` has no owners by default and crashes the first time the keyboard is shown. This is the known trap and it is written here so the build does not rediscover it.
- `AndroidManifest.xml` — a `<service>` declaration with `BIND_INPUT_METHOD`, an intent filter for `android.view.InputMethod`, and meta-data pointing at the descriptor below.
- `res/xml/method.xml` (new) — the input-method descriptor and its subtype.
- `res/values/strings.xml` — the label Android shows in the keyboard picker.
- `MainActivity.kt` — a button opening the system's input-method settings, so switching Hexboard on is findable rather than hunted for.

Held below the line because there is nothing to host before keys render. Building and installing the APK is not Claude's — no `adb` on this machine and Gradle cannot run here — so that half is split out as [install-and-enable-on-pixel], which this item releases.

Files: `android/app/src/main/AndroidManifest.xml`, `android/app/src/main/res/values/strings.xml`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, and two new files (`HexboardImeService.kt`, `res/xml/method.xml`).

#### Verify every key in the config actually renders and emits its character [android-key-audit]
Blocked by: [compose-keyboard-renders-config]
Captured by you. Rewritten during planning as the second half of a split; the validator half is [key-config-validator].

Original framing was to confirm the Kotlin keyboard's character set matches the manifest. The [layout-config-source] decision removes that need: the app reads `resources/key-layout.json` directly, so its key set *is* the config and the two cannot disagree. What remains worth checking is wiring — a key correctly declared in the config can still render nothing, render in the wrong slot, or emit the wrong character.

The build: an instrumented or Compose UI test that walks every key in the config, asserts a key node exists at the expected panel, row and column, and asserts that activating it emits exactly the character the config declares. Long-press accents get the same treatment — each accent in a key's list is reachable and emits its own character. Failures name the character and its panel position.

This is what manifest rule 1 — verify the shipped key set before shipping — actually means once the config is authoritative: not a comparison of two lists, but proof that the one list reaches the screen intact.

Lift-condition: cleared to run once the Compose keyboard renders keys from the config, since there is nothing to drive until keys exist on screen.

#### Apply the chosen row-3 space arrangement to the key config [left-space-relocation]
Blocked by: [row3-space-choice]
Reduced to the applying step on 2026-08-20, when the item was split three ways. Putting the candidates on screen is [row3-space-candidates] and choosing between them is [row3-space-choice]; what stays here is writing the winner into the config. The finding that forced the split — that two space bars cannot be both edge-symmetric and at the same height, because the zag rule keys on column parity — is recorded in full on [row3-space-candidates].

One of the item's two original open questions answers itself, and the answer is worth having before the choice comes back. Both candidates are **swaps, not vacancies**. Moving the left space to col 2 displaces the comma and frees col 4, so the comma takes col 4. Moving it to col 3 displaces `!` and frees col 4, so `!` takes col 4. Either way nothing is left empty, so SPEC's rule that a freed slot must be filled with a character that has no other home never fires — there is no freed slot. If you pick today's 4-and-6 after all, this item is deleted rather than built.

The build: edit row 3 of the `qwerty` panel in `resources/key-layout.json` to the chosen arrangement, then regenerate `resources/key-manifest.md` by running `scripts/generate-key-manifest.py` rather than hand-editing it, as the config's own `about` field requires. The character set is unchanged — every key that exists still exists, in a different column — so SPEC's no-key-may-be-lost and no-unresolved-duplicates rules are satisfied by construction, and the commit message carries the move per the no-silent-changes rule.

Files: `resources/key-layout.json`, `resources/key-manifest.md`.

Original framing, kept for the record. Carried out of `hexboard-plan.md` on 2026-08-07, when that document was folded into SPEC and deleted. It was the one open question in it that nothing else records, so deleting the file as-is would have lost it.

The two space bars in QWERTY row 3 currently sit at col 4 and col 6. Confirmed against `resources/key-layout.json` during this planning session: left space is still at col 4, and the comma is still at col 2. Because col 4 and col 6 are adjacent-but-one rather than symmetric about the row, the two spaces are not equidistant from the screen edges, so the left-thumb key is further from the left thumb than the right-thumb key is from the right thumb. Moving the left space to col 2 makes the pair roughly symmetric, one comfortable per thumb.

Two questions the move opens, and they are the reason this is not a one-line change. The comma currently at col 2 has to go somewhere. And col 4 is then vacant — which SPEC's manifest rules say is an opportunity to be filled deliberately with a character that has no other home, not a gap to leave. Both need agreeing before any edit to the config.

Not designed enough to build. It needs a layout-preview session: edit the `LAYOUTS` block in `planning/layout-preview.html` to try candidate arrangements and look at them, rather than reasoning about column numbers in the abstract.

Filed after `e6742fc`.

#### Dictation test screen calling Android's on-device recogniser [ondevice-recogniser-test]
Blocked by: [compile-and-view-panel]
Filed on 2026-09-02, out of the question of whether Hexboard's dictation can match Gboard's. The research is `workshop/resources/research/gboard-speech-correction.md`, and its central unknown is this: Gboard uses Google's own recogniser, Hexboard would call Android's public on-device one, and nothing found establishes whether those are the same engine on a Pixel. If they are, Hexboard starts at Gboard's recognition quality for nothing; if they are not, the gap is inside recognition where no transcript-level correction can reach it. That difference decides whether a correction feature is worth designing at all, so it is settled by measurement before any design.

It rides the existing app rather than waiting for the keyboard. This needs no input method service and no keyboard: `MainActivity` is an ordinary app screen that already exists, so a button and a text area are enough to exercise the recogniser. That is what makes the answer available now rather than several items downstream.

The build:
- `MainActivity.kt` — a test screen: a button that starts on-device recognition and shows the returned text, and a line reporting what `SpeechRecognizer.isOnDeviceRecognitionAvailable()` returned, which also answers whether this handset has on-device recognition at all.
- `AndroidManifest.xml` — the `RECORD_AUDIO` permission and the runtime request for it.

**This scaffolding must not reach a shipped build.** The microphone permission belongs to [in-keyboard-voice-input], which introduces it properly with the press-and-hold control and the guarantees around it; a test screen carrying it is fine on your own handset and is not something to publish. Removing both is part of that item's work rather than a trailing step here.

The observation that shows it landed: on the Pixel 6, the screen reports whether on-device recognition is available, and pressing the button and speaking a sentence puts recognised text on screen.

Cites research: `workshop/resources/research/gboard-speech-correction.md`.

Rests on: `SpeechRecognizer.isOnDeviceRecognitionAvailable()` and `createOnDeviceSpeechRecognizer()` existing from API 31 and requiring `RECORD_AUDIO`, read from Android's documentation on 2026-09-01 and recorded in `workshop/resources/research/android-voice-input-and-accents.md`; that `MainActivity` is a plain Compose activity, read on 2026-09-01.

Held against [compile-and-view-panel] because it changes the same untried app and wants the Pixel already paired, which that item does.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, `android/app/src/main/AndroidManifest.xml`.

#### Horizontal swipe between the three letter panels [panel-switch-gestures]
Blocked by: [compile-and-view-panel]
Split out of [compose-keyboard-renders-config] during the /plan session of 2026-08-20, which kept the rendering half and returned this half here. The rendering item draws one panel; this is what makes RARE, QWERTY and SYMBOLS reachable from each other, with QWERTY as home per SPEC.

Design progress made before the split, so the next session starts further along. Two of the three questions the original item raised are settled and belong to the rendering half: the zag rule gets one Kotlin home ported from `planning/layout-preview.html`, and `hexboard17.html` is treated as specification for layout, gestures and key inventory exactly as SPEC already says. What was not answerable in that session is this one — where panel switching lives.

That question is genuinely architectural rather than a detail. It decides whether a panel is a screen the keyboard navigates between or a slice of one continuously drawn surface, and that choice reaches how state is held, how a swipe is disambiguated from a key press near a panel edge, and whether an in-flight drag can cross a boundary. SPEC already rules out one thing: a mis-tap cannot cross a swipe boundary, recorded in [uniform-neighbours-predictive].

What would settle it: a rendering surface that exists, so the gesture can be tried against real keys rather than reasoned about. So this waits on the rendering half rather than on a decision anyone can make now.

Designed out on 2026-09-01, and the architectural question is answered by reading the code rather than by preference. `KeyboardPanel.kt` puts its tap detector on the board, not on the individual keys, and it has to: SPEC requires nearest-centre routing, so a tap goes to the closest key centre rather than to whichever circle contains it. Panel switching therefore cannot be a per-key concern and must wrap the whole board. That settles the screens-versus-one-surface question in favour of a horizontal pager holding three pages, one `KeyboardPanel` each, opening on QWERTY — which also keeps the drag-follows-finger feel `hexboard17.html` has, rather than a hard cut between panels.

The known trap, which is why this item exists in this shape. `detectTapGestures`, which the board already runs, consumes the pointer-down event, and a consumed down starves whatever else is contending for the gesture — so the obvious construction is exactly the one reported to leave the pager unable to swipe. Two established remedies: a hand-written tap detector that detects without consuming, or having the parent detect its drag in Compose's Initial pass, which runs ahead of the child's Main-pass detector. Both live in `KeyboardPanel.kt`, so which one is needed does not change the file list — it is discovered by running the thing, not decided at a desk.

Cites research: `workshop/resources/research/compose-pager-vs-board-tap-gesture.md`, which carries the sources and states plainly that none of it was executed.

Two alternatives were never investigated rather than ruled out, and are recorded so nobody assumes they were weighed: drawing all three panels on one continuously offset surface with a single detector handling both tap and drag, and switching on a discrete fling with no drag-follows-finger, which would sidestep the gesture contention entirely.

The build: wrap the three panels in a horizontal pager opening on QWERTY, with panel state held at the surface rather than inside a panel, and apply whichever of the two remedies the run shows is needed.

The observation that shows it landed: on the Pixel 6, a horizontal swipe moves between RARE, QWERTY and SYMBOLS, and a tap that does not travel still types its key — including near a panel edge, which is where the two behaviours compete.

Rests on: `KeyboardPanel.kt`'s board-level tap detector, read 2026-09-01; the consumed-down behaviour of `detectTapGestures`, read from documentation on 2026-09-01 and not executed; the project's Compose version, which the research does not pin down and which the build should read off `android/app/build.gradle.kts` before trusting any of it.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`.

Shares `KeyboardPanel.kt` with [key-press-feedback], which is held against the same item. Whichever is built second should expect the other's changes to be there already. They also meet in one place rather than merely coexisting: a gesture that turns into a panel swipe must not leave a key stuck lit, which is stated on both.

Held below the line against [compile-and-view-panel] rather than against the rendering item, which shipped. The old wording said this waits on "a rendering surface that exists" — the surface exists as code, but nothing has compiled or run it, and the observation above is a thing seen on a phone. The compile check is that release condition made into an item that can actually resolve.

#### Key press feedback — the pressed key lightens and fades back [key-press-feedback]
Blocked by: [compile-and-view-panel]
Captured by you on 2026-09-01 and designed with you in the same session. Your words for the problem: the keyboard as it stands is a bit inert.

**What was decided, and it is narrower than the original ask.** You first described touches showing and fading on each press alongside the key highlight. Presented with the choice, you chose the key highlight alone: the pressed key lightens instantly, holds, then fades back, and nothing marks where the finger actually landed.

**Why the touch-point marker lost, recorded because it is the more obvious of the two and will look like an oversight.** The board's tap handler resolves a tap by nearest centre, so the touch target is larger than the drawn circle and a tap landing between two circles still goes somewhere definite. A marker at the real touch point would therefore show the gap between where you aimed and which key won. That is honest feedback, and it is exactly the feedback SPEC's perceptual wedge does not want: the wedge is about aiming *confidently* at circles, and a display that draws the eye to near-misses the routing already absorbed works against it. The highlight alone still answers "did that register, and on which key".

**The build:** in `KeyboardPanel.kt`, hold per-key press state at the board level — which is where the tap is already resolved — and render the resolved key with a lighter fill that animates back to its normal colour. Concurrent presses each animate independently, because a fast typist starts the next press before the last has finished fading. A gesture that turns into a panel swipe rather than a tap must not leave a key stuck lit.

**Timing, to be set by eye rather than by argument.** Start with the highlight instant, a brief hold, and a fade of about 150ms. These are opening values; you will have the keyboard in front of you when you run [compile-and-view-panel], and adjusting them on sight is the point rather than a fallback.

The observation that shows it landed: on the Pixel 6, tapping a key visibly lightens that key and it fades back, and typing quickly shows several keys fading at once rather than one cancelling another.

Held below the line against [compile-and-view-panel] rather than cleared. This changes code that has never been compiled, and stacking a second unverified change on the first means debugging both together if the compile fails. The cost was named to you and accepted: the effect appears on the phone a sitting later than it otherwise would. Placement interacts with [panel-switch-gestures], which is held against the same item and touches the same file — whichever is built second should expect the other's changes to `KeyboardPanel.kt` to be there already.

Rests on: `KeyboardPanel.kt` resolving taps at board level by nearest centre, read 2026-09-01; SPEC's perceptual wedge, which is what rules out the touch-point marker.

Files: `android/app/src/main/java/tech/flintcraft/hexboard/KeyboardPanel.kt`.

#### [user] Install Hexboard on the Pixel 6 and switch it on as a keyboard [install-and-enable-on-pixel]
Blocked by: [first-installable-build]
Split out of [first-installable-build] during the /plan session of 2026-08-20. Claude writes the IME service; producing the APK and putting it on the phone is yours, and burying that in the build item's prose would have left it invisible as next-work.

Confirmed by attempt rather than assumed, at the keep-step. There is no `adb` on this machine — not on PATH, and no Android SDK platform-tools directory in either of the two places it installs to — so Claude has no route to a device even if one were connected. And Gradle cannot run here at all: it needs a loopback connection to its own daemon, which every route Claude has is blocked from making, established across four attempts on 2026-08-06 and recorded in [run-key-config-validator]. So Claude cannot build the APK either. Android Studio has neither restriction and does both in one click.

Steps 1 and 2 below are the same pairing as steps 1–4 of [compile-and-view-panel], which was cleared to run on 2026-09-01 and does the pairing first. If that item has been done, the phone is already paired and this one starts at step 3.

The walkthrough:
1. On the Pixel 6, open Settings → System → Developer options → Wireless debugging and turn it on. Look for the entry **Pair device with QR code**.
2. In Android Studio, open the device dropdown in the top toolbar and choose **Pair Devices Using Wi-Fi**, then scan the QR code with the phone. You'll know it worked when the Pixel 6 appears by name in that same dropdown.
3. With the Pixel 6 selected in the dropdown, click the green ▶ Run button. Watch the Run panel along the bottom — success reads **Install successfully finished**. An error there is a build failure, not a phone problem; report the text.
4. On the phone, open Settings → System → Languages & input → On-screen keyboard → **Manage on-screen keyboards**, and switch **Hexboard** on. Android shows a warning that a keyboard can collect what you type — that dialog is expected for any keyboard, and you have to accept it to continue.
5. Open anything with a text field and tap into it. A small keyboard icon appears in the navigation bar at the bottom right — tap it and choose **Hexboard**. You should see circular keys in zig-zag rows.
6. Report three things: whether it appears at all, whether keys respond to a tap, and whether the characters that arrive in the text field are the ones you aimed at.

If step 5 shows no keyboard icon, the service is registered but crashing on first show; the likely cause is the Compose lifecycle-owner trap named in [first-installable-build], and the Run panel's log will say so.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Blocked by: [first-installable-build], [install-and-enable-on-pixel]
Captured by you. The second blocker was added on 2026-08-20: TalkBack cannot be tested against a keyboard nobody has switched on, so this waits on the install as well as on the code that makes it installable. Once Hexboard is running on the Pixel 6, confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

#### [user] Compare dictation through Gboard and through the on-device recogniser [recogniser-gap-comparison]
Blocked by: [ondevice-recogniser-test]
Filed on 2026-09-02. The measurement half of the recogniser question, split from the build because only you have the phone and only you can judge what came out.

Why it cannot be Claude's: there is no `adb` on this machine and Gradle cannot run here, both established by attempt and recorded in [run-key-config-validator] and [install-and-enable-on-pixel]. Beyond that, the test is someone speaking, which nothing here can do.

The walkthrough:
1. Pick three passages of about thirty seconds each and write them down: one of ordinary conversational sentences, one with names and places in it, and one you will speak at your natural speed rather than dictation speed. Look for: having the text in front of you to compare against afterwards.
2. Open any messaging app, tap the microphone on Gboard's keyboard, and dictate all three. Save what comes out. Look for: three transcripts you can read back.
3. Open Hexboard's test screen and dictate the same three passages through it. Save what comes out. Look for: whether the availability line said on-device recognition was available at all, which is the first thing to report.
4. Report three things: whether the two sets of transcripts differ noticeably, where each went wrong, and whether Gboard added punctuation and capitals that the test screen did not.

What a result means, so the report is worth making: near-identical output suggests the same engine behind both doors, and the remaining work is punctuation and capitalisation rather than accuracy. A clear Gboard advantage means the gap is inside recognition, which correcting a finished transcript cannot fix.

#### [user] Read SPEC end to end and say whether it still sounds like your project [spec-coherence-readthrough]
Blocked by: [spec-principles-rework]
Filed by /rescan on 2026-09-01, from a suggestion made in passing during that session's planning and never written down.

SPEC took seven changes in one session on 2026-09-01: `key-layout.json` reworded from the canonical layout to the default layout the app ships with; a sentence saying the layout picker lives in the app's own settings; press feedback as the key's own highlight with no touch-point marker; the persistent clipboard with its hour-and-twenty rule, drag-to-bin deletion and five-minute rule for password clips; voice input held open by the thumb; voice recognition adapting to its own user with enrolment audio destroyed after adaptation; and the predictive text principle rewritten around saved words and no proper nouns. The stale "Project docs" section was removed the same day.

It changed again on 2026-09-02: the platform principle was rewritten from variants arriving by outside contribution to Hexboard shipping a layout per language copied from that language's own standard, and the key-inventory principle reworded to match. That rewrite replaced sentences written the day before, which is itself a reason to read the whole thing.

Each of those sentences was written against its own discussion and is correct on its own terms. Nobody has read the result end to end. The risk is a document that is accurate line by line and no longer reads as a description of a keyboard — a pile of rulings rather than product truth — and that is a judgment about tone and coherence rather than about facts, which is why it is yours.

**Narrowed to one question on 2026-09-02, and this is the whole change.** The item originally asked for four things. Two of them — spotting sentences that describe machinery rather than product truth, and spotting a feature that has outgrown its importance — turned out to need no judgment about what you meant, only a careful read, so they were done in that session's planning and became [spec-principles-rework]. What is left is the one thing Claude genuinely cannot do: whether SPEC still sounds like *your* project. Claude wrote most of those sentences, so it would be marking its own homework.

The walkthrough:
1. Open `SPEC.md` and read it start to finish in one sitting, without stopping to fix anything. Look for: whether it still describes one product with a point of view, or reads as a list of decisions that happen to sit together.
2. Report what you found, in whatever words come. Any rewording is ordinary planning work filed from what you report, not something to do while reading.

This names no observable that a later session could check — nothing in the world changes when it is done — so it waits until you mention it rather than being verified.

Held below the line against [spec-principles-rework], which rebalances the Principles list. Reading the document to judge its coherence is worth doing once, after that rewrite, rather than twice. That ordering is written on both items.

## Unprocessed

> Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Last session advises processing repo-go-public next [forward-advisory]
Filed at the close of 2026-09-02, replacing the spent advisory of the day before, which pointed at the same item and has done its job.

**Read this before starting a run, because the ordering is now wrong by [repo-go-public]'s own recorded reasoning.** That item sits first in the cleared region and is marked `Runs alone`, so a run reaches it and stops there having built nothing else. Its own prose records why it was placed where it is: it goes after items that correct text it publishes, because flipping the repository public publishes whatever those items have not yet fixed. Two cleared items now do exactly that and both sit behind it — [spec-principles-rework], which rebalances SPEC's principles and strips machinery out of them, and [claude-md-phase-stale], which corrects a CLAUDE.md line still telling every reader the project has no implementation. Neither existed when the placement was decided on 2026-08-14.

So the recommendation is to move [repo-go-public] below those two before running anything, which restores the rule the item already states rather than inventing a new one. That is a planning decision rather than something a run should do for itself, which is why it is here.

[repo-go-public] itself is unchanged and still irreversible in the way that matters: once the history is public it can be cloned, and making the repository private again un-exposes nothing. Whoever runs it must say plainly what becomes readable and stop for an explicit yes in the same session. The three limitations recorded on it — a personal address in commit metadata and in file content, a reworded candid line, and the FAQ in every commit so far — all stay readable in history afterwards and were each consciously accepted.

The overlap scan was run over the unprocessed work and found nothing bearing on [repo-go-public]. Everything waiting there is held by an open blocker: the predictive text engine, the clipboard, voice input, the personal voice model, the layout picker, the Russian native-reader check, the language list, and speech-output correction. The conflict this advisory names is inside the cleared region, not in Unprocessed.

#### Let a user choose which layout variant they are typing on [layout-switching]
Blocked by: [first-installable-build]
Filed by /rescan on 2026-08-20, from a requirement SPEC created the same day and nothing in the queue held: if one app ships several layouts, the user has to be able to pick theirs. It was named as an accepted cost when the platform decision was made and then not filed, which is how a feature dies in SPEC.

**Where the picker lives** was settled by you on 2026-09-01 and is in SPEC: the app's own settings, not the keyboard surface. The keyboard-surface option — a long-press or a gesture, reachable without leaving what you are typing — lost because horizontal swipe already means "change panel", so the picker would have had to find a gesture the layout has not already spent. Recorded because it is the obvious idea and will otherwise be re-proposed.

**What the picker shows**, settled with you on 2026-09-01 and narrowed on 2026-09-02: layouts grouped by language, ordered within each language by a set position rather than by anything counted. Usage telemetry was the alternative for ordering and lost outright — it is the only true measure of popularity and it would have been the first thing in Hexboard to report what a user does back to a server, against every other feature's posture. Nothing counts anything.

Two things this picker was going to show and now will not, recorded because both were designed and then removed rather than forgotten: a link to whoever made the layout, and layouts the user built on the device sitting alongside the shipped ones. Both went on 2026-09-02 with the layout editor, when you settled that Hexboard ships layouts copied from each language's own standard and nobody builds their own.

**The data it needs is [variant-language-fields]**, split out on 2026-09-01 and cleared to run, so every layout written carries the fields before there is a picker to read them. That ordering is written on both items.

What is still open, and none of it is a desk decision: how the app enumerates the available layouts at runtime, and where it remembers the choice. Both want a keyboard that runs.

Now held against [first-installable-build] rather than against [variant-schema], which shipped on 2026-09-01. The hold is the same in substance and more honest about what it waits for: there is no settings screen to add a picker to, because the app has no IME service at all today. It stays in Unprocessed rather than below the cleared-to-run line for that reason — what a build would change still cannot be stated. Nothing here is waiting on you.

Interacts with [panel-switch-gestures]: horizontal swipe already moves between the three letter panels, so whatever switches layouts must not collide with a gesture that already means something.

SPEC puts language switching out of scope for early iterations while the platform principle says the app must *eventually* let a user switch layouts, so the deferral is what SPEC asks for rather than a delay against it.

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Red flag · State: cleared
Blocked by: [install-and-enable-on-pixel]
Captured by you, and sharpened during the /plan session of 2026-08-06.

Two decisions of yours on 2026-09-01 supersede the storage half of the 2026-08-20 design. Both are now in SPEC.

**No proper nouns in the shipped dictionary.** Your reason, in your own framing: a word being corrected into a name is a particularly irritating failure. This is a property of the word list rather than of the engine, so it becomes a criterion the dictionary research has to satisfy — most frequency-ranked lists include names, so one that excludes them or can be filtered is what the search is for.

**Saved words, which reverses "stores nothing".** The 2026-08-20 decision was a fixed dictionary learning from nothing and recording nothing, chosen so that the engine never holds a record of your writing on the device — the exposure Android's own keyboard warning is about — and that choice is what cleared the flag above. You were told plainly on 2026-09-01 that saving words means the device holds a file of words you typed, that this is smaller than silent learning because each word is a deliberate act rather than a harvest, and that it stops the "stores nothing" sentence being true. You chose the saved-word store, and the reason is the obvious one: a keyboard that cannot learn a surname or a street name is worse to use.

So the flag stays **cleared**, but on a different footing, and the change of footing is the thing to notice. It was cleared by designing the risk out; it is now cleared by your informed consent to a smaller risk, recorded above. Three constraints came with that consent and bound the build: the list grows only when you deliberately save a word, it never leaves the device, and you can read and delete it. A later session must not read this flag as cleared-by-design-out and quietly drop those.

The one thing this does not settle is whether a saved-word list needs anything further — an export, a lock, a way to clear it wholesale. That is a design question for the session that builds this, not something to guess at now.

Reshaped on 2026-08-20 by your design decision, which supersedes the mechanism described further down rather than adding to it. Correction fires **on the space bar**, not per keystroke: at that moment the whole word is in hand, it is compared against the dictionary, and it is replaced by the closest match — ties broken by which word is more common.

Why this is the stronger design, recorded because the earlier one reads persuasively and would otherwise be restored. The forwards-from-seven-candidates mechanism below exists to work around reading a word before it is finished; it treats the first letter as the hard case because a prefix lookup has nothing else to go on. Correcting at the word boundary dissolves that: no letter is trusted more than any other, so the first-letter problem — your original complaint about autocomplete — stops being a special case rather than being compensated for. It is also far simpler, needing no per-keystroke candidate UI.

"Closest match" is where the geometry pays off, and it needs stating precisely: substituting a key for one of its six neighbours is a near-miss and costs little, any other substitution is a real difference and costs a lot. That is a neighbour-weighted edit distance, and it is exactly what no general-purpose library can compute, because none of them know which keys touch which. The argument for building rather than borrowing survives the change of mechanism intact.

Two guards, agreed at the same time. A word already in the dictionary exactly is never corrected, or the engine mangles deliberate spellings. And a correction must be revertable by pressing backspace immediately after it lands, because silent unrevertable autocorrect is the most resented behaviour keyboards have.

The privacy question, raised by Claude in that session and settled by you in the same exchange, which is what clears the flag above. A predictive engine improves by learning from what its user types, and that means storing a record of your writing on the device — the thing Android's own warning dialog cautions about whenever a keyboard is enabled. Your decision: a fixed dictionary only, learning from nothing and recording nothing. The risk is designed out rather than accepted, at a known cost in accuracy.

Still not designable, and left in Unprocessed for that reason alone. What settles it is a build — SPEC holds predictive text until the keyboard works, and there is nothing to correct into until there is something to type on.

The word-list lookup this item named as a later session's job was done on 2026-09-01 instead, and is filed as `workshop/resources/research/word-list-licence-and-frequency.md`. Its answer in one line: SCOWL's licence is permissive enough to ship inside this public repository under PolyForm Noncommercial, and it separates proper names into a category that can simply be left out at build time, which satisfies the no-proper-nouns rule without anyone writing a filter. What SCOWL lacks is frequency data; its size levels are a coarse commonness ranking that may serve for tie-breaking, and if a real table is wanted, Leipzig (CC BY) raises no ShareAlike question where wordfreq (CC BY-SA) does. The finding also names one trap by name, so nobody walks into it twice.

Rests on: SCOWL's licence and its proper-names category, read from its own readme on 2026-09-01; the Leipzig licence, reported second-hand in that finding rather than read off Leipzig's terms, and to be checked before anything is bundled.

In a hexagonal tessellation every key sits the same distance from each of its neighbours, and each interior key has exactly six of them. A standard rectangular keyboard doesn't have this property: horizontal neighbours are closer than diagonal ones, so the set of plausible mis-taps is uneven and direction-dependent. On Hexboard, for any key pressed there are exactly six other keys the user might have meant, equally likely by distance alone — a clean, uniform confusion set.

The user's reason for caring about this: their biggest complaint about autocomplete is that getting the first letter wrong is far worse than getting any later letter wrong, because ordinary autocomplete is a prefix lookup that only reads words forwards and so treats the first letter as certain. The neighbour set dissolves that. Rather than trusting the pressed key, the engine searches forwards from all seven candidates — the pressed key and its six neighbours — and ranks results by geometric plausibility times word frequency. Prefix-trie walks are cheap enough that seven of them cost nothing noticeable. Searching the letters backwards, the fix SPEC previously recorded, remains useful for typos later in a word, but is no longer what rescues the first letter.

This is the argument for building the engine rather than adopting a library: no general-purpose library knows the key geometry, so none can exploit any of it. Neighbour sets come from `resources/key-layout.json` rather than being hand-maintained.

Three things to check before relying on it, recorded so they aren't discovered late. Keys at a panel's edge have fewer than six neighbours, so the uniform case is the interior one. The two space bars in row 3 are not ordinary circles and won't fit the neighbour model cleanly. And the property holds within a panel, not across panels — a mis-tap can't cross a swipe boundary.

A fourth thing to know before designing this, added on 2026-08-07. The neighbour sets are derivable from `resources/key-layout.json`, but not from it alone. The config carries each key's `row` and `col` and nothing more — geometry is deliberately excluded from it, because the zag rule (odd columns sit half a key lower) is the perceptual wedge SPEC calls inviolable and so lives in Kotlin. Which six keys neighbour a given key depends on that zag parity, not just on row and column. So the neighbour table is computed by the app at runtime from the config plus the zag rule; it is not a lookup the config can hold, and nothing should be designed on the assumption that reading the config is sufficient.

Not yet designed enough to build — what exists is the insight and the approach, not a description of what any build would change. It needs a later /plan to turn into buildable work, and SPEC holds predictive text until after the first working keyboard anyway. The three caveats above are the known starting points for that design session.

Filed after `a42cd01`.

#### Clipboard history that persists, with a screen and drag-to-bin deletion [persistent-clipboard]
Red flag · State: cleared
Blocked by: [first-installable-build]
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

Held in Unprocessed against [first-installable-build] because a keyboard with no input method service cannot read the clipboard at all, and because what a build would change cannot be stated yet: there is no clipboard screen to add to, no panel structure settled to reach it from, and the screen's relationship to the panels that [panel-switch-gestures] governs is undesigned. Nothing here is waiting on you.

Rests on: `EditorInfo.inputType` exposing password variations to an input method, and `ClipDescription.EXTRA_IS_SENSITIVE` existing from API 33 with a usable string constant below it — both read from Android's documentation on 2026-09-01, neither run.

#### Voice input inside the keyboard, held open by the thumb [in-keyboard-voice-input]
Red flag · State: cleared
Blocked by: [first-installable-build]
Captured by you on 2026-09-01 and designed with you the same session. Your requirement, and the reason the feature exists at all: people should not have to switch to another keyboard to dictate, because they switch away and never switch back.

**That requirement is satisfiable, and this was checked rather than assumed.** An input method can run recognition itself — `SpeechRecognizer.isOnDeviceRecognitionAvailable()` and `createOnDeviceSpeechRecognizer()` exist from API 31, with on-device recognition forced from API 33 — and it needs the `RECORD_AUDIO` permission. Two existing keyboards, WhisperInput and Transcribro, are built this way, so this is a trodden path rather than a hopeful one. Read from Android's documentation and those projects on 2026-09-01; nothing was run.

**The privacy risk, raised by Claude and settled with you in the same exchange, which is what clears the flag above.** A keyboard requesting microphone permission is on its face indistinguishable from a keyboard that listens to you, and this repository is public and will be read by people deciding whether to trust it. `RECORD_AUDIO` on an input method is exactly what a malicious keyboard would ask for. Four things answer that, and three of them are design rather than assurance:

- **Recognition is on-device and audio never leaves the phone.**
- **Where a device cannot recognise on-device, voice input is simply unavailable.** This is the conservative arm of a real fork and it costs something: the project's minimum is API 26, and the on-device recogniser starts at 31, so devices between those levels get no voice input at all. The alternative was falling back to the network recogniser, which would send audio off the device — rejected, because a keyboard that quietly ships your voice somewhere on older hardware makes every other claim here worthless.
- **The microphone is open only while the control is held down.** Your choice, from a straight comparison with tap-to-start-tap-to-stop. The reason it won: "the microphone is open only while you are holding the button" is a claim a stranger can verify by using the keyboard, rather than one they have to take on trust from an indicator and a timeout. The cost was named and accepted — press-and-hold is tiring for anything longer than a sentence and you cannot dictate while looking away, so comfortable long-form dictation is a later question, not this item's.
- **No audio is retained once the words are transcribed**, and the mic is visibly indicated while open.

**The control grows while held and shrinks when fully released.** Yours, on 2026-09-01, and it answers the objection to press-and-hold rather than restating it: the button is hard to keep hold of while you are moving about, and a thumb drifting slightly off it loses the message halfway through. Enlarging the control the moment it is held makes the target forgiving exactly when forgiveness is needed, and shrinking it back on release keeps the resting keyboard uncluttered. It is the project's own thesis — a generous touch target — applied to the one control where losing your grip costs a whole sentence rather than one character. What still needs settling at build time is what counts as "fully released", since the point of the growth is that small movements must not end the recording.

**What is not designed, and why this sits in Unprocessed rather than below the line.** Where the mic control lives is unsettled: whether it is a key declared in the layout config like any other, or a control belonging to the board rather than the key inventory. That reaches `resources/key-layout.json` and the manifest rules if it is a key, and reaches nothing if it is not, so no honest file list can be written yet. It also touches whatever [panel-switch-gestures] settles, since a press-and-hold on the board has to be told apart from a swipe and from an ordinary tap.

**A defeated alternative, recorded so it is not revived.** An accent picker — offering the English varieties a device supports, presented as accents rather than locales — was designed and then dropped by you on 2026-09-01. It reached only speakers who happen to match one of a short list organised by where a variety of English is spoken natively, and missed the case you actually raised: a second-language English speaker in a multicultural country, who matches no entry. Your verdict was that it compares poorly with training on the user's own voice, which is [personal-voice-model]. Recognition here therefore uses the language the phone is set to, with no accent setting of its own.

Cites research: `workshop/resources/research/android-voice-input-and-accents.md`, which carries the API levels, the API 26–30 gap, the recognisable English varieties and why that list cannot answer an accent complaint.

Two things this item inherits from work filed on 2026-09-02. [ondevice-recogniser-test] puts a temporary dictation screen and the `RECORD_AUDIO` permission into `MainActivity` to measure the recogniser early; **removing both belongs here**, since this item is what introduces the microphone properly, and a test screen carrying that permission must not survive into anything published. And [recogniser-gap-comparison] measures how the platform recogniser compares with Gboard's, which tells this item whether the recognition it gets is good enough to build the press-and-hold control around at all. That ordering is written on all three items.

Rests on: the on-device recogniser's API levels and the `RECORD_AUDIO` requirement, read from Android's documentation on 2026-09-01; that on-device recognition genuinely keeps audio on the device, which is what the API claims and what a build should confirm before the README repeats it.

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

#### [user] Have a Russian reader check the ЙЦУКЕН layout before it ships [russian-layout-check]
Blocked by: [language-starter-layouts]
Filed on 2026-09-02, at the moment the Russian layout was designed. The row contents are copied from the published standard and are checkable against sources, so they are not the risk. Two things in that layout are Claude's own reading and are not copied from anywhere: that the hard sign Ъ belongs behind a long-press on the soft sign Ь, and that Щ belongs behind Ш. Both are plausible pairings from the shape of the writing system and neither was verified by anyone who types Russian.

Why this cannot be Claude's. Whether a pairing is guessable rather than merely defensible is a judgment only a reader of the language can make, and the research this rests on came from English-language explainers of the Russian layout rather than from Russian typists — a limit recorded in `workshop/resources/research/cyrillic-overflow-and-slot-budget.md` rather than glossed over.

Why it matters more than it looks. A shipped layout is copied rather than read: it becomes the thing later layouts and later contributors imitate, so an error in it propagates rather than sitting still.

**Narrowed on 2026-09-02.** This walkthrough opened by having you repoint the preview page's `LAYOUTS` block at the Russian config — an edit to JavaScript, which Claude can make and which therefore should never have been yours. It moved into [language-starter-layouts], so by the time you reach this the board is already there to open.

The walkthrough:
1. Open `planning/layout-preview.html` by double-clicking it. Look for: a board of Cyrillic letters in the usual zag rows, with the Russian layout among those the page offers.
2. Show it to someone who types Russian. Ask them one question — is every letter where they expect it? Look for: hesitation over any particular key, which tells you more than a yes does.
3. Ask them about the two hidden letters: would they think to hold Ь to get Ъ, and Ш to get Щ? Look for: whether they guessed before you explained.
4. Report what they said. A rejected pairing is ordinary planning work filed from your report, not something to fix while you are with them.

The observable that shows this is done is the report itself, so this item waits until you mention it rather than being checked against anything in the world.

[language-list-choice] is held against this item, and the reason is worth knowing while you run it: how much trouble it is to find a reader and get an answer is the fact that decides how many further languages are worth committing to. So note what the arranging cost, not only what they said. That ordering is written on both items.

#### Which languages get a layout, and in what order [language-list-choice]
Blocked by: [russian-layout-check]
Filed on 2026-09-02, when you replaced the contributor-facing editor with layouts copied from each language's own standard. Russian is being done first as the hard case, in [language-starter-layouts]; this is the decision about what follows it.

**Half of it is already answered and is in SPEC:** layouts are added as they are asked for. You settled that on 2026-09-02, and the standing policy is demand-driven rather than a planned rollout. What stays open is only whether there is a first batch beyond Russian.

**Held against [russian-layout-check] on 2026-09-02, and the reason is the thing this decision is missing.** Each language costs three things: finding its standard mobile layout, mapping it onto 30 letter slots with the overflow rule, and a check by someone who reads it. The first two are Claude's and are cheap. The third is the bottleneck, and it does not scale with how many people speak a language — it scales with whether a reader of it can be found to look. A language with a hundred million speakers and nobody to check it costs more than one with five million and someone willing. Nothing has tested that yet: [russian-layout-check] is the first attempt this project makes at getting a native reader to look at a layout, and how much trouble it is to arrange is what should set the size of any list. So the item returns by itself carrying the one fact it lacks.

An alternative was offered and not taken up now: researching which alphabets fit 30 slots and which overflow — Greek at 24 letters and Hebrew at 22 being transcription jobs, while Arabic, Vietnamese and the Indic scripts each raise their own version of the Cyrillic problem. It is useful whenever this is taken up, and it answers what is cheap rather than what is wanted, so it was not a reason to decide now.

Worth settling in the same conversation: what happens where a language has two competing standards. Russian already raises it — the phonetic ЯВЕРТЫ layout sits alongside ЙЦУКЕН and was never investigated — and Hexboard would have to either pick one or ship both as separate layouts, which the picker's ordering within a language is capable of holding.

#### Correcting what the speech recogniser returns [speech-output-correction]
Blocked by: [recogniser-gap-comparison]
Captured by you on 2026-09-02, and separate from the keyboard's own autocorrect by your instruction. Your target: dictation as good as Gboard's. Your reason for raising it: the correction Gboard does looks like AI to you, and you wanted that checked rather than assumed.

It is AI, and the check changed the shape of the feature. Gboard's on-device recogniser has been an all-neural end-to-end model since 2019, and there is no separate autocorrect stage at all: correction happens inside recognition, with an optional second pass where a language model re-ranks the recogniser's own N best guesses at the whole utterance. So a corrector applied to a finished transcript has strictly less to work with than the recogniser had — the audio is gone, and with it every alternative that was considered and rejected. It can tidy output; it cannot close a gap in recognition quality. That is why [recogniser-gap-comparison] comes first: it establishes whether there is a gap to close.

**The tension with the rest of this project, which is a decision for you and not an implementation detail.** Google's own documentation says transcripts of what the user says and types are saved on the device, and that corrections are used to improve dictation for that user. Part of Gboard's advantage is a per-user record of how someone speaks and how they fix it. Hexboard refuses that class of storage everywhere else: predictive text keeps only words deliberately saved, and enrolment audio for [personal-voice-model] is destroyed after adaptation. Matching Gboard by the same means would reverse that. Nothing is decided here, and no design should assume either answer.

Three routes are visible and none is chosen: rely on the platform recogniser and add only punctuation and capitalisation; bias recognition toward the user's saved words and contacts, if the on-device API permits biasing at all, which was not researched; or correct the transcript against the saved-word list the predictive engine already holds. Which of them are open depends on what the comparison finds.

Cites research: `workshop/resources/research/gboard-speech-correction.md`, which carries the sources and states plainly that the architecture papers are from 2019 and 2020 — sound on where correction happens, and not a description of what Gboard ships today.

