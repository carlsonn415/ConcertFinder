package com.example.lineup_app.presentation.common_ui.location_menu.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.lineup_app.R
import com.example.lineup_app.domain.model.LoadingStatus
import com.example.lineup_app.presentation.common_ui.elements.MapItem
import com.example.lineup_app.presentation.common_ui.location_menu.LocationViewModel
import com.example.lineup_app.presentation.ui.theme.MyIcons
import com.example.lineup_app.presentation.utils.LaunchLocationPermission

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMenu(
    isLocationPreferencesMenuExpanded: Boolean,
    locationSearchQuery: String,
    onExpandLocationMenu: (Boolean) -> Unit,
    onLocationQueryUpdate: (String) -> Unit,
    onLocationUpdated: () -> Unit,
    viewModel: LocationViewModel,
    modifier: Modifier = Modifier,
) {

    val uiState = viewModel.uiState.collectAsState()

    LaunchLocationPermission(
        context = LocalContext.current,
        updateLocation = {
            viewModel.updateLocation()
        },
        requestLocationPermissionEvent = viewModel.requestLocationPermissionEvent
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier
        ) {
            // Shows current location and arrow button to expand dropdown
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .clickable {
                        onExpandLocationMenu(!isLocationPreferencesMenuExpanded)
                    }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                        vertical = dimensionResource(R.dimen.padding_small)
                    )
            ) {
                Text(
                    text = if (uiState.value.locationLoadingStatus == LoadingStatus.Loading) {
                        stringResource(R.string.getting_location)
                    } else {
                        stringResource(R.string.searching_near) + uiState.value.address
                    },
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    modifier = modifier.weight(1f)
                )
                if (isLocationPreferencesMenuExpanded) {
                    Icon(
                        imageVector = MyIcons.arrowUp,
                        contentDescription = stringResource(id = R.string.drop_down_arrow),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
                    )
                } else {
                    Icon(
                        imageVector = MyIcons.arrowDown,
                        contentDescription = stringResource(id = R.string.drop_down_arrow),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
                    )
                }
            }

            if (isLocationPreferencesMenuExpanded) {

                MapItem(
                    latitude = uiState.value.latitude,
                    longitude = uiState.value.longitude,
                    eventTitle = stringResource(R.string.searching_near_here),
                    modifier = modifier
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_medium),
                            vertical = dimensionResource(R.dimen.padding_small)
                        )
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.map_height))
                        .clip(MaterialTheme.shapes.small),
                    showMarker = false,
                    showRadius = true,
                    radiusInMiles = uiState.value.radius.toDoubleOrNull()
                )

                RadiusSlider(
                    modifier = modifier.padding(
                        horizontal = dimensionResource(R.dimen.padding_large)
                    ),
                    initialRadius = uiState.value.radius,
                    onRadiusChange = {viewModel.updateRadiusFilterPreference(it)}
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium_small)))

                LocationSearchField(
                    locationSearchQuery = locationSearchQuery,
                    onGetLocation = {
                        viewModel.initiateLocationUpdate()
                        onLocationUpdated()
                    },
                    onLocationQueryUpdate = onLocationQueryUpdate,
                    onLocationSearch = {
                        viewModel.searchForLocation(it)
                        onLocationUpdated()
                    },
                    modifier = modifier
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_large)))
            }
        }
    }
}