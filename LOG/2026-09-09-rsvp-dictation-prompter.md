# [HASH] — [rsvp-dictation-prompter] a throwaway page to kill an idea in an evening rather than after months of building on it

This session ran across 2026-09-05 and 2026-09-09.

The design this page tests came from the user noticing that dictation performed far better in a test than it does for him ordinarily, and working out why: handed a written passage he can read ahead, and knowing what he is about to say is exactly what removes the hesitation. His ordinary use runs to at least three corrections per short sentence. It bites hardest on training a personal voice model, because a model trained on someone reading aloud adapts to their reading voice rather than the voice they dictate in — and a method requiring fluent reading aloud gives anyone with a reading disability a worse personal model, which is worse than giving them a worse test score.

**The page is a trial instrument, not the feature, and that is the whole point of building it.** The claim everything rests on — that a phrase-at-a-time, speaker-paced prompt produces ordinary hesitant speech — has never been tried by anyone. A page you can open and speak into settles it in an evening. If people slip into reading-aloud voice anyway, the idea dies for a few hours' work rather than after being built into an enrolment flow that sits months away behind two other items.

Three things carry the design, and two of them were the user's corrections to something simpler. One piece at a time, large, so foreknowledge is removed by construction rather than by asking someone not to read ahead. **Phrases rather than single words** — one word at a time is its own unusual act and elicits its own stilted delivery, which is a different unnatural speech rather than the natural speech wanted. And **the speaker sets the pace**, explicitly not a forced march, which would make the speaker chase the prompt and fail worst for the slower readers this is meant to include. The passage set is deliberately larger than one sitting needs, since repeated runs teaching the content would reintroduce foreknowledge on a slower clock; a shuffled queue rather than a random pick is what stops a passage repeating.

The passages live in a block inside the HTML because the page is opened by double-clicking with no server, and a locally opened page cannot fetch a sibling file. It stores nothing and makes no network request.

The observation is made by opening the page. It was sent to the user to open rather than driven in a browser here, because a browser call in this environment can stall on a permission prompt he cannot see — a fact now recorded in `TOOLS.md`. The page's logic was read back against each clause of the observation in the meantime.

**Files touched:** `planning/dictation-prompter.html`.

**Routed to Captures:** none.
