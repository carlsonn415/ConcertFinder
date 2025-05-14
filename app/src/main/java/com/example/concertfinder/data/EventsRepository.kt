package com.example.concertfinder.data

import com.example.concertfinder.model.ApiResponse
import com.example.concertfinder.model.Event
import com.example.concertfinder.network.ConcertFinderApiService

interface EventsRepository {
    suspend fun getEvents(
        //radius: String,
        //postalCode: String,
        keyWord: String?,
        //page: String?,
    ): List<Event>
}

class NetworkEventsRepository(
    private val apiService: ConcertFinderApiService
) : EventsRepository {
    override suspend fun getEvents(
        //radius: String,
        //postalCode: String,
        keyWord: String?,
        //page: String?,
    ): List<Event> = apiService.getApiResponse(
        //radius = radius,
        //postalCode = postalCode,
        keyWord = keyWord,
        //page = page
    ).embedded?.events ?: emptyList()
}