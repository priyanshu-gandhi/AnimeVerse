package com.example.animeverse.domain

data class AnimeDetails(
    val id: Int,
    val title: String,
    val plot: String,
    val episodes: Int,
    val rating: Double,
    val imageUrl: String,
    val trailerEmbedUrl: String?,
    val genres: List<String>,
    val mainCast: String = "Cast information not available" // Default fallback
)