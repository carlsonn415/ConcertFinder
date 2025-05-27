package com.example.concertfinder.presentation.event_list_screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.common.Constants.PARAM_KEYWORD
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.repository.PreferencesRepository
import com.example.concertfinder.domain.use_case.display_event.DisplayEventUseCase
import com.example.concertfinder.domain.use_case.get_events_from_network.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class EventListViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val displayEventUseCase: DisplayEventUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _uiState = MutableStateFlow(EventListUiState())
    val uiState = _uiState.asStateFlow()

    init {                                                                                          // initialize events
        getEvents()
    }


    // load events from repository
    fun getEvents(
        keyWord: String? = savedStateHandle.get<String>(PARAM_KEYWORD),                             // keyword to gets passed from search bar screen
        geoPoint: String = preferencesRepository.getLocationPreferences().getLocation(),            // \
        radius: String = preferencesRepository.getFilterPreferences().getRadius(),                  // |
        startDateTime: String = preferencesRepository.getFilterPreferences().getStartDateTime(),    // |
        sort: String = preferencesRepository.getFilterPreferences().getSort(),                      // filters are fetched from saved preferences
        genres: List<String>? = preferencesRepository.getFilterPreferences().getGenres(),           // |
        subgenres: List<String>? = preferencesRepository.getFilterPreferences().getSubgenres(),     // |
        segment: String? = preferencesRepository.getFilterPreferences().getSegment(),               // /
        page: String = uiState.value.page.toString(),                                               // ui state holds current page number
    ) {

        // launch coroutine to get events from repository
        viewModelScope.launch(Dispatchers.IO) {

            Log.d("EventListViewModel", "Getting events")                                           // log for debugging
            Log.d("EventListViewModel", "Radius: $radius")
            Log.d("EventListViewModel", "GeoPoint: $geoPoint")
            Log.d("EventListViewModel", "StartDateTime: $startDateTime")
            Log.d("EventListViewModel", "Sort: $sort")
            Log.d("EventListViewModel", "KeyWord: $keyWord")
            Log.d("EventListViewModel", "Genres: $genres")
            Log.d("EventListViewModel", "Subgenres: $subgenres")
            Log.d("EventListViewModel", "Segment: $segment")
            Log.d("EventListViewModel", "Page: $page")

            // get events from repository
            getEventsUseCase(
                radius = radius,
                geoPoint = geoPoint,
                startDateTime = startDateTime,
                sort = sort,
                genres = genres,
                subgenres = subgenres,
                segment = segment,
                keyWord = keyWord,
                page = page,
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {                                                        // if success, update ui state
                        _uiState.update { currentState ->
                            currentState.copy(

                                eventsResource = if (currentState.hasLoadedOnce) {                          // If this is not a new search, add the new events to the existing list
                                    val currentEvents = currentState.eventsResource.data ?: emptyList()
                                    Resource.Success(currentEvents + (result.data ?: emptyList()))
                                } else result,                                                      // If this is a new search, set the events to the new list
                                // Checks if there are more events to load
                                canLoadMore = (result.totalPages?.toInt() ?: 0) > page.toInt() + 1, // If there are more events to load, set canLoadMore to true
                                isLoadingMore = false,                                              // Set isLoadingMore to false since load is complete
                                hasLoadedOnce = true                                                // Set hasLoadedOnce to true since a search has been performed
                            )
                        }

                        Log.d("EventListViewModel", "Success, total pages: ${result.totalPages}")
                    }

                    is Resource.Error -> {                                                          // if error, update ui state
                        if (uiState.value.eventsResource.data?.isEmpty() != false) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    eventsResource = result,
                                )
                            }
                            Log.d("EventListViewModel", "Error: ${result.message}")
                        }
                    }

                    is Resource.Loading -> {                                                        // if loading, update ui state
                        if (uiState.value.eventsResource.data?.isEmpty() != false) {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    eventsResource = result,
                                    isLoadingMore = true
                                )
                            }
                            Log.d("EventListViewModel", "Loading")
                        }
                    }
                }
            }
        }
    }

    fun getDistanceFromLocation(
        latitude: Double?,
        longitude: Double?,
        unit: DistanceUnit
    ): String {
        return if (latitude != null && longitude != null && unit == DistanceUnit.Miles) {
            "${displayEventUseCase.getDistanceToEvent(latitude, longitude, unit)} mi"
        } else if (latitude != null && longitude != null && unit == DistanceUnit.Kilometers) {
            "${displayEventUseCase.getDistanceToEvent(latitude, longitude, unit)} km"
        } else {
            ""
        }
    }

    @SuppressLint("NewApi")
    fun getFormattedEventStartDates(dates: DateData?): String? {
        return displayEventUseCase.getFormattedEventStartDates(dates)
    }

    fun getImageUrl(
        images: List<EventImage>?,
        aspectRatio: String = "16_9",
        minImageWidth: Int = 1080
    ): String? {
        return displayEventUseCase.getImageUrl(images, aspectRatio, minImageWidth)
    }

    fun loadMoreEvents() {
        // Ensure that there is more events to load and that we are not already loading more events
        if (_uiState.value.canLoadMore && !_uiState.value.isLoadingMore) {
            // Update page number
            if (uiState.value.hasLoadedOnce) {
                _uiState.update { currentState ->
                    currentState.copy(
                        // Increase page number by 1
                        page = (currentState.page.toInt() + 1).toString(),
                        isLoadingMore = true
                    )
                }
            }

            // Get the events
            getEvents()
        }
    }
}