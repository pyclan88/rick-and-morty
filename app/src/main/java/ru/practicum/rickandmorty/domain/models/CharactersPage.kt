package ru.practicum.rickandmorty.domain.models

data class CharactersPage(
    val info: Info,
    val results: List<Character>
)
