package ru.practicum.rickandmorty.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.rickandmorty.data.NetworkClient
import ru.practicum.rickandmorty.data.local.AppDatabase
import ru.practicum.rickandmorty.data.network.ApiService
import ru.practicum.rickandmorty.data.network.NetworkConnectivityObserver
import ru.practicum.rickandmorty.data.network.RetrofitNetworkClient
import ru.practicum.rickandmorty.data.repository.CharactersRepositoryImpl
import ru.practicum.rickandmorty.domain.api.CharactersRepository
import ru.practicum.rickandmorty.utils.ConnectivityObserver

private const val BASE_URL = "https://rickandmortyapi.com/api/"

val dataModule = module {
    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = get(),
            apiService = get()
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "rick_and_morty_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single<ConnectivityObserver> {
        NetworkConnectivityObserver(context = androidContext())
    }

    single<CharactersRepository> {
        CharactersRepositoryImpl(
            database = get(),
            apiService = get(),
        )
    }
}
