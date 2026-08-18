# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

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

#### Verify every key in the config actually renders and emits its character [android-key-audit]
Blocked by: [compose-keyboard-renders-config]
Captured by you. Rewritten during planning as the second half of a split; the validator half is [key-config-validator].

Original framing was to confirm the Kotlin keyboard's character set matches the manifest. The [layout-config-source] decision removes that need: the app reads `resources/key-layout.json` directly, so its key set *is* the config and the two cannot disagree. What remains worth checking is wiring — a key correctly declared in the config can still render nothing, render in the wrong slot, or emit the wrong character.

The build: an instrumented or Compose UI test that walks every key in the config, asserts a key node exists at the expected panel, row and column, and asserts that activating it emits exactly the character the config declares. Long-press accents get the same treatment — each accent in a key's list is reachable and emits its own character. Failures name the character and its panel position.

This is what manifest rule 1 — verify the shipped key set before shipping — actually means once the config is authoritative: not a comparison of two lists, but proof that the one list reaches the screen intact.

Lift-condition: cleared to run once the Compose keyboard renders keys from the config, since there is nothing to drive until keys exist on screen.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Blocked by: [first-installable-build]
Captured by you. Once an Android build exists, install it on the Pixel 6 (wireless debugging) and confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Last session advises processing [hexboard-editor-status] next [forward-advisory]
It was filed at the close of 2026-08-14 and bears directly on work that is already cleared to run. [repo-go-public] sits in the cleared region, and a /next run that reaches it publishes `hexboard-editor.html` along with everything else — a tracked, root-level file that no document in the repo explains. Going public cannot be undone, so the cheap moment to decide what that file is comes before the run, not after it.

It was deliberately not written as a blocker on [repo-go-public], because whether an unexplained file is enough to hold up the flip is a judgment for a planning session with the user present, not something a close should decide alone.

Also worth knowing when planning that session: a reply is waiting in this project's INBOX from the Throughliner project, answering the report sent on 2026-08-14 about closing asks that name a slash command.

Still current at the close of the /next run of 2026-08-14, and now with one more thing to process alongside it. The run built the two text items ahead of [repo-go-public] and stopped there, because that item is marked `Runs alone` — so the flip is now the first thing a /next run would reach, and the editor question is still unsettled in front of it. The INBOX reply named above was opened and archived during that run; it needed no reply and changed no work here, so it is no longer waiting. What is new is [editor-public-if-no-personal-details], the user's condition for publishing the editor, which should be processed in the same pass as [hexboard-editor-status] since it settles that item's risk half and leaves only the question of what the file is.

#### Move the left space bar to row 3 col 2, and decide what displaces and what fills col 4 [left-space-relocation]
Carried out of `hexboard-plan.md` on 2026-08-07, when that document was folded into SPEC and deleted. It was the one open question in it that nothing else records, so deleting the file as-is would have lost it.

The two space bars in QWERTY row 3 currently sit at col 4 and col 6. Confirmed against `resources/key-layout.json` during this planning session: left space is still at col 4, and the comma is still at col 2. Because col 4 and col 6 are adjacent-but-one rather than symmetric about the row, the two spaces are not equidistant from the screen edges, so the left-thumb key is further from the left thumb than the right-thumb key is from the right thumb. Moving the left space to col 2 makes the pair roughly symmetric, one comfortable per thumb.

Two questions the move opens, and they are the reason this is not a one-line change. The comma currently at col 2 has to go somewhere. And col 4 is then vacant — which SPEC's manifest rules say is an opportunity to be filled deliberately with a character that has no other home, not a gap to leave. Both need agreeing before any edit to the config.

Not designed enough to build. It needs a layout-preview session: edit the `LAYOUTS` block in `planning/layout-preview.html` to try candidate arrangements and look at them, rather than reasoning about column numbers in the abstract.

Filed after `e6742fc`.

#### A contributor-facing layout editor for building language / key-set variants via fork [variant-editor]
Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. Prerequisite, now met: [layout-config-source] has landed. `resources/key-layout.json` exists and SPEC names it the canonical manifest, so the config this editor would produce and the Android build would consume is real rather than hypothetical. Confirmed during the /plan session of 2026-08-07. Far downstream of a first Android build; filed as a design thread, not near-term work.

Carries an open question relocated from [licence-and-go-public] during planning: once language forks exist, how do they stay in step with canonical Hexboard as it changes? This is the part of the item that most needs outside input — it's a question about distribution and project governance rather than about the keyboard itself, and nothing already decided here settles it. It's an upstream/downstream design question — fork-and-cherry-pick, a shared config, or contribution-back terms written into the licence itself — and it interacts with [layout-config-source], since a shared machine-readable key config is one way forks track upstream without merging code. Not answerable until the config lands and a real fork exists.

Sharpened in the /plan session of 2026-08-04, then deliberately deferred rather than designed. Two things are now settled that narrow it: the editor's target format is `resources/key-layout.json`, no longer TBD; and because the config carries the key inventory only, with geometry staying in Kotlin, the editor's scope is hard-bounded to key data and structurally cannot touch the perceptual wedge. What still blocks design is that none of its subjects exist yet — no keyboard, so nothing to vary; no fork, so the fork-sync question has no real case to reason about; and the SPEC-level scope decision underneath it (whether Hexboard becomes a platform for variants rather than one opinionated keyboard) is better made with a working keyboard in hand than in the abstract.

Re-weighed on 2026-08-07 and deferred again, with the prerequisite above now cleared. What still blocks design is unchanged and is not something a planning session can resolve by thinking harder: there is no keyboard yet, so there is nothing to vary; there is no fork, so the upstream/downstream sync question has no concrete case to reason about; and the scope decision underneath it is better made with a working keyboard in hand.

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Captured by you, and sharpened during the /plan session of 2026-08-06.

In a hexagonal tessellation every key sits the same distance from each of its neighbours, and each interior key has exactly six of them. A standard rectangular keyboard doesn't have this property: horizontal neighbours are closer than diagonal ones, so the set of plausible mis-taps is uneven and direction-dependent. On Hexboard, for any key pressed there are exactly six other keys the user might have meant, equally likely by distance alone — a clean, uniform confusion set.

The user's reason for caring about this: their biggest complaint about autocomplete is that getting the first letter wrong is far worse than getting any later letter wrong, because ordinary autocomplete is a prefix lookup that only reads words forwards and so treats the first letter as certain. The neighbour set dissolves that. Rather than trusting the pressed key, the engine searches forwards from all seven candidates — the pressed key and its six neighbours — and ranks results by geometric plausibility times word frequency. Prefix-trie walks are cheap enough that seven of them cost nothing noticeable. Searching the letters backwards, the fix SPEC previously recorded, remains useful for typos later in a word, but is no longer what rescues the first letter.

This is the argument for building the engine rather than adopting a library: no general-purpose library knows the key geometry, so none can exploit any of it. Neighbour sets come from `resources/key-layout.json` rather than being hand-maintained.

Three things to check before relying on it, recorded so they aren't discovered late. Keys at a panel's edge have fewer than six neighbours, so the uniform case is the interior one. The two space bars in row 3 are not ordinary circles and won't fit the neighbour model cleanly. And the property holds within a panel, not across panels — a mis-tap can't cross a swipe boundary.

A fourth thing to know before designing this, added on 2026-08-07. The neighbour sets are derivable from `resources/key-layout.json`, but not from it alone. The config carries each key's `row` and `col` and nothing more — geometry is deliberately excluded from it, because the zag rule (odd columns sit half a key lower) is the perceptual wedge SPEC calls inviolable and so lives in Kotlin. Which six keys neighbour a given key depends on that zag parity, not just on row and column. So the neighbour table is computed by the app at runtime from the config plus the zag rule; it is not a lookup the config can hold, and nothing should be designed on the assumption that reading the config is sufficient.

Not yet designed enough to build — what exists is the insight and the approach, not a description of what any build would change. It needs a later /plan to turn into buildable work, and SPEC holds predictive text until after the first working keyboard anyway. The three caveats above are the known starting points for that design session.

Filed after `a42cd01`.

#### A Compose keyboard that renders keys from the config on screen [compose-keyboard-renders-config]
Filed on 2026-08-14 during planning, when [throughliner-doc-drift] surfaced that two held items were waiting on things in the world with no queue item to name. This is the larger of the two, and filing it exposed something worth stating plainly: until now the queue had no item for building the keyboard itself. The pieces around it were all queued — the key config, its validator, the audit that checks the keys reach the screen — but not the thing they are about.

What it is: the Jetpack Compose surface that reads `resources/key-layout.json` and draws its keys, in the zag arrangement and circular geometry SPEC calls inviolable, with the three letter panels reachable by horizontal swipe. Nearest-centre hit-testing and per-key accessibility nodes are named in SPEC as requirements of this surface, so they belong in its design rather than being retrofitted.

Not designed enough to build. It is the project's central build and needs a /plan session of its own to break down — at minimum, where the panel switching lives, how the zag parity is expressed in code as the single home of the geometry, and how much of `hexboard17.html` is treated as reference versus specification. `planning/layout-preview.html` is the standing fixture for trying arrangements visually before any Kotlin is written.

#### Decide what `hexboard-editor.html` is for before the repo goes public [hexboard-editor-status]
Noticed on 2026-08-14 during the /plan close, while listing tracked files to check go-public readiness. It was seen and not asked about at the time, which is why it is filed rather than left in conversation.

`hexboard-editor.html` sits at the repo root, is tracked, and is 557 lines of a self-contained page titled "HexBoard Layout Editor". It has not been touched since the project was adopted on 2026-07-02, and nothing in SPEC, CLAUDE.md, the README or any queue item mentions it. The README points a visitor at `hexboard17.html` as the frozen prototype and at `planning/layout-preview.html` as the planning fixture; this third HTML file at the root is named in none of them.

Three things it could be, and they lead different ways. It may be an earlier attempt at what [variant-editor] describes, in which case it is prior art that item should reference rather than a stray file. It may be a superseded experiment, in which case it should be deleted or moved out of the root. Or it may be a live tool that simply never got written down, in which case the README should say what it is.

The reason it matters now rather than later: [repo-go-public] publishes it. A tracked, unexplained, root-level file in a repository whose whole pitch is a readable planning record is the one thing that reads as clutter — and going public cannot be undone, though the file could of course be explained or removed afterwards.

Not urgent enough to block the flip on its own, and deliberately not written as a blocker on [repo-go-public]; that is a judgment for the /plan session that processes this.

#### A first Hexboard build installable on the Pixel 6 [first-installable-build]
Filed on 2026-08-14 during planning, alongside [compose-keyboard-renders-config], for the same reason: [verify-a11y-ondevice] waited on an installable build with no queue item to name as its blocker.

What it is: an APK that installs and runs on the Pixel 6 over wireless debugging, with enough of the IME service in place that the keyboard can actually be selected and typed on. That is a different threshold from [compose-keyboard-renders-config], which only needs keys on screen — this one needs Android to accept it as an input method, which brings in the IME service registration, the manifest declaration and the system's keyboard-picker flow.

Not designed enough to build, and it sits downstream of [compose-keyboard-renders-config] since there is nothing worth installing before keys render. SPEC puts IME service polish — settings screen, language switching — out of scope for early iterations, so the target here is the minimum that lets you type on the thing, not a finished input method.

#### `hexboard-editor.html` is cleared to go public provided it carries no personal details [editor-public-if-no-personal-details]
Captured by you during the /next run of 2026-08-14, in your own words: the editor is fine to go public as long as it doesn't have your email or anything like that in it.

This settles the part of [hexboard-editor-status] that was about risk, and leaves that item to decide only what the file *is* — prior art for [variant-editor], a superseded experiment, or a live tool the README should name. Your condition is about exposure, not about tidiness, so a file that carries nothing personal may ship whatever else is decided about explaining it.

A first check was run at capture time and found nothing: searching `hexboard-editor.html` for an `@` sign, a `mailto:` link, your name, your email address and any local `C:\Users\` path returns one hit, and it is the `@` key in the symbol panel's layout data at line 238. That is a scan for known shapes, not proof the file is clean — it cannot tell whether some other line quietly identifies a real person, and only reading the file settles that. The /plan session that processes this should say which of the two it is relying on.

Filed after `263eb26`.

