package com.example.concertfinder.ui

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
import com.example.concertfinder.ui.utils.TopLevelRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


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
        //radius: String = "50",
        //postalCode: String = "TX 78701, US",
        keyWord: String? = null,
        //page: String? = null,
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
                    //radius = radius,
                    //postalCode = postalCode,
                    keyWord = keyWord,
                    //page = page
                )
                Log.d("ConcertFinderViewModel", "getEvents: $events")
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