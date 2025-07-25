package com.example.lineup_app.presentation.app

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.lineup_app.data.model.Event
import com.example.lineup_app.domain.use_case.get_saved_events.GetSavedEventsUseCase
import com.example.lineup_app.domain.use_case.save_event.SaveEventUseCase
import com.example.lineup_app.domain.use_case.unsave_event.UnsaveEventUseCase
import com.example.lineup_app.presentation.utils.AppDestinations
import com.example.lineup_app.presentation.utils.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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

    // Snackbar messages
    private val _snackbarMessages = MutableSharedFlow<SnackbarMessage>()
    val snackbarMessages: SharedFlow<SnackbarMessage> = _snackbarMessages.asSharedFlow()

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
        startDateTime: String? = null,
        endDateTime: String? = null,
        sort: String? = null,
        radius: String? = null,
        segmentName: String? = null
    ) {
        var modifiedSearchQuery = searchQuery
        if (searchQuery.contains("/")) {
            modifiedSearchQuery = modifiedSearchQuery.replace("/", "")
        }
        // URL Encode the path segment
        val encodedSearchQuery = URLEncoder.encode(modifiedSearchQuery, StandardCharsets.UTF_8.toString())

        // Build the route
        var route = "${AppDestinations.EVENT_LIST}/$encodedSearchQuery"

        // Append optional query parameters safely
        val queryParams = mutableListOf<String>()
        startDateTime?.let { queryParams.add("startDateTime=$it") }
        endDateTime?.let { queryParams.add("endDateTime=$it") }
        sort?.let { queryParams.add("sort=$it") }
        radius?.let { queryParams.add("radius=$it") }
        segmentName?.let { queryParams.add("segmentName=$it") }


        if (queryParams.isNotEmpty()) {
            route += "?${queryParams.joinToString("&")}"
        }

        Log.d("navigation", "Navigating to $route")
        navController.navigate(route)
    }

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

    fun toggleEventSaved(event: Event, undo: Boolean = false) {
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
                if (!undo) {
                    unsaveEventUseCase(event.id ?: throw Exception("Event ID cannot be null"))
                } else {
                    saveEventUseCase(event)
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        savedEventsIds = getSavedEventsUseCase.getSavedEventsIds()
                    )
                }
                Log.d("navigation", "Saved events ids: ${_uiState.value.savedEventsIds}")
            }

            // sends flags to reload all events
            updateAllEventSavedFlags(true)
        }
    }

    fun updateFabVisibility(show : Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showFab = show
            )
        }
    }

    fun pushOntoTopBarTitle(titleId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                topBarTitleStack = currentState.topBarTitleStack.plus(titleId)
            )
        }
    }

    fun popTopBarTitle() {
        _uiState.update { currentState ->
            currentState.copy(
                topBarTitleStack = currentState.topBarTitleStack.dropLast(1)
            )
        }
    }

    fun updateAreFiltersApplied(areFiltersApplied: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                areFiltersAppliedFlag = areFiltersApplied
            )
        }
    }

    fun updateAllEventSavedFlags(saved: Boolean) {
        updateSavedEventsScreenReloadFlag(saved)
        updateDiscoverScreenReloadFlag(saved)
        updateEventListScreenReloadFlag(saved)
    }

    fun updateSavedEventsScreenReloadFlag(savedEventsScreenReloadFlag: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                savedEventsScreenReloadSavedEventsFlag = savedEventsScreenReloadFlag,
            )
        }
    }

    fun updateDiscoverScreenReloadFlag(discoverScreenReloadFlag: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                discoverScreenReloadSavedEventsFlag = discoverScreenReloadFlag,
            )
        }
    }

    fun updateEventListScreenReloadFlag(eventListScreenReloadFlag: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                eventListScreenReloadSavedEventsFlag = eventListScreenReloadFlag,
            )
        }
    }

    fun updateLocationChangedFlag(locationChangedFlag: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                locationChangedFlag = locationChangedFlag,
            )
        }
    }

    fun updateEventListScreenLoadNewEventsFlag(eventListScreenLoadNewEventsFlag: Boolean) {
        if (eventListScreenLoadNewEventsFlag == false) {
            _uiState.update { currentState ->
                currentState.copy(
                    eventListScreenLoadNewEventsFlag = false,
                )
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                delay(1000)
                _uiState.update { currentState ->
                    currentState.copy(
                        eventListScreenLoadNewEventsFlag = true,
                    )
                }
            }
        }
    }

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        viewModelScope.launch {
            _snackbarMessages.emit(
                SnackbarMessage(
                    message = message,
                    actionLabel = actionLabel,
                    onAction = onAction,
                    duration = duration
                )
            )
        }
    }
}