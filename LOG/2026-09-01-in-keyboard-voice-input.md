# be3516e — Voice input designed to live inside the keyboard, held open by the thumb, with the accent picker designed and then dropped

The user's requirement was that nobody should have to switch to another keyboard to dictate, because they switch away and never switch back. That this is achievable was checked rather than assumed: an input method can run recognition itself, `isOnDeviceRecognitionAvailable` and `createOnDeviceSpeechRecognizer` existing from API 31 and on-device recognition being forced from API 33, and two existing keyboards are built this way.

A keyboard requesting microphone permission is on its face indistinguishable from a keyboard that listens, and this repository is public and will be read by people deciding whether to trust it. That was surfaced as the item's central problem rather than a footnote. Three design answers and one accepted cost: recognition on-device so audio never leaves the phone; no audio retained after transcription; and the microphone open only while the control is held down.

Press-and-hold was the user's choice over tap-to-start-tap-to-stop, and the reason it won is that "the microphone is open only while you are holding the button" is a claim a stranger can verify by using the keyboard, rather than one they must take on trust from an indicator and a timeout. Comfortable long-form dictation is deferred rather than solved.

The user then answered press-and-hold's real weakness themselves: the button is hard to keep hold of while moving about, and a thumb drifting slightly loses the message halfway through. Their fix is that the control grows while held and shrinks when fully released — which is this project's own thesis, a generous touch target, applied to the one control where losing your grip costs a sentence rather than a character.

One decision was made rather than asked, and the user was told they could overturn it: on devices too old for on-device recognition, between API 26 and 31, voice input is absent rather than falling back to Android's network recognizer. A keyboard that quietly ships someone's voice off-device on older hardware would make every other privacy claim here worthless.

**An accent picker was designed and dropped, and the reason is the item's most useful record.** Offering the English varieties a device supports would have helped whoever matches an entry — Indian English, Nigerian English — and missed the case the user actually raised: a second-language speaker in a multicultural country, who matches no entry, because the list is organised by where a variety of English is spoken natively. Their verdict was that it compares poorly with training on the user's own voice. Recognition therefore uses the phone's language with no accent setting of its own. The supporting material is filed as `workshop/resources/research/android-voice-input-and-accents.md`.

Not designable further yet: where the mic control lives — a key declared in the layout config, or a control belonging to the board — is unsettled, and it reaches the config and the manifest rules if it is a key.

**Queue changes:** [in-keyboard-voice-input] filed in Unprocessed, held against [first-installable-build], carrying a cleared red flag and a research citation. SPEC gained the voice input principle.

**Work processed:** kept in Unprocessed, held — [in-keyboard-voice-input]. Red flag cleared by design plus informed consent.
