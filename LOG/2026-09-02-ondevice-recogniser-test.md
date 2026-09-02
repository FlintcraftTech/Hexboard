# [HASH] — A dictation test filed onto the existing app rather than waiting for the keyboard, so the recogniser question can be answered early

The measurement that decides whether correcting dictated speech is worth designing at all. If Android's public on-device recogniser is the same engine Gboard uses, Hexboard starts at Gboard's recognition quality for nothing; if it is not, the gap is inside recognition where no transcript-level correction reaches.

The useful observation while filing it was that this needs no keyboard. `MainActivity` is an ordinary app screen that already exists, so a button, a text area and the availability check are enough — which brings the answer forward from several items downstream to one item behind the compile that has to happen anyway. It also answers a second question for free: whether the Pixel 6 has on-device recognition at all.

Written into the item rather than left to a build: the `RECORD_AUDIO` permission and the test screen must not reach a shipped build. The microphone belongs to [in-keyboard-voice-input], which introduces it properly with the press-and-hold control and its guarantees, and removing this scaffolding is written into that item rather than left as a trailing step here.

The research behind it is in the [speech-output-correction] entry of the same date.

**Queue changes:** filed into Unprocessed and placed below the cleared line with `Blocked by: [compile-and-view-panel]`.

**Work processed:** kept, held below the line — [ondevice-recogniser-test].
