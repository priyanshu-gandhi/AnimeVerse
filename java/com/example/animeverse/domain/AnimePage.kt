package com.example.animeverse.domain

data class AnimePage(
    val animeList: List<Anime>,
    val currentPage: Int,
    val hasNextPage: Boolean
)

data class Anime(
    val id: Int,
    val title: String,
    val episodes: Int,
    val rating: Double,
    val imageUrl: String,
    val trailerUrl: String?,
    val synopsis: String,
    val genres: List<String>
)
