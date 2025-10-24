package ru.practicum.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val characterId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val lastUpdated: Long?,
)