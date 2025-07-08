package com.example.concertfinder.presentation.filter_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.concertfinder.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClassificationFlowRowClickable(
    segmentName: String,
    genreNames: List<String>,
    subgenreNames: List<String>,
    onSegmentDeleted: (String) -> Unit,
    onGenreDeleted: (String) -> Unit,
    onSubgenreDeleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (segmentName != "") {

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small)),
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
        ) {
            ClassificationItemClickable(
                classificationName = segmentName,
                onClassificationDeleted = onSegmentDeleted
            )
            genreNames.forEach {
                ClassificationItemClickable(
                    classificationName = it,
                    onClassificationDeleted = onGenreDeleted
                )
            }
            subgenreNames.forEach {
                ClassificationItemClickable(
                    classificationName = it,
                    onClassificationDeleted = onSubgenreDeleted
                )
            }
        }
    }
}