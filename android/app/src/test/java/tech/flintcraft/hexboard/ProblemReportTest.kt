package tech.flintcraft.hexboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * What a problem report carries, and — the part that matters — what it cannot carry.
 *
 * A keyboard that sends reports is one careless keystroke from being a keyboard that sends
 * your text. The guarantee against that is not a promise in a comment: [ProblemReport] is a
 * pure function over five named values, so what can reach a report is decided by its
 * signature and is checkable here without a running app. The last test asserts exactly
 * that, which is what makes "it cannot carry what you typed" a property of the code.
 *
 * Whether the mail app actually opens is a fact about a handset and belongs to an install.
 */
class ProblemReportTest {

    private val report = ProblemReport.compose(
        layoutId = "qwerty-en",
        appVersion = "1.0",
        androidVersion = "14",
        deviceModel = "Pixel 6",
        description = "The Z key types an X."
    )

    @Test
    fun itNamesTheLayoutTheAppAndTheHandset() {
        listOf("qwerty-en", "1.0", "14", "Pixel 6").forEach { value ->
            assertTrue(
                "A report should name '$value', so whoever reads it knows which layout on " +
                    "which phone. Report was:\n$report",
                report.contains(value)
            )
        }
    }

    @Test
    fun itCarriesTheDescriptionAndNoOtherFreeText() {
        assertTrue(
            "A report should carry what the person wrote. Report was:\n$report",
            report.contains("The Z key types an X.")
        )
        val everythingElse = report
            .lineSequence()
            .filterNot { it.contains("The Z key types an X.") }
            .filter { it.isNotBlank() }
            .toList()
        val labelled = everythingElse.filter { line ->
            LABELS.any { line.startsWith("$it: ") }
        }
        assertEquals(
            "Every line of a report other than the description should be one of the four " +
                "labelled fields. These were not: ${everythingElse - labelled.toSet()}",
            everythingElse,
            labelled
        )
    }

    @Test
    fun anEmptyDescriptionLeavesTheFieldsIntact() {
        val blank = ProblemReport.compose("qwerty-ru", "1.0", "14", "Pixel 6", "   ")
        assertTrue(
            "A report with nothing typed should still name its layout.",
            blank.contains("qwerty-ru")
        )
        assertTrue(
            "A blank description should not leave stray whitespace at the end of a report.",
            blank == blank.trimEnd()
        )
    }

    @Test
    fun composeTakesFiveStringsAndNothingElse() {
        // Java reflection rather than kotlin-reflect, which is not a dependency of this
        // module. Parameter names are not readable this way; the count and the types are,
        // and they are what the guarantee turns on.
        val methods = ProblemReport::class.java.declaredMethods.filterNot { it.isSynthetic }
        val compose = methods.single { it.name == "compose" }
        assertEquals(
            "ProblemReport.compose takes ${compose.parameterTypes.size} parameters. It must " +
                "take exactly the five values a report is built from — a sixth parameter is " +
                "the route by which an input connection, a clipboard or a key history could " +
                "reach a report, and the whole guarantee here is that no such route exists.",
            5,
            compose.parameterTypes.size
        )
        assertTrue(
            "Every parameter of compose must be a String. Anything else is an object that " +
                "could carry more than the field it is named for. Got: " +
                compose.parameterTypes.map { it.simpleName },
            compose.parameterTypes.all { it == String::class.java }
        )
        assertEquals(
            "ProblemReport should expose compose and subject and nothing else, so there is " +
                "one way to build a report and it is the one checked above.",
            setOf("compose", "subject"),
            methods.map { it.name }.toSet()
        )
    }

    private companion object {
        val LABELS = listOf("Layout", "Hexboard", "Android", "Device")
    }
}
