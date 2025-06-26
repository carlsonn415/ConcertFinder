package com.example.concertfinder.presentation.discover_screen.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorListItem(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(message)
}