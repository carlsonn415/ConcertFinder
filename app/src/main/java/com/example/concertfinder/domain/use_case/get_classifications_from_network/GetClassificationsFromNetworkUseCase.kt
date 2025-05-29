package com.example.concertfinder.domain.use_case.get_classifications_from_network

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Classification
import com.example.concertfinder.domain.repository.RemoteClassificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassificationsFromNetworkUseCase @Inject constructor(
    private val remoteClassificationRepository: RemoteClassificationRepository
) {
    operator fun invoke(): Flow<Resource<List<Classification>>> = flow {
        // Emit loading state
        emit(Resource.Loading())

        // Fetch classifications from repository and map to domain model
        val classifications = remoteClassificationRepository.getClassifications()

        // Emit success state with classifications
        emit(classifications)
    }
}