package com.rodriguesalex.droidpokedex.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesalex.details.domain.usecase.GetPokeDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DroidDetailsViewModel
    @Inject
    constructor(
        private val getPokeDetailsUseCase: GetPokeDetailsUseCase,
    ) : ViewModel() {
        private val _detailsStateFlow = MutableStateFlow<DroidDetailsUiState>(DroidDetailsUiState.Loading)
        val detailsStateFlow: StateFlow<DroidDetailsUiState> = _detailsStateFlow

        fun loadPokemonDetails(pokemonId: String) {
            val id = pokemonId.toIntOrNull()
            if (id == null) {
                _detailsStateFlow.value = DroidDetailsUiState.Error
                return
            }

            _detailsStateFlow.value = DroidDetailsUiState.Loading

            viewModelScope.launch {
                runCatching {
                    getPokeDetailsUseCase.invoke(
                        GetPokeDetailsUseCase.Params(id = id),
                    )
                }.onSuccess { pokemonDetails ->
                    _detailsStateFlow.value = DroidDetailsUiState.Success(pokemonDetails)
                }.onFailure { throwable ->
                    _detailsStateFlow.value = DroidDetailsUiState.Error
                }
            }
        }

        fun onRetry(pokemonId: String) {
            loadPokemonDetails(pokemonId)
        }
    }
