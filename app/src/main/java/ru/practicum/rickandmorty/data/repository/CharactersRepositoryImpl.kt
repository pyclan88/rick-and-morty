package ru.practicum.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.rickandmorty.data.local.AppDatabase
import ru.practicum.rickandmorty.data.mapper.toDomain
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.data.paging.CharacterRemoteMediator
import ru.practicum.rickandmorty.domain.api.CharactersRepository
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.CharacterFilters

const val PAGE_SIZE = 20

class CharactersRepositoryImpl(
    private val database: AppDatabase,
    private val apiService: ApiService,
) : CharactersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharactersStream(query: String?, filters: CharacterFilters): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 5,
            ),
            remoteMediator = CharacterRemoteMediator(
                apiService = apiService,
                database = database,
                query = query,
                filters = filters
            ),
            pagingSourceFactory = {
                database.characterDao().pagingSource(
                    name = query,
                    status = filters.status,
                    gender = filters.gender,
                    species = filters.species,
                    type = filters.type,
                    isFavoritesOnly = filters.isFavoritesOnly
                )
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

    override suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            database.characterDao().updateFavoriteStatus(id, isFavorite)
        }
    }
}
