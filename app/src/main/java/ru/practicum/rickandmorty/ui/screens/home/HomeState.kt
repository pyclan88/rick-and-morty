package ru.practicum.rickandmorty.ui.screens.home

import ru.practicum.rickandmorty.domain.models.CharactersPage

sealed interface HomeState {
    object Loading : HomeState
    data class Content(val data: CharactersPage) : HomeState
    data class Error(val message: String) : HomeState
}