package com.example.concertfinder.fake

import com.example.concertfinder.data.repositories.EventsRepository
import com.example.concertfinder.model.Event

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