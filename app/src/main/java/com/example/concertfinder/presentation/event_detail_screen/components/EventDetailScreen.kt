package com.example.concertfinder.presentation.event_detail_screen.components

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.presentation.event_detail_screen.EventDetailViewModel

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailScreen(
    event: Event,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: EventDetailViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    // Queue of info to display, as info is displayed, it is removed from the queue
    val infoQueue = mutableMapOf<String, String>(
        Pair(stringResource(R.string.event_description), event.description ?: ""),
        Pair(stringResource(R.string.event_information), event.info ?: ""),
        Pair(stringResource(R.string.additional_information), event.additionalInfo ?: ""),
        Pair(stringResource(R.string.please_note), event.pleaseNote ?: "")
    )

    Box( // Displays additional info over event screen if applicable
        contentAlignment = Alignment.Center
    ) {
        Column( // Displays event screen
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(innerPadding)
                // Blur the background if additional info is displayed
                .then(
                    if (uiState.value.showAdditionalInfo) {
                        Modifier
                            .blur(
                                radiusX = 16.dp,
                                radiusY = 16.dp,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            )
                            .background(color = MaterialTheme.colorScheme.background)
                    } else {
                        Modifier
                    }
                )
        ) {
            // ------------------------------------------------------------------------------------------------------------Event image
            AsyncImage(
                model = viewModel.getImageUrl(event.images),
                contentDescription = stringResource(R.string.event_image),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp, max = 200.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

            // -------------------------------------------------------------------------------------------------------------Event name
            Text(
                text = event.name ?: stringResource(R.string.no_event_name),
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_medium)
                    )
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

            // Gets distance from location
            val distanceFromLocation = viewModel.getDistanceFromLocation(
                event.location?.latitude ?: event.embedded?.venues?.get(0)?.location?.latitude,
                event.location?.longitude ?: event.embedded?.venues?.get(0)?.location?.longitude,
                DistanceUnit.Miles // TODO: Allow user to select distance unit
            )

            // -------------------------------------------------------------------------------------------------------------Event price, location, and distance from location
            PriceAndLocationRow(event, distanceFromLocation, modifier)

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

            val eventStartDate = viewModel.getFormattedEventStartDates(event.dates)
            val eventEndDate = viewModel.getFormattedEventEndDates(event.dates)

            // -------------------------------------------------------------------------------------------------------------Event start date
            if (eventStartDate != null) {
                Text(
                    text = eventStartDate,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))
            }
            // -------------------------------------------------------------------------------------------------------------Event end date
            if (eventEndDate != null) {
                Text(
                    text = eventEndDate,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_medium),
                        )
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))
            }

            // ------------------------------------------------------------------------------------------------------------Event description
            // Displays most relevant info on screen,
            // any additional info will be displayed when additional info button is clicked
            TextBlock(
                title = determineTextBlockTitle(infoQueue),
                text = determineTextBlockText(infoQueue),
                expanded = uiState.value.isDescriptionExpanded,
                onClick = { viewModel.toggleDescriptionExpanded() },
                clickable = true,
                modifier = modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

            // ------------------------------------------------------------------------------------------------------------Event additional info and get tickets buttons
            EventButtonRow(
                url = event.url,
                infoQueue = infoQueue,
                onToggleDisplayAdditionalInfo = {
                    viewModel.toggleDisplayAdditionalInfo()
                    viewModel.updateInfoQueue(infoQueue)
                },
                modifier = modifier
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

            // ------------------------------------------------------------------------------------------------------------Event classifications
            ClassificationFlowRow(
                classifications = event.classifications ?: emptyList(),
                modifier = modifier
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

            // ------------------------------------------------------------------------------------------------------------Event venue(s)

            // Checks if event has at least one venue
            if (event.embedded?.venues?.isNotEmpty() == true) {
                Column(
                    modifier = modifier
                        .animateContentSize()
                ) {
                    HorizontalDivider()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .clickable { viewModel.toggleVenuePageExpanded() }
                            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    ) {
                        Text(
                            text = stringResource(R.string.venue_information),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier.padding(vertical = dimensionResource(R.dimen.padding_medium))
                        )
                        Spacer(modifier = modifier.weight(1f))
                        if (uiState.value.isVenuePageExpanded) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.KeyboardArrowUp,
                                contentDescription = null,
                                modifier = modifier.padding(
                                    end = dimensionResource(R.dimen.padding_small)
                                )

                            )
                        } else {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = modifier.padding(
                                    end = dimensionResource(R.dimen.padding_small)
                                )
                            )
                        }
                    }
                    HorizontalDivider()

                    if (uiState.value.isVenuePageExpanded) {
                        Column {
                            for (venue in event.embedded.venues) {
                                VenueItem(
                                    findImage = { viewModel.getImageUrl(it) },
                                    onDisplayAdditionalInfo = {
                                        viewModel.updateInfoQueue(it)
                                        viewModel.toggleDisplayAdditionalInfo()
                                    },
                                    venue = venue
                                )
                            }
                        }
                    }
                }
            }

            // Checks if event has at least one attraction
            if (event.embedded?.attractions?.isNotEmpty() == true) {
                Column(
                    modifier = modifier
                        .animateContentSize()
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .clickable { viewModel.toggleAttractionPageExpanded() }
                            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    ) {
                        Text(
                            text = stringResource(R.string.attraction_information),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier.padding(vertical = dimensionResource(R.dimen.padding_medium))
                        )
                        Spacer(modifier = modifier.weight(1f))
                        if (uiState.value.isAttractionPageExpanded) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.KeyboardArrowUp,
                                contentDescription = null,
                                modifier = modifier.padding(
                                    end = dimensionResource(R.dimen.padding_small)
                                )

                            )
                        } else {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = modifier.padding(
                                    end = dimensionResource(R.dimen.padding_small)
                                )
                            )
                        }
                    }
                    HorizontalDivider()

                    if (uiState.value.isAttractionPageExpanded) {
                        Column {
                            for (attraction in event.embedded.attractions) {
                                AttractionItem(
                                    attraction = attraction,
                                    findImage = { viewModel.getImageUrl(it) },
                                    onDisplayAdditionalInfo = {
                                        viewModel.updateInfoQueue(it)
                                        viewModel.toggleDisplayAdditionalInfo()
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }

        // Displays additional info popup if applicable
        if (uiState.value.showAdditionalInfo) {

            // Handle back button press to close additional info popup
            BackHandler {
                viewModel.toggleDisplayAdditionalInfo()
            }

            AdditionalInfoPopup(
                infoMap = uiState.value.infoQueue,
                onDismiss = { viewModel.toggleDisplayAdditionalInfo() },
                modifier = modifier
            )
        }
    }
}

@Composable
fun AdditionalInfoPopup(
    infoMap: MutableMap<String, String>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
) {
    // Additional info popup
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(24.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Box(
            modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = modifier.verticalScroll(rememberScrollState())
            ) {
                infoMap.forEach {
                    if (it.value.isNotEmpty()) {
                        Row {
                            TextBlock(
                                title = it.key,
                                text = it.value,
                                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                            )
                            Spacer(modifier = modifier.weight(1f))
                        }
                    }
                }
                Spacer(modifier = modifier.height(32.dp))
            }
            // Close button
            Row(
                modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                Spacer(modifier = modifier.weight(1f))
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = modifier
                        .clickable {
                            onDismiss()
                        }
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_medium),
                            vertical = dimensionResource(R.dimen.padding_small)
                        )
                )
            }
        }
    }
}

@Composable
private fun EventButtonRow(
    url: String?,
    infoQueue: MutableMap<String, String>,
    onToggleDisplayAdditionalInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Event button row
    Row {
        // Checks if there is any undisplayed info
        if (infoQueue.values.any { it.isNotEmpty() }) {
            // Event additional info button
            InfoButton(
                text = stringResource(R.string.additional_info),
                onClick = onToggleDisplayAdditionalInfo,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_small)
                    )
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            InfoButton(
                text = stringResource(R.string.additional_info),
                enabled = false,
                modifier = modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_small)
                    )
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        // Checks if event has a url
        if (url != null) {
            // Event get tickets button
            UrlButton(
                context = LocalContext.current,
                content = stringResource(R.string.get_tickets),
                url = url.toString(),
                modifier = modifier
                    .padding(
                        end = dimensionResource(R.dimen.padding_medium),
                        start = dimensionResource(R.dimen.padding_small)
                    )
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            UrlButton(
                content = stringResource(R.string.get_tickets),
                enabled = false,
                modifier = modifier
                    .padding(
                        end = dimensionResource(R.dimen.padding_medium),
                        start = dimensionResource(R.dimen.padding_small)
                    )
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

private fun determineTextBlockTitle(infoQueue: MutableMap<String, String>): String {
    // Displays most relevant info on screen, any additional info will be displayed when additional info button is clicked
    return if (infoQueue.values.first().isNotEmpty()) {
        infoQueue.keys.first()

    } else if (infoQueue.values.elementAt(1).isNotEmpty()) {
        infoQueue.keys.elementAt(1)

    } else if (infoQueue.values.elementAt(2).isNotEmpty()) {
        infoQueue.keys.elementAt(2)

    } else if (infoQueue.values.elementAt(3).isNotEmpty()) {
        infoQueue.keys.elementAt(3)

    } else {
        "Event description"
    }
}

private fun determineTextBlockText(infoQueue: MutableMap<String, String>): String {
    // Displays most relevant info on screen, any additional info will be displayed when additional info button is clicked
    if (infoQueue.values.first().isNotEmpty()) {
        val result = infoQueue.values.first()
        infoQueue.remove(infoQueue.keys.first()) // Remove first element
        return result

    } else if (infoQueue.values.elementAt(1).isNotEmpty()) {
        val result = infoQueue.values.elementAt(1)
        infoQueue.remove(infoQueue.keys.first()) // Remove first element
        infoQueue.remove(infoQueue.keys.first()) // Remove second element
        return result

    } else if (infoQueue.values.elementAt(2).isNotEmpty()) {
        val result = infoQueue.values.elementAt(2)
        infoQueue.remove(infoQueue.keys.first()) // Remove first element
        infoQueue.remove(infoQueue.keys.first()) // Remove second element
        infoQueue.remove(infoQueue.keys.first()) // Remove third element
        return result

    } else if (infoQueue.values.elementAt(3).isNotEmpty()) {
        val result = infoQueue.values.elementAt(3)
        infoQueue.clear() // Remove all elements
        return result

    } else {
        infoQueue.clear() // Remove all elements
        return "Description not provided."
    }
}