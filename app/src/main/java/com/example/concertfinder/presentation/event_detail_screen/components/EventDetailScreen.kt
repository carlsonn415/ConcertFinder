package com.example.concertfinder.presentation.event_detail_screen.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.example.concertfinder.R
import com.example.concertfinder.data.local.AppSnackbarManager
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.presentation.common_ui.ClassificationFlowRow
import com.example.concertfinder.presentation.common_ui.MapItem
import com.example.concertfinder.presentation.event_detail_screen.EventDetailViewModel
import com.example.concertfinder.presentation.event_detail_screen.components.composables.AdditionalInfoDialog
import com.example.concertfinder.presentation.event_detail_screen.components.composables.AddressText
import com.example.concertfinder.presentation.event_detail_screen.components.composables.AttractionItem
import com.example.concertfinder.presentation.event_detail_screen.components.composables.InfoButton
import com.example.concertfinder.presentation.event_detail_screen.components.composables.PriceAndLocationRow
import com.example.concertfinder.presentation.event_detail_screen.components.composables.TextBlock
import com.example.concertfinder.presentation.event_detail_screen.components.composables.UrlButton
import com.example.concertfinder.presentation.event_detail_screen.components.composables.VenueItem
import com.example.concertfinder.presentation.ui.theme.MyIcons
import com.example.concertfinder.presentation.utils.AppDestinations
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventDetailScreen(
    event: Event,
    navBackStackEntry: NavBackStackEntry,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: EventDetailViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(navBackStackEntry) {
        // Check if the current destination is THIS screen
        if (navBackStackEntry.destination.route == AppDestinations.EVENT_DETAILS) {
            // Access the state within the scroll behavior
            val topAppBarState = scrollBehavior.state
            if (topAppBarState.heightOffset != 0f || topAppBarState.contentOffset != 0f) {
                Log.d("TopAppBarReset", "Resetting TopAppBar state for ${navBackStackEntry.destination.route}")
                topAppBarState.heightOffset = 0f
                topAppBarState.contentOffset = 0f
            }
        }
    }

    // Queue of info to display, as info is displayed, it is removed from the queue
    val infoQueue = mutableMapOf<String, String>(
        Pair(stringResource(R.string.event_description), event.description ?: ""),
        Pair(stringResource(R.string.event_information), event.info ?: ""),
        Pair(stringResource(R.string.additional_information), event.additionalInfo ?: ""),
        Pair(stringResource(R.string.please_note), event.pleaseNote ?: "")
    )

    // Displays additional info dialog over event screen if applicable
    Box {
        // Displays event screen
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            state = lazyListState,
            modifier = modifier
                .fillMaxWidth()
                .padding(innerPadding)
                // Blur the background if additional info dialog is displayed
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
            // Event block
            item {
                // ------------------------------------------------------------------------------------------------------------Event image
                AsyncImage(
                    model = viewModel.getImageUrl(event.images),
                    contentDescription = stringResource(R.string.event_image),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.image_placeholder),
                    placeholder = painterResource(R.drawable.image_placeholder),
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

                val firstVenue = event.embedded?.venues?.firstOrNull()
                val latitude = event.location?.latitude ?: firstVenue?.location?.latitude
                val longitude = event.location?.longitude ?: firstVenue?.location?.longitude

                val distanceFromLocation = if (latitude != null && longitude != null) {
                    viewModel.getDistanceFromLocation(
                        latitude,
                        longitude,
                        DistanceUnit.Miles
                    )
                } else {
                    // Handle case where location is completely unavailable
                    "Unknown distance"
                }

                // -------------------------------------------------------------------------------------------------------------Event price, location, and distance from location
                PriceAndLocationRow(event, distanceFromLocation, modifier)

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                val eventStartDate = viewModel.getFormattedEventStartDates(event.dates)
                val eventEndDate = viewModel.getFormattedEventEndDates(event.dates)

                // -------------------------------------------------------------------------------------------------------------Event start date
                if (eventStartDate != null) {
                    Text(
                        text = "Start: $eventStartDate",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    )

                    Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))
                }
                // -------------------------------------------------------------------------------------------------------------Event end date
                if (eventEndDate != null) {
                    Text(
                        text = "End: $eventEndDate",
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
                    modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

                // ------------------------------------------------------------------------------------------------------------Location

                HorizontalDivider()
                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                Text(
                    text = stringResource(R.string.location),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_medium)
                        )
                )
                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .clickable {
                            openMapsAndSearch(context = context, getEventAddress(event))
                        }
                ) {
                    AddressText(
                        event,
                        clickable = true,
                        modifier = modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                            .widthIn(max = 320.dp)
                    )

                    Spacer(modifier = modifier.weight(1f))

                    Icon(
                        imageVector = MyIcons.goToLocation,
                        contentDescription = null,
                        modifier = modifier
                            .padding(
                                end = dimensionResource(R.dimen.padding_medium)
                            )
                            .size(dimensionResource(R.dimen.icon_button_size))
                    )
                }

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                MapItem(
                    latitude = latitude ?: 0.0,
                    longitude = longitude ?: 0.0,
                    eventTitle = event.name ?: stringResource(R.string.no_event_name),
                    showMarker = true,
                    showRadius = false,
                    modifier = modifier
                        .height(dimensionResource(R.dimen.event_details_map_height))
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                )

                // ------------------------------------------------------------------------------------------------------------Event venue(s)

                // Checks if event has at least one venue
                if (event.embedded?.venues?.isNotEmpty() == true) {

                    HorizontalDivider()
                    Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                    // Checks if event has more than one venue
                    if (event.embedded.venues.size == 1) {
                        Text(
                            text = stringResource(R.string.venue),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                                )
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.venues),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = modifier
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                                )
                        )
                    }

                    Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_small)))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            dimensionResource(R.dimen.padding_medium)
                        )
                    ) {
                        for (venue in event.embedded.venues) {
                            VenueItem(
                                findImage = { images, aspectRatio, minImageWidth ->
                                    viewModel.getImageUrl(images, aspectRatio, minImageWidth)
                                },
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

            //----------------------------------------------------------------------------------------------------------------Event attraction(s)

            // Checks if event has at least one attraction
            if (event.embedded?.attractions?.isNotEmpty() == true) {
                // Attraction header
                item(key = "attraction_header") {
                    Column {

                        HorizontalDivider()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .clickable {
                                    viewModel.toggleAttractionPageExpanded()
                                }
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
                                    imageVector = MyIcons.arrowUp,
                                    contentDescription = null,
                                    modifier = modifier.padding(
                                        end = dimensionResource(R.dimen.padding_small)
                                    )

                                )
                            } else {
                                Icon(
                                    imageVector = MyIcons.arrowDown,
                                    contentDescription = null,
                                    modifier = modifier.padding(
                                        end = dimensionResource(R.dimen.padding_small)
                                    )
                                )
                            }
                        }
                    }
                }

                if (uiState.value.isAttractionPageExpanded) {
                    // Attraction list
                    items(event.embedded.attractions) { attraction ->

                        AttractionItem(
                            attraction = attraction,
                            findImage = { images, aspectRatio, minImageWidth ->
                                viewModel.getImageUrl(
                                    images,
                                    aspectRatio,
                                    minImageWidth
                                )
                            },
                            onDisplayAdditionalInfo = {
                                viewModel.updateInfoQueue(it)
                                viewModel.toggleDisplayAdditionalInfo()
                            },
                        )
                    }
                }

                item() {
                    Spacer(
                        modifier = modifier.height(
                            dimensionResource(R.dimen.padding_extra_large)
                        )
                    )
                }
            }
        }

        val isAttractionPageExpanded = uiState.value.isAttractionPageExpanded
        val attractionsCount = event.embedded?.attractions?.size ?: 0

        // Scrolls to show attractions when attraction list is expanded
        LaunchedEffect(isAttractionPageExpanded, attractionsCount) {
            if (isAttractionPageExpanded && attractionsCount > 0) {
                scope.launch {
                    lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount - attractionsCount - 1)
                }
            }
        }

        // Displays additional info popup if applicable
        if (uiState.value.showAdditionalInfo) {

            AdditionalInfoDialog(
                infoMap = uiState.value.infoQueue,
                onDismiss = { viewModel.toggleDisplayAdditionalInfo() },
                modifier = modifier,
            )
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
    return if (infoQueue.values.firstOrNull()?.isNotEmpty() ?: return "Event description") {
        infoQueue.keys.first()

    } else if (infoQueue.values.elementAtOrNull(1)?.isNotEmpty() ?: return "Event description") {
        infoQueue.keys.elementAt(1)

    } else if (infoQueue.values.elementAtOrNull(2)?.isNotEmpty() ?: return "Event description") {
        infoQueue.keys.elementAt(2)

    } else if (infoQueue.values.elementAtOrNull(3)?.isNotEmpty() ?: return "Event description") {
        infoQueue.keys.elementAt(3)
    } else {
        "Event description"
    }
}

private fun determineTextBlockText(infoQueue: MutableMap<String, String>): String {
    // Displays most relevant info on screen, any additional info will be displayed when additional info button is clicked
    if (infoQueue.values.firstOrNull()?.isNotEmpty() ?: return "Description not provided.") {
        val result = infoQueue.values.first()
        infoQueue.remove(infoQueue.keys.first()) // Remove first element
        return result

    } else if (infoQueue.values.elementAtOrNull(1)?.isNotEmpty() ?: return "Description not provided.") {
        val result = infoQueue.values.elementAt(1)
        infoQueue.remove(infoQueue.keys.first()) // Remove first element
        infoQueue.remove(infoQueue.keys.first()) // Remove second element
        return result

    } else if (infoQueue.values.elementAtOrNull(2)?.isNotEmpty() ?: return "Description not provided.") {
        val result = infoQueue.values.elementAt(2)
        infoQueue.remove(infoQueue.keys.first()) // Remove first element
        infoQueue.remove(infoQueue.keys.first()) // Remove second element
        infoQueue.remove(infoQueue.keys.first()) // Remove third element
        return result

    } else if (infoQueue.values.elementAtOrNull(3)?.isNotEmpty() ?: return "Description not provided.") {
        val result = infoQueue.values.elementAt(3)
        infoQueue.clear() // Remove all elements
        return result

    } else {
        infoQueue.clear() // Remove all elements
        return "Description not provided."
    }
}

private fun getEventAddress(event: Event): String {
    if (
        event.place != null
        && event.place.address?.line1.toString() != "null"
        && event.place.city?.name.toString() != "null"
        && event.place.state?.name.toString() != "null"
    ) {
        return event.place.address?.line1 + ", " + event.place.city?.name + ", " + event.place.state
    } else if (
        event.embedded?.venues != null
        && event.embedded.venues.first().address.toString() != "null"
        && event.embedded.venues.first().city.toString() != "null"
        && event.embedded.venues.first().state.toString() != "null"
    ) {
        return event.embedded.venues.first().address?.line1 + ", " + event.embedded.venues.first().city?.name + ", " + event.embedded.venues.first().state?.name
    } else {
        return "No location provided."
    }
}

private fun openMapsAndSearch(context: Context, searchQuery: String) {
    // Encode the search query to be URL-safe
    val encodedSearchQuery = Uri.encode(searchQuery)

    // Create the base URI for a search query
    // geo:0,0?q= defines a search query.
    // The 0,0 are placeholders for lat/lng if you wanted to search near a specific point,
    // but for a general search query, just the 'q' parameter is sufficient.
    val gmmIntentUri = "geo:0,0?q=$encodedSearchQuery".toUri()

    // Create an Intent with the action view and the URI
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

    // Set the package to specifically launch Google Maps
    // This is optional but ensures Google Maps is opened if installed.
    // If not set, and multiple apps can handle geo URIs, the user might be prompted.
    mapIntent.setPackage("com.google.android.apps.maps")

    // Verify that Google Maps is installed before attempting to start the intent
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        // Google Maps is not installed, handle this case by trying to open in browser
        val webIntentUri = "https://www.google.com/maps/search/?api=1&query=$encodedSearchQuery".toUri()
        val webIntent = Intent(Intent.ACTION_VIEW, webIntentUri)
        if (webIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(webIntent)
        } else {
            AppSnackbarManager.showSnackbar(message = "No maps application found.")
        }
    }
}