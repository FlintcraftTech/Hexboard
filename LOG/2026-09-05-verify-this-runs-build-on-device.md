# 5f9d97e — [verify-this-runs-build-on-device] cleared with its bundled step split and a key-size judgement added, and the three accessibility items given the blocker that was only ever stated in prose

Processed first on the previous session's advice, which was right for a reason worth keeping: every other item in the cleared region was one of three `[user]` checks the user had deferred during the build run of 2026-09-04, all for the same reason — that run's work is not on the phone. A /next run would have presented three items whose answer was already "not yet".

Two faults were fixed while clearing it. The item's own prose said [verify-a11y-ondevice], [verify-switch-access-ondevice] and [physical-keyboard-handover] "are each held until this has run", but none of them carried a `Blocked by:` line, so the queue would have gone on offering all three as ready — which is exactly what had happened. All three gained the blocker and dropped below the readiness line. And step 3 bundled a run, an install, a text-field tap and six visual checks into one instruction; it now splits into three steps of three checks each.

The capability question was re-run rather than taken from the record: no `adb` on this machine and no Gradle on the path, checked on 2026-09-04. A judgement step was added for the key and label size that [soft-edge-fraction-values] changes in the same build, which is what let [label-size-after-soft-edge] be merged away rather than needing its own sitting.

At the close the item moved to the end of the cleared region, so a /next run builds all fourteen build items and then hands over one install that verifies the lot.

**Queue changes:** [verify-this-runs-build-on-device] into Processed, cleared to run and later moved to the end of the cleared region; [verify-a11y-ondevice], [verify-switch-access-ondevice] and [physical-keyboard-handover] each given `Blocked by: [verify-this-runs-build-on-device]` and moved below the line; [settings-steps-name-a-search] moved down with them so its own blocker still precedes it.

**Work processed:** kept — [verify-this-runs-build-on-device].
