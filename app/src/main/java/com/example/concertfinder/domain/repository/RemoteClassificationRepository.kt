package com.example.concertfinder.domain.repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Classification

interface RemoteClassificationRepository {
    suspend fun getClassifications(): Resource<List<Classification>>
}