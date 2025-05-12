package com.example.concertfinder.fake

import com.example.concertfinder.model.ApiResponse
import com.example.concertfinder.model.EmbeddedData
import com.example.concertfinder.network.ConcertFinderApiService

class FakeConcertFinderApiService : ConcertFinderApiService {
    override suspend fun getApiResponse(
        size: Int,
        apiKey: String
    ): ApiResponse {
        return ApiResponse(
            embedded = EmbeddedData(
                events = FakeDataSource.eventsList
            )
        )
    }
}