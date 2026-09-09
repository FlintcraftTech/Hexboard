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
