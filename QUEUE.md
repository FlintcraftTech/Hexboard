# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

#### Point this repo's git identity at the GitHub noreply address [git-noreply-email]
Captured by you as [git-history-email]; rewritten during planning after the finding below reduced its scope.

The build: set this repository's local git config to `283077209+FlintCraftTech@users.noreply.github.com`, so commits made here from now on no longer carry the real address. One `git config --local user.email` command; no project files change. Verify by making the next commit and checking its author metadata.

The history rewrite the original item weighed is **ruled out**, and the reasoning is recorded here so it isn't re-proposed. The address is already public: `FlintCraftTech/sovereign-implementer` and `FlintCraftTech/Taskflowapp` are both public repos and every commit in each carries the same personal address, checked during this planning session. The address itself is deliberately not written into this file — naming it here would publish it in the very document that argues against publishing it. Rewriting Hexboard's six commits would change every commit hash, force a rewrite of the pushed remote, and invalidate every hash reference in `LOG/` and `QUEUE.md` — real cost, to hide an address that stays visible elsewhere regardless. So this item is tidiness for future commits, not protection; it does not un-publish anything.

A global git identity was set to the same noreply address during this planning session. It does not cover this repo, because a local setting overrides it — which is why this item still exists.

One further fact found while scrubbing this file: the address is present in tracked file *content* in commit `a42cd01`, inside the earlier version of this item, which described the address by naming it. So it sits in the repo's file history as well as in commit metadata. The current working tree is clean. Removing the historical occurrence would need the same rewrite ruled out above, and the same reasoning applies — so it stands as a recorded fact, not an open question.

Standing rule this item exists under: the personal address is never written into a tracked project file, including in text *about* the address. Refer to it indirectly.

#### Create the root `.gitignore` — untrack `FAQ/` and block the session-payload sample [untrack-faq]
Red flag · State: cleared

Captured by you during the [planning-record-public] decision, and widened on the same date when the payload-sample risk turned out to need the same file.

`FAQ/` documents how the Sovereign Implementer method works. It is installed into each project on purpose, so that both Alex and Claude can consult it mid-session — so the folder must stay on disk and keep working. What it should not do is ship with a public keyboard repo, where it is method documentation a visitor has no use for.

**Red flag, and how it cleared.** `resources/research/session-start-payload-sample.json` contains this machine's account paths, a Claude Code session ID and a local transcript path. An earlier decision this session was to delete it; that was wrong, and the correction is recorded here so it isn't repeated. The Sovereign Implementer plugin's session-start hook writes this file whenever it is absent, so deleting it guarantees a fresh copy at the next session start with that session's paths in it. The risk is designed out by ignoring rather than deleting: the `.gitignore` this item creates lists the file, so no session can stage it and it can never enter a commit. The file stays on disk, ignored and harmless. This item must land before [repo-go-public], which it already gates.

The build: create a root `.gitignore` (the repo has none — only `android/.gitignore` exists) listing both `FAQ/` and `resources/research/session-start-payload-sample.json`, then run `git rm --cached -r FAQ/` to remove the FAQ from git's index without touching the working copy. Verify four things: `git ls-files FAQ/` returns nothing; the `FAQ/` folder and its two files are still present on disk; `git status` no longer lists the payload sample as untracked; and `git status` shows the FAQ removal staged with no other untracked-file noise.

Known limitation, accepted when this was decided: `FAQ/faq.md` and `FAQ/index.md` are in every commit made so far, so untracking cleans the current file tree and all future commits, but not the history. Anyone browsing past commits could still read them. Full removal would need the history rewrite ruled out in [git-noreply-email], and the same reasoning applies. The payload sample carries no such limitation — it has never been committed, and this item ensures it never can be.

Must land before [repo-go-public].

#### Write a proper public-facing README [public-readme]
Surfaced in the wind-down re-scan of the /plan session on 2026-08-04; scoped during the /plan session of 2026-08-06 after reading the current file.

The existing `README.md` is not empty — [add-licence] gave it a thorough licence section that already explains forking in plain terms, and that section is kept as-is. What's missing is everything a visitor needs before reaching the licence: the entire description of the project is currently one sentence.

The build adds five things to `README.md`, above the existing licence section:

1. **What Hexboard is and why it exists** — circular keys in a hexagonal tessellation, zig-zag rows trading row-straightness for larger keys, and the perceptual claim stated properly: users aim more confidently at circles than at hexagons with visible corners, so they aim more centrally and type more accurately. This is what separates Hexboard from Typewise, MessagEase and Thumb-Key, and it is the most interesting sentence available. State it as the perceptual claim it is — not the functional claim that hex keyboards mis-route taps, which SPEC.md explicitly disowns.
2. **Honest status** — an in-progress Android build with no working keyboard yet. Say so plainly rather than letting a visitor hunt for an install that doesn't exist.
3. **The browser prototype** — `hexboard17.html` runs in any browser with no build step, and is currently the only part anyone can actually try. Say how to open it and what it demonstrates (layout, gestures, key inventory), and that it is a frozen reference rather than the product.
4. **A pointer to the planning record** — `LOG/` and `QUEUE.md` are public by deliberate decision (see CLAUDE.md, decided 2026-08-06), and are arguably the most compelling content in the repo. A visitor won't find them unless the README says they're there and why: this is a worked example of the Sovereign Implementer method.
5. **An image of the layout** — a keyboard is a visual product and a README about key shapes with no picture asks too much of the reader. Capture a screenshot of `hexboard17.html` (or `planning/layout-preview.html`) rendered in a browser, save it under a sensible path in the repo, and embed it near the top.

Files: `README.md`, plus one new image file.

Must land before [repo-go-public] — this is the page a stranger sees first.

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

#### A bespoke predictive text engine built around the six-neighbour confusion set [uniform-neighbours-predictive]
Captured by you, and sharpened during the /plan session of 2026-08-06.

In a hexagonal tessellation every key sits the same distance from each of its neighbours, and each interior key has exactly six of them. A standard rectangular keyboard doesn't have this property: horizontal neighbours are closer than diagonal ones, so the set of plausible mis-taps is uneven and direction-dependent. On Hexboard, for any key pressed there are exactly six other keys the user might have meant, equally likely by distance alone — a clean, uniform confusion set.

The user's reason for caring about this: their biggest complaint about autocomplete is that getting the first letter wrong is far worse than getting any later letter wrong, because ordinary autocomplete is a prefix lookup that only reads words forwards and so treats the first letter as certain. The neighbour set dissolves that. Rather than trusting the pressed key, the engine searches forwards from all seven candidates — the pressed key and its six neighbours — and ranks results by geometric plausibility times word frequency. Prefix-trie walks are cheap enough that seven of them cost nothing noticeable. Searching the letters backwards, the fix SPEC previously recorded, remains useful for typos later in a word, but is no longer what rescues the first letter.

This is the argument for building the engine rather than adopting a library: no general-purpose library knows the key geometry, so none can exploit any of it. Neighbour sets come from `resources/key-layout.json` rather than being hand-maintained.

Three things to check before relying on it, recorded so they aren't discovered late. Keys at a panel's edge have fewer than six neighbours, so the uniform case is the interior one. The two space bars in row 3 are not ordinary circles and won't fit the neighbour model cleanly. And the property holds within a panel, not across panels — a mis-tap can't cross a swipe boundary.

Not yet designed enough to build — what exists is the insight and the approach, not a description of what any build would change. It needs a later /plan to turn into buildable work, and SPEC holds predictive text until after the first working keyboard anyway. The three caveats above are the known starting points for that design session.

Filed after `a42cd01`.

#### A contributor-facing layout editor for building language / key-set variants via fork [variant-editor]
Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. Prerequisite: [layout-config-source] — the editor is a downstream consumer of that single machine-readable config and can't sensibly exist before it. Far downstream of a first Android build; filed as a design thread, not near-term work.

Carries an open question relocated from [licence-and-go-public] during planning: once language forks exist, how do they stay in step with canonical Hexboard as it changes? This is the part of the item that most needs outside input — it's a question about distribution and project governance rather than about the keyboard itself, and nothing already decided here settles it. It's an upstream/downstream design question — fork-and-cherry-pick, a shared config, or contribution-back terms written into the licence itself — and it interacts with [layout-config-source], since a shared machine-readable key config is one way forks track upstream without merging code. Not answerable until the config lands and a real fork exists.

Sharpened in the /plan session of 2026-08-04, then deliberately deferred rather than designed. Two things are now settled that narrow it: the editor's target format is `resources/key-layout.json`, no longer TBD; and because the config carries the key inventory only, with geometry staying in Kotlin, the editor's scope is hard-bounded to key data and structurally cannot touch the perceptual wedge. What still blocks design is that none of its subjects exist yet — no keyboard, so nothing to vary; no fork, so the fork-sync question has no real case to reason about; and the SPEC-level scope decision underneath it (whether Hexboard becomes a platform for variants rather than one opinionated keyboard) is better made with a working keyboard in hand than in the abstract.

#### `hexboard-plan.md` duplicates SPEC.md and would confuse a visitor [stale-plan-doc]
Noticed while scoping [public-readme] on 2026-08-06. `hexboard-plan.md` is a 78-line planning hand-off from before the project adopted the method. Its content — what Hexboard is, the perceptual claim, the prototype's status — has since been carried into `SPEC.md`, which is now the product truth. Nothing in it is wrong or embarrassing, but it is tracked, so a visitor to the public repo meets two overlapping descriptions of the same project with nothing saying which is current. Options are to delete it (the history keeps it), to fold anything SPEC lacks into SPEC and then delete it, or to keep it with a line at the top marking it superseded. Worth deciding before [repo-go-public]. Filed after `a42cd01`.

#### Add `android/.idea/` to the root `.gitignore` [ignore-idea-folder]
Noticed at the /done close on 2026-08-06. Android Studio writes `android/.idea/`, which sits permanently untracked and shows up in every `git status` as noise. The [git-history-audit] pass confirmed it has never been committed, so nothing is exposed — this is tidiness, not a risk. [untrack-faq] is already creating a root `.gitignore`, so this is one more line in a file that is about to exist. Filed separately rather than folded into that item because it is unrelated to why that item exists. Filed after `a42cd01`.

