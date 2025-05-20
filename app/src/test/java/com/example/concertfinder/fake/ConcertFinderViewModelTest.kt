package com.example.concertfinder.fake

import com.example.concertfinder.fake.rules.TestDispatcherRule
import com.example.concertfinder.presentation.app.AppUiState
import com.example.concertfinder.domain.model.LoadingStatus
import com.example.concertfinder.ui.ConcertFinderViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ConcertFinderViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun concertFinderViewModel_getEvents_verifyConcertFinderUiStateSuccess() = runTest {

        val concertFinderViewModel = ConcertFinderViewModel(
            eventsRepository = FakeNetworkEventsRepository(),
            locationManager = FakeLocationManagerService(),
            locationPreferences = FakeLocationPreferencesRepository()
        )

        concertFinderViewModel.getEvents()

        assertEquals(
            AppUiState(
                loadingStatus = LoadingStatus.Success,
                address = "New York, NY",
            ),
            concertFinderViewModel.uiState.value
        )
    }
}