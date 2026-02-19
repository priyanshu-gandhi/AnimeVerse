package com.example.animeverse.data.repoImpl

import com.example.animeverse.data.local.dao.AnimeDao
import com.example.animeverse.data.mapper.toDomain
import com.example.animeverse.data.mapper.toEntity
import com.example.animeverse.data.mapper.toUIModel
import com.example.animeverse.data.remote.AnimeApiService
import com.example.animeverse.domain.Anime
import com.example.animeverse.domain.AnimePage
import com.example.animeverse.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class HomeRepositoryImpl @Inject constructor(
    private val apiService: AnimeApiService,
    private val animeDao : AnimeDao
) : HomeRepository {

    override fun  getTopAnime(page: Int, pageSize: Int): Flow<List<Anime>> {
        val offset = (page - 1) * pageSize
        return animeDao.getTopAnime(pageSize, offset).map { entities ->
            entities.map { it.toUIModel() }
        }
    }

    override suspend fun syncTopAnime(page: Int) {
        val response = apiService.getTopAnime(page)
        val entities = response.data.map { it.toEntity() }
        animeDao.UpsertAnime(entities)
    }

    override fun searchAnime(query: String): Flow<List<Anime>> {
        return animeDao.searchAnime(query).map { entities ->
            entities.map { it.toUIModel() }
        }
    }

    override suspend fun syncSearchAnime(query: String) {
        // API: https://api.jikan.moe/v4/anime?q={query}
        val response = apiService.searchAnime(query)
        val entities = response.data.map { it.toEntity() }
        animeDao.UpsertAnime(entities)
    }
}
