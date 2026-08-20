package tech.flintcraft.hexboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import tech.flintcraft.hexboard.ui.theme.HexboardTheme

/**
 * A host for the keyboard panel, so it can be looked at and typed on without an input
 * method service.
 *
 * This is scaffolding for development, not the product: registering Hexboard as a real
 * Android keyboard is separate work. What it proves is that the config reaches the screen
 * and that a tap lands on the key it was aimed at.
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
    val panel = remember(layout) { layout.panel("qwerty") }
    var typed by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = typed.ifEmpty { "Tap the keys" },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        if (panel == null) {
            Text(
                text = "No QWERTY panel in the key config.",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            KeyboardPanel(
                panel = panel,
                modifier = Modifier.fillMaxWidth()
            ) { key ->
                typed = apply(key, typed)
            }
        }
    }
}

/**
 * What a key press does to the text so far.
 *
 * The real input connection belongs to the input method service; this is the smallest
 * thing that makes a tap visible while the panel is being looked at on screen. Shift is
 * inert here — the config declares it, and what it does is settled with the service.
 */
private fun apply(key: Key, text: String): String = when (key.action) {
    Key.ACTION_BACKSPACE -> text.dropLast(1)
    Key.ACTION_INSERT, Key.ACTION_ENTER -> text + (key.output ?: "")
    else -> text
}
