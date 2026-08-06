# a42cd01 — Unit test added validating key-layout.json against the manifest's inviolable rules

The config being the single source of truth stops the app's key set drifting from it, but says nothing about whether the config itself is sound — and soundness is where the manifest's rules actually bite. This test closes that gap: six checks over the config alone, needing no keyboard and no device.

The checks are cross-panel duplicate outputs, within-panel duplicate outputs, row/column collisions, containment inside each row's declared bounds, every declared slot either filled or carrying a note explaining why it is empty, and well-formed accent lists with distinct characters. Together they cover manifest rule 2 and rule 4, and they enforce rule 3 by failing the build when a change breaks either.

The within-panel duplicate check goes slightly beyond what the queue item asked for, which named cross-panel duplicates only. It was worth adding because the two space bars are the one within-panel duplicate that exists, they are deliberate — one per thumb — and a rule that only looked across panels would have let a genuinely accidental repeat pass silently. Both space keys now carry an explicit justification field, so the deliberate case is recorded rather than merely tolerated.

Failure messages name the offending character and panel, and invisible characters are named rather than printed — a failure reading "space appears twice" is actionable where one reading "' ' appears twice" is not.

The test could not be run this session. Gradle needs a local loopback connection the environment blocks, and it failed identically three times, including with Android Studio's bundled JDK. Rather than close with nothing verified, all six checks were reimplemented in a throwaway Python script and run against the config: three panels, one hundred keys, ninety-five distinct output characters, every check passing. So the key data is verified and the Kotlin is not — that specific gap is filed as [run-key-config-validator] for Alex to run in Android Studio.

**Files touched:** `android/app/src/test/java/tech/flintcraft/hexboard/KeyLayoutValidationTest.kt` (created), `android/gradle/libs.versions.toml` and `android/app/build.gradle.kts` (Gson 2.13.2 as a test-only dependency, and a repo-root system property so the test can find the config)

**Routed to Captures:** [run-key-config-validator]
