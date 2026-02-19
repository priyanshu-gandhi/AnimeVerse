package com.example.animeverse.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String,
    val rating: Double,
    val episodes: Int,
    val genres: List<String>,
    val plot: String? = null,
    val mainCast: String =  "Cast information not available" ,
    val trailerUrl: String? = null
)
