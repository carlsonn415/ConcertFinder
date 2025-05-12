package com.example.concertfinder.data

import com.example.concertfinder.model.ApiResponse
import com.example.concertfinder.model.Event
import com.example.concertfinder.network.ConcertFinderApiService

interface EventsRepository {
    suspend fun getEvents(): List<Event>
}

class NetworkEventsRepository(
    private val apiService: ConcertFinderApiService
) : EventsRepository {
    override suspend fun getEvents(): List<Event> = apiService.getApiResponse().embedded?.events ?: emptyList()
}