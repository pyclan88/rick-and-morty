package ru.practicum.rickandmorty.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")

    object Details : Screen("details/{${CHARACTER_ID}}") {
        fun createRoute(characterId: Int) = "details/$characterId"
    }

    object Filters : Screen("filters")
}