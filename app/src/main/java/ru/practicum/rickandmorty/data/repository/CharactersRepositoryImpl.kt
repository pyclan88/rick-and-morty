package ru.practicum.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.rickandmorty.data.local.AppDatabase
import ru.practicum.rickandmorty.data.mappers.toDomain
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.data.paging.CharacterRemoteMediator
import ru.practicum.rickandmorty.domain.api.CharactersRepository
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.QueryParams

class CharactersRepositoryImpl(
    private val database: AppDatabase,
    private val apiService: ApiService,
) : CharactersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharactersStream(params: QueryParams): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(
                apiService = apiService,
                database = database,
                params = params
            ),
            pagingSourceFactory = {
                database.characterDao().pagingSource(params.query)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomain()
            }
        }
    }

    override fun getCharacterById(id: Int): Flow<Character> {
        return database.characterDao().getCharacterById(id)
            .map { entity ->
                entity.toDomain()
            }
    }
}
