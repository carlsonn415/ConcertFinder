package com.example.concertfinder.data.repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.data.remote.AppApiService
import com.example.concertfinder.data.remote.event_dto.toEvent
import com.example.concertfinder.domain.repository.EventsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class AppEventsRepository @Inject constructor(
    private val apiService: AppApiService
) : EventsRepository {

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
            val events = apiService.getEventsApiResponse(
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
            ).embedded?.events?.map { it.toEvent() }

            return if (events != null) {
                Resource.Success(events)
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