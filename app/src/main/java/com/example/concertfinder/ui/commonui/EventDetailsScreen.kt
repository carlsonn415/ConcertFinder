package com.example.concertfinder.ui.commonui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EventDetailsScreen(modifier: Modifier = Modifier.Companion) {
    Box(
        contentAlignment = Alignment.Companion.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Event Details"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailsScreenPreview() {
    EventDetailsScreen()
}