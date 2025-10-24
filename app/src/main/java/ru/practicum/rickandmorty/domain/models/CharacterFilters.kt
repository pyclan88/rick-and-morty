package ru.practicum.rickandmorty.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

    @Parcelize
    data class CharacterFilters(
        val status: String? = null,
        val gender: String? = null,
        val species: String? = null,
        val type: String? = null,
        val isFavoritesOnly: Boolean = false,
    ) : Parcelable {

        fun areActive(): Boolean {
            return status != null ||
                    gender != null ||
                    species != null ||
                    type != null ||
                    isFavoritesOnly
        }
    }