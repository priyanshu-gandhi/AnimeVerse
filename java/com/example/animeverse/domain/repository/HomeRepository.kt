package com.example.animeverse.domain.repository

import com.example.animeverse.domain.Anime
import com.example.animeverse.domain.AnimePage
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

     fun getTopAnime(page: Int, pageSize: Int): Flow<List<Anime>>

    suspend fun syncTopAnime(page: Int)

    fun searchAnime(query: String): Flow<List<Anime>>

    suspend fun syncSearchAnime(query: String)
}





