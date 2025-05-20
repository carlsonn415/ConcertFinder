package com.example.concertfinder.data.remote.classification_dto

import com.example.concertfinder.data.model.Genre
import com.google.gson.annotations.SerializedName

data class EmbeddedX(
    @SerializedName("genres")
    val genres: List<GenreDto>?
) {
    fun toGenreList(): List<Genre> {
        val genres = mutableListOf<Genre>()
        for (genre in this.genres ?: emptyList()) {
            genres.add(
                Genre(
                    id = genre.id ?: "",
                    name = genre.name ?: "",
                    subgenres = genre.embedded?.toSubgenreList() ?: emptyList()
                )
            )
        }
        return genres
    }
}