package com.example.lineup_app.domain.use_case.unsave_event

import com.example.lineup_app.domain.repository.LocalEventsRepository
import javax.inject.Inject

class UnsaveEventUseCase @Inject constructor(
    private val localEventsRepository: LocalEventsRepository
) {
    suspend operator fun invoke(eventId: String) {
        localEventsRepository.deleteEventById(eventId)
    }
}