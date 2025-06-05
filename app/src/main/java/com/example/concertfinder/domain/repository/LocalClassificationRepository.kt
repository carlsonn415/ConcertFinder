package com.example.concertfinder.domain.repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Genre
import com.example.concertfinder.data.model.Segment
import com.example.concertfinder.data.model.Subgenre

interface LocalClassificationRepository {
    suspend fun getSegments(): Resource<List<Segment>>
    suspend fun getGenres(segmentId: String): Resource<List<Genre>>
    suspend fun getSubgenres(genreIdList: List<String>): Resource<List<Subgenre>>

    suspend fun saveNewClassifications()

    suspend fun shouldFetchNewClassifications(): Boolean
}