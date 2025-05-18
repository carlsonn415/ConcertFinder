package com.example.concertfinder.network

import com.example.concertfinder.model.apidata.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

// IMPORTANT: STORE API KEY ON SERVER AND NOT IN CODE BEFORE DISTRIBUTION
const val apiKey = "0FhVbrCAPJdEh7PBbHl8Gk8T6OLpKJI2"

interface ConcertFinderApiService {

    @GET("events")
    suspend fun getApiResponse(
        @Query("radius") radius: String,
        @Query("geoPoint") geoPoint: String,
        @Query("startDateTime") startDateTime: String,
        @Query("sort") sort: String,
        @Query("keyword") keyWord: String?,
        @Query("page") page: String?,
        @Query("apikey") apiKey: String = com.example.concertfinder.network.apiKey,
    ): ApiResponse
}