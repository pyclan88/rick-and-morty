package ru.practicum.rickandmorty.domain.api

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.CharacterFilters

interface CharactersInteractor {
    fun getCharactersStream(query: String?, filters: CharacterFilters): Flow<PagingData<Character>>
    fun getCharacterById(id: Int): Flow<Character>
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
}
