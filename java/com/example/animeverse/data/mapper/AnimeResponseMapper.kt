package com.example.animeverse.data.mapper

import com.example.animeverse.data.dto.AnimeResponseDto
import com.example.animeverse.domain.Anime
import com.example.animeverse.domain.AnimePage

fun AnimeResponseDto.toDomain() : AnimePage {
    return AnimePage(
        animeList = this.data.map { dto ->
            Anime(
                id = dto.malId,
                title = dto.title,
                episodes = dto.episodes ?: 0,
                rating = dto.score ?: 0.0,
                imageUrl = dto.images.jpg.imageUrl,
                trailerUrl = dto.trailer.embedUrl,
                synopsis = dto.synopsis ?: "No description available.",
                genres = dto.genres.map { it.name }
            )
        },
        currentPage = this.pagination.currentPage,
        hasNextPage = this.pagination.hasNextPage
    )
}