package com.example.concertfinder.data.remote.classification_dto


import com.example.concertfinder.data.model.Subgenre
import com.google.gson.annotations.SerializedName

data class EmbeddedXX(
    @SerializedName("subgenres")
    val subgenres: List<SubgenreDto>?
)

fun EmbeddedXX.toSubgenreList(): List<Subgenre> {
    val subgenres = mutableListOf<Subgenre>()

    for (subgenre in this.subgenres ?: emptyList()) {
        subgenres.add(
            Subgenre(
                id = subgenre.id ?: "",
                name = subgenre.name ?: ""
            )
        )
    }
    return subgenres
}