package com.example.lineup_app.data.remote

import com.example.lineup_app.common.Constants.TICKETMASTER_API_KEY
import com.example.lineup_app.data.remote.classification_dto.ClassificationApiResponse
import com.example.lineup_app.data.remote.event_dto.EventsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {

    @GET("events")
    suspend fun getEventsApiResponse(
        @Query("radius") radius: String,
        @Query("geoPoint") geoPoint: String,
        @Query("startDateTime") startDateTime: String,
        @Query("endDateTime") endDateTime: String,
        @Query("sort") sort: String,
        @Query("includeSpellcheck") includeSpellcheck: String,
        @Query("genreId") genres: List<String>?,
        @Query("subGenreId") subgenres: List<String>?,
        @Query("segmentId") segment: List<String>?,
        @Query("segmentName") segmentName: String?,
        @Query("keyword") keyWord: String?,
        @Query("page") page: String?,
        @Query("size") pageSize: String?,
        @Query("apikey") apiKey: String = TICKETMASTER_API_KEY,
    ): EventsApiResponse

    @GET("classifications")
    suspend fun getClassificationsApiResponse(
        @Query("apikey") apiKey: String = TICKETMASTER_API_KEY,
    ): ClassificationApiResponse

}