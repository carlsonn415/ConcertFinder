package com.example.concertfinder.presentation.search_screen.components

import android.os.Build
import androidx.annotation.RequiresExtension
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.preference_menus.LocationMenu
import com.example.concertfinder.presentation.search_screen.SearchScreenViewModel
import com.example.concertfinder.presentation.utils.LaunchLocationPermission


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchScreenViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {

    val uiState = viewModel.uiState.collectAsState()

    // dispose of search bar when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetSearchBarScreen()
        }
    }

    // launch location permission launcher when view model requests it
    LaunchLocationPermission(
        context = LocalContext.current,
        updateLocation = {
            viewModel.updateLocation()
        },
        requestLocationPermissionEvent = viewModel.requestLocationPermissionEvent
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
                        query = uiState.value.searchQuery,
                        onQueryChange = {
                            viewModel.updateSearchText(it)
                        },
                        onSearch = {
                            onSearch(uiState.value.searchQuery)
                        },
                        expanded = uiState.value.isSearchBarExpanded,
                        onExpandedChange = {
                            viewModel.updateSearchExpanded(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.search_placeholder))
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onSearch(uiState.value.searchQuery)
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
                expanded = uiState.value.isSearchBarExpanded,
                onExpandedChange = {
                    viewModel.updateSearchExpanded(it)
                },
                windowInsets = WindowInsets(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_medium),
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    )
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // TODO: add search history here

                // Content for when the SearchBar is expanded (e.g., search history)
                // Apply heightIn modifier here to constrain the expanded content area
                val searchHistory = listOf(
                    "Rock concerts",
                    "Jazz festivals",
                    "Nearby events",
                    "Classical music",
                ) // Example history

                if (searchHistory.isNotEmpty()) {
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
                            items(searchHistory) { historyItem ->
                                ListItem(
                                    headlineContent = { Text(historyItem) },
                                    trailingContent = {
                                        Icon(
                                            // TODO: add icon for search history
                                            Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null
                                        )
                                    },
                                    modifier = Modifier.clickable {
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
                viewModel.updateFilterPreferences(radius = it)
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