package com.example.concertfinder.network

import com.example.concertfinder.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

// IMPORTANT: STORE API KEY ON SERVER AND NOT IN CODE BEFORE DISTRIBUTION
const val apiKey = "0FhVbrCAPJdEh7PBbHl8Gk8T6OLpKJI2"

interface ConcertFinderApiService {

    @GET("events")
    suspend fun getApiResponse(
        @Query("dmaId") locale: String = "245",
        @Query("size") size: Int = 20,
        @Query("apikey") apiKey: String = com.example.concertfinder.network.apiKey,
    ): ApiResponse
}