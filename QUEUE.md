# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Render one panel of keys from the config, on the real zag geometry [compose-keyboard-renders-config]
Filed on 2026-08-14 during planning, when [throughliner-doc-drift] surfaced that two held items were waiting on things in the world with no queue item to name. Filing it exposed something worth stating plainly: until then the queue had no item for building the keyboard itself. The pieces around it were all queued — the key config, its validator, the audit that checks the keys reach the screen — but not the thing they are about.

Narrowed to one panel on 2026-08-20, when the item was designed out in planning and split. The horizontal-swipe half went back to Unprocessed as [panel-switch-gestures], because where panel switching lives is an architecture question best answered against a surface that exists. What remains here is the surface: the QWERTY panel drawn from the config, which is what everything downstream actually waits on.

Two of the item's three original questions were settled here rather than deferred. The zag rule gets exactly one home in Kotlin — SPEC calls the geometry inviolable and the config's own `about` field already excludes it — and it is ported from `planning/layout-preview.html`, which carries the working parity and circle maths (`par(c)`, `pt(col)`) lifted from the prototype. And `hexboard17.html` is treated as specification for layout, gestures and key inventory, which is what SPEC already says of it; the porting question was never open.

The plumbing is further along than the item assumed. `android/app/build.gradle.kts` already copies `resources/key-layout.json` into the app's assets at build time, so the config reaches the APK with no second copy checked in. What it lacks is Gson on the main source set — it is `testImplementation` only today.

The build:
- `android/app/build.gradle.kts` — promote Gson to `implementation` so the app can parse the config it already ships.
- `KeyLayout.kt` (new) — data classes matching the config's panel/row/key shape, and a loader that reads `key-layout.json` from assets.
- `KeyGeometry.kt` (new) — the single home of the perceptual geometry: column parity, key centre from row and column, radius solved to the available width, touch target larger than the drawn circle, uppercase at 0.92×. Nothing else in the codebase computes a position.
- `KeyboardPanel.kt` (new) — the Compose surface. Draws the QWERTY panel's circles, resolves a touch by nearest centre across all keys rather than by per-key bounds, and gives every key its own semantics node with label and bounds, per SPEC's accessibility requirement.
- `MainActivity.kt` — host the panel so it can be looked at on screen without an IME service, which [first-installable-build] brings.

Not marked `Runs alone`; it adds files rather than moving them.

Files: `android/app/build.gradle.kts`, `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, and three new files under `android/app/src/main/java/tech/flintcraft/hexboard/`.

#### Move `hexboard-editor.html` to `planning/` and record it as prior art [hexboard-editor-status]
Noticed on 2026-08-14 during the /plan close, while listing tracked files to check go-public readiness. It was seen and not asked about at the time, which is why it is filed rather than left in conversation.

`hexboard-editor.html` sits at the repo root, is tracked, and is 557 lines of a self-contained page titled "HexBoard Layout Editor". It has not been touched since the project was adopted on 2026-07-02, and nothing in SPEC, CLAUDE.md, the README or any queue item mentions it. The README points a visitor at `hexboard17.html` as the frozen prototype and at `planning/layout-preview.html` as the planning fixture; this third HTML file at the root is named in none of them.

Settled by reading the file during the /plan session of 2026-08-20. It is a working drag-and-drop key-arrangement editor — three panel tabs, real zag geometry, keys dragged between slots, structural keys locked — and its export button is what dates it. It emits JavaScript source fragments (`RAR_ROWS_EXPORT`, `SK_EXPORT`, a commented manifest summary) for pasting into the prototype's code. That targets the pre-config world, where key data lived in `hexboard17.html`. Since [layout-config-source] landed, `resources/key-layout.json` is the canonical manifest, so what this editor exports now goes to the wrong place in the wrong format.

So it is the second of the three candidate fates — a superseded experiment — with one qualification that decides what happens to it. It is also genuine prior art for [variant-editor], which wants a contributor-facing layout editor: a working drag-and-drop board on the real geometry already exists, and only the export target changed. Deleting it would discard that, so it is moved rather than removed. The user's call, on Claude's recommendation.

The build: move `hexboard-editor.html` to `planning/`, alongside `layout-preview.html`; add a line to `README.md` naming it a prototype-era editor kept as prior art for a future variant editor and not maintained; and check the README's existing HTML-file paragraph still reads correctly with a third file in it.

Cleared to go public, on your condition and on a read rather than a scan. You said during the /next run of 2026-08-14 that the editor is fine to publish as long as it doesn't carry your email or anything like that; that was filed as [editor-public-if-no-personal-details], which recorded a shape-scan and said plainly that a scan cannot settle whether some line quietly identifies a real person. The read was done on 2026-08-20 and it can. Every piece of free text in the file is structural — the page title and heading, the tagline, the key-type legend, two button labels, and section comments of the form `// ── EXPORT ──` — with the rest CSS, key data and drag handlers. The one `@` is the symbol panel's `@` key at line 238. Nothing in it refers to a person. That item was a clearance rather than work, so it was folded here and deleted.

Ordering: this sits ahead of [repo-go-public], because the whole reason it was raised is to settle the file before publication makes the root permanent reading. Not marked `Runs alone` — no other queue item names this path, so no run in flight goes stale when it moves.

Files: `hexboard-editor.html` (moved), `README.md`.

#### Put both candidate row-3 space arrangements into the layout preview [row3-space-candidates]
Split out of [left-space-relocation] on 2026-08-20, when the choice turned out to rest on a trade-off that has to be looked at rather than argued about.

QWERTY row 3 today is `⇧ ? , ! ␣ ' ␣ " . -`, with space bars at columns 4 and 6. The finding that reframes the whole thing: column parity is what the zag rule keys on, so even columns sit half a key higher than odd ones. Both spaces are even today and therefore sit at the same height, reading as a matched pair. A pair genuinely symmetric about the row means columns `c` and `9−c`, and because 9 is odd one of those is always even and the other always odd. **Two space bars cannot be both edge-symmetric and at the same height.** That is arithmetic, not taste, and it is why the original item's assumption that col 2 would make the pair symmetric is wrong.

So there are two candidates, each giving up one thing, and both better than today for thumb reach:
- spaces at cols 2 and 6 — same height, matched pair kept; left space 2 from the left edge, right space 3 from the right;
- spaces at cols 3 and 6 — exactly equal from each edge; the two spaces sit at different heights.

The build: add both arrangements to the `LAYOUTS` block in `planning/layout-preview.html`, alongside the current 4-and-6 row for comparison, labelled so they can be told apart on screen. That is the fixture's stated purpose and CLAUDE.md requires maintaining it rather than building a new previewer. No other file changes; nothing here touches `resources/key-layout.json`.

Files: `planning/layout-preview.html`.

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

#### [user] Install Hexboard on the Pixel 6 and switch it on as a keyboard [install-and-enable-on-pixel]
Blocked by: [first-installable-build]
Split out of [first-installable-build] during the /plan session of 2026-08-20. Claude writes the IME service; producing the APK and putting it on the phone is yours, and burying that in the build item's prose would have left it invisible as next-work.

Confirmed by attempt rather than assumed, at the keep-step. There is no `adb` on this machine — not on PATH, and no Android SDK platform-tools directory in either of the two places it installs to — so Claude has no route to a device even if one were connected. And Gradle cannot run here at all: it needs a loopback connection to its own daemon, which every route Claude has is blocked from making, established across four attempts on 2026-08-06 and recorded in [run-key-config-validator]. So Claude cannot build the APK either. Android Studio has neither restriction and does both in one click.

The walkthrough:
1. On the Pixel 6, open Settings → System → Developer options → Wireless debugging and turn it on. Look for the entry **Pair device with QR code**.
2. In Android Studio, open the device dropdown in the top toolbar and choose **Pair Devices Using Wi-Fi**, then scan the QR code with the phone. You'll know it worked when the Pixel 6 appears by name in that same dropdown.
3. With the Pixel 6 selected in the dropdown, click the green ▶ Run button. Watch the Run panel along the bottom — success reads **Install successfully finished**. An error there is a build failure, not a phone problem; report the text.
4. On the phone, open Settings → System → Languages & input → On-screen keyboard → **Manage on-screen keyboards**, and switch **Hexboard** on. Android shows a warning that a keyboard can collect what you type — that dialog is expected for any keyboard, and you have to accept it to continue.
5. Open anything with a text field and tap into it. A small keyboard icon appears in the navigation bar at the bottom right — tap it and choose **Hexboard**. You should see circular keys in zig-zag rows.
6. Report three things: whether it appears at all, whether keys respond to a tap, and whether the characters that arrive in the text field are the ones you aimed at.

If step 5 shows no keyboard icon, the service is registered but crashing on first show; the likely cause is the Compose lifecycle-owner trap named in [first-installable-build], and the Run panel's log will say so.

#### [user] Look at the three row-3 arrangements and pick one [row3-space-choice]
Blocked by: [row3-space-candidates]
Split out of [left-space-relocation] on 2026-08-20. This is the deciding step, and it is genuinely yours: the trade-off is between a matched-looking pair and equal thumb reach, which is a judgment about how the keyboard feels rather than anything that can be computed.

The walkthrough:
1. Open `planning/layout-preview.html` by double-clicking it — it is a plain page and needs no server. Look for three rows stacked down the page, each labelled with its column numbers.
2. Compare them with your thumbs where they would actually sit holding a phone. The current arrangement is the 4-and-6 row; the other two are the candidates.
3. Say which you want: 2-and-6 (matched height, off-centre by one) or 3-and-6 (even reach, spaces at different heights) — or that today's 4-and-6 is fine after all, which is a real answer and closes the whole thread.

Your choice releases [left-space-relocation], which applies it to the config.

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

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Blocked by: [first-installable-build], [install-and-enable-on-pixel]
Captured by you. The second blocker was added on 2026-08-20: TalkBack cannot be tested against a keyboard nobody has switched on, so this waits on the install as well as on the code that makes it installable. Once Hexboard is running on the Pixel 6, confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Last session advises starting with [compose-keyboard-renders-config] [forward-advisory]
Filed at the close of 2026-08-20. It is the first cleared item and the one the most work waits on: [first-installable-build] and [android-key-audit] both name it as their blocker, and [install-and-enable-on-pixel] and [verify-a11y-ondevice] sit behind those, so four held items are downstream of this one shipping.

It is also the first point at which Hexboard becomes something to look at rather than to read about, which matters for the items that follow it — [panel-switch-gestures] was deliberately deferred on the grounds that where panel switching lives is better answered against a surface that exists.

Two things worth knowing before the run. It was designed out in full on 2026-08-20, so its file list and what changes inside each file are already written into the item — the build should not need to re-open the design. And a run that carries on past it reaches [repo-go-public], which is marked `Runs alone` and irreversible, so the run stops there rather than continuing.

#### A contributor-facing layout editor for building language / key-set variants [variant-editor]
Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. (That decision was taken on 2026-08-20 and is recorded further down; the two sentences before this one describe the fork framing the same session rejected, and are kept as the original capture rather than as current design.) Prerequisite, now met: [layout-config-source] has landed. `resources/key-layout.json` exists and SPEC names it the canonical manifest, so the config this editor would produce and the Android build would consume is real rather than hypothetical. Confirmed during the /plan session of 2026-08-07. Far downstream of a first Android build; filed as a design thread, not near-term work.

Two things were settled on 2026-08-20, and they change what this item is.

**Hexboard is a platform for variants.** The scope decision underneath this item — one opinionated keyboard, or a platform other people build on — had been deferred three times as better made with a working keyboard in hand, and was never actually put to you. Asked directly, you chose the platform. It is now written into SPEC as a principle, so this item no longer carries a scope question.

**Variants arrive by contribution, not by fork, which dissolves this item's hardest open question rather than answering it.** The fork-sync problem below exists only because a fork copies the code; since the geometry is inviolable and only the key inventory varies, a variant is one JSON file and needs no copy of the code at all. So layouts are contributed into this repository and one app ships them all. A fix to the rendering code then reaches every variant at once and nothing has to be re-synchronised. Your reason for the choice was that you were unsure where the fork sat best in the picture; the recommendation was Claude's, on the grounds that it makes that uncertainty stop existing. The costs were named and accepted: reviewing contributed key data, and the app eventually needing layout switching, which SPEC currently defers.

The paragraph below is kept as the record of the fork question and why it lost, not as live design.

Carries an open question relocated from [licence-and-go-public] during planning: once language forks exist, how do they stay in step with canonical Hexboard as it changes? This is the part of the item that most needs outside input — it's a question about distribution and project governance rather than about the keyboard itself, and nothing already decided here settles it. It's an upstream/downstream design question — fork-and-cherry-pick, a shared config, or contribution-back terms written into the licence itself — and it interacts with [layout-config-source], since a shared machine-readable key config is one way forks track upstream without merging code. Not answerable until the config lands and a real fork exists.

Sharpened in the /plan session of 2026-08-04, then deliberately deferred rather than designed. Two things are now settled that narrow it: the editor's target format is `resources/key-layout.json`, no longer TBD; and because the config carries the key inventory only, with geometry staying in Kotlin, the editor's scope is hard-bounded to key data and structurally cannot touch the perceptual wedge. What still blocks design is that none of its subjects exist yet — no keyboard, so nothing to vary; no fork, so the fork-sync question has no real case to reason about; and the SPEC-level scope decision underneath it (whether Hexboard becomes a platform for variants rather than one opinionated keyboard) is better made with a working keyboard in hand than in the abstract.

Prior art exists and should be read before designing this. `hexboard-editor.html` — relocated to `planning/` by [hexboard-editor-status] — is a working drag-and-drop key-arrangement editor on the real zag geometry, with panel tabs and locked structural keys. Only its export is obsolete: it emits JavaScript fragments for the prototype rather than `resources/key-layout.json`. The interaction design is the expensive half and it is already built.

Re-weighed on 2026-08-07 and deferred again, with the prerequisite above now cleared. What still blocks design is unchanged and is not something a planning session can resolve by thinking harder: there is no keyboard yet, so there is nothing to vary; there is no fork, so the upstream/downstream sync question has no concrete case to reason about; and the scope decision underneath it is better made with a working keyboard in hand.

Skipped again on 2026-08-20, but for one precise reason rather than three vague ones, so the next session knows exactly what it is waiting for. Two of the three old reasons are gone: the scope decision is made, and the fork-sync question is dissolved. What remains is a schema question the second limb fails on. An editor has to write a config that identifies *which variant it is* — a language or key-set identifier, and whatever else distinguishes one contributed layout from another — and `resources/key-layout.json` has no such field today, because it was designed when there was exactly one layout. That shape cannot be settled here, because it depends on how the app holds and switches between several layouts, which is undesigned. Until then the editor's output format is unknown and no honest file list can be written.

Who owns what: the schema follows from a build, not from a decision anyone can make now. Nothing here is waiting on you.

[layout-switching] is held by this same unknown and was skipped alongside it on 2026-08-20. The two are worth processing together whenever the schema question comes up, since one answer releases both: an editor has to write a variant identifier and a picker has to read one.

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Red flag · State: cleared
Captured by you, and sharpened during the /plan session of 2026-08-06.

Reshaped on 2026-08-20 by your design decision, which supersedes the mechanism described further down rather than adding to it. Correction fires **on the space bar**, not per keystroke: at that moment the whole word is in hand, it is compared against the dictionary, and it is replaced by the closest match — ties broken by which word is more common.

Why this is the stronger design, recorded because the earlier one reads persuasively and would otherwise be restored. The forwards-from-seven-candidates mechanism below exists to work around reading a word before it is finished; it treats the first letter as the hard case because a prefix lookup has nothing else to go on. Correcting at the word boundary dissolves that: no letter is trusted more than any other, so the first-letter problem — your original complaint about autocomplete — stops being a special case rather than being compensated for. It is also far simpler, needing no per-keystroke candidate UI.

"Closest match" is where the geometry pays off, and it needs stating precisely: substituting a key for one of its six neighbours is a near-miss and costs little, any other substitution is a real difference and costs a lot. That is a neighbour-weighted edit distance, and it is exactly what no general-purpose library can compute, because none of them know which keys touch which. The argument for building rather than borrowing survives the change of mechanism intact.

Two guards, agreed at the same time. A word already in the dictionary exactly is never corrected, or the engine mangles deliberate spellings. And a correction must be revertable by pressing backspace immediately after it lands, because silent unrevertable autocorrect is the most resented behaviour keyboards have.

The privacy question, raised by Claude in that session and settled by you in the same exchange, which is what clears the flag above. A predictive engine improves by learning from what its user types, and that means storing a record of your writing on the device — the thing Android's own warning dialog cautions about whenever a keyboard is enabled. Your decision: a fixed dictionary only, learning from nothing and recording nothing. The risk is designed out rather than accepted, at a known cost in accuracy.

Still not designable, and left in Unprocessed for that reason alone. What settles it is a build — SPEC holds predictive text until the keyboard works, and there is nothing to correct into until there is something to type on. One thing a later session must look up rather than decide: the word list needs frequencies and a licence compatible with a public repository.

In a hexagonal tessellation every key sits the same distance from each of its neighbours, and each interior key has exactly six of them. A standard rectangular keyboard doesn't have this property: horizontal neighbours are closer than diagonal ones, so the set of plausible mis-taps is uneven and direction-dependent. On Hexboard, for any key pressed there are exactly six other keys the user might have meant, equally likely by distance alone — a clean, uniform confusion set.

The user's reason for caring about this: their biggest complaint about autocomplete is that getting the first letter wrong is far worse than getting any later letter wrong, because ordinary autocomplete is a prefix lookup that only reads words forwards and so treats the first letter as certain. The neighbour set dissolves that. Rather than trusting the pressed key, the engine searches forwards from all seven candidates — the pressed key and its six neighbours — and ranks results by geometric plausibility times word frequency. Prefix-trie walks are cheap enough that seven of them cost nothing noticeable. Searching the letters backwards, the fix SPEC previously recorded, remains useful for typos later in a word, but is no longer what rescues the first letter.

This is the argument for building the engine rather than adopting a library: no general-purpose library knows the key geometry, so none can exploit any of it. Neighbour sets come from `resources/key-layout.json` rather than being hand-maintained.

Three things to check before relying on it, recorded so they aren't discovered late. Keys at a panel's edge have fewer than six neighbours, so the uniform case is the interior one. The two space bars in row 3 are not ordinary circles and won't fit the neighbour model cleanly. And the property holds within a panel, not across panels — a mis-tap can't cross a swipe boundary.

A fourth thing to know before designing this, added on 2026-08-07. The neighbour sets are derivable from `resources/key-layout.json`, but not from it alone. The config carries each key's `row` and `col` and nothing more — geometry is deliberately excluded from it, because the zag rule (odd columns sit half a key lower) is the perceptual wedge SPEC calls inviolable and so lives in Kotlin. Which six keys neighbour a given key depends on that zag parity, not just on row and column. So the neighbour table is computed by the app at runtime from the config plus the zag rule; it is not a lookup the config can hold, and nothing should be designed on the assumption that reading the config is sufficient.

Not yet designed enough to build — what exists is the insight and the approach, not a description of what any build would change. It needs a later /plan to turn into buildable work, and SPEC holds predictive text until after the first working keyboard anyway. The three caveats above are the known starting points for that design session.

Filed after `a42cd01`.

#### Horizontal swipe between the three letter panels [panel-switch-gestures]
Split out of [compose-keyboard-renders-config] during the /plan session of 2026-08-20, which kept the rendering half and returned this half here. The rendering item draws one panel; this is what makes RARE, QWERTY and SYMBOLS reachable from each other, with QWERTY as home per SPEC.

Design progress made before the split, so the next session starts further along. Two of the three questions the original item raised are settled and belong to the rendering half: the zag rule gets one Kotlin home ported from `planning/layout-preview.html`, and `hexboard17.html` is treated as specification for layout, gestures and key inventory exactly as SPEC already says. What was not answerable in that session is this one — where panel switching lives.

That question is genuinely architectural rather than a detail. It decides whether a panel is a screen the keyboard navigates between or a slice of one continuously drawn surface, and that choice reaches how state is held, how a swipe is disambiguated from a key press near a panel edge, and whether an in-flight drag can cross a boundary. SPEC already rules out one thing: a mis-tap cannot cross a swipe boundary, recorded in [uniform-neighbours-predictive].

What would settle it: a rendering surface that exists, so the gesture can be tried against real keys rather than reasoned about. So this waits on the rendering half rather than on a decision anyone can make now.

#### Let a user choose which layout variant they are typing on [layout-switching]
Filed by /rescan on 2026-08-20. SPEC gained a principle that day — Hexboard is a platform for layout variants, contributed into this repository, with one app shipping them all. That principle carries a requirement nothing in the queue holds: if one app ships several layouts, the user has to be able to pick theirs.

It was named as an accepted cost when the decision was made and then not filed, which is exactly how a feature dies in SPEC. SPEC also still puts language switching out of scope for early iterations, so this is deliberately not urgent — what it must not be is absent.

Interacts with [panel-switch-gestures]: horizontal swipe already moves between the three letter panels, so whatever switches layouts must not collide with a gesture that already means something.

Skipped on 2026-08-20 rather than kept, and held by the same single unknown as [variant-editor]. `resources/key-layout.json` has no notion of more than one layout — it was written when there was exactly one, so it carries no variant identifier and no way to say which layout a set of keys belongs to. Until that shape exists there is nothing to say about what a picker reads or writes. The shape follows from a build rather than from any decision available now, so nothing here is waiting on you.

SPEC also puts language switching out of scope for early iterations and the platform principle says the app must *eventually* let a user switch layouts, so the deferral is what SPEC asks for rather than a delay against it.

#### SPEC calls one config the single source of truth, but a platform ships many [manifest-principle-vs-variants]
Filed by /rescan on 2026-08-20, from a tension created by that day's own SPEC edit rather than from anything older.

SPEC's manifest principle says `resources/key-layout.json` is the single source of truth for which characters the keyboard offers and where they sit. The platform principle added the same day says other people contribute layouts and one app ships them all. Both sentences are now in SPEC and they do not obviously agree: with several layouts present, either one file is no longer the only source, or a contributed variant is not really a layout in the sense the manifest means.

The likely resolution is that the manifest rules describe *a* layout rather than *the* layout, and each contributed variant is governed by them individually — but that is a guess, and it should be settled deliberately rather than inferred by whoever next reads the two sentences together.

It interacts with the schema question that currently holds [variant-editor] and [layout-switching], because how many files there are and what identifies each one is the same question seen from the SPEC side.

