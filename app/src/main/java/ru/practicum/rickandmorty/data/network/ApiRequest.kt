package ru.practicum.rickandmorty.data.network

import ru.practicum.rickandmorty.data.network.response.Response

interface ApiRequest {
    suspend fun execute(apiService: ApiService): Response
}