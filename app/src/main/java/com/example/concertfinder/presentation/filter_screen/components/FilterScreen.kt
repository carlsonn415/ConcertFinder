package com.example.concertfinder.presentation.filter_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.preference_menus.PreferencesDropdown
import com.example.concertfinder.presentation.common_ui.preference_menus.SortMenu
import com.example.concertfinder.presentation.filter_screen.FilterScreenViewModel

@Composable
fun FilterScreen(
    modifier: Modifier = Modifier,
    viewModel: FilterScreenViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState())
    ) {
        SortMenu(
            currentSortOption = uiState.value.currentSortOption,
            isSortOptionsExpanded = uiState.value.isSortOptionsExpanded,
            onSortOptionSelected = {
                viewModel.onSortOptionSelected(it)
            },
            onExpandSortOptionsDropdown = {
                viewModel.toggleSortOptionExpanded()
            },
            currentSortType = uiState.value.currentSortType,
            isSortTypeExpanded = uiState.value.isSortTypeExpanded,
            onSortTypeSelected = {
                viewModel.onSortTypeSelected(it)
            },
            onExpandSortTypeDropdown = {
                viewModel.toggleSortTypeExpanded()
            },
            isSortMenuExpanded = uiState.value.isSortMenuExpanded,
            onExpandSortMenuDropdown = {
                viewModel.toggleSortMenuExpanded()
            }
        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        PreferencesDropdown(
            currentPreference = uiState.value.currentSegment,
            dropdownLabel = "Category",
            preferenceOptions = uiState.value.segmentOptions.map { it.name },
            isPreferencesExpanded = uiState.value.isSegmentPreferencesExpanded,
            onPreferencesExpandedChange = { viewModel.toggleSegmentPreferencesExpanded() },
            onPreferenceSelected = { viewModel.onSegmentSelected(it) }
        )
        Button(onClick = { viewModel.clearSegmentPreferences() }) {
            Text(text = "Reset")
        }

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        PreferencesDropdown(
            currentPreference = uiState.value.currentGenres.firstOrNull() ?: "",
            dropdownLabel = "Genres",
            preferenceOptions = uiState.value.genreOptions.map { it.name },
            isPreferencesExpanded = uiState.value.isGenrePreferencesExpanded,
            onPreferencesExpandedChange = { viewModel.toggleGenrePreferencesExpanded() },
            onPreferenceSelected = { viewModel.onGenreSelected(it) }
        )
        Button(onClick = { viewModel.clearGenrePreferences() }) {
            Text(text = "Reset")
        }

        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        PreferencesDropdown(
            currentPreference = uiState.value.currentSubgenres.firstOrNull() ?: "",
            dropdownLabel = "Subgenres",
            preferenceOptions = uiState.value.subgenreOptions.map { it.name },
            isPreferencesExpanded = uiState.value.isSubgenrePreferencesExpanded,
            onPreferencesExpandedChange = { viewModel.toggleSubgenrePreferencesExpanded() },
            onPreferenceSelected = { viewModel.onSubgenreSelected(it) }
        )
        Button(onClick = { viewModel.clearSubgenrePreferences() }) {
            Text(text = "Reset")
        }
    }
}

@Preview
@Composable
private fun FilterScreenPreview() {
    FilterScreen()
}