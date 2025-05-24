package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.data.remote.event_dto.Classification

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassificationFlowRow(
    classifications: List<Classification>,
    modifier: Modifier = Modifier,
    showSegment: Boolean = true,
) {
    if (classifications.isNotEmpty()) {
        FlowRow(
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            if (showSegment) {
                FlowRowItem(
                    text = classifications.first().segment?.name.toString(),
                )
            }

            classifications.forEach {
                FlowRowItem(
                    text = it.genre?.name.toString(),
                )
            }

            classifications.forEach {
                if (it.genre?.name != it.subGenre?.name) {
                    FlowRowItem(
                        text = it.subGenre?.name.toString(),
                    )
                }
            }
        }
    }
}

@Composable
fun FlowRowItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(end = dimensionResource(R.dimen.padding_extra_small))
            .clip(MaterialTheme.shapes.extraSmall)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
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
            modifier = modifier
                .padding(
                    vertical = dimensionResource(R.dimen.padding_extra_small),
                    horizontal = dimensionResource(R.dimen.padding_small)
                )

        )
    }
}