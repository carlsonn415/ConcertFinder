package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R

@Composable
fun TextBlock(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview
@Composable
private fun TextBlockPreview() {
    TextBlock(
        title = "Test Title",
        text = "This is a test text block"
    )
}