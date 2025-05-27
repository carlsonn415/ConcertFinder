package com.example.concertfinder.common

sealed class Resource<T>(val data: T? = null, val message: String? = null, val totalPages: String? = null) {
    class Success<T>(data: T, totalPages: String? = null) : Resource<T>(data, totalPages = totalPages)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}