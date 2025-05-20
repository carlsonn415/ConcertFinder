package com.example.concertfinder.domain.model

sealed interface LoadingStatus {
    object Idle : LoadingStatus
    object Success : LoadingStatus
    data class Error(val message: String) : LoadingStatus
    object Loading : LoadingStatus
}