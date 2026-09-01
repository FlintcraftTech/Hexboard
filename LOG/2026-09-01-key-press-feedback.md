# be3516e — Press feedback settled as the key's own highlight, with the touch-point marker rejected on the perceptual wedge

The user's complaint was that the keyboard as it stands is inert, and their first description asked for touches showing and fading alongside a key highlight. Reading `KeyboardPanel.kt` turned that into a sharper question than a polish job.

The tap handler sits on the board rather than on the keys, because nearest-centre routing sends a tap to the closest key centre rather than to whichever circle contains it. So the touch target is larger than the drawn circle, and nothing on screen currently tells anyone that. A key highlight is therefore not only feedback that something registered — it is the only thing that would ever show which key the routing picked when a tap lands between two.

Offered the choice, the user took the key highlight alone. The touch-point marker lost for a reason worth keeping: it would show the gap between where the finger landed and which key won, and that is exactly the feedback SPEC's perceptual wedge does not want. The wedge is about aiming confidently at circles, and a display drawing the eye to near-misses the routing already absorbed works against it. The highlight alone still answers "did that register, and where".

Timing was deliberately not argued: instant highlight, brief hold, fade of about 150ms as opening values, to be set by eye once the keyboard is on the phone.

Held below the line against [compile-and-view-panel] rather than cleared. The reason was named to the user and accepted: this changes code nobody has compiled, and stacking a second unverified change on the first means debugging both together if the compile fails. The cost is seeing the effect a sitting later.

**Queue changes:** [key-press-feedback] filed and moved to Processed, held below the line against [compile-and-view-panel]; cross-referenced with [panel-switch-gestures], which shares `KeyboardPanel.kt`. SPEC gained a press-feedback line.

**Work processed:** kept — [key-press-feedback].
