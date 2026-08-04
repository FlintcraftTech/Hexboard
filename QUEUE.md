# QUEUE

## Processed

Vetted work, ready to build — worked top to bottom. Each piece of work is one item: a `#### ` heading naming it, a `[slug]` at the end of that heading line, and a short rationale beneath. A leading flavor tag names how it runs — none for a build (Claude edits files), `[audit]` for a review pass, `[user]` for a step only you can do. A security or privacy risk Claude surfaces lives here too, as a work item carrying a `Red flag · State: cleared/uncleared` marker. The line below marks how far down is cleared to build; anything below it is decided but not ready yet.

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

Lift-condition: cleared to run once [add-licence] has landed *and* [git-history-audit] has run with any findings dealt with. Both gates matter — the licence should be in place before anyone can read the code, and the history audit is the red-flag mitigation. Going public is effectively irreversible: once the history is public it can be cloned, so taking the repo private again does not un-expose it.

#### [user] Verify hit-testing and accessibility nodes on-device against TalkBack and switch access [verify-a11y-ondevice]
Captured by you. Once an Android build exists, install it on the Pixel 6 (wireless debugging) and confirm two things with accessibility services active: (1) nearest-centre routing still selects the intended key, and (2) each key's accessibility node exposes the right label and bounds under TalkBack and switch access. You run this on-device. Lift-condition: cleared to run once a first Android build is installable on the Pixel 6.

## Unprocessed

Captured ideas and tasks not yet fully processed. The next /plan session goes through these with you and decides each one's fate — keep it (move it up to Processed) or drop it. Each is filed as its own `#### ` heading, so the list shows up in an editor's outline.

#### Last session advises building layout-config-source next [advisory]
The /plan session of 2026-08-04 settled the format question that was blocking it: `resources/key-layout.json` is the source of truth, `resources/key-manifest.md` is generated from it, and geometry stays in Kotlin. Nothing unprocessed overlaps it, and three cleared items behind it — [key-config-validator], [add-licence], [git-history-audit] — depend on nothing else. Building the config first also gets it in place before any Kotlin key code starts consuming the manifest by hand. Suggested next step is /next. Filed after `6e09dad`. (This advisory is consumed and cleared at the next /plan.)

#### [user] Run the key-config validator test in Android Studio to confirm it compiles and passes [run-key-config-validator]
Claude wrote `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` during the build of [key-config-validator], but could not run it: Gradle needs a local loopback network connection that the session's environment blocks, so every attempt failed before the build started. The config itself was verified — Claude reimplemented all six checks in a throwaway Python script and every one passed against `resources/key-layout.json` — so what's unverified is the Kotlin, not the key data. Specifically: that the test compiles, that Gson resolves as a test dependency, and that the test locates the config file at runtime.

The walkthrough:
1. Open the `android` folder in Android Studio and let it sync Gradle.
2. In the Project pane, open `app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt`.
3. Click the green run arrow next to the class name `KeyLayoutValidationTest` and choose Run.
4. Report what happens — all six tests green, a compile error, or a test failure. A test failure would name the offending character and panel.

If it can't find the config file, the likely cause is the `hexboard.repoRoot` system property set in `android/app/build.gradle.kts`; the test also walks up from the working directory as a fallback.

Filed after `6e09dad`.

#### Decide what to do with the untracked session-payload sample containing machine paths [session-payload-sample]
Red flag · State: uncleared

`resources/research/session-start-payload-sample.json` sits untracked in the working tree. It was saved by an earlier session as a sample of the data a session-start hook receives. It contains `C:\Users\Alex 2\...` absolute paths twice, a Claude Code session ID, and a full transcript path.

Red flag: the [git-history-audit] pass confirmed the repo's history holds no machine paths at all. Committing this file would put them there, and going public would then publish them — a later deletion would not help, because the history keeps the old content. Machine paths reveal the account name and folder structure; the transcript path points at a local conversation record.

Three options. Delete it, if the sample has served its purpose. Keep it untracked and add it to `.gitignore`, so no future session can stage it by accident — this is the safest option if the sample is still wanted. Or scrub the paths and session ID to placeholders and commit the scrubbed version, if the shape of the payload is what matters rather than the values.

This is not something Claude should decide alone: the file may still be needed for work on the Sovereign Implementer method, which is a separate project. It stays uncommitted until then, so nothing is exposed by waiting.

Filed after `6e09dad`.

#### Decide what to do about the email address in every commit before going public [git-history-email]
Finding 1 of the [git-history-audit] pass. Every commit in the repo carries `recyclobat@gmail.com` as both author and committer — all six, from `18ea3b9` (2026-07-02) through `6e09dad` (2026-08-04). Commit metadata is public and machine-readable on GitHub, and address-harvesting from it is routine, so publishing the repo publishes the address.

Two halves to decide separately. For future commits, GitHub can supply a `noreply` address that hides the real one; setting it is a config change and costs nothing. For the six commits already made, the address can only be removed by rewriting the whole history, which changes every commit hash — a destructive operation that needs its own decision and its own build.

Worth weighing against the fact that the address may already be public elsewhere, in which case the rewrite buys little.

Filed after `6e09dad`.

#### Reword the candid line about Alex in the variant-editor queue item [queue-candid-line]
Finding 2 of the [git-history-audit] pass. `QUEUE.md` line 88, inside the [variant-editor] item, reads "Alex has flagged this as beyond him". It was written for a private planning doc and reads differently on a public page — it is a self-assessment about Alex's own limits, not a fact about the project.

It appears in one commit only, `6e09dad`, so rewording it in the working file is cheap and stops it appearing in the published current state. The original wording stays in the git history unless that commit is rewritten, which is the same destructive operation [git-history-email] weighs — so the two may be worth deciding together.

The substance is worth keeping: the fork-sync question genuinely is unresolved and genuinely needs outside input. What changes is framing it as a property of the question rather than of the person.

Filed after `6e09dad`.

#### Decide consciously whether the internal planning record goes public [planning-record-public]
Finding 3 of the [git-history-audit] pass. `LOG/` (six session entries), `QUEUE.md`, `CLAUDE.md` and `FAQ/` are all tracked, so making the repo public publishes the entire internal planning record: every design decision and its reasoning, every alternative that was weighed and why it lost, the working process, and the fact that the project is built with an AI method plugin.

None of this is a data leak and nothing here is unsafe. Some projects publish exactly this deliberately, and it is arguably the most interesting thing in the repo for a visitor. The reason it is filed is that it would otherwise happen by default rather than by choice, and it is the bulk of what a stranger landing on the repo would actually read.

Options if the answer is no: move the method docs to a separate private repo, or add them to `.gitignore` and remove them from tracking — the second still leaves them in history, which again ties into [git-history-email].

Filed after `6e09dad`.

#### Write a proper README before the repo goes public [public-readme]
Surfaced in the wind-down re-scan of the /plan session on 2026-08-04. [add-licence] creates a README only as a home for a licence section, which is not enough for a repo strangers will land on. A public README should say what Hexboard is, state the perceptual claim that distinguishes it from other tessellation keyboards, make clear it is an in-progress Android build rather than a shipping app, and spell out plainly what a fork may and may not do under PolyForm Noncommercial. Should be ordered before [repo-go-public]. Filed after `dca16ac`.

#### Consider a legal read of the licence choice before going public [licence-legal-read]
Surfaced in the wind-down re-scan of the /plan session on 2026-08-04. The PolyForm Noncommercial choice was made from a research summary written by Claude, which is not legal advice. The risk is low — the licence is standard and lawyer-drafted, and it was not modified — but the decision is effectively irreversible once forks exist under it, so a short professional read is worth weighing. Filed so the option is consciously taken or declined rather than never raised. Filed after `dca16ac`.

#### A contributor-facing layout editor for building language / key-set variants via fork [variant-editor]
Captured by you. Idea: a tool that lets a collaborator who forks Hexboard define their own key set — other languages, alternate character sets, long-press accent maps, panel contents — and output a config the Android build consumes, so people build Hexboard variants without hand-editing code. The fixed perceptual geometry (zag rows, circular keys) stays; only the key set varies, keeping variants clear of the inviolable perceptual wedge. Strategic note: this expands Hexboard's posture from one opinionated keyboard to a layout platform for variants — a conscious SPEC-level scope decision to make when taken up, not assumed now. Prerequisite: [layout-config-source] — the editor is a downstream consumer of that single machine-readable config and can't sensibly exist before it. Far downstream of a first Android build; filed as a design thread, not near-term work.

Carries an open question relocated from [licence-and-go-public] during planning: once language forks exist, how do they stay in step with canonical Hexboard as it changes? Alex has flagged this as beyond him. It's an upstream/downstream design question — fork-and-cherry-pick, a shared config, or contribution-back terms written into the licence itself — and it interacts with [layout-config-source], since a shared machine-readable key config is one way forks track upstream without merging code. Not answerable until the config lands and a real fork exists.

Sharpened in the /plan session of 2026-08-04, then deliberately deferred rather than designed. Two things are now settled that narrow it: the editor's target format is `resources/key-layout.json`, no longer TBD; and because the config carries the key inventory only, with geometry staying in Kotlin, the editor's scope is hard-bounded to key data and structurally cannot touch the perceptual wedge. What still blocks design is that none of its subjects exist yet — no keyboard, so nothing to vary; no fork, so the fork-sync question has no real case to reason about; and the SPEC-level scope decision underneath it (whether Hexboard becomes a platform for variants rather than one opinionated keyboard) is better made with a working keyboard in hand than in the abstract.

