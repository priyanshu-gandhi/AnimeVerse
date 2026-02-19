package com.example.animeverse.domain.repository

import com.example.animeverse.domain.AnimeDetails
import kotlinx.coroutines.flow.Flow

interface DetailsRepository  {

    suspend fun getAnimeDetails(animeId: Int): Flow<AnimeDetails?>

    suspend fun syncDetails(animeId : Int)
}