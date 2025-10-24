package ru.practicum.rickandmorty

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.rickandmorty.di.dataModule
import ru.practicum.rickandmorty.di.interactorModule
import ru.practicum.rickandmorty.di.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, viewModelModule)
        }
    }
}
