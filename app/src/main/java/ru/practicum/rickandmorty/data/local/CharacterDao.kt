package ru.practicum.rickandmorty.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query(
        """
        SELECT * FROM characters 
        WHERE 
            (:name IS NULL OR name LIKE '%' || :name || '%') AND 
            (:status IS NULL OR status = :status) AND 
            (:gender IS NULL OR gender = :gender) AND
            (:species IS NULL OR species = :species) AND
            (:type IS NULL OR type = :type) AND
            (:isFavoritesOnly = 0 OR isFavorite = 1)
        ORDER BY id ASC
        """
    )
    fun pagingSource(
        name: String?,
        status: String?,
        gender: String?,
        species: String?,
        type: String?,
        isFavoritesOnly: Boolean
    ): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterEntity>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
}