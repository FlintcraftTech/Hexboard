# def1f5a — README expanded from one sentence to a full public-facing page, with a rendered layout screenshot and the method named as Throughliner

The licence section an earlier session wrote was left exactly as it was. Everything added sits above it, because what was missing was not the terms but the entire description of the project — a visitor reached a thorough licence for something described in one sentence.

Five things went in, as planned. What Hexboard is, leading on the zag rows and stating the perceptual claim properly: people aim more confidently at a circle than at a shape with visible corners, so they aim closer to the centre and their taps land better. The page is explicit about what is *not* being claimed — hex-shaped keyboards do not mis-route taps — because SPEC disowns that functional version and a README is exactly where the stronger, wronger claim would creep back in. It also says plainly that this is a claim about people and therefore testable rather than proven. Then an honest status section saying there is no working keyboard and nothing to install; how to open the browser prototype and that it is frozen; and a pointer to `LOG/` and `QUEUE.md` explaining why the planning record is in a public repository at all.

Two things were resolved during the build rather than planned.

The screenshot needed producing, not just embedding. Rendering `hexboard17.html` headlessly in Microsoft Edge gave the image, but at every window size the horizontal-swipe carousel showed slivers of the neighbouring RARE and SYMBOLS panels at both edges, which read as clipped keys rather than as a carousel. Cropping to the QWERTY panel with Pillow fixed it. Worth recording because the obvious approach — pick a better window size — cannot work: the carousel is always there, so no viewport shows one panel alone.

The method's name was wrong, and the check that caught it was worth making. The section first said the project was built with Sovereign Implementer and linked `flintcraft.tech`. Fetching that page showed it never uses that name — it presents the method publicly as **Throughliner**. The link was left in and the name changed, on the reasoning that a README written for strangers should use the name the page it links to actually uses; sending a reader to a page that does not mention the name they just read is worse than either dropping the link or keeping the internal name. Alex made that call when the alternatives were put to them.

That mismatch turned out to be broader than this file, and the rest of it is filed as [throughliner-doc-drift] rather than fixed here — CLAUDE.md still uses the old name throughout, and whether the LOG entries should be rewritten is a real question, since they record what those sessions actually said.

**Files touched:** `README.md` (five sections added above the untouched licence section); `resources/images/hexboard-layout.png` (new, 636×342).

**Routed to Captures:** [throughliner-doc-drift], [github-org-case-drift]
