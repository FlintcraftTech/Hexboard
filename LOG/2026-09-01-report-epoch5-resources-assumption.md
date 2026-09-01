# be3516e — The epoch-5 folder-move trap reported by mail to the project that maintains the method

The migration of 2026-09-01 had run into an instruction that assumes a project's root `resources/` folder holds nothing but method material. This project's also holds the keyboard's key data, which the build system, the app code, a generator script, a test, SPEC and the README all reference by that path, so following the instruction literally would have broken the build.

The claim was verified against the installed plugin before the report was drafted, rather than described from the migration's memory: `docs/migrate-checklist.md` in 1.21.1-test5 still says to move the whole folder, and its check-it-landed test still requires that no `resources/` folder remains at the root — which makes the correct outcome here read as a failure. The defect is live, not already fixed.

Mail was recommended over a public issue and the user agreed: same machine, nothing published under their account, and it lands in the queue that would fix it. The report was scrubbed by construction — no project or product names, the case described generically as a project whose `resources/` also holds product data — which also makes it a report about the general trap rather than about one project.

The item was deleted once sent, the report being the whole of the work.

**Queue changes:** [report-epoch5-resources-assumption] deleted from Unprocessed. `INBOX/sent.md` created and its first line written.

**Work processed:** deleted after completion — [report-epoch5-resources-assumption].
