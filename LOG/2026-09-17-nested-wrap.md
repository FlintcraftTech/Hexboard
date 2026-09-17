# [HASH] — The nested wrap chosen, reversing the decision to publish the planning record, and filed rather than performed

Recorded 2026-09-17 13:59.

The setup top-up offers every flat project a conversion to the nested shape: the product in a subfolder with its own repository that goes public, and the planning documents tracked in an outer repository that never gets a remote and so stays private. This checkout has a remote, so the arm available is the wrap — the existing repository is kept whole as the inner one, and this folder becomes the private outer.

**Claude recommended declining and the owner chose it anyway, which is recorded as a reversal rather than a preference.** On 2026-08-06 this project deliberately chose to publish its planning record — `LOG/`, `QUEUE.md`, `CLAUDE.md` and `SPEC.md` tracked and public — because the record is a worked example of the Throughliner method and the demonstration value was the point. The wrap exists to make exactly those documents private. The older decision is written into the item as superseded, so publishing the record again would be a deliberate move from there rather than a return to a default.

One concern was raised and cleared by measurement rather than left hanging: this project carries a recorded Windows path-ceiling hazard, and a nested folder adds about nine characters to every path. The longest live path measured about 160 against Windows' 260, and the 239-character one found alongside it is stale Gradle output that [stale-android-build-dir] removes, with live build output already relocated off the Drive folder. No obstacle.

The split was settled with the owner file by file. Product, staying inside: `android/`, `resources/`, `scripts/`, `README.md`, `LICENSE` and the inner `.gitignore`. Coming up to the private outer: the method's documents, `workshop/`, `temp/`, the markers, and `.claude/`. Two were put as borderline — `hexboard17.html`, the frozen prototype, and `planning/`, holding the layout-preview fixture — and the owner placed both outer.

**The bulk of the work is not the move.** About twenty queue items name paths that all go one level deeper, and a build reading a stale path either fails or is refused. The rewrite is mechanical but has to land in the same move, because a checkout in the new shape with a queue describing the old one is the failure state.

**It was filed rather than done, for an engineering reason rather than caution.** The outer's documents are moved *out* of the inner repository, so git sees them as deletions of their last committed state — and six of them carried uncommitted edits at that moment. Committing first makes the wrap a pure restructure.

**Queue changes:** [nested-wrap] filed and placed first in the cleared region, marked `[freeform]` and `Runs alone`, carrying the split, the product subfolder name `hexboard/`, the queue-path rewrite, the two ripples to check during the move, the visibility and project-rules rewrites it forces, and the parts question the top-up could not ask.

**Work processed:** kept — [nested-wrap].
