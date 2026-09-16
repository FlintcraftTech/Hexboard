# 770b778 — [russian-panel-gaps] one rule fills every non-English layout's empty symbol slots, quotes taken from Unicode's per-locale data

Session of 2026-09-12, 12:54.

Six layouts had empty key positions on their symbol panels — ten on Russian's SYMBOLS and three more on its RARE panel, and the same ten on each of French, German, Spanish, Portuguese and Italian. The English fill of `“ ” ‘ ’ • ← → ½ ¢ ≈` could not simply be copied, because four of those ten are English curly quotes and most of these languages set off speech with guillemets.

**The entry proposed transcribing what each language's own keyboards offer, and that turned out not to be available.** FlorisBoard — the source this project transcribes layouts from — organises symbol layouts by script and region rather than by language: `western.json` covers French, German, Spanish, Portuguese and Italian alike, and there is no Russian symbol layout at all. So the source that settles the letters cannot settle the quotes. Read on 2026-09-12 and written into `workshop/resources/research/open-source-layout-sources.md`.

**What replaced it is published data: CLDR's `delimiters` element gives every locale a primary and an alternate quotation pair** — four characters, which is exactly the number of quote slots, across 574 locales. That puts the quote slots on the same footing as the bundled emoji list, under a licence already read, and it answers every future language on the day its layout is transcribed rather than leaving a per-layout judgment with no source behind it.

**Three qualifications came out of working it through, and each one came from a fact rather than from taste.** A character already on another panel of the same layout is skipped rather than duplicated, because « » sit on RARE on every layout and the plain rule would have created an unresolved duplicate on five of the six. A long-press alternative is explicitly *not* "elsewhere" for that test — settled with [curly-quote-double-route] the same day and now in SPEC — because reading it the other way would empty most of the slots the rule exists to fill. And where the fallback runs out the build leaves the slot empty and files a capture, because SPEC requires a freed slot's character to be *agreed first*, so a build inventing one would break the rule it was satisfying.

**[latin-panel-gaps] was folded in and deleted**, being the same ten slots on the five Latin layouts; its content — the five languages and the quotes-versus-punctuation split the rule is built on — is carried in the entry. Two other items now depend on this one: [russian-missing-cursor-right] reserves one Russian slot for the apostrophe it evicts, and [spanish-letter-panel-gaps] spends Spanish's fallback characters, so Spanish is expected to finish with about two slots this rule cannot fill.

**Queue changes:** [russian-panel-gaps] rewritten to cover all six layouts and cleared to run; [latin-panel-gaps] folded into it and deleted; SPEC gained the per-language quotation-mark sentence at the /done gate.

**Work processed:** kept — [russian-panel-gaps]; deleted — [latin-panel-gaps].
