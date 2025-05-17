package com.example.concertfinder.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.model.uistate.ConcertFinderUiState
import com.example.concertfinder.model.radiusOptions
import com.example.concertfinder.model.uistate.SearchScreenUiState
import com.example.concertfinder.ui.ConcertFinderViewModel
import com.example.concertfinder.ui.commonui.LocationMenu


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    uiState: ConcertFinderUiState,
    searchScreenUiState: SearchScreenUiState,
    viewModel: ConcertFinderViewModel,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    // dispose of search bar when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetSearchBar()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .heightIn(max = 500.dp)
        ) {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchScreenUiState.searchQuery,
                        onQueryChange = {
                            viewModel.updateSearchText(it)
                        },
                        onSearch = {
                            onSearch(searchScreenUiState.searchQuery)
                        },
                        expanded = searchScreenUiState.isSearchBarExpanded,
                        onExpandedChange = {
                            viewModel.updateSearchExpanded(it)
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.search_placeholder))
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onSearch(searchScreenUiState.searchQuery)
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
                expanded = searchScreenUiState.isSearchBarExpanded,
                onExpandedChange = {
                    viewModel.updateSearchExpanded(it)
                },
                windowInsets = WindowInsets(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                        vertical = dimensionResource(R.dimen.padding_small)
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
                                        viewModel.updateSearchText(historyItem)
                                        viewModel.updateSearchExpanded(false)
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
                            .heightIn(min = 56.dp, max = 500.dp)
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_search_history))
                    }
                }
            }
        }

        LocationMenu(
            uiState = uiState,
            searchScreenUiState = searchScreenUiState,
            onExposeRadiusDropdownChange = {
                viewModel.updateDropDownExpanded(it)
            },
            onOptionSelected = {
                viewModel.updateSearchRadius(it)
                viewModel.updateDropDownExpanded(false)
            },
            onExpandLocationDropdown = {
                viewModel.updateLocationMenuExpanded(it)
            },
            onGetCurrentLocation = {
                viewModel.initiateLocationUpdate()
            },
            onLocationQueryUpdate = {
                viewModel.updateLocationSearchQuery(it)
            },
            onLocationSearch = {
                viewModel.onLocationSearch(it)
            },
            radiusOptions = radiusOptions
        )
    }
}