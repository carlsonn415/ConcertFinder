package com.example.concertfinder.data.remote

import com.example.concertfinder.common.Constants.API_KEY
import com.example.concertfinder.data.remote.classification_dto.ClassificationApiResponse
import com.example.concertfinder.data.remote.event_dto.EventsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {

    @GET("events")
    suspend fun getEventsApiResponse(
        @Query("radius") radius: String,
        @Query("geoPoint") geoPoint: String,
        @Query("startDateTime") startDateTime: String,
        @Query("sort") sort: String,
        @Query("includeSpellcheck") includeSpellcheck: String,
        @Query("genreId") genres: List<String>?,
        @Query("subGenreId") subgenres: List<String>?,
        @Query("segmentId") segment: List<String>?,
        @Query("keyword") keyWord: String?,
        @Query("page") page: String?,
        @Query("apikey") apiKey: String = API_KEY,
    ): EventsApiResponse

    @GET("classifications")
    suspend fun getClassificationsApiResponse(
        @Query("apikey") apiKey: String = API_KEY,
    ): ClassificationApiResponse

}