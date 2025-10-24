package ru.practicum.rickandmorty.di

import org.koin.dsl.module
import ru.practicum.rickandmorty.domain.api.CharactersInteractor
import ru.practicum.rickandmorty.domain.impl.CharactersInteractorImpl

val interactorModule = module {
    single<CharactersInteractor> {
        CharactersInteractorImpl(repository = get())
    }
}
