package com.example.concertfinder.data

import com.example.concertfinder.model.Event
import com.example.concertfinder.network.ConcertFinderApiService

interface EventsRepository {
    suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        keyWord: String?,
        page: String?,
    ): List<Event>
}

class NetworkEventsRepository(
    private val apiService: ConcertFinderApiService
) : EventsRepository {
    override suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        keyWord: String?,
        page: String?,
    ): List<Event> = apiService.getApiResponse(
        radius = radius,
        geoPoint = geoPoint,
        startDateTime = startDateTime,
        sort = sort,
        keyWord = keyWord,
        page = page
    ).embedded?.events ?: emptyList()
}