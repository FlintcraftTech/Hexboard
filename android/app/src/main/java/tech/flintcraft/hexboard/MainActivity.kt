package tech.flintcraft.hexboard

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.Locale
import tech.flintcraft.hexboard.ui.theme.HexboardTheme

/**
 * The app's own screen. It does three things: opens the system's input-method settings so
 * switching Hexboard on is findable rather than hunted for, carries a temporary dictation
 * test that calls Android's on-device recogniser, and hosts the keyboard surface so it can
 * be looked at and typed on without switching keyboards.
 *
 * The board here is development scaffolding, not the product — the product is
 * [HexboardImeService]. What this screen proves is that the config reaches the screen and
 * that a tap lands on the key it was aimed at.
 *
 * **The dictation test and the microphone permission it needs must not reach a shipped
 * build.** They exist to measure the platform recogniser on one handset; the microphone
 * is introduced properly, with its guarantees, by the in-keyboard voice input work, and
 * removing both from here is part of that work.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HexboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PanelPreviewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun PanelPreviewScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val catalogue = remember { LayoutCatalogue.fromAssets(context) }
    var chosenId by remember { mutableStateOf(LayoutPreference.storedId(context)) }
    val assetName = remember(chosenId) {
        LayoutPreference.resolve(catalogue.values.flatten(), chosenId)
    }
    val layout = remember(assetName) { KeyLayoutLoader.fromAssets(context, assetName) }
    var typed by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        // The controls scroll and the board stays put. This screen held one button and the
        // dictation test when it was written; the layout picker and the report entry made it
        // taller than the phone, and without a scroll the bottom of it could not be reached
        // at all — found on the Pixel 6 on 2026-09-09. The weight is what leaves the board
        // its own space rather than letting the controls push it off.
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Button(onClick = {
                context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }) {
                Text(stringResource(R.string.open_keyboard_settings))
            }
            LayoutPicker(
                catalogue = catalogue,
                currentId = layout.id,
                onChoose = { id ->
                    LayoutPreference.store(context, id)
                    chosenId = id
                    typed = ""
                },
                modifier = Modifier.padding(top = 16.dp)
            )
            DictationTest(modifier = Modifier.padding(top = 16.dp))
            ReportAProblem(
                layoutId = layout.id,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = typed.ifEmpty { "Tap the keys" },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (layout.allPanels.isEmpty()) {
            Text(
                text = "No panels in the key config.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            // onKey is named rather than passed as a trailing lambda. `onEmoji` was added to
            // HexboardBoard after `onKey`, so a trailing lambda binds to the emoji callback
            // and its parameter is a String — which is what failed the build of 2026-09-09.
            HexboardBoard(
                layout = layout,
                modifier = Modifier.fillMaxWidth(),
                onKey = { key -> typed = apply(key, typed) }
            )
        }
    }
}

/**
 * The layout picker: every shipped layout, grouped by language, with the current one marked.
 *
 * SPEC puts this in the app's own settings rather than on the keyboard surface. The
 * keyboard-surface version — a long-press or a gesture, reachable without leaving what you
 * are typing — lost because horizontal swipe already means "change panel", so a picker
 * there would have had to find a gesture the layout has not already spent.
 *
 * A language is named by its BCP 47 tag through [java.util.Locale], so the reader's own
 * device supplies the name in the reader's own language and no display name is stored in
 * any config.
 */
@Composable
private fun LayoutPicker(
    catalogue: Map<String, List<LayoutCatalogue.Entry>>,
    currentId: String,
    onChoose: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Layout", style = MaterialTheme.typography.titleMedium)
        if (catalogue.isEmpty()) {
            Text(
                text = "No layouts reached the app.",
                style = MaterialTheme.typography.bodyMedium
            )
            return@Column
        }
        catalogue.forEach { (language, entries) ->
            Text(
                text = Locale.forLanguageTag(language).displayLanguage
                    .ifEmpty { language.ifEmpty { "Unknown language" } },
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            entries.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onChoose(entry.id) }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = entry.id == currentId,
                        onClick = { onChoose(entry.id) }
                    )
                    Text(
                        text = entry.name.ifEmpty { entry.id },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/**
 * The standing "Report a problem" entry: a box to describe what is wrong with the layout,
 * the exact report shown beneath it, and a button that hands that text to the person's own
 * mail app.
 *
 * **Three things this deliberately is not.** It is not attached to a rating prompt — it is
 * always present and asks nobody how they are enjoying the app, because Google's in-app
 * review guidance forbids screening people before a rating and this must never be mistaken
 * for that. It does not send anything: the mail app opens with the text prefilled and the
 * person presses send, so Hexboard makes no network request and needs no internet
 * permission. And it cannot carry what anyone has typed on the keyboard — [ProblemReport]
 * takes five named values and there is no route from an input connection or a clipboard
 * into it.
 *
 * The composed text is shown in full before it goes, so the person can check that claim
 * for themselves rather than taking it on trust.
 *
 * Where `hexboard.reportAddress` is unset in `android/local.properties` — which it is on a
 * fresh clone, because this repository is public and the address is not in it — the whole
 * entry is absent.
 */
@Composable
private fun ReportAProblem(layoutId: String, modifier: Modifier = Modifier) {
    val address = BuildConfig.REPORT_ADDRESS
    if (address.isEmpty()) return

    val context = LocalContext.current
    var description by remember { mutableStateOf("") }
    var sendError by remember { mutableStateOf("") }

    val report = ProblemReport.compose(
        layoutId = layoutId,
        appVersion = BuildConfig.VERSION_NAME,
        androidVersion = Build.VERSION.RELEASE.orEmpty(),
        deviceModel = Build.MODEL.orEmpty(),
        description = description
    )

    Column(modifier = modifier) {
        Text(
            text = "Report a problem",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "A wrong letter, or a key in the wrong place? Say what you found. " +
                "Nothing you have typed on the keyboard is included.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("What is wrong") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        Text(
            text = "This is what will be sent:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = report,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
        Button(
            enabled = description.isNotBlank(),
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
                    putExtra(Intent.EXTRA_SUBJECT, ProblemReport.subject(layoutId))
                    putExtra(Intent.EXTRA_TEXT, report)
                }
                sendError = try {
                    context.startActivity(intent)
                    ""
                } catch (notInstalled: ActivityNotFoundException) {
                    "No mail app on this phone to open."
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Open my mail app")
        }
        if (sendError.isNotEmpty()) {
            Text(
                text = sendError,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * A button that runs one utterance through Android's on-device recogniser and shows what
 * came back, under a line saying whether this handset has on-device recognition at all.
 *
 * The on-device recogniser exists from API 31. Below that, or where the handset reports
 * none, the button does nothing and the line says so — there is deliberately no fallback
 * to the network recogniser, which would send audio off the device.
 */
@Composable
private fun DictationTest(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val available = remember {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
    }
    var status by remember { mutableStateOf("") }
    var heard by remember { mutableStateOf("") }

    val recognizer = remember {
        if (available) createOnDeviceRecognizer(context) else null
    }
    DisposableEffect(recognizer) {
        recognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { status = "Listening…" }
            override fun onBeginningOfSpeech() { status = "Hearing speech" }
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { status = "Recognising…" }
            override fun onError(error: Int) { status = "Recogniser error $error" }
            override fun onResults(results: Bundle?) {
                status = "Done"
                heard = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    .orEmpty()
            }
            override fun onPartialResults(partialResults: Bundle?) {
                partialResults
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?.let { heard = it }
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        onDispose { recognizer?.destroy() }
    }

    fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        heard = ""
        recognizer?.startListening(intent)
    }

    val askPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) listen() else status = "Microphone permission refused" }

    Column(modifier = modifier) {
        Text(
            text = when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.S ->
                    "On-device recognition needs Android 12; this phone is older."
                available -> "On-device recognition: available"
                else -> "On-device recognition: not available on this phone"
            },
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(
                enabled = recognizer != null,
                onClick = {
                    val granted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                    if (granted) listen() else askPermission.launch(Manifest.permission.RECORD_AUDIO)
                }
            ) {
                Text("Dictate (test)")
            }
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp)
            )
        }
        Text(
            text = heard.ifEmpty { "Recognised text appears here." },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun createOnDeviceRecognizer(context: Context): SpeechRecognizer? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        SpeechRecognizer.createOnDeviceSpeechRecognizer(context)
    } else {
        null
    }

/**
 * What a key press does to the text so far, on this screen only.
 *
 * The real input connection belongs to [HexboardImeService]; this is the smallest thing
 * that makes a tap visible while the panel is being looked at on screen. Shift is inert
 * here.
 */
private fun apply(key: Key, text: String): String = when (key.action) {
    Key.ACTION_BACKSPACE -> text.dropLast(1)
    Key.ACTION_INSERT, Key.ACTION_ENTER -> text + (key.output ?: "")
    else -> text
}
