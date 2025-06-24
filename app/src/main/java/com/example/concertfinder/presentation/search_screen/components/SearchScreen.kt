package com.example.concertfinder.presentation.search_screen.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.location_menu.LocationMenu
import com.example.concertfinder.presentation.common_ui.location_menu.LocationViewModel
import com.example.concertfinder.presentation.search_screen.SearchScreenViewModel
import com.example.concertfinder.presentation.utils.LaunchLocationPermission


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {

    val searchScreenUiState = searchScreenViewModel.uiState.collectAsState()
    val locationUiState = locationViewModel.uiState.collectAsState()

    // Animate the horizontal padding
    val searchBarPadding by animateDpAsState(
        targetValue = if (searchScreenUiState.value.isSearchBarExpanded) 0.dp else dimensionResource(id = R.dimen.padding_medium),
        animationSpec = tween(durationMillis = 300), // Optional: customize animation duration/easing
    )

    // dispose of search bar when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            searchScreenViewModel.resetSearchBarScreen()
        }
    }

    // launch location permission launcher when view model requests it
    LaunchLocationPermission(
        context = LocalContext.current,
        updateLocation = {
            locationViewModel.updateLocation()
        },
        requestLocationPermissionEvent = locationViewModel.requestLocationPermissionEvent
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(innerPadding)
    ) {
        Box(
            modifier = Modifier
                .heightIn(max = 500.dp)
        ) {

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchScreenUiState.value.searchQuery,
                        onQueryChange = {
                            searchScreenViewModel.updateSearchText(it)
                        },
                        onSearch = {
                            searchScreenViewModel.saveSearchQuery(searchScreenUiState.value.searchQuery)
                            onSearch(searchScreenUiState.value.searchQuery)
                        },
                        expanded = searchScreenUiState.value.isSearchBarExpanded,
                        onExpandedChange = {
                            searchScreenViewModel.updateSearchExpanded(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.search_placeholder))
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    searchScreenViewModel.saveSearchQuery(searchScreenUiState.value.searchQuery)
                                    onSearch(searchScreenUiState.value.searchQuery)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = stringResource(id = R.string.search)
                                )
                            }
                        },
                    )
                },
                expanded = searchScreenUiState.value.isSearchBarExpanded,
                onExpandedChange = {
                    searchScreenViewModel.updateSearchExpanded(it)
                },
                windowInsets = WindowInsets(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = searchBarPadding,
                        vertical = searchBarPadding
                    )
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (searchScreenUiState.value.searchHistory.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .heightIn(min = 56.dp, max = 500.dp)
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 500.dp)
                        ) {
                            items(searchScreenUiState.value.searchHistory) { historyItem ->
                                ListItem(
                                    headlineContent = { Text(historyItem) },
                                    trailingContent = {
                                        IconButton(
                                            onClick = {
                                                searchScreenViewModel.deletePreviousSearch(query = historyItem)
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    modifier = Modifier.clickable {
                                        searchScreenViewModel.saveSearchQuery(historyItem)
                                        onSearch(historyItem)
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        HorizontalDivider()
                    }
                } else {
                    Box( // Show a message if history is empty
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .heightIn(min = 56.dp, max = 500.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_search_history))
                    }
                }
            }
        }

        LocationMenu(
            address = locationUiState.value.address,
            radius = locationUiState.value.radius,
            isLocationPreferencesMenuExpanded = searchScreenUiState.value.isLocationPreferencesMenuExpanded,
            locationSearchQuery = searchScreenUiState.value.locationSearchQuery,
            isRadiusPreferencesExpanded = searchScreenUiState.value.isRadiusPreferencesExpanded,
            locationLoadingStatus = locationUiState.value.locationLoadingStatus,
            onExposeRadiusDropdownChange = {
                searchScreenViewModel.updateDropDownExpanded(it)
            },
            onRadiusOptionSelected = {
                locationViewModel.updateRadiusFilterPreference(radius = it)
                searchScreenViewModel.updateDropDownExpanded(false)
            },
            onExpandLocationDropdown = {
                searchScreenViewModel.updateLocationMenuExpanded(it)
            },
            onGetLocation = { locationViewModel.initiateLocationUpdate() },
            onLocationQueryUpdate = {
                searchScreenViewModel.updateLocationSearchQuery(it)
            },
            onLocationSearch = {
                locationViewModel.searchForLocation(it)
            }
        )
    }
}