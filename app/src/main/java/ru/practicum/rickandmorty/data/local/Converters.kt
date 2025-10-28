package ru.practicum.rickandmorty.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromEpisodeList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toEpisodeList(string: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, listType)
    }
}