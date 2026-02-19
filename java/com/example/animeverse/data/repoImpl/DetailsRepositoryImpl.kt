package com.example.animeverse.data.repoImpl

import android.util.Log
import com.example.animeverse.data.local.dao.AnimeDao
import com.example.animeverse.data.mapper.toDomain
import com.example.animeverse.data.mapper.toEntity
import com.example.animeverse.data.remote.AnimeApiService
import com.example.animeverse.domain.AnimeDetails
import com.example.animeverse.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DetailsRepositoryImpl @Inject constructor(
    private val apiService: AnimeApiService,
    private val animeDao: AnimeDao
) : DetailsRepository {
    override suspend fun getAnimeDetails(animeId: Int): Flow<AnimeDetails?> {
        return animeDao.getAnimeById(animeId).map{ entity ->
            if(entity == null){
                syncDetails(animeId)
            }
            entity?.toDomain()}
    }

    override suspend fun syncDetails(animeId: Int) {
       try{
           val response = apiService.getAnimeDetails(animeId)
           val entity = response.data.toEntity()
           animeDao.UpsertAnime(listOf(entity))
       }
       catch (e: Exception){
           Log.d("DetailsRepositoryImpl", "syncDetails: ${e.message}")
       }
    }

}