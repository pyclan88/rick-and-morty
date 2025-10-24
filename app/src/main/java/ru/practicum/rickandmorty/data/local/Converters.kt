package ru.practicum.rickandmorty.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.rickandmorty.domain.models.Location
import ru.practicum.rickandmorty.domain.models.Origin

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromOrigin(origin: Origin): String {
        return gson.toJson(origin)
    }

    @TypeConverter
    fun toOrigin(originString: String): Origin {
        return gson.fromJson(originString, Origin::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun toLocation(locationString: String): Location {
        return gson.fromJson(locationString, Location::class.java)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(string: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, listType)
    }
}