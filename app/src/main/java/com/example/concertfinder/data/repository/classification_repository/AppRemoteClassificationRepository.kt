package com.example.concertfinder.data.repository.classification_repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Classification
import com.example.concertfinder.data.remote.AppApiService
import com.example.concertfinder.data.remote.classification_dto.toClassificationList
import com.example.concertfinder.domain.repository.RemoteClassificationRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AppRemoteClassificationRepository @Inject constructor(
    private val apiService: AppApiService
) : RemoteClassificationRepository {
    override suspend fun getClassifications(): Resource<List<Classification>> {

        return try {
            val classificationsApiResponse = apiService.getClassificationsApiResponse()
            val classificationList = classificationsApiResponse.toClassificationList()

            if (classificationList.isNotEmpty()) {
                Resource.Success(classificationList)
            } else {
                Resource.Error("No classifications found")
            }
        } catch (e: HttpException) {
            Resource.Error<List<Classification>>(e.localizedMessage ?: "An unexpected error occurred")
        } catch (_: IOException) {
            Resource.Error<List<Classification>>("Couldn't reach server. Check your internet connection.")
        }
    }
}