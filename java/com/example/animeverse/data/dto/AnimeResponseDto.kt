package com.example.animeverse.data.dto

import com.google.gson.annotations.SerializedName

data class AnimeResponseDto(
    @SerializedName("pagination") val pagination: PaginationDto,
    @SerializedName("data") val data: List<AnimeDto>
)

data class PaginationDto(
    @SerializedName("last_visible_page") val lastVisiblePage: Int,
    @SerializedName("has_next_page") val hasNextPage: Boolean,
    @SerializedName("current_page") val currentPage: Int
)

data class AnimeDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("score") val score: Double?,
    @SerializedName("images") val images: ImageDto,
    @SerializedName("trailer") val trailer: TrailerDto,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("genres") val genres: List<GenreDto>
)

data class ImageDto(
    @SerializedName("jpg") val jpg: ImageUrlDto
)

data class ImageUrlDto(
    @SerializedName("large_image_url") val imageUrl: String
)

data class GenreDto(
    @SerializedName("name") val name: String
)