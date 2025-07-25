package com.example.lineup_app.domain.use_case.get_events_from_network

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event
import com.example.lineup_app.domain.repository.RemoteEventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val remoteRepository: RemoteEventsRepository
) {
    operator fun invoke(
        radius: String = "",
        geoPoint: String = "",
        startDateTime: String = "",
        endDateTime: String = "",
        sort: String = "",
        genres: List<String>? = null,
        subgenres: List<String>? = null,
        segmentId: String? = null,
        segmentName: String? = null,
        keyWord: String? = null,
        page: String? = null,
        pageSize: String? = null,
    ): Flow<Resource<List<Event>>> = flow {

        // Emit loading state
        emit(Resource.Loading())

        // Fetch events from repository and map to domain model
        emit(
            remoteRepository.getEvents(
                radius = radius,
                geoPoint = geoPoint,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                sort = sort,
                includeSpellcheck = "yes", // Always include spellcheck, user doesn't need this parameter
                genres = genres,
                subgenres = subgenres,
                segmentId = if (segmentId == null) null else listOf(segmentId), // Convert to list for api request if not null
                segmentName = segmentName,
                keyWord = keyWord,
                page = page,
                pageSize = pageSize,
            )
        )
    }
}