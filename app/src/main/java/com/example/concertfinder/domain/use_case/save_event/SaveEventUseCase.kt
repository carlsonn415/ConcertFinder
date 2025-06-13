package com.example.concertfinder.domain.use_case.save_event

import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.repository.LocalEventsRepository
import javax.inject.Inject

class SaveEventUseCase @Inject constructor(
    private val localEventsRepository: LocalEventsRepository
) {
    suspend operator fun invoke(event: Event) {
        localEventsRepository.saveNewEvent(event)
    }
}