package ru.practicum.rickandmorty.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.practicum.rickandmorty.domain.api.CharactersInteractor
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.CharacterFilters
import ru.practicum.rickandmorty.utils.ConnectivityObserver

class HomeScreenViewModel(
    private val charactersInteractor: CharactersInteractor,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _filters = MutableStateFlow<CharacterFilters>(CharacterFilters())
    val filters = _filters.asStateFlow()

    val networkStatus: StateFlow<ConnectivityObserver.Status> = connectivityObserver.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ConnectivityObserver.Status.Unavailable
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val characters: Flow<PagingData<Character>> = combine(
        _searchQuery.debounce(500),
        _filters
    ) { query, filter ->
        query to filter
    }.flatMapLatest { (query, filter) ->
        charactersInteractor.getCharactersStream(query, filter)
    }.cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFiltersChanged(newFilters: CharacterFilters) {
        _filters.value = newFilters
    }

    fun onFavoriteClick(character: Character) {
        viewModelScope.launch {
            charactersInteractor.updateFavoriteStatus(character.id, !character.isFavorite)
        }
    }
}
