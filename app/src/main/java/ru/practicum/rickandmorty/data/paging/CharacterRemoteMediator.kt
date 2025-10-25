package ru.practicum.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.practicum.rickandmorty.data.local.AppDatabase
import ru.practicum.rickandmorty.data.local.CharacterEntity
import ru.practicum.rickandmorty.data.local.RemoteKey
import ru.practicum.rickandmorty.data.mapper.toEntity
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.domain.models.CharacterFilters
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val query: String?,
    private val filters: CharacterFilters,
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        if (filters.isFavoritesOnly) {
            return InitializeAction.SKIP_INITIAL_REFRESH
        }

        val cacheTimeout = TimeUnit.HOURS.toMillis(1)
        val lastUpdated = remoteKeyDao.getLastUpdated() ?: 0L

        return if (System.currentTimeMillis() - lastUpdated < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        if (filters.isFavoritesOnly) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                remoteKey?.nextKey ?: (state.pages.count { it.data.isNotEmpty() } + 1)
            }
        }

        return try {
            val response = apiService.filterCharacters(
                page = page,
                name = query?.ifBlank { null },
                status = filters.status,
                gender = filters.gender,
                species = filters.species,
                type = filters.type
            )

            val characters = response.results
            val endOfPaginationReached = response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAllRemoteKeys()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val currentTime = System.currentTimeMillis()

                remoteKeyDao.insertAll(
                    characters.map {
                        RemoteKey(
                            characterId = it.id,
                            prevKey = prevKey,
                            nextKey = nextKey,
                            lastUpdated = currentTime
                        )
                    }
                )

                characterDao.insertAll(characters.map { it.toEntity() })
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            if (e.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                MediatorResult.Error(e)
            }
        }
    }


    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character -> remoteKeyDao.remoteKeyByCharacterId(character.id) }
    }
}