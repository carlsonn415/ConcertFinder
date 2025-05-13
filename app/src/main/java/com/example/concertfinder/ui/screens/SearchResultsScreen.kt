package com.example.concertfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchResultsScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = onClick,
        ) {
            Text(
                text = "Results"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsScreenPreview() {
    SearchResultsScreen(
        onClick = {},
        modifier = Modifier
    )
}