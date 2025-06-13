package com.example.concertfinder.data.repository.events_repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.data.remote.AppApiService
import com.example.concertfinder.data.remote.event_dto.toEvent
import com.example.concertfinder.domain.repository.RemoteEventsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AppRemoteRemoteEventsRepository @Inject constructor(
    private val apiService: AppApiService
) : RemoteEventsRepository {

    override suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        sort: String,
        includeSpellcheck: String,
        genres: List<String>?,
        subgenres: List<String>?,
        segment: List<String>?,
        keyWord: String?,
        page: String?
    ): Resource<List<Event>> {
        return try {
            val eventApiResponse = apiService.getEventsApiResponse(
                radius = radius,
                geoPoint = geoPoint,
                startDateTime = startDateTime,
                sort = sort,
                includeSpellcheck = includeSpellcheck,
                genres = genres,
                subgenres = subgenres,
                segment = segment,
                keyWord = keyWord,
                page = page
            )

            val eventsList = eventApiResponse.embedded?.events?.map { it.toEvent() }
            val totalPages = eventApiResponse.pageData?.totalPages.toString()

            return if (eventsList != null) {
                Resource.Success(eventsList, totalPages)
            } else {
                Resource.Error("No events found")
            }
        } catch (e: HttpException) {
            Resource.Error<List<Event>>(e.localizedMessage ?: "An unexpected error occurred")
        } catch (_: IOException) {
            Resource.Error<List<Event>>("Couldn't reach server. Check your internet connection.")
        }
    }
}