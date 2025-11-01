package ru.practicum.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: RemoteKey)

    @Query("SELECT lastUpdated FROM remote_keys ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastUpdated(): Long?

    @Query("SELECT * FROM remote_keys WHERE characterId = :id")
    suspend fun remoteKeyByCharacterId(id: Int): RemoteKey?

    @Query("SELECT * FROM remote_keys ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLastRemoteKey(): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clearAllRemoteKeys()
}
