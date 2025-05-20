package com.example.concertfinder.presentation.calendar_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = onClick
        ) {
            Text(
                text = "Calendar"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPreview() {
    CalendarScreen(
        onClick = {},
        modifier = Modifier
    )
}