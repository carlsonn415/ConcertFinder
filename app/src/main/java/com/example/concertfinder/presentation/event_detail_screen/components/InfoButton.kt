package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InfoButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    if (enabled) {
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            modifier = modifier
        ) {
            Text(
                text = text,
                maxLines = 1
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            enabled = false,
            modifier = modifier
        ) {
            Text(
                text = text,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoButtonPreview() {
    InfoButton(text = "Additional info")
}