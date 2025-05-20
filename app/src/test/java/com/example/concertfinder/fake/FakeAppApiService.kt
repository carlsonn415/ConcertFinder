package com.example.concertfinder.fake

import com.example.concertfinder.data.remote.event_dto.EventsApiResponse
import com.example.concertfinder.data.remote.event_dto.EmbeddedApiData
import com.example.concertfinder.data.remote.AppApiService

class FakeAppApiService : AppApiService {

    override suspend fun getEventsApiResponse(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        keyWord: String?,
        page: String?,
        apiKey: String
    ): EventsApiResponse {
        return EventsApiResponse(
            embedded = EmbeddedApiData(
                events = FakeDataSource.eventsList
            )
        )
    }
}