package com.example.concertfinder.fake

import com.example.concertfinder.model.apidata.ApiResponse
import com.example.concertfinder.model.apidata.EmbeddedApiData
import com.example.concertfinder.network.ConcertFinderApiService

class FakeConcertFinderApiService : ConcertFinderApiService {

    override suspend fun getApiResponse(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        keyWord: String?,
        page: String?,
        apiKey: String
    ): ApiResponse {
        return ApiResponse(
            embedded = EmbeddedApiData(
                events = FakeDataSource.eventsList
            )
        )
    }
}