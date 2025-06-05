package com.example.concertfinder.presentation.filter_screen.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.preference_menus.LocationMenu
import com.example.concertfinder.presentation.common_ui.preference_menus.PreferencesDropdown
import com.example.concertfinder.presentation.common_ui.preference_menus.SortMenu
import com.example.concertfinder.presentation.filter_screen.FilterScreenViewModel
import com.example.concertfinder.presentation.utils.LaunchLocationPermission

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FilterScreen(
    modifier: Modifier = Modifier,
    viewModel: FilterScreenViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    val uiState = viewModel.uiState.collectAsState()

    // launch location permission launcher when view model requests it
    LaunchLocationPermission(
        context = LocalContext.current,
        updateLocation = {
            viewModel.updateLocation()
        },
        requestLocationPermissionEvent = viewModel.requestLocationPermissionEvent
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        SortMenu(
            currentSortOption = uiState.value.currentSortOption,
            onSortOptionSelected = {
                viewModel.onSortOptionSelected(it)
            },
            isSortMenuExpanded = uiState.value.isSortMenuExpanded,
            onExpandSortMenuDropdown = {
                viewModel.toggleSortMenuExpanded()
            }
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_small)))
        HorizontalDivider()
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clickable {
                    viewModel.toggleFilterMenuExpanded()
                }
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.weight(1f)
            )
            if (uiState.value.isFilterMenuExpanded) {
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

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        if (uiState.value.isFilterMenuExpanded) {
            PreferencesDropdown(
                currentPreference = uiState.value.currentSegment,
                dropdownLabel = stringResource(R.string.category),
                preferenceOptions = uiState.value.segmentOptions.map { it.name },
                isPreferencesExpanded = uiState.value.isSegmentPreferencesExpanded,
                onPreferencesExpandedChange = { viewModel.toggleSegmentPreferencesExpanded() },
                onPreferenceSelected = { viewModel.onSegmentSelected(it) },
                showValue = false
            )

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

            PreferencesDropdown(
                currentPreference = uiState.value.currentGenres.firstOrNull() ?: "",
                dropdownLabel = stringResource(R.string.genre),
                preferenceOptions = uiState.value.genreOptions.map { it.name },
                isPreferencesExpanded = uiState.value.isGenrePreferencesExpanded,
                onPreferencesExpandedChange = { viewModel.toggleGenrePreferencesExpanded() },
                onPreferenceSelected = { viewModel.onGenreSelected(it) },
                showValue = false,
                enabled = uiState.value.currentSegment != ""
            )

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

            PreferencesDropdown(
                currentPreference = uiState.value.currentSubgenres.firstOrNull() ?: "",
                dropdownLabel = stringResource(R.string.subgenre),
                preferenceOptions = uiState.value.subgenreOptions.map { it.name },
                isPreferencesExpanded = uiState.value.isSubgenrePreferencesExpanded,
                onPreferencesExpandedChange = { viewModel.toggleSubgenrePreferencesExpanded() },
                onPreferenceSelected = { viewModel.onSubgenreSelected(it) },
                showValue = false,
                enabled = uiState.value.currentGenres.isNotEmpty()
            )

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

            if (uiState.value.currentSegment != "") {
                ClassificationFlowRowClickable(
                    segmentName = uiState.value.currentSegment,
                    genreNames = uiState.value.currentGenres,
                    subgenreNames = uiState.value.currentSubgenres,
                    onSegmentDeleted = { viewModel.clearSegmentPreferences() },
                    onGenreDeleted = { viewModel.deleteGenre(it) },
                    onSubgenreDeleted = { viewModel.deleteSubgenre(it) },
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )
                Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
            }

            Button(
                onClick = { viewModel.clearSegmentPreferences() },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .align(alignment = Alignment.End)
            ) {
                Text(text = stringResource(R.string.reset_all))
            }
            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_small)))
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_small)))

        LocationMenu(
            address = uiState.value.address,
            radius = uiState.value.radius,
            isLocationPreferencesMenuExpanded = uiState.value.isLocationPreferencesMenuExpanded,
            locationSearchQuery = uiState.value.locationSearchQuery,
            isRadiusPreferencesExpanded = uiState.value.isRadiusPreferencesExpanded,
            locationLoadingStatus = uiState.value.locationLoadingStatus,
            onExposeRadiusDropdownChange = {
                viewModel.updateDropDownExpanded(it)
            },
            onRadiusOptionSelected = {
                viewModel.updateRadiusFilterPreference(radius = it)
                viewModel.updateDropDownExpanded(false)
            },
            onExpandLocationDropdown = {
                viewModel.updateLocationMenuExpanded(it)
            },
            onGetLocation = { viewModel.initiateLocationUpdate() },
            onLocationQueryUpdate = {
                viewModel.updateLocationSearchQuery(it)
            },
            onLocationSearch = {
                viewModel.searchForLocation(it)
            }
        )
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview
@Composable
private fun FilterScreenPreview() {
    FilterScreen()
}