# 5f9d97e — [dead-key-border-colour] cleared, with the one live border in the same file explicitly protected

Confirmed by grep rather than taken from the item: `KeyColors` declares a `border` colour, `colorsFor` supplies one for all five key kinds, and nothing reads any of them since [soft-key-edge] removed the border from every key.

The find worth having is what the build must *not* also remove. There is still a live `.border(1.5.dp, …)` in `KeyboardPanel.kt`, on the accent popup's container — the rounded panel that floats above the board while a key is held — and it is correct: it separates a floating panel from the board behind it, where the no-border rule is about keys. Without that written down, whoever does the tidy-up greps for `border`, finds two things and is one careless moment from removing both.

It is dead rather than reserved for a future theme, since SPEC now says a key is drawn with no border at all. The item's original reason for deferring — five lines in a file that run had already changed heavily and could not compile — has expired with that run.

**Queue changes:** [dead-key-border-colour] rewritten and moved into Processed, cleared to run.

**Work processed:** kept — [dead-key-border-colour].
