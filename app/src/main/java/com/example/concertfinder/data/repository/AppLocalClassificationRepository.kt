package com.example.concertfinder.data.repository

import android.util.Log
import com.example.concertfinder.common.Resource
import com.example.concertfinder.domain.repository.LocalClassificationRepository
import com.example.concertfinder.data.local.ClassificationDao
import com.example.concertfinder.data.model.Genre
import com.example.concertfinder.data.model.Segment
import com.example.concertfinder.data.model.Subgenre
import com.example.concertfinder.data.remote.AppApiService
import com.example.concertfinder.data.remote.classification_dto.SegmentDto
import com.example.concertfinder.data.repository.preference_repository.AppPreferencesRepository
import javax.inject.Inject


class AppLocalClassificationRepository @Inject constructor(
    private val classificationDao: ClassificationDao,
    private val apiService: AppApiService,
    private val appPreferencesRepository: AppPreferencesRepository
) : LocalClassificationRepository {

    override suspend fun getSegments(): Resource<List<Segment>> {
        try {
            // Fetch segments from the database
            val segmentEntities = classificationDao.getAllSegmentEntities()
            val segmentList = mutableListOf<Segment>()
            // Convert the entities to domain models
            for (segmentEntity in segmentEntities) {
                segmentList.add(Segment(segmentEntity.segmentId, segmentEntity.name))
            }
            // Return the list of segments
            return Resource.Success(segmentList)
        } catch (e: Exception) {
            Log.e("AppLocalDataRepository", "Error fetching segments from database: ${e.message}")
            return Resource.Error("Error fetching segments from database: ${e.message}")
        }
    }

    override suspend fun getGenres(segmentId: String): Resource<List<Genre>> {
        try {
            // Fetch genres from the database
            val genreEntities = classificationDao.getGenreEntitiesForSegment(segmentId)
            val genreList = mutableListOf<Genre>()
            // Convert the entities to domain models
            for (genreEntity in genreEntities) {
                genreList.add(Genre(genreEntity.genreId, genreEntity.name, emptyList()))
            }
            // Return the list of genres
            return Resource.Success(genreList)
        } catch (e: Exception) {
            Log.e("AppLocalDataRepository", "Error fetching genres from database: ${e.message}")
            return Resource.Error("Error fetching genres from database: ${e.message}")
        }
    }

    override suspend fun getSubgenres(genreIdList: List<String>): Resource<List<Subgenre>> {
        val genreList = appPreferencesRepository.getFilterPreferences().getGenres() ?: emptyList()
        if (genreList.isNotEmpty()) {
            try {
                // Fetch subgenres from the database
                val subgenreList = mutableListOf<Subgenre>()
                for (genre in genreList) {
                    subgenreList.addAll(classificationDao.getSubgenreEntitiesForGenre(genre).map { Subgenre(it.subgenreId, it.name) })
                }
                // Return the list of subgenres
                return Resource.Success(subgenreList)
            } catch (e: Exception) {
                Log.e("AppLocalDataRepository", "Error fetching subgenres from database: ${e.message}")
                return Resource.Error("Error fetching subgenres from database: ${e.message}")
            }
        }
        return Resource.Error("No genres selected")
    }

    override suspend fun saveNewClassifications() {
        try {
            val segments: MutableList<SegmentDto> = mutableListOf()
            // Fetch classifications from the API
            try {
                val response = apiService.getClassificationsApiResponse().embedded?.classifications ?: emptyList()
                // Add each classification to the list
                for (classification in response) {
                    if (classification.segmentDto != null) {
                        segments.add(classification.segmentDto)
                    }
                }
            } catch (e: Exception) {
                Log.e("AppLocalDataRepository", "Error fetching classifications from API: ${e.message}")
                return
            }
            // Save the classifications to the database
            classificationDao.saveFullSegments(segments)
        } catch (e: Exception) {
            Log.e("AppLocalDataRepository", "Error saving new classifications: ${e.message}")
            throw e
        }
    }

    override suspend fun shouldFetchNewClassifications(): Boolean {
        // Check if the database is empty
        return classificationDao.getAllSegmentEntities().isEmpty()
    }
}