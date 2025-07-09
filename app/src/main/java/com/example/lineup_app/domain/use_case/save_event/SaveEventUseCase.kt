package com.example.lineup_app.domain.use_case.save_event

import com.example.lineup_app.data.model.Event
import com.example.lineup_app.domain.repository.LocalEventsRepository
import javax.inject.Inject

class SaveEventUseCase @Inject constructor(
    private val localEventsRepository: LocalEventsRepository
) {
    suspend operator fun invoke(event: Event) {
        localEventsRepository.saveNewEvent(event)
    }
}