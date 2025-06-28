package com.example.concertfinder.presentation.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.use_case.get_saved_events.GetSavedEventsUseCase
import com.example.concertfinder.domain.use_case.save_event.SaveEventUseCase
import com.example.concertfinder.domain.use_case.unsave_event.UnsaveEventUseCase
import com.example.concertfinder.presentation.utils.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val saveEventUseCase: SaveEventUseCase,
    private val unsaveEventUseCase: UnsaveEventUseCase,
    private val getSavedEventsUseCase: GetSavedEventsUseCase
) : ViewModel() {

    // Top level ui state
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    savedEventsIds = getSavedEventsUseCase.getSavedEventsIds()
                )
            }
            Log.d("navigation", "Saved events ids: ${_uiState.value.savedEventsIds}")
        }
    }

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

    fun toggleEventSaved(event: Event) {
        if (event.id == _uiState.value.currentEvent.id) {
            _uiState.update { currentState ->
                currentState.copy(
                    currentEvent = currentState.currentEvent.copy(
                        saved = !currentState.currentEvent.saved
                    )
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (!event.saved) {
                saveEventUseCase(event)
                _uiState.update { currentState ->
                    currentState.copy(
                        savedEventsIds = getSavedEventsUseCase.getSavedEventsIds()
                    )
                }
                Log.d("navigation", "Saved events ids: ${_uiState.value.savedEventsIds}")
            } else {
                unsaveEventUseCase(event.id ?: throw Exception("Event ID cannot be null"))
                _uiState.update { currentState ->
                    currentState.copy(
                        savedEventsIds = getSavedEventsUseCase.getSavedEventsIds()
                    )
                }
                Log.d("navigation", "Saved events ids: ${_uiState.value.savedEventsIds}")
            }
            updateSavedEventsUpdated(true)
        }
    }

    fun updateFabVisibility(show : Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showFab = show
            )
        }
    }

    fun updateAreFiltersApplied(areFiltersApplied: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                areFiltersApplied = areFiltersApplied
            )
        }
    }

    fun updateSavedEventsUpdated(savedEventsUpdated: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                savedEventsUpdated = savedEventsUpdated,
            )
        }
        Log.d("navigation", "Saved events updated: ${_uiState.value.savedEventsUpdated}")
    }
}