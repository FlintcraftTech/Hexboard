# [HASH] — [rsvp-dictation-prompter] designed out as a page, then reframed when the user said what he had actually meant it for

Designed first as a testing instrument: a page in `planning/` beside `layout-preview.html`, showing one phrase at a time and advancing on the speaker's own key press. That home answered all three of the item's undesigned questions at once — passages authored with their phrase breaks in them, so nothing computes a split; the button press, because detecting the end of a spoken phrase needs the recogniser a page does not have; and a rotating set that grows rather than needing a size now. The argument for the page over the app was that the prompter has to work while dictating into Gboard too, since comparing two recognisers is the point, and anything inside Hexboard can only prompt for Hexboard.

Then the user said it was not really dictation tests he had been thinking of — it was the sitting where someone trains the speech model. That changes the home rather than the design: enrolment happens on the phone, and a model trained on someone reading aloud adapts to their reading voice, which is not the voice they dictate in. The fairness point lands harder there too, since a method requiring fluent reading aloud gives anyone with dyslexia a permanently worse personal model rather than a worse test score.

The page survived the reframing for one reason: the claim the whole design rests on — that phrase-at-a-time speaker-paced prompting produces ordinary hesitant speech — has never been tried. A page settles that in an evening, where building it into an enrolment flow behind [personal-voice-model] and [in-keyboard-voice-input] would test it months later. So the page is now explicitly a throwaway trial, the trial itself is [prompter-elicits-natural-speech], and [enrolment-prompter] is the destination.

One genuine design difference was recorded rather than decided: a dictation test wants speech representative of how someone talks, while enrolment may want phonetic coverage, and those can pull against each other.

**Queue changes:** [rsvp-dictation-prompter] rewritten as a trial and cleared to run; [prompter-elicits-natural-speech] created as a `[user]` item held against it; [enrolment-prompter] created as a capture held against [personal-voice-model].

**Work processed:** kept — [rsvp-dictation-prompter], [prompter-elicits-natural-speech]. Filed — [enrolment-prompter].
