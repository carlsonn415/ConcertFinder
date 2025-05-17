package com.example.concertfinder.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.concertfinder.model.ConcertFinderUiState
import com.example.concertfinder.ui.ConcertFinderViewModel


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    uiState: ConcertFinderUiState,
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
        modifier = modifier.fillMaxSize()
    ) {
        Button(
            onClick = { viewModel.initiateLocationUpdate() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Update Location")
        }

        Text(
            text = uiState.currentAddress,
        )

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchText,
                    onQueryChange = {
                        viewModel.updateSearchText(it)
                    },
                    onSearch = {
                        onSearch(uiState.searchText)
                    },
                    expanded = uiState.searchExpanded,
                    onExpandedChange = {
                        viewModel.updateSearchExpanded(it)
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_placeholder))
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onSearch(uiState.searchText)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = R.string.search)
                            )
                        }
                    }
                )
            },
            expanded = uiState.searchExpanded,
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
        ) {
            // TODO: add search history here
        }
    }
}