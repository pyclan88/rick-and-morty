package ru.practicum.rickandmorty.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.rickandmorty.domain.api.CharactersRepository
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.QueryParams

class GetCharactersStreamUseCase(
    private val repository: CharactersRepository
) {
    operator fun invoke(params: QueryParams): Flow<PagingData<Character>> {
        return repository.getCharactersStream(params)
    }
}
