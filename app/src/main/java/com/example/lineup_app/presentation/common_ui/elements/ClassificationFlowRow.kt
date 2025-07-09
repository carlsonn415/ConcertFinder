package com.example.lineup_app.presentation.common_ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.lineup_app.R
import com.example.lineup_app.data.remote.event_dto.Classification

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