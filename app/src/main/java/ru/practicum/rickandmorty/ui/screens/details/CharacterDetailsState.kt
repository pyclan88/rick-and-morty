package ru.practicum.rickandmorty.ui.screens.details

import ru.practicum.rickandmorty.domain.models.Character

sealed class CharacterDetailsState {
    object Loading : CharacterDetailsState()
    data class Content(val character: Character) : CharacterDetailsState()
    data class Error(val message: String) : CharacterDetailsState()
}