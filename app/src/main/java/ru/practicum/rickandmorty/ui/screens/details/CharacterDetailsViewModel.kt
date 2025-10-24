package ru.practicum.rickandmorty.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.rickandmorty.domain.usecase.GetCharacterByIdUseCase

class CharacterDetailsViewModel(
    private val getCharacterDetailsUseCase: GetCharacterByIdUseCase,
    private val characterId: Int
) : ViewModel() {

    private val _state = MutableStateFlow<CharacterDetailsState>(CharacterDetailsState.Loading)
    val state: StateFlow<CharacterDetailsState> = _state

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            try {
                val character = getCharacterDetailsUseCase(characterId).first()
                _state.value = CharacterDetailsState.Content(character)
            } catch (e: Exception) {
                _state.value = CharacterDetailsState.Error("Could not load character from cache.")
            }
        }
    }
}