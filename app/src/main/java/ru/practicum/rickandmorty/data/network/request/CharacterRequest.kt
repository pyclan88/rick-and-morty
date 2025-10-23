package ru.practicum.rickandmorty.data.network.request

import ru.practicum.rickandmorty.data.network.ApiRequest
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.data.network.response.CharacterByIdResponse
import ru.practicum.rickandmorty.data.network.response.Response

class CharacterRequest(private val id: Int) : ApiRequest {
    override suspend fun execute(apiService: ApiService): Response {
        val character = apiService.getCharacterById(id)
        return CharacterByIdResponse(character)
    }
}