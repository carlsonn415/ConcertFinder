package com.example.lineup_app.presentation.common_ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lineup_app.R

@Composable
fun FlowRowItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(end = dimensionResource(R.dimen.padding_extra_small))
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.extraSmall
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .padding(
                    vertical = dimensionResource(R.dimen.padding_extra_small),
                    horizontal = dimensionResource(R.dimen.padding_small)
                )

        )
    }
}