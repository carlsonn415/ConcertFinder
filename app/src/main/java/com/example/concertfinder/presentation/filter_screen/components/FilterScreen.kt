package com.example.concertfinder.presentation.filter_screen.components

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.PreferencesDropdown
import com.example.concertfinder.presentation.common_ui.SortMenu
import com.example.concertfinder.presentation.common_ui.location_menu.LocationMenu
import com.example.concertfinder.presentation.common_ui.location_menu.LocationViewModel
import com.example.concertfinder.presentation.filter_screen.FilterScreenViewModel
import com.example.concertfinder.presentation.ui.theme.MyIcons
import com.example.concertfinder.presentation.utils.AppDestinations
import com.example.concertfinder.presentation.utils.LaunchLocationPermission

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FilterScreen(
    navController: NavController,
    onFilterApplied: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    filterScreenViewModel: FilterScreenViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    val filterScreenUiState = filterScreenViewModel.uiState.collectAsState()
    val locationUiState = locationViewModel.uiState.collectAsState()

    // launch location permission launcher when view model requests it
    LaunchLocationPermission(
        context = LocalContext.current,
        updateLocation = {
            locationViewModel.updateLocation()
            filterScreenViewModel.setPreferencesUpdated(true)
        },
        requestLocationPermissionEvent = locationViewModel.requestLocationPermissionEvent
    )

    BackHandler {
        // Checks if preferences have been updated and if so, sends signal to event list screen to reload
        if (filterScreenUiState.value.preferencesUpdated) {
            navController.previousBackStackEntry?.savedStateHandle?.set("filters_updated", true)
        }
        navController.popBackStack()
    }

    LaunchedEffect(filterScreenUiState.value.preferencesUpdated) {
        if (filterScreenUiState.value.preferencesUpdated) {
            onFilterApplied()
        }
    }

    val navBackStackEntry = navController.currentBackStackEntryAsState().value

    LaunchedEffect(navBackStackEntry) {
        // Check if the current destination is THIS screen
        if (navBackStackEntry?.destination?.route == AppDestinations.FILTER) {
            // Access the state within the scroll behavior
            val topAppBarState = scrollBehavior.state
            if (topAppBarState.heightOffset != 0f || topAppBarState.contentOffset != 0f) {
                Log.d("TopAppBarReset", "Resetting TopAppBar state for ${navBackStackEntry.destination.route}")
                topAppBarState.heightOffset = 0f
                topAppBarState.contentOffset = 0f
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        SortMenu(
            currentSortOption = filterScreenUiState.value.currentSortOption,
            onSortOptionSelected = {
                filterScreenViewModel.onSortOptionSelected(it)
            },
            isSortMenuExpanded = filterScreenUiState.value.isSortMenuExpanded,
            onExpandSortMenuDropdown = {
                filterScreenViewModel.toggleSortMenuExpanded()
            }
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_small)))
        HorizontalDivider()
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clickable {
                    filterScreenViewModel.toggleFilterMenuExpanded()
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
            if (filterScreenUiState.value.isFilterMenuExpanded) {
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

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        if (filterScreenUiState.value.isFilterMenuExpanded) {
            PreferencesDropdown(
                currentPreference = filterScreenUiState.value.currentSegment,
                dropdownLabel = stringResource(R.string.category),
                preferenceOptions = filterScreenUiState.value.segmentOptions.map { it.name },
                isPreferencesExpanded = filterScreenUiState.value.isSegmentPreferencesExpanded,
                onPreferencesExpandedChange = { filterScreenViewModel.toggleSegmentPreferencesExpanded() },
                onPreferenceSelected = { filterScreenViewModel.onSegmentSelected(it) },
                showValue = false
            )

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

            PreferencesDropdown(
                currentPreference = filterScreenUiState.value.currentGenres.firstOrNull() ?: "",
                dropdownLabel = stringResource(R.string.genre),
                preferenceOptions = filterScreenUiState.value.genreOptions.map { it.name },
                isPreferencesExpanded = filterScreenUiState.value.isGenrePreferencesExpanded,
                onPreferencesExpandedChange = { filterScreenViewModel.toggleGenrePreferencesExpanded() },
                onPreferenceSelected = { filterScreenViewModel.onGenreSelected(it) },
                showValue = false,
                enabled = filterScreenUiState.value.currentSegment != ""
            )

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

            PreferencesDropdown(
                currentPreference = filterScreenUiState.value.currentSubgenres.firstOrNull() ?: "",
                dropdownLabel = stringResource(R.string.subgenre),
                preferenceOptions = filterScreenUiState.value.subgenreOptions.map { it.name },
                isPreferencesExpanded = filterScreenUiState.value.isSubgenrePreferencesExpanded,
                onPreferencesExpandedChange = { filterScreenViewModel.toggleSubgenrePreferencesExpanded() },
                onPreferenceSelected = { filterScreenViewModel.onSubgenreSelected(it) },
                showValue = false,
                enabled = filterScreenUiState.value.currentGenres.isNotEmpty()
            )

            Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

            if (filterScreenUiState.value.currentSegment != "") {
                ClassificationFlowRowClickable(
                    segmentName = filterScreenUiState.value.currentSegment,
                    genreNames = filterScreenUiState.value.currentGenres,
                    subgenreNames = filterScreenUiState.value.currentSubgenres,
                    onSegmentDeleted = { filterScreenViewModel.clearSegmentPreferences() },
                    onGenreDeleted = { filterScreenViewModel.deleteGenre(it) },
                    onSubgenreDeleted = { filterScreenViewModel.deleteSubgenre(it) },
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )
                Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
            }

            Button(
                onClick = { filterScreenViewModel.clearSegmentPreferences() },
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
            address = locationUiState.value.address,
            radius = locationUiState.value.radius,
            isLocationPreferencesMenuExpanded = filterScreenUiState.value.isLocationPreferencesMenuExpanded,
            locationSearchQuery = filterScreenUiState.value.locationSearchQuery,
            isRadiusPreferencesExpanded = filterScreenUiState.value.isRadiusPreferencesExpanded,
            locationLoadingStatus = locationUiState.value.locationLoadingStatus,
            onExposeRadiusDropdownChange = {
                filterScreenViewModel.updateDropDownExpanded(it)
            },
            onRadiusOptionSelected = {
                locationViewModel.updateRadiusFilterPreference(radius = it)
                filterScreenViewModel.setPreferencesUpdated(true)
                filterScreenViewModel.updateDropDownExpanded(false)
            },
            onExpandLocationDropdown = {
                filterScreenViewModel.updateLocationMenuExpanded(it)
            },
            onGetLocation = {
                locationViewModel.initiateLocationUpdate()
                filterScreenViewModel.setPreferencesUpdated(true)
            },
            onLocationQueryUpdate = {
                filterScreenViewModel.updateLocationSearchQuery(it)
            },
            onLocationSearch = {
                locationViewModel.searchForLocation(it)
            }
        )

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
    }
}