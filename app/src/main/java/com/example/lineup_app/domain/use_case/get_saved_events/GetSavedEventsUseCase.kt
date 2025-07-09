package com.example.lineup_app.domain.use_case.get_saved_events

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event
import com.example.lineup_app.domain.repository.LocalEventsRepository
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