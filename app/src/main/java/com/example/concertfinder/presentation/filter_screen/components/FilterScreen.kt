package com.example.concertfinder.presentation.filter_screen.components

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.presentation.common_ui.preference_menus.SortMenu
import com.example.concertfinder.presentation.filter_screen.FilterScreenViewModel

@Composable
fun FilterScreen(
    modifier: Modifier = Modifier,
    viewModel: FilterScreenViewModel = hiltViewModel(),
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getClassifications()
    }

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

        if (uiState.value.loading == false && uiState.value.classifications != null) {
            Log.d("FilterScreen", "Classifications: ${uiState.value.classifications}")
            Text(text = "Segments:")
            for (classification in uiState.value.classifications) {
                Text(text = classification.segment.name)
            }

            var subgenreList: MutableList<String> = mutableListOf()
            Text(text = "Genres:")
            for (classification in uiState.value.classifications) {
                val genreList = classification.genres
                for (genre in genreList) {
                    Text(text = genre.name)
                    subgenreList.addAll(genre.subgenres.map { it.name })
                }
            }
            Text(text = "Subgenres:")
            for (subgenre in subgenreList) {
                Text(text = subgenre)
            }
        } else {
            Text(text = "Loading...")
        }

    }
}

@Preview
@Composable
private fun FilterScreenPreview() {
    FilterScreen()
}