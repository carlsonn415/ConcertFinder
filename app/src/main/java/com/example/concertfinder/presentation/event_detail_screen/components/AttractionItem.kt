package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concertfinder.R
import com.example.concertfinder.data.remote.event_dto.Attraction
import com.example.concertfinder.data.remote.event_dto.Classification
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.data.remote.event_dto.Genre
import com.example.concertfinder.data.remote.event_dto.Segment
import com.example.concertfinder.data.remote.event_dto.SubGenre

@Composable
fun AttractionItem(
    findImage: (
        List<EventImage>?,
        aspectRatio: String,
        minImageWidth: Int
    ) -> String?,
    onDisplayAdditionalInfo: (MutableMap<String, String>) -> Unit,
    attraction: Attraction,
    modifier: Modifier = Modifier
) {
    
    val infoMap = mutableMapOf<String, String>(
        Pair("Description", attraction.description ?: ""),
        Pair("Additional Info", attraction.additionalInfo ?: "")
    )

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            AsyncImage(
                model = findImage(attraction.images, "4_3", 360),
                contentDescription = stringResource(R.string.attraction_image),
                contentScale = ContentScale.Fit,
                error = painterResource(R.drawable.image_placeholder),
                placeholder = painterResource(R.drawable.image_placeholder),
                modifier = modifier
                    .size(width = 100.dp, height = 75.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            )

            Spacer(modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)))

            Column {

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                Text(
                    text = attraction.name.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_extra_small)))

                Row {
                    InfoButton(
                        text = stringResource(R.string.info),
                        onClick = {
                            onDisplayAdditionalInfo(infoMap)
                        },
                        enabled = infoMap.values.any { it.isNotEmpty() },
                        modifier = modifier.weight(1f),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                    Spacer(modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small)))

                    UrlButton(
                        url = attraction.url ?: "",
                        content = stringResource(R.string.profile),
                        modifier = modifier.weight(1f),
                        enabled = attraction.url != null,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                }
            }
        }
        if (attraction.classifications?.get(0)?.segment?.name == "Music") {

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

            ClassificationFlowRow(
                classifications = attraction.classifications,
                showSegment = false,
                modifier = modifier
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AttractionItemPreview() {
    AttractionItem(
        attraction = Attraction(
            name = "Test Attraction",
            images = emptyList(),
            description = "Test Description",
            additionalInfo = "Test Additional Info",
            url = "https://www.google.com",
            classifications = listOf(
                Classification(
                    segment = Segment(name = "Music"),
                    genre = Genre(name = "Test Genre"),
                    subGenre = SubGenre(name = "Test SubGenre")
                )
            )
        ),
        findImage = {
            images, aspectRatio, minImageWidth -> ""
        },
        onDisplayAdditionalInfo = {
        }
    )
}