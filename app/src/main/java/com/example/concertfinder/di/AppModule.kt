package com.example.concertfinder.di

import android.content.Context
import com.example.concertfinder.common.Constants.BASE_URL
import com.example.concertfinder.data.remote.AppApiService
import com.example.concertfinder.data.repository.AppPreferencesRepository
import com.example.concertfinder.data.repository.AppEventsRepository
import com.example.concertfinder.domain.repository.EventsRepository
import com.example.concertfinder.domain.repository.PreferencesRepository
import com.example.concertfinder.data.remote.AppLocationManagerService
import com.example.concertfinder.data.remote.LocationManagerService
import com.example.concertfinder.data.repository.AppLocationPreferencesRepository
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // ensures the app module lives as long as the application
object AppModule {

    @Provides
    @Singleton // ensures that there is only one instance
    fun provideConcertFinderApiService(): AppApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppApiService::class.java)

    }

    @Provides
    @Singleton
    fun provideEventsRepository(apiService: AppApiService): EventsRepository {
        return AppEventsRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManagerService {
        return AppLocationManagerService(
            context = context,
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        )
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository {
        return AppPreferencesRepository(context)
    }
}