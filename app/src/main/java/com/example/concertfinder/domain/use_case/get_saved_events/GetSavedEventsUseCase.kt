package com.example.concertfinder.domain.use_case.get_saved_events

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.repository.LocalEventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedEventsUseCase @Inject constructor(
    private val localEventsRepository: LocalEventsRepository
) {
    suspend fun getSavedEvents(): Flow<Resource<List<Event>>> {
        return localEventsRepository.getEvents()
    }

    suspend fun getSavedEventsIds(): Set<String> {
        return localEventsRepository.getSavedEventsIds()
    }
}