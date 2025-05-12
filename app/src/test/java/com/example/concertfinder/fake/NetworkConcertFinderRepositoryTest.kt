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
        assertEquals(FakeDataSource.eventsList, repository.getEvents())
    }

}