# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

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
Captured by you. Rewritten during planning as the second half of a split; the validator half is [key-config-validator].

Original framing was to confirm the Kotlin keyboard's character set matches the manifest. The [layout-config-source] decision removes that need: the app reads `resources/key-layout.json` directly, so its key set *is* the config and the two cannot disagree. What remains worth checking is wiring — a key correctly declared in the config can still render nothing, render in the wrong slot, or emit the wrong character.

The build: an instrumented or Compose UI test that walks every key in the config, asserts a key node exists at the expected panel, row and column, and asserts that activating it emits exactly the character the config declares. Long-press accents get the same treatment — each accent in a key's list is reachable and emits its own character. Failures name the character and its panel position.

This is what manifest rule 1 — verify the shipped key set before shipping — actually means once the config is authoritative: not a comparison of two lists, but proof that the one list reaches the screen intact.

Lift-condition: cleared to run once the Compose keyboard renders keys from the config, since there is nothing to drive until keys exist on screen.

#### [user] Flip the Hexboard repo from private to public on GitHub [repo-go-public]
Captured by you. Split out of [licence-and-go-public] during planning. Only you can do this — it's an account action on github.com that Claude can't perform.

The walkthrough, once the lift-condition below has cleared:
1. Open `https://github.com/FlintCraftTech/Hexboard` in a browser, signed in as the account that owns it.
2. Go to **Settings** (the tab across the top of the repo, not your account settings).
3. Scroll to the bottom of that page, to the red-bordered **Danger Zone** section.
4. Find **Change repository visibility** and click **Change visibility**.
5. Choose **Make public**, then confirm. GitHub asks you to type the repository name — `FlintCraftTech/Hexboard` — to prove it's deliberate.
6. Reload the repo while signed out, or in a private browsing window, to confirm it's genuinely visible.

Lift-condition: cleared to run once all of the following have landed — [add-licence] (done), [git-history-audit] with its findings dealt with (the findings were processed in the /plan session of 2026-08-06), [untrack-faq], [git-noreply-email], and [public-readme]. The licence should be in place before anyone can read the code; the audit was the red-flag mitigation; the FAQ should leave the tree before the tree is public; and the noreply address should be set before any further commit is published.

Going public is effectively irreversible: once the history is public it can be cloned, so taking the repo private again does not un-expose it. Note what this means for the three limitations recorded elsewhere — the personal address in commit metadata and in file content at `a42cd01`, the reworded candid line at `6e09dad`, and the FAQ in every commit so far — all remain readable in history after this runs, and each was consciously accepted rather than overlooked.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Captured by you. Once an Android build exists, install it on the Pixel 6 (wireless debugging) and confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Move the left space bar to row 3 col 2, and decide what displaces and what fills col 4 [left-space-relocation]
Carried out of `hexboard-plan.md` on 2026-08-07, when that document was folded into SPEC and deleted. It was the one open question in it that nothing else records, so deleting the file as-is would have lost it.

The two space bars in QWERTY row 3 currently sit at col 4 and col 6. Confirmed against `resources/key-layout.json` during this planning session: left space is still at col 4, and the comma is still at col 2. Because col 4 and col 6 are adjacent-but-one rather than symmetric about the row, the two spaces are not equidistant from the screen edges, so the left-thumb key is further from the left thumb than the right-thumb key is from the right thumb. Moving the left space to col 2 makes the pair roughly symmetric, one comfortable per thumb.

Two questions the move opens, and they are the reason this is not a one-line change. The comma currently at col 2 has to go somewhere. And col 4 is then vacant — which SPEC's manifest rules say is an opportunity to be filled deliberately with a character that has no other home, not a gap to leave. Both need agreeing before any edit to the config.

Not designed enough to build. It needs a layout-preview session: edit the `LAYOUTS` block in `planning/layout-preview.html` to try candidate arrangements and look at them, rather than reasoning about column numbers in the abstract.

Filed after `e6742fc`.

#### The key config holds no emoji panels, though SPEC says five exist [emoji-panels-missing-from-config]
Noticed on 2026-08-07 while checking the emoji panel count during planning. `resources/key-layout.json` contains three panels — `rare`, `qwerty` and `symbols`. SPEC says five emoji panels are reached by vertical swipe down, and `hexboard17.html` implements them.

The tension is with the config's own claim about itself. Its `about` field calls it the single source of truth for which characters exist and where they sit, and SPEC's manifest principle says the same. Emoji are characters on panels, so on a plain reading they belong in it and are missing.

There is a defensible reason they might not, which is why this is a question rather than a defect. Emoji panels may be intended as generated or system-supplied content rather than a hand-curated inventory, in which case the manifest rules — no key lost, no unresolved duplicates, empty slots are opportunities — were only ever meant to govern the letter and symbol panels. If that's the intent, the config's `about` field and SPEC's principle should say so, because neither currently carves emoji out.

Either way something changes: emoji panels get added to the config, or both documents get a sentence scoping the manifest to the non-emoji panels. Deciding which is what this item is for.

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

#### Bring the project docs in line with the renamed and reshaped method [throughliner-doc-drift]
Filed during the /setup migration on 2026-08-14, which brought this project up to the current document format. The migration updated the scaffolding files it owns, but it does not rewrite content, so two kinds of drift are left over and both need a decision rather than a find-and-replace.

The name. The method was called Sovereign Implementer and is now called Throughliner. `CLAUDE.md` still uses the old name throughout, and so does the prose in several queue items and LOG entries. The README was written this session and already says Throughliner, so the repo is currently inconsistent with itself. What needs deciding is how far back to go: CLAUDE.md is live instruction and should almost certainly be updated, but LOG entries are a record of what happened at the time, and rewriting them would misreport what the sessions actually said.

The queue shape. The current format expects every item below the cleared-to-run line to carry a `Blocked by: [slug]` line naming the queue item that holds it. Three items are below the line without one — [android-key-audit], [repo-go-public] and [verify-a11y-ondevice]. Each does state a lift-condition in prose, but two of the three wait on something in the world (a working Compose keyboard, an installable build on the Pixel 6) rather than on a queue item, and under the new shape that thing has to be filed as its own item before it can be named as a blocker. So this is real processing work, not formatting.

Filed after `24b811a`.

