package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concertfinder.R
import com.example.concertfinder.data.remote.event_dto.Attraction
import com.example.concertfinder.data.remote.event_dto.EventImage

@Composable
fun AttractionItem(
    findImage: (List<EventImage>?) -> String?,
    onDisplayAdditionalInfo: (MutableMap<String, String>) -> Unit,
    attraction: Attraction,
    modifier: Modifier = Modifier
) {
    
    val infoMap = mutableMapOf<String, String>(
        Pair("Description", attraction.description ?: ""),
        Pair("Additional Info", attraction.additionalInfo ?: "")
    )

    Column {
        AsyncImage(
            model = findImage(attraction.images),
            contentDescription = stringResource(R.string.attraction_image),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp, max = 200.dp)
                .background(color = MaterialTheme.colorScheme.secondary)
        )
        Column(
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

            Text(
                text = attraction.name.toString(),
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

            Row {
                InfoButton(
                    text = stringResource(R.string.information),
                    onClick = {
                        onDisplayAdditionalInfo(infoMap)
                    },
                    enabled = infoMap.values.any { it.isNotEmpty() },
                    modifier = modifier.weight(1f)
                )

                Spacer(modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)))

                UrlButton(
                    url = attraction.url ?: "",
                    content = stringResource(R.string.Profile),
                    modifier = modifier.weight(1f),
                    enabled = attraction.url != null
                )
            }
        }
    }
}