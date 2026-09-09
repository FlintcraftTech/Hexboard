package tech.flintcraft.hexboard

/**
 * The text of a problem report about a layout.
 *
 * SPEC no longer holds a layout back until someone who reads the language has confirmed
 * it. What catches an error instead is that the app carries a standing "Report a problem"
 * entry naming the layout in use and letting somebody say what is wrong. Layouts are not
 * designed here — each is transcribed from an open-source keyboard's own layout data — so
 * what goes wrong is a transcription error, a letter in the wrong slot, which a user
 * notices at once and a report fixes in one line.
 *
 * **The privacy guarantee, and it is structural rather than a promise.** A keyboard that
 * sends reports is one careless keystroke away from being a keyboard that sends your text.
 * [compose] takes five values and nothing else exists for it to take: there is no
 * parameter here through which an input connection, a clipboard or any key history could
 * reach a report, so "it cannot carry what you typed" is a property of this signature. It
 * is a pure function for that reason — what it can and cannot contain is checkable without
 * a running app.
 *
 * Nothing here sends anything either. The caller hands the composed text to the person's
 * own mail app, which is why Hexboard needs no internet permission for this and why the
 * person reads the report before it goes.
 */
object ProblemReport {

    /**
     * The report, as the person will see it and as their mail app will carry it.
     *
     * @param layoutId the config id of the layout being typed on, e.g. `qwerty-en`
     * @param appVersion Hexboard's own version name
     * @param androidVersion the Android release the handset runs
     * @param deviceModel the handset model
     * @param description what the person typed about what is wrong
     */
    fun compose(
        layoutId: String,
        appVersion: String,
        androidVersion: String,
        deviceModel: String,
        description: String
    ): String = buildString {
        appendLine("Layout: $layoutId")
        appendLine("Hexboard: $appVersion")
        appendLine("Android: $androidVersion")
        appendLine("Device: $deviceModel")
        appendLine()
        append(description.trim())
    }.trimEnd()

    /** The subject line, which names the layout so reports sort by it. */
    fun subject(layoutId: String): String = "Hexboard layout problem: $layoutId"
}
