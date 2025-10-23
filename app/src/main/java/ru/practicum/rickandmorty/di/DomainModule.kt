package ru.practicum.rickandmorty.di

import org.koin.dsl.module
import ru.practicum.rickandmorty.domain.usecase.GetCharacterByIdUseCase
import ru.practicum.rickandmorty.domain.usecase.GetCharactersStreamUseCase

val domainModule = module {
    factory {
        GetCharactersStreamUseCase(repository = get())
    }

    factory {
        GetCharacterByIdUseCase(repository = get())
    }
}
