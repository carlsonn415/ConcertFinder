package com.example.concertfinder.data

import com.example.concertfinder.network.ConcertFinderApiService
import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



interface AppContainer {
    val eventsRepository: EventsRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://app.ticketmaster.com/discovery/v2/"

    private val gson = GsonBuilder().create()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: ConcertFinderApiService by lazy {
        retrofit.create(ConcertFinderApiService::class.java)
    }

    override val eventsRepository: EventsRepository by lazy {
        NetworkEventsRepository(retrofitService)
    }

}