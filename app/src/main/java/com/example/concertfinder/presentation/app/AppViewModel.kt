package com.example.concertfinder.presentation.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
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
        var route = AppDestinations.EVENT_LIST + "/$searchQuery"

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
}