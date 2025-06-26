package com.example.concertfinder.presentation.discover_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.common.Constants
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.repository.PreferencesRepository
import com.example.concertfinder.domain.use_case.get_events_from_network.GetEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverScreenViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverScreenUiState())
    val uiState: StateFlow<DiscoverScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadEventsNearYou()
            delay(200)
            loadMusicEvents()
            delay(200)
            loadSportsEvents()
            delay(200)
            loadArtsEvents()
            delay(200)
            loadEventsThisWeekend()
        }
    }

    fun loadEventsThisWeekend() {
        viewModelScope.launch(Dispatchers.IO) {
            getEvents(
                startDateTime = getWeekendStartDate(),
                endDateTime = getWeekendEndDate()
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                eventsThisWeekend = resource
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                eventsThisWeekend = resource
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                eventsThisWeekend = Resource.Loading()
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadEventsNearYou() {
        viewModelScope.launch(Dispatchers.IO) {
            getEvents(
                sort = "distance,asc",
                radius = Constants.EVENTS_NEAR_YOU_RADIUS
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                eventsNearYou = resource
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                eventsNearYou = resource
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                eventsNearYou = Resource.Loading()
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadMusicEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            getEvents(
                segmentName = "Music"
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                musicEvents = resource
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                musicEvents = resource
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                musicEvents = Resource.Loading()
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadSportsEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            getEvents(
                segmentName = "Sports"
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                sportsEvents = resource
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                sportsEvents = resource
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                sportsEvents = Resource.Loading()
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadArtsEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            getEvents(
                segmentName = "Arts & Theatre"
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                artsEvents = resource
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                artsEvents = resource
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                artsEvents = Resource.Loading()
                            )
                        }
                    }
                }
            }
        }
    }

    // load events from repository
    fun getEvents(
        geoPoint: String = preferencesRepository.getLocationPreferences().getLocation(),
        radius: String = "",
        startDateTime: String = "",
        endDateTime: String = "",
        sort: String = "", // Default sort is relevance
        segmentName: String? = null,
        pageSize: String? = Constants.DISCOVER_PAGE_SIZE,
    ): Flow<Resource<List<Event>>> {
        return getEventsUseCase(
            radius = radius,
            geoPoint = geoPoint,
            startDateTime = startDateTime,
            sort = sort,
            segmentName = segmentName,
            pageSize = pageSize
        )
    }

    fun getWeekendStartDate(): String {
        // TODO: get weekend start date
        return ""
    }

    fun getWeekendEndDate(): String {
        // TODO: get weekend end date
        return ""
    }

}