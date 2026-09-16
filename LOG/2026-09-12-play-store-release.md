# 770b778 — [play-store-release] filed so two items waiting on a Play release could name it, and left undesigned deliberately

Session of 2026-09-12, 12:54.

Two entries turned out to be waiting on the same thing, and nothing in the queue named it. [feedback-funnel-before-store] is a complaint channel whose purpose is catching people before they leave a review, and there are no reviews until there is a listing. [dictionary-asset-packs] delivers word lists through Google Play Asset Delivery, which requires publishing as an App Bundle through Play. SPEC also already promises a language's word list arrives separately with no network request of Hexboard's own, which is a promise resting on the Play route existing.

Both were repointed onto this entry, each having been held against a blocker that shipped in the build run ending 2026-09-09 — [layout-error-report] and [predictive-dictionary-bundle] respectively. In both cases the spent blocker was never the binding one.

**It was discussed the session it was filed and deliberately left in Unprocessed.** Whether to release, and when, is the owner's decision rather than work anyone can specify, and everything specifiable follows from it — the release track, what an input method must declare about the data it handles, where the signing key lives, what the listing says about a keyboard whose differentiator is a perceptual claim.

Two consequences were named and accepted. It will surface near the top of most planning sessions, because two entries cite it and the ordering rule ranks by how much other work an item would release — being set aside each time is the correct outcome for a standing reminder. And a `Not before:` date was refused: a date is for something outside the project's control, and one here would be a guess about the owner's own intentions.

One concrete thing it already carries: [layout-error-report]'s address lives in gitignored `local.properties`, so a released build needs that value supplied some other way.

**Queue changes:** [play-store-release] filed; [feedback-funnel-before-store] and [dictionary-asset-packs] repointed onto it.

**Work processed:** kept in Unprocessed — [play-store-release], [feedback-funnel-before-store], [dictionary-asset-packs].
