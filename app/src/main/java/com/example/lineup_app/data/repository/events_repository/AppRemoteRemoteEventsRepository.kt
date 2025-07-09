package com.example.lineup_app.data.repository.events_repository

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event
import com.example.lineup_app.data.remote.AppApiService
import com.example.lineup_app.data.remote.event_dto.toEvent
import com.example.lineup_app.domain.repository.LocalEventsRepository
import com.example.lineup_app.domain.repository.RemoteEventsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AppRemoteRemoteEventsRepository @Inject constructor(
    private val apiService: AppApiService,
    private val localEventsRepository: LocalEventsRepository
) : RemoteEventsRepository {

    override suspend fun getEvents(
        radius: String,
        geoPoint: String,
        startDateTime: String,
        endDateTime: String,
        sort: String,
        includeSpellcheck: String,
        genres: List<String>?,
        subgenres: List<String>?,
        segmentId: List<String>?,
        segmentName: String?,
        keyWord: String?,
        page: String?,
        pageSize: String?
    ): Resource<List<Event>> {
        return try {
            val eventApiResponse = apiService.getEventsApiResponse(
                radius = radius,
                geoPoint = geoPoint,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                sort = sort,
                includeSpellcheck = includeSpellcheck,
                genres = genres,
                subgenres = subgenres,
                segment = segmentId,
                segmentName = segmentName,
                keyWord = keyWord,
                page = page,
                pageSize = pageSize
            )

            val eventsList = eventApiResponse.embedded?.events?.map { it.toEvent() }
            eventsList?.forEach { event ->
                if (localEventsRepository.getEventSavedById(event.id.toString())) {
                    event.saved = true
                }
            }

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