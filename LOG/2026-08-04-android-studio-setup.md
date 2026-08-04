# dca16ac — Android Studio project stood up at android/ (Kotlin, Compose, Empty Activity)

The first concrete build step: a Kotlin / Jetpack Compose project created through Android Studio Panda 4 (2025.3.4) rather than scaffolded by hand, walked through step by step. Name `Hexboard`, package `tech.flintcraft.hexboard`, minimum SDK 26, Kotlin DSL build files — the wizard defaults were kept everywhere they were sensible.

The one judgment call was the save location: the project went to `android/` *inside* the existing Hexboard folder rather than a sibling directory, so the code lives in the same git repo as SPEC.md, QUEUE.md and LOG/. A separate folder would have split the product record from the code and left the Android project uncommitted alongside the docs that describe it.

Gradle sync completed successfully, and the scaffolding was verified on disk: Gradle wrapper, `settings.gradle.kts`, and `MainActivity.kt` under `app/src/main/java/tech/flintcraft/hexboard`. Android Studio's generated `.gitignore` excludes `local.properties`, which holds a machine-specific SDK path — correct, and worth noting since it means the project is portable as committed.

**Files touched:** `android/` (whole project tree, created by Android Studio).
**Routed to Captures:** `[licence-and-go-public]` — choose a licence matching the read-and-fork-to-translate intent and make the repo public; carries an uncleared red flag on git-history exposure.
