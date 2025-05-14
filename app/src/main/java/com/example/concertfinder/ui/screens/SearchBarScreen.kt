package com.example.concertfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.model.ConcertFinderUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    uiState: ConcertFinderUiState,
    onExpandedChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onDispose: () -> Unit,
    modifier: Modifier = Modifier
) {

    DisposableEffect(Unit) {
        onDispose {
            onDispose()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchText,
                    onQueryChange = {
                        onQueryChange(it)
                    },
                    onSearch = onSearch,
                    expanded = uiState.searchExpanded,
                    onExpandedChange = { onExpandedChange(it) },
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_placeholder))
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search)
                        )
                    }
                )
            },
            expanded = uiState.searchExpanded,
            onExpandedChange = { onExpandedChange(it) },
            windowInsets = WindowInsets(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small))
        ) {
            // TODO: add search history here
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchBarScreen(
        uiState = ConcertFinderUiState(),
        onExpandedChange = {},
        onQueryChange = {},
        onSearch = {},
        onDispose = {}
    )
}