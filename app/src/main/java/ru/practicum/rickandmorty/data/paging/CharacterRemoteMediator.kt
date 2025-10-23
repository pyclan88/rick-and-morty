package ru.practicum.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import okio.IOException
import retrofit2.HttpException
import ru.practicum.rickandmorty.data.local.AppDatabase
import ru.practicum.rickandmorty.data.local.CharacterEntity
import ru.practicum.rickandmorty.data.local.RemoteKey
import ru.practicum.rickandmorty.data.mappers.toEntity
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.domain.models.QueryParams
import javax.net.ssl.HttpsURLConnection

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val params: QueryParams,
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = getLastRemoteKey(state)
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = apiService.filterCharacters(
                page = page,
                name = params.query.ifBlank { null },
                status = params.filters.status,
                gender = params.filters.gender,
            )

            val characters = response.results
            val endOfPaginationReached = response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDao.clearAllCharacters()
                    remoteKeyDao.clearAllRemoteKeys()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = characters.map {
                    RemoteKey(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                val charactersEntities = characters.map { it.toEntity() }

                remoteKeyDao.insertAll(keys)
                characterDao.insertAll(charactersEntities)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            if (e.code() == HttpsURLConnection.HTTP_NOT_FOUND) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, CharacterEntity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character -> remoteKeyDao.remoteKeyByCharacterId(character.id) }
    }
}