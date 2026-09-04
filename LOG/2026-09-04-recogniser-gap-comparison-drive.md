# Driving [recogniser-gap-comparison] — the dictation comparison

Opened 2026-09-04 during the /next run that built eleven items. This is the drive record for
the `[user]` item comparing dictation through Gboard against dictation through Hexboard's own
on-device recogniser test screen. Written as it happens, so an interrupted drive leaves a
record of where it got to.

## What Claude did before handing over

Step 1 of the walkthrough asks for three passages of about thirty seconds, written down: one
of ordinary conversational sentences, one carrying names and places, one to be spoken at
natural rather than dictation speed. Drafting text is not user work, so the three were
written to the session scratchpad as `dictation-passages.txt` for the user to edit or replace
before reading them aloud. Each carries its one property and nothing else; the file says so
above each passage, and says that the user's own words would be better rather than worse.

## The precondition, tested on this item's own turn

Step 3 needs Hexboard's dictation test screen on the phone. The build installed on the Pixel 6
is the one from the install run of 2026-09-03, which carries the temporary dictation screen
`[ondevice-recogniser-test]` put into `MainActivity` — so this item can be run against the
app already on the handset and does not wait on the eleven builds of this run reaching it.
Recorded because the reverse would have been worth knowing before any speaking happened.

## Steps

- Step 1 — passages drafted by Claude, handed to the user to edit. **Done**: the user read them
  and accepted them unchanged.
- Step 2 — getting Gboard back on screen before anything is dictated through it. Split out of
  the walkthrough's own step 2 while driving, because Hexboard was switched on as a keyboard by
  the install run of 2026-09-03, so Gboard is not necessarily what comes up in a text field
  any more — and the original step bundled the switch with opening an app, tapping the
  microphone and dictating three passages, which is more instructions than one step carries.
  **Done**: the user reports Gboard up with its microphone visible. The keyboard-switch route
  that worked is not recorded, because the step offered two and the user did not say which —
  worth asking if the question recurs.
- Step 3 — dictating the three passages through Gboard and saving the transcripts. **Done**,
  and it turned up something the item did not anticipate.

- Step 4 — opening Hexboard's own dictation test screen and reading its availability line,
  which the walkthrough names as the first thing to report. Split from the dictating itself
  while driving, because the availability line is a gate: if on-device recognition is
  unavailable on this handset there is nothing to dictate into. **Done, and it passes**: the
  screen reads "On-device recognition: available" on the Pixel 6. That is the first thing the
  walkthrough asks to be reported, and it settles a premise the whole voice-input design rests
  on — `[in-keyboard-voice-input]` chose to make voice input simply absent where a device
  cannot recognise on-device, rather than falling back to sending audio away, and this handset
  is not in that gap.

  Recorded while driving: the phrase "availability line" is Claude's, from the queue item, and
  named nothing the user could see. What is actually on screen is one line above the
  "Dictate (test)" button reading either "On-device recognition: available" or
  "On-device recognition: not available on this phone", read from `MainActivity.kt` when the
  user asked. A walkthrough step should have quoted those two strings in the first place.
- Step 5 — dictating the same three passages through the test screen and saving what comes out.
  **Done**: the user reports all three dictated and saved.
- Step 6 — the report itself, which is this item's whole point and its only observable.
  **Done.** The item is complete.

## The result

The user's finding, 2026-09-04: the two sets of transcripts are basically identical, and the
only difference is that Gboard adds punctuation.

**What that means, read against what the item said each outcome would mean.** Near-identical
output was written into this item as suggesting the same engine behind both doors, with the
remaining work being punctuation and capitalisation rather than accuracy. That is the outcome
that arrived. The platform's on-device recogniser is good enough to build a press-and-hold
control around, which is the premise `[in-keyboard-voice-input]` was held against — that hold
is answered, and lifting it is planning's call rather than this run's.

**And what it does not mean.** The comparison was run on read passages, which the user
established mid-drive is the easy case and not how he ordinarily dictates. So this says the two
engines are equivalent; it does not say either is good. The absolute standard is untested and
`[rsvp-dictation-prompter]` is the method that would test it.

**The work the result points at is already queued.** Punctuation and capitalisation of what the
recogniser returns is `[speech-output-correction]`'s subject, which already carries the
research on how Gboard does the equivalent — so nothing new is filed from this. Worth noting
that the research there found Gboard's correction happens inside recognition rather than in a
separate stage, which sits oddly beside a result saying the recognition itself matches: the
punctuation gap may be a formatting step this project can simply add, or it may be something
that engine does that a finished transcript cannot recover. That is a question for that item,
not this one.

## Close

Complete, walked to its end this session. It stays in Processed until /done records it under
its slug and removes it.

## A limit in the test's own design, found by the user while running it

Reported by the user on 2026-09-04, from doing step 3: dictation performed far better than it
does for him ordinarily. His account is that he normally needs at least three corrections per
short sentence, and that reading aloud from a page is not how he usually speaks — he narrates
fluently when reading, and hesitates constantly when speaking off the cuff, which is what
Gboard cannot follow.

**Why this matters beyond one person's experience.** A walkthrough that hands someone a written
passage measures the recogniser against read speech, and read speech is the easy case. Every
transcript this item produces is therefore flattered, and a result saying "the recogniser is
fine" would be evidence about reading aloud rather than about dictating.

**What it does not spoil.** The item's actual question is comparative — how the platform's
on-device recogniser stands against Gboard's — and both sides are being fed the same read
passages, so the gap between them is still measured on equal terms. What the read passages
cannot tell us is the absolute standard either engine reaches in real use. Recorded so a later
reader does not throw the whole comparison out on the strength of this.

**The open problem the user named:** how to reproduce ordinary hesitant speech in a test, when
the act of giving someone a script removes the hesitation. He raised an idea in the same
message, which he also expects to bear on making dictation work for people with dyslexia or a
reading disability. He gave it in the same exchange and it is filed as
[rsvp-dictation-prompter]: display the passage one word at a time and large, in the manner of
Spritz or Spreeder, so there is no eye scanning and no seeing ahead; and draw from a rotating
set large enough that repeated runs do not teach the content. The first half removes the
foreknowledge that suppresses hesitation; the second stops it creeping back on a slower clock.
The same design removes the fluent-reading-aloud requirement that would otherwise decide who
can run this test at all.
