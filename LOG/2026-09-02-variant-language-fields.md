# 73231d1 — Language and ordering fields split out ahead of any layout being written, and the creator field dropped hours after it was designed

The layout picker groups layouts by language and orders them within a language, and the config could express neither: schemaVersion 2 carries `id`, `name`, `isDefault` and `generates`, with the language only implied by the id reading `qwerty-en`, which is a convention rather than data. The reason to split this out and place it first is ordering: any layout written before these fields exist has to be edited afterwards.

A display name for the language was considered and refused as a field. The tag alone is enough, because Android and browsers both resolve a BCP 47 tag to a language name in the *reader's* own language; a hand-written name would arrive in twenty spellings and be wrong for everyone but its author.

A `creator` field was designed in the morning and dropped the same afternoon, and it is recorded because it looks obviously useful. It existed to credit outside contributors and link to them from the picker. Once layouts became copies of each language's own standard, authored in this project, there was no outside author to credit and a field naming this project on every layout says nothing.

The reasoning behind that reversal is in the [variant-editor] entry of the same date.

**Queue changes:** created, cleared to run, placed after [repo-go-public] and ahead of [language-starter-layouts]; narrowed from three fields to two.

**Work processed:** kept, cleared to run — [variant-language-fields].
