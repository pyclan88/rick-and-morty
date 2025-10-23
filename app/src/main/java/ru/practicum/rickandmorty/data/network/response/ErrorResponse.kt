package ru.practicum.rickandmorty.data.network.response

data class ErrorResponse(
    override val resultCode: Int,
    val errorMessage: String? = null
) : Response()
