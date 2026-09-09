# 5f9d97e — [emoji-parse-guard] created so a changed Unicode format fails loudly instead of emptying the panels

Split out of [emoji-data-refresh] on 2026-09-05 when its recurring half became a cycle; that item's record for the same date carries the reasoning.

The part worth finding from this slug is why its assertions are shaped as they are. A test asserting an exact emoji count fails on every legitimate refresh, and one asserting a fixed minimum is a bare number with no derivation. The three used are each a proportion or a requirement the app already has: at least as many entries as the panels display, more than one group heading, and a parsed count of at least half the file's non-comment lines. The third is what actually catches a moved status column, since a wrong column reads as an unrecognised status and drops nearly everything.
