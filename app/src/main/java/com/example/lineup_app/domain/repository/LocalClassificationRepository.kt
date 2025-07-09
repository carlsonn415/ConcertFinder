package com.example.lineup_app.domain.repository

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Genre
import com.example.lineup_app.data.model.Segment
import com.example.lineup_app.data.model.Subgenre

interface LocalClassificationRepository {
    suspend fun getSegments(): Resource<List<Segment>>
    suspend fun getGenres(segmentId: String): Resource<List<Genre>>
    suspend fun getSubgenres(genreIdList: List<String>): Resource<List<Subgenre>>

    suspend fun saveNewClassifications()

    suspend fun shouldFetchNewClassifications(): Boolean
}