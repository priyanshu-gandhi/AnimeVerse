package com.example.animeverse.data.remote

import com.example.animeverse.data.dto.AnimeDetailResponseDto
import com.example.animeverse.data.dto.AnimeResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeApiService {

    @GET("v4/top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1
    ) : AnimeResponseDto

    @GET("v4/anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") animeId: Int
    ): AnimeDetailResponseDto

    @GET("v4/anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("sfw") sfw: Boolean = true // Filters out adult content for better UX
    ): AnimeResponseDto
}