package com.example.concertfinder.presentation.event_detail_screen.components.composables

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.core.net.toUri
import com.example.concertfinder.R
import com.example.concertfinder.data.local.AppSnackbarManager

@Composable
fun UrlButton(
    content: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    context: Context = LocalContext.current,
    url: String = "",
    fontSize: TextUnit = TextUnit.Unspecified
) {
    if (enabled) {
        Button(
            onClick = {
                // Ensure the URL starts with "https://"
                val webpageUrl = if (!url.startsWith("https://")) {
                    "https://$url"
                } else {
                    url
                }

                try {
                    val intent = Intent(Intent.ACTION_VIEW, webpageUrl.toUri())
                    // Check if there's an activity that can handle the intent
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        // No activity found to handle the intent
                        AppSnackbarManager.showSnackbar(context, R.string.no_web_browser_found)
                        Log.e("UrlButton", "No web browser found")
                    }
                } catch (e: Exception) {
                    AppSnackbarManager.showSnackbar(context, R.string.error_opening_url)
                    Log.e("UrlButton", "Error opening URL: $url", e)
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = modifier
        ) {
            Text(
                text = content,
                fontSize = fontSize,
                maxLines = 1
            )
        }
    } else {
        OutlinedButton(
            onClick = {},
            shape = MaterialTheme.shapes.small,
            enabled = false,
            modifier = modifier
        ) {
            Text(
                text = content,
                fontSize = fontSize,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun UrlButtonPreview() {
    UrlButton(content = "Get tickets")
}