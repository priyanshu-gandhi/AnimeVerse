package com.example.animeverse.data.local.converters

import androidx.room.TypeConverter

class AnimeConverters {

    @TypeConverter
    fun fromString(value :String) : List<String>{
        return value.split(",").map{ it.trim()}
    }

    @TypeConverter
    fun fromList(value : List<String>) : String{
        return value.joinToString(",")
    }
}