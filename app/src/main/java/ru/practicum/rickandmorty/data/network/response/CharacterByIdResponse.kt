package ru.practicum.rickandmorty.data.network.response

import ru.practicum.rickandmorty.data.dto.CharacterDto

data class CharacterByIdResponse(val characterDto: CharacterDto) : Response()