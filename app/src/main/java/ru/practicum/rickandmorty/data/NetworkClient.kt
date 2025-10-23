package ru.practicum.rickandmorty.data

import ru.practicum.rickandmorty.data.network.ApiRequest
import ru.practicum.rickandmorty.data.network.response.Response

interface NetworkClient {
    suspend fun doRequest(dto: ApiRequest): Response
}