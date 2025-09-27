package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import com.rodriguesalex.domain.usecase.SearchPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DroidHomeViewModel
    @Inject
    constructor(
        private val getHomeUseCase: GetPokeHomeUseCase,
        private val searchPokemonUseCase: SearchPokemonUseCase,
    ) : ViewModel() {
        private val _homeStateFlow = MutableStateFlow<DroidHomeUiState>(DroidHomeUiState.Loading)
        val homeStateFlow = _homeStateFlow

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery

        private var currentPage = 0

        private var pokemonList = mutableListOf<PokemonListItem>()

        init {
            loadMorePokemons()
        }

        fun loadMorePokemons() {
            if (_isLoading.value) return

            _isLoading.value = true
            viewModelScope.launch {
                runCatching {
                    getHomeUseCase.invoke(
                        GetPokeHomeUseCase.Params(limit = PAGE_SIZE, offset = currentPage * PAGE_SIZE),
                    ).results
                }.onSuccess { newPokemons ->
                    pokemonList.addAll(newPokemons)
                    _homeStateFlow.updateStateWith(newPokemons)
                    currentPage++
                }.onFailure { throwable ->
                    _homeStateFlow.value = DroidHomeUiState.Error
                }
                _isLoading.value = false
            }
        }

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
            if (query.isBlank()) {
                currentPage = 0
                _homeStateFlow.value = DroidHomeUiState.Loading
                loadMorePokemons()
            } else {
                _homeStateFlow.value = DroidHomeUiState.Loading
                filterPokemons(query)
            }
        }

        private fun filterPokemons(query: String) {
            viewModelScope.launch {
                runCatching {
                    searchPokemonUseCase.invoke(SearchPokemonUseCase.Params(query))
                }.onSuccess { pokemon ->
                    _homeStateFlow.value =
                        DroidHomeUiState.Success(
                            pokemons = listOf(pokemon),
                            isSearching = true,
                        )
                }.onFailure { throwable ->
                    _homeStateFlow.value = DroidHomeUiState.Error
                }
            }
        }

        fun onRetry() {
            _homeStateFlow.value = DroidHomeUiState.Loading
            _searchQuery.value = ""
            currentPage = 0
            loadMorePokemons()
        }

        companion object {
            private const val PAGE_SIZE = 20
        }
    }

private fun MutableStateFlow<DroidHomeUiState>.updateStateWith(newPokemons: List<PokemonListItem>) {
    value =
        when (val currentState = value) {
            is DroidHomeUiState.Success -> {
                DroidHomeUiState.Success(
                    pokemons = currentState.pokemons + newPokemons,
                    isSearching = false,
                )
            }
            else -> DroidHomeUiState.Success(newPokemons)
        }
}
