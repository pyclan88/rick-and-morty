package ru.practicum.rickandmorty.domain.api

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.QueryParams

interface CharactersRepository {
    fun getCharactersStream(params: QueryParams): Flow<PagingData<Character>>
    fun getCharacterById(id: Int): Flow<Character>
}