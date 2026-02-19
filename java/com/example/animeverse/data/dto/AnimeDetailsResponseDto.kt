package com.example.animeverse.data.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data") val data: AnimeDetailDto
)

data class AnimeDetailDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("score") val score: Double?,
    @SerializedName("images") val images: ImageDto,
    @SerializedName("trailer") val trailer: TrailerDto,
    @SerializedName("genres") val genres: List<GenreDto>
)

data class TrailerDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("embed_url") val embedUrl: String?
)