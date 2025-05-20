package com.example.concertfinder.fake

import com.example.concertfinder.data.repository.EventsRepository
import com.example.concertfinder.data.model.Event

class FakeNetworkEventsRepository : EventsRepository {

    override suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        keyWord: String?,
        page: String?
    ): List<Event> {
        return FakeDataSource.eventsList
    }
}