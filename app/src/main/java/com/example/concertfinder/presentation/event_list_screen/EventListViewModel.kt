package com.example.concertfinder.presentation.event_list_screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

    // initialize state with data from saved state handle and preferences
    private val _uiState = MutableStateFlow(
        EventListUiState(
            currentRadius = preferencesRepository.getFilterPreferences().getRadius(),
            currentGeoPoint = preferencesRepository.getLocationPreferences().getLocation(),
            currentStartDateTime = preferencesRepository.getFilterPreferences().getStartDateTime(),
            currentSort = preferencesRepository.getFilterPreferences().getSort(),
            currentKeyWord = savedStateHandle.get<String>(PARAM_KEYWORD),
            currentGenres = preferencesRepository.getFilterPreferences().getGenres(),
            currentSubgenres = preferencesRepository.getFilterPreferences().getSubgenres(),
            currentSegment = preferencesRepository.getFilterPreferences().getSegment()
        )
    )
    val uiState = _uiState.asStateFlow()


    // load events from repository
    fun getEvents(
        keyWord: String? = savedStateHandle.get<String>(PARAM_KEYWORD),
        geoPoint: String = preferencesRepository.getLocationPreferences().getLocation(),
        radius: String = preferencesRepository.getFilterPreferences().getRadius(),
        startDateTime: String = preferencesRepository.getFilterPreferences().getStartDateTime(),
        sort: String = preferencesRepository.getFilterPreferences().getSort(),
        genres: List<String>? = preferencesRepository.getFilterPreferences().getGenres(),
        subgenres: List<String>? = preferencesRepository.getFilterPreferences().getSubgenres(),
        segment: String? = preferencesRepository.getFilterPreferences().getSegment(),
        page: String? = null,
    ) {

        // check if any parameters have changed from last call, if not return and use saved event list
        if (
            radius == _uiState.value.currentRadius &&
            geoPoint == _uiState.value.currentGeoPoint &&
            startDateTime == _uiState.value.currentStartDateTime &&
            sort == _uiState.value.currentSort &&
            keyWord == _uiState.value.currentKeyWord &&
            genres == _uiState.value.currentGenres &&
            subgenres == _uiState.value.currentSubgenres &&
            segment == _uiState.value.currentSegment &&
            _uiState.value.hasLoadedOnce == true
        ) return


        // launch coroutine to get events from repository
        viewModelScope.launch(Dispatchers.IO) {

            Log.d("EventListViewModel", "Getting events")
            Log.d("EventListViewModel", "Radius: $radius")
            Log.d("EventListViewModel", "GeoPoint: $geoPoint")
            Log.d("EventListViewModel", "StartDateTime: $startDateTime")
            Log.d("EventListViewModel", "Sort: $sort")
            Log.d("EventListViewModel", "KeyWord: $keyWord")
            Log.d("EventListViewModel", "Genres: $genres")
            Log.d("EventListViewModel", "Subgenres: $subgenres")
            Log.d("EventListViewModel", "Segment: $segment")
            Log.d("EventListViewModel", "Page: $page")

            // update state with new parameters
            _uiState.update { currentState ->
                currentState.copy(
                    currentRadius = radius,
                    currentGeoPoint = geoPoint,
                    currentStartDateTime = startDateTime,
                    currentSort = sort,
                    currentKeyWord = keyWord,
                    currentGenres = genres,
                    currentSubgenres = subgenres,
                    currentSegment = segment,
                    hasLoadedOnce = true
                )
            }

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
                    is Resource.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                events = result,
                            )
                        }
                        Log.d("EventListViewModel", "Success")
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                events = result
                            )
                        }
                        Log.d("EventListViewModel", "Error: ${result.message}")
                    }

                    is Resource.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                events = result
                            )
                        }
                        Log.d("EventListViewModel", "Loading")
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
}