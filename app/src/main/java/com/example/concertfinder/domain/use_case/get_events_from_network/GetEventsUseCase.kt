package com.example.concertfinder.domain.use_case.get_events_from_network

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.IncludeSpellcheck
import com.example.concertfinder.domain.repository.RemoteEventsRepository
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
        sort: String = "",
        genres: List<String>? = null,
        subgenres: List<String>? = null,
        segment: String? = null,
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
                sort = sort,
                includeSpellcheck = IncludeSpellcheck.Yes.value, // Always include spellcheck, user doesn't need this parameter
                genres = genres,
                subgenres = subgenres,
                segment = if (segment == null) null else listOf(segment), // Convert to list for api request if not null
                segmentName = segmentName,
                keyWord = keyWord,
                page = page,
                pageSize = pageSize,
            )
        )
    }
}