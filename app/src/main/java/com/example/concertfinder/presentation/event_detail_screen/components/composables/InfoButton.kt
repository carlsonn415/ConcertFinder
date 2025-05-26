package com.example.concertfinder.presentation.event_detail_screen.components.composables

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit

@Composable
fun InfoButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    if (enabled) {
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.small,
            modifier = modifier
        ) {
            Text(
                text = text,
                fontSize = fontSize,
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
                fontSize = fontSize,
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