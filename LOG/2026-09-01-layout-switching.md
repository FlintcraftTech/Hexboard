# [HASH] — The layout picker settled as living in the app's own settings, not on the keyboard surface

[layout-switching] was held by the same variant-identity gap as [variant-editor], and gained the same `Blocked by: [variant-schema]` line on the same terms — the reasoning is in the entry for [variant-schema].

What is new here is a product decision the user made rather than a disposition. Asked where the picker should live, they chose the app's own settings. The alternative — a long-press or gesture on the keyboard itself, reachable without leaving the text field — lost because horizontal swipe already means "change panel", so putting the picker on the keyboard would have to find a gesture that does not collide with one the layout already spends. That defeated option is written into the item, because it is the obvious idea and would otherwise be re-proposed as a gap.

The decision was written into SPEC's platform principle in the same session, at the decision step rather than at the close.

What remains open beyond the schema is recorded and is not anyone's decision to make now: how the app finds available layouts at runtime, and where it remembers the choice. Both wait on a keyboard that runs. The item stays in Unprocessed rather than below the cleared-to-run line for a concrete reason — there is no settings screen to add to, the app having no input method service at all today.

**Queue changes:** [layout-switching] gained a `Blocked by:` line naming [variant-schema] and the picker-location decision; stays in Unprocessed. SPEC's platform principle gained a sentence.

**Work processed:** kept in Unprocessed, held — [layout-switching].
