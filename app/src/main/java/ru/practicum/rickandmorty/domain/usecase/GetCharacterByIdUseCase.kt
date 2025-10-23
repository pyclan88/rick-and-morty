package ru.practicum.rickandmorty.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.practicum.rickandmorty.domain.api.CharactersRepository
import ru.practicum.rickandmorty.domain.models.Character

class GetCharacterByIdUseCase(
    private val repository: CharactersRepository
) {
    operator fun invoke(id: Int): Flow<Character> {
        return repository.getCharacterById(id)
    }
}