package com.example.concertfinder.presentation.common_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
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
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    maxLines: Int = 3
) {
    if (classifications.isNotEmpty()) {

        val genreList = classifications.mapNotNull { it.genre?.name }.toSet()
        val subGenreList = classifications.mapNotNull { it.subGenre?.name }.toSet()

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small)),
            horizontalArrangement = horizontalArrangement,
            maxLines = maxLines,
            overflow = FlowRowOverflow.Clip,
            modifier = modifier
        ) {
            if (showSegment) {
                FlowRowItem(
                    text = classifications.first().segment?.name.toString(),
                )
            }

            genreList.forEach {
                FlowRowItem(
                    text = it,
                )
            }

            subGenreList.forEach {
                if (!genreList.contains(it)) {
                    FlowRowItem(
                        text = it,
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