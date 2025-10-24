package ru.practicum.rickandmorty.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filters(
    val name: String? = null,
    val status: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null
) : Parcelable {

    fun areActive() = status != null || gender != null
}