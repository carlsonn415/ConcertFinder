package com.example.concertfinder.presentation.filter_screen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.presentation.filter_screen.FilterScreenViewModel

@Composable
fun FilterScreen(
    modifier: Modifier = Modifier,
    viewModel: FilterScreenViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()


}

@Preview
@Composable
private fun FilterScreenPreview() {
    FilterScreen()
}