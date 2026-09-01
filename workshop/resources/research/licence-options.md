# Licence options for Hexboard

Researched 2026-08-04, during the /plan session that processed `[licence-and-go-public]`.
Not legal advice — a summary of what the licences say.

## The intent being matched

Alex's stated intent: people may read the source, and may fork it *in order to build Hexboard in
another language*, but they don't get a general right to copy it.

This is a **purpose-limited derivative right**, and nothing standard grants exactly that. Standard
source-available licences limit by *who* may use the software or *for what commercial purpose* —
not by "only this kind of derivative is allowed." So an exact match would have to be bespoke.

## The PolyForm family

PolyForm is a set of plain-language source-available licences drafted by licensing lawyers, meant
to fill gaps left by the standard open-source menu. The three relevant members:

- **Noncommercial 1.0.0** — permits any noncommercial purpose: personal use, research, study,
  hobby projects, amateur pursuits. Charities, educational institutions and government bodies are
  permitted regardless of funding. Commercial use is not.
- **Internal Use** — a company may run it for its own internal operations, but may not provide
  services to others with it.
- **Shield** — broadly permissive, including commercially, except that you may not use it to
  compete with the licensor's offerings.

## Options weighed

1. **PolyForm Noncommercial 1.0.0** — chosen. A hobbyist forking to build a German Hexboard is
   squarely permitted; anyone selling Hexboard on the Play Store is not. Known gap: it also permits
   noncommercial forks that weren't the intent, so someone could publish a free rival keyboard
   built on this code. Judged an acceptable gap — the realistic threat is commercial appropriation,
   not a hobbyist rival.
2. **A custom licence** — would match the intent exactly on paper, but bespoke licence text is
   risky without a lawyer: ambiguous drafting is often unenforceable, and unrecognised terms deter
   the contributors the project wants. Rejected.
3. **Public repo with no licence at all** — source visible, no rights granted, forks permitted case
   by case in writing. Maximum control and honest, but nobody can contribute without asking first.
   Rejected as too high-friction for a project that wants language forks.

Note that source-available terms of any kind exclude the project from some open-source ecosystems
and can deter contributors. That trade-off is accepted knowingly.

## Sources

- https://polyformproject.org/licenses/noncommercial/1.0.0
- https://github.com/polyformproject/polyform-licenses
- https://writing.kemitchell.com/2021/06/20/License-Round-Up
