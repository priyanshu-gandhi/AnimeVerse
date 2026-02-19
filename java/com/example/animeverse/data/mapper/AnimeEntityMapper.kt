package com.example.animeverse.data.mapper

import com.example.animeverse.data.dto.AnimeDetailDto
import com.example.animeverse.data.dto.AnimeDto
import com.example.animeverse.data.local.entities.AnimeEntity
import com.example.animeverse.domain.Anime
import com.example.animeverse.domain.AnimeDetails


fun AnimeDto.toEntity(): AnimeEntity {
    return AnimeEntity(
        id = this.malId,
        title = this.title,
        imageUrl = this.images.jpg.imageUrl,
        rating = this.score ?: 0.0,
        episodes = this.episodes ?: 0,
        genres = this.genres.map { it.name },
        plot = this.synopsis,
        trailerUrl = this.trailer.embedUrl
    )
}

fun AnimeDetailDto.toEntity(): AnimeEntity{
    return AnimeEntity(
        id = this.malId,
        title = this.title,
        imageUrl = this.images.jpg.imageUrl,
        rating = this.score ?: 0.0,
        episodes = this.episodes ?: 0,
        genres = this.genres.map { it.name },
        plot = this.synopsis,
        trailerUrl = this.trailer.embedUrl,
        mainCast = "Spike Spiegel, Jet Black, Faye Valentine"
    )
}

fun AnimeEntity.toUIModel(): Anime {
    return Anime(
        id = this.id,
        title = this.title,
        rating = this.rating,
        episodes = this.episodes,
        imageUrl = this.imageUrl,
        synopsis = this.plot ?: "No description available.",
        trailerUrl = this.trailerUrl,
        genres = this.genres
    )
}

fun AnimeEntity.toDomain(): AnimeDetails{
    return AnimeDetails(
        id = this.id,
        title = this.title,
        plot = this.plot ?: "No description available.",
        episodes = this.episodes,
        rating = this.rating,
        imageUrl = this.imageUrl,
        trailerEmbedUrl = this.trailerUrl,
        genres = this.genres,
        mainCast = this.mainCast
    )
}