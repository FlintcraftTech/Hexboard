package tech.flintcraft.hexboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
    val layout = remember { KeyLayoutLoader.fromAssets(context) }
    var typed by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = {
                context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }) {
                Text(stringResource(R.string.open_keyboard_settings))
            }
            DictationTest(modifier = Modifier.padding(top = 16.dp))
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
            HexboardBoard(
                layout = layout,
                modifier = Modifier.fillMaxWidth()
            ) { key ->
                typed = apply(key, typed)
            }
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
