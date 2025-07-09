package com.example.lineup_app.di

import android.content.Context
import com.example.lineup_app.common.Constants.BASE_URL
import com.example.lineup_app.data.local.AndroidStringResourceProvider
import com.example.lineup_app.data.local.AppDatabase
import com.example.lineup_app.data.local.ClassificationDao
import com.example.lineup_app.data.local.EventDao
import com.example.lineup_app.data.remote.AppApiService
import com.example.lineup_app.data.remote.AppLocationManagerService
import com.example.lineup_app.data.remote.LocationManagerService
import com.example.lineup_app.data.repository.AppSearchHistoryRepository
import com.example.lineup_app.data.repository.classification_repository.AppLocalClassificationRepository
import com.example.lineup_app.data.repository.classification_repository.AppRemoteClassificationRepository
import com.example.lineup_app.data.repository.events_repository.AppLocalEventsRepository
import com.example.lineup_app.data.repository.events_repository.AppRemoteRemoteEventsRepository
import com.example.lineup_app.data.repository.preference_repository.AppPreferencesRepository
import com.example.lineup_app.domain.repository.LocalClassificationRepository
import com.example.lineup_app.domain.repository.LocalEventsRepository
import com.example.lineup_app.domain.repository.PreferencesRepository
import com.example.lineup_app.domain.repository.RemoteClassificationRepository
import com.example.lineup_app.domain.repository.RemoteEventsRepository
import com.example.lineup_app.domain.repository.SearchHistoryRepository
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
    fun provideLineUpAppApiService(): AppApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppApiService::class.java)

    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideClassificationDao(appDatabase: AppDatabase): ClassificationDao {
        return appDatabase.classificationDao()
    }

    @Provides
    @Singleton
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDao()
    }

    @Provides
    @Singleton
    fun provideRemoteEventsRepository(apiService: AppApiService, localEventsRepository: LocalEventsRepository): RemoteEventsRepository {
        return AppRemoteRemoteEventsRepository(apiService, localEventsRepository)
    }

    @Provides
    @Singleton
    fun provideLocalEventsRepository(eventDao: EventDao): LocalEventsRepository {
        return AppLocalEventsRepository(eventDao)
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

    @Provides
    @Singleton
    fun provideRemoteClassificationRepository(apiService: AppApiService): RemoteClassificationRepository {
        return AppRemoteClassificationRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideLocalClassificationRepository(
        apiService: AppApiService,
        classificationDao: ClassificationDao,
        preferencesRepository: AppPreferencesRepository
    ): LocalClassificationRepository {
        return AppLocalClassificationRepository(classificationDao, apiService, preferencesRepository)
    }

    @Provides
    @Singleton
    fun provideAppSearchHistoryRepository(@ApplicationContext context: Context): SearchHistoryRepository {
        return AppSearchHistoryRepository(context)
    }

    @Provides
    @Singleton
    fun provideStringResourceProvider(@ApplicationContext context: Context): AndroidStringResourceProvider {
        return AndroidStringResourceProvider(context)
    }

}