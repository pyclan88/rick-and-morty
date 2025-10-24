package ru.practicum.rickandmorty.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.rickandmorty.ui.screens.details.CharacterDetailsViewModel
import ru.practicum.rickandmorty.ui.screens.home.HomeScreenViewModel

val viewModelModule = module {
    viewModel {
        HomeScreenViewModel(
            charactersInteractor = get(),
            connectivityObserver = get()
        )
    }

    viewModel { (id: Int) ->
        CharacterDetailsViewModel(
            charactersInteractor = get(),
            characterId = id
        )
    }
}