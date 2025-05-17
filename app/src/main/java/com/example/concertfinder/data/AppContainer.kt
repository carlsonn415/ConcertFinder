package com.example.concertfinder.data

import android.content.Context
import com.example.concertfinder.data.repositories.EventsRepository
import com.example.concertfinder.data.repositories.NetworkEventsRepository
import com.example.concertfinder.network.ConcertFinderApiService
import com.example.concertfinder.network.DefaultLocationManager
import com.example.concertfinder.network.LocationManager
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



interface AppContainer {
    val eventsRepository: EventsRepository
    val locationManager: LocationManager
    val userLocationPreferences: UserLocationPreferences
}

class DefaultAppContainer(
    private val applicationContext: Context
) : AppContainer {

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

    override val locationManager: LocationManager by lazy {
        DefaultLocationManager(
            context = applicationContext,
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override val userLocationPreferences: UserLocationPreferences by lazy {
        UserLocationPreferences(applicationContext)
    }
}