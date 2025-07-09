package com.example.lineup_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.lineup_app.data.local.entities.classification_entities.GenreEntity
import com.example.lineup_app.data.local.entities.classification_entities.SegmentEntity
import com.example.lineup_app.data.local.entities.classification_entities.SubgenreEntity
import com.example.lineup_app.data.remote.classification_dto.SegmentDto


@Dao
interface ClassificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSegments(segments: List<SegmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubgenres(subgenres: List<SubgenreEntity>)

    @Transaction
    suspend fun saveFullSegments(segments: List<SegmentDto>) {
        val segmentEntities = mutableListOf<SegmentEntity>()
        val allGenreEntities = mutableListOf<GenreEntity>()
        val allSubgenreEntities = mutableListOf<SubgenreEntity>()

        segments.forEach { segmentModel ->

            if (segmentModel.id != null && segmentModel.name != null) {

                segmentEntities.add(SegmentEntity(segmentModel.id, segmentModel.name))

                segmentModel.embedded?.genres?.forEach { genreModel ->

                    if (genreModel.id != null && genreModel.name != null) {

                        allGenreEntities.add(GenreEntity(genreModel.id, genreModel.name, segmentModel.id))

                        genreModel.embedded?.subgenres?.forEach { subgenreModel ->

                            if (subgenreModel.id != null && subgenreModel.name != null) {

                                allSubgenreEntities.add(SubgenreEntity(subgenreModel.id, subgenreModel.name, genreModel.id))
                            }
                        }
                    }
                }
            }
        }

        insertSegments(segmentEntities)
        insertGenres(allGenreEntities)
        insertSubgenres(allSubgenreEntities)

    }

    // Get operations

    @Query("SELECT * FROM subgenres")
    suspend fun getAllSubgenreEntities(): List<SubgenreEntity>

    @Transaction
    @Query("SELECT * FROM genres")
    suspend fun getAllGenreEntities(): List<GenreEntity>

    @Transaction
    @Query("SELECT * FROM segments")
    suspend fun getAllSegmentEntities(): List<SegmentEntity>

    @Query("SELECT * FROM genres WHERE parentSegmentId = :segmentId")
    suspend fun getGenreEntitiesForSegment(segmentId: String): List<GenreEntity>

    @Query("SELECT * FROM subgenres WHERE parentGenreId = :genreId")
    suspend fun getSubgenreEntitiesForGenre(genreId: String): List<SubgenreEntity>

}