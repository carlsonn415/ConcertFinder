package com.example.concertfinder.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.concertfinder.model.ConcertFinderUiState
import com.example.concertfinder.ui.utils.NavigationBarElement

@Composable
fun ConcertFinderHomeScreen(
    uiState: ConcertFinderUiState,
    onClick: (NavigationBarElement) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState.currentScreen) {
        NavigationBarElement.Events -> EventsScreen(
            onClick = {
                onClick(NavigationBarElement.Events)
            },
            modifier = modifier,
        )
        NavigationBarElement.Search -> SearchBarScreen(
            onClick = {
                onClick(NavigationBarElement.Search)
            },
            modifier = modifier,
        )
        NavigationBarElement.Calendar -> CalendarScreen(
            onClick = {
                onClick(NavigationBarElement.Calendar)
            },
            modifier = modifier,
        )
    }
}