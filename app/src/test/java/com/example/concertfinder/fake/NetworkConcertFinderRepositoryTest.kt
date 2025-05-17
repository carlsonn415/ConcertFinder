package com.example.concertfinder.fake

import com.example.concertfinder.data.NetworkEventsRepository
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlinx.coroutines.test.runTest

class NetworkEventsRepositoryTest {
    @Test
    fun networkConcertFinderRepository_getEvents_verifyEventList() = runTest {
        val repository = NetworkEventsRepository(
            apiService = FakeConcertFinderApiService()
        )
        assertEquals(
            FakeDataSource.eventsList,
            repository.getEvents(
                // values don't matter for these tests
                radius = "50",
                geoPoint = "40.7128,-74.0060",
                startDateTime = "2023-06-01T00:00:00Z",
                sort = "date,asc",
                keyWord = null,
                page = null
            )
        )
    }

}