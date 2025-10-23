package ru.practicum.rickandmorty.domain.models

data class QueryParams(
    val query: String,
    val filters: Filters
)
