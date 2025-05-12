package com.example.concertfinder.fake

import com.example.concertfinder.fake.rules.TestDispatcherRule
import com.example.concertfinder.model.ConcertFinderUiState
import com.example.concertfinder.model.LoadingStatus
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
            eventsRepository = FakeNetworkEventsRepository()
        )
        assertEquals(
            ConcertFinderUiState(loadingStatus =
                LoadingStatus.Success(
                    message = "Success: ${FakeDataSource.eventsList.size}",
                    eventList = FakeDataSource.eventsList
                )
            ),
            concertFinderViewModel.uiState.value
        )
    }
}