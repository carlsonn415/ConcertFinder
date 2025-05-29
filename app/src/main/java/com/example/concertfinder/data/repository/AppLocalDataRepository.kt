package com.example.concertfinder.data.repository

import com.example.concertfinder.domain.repository.LocalDataRepository
import com.example.concertfinder.data.local.ClassificationDao


class AppLocalDataRepository(
    private val classificationDao: ClassificationDao
) : LocalDataRepository {

}