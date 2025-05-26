package com.example.concertfinder.presentation.common_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.concertfinder.R

@Composable
fun ErrorScreen(
    message: String
) {
    Box(
        contentAlignment = Alignment.Companion.Center,
        modifier = Modifier.Companion.fillMaxSize()
    ) {
        if (message == "No events found") {
            Text(text = stringResource(R.string.no_events_found))
            Text(text = stringResource(R.string.try_different_search))
        } else {
            Text(text = "Error: $message")
        }
    }
}