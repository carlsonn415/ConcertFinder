package com.example.lineup_app.domain.repository

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Classification

interface RemoteClassificationRepository {
    suspend fun getClassifications(): Resource<List<Classification>>
}