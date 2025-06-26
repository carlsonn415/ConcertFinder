package com.example.concertfinder.domain.repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event

interface RemoteEventsRepository {
    suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        includeSpellcheck: String,
        genres: List<String>?,
        subgenres: List<String>?,
        segment: List<String>?,
        segmentName: String?,
        keyWord: String?,
        page: String?,
        pageSize: String?,
    ): Resource<List<Event>>
}