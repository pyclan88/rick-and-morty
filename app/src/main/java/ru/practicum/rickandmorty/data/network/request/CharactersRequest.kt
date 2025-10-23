package ru.practicum.rickandmorty.data.network.request

import ru.practicum.rickandmorty.data.network.ApiRequest
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.data.network.response.Response

class CharactersRequest(private val page: Int) : ApiRequest {
    override suspend fun execute(apiService: ApiService): Response {
        return apiService.getCharacters(page)
    }
}