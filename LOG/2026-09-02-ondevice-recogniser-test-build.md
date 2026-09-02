# PLACEHOLDER — [ondevice-recogniser-test] built: a dictation test on the app screen that reports on-device availability and puts one recognised utterance on screen

Closed 2026-09-02 16:18. Sixth item of the run.

The item exists to answer one question before any correction feature is designed: whether Android's public on-device recogniser is the engine behind Gboard's dictation on a Pixel. That is settled by measurement, and the measurement is [recogniser-gap-comparison], which this item makes possible by giving the app screen a button that runs one utterance through `createOnDeviceSpeechRecognizer` and shows the result, under a line reporting what `isOnDeviceRecognitionAvailable()` returned.

The gate is API 31, as the research recorded, and there is deliberately no fallback to the network recogniser below it or where the phone reports none: that would send audio off the device, which the voice-input design refuses. The button asks for the microphone permission at runtime on first press.

The item is emphatic that this scaffolding must not reach a shipped build, and the build carried that: the manifest's `RECORD_AUDIO` line and the screen's doc comment both say so and name the voice-input item as the one that removes them.

Tick: done, UNCONFIRMED: needs the app run on the Pixel 6 (availability line, then dictate a sentence); the runtime permission prompt is the first thing the button does.

**Files touched:** `android/app/src/main/java/tech/flintcraft/hexboard/MainActivity.kt`, `android/app/src/main/AndroidManifest.xml`.

**Routed to Captures:** none.
