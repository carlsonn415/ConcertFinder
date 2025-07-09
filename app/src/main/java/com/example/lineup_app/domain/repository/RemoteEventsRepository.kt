package com.example.lineup_app.domain.repository

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event

interface RemoteEventsRepository {
    suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        endDateTime: String,
        sort: String,
        includeSpellcheck: String,
        genres: List<String>?,
        subgenres: List<String>?,
        segmentId: List<String>?,
        segmentName: String?,
        keyWord: String?,
        page: String?,
        pageSize: String?,
    ): Resource<List<Event>>
}