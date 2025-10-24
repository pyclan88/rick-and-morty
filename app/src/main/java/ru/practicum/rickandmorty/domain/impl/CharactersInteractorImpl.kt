package ru.practicum.rickandmorty.domain.impl

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.rickandmorty.domain.api.CharactersInteractor
import ru.practicum.rickandmorty.domain.api.CharactersRepository
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.CharacterFilters

class CharactersInteractorImpl(
    private val repository: CharactersRepository
) : CharactersInteractor {

   override fun getCharactersStream(query: String?, filters: CharacterFilters): Flow<PagingData<Character>> {
        return repository.getCharactersStream(query, filters)
    }

    override fun getCharacterById(id: Int): Flow<Character> {
        return repository.getCharacterById(id)
    }

    override suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        repository.updateFavoriteStatus(id, isFavorite)
    }
}