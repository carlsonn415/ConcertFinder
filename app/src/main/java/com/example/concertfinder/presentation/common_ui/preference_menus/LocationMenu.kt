package com.example.concertfinder.presentation.common_ui.preference_menus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants
import com.example.concertfinder.domain.model.LoadingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMenu(
    address: String,
    radius: String,
    isLocationPreferencesMenuExpanded: Boolean,
    locationSearchQuery: String,
    isRadiusPreferencesExpanded: Boolean,
    locationLoadingStatus: LoadingStatus,
    onExpandLocationDropdown: (Boolean) -> Unit,
    onGetLocation: () -> Unit,
    onExposeRadiusDropdownChange: (Boolean) -> Unit,
    onRadiusOptionSelected: (String) -> Unit,
    onLocationQueryUpdate: (String) -> Unit,
    onLocationSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        onExpandLocationDropdown(!isLocationPreferencesMenuExpanded)
                    }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                        vertical = dimensionResource(R.dimen.padding_small)
                    )
            ) {
                Text(
                    text = if (locationLoadingStatus == LoadingStatus.Loading) {
                        stringResource(R.string.getting_location)
                    } else {
                        stringResource(R.string.searching_near) + address
                    },
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    modifier = modifier.weight(1f)
                )
                if (isLocationPreferencesMenuExpanded) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = stringResource(id = R.string.drop_down_arrow),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.drop_down_arrow),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
                    )
                }
            }

            if (isLocationPreferencesMenuExpanded) {

                LocationSearchField(
                    locationSearchQuery = locationSearchQuery,
                    onGetLocation = onGetLocation,
                    onLocationQueryUpdate = onLocationQueryUpdate,
                    onLocationSearch = onLocationSearch,
                    modifier = modifier
                )

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

                PreferencesDropdown(
                    currentPreference = radius,
                    dropdownLabel = stringResource(R.string.select_a_radius_to_search),
                    preferenceOptions = Constants.radiusOptions.map { it.radius },
                    preferenceLabel = Constants.radiusOptions.first().unit.name,
                    isPreferencesExpanded = isRadiusPreferencesExpanded,
                    onPreferencesExpandedChange = onExposeRadiusDropdownChange,
                    onPreferenceSelected = onRadiusOptionSelected,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationMenuPreview() {
    LocationMenu(
        address = "123 Main St",
        radius = "10",
        locationLoadingStatus = LoadingStatus.Success,
        isLocationPreferencesMenuExpanded = true,
        locationSearchQuery = "New York",
        isRadiusPreferencesExpanded = true,
        onExpandLocationDropdown = {},
        onRadiusOptionSelected = {},
        onExposeRadiusDropdownChange = {},
        onGetLocation = {},
        onLocationQueryUpdate = {},
        onLocationSearch = {},
    )
}