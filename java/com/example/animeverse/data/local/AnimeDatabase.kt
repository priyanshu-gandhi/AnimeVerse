package com.example.animeverse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.animeverse.data.local.converters.AnimeConverters
import com.example.animeverse.data.local.dao.AnimeDao
import com.example.animeverse.data.local.entities.AnimeEntity

@Database(entities = [AnimeEntity::class], version = 1, exportSchema = false)
@TypeConverters(AnimeConverters::class)
abstract class AnimeDatabase : RoomDatabase(){
    abstract fun animeDao(): AnimeDao
}