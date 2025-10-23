package ru.practicum.rickandmorty.data.dto

import com.google.gson.annotations.SerializedName

data class OriginDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
