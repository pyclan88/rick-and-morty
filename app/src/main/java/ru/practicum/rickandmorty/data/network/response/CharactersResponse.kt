package ru.practicum.rickandmorty.data.network.response

import com.google.gson.annotations.SerializedName
import ru.practicum.rickandmorty.data.dto.CharacterDto
import ru.practicum.rickandmorty.data.dto.InfoDto

data class CharactersResponse(
    @SerializedName("info") val info: InfoDto,
    @SerializedName("results") val results: List<CharacterDto>
) : Response()
