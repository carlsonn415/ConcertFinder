package com.example.concertfinder.presentation.saved_events_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.data.model.Event

@Composable
fun SavedEventsScreen(
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = { onClick(Event()) },
        ) {
            Text(
                text = "Events"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventsScreenPreview() {
    SavedEventsScreen(
        onClick = {},
        modifier = Modifier
    )
}