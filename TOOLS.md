# TOOLS

Environment facts learned by running things, one line per fact. Written when learned, so a
later session need not rediscover them.

- `java` is not on PATH and `JAVA_HOME` is unset from Claude's shell, so `android/gradlew`
  fails immediately — checked 2026-09-05. Android Studio brings its own JDK, so this says
  nothing about whether the project builds there.
- `gradle` is not on PATH — checked 2026-09-05.
- `adb` is not on PATH, so nothing here can reach a connected handset — checked 2026-09-05.
- `python` is on PATH and resolves to Inkscape's bundled Python 3.12 from Claude's shell —
  checked 2026-09-05. Fine for the project's own scripts, which use only the standard
  library.
- `curl` works and reaches the network — checked 2026-09-05 against sourceforge.net and
  raw.githubusercontent.com.
- Browser and preview tool calls from this session can sit on an "allow once" prompt the
  user cannot see or click, so a page that needs looking at is sent to the user as a file
  rather than driven here.
- The browser pane does get through a proof-of-work bot challenge that defeats a plain
  page fetch — checked 2026-09-17 against the Leipzig Corpora Collection's own pages,
  which a fetch could not read and the pane could. So a page behind that kind of wall is
  worth one attempt in the pane before it is handed over.
- `gh` is installed and signed in — version 2.96.0, checked 2026-09-17.
- Throughliner channel: none published. The plugin is installed from a marketplace
  registered as a local folder on this machine rather than from the stable or beta
  channel, so the weekly update check has no published channel to read and this project is
  treated as stable. Checked 2026-09-17. The path is deliberately not written here, this
  repository being public.
