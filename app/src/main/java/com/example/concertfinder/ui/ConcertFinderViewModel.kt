package com.example.concertfinder.ui

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.concertfinder.ConcertFinderApplication
import com.example.concertfinder.data.EventsRepository
import com.example.concertfinder.model.ConcertFinderUiState
import com.example.concertfinder.model.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class ConcertFinderViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ConcertFinderUiState())
    val uiState = _uiState.asStateFlow()

    init {

    }

    // update show bottom bar
    fun updateShowBottomBar(show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomBar = show
            )
        }
    }

    // update search expanded
    fun updateSearchExpanded(expanded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                searchExpanded = expanded
            )
        }
    }

    // update search text
    fun updateSearchText(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchText = text
            )
        }
    }

    // reset search bar
    fun resetSearchBar() {
        _uiState.update { currentState ->
            currentState.copy(
                searchExpanded = false,
                searchText = ""
            )
        }
    }



    // load events from repository
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getEvents(
        radius: String = "50",
        geoPoint: String = "40.7128,-74.0060",
        startDateTime: String = getFormattedDate(Calendar.getInstance()),
        sort: String = "date,asc",
        keyWord: String? = null,
        page: String? = null,
    ) {

        // launch coroutine to get events from repository
        viewModelScope.launch(Dispatchers.IO) {
            // update ui state to loading
            _uiState.update {
                    currentState -> currentState.copy(
                loadingStatus = LoadingStatus.Loading
            )
            }
             try { // try to get events from repository
                val events = eventsRepository.getEvents(
                    radius = radius,
                    geoPoint = geoPoint,
                    startDateTime = startDateTime,
                    sort = sort,
                    keyWord = keyWord,
                    page = page
                )
                _uiState.update {
                    currentState -> currentState.copy(
                        loadingStatus = LoadingStatus.Success,
                        searchResults = events
                    )
                }
            } catch (e: IOException) { // catch exceptions
                _uiState.update {
                    currentState -> currentState.copy(
                        loadingStatus = LoadingStatus.Error(e.message.toString())
                    )
                }
            } catch (e: HttpException) {
                 _uiState.update {
                     currentState -> currentState.copy(
                        loadingStatus = LoadingStatus.Error(e.message.toString())
                    )
                 }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun getFormattedDate(calendar: Calendar): String {
        // convert calendar to instant
        val instant: Instant = calendar.toInstant()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneId.of("UTC"))

        return formatter.format(instant)
    }

    // injects view model with events repository
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ConcertFinderApplication)
                val eventsRepository = application.container.eventsRepository
                ConcertFinderViewModel(eventsRepository = eventsRepository)
            }
        }
    }
}