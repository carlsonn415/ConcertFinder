package com.example.concertfinder.presentation.event_detail_screen.components.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R

@Composable
fun TextBlock(
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    clickable: Boolean = false,
    expanded: Boolean = true,
) {

    Column(
        modifier = if(clickable) modifier
            .clickable { onClick() }
            .animateContentSize() else modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
        )

        if (expanded) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (text.length > 200 && clickable) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (expanded) "Show Less" else "Show More",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_small))
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
    }

}

@Preview
@Composable
private fun TextBlockPreview() {
    TextBlock(
        title = "test title",
        text = "This is a test text block"
    )
}