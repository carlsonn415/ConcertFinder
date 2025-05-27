package com.example.concertfinder.presentation.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.presentation.utils.AppDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel() : ViewModel() {

    // Top level ui state
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun onNavigateToEventList(
        navController: NavController,
        searchQuery: String,
    ) {
        var modifiedSearchQuery = searchQuery
        if (searchQuery.contains("/")) {
            modifiedSearchQuery = modifiedSearchQuery.replace("/", "")
        }

        var route = AppDestinations.EVENT_LIST + "/${modifiedSearchQuery}"

        Log.d("navigation", "Navigating to $route")
        navController.navigate(route)
    }

    // UI STATE UPDATES BELOW THIS LINE
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // update show bottom bar
    fun updateShowBottomBar(show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomBar = show
            )
        }
    }

    fun updateCurrentEvent(event: Event) {
        _uiState.update { currentState ->
            currentState.copy(
                currentEvent = event
            )
        }
    }

    fun toggleCurrentEventSaved() {
        _uiState.update { currentState ->
            currentState.copy(
                currentEvent = currentState.currentEvent.copy(
                    saved = !currentState.currentEvent.saved
                )
            )
        }
    }

    fun updateFabVisibility(show : Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showFab = show
            )
        }
    }
}