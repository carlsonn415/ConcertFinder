package com.example.concertfinder.domain.use_case.unsave_event

import com.example.concertfinder.domain.repository.LocalEventsRepository
import javax.inject.Inject

class UnsaveEventUseCase @Inject constructor(
    private val localEventsRepository: LocalEventsRepository
) {
    suspend operator fun invoke(eventId: String) {
        localEventsRepository.deleteEventById(eventId)
    }
}