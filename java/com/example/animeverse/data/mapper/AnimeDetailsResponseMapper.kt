package com.example.animeverse.data.mapper

import com.example.animeverse.data.dto.AnimeDetailDto
import com.example.animeverse.data.dto.AnimeDetailResponseDto
import com.example.animeverse.domain.AnimeDetails

fun AnimeDetailDto.toDomain(): AnimeDetails {
    return AnimeDetails(
        id = this.malId,
        title = this.title,
        plot = this.synopsis ?: "No synopsis available.",
        episodes = this.episodes ?: 0,
        rating = this.score ?: 0.0,
        imageUrl = this.images.jpg.imageUrl,
        trailerEmbedUrl = this.trailer.embedUrl,
        genres = this.genres.map { it.name },
        mainCast = "Spike Spiegel, Jet Black, Faye Valentine" // Mocked for this api is not returning this
    )
}