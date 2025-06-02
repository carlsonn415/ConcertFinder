package com.example.concertfinder.domain.use_case.get_classifications

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Genre
import com.example.concertfinder.data.model.Segment
import com.example.concertfinder.data.model.Subgenre
import com.example.concertfinder.domain.repository.LocalClassificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassificationsUseCase @Inject constructor(
    private val localClassificationRepository: LocalClassificationRepository
) {
    fun getSegments(): Flow<Resource<List<Segment>>> = flow {
        // Emit loading state
        emit(Resource.Loading())

        // Fetch segments from repository and map to domain model
        val segments = localClassificationRepository.getSegments()

        // Emit success state with segments
        emit(segments)
    }

    fun getGenres(segmentId: String): Flow<Resource<List<Genre>>> = flow {
        // Emit loading state
        emit(Resource.Loading())

        // Fetch genres from repository and map to domain model
        val genres = localClassificationRepository.getGenres(segmentId)

        // Emit success state with genres
        emit(genres)
    }

    fun getSubgenres(genreIdList: List<String>): Flow<Resource<List<Subgenre>>> = flow {
        // Emit loading state
        emit(Resource.Loading())

        // Fetch subgenres from repository and map to domain model
        val subgenres = localClassificationRepository.getSubgenres(genreIdList)

        // Emit success state with subgenres
        emit(subgenres)
    }

    suspend fun saveNewClassifications() {
        localClassificationRepository.saveNewClassifications()
    }

}