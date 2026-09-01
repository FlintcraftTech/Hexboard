# be3516e — SPEC's manifest-versus-platform tension found already resolved, one surviving word fixed, and the item deleted

A rescan pass filed this on 2026-08-20, having noticed that SPEC's manifest principle called `resources/key-layout.json` the single source of truth while the platform principle added the same day said many contributed layouts ship in one app. The item asked for the contradiction to be settled deliberately rather than guessed at.

Reading SPEC before analysing it showed most of the contradiction had been fixed in the very session that filed the item, which the rescan pass had not accounted for. Two things landed in commit `9a85807`: "single source of truth" was reworded to "the key inventory is governed by config, never by code", and a sentence was added reading "Where other layouts exist as contributed variants, each is described by its own config and the four rules below bind each layout individually" — which is exactly the resolution the item called a guess. That commit was checked rather than the dates trusted.

What survived was one word. SPEC still called that file "the canonical layout", singular and definite, which reads as the only layout even with the variants sentence beside it. Reworded to "the default layout the app ships with" — a change that needed no answer to the variant-identity schema question, since it says nothing about how a variant identifies itself.

The item was then deleted rather than kept, its content having moved into SPEC. Nothing else in the queue cited its slug, so no references broke.

**Queue changes:** [manifest-principle-vs-variants] deleted from Unprocessed.

**Work processed:** deleted — [manifest-principle-vs-variants].
