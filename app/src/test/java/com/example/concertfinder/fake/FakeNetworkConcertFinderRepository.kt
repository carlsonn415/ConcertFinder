package com.example.concertfinder.fake

import com.example.concertfinder.data.EventsRepository
import com.example.concertfinder.model.Event

class FakeNetworkEventsRepository : EventsRepository {
    override suspend fun getEvents(): List<Event> {
        return FakeDataSource.eventsList
    }
}