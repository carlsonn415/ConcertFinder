package com.example.concertfinder.presentation.event_detail_screen.components

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.net.toUri

@Composable
fun UrlButton(
    context: Context,
    content: String,
    url: String,
    modifier: Modifier = Modifier
) {
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
                    Toast.makeText(context, "No web browser found", Toast.LENGTH_SHORT).show() // TODO: make into snackbar
                    Log.e("UrlButton", "No web browser found")
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error opening URL", Toast.LENGTH_SHORT).show() // TODO: make into snackbar
                Log.e("UrlButton", "Error opening URL: $url", e)
            }
        },
        modifier = modifier
    ) {
        Text(text = content)
    }
}