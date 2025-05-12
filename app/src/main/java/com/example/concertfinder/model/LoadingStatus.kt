package com.example.concertfinder.model

sealed interface LoadingStatus {
    data class Success(
        val message: String,
        val eventList: List<Event>
    ) : LoadingStatus
    data class Error(val message: String) : LoadingStatus
    object Loading : LoadingStatus
}