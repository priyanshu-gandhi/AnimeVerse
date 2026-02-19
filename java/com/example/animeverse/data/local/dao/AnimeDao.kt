package com.example.animeverse.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.animeverse.data.local.entities.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Upsert
    suspend fun UpsertAnime(animes: List<AnimeEntity>)

    @Query("SELECT * FROM anime LIMIT :limit OFFSET :offset")
    fun getTopAnime(limit : Int = 20, offset : Int): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE id = :id")
    fun getAnimeById(id: Int): Flow<AnimeEntity?>

    @Query("SELECT * FROM anime WHERE title LIKE '%' || :query || '%'")
    fun searchAnime(query: String): Flow<List<AnimeEntity>>

}