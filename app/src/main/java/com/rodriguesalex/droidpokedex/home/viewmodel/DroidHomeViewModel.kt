package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.model.RemoteDataSource
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import com.rodriguesalex.domain.usecase.SearchPokemonUseCase
import com.rodriguesalex.droidpokedex.util.UserErrorMapper
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

        /** True once any page in this session was served from in-memory cache (offline / API failure). */
        private var usedCacheThisSession = false

        init {
            loadMorePokemons()
        }

        fun loadMorePokemons() {
            if (_isLoading.value) return

            _isLoading.value = true
            viewModelScope.launch {
                runCatching {
                    val outcome =
                        getHomeUseCase.invoke(
                            GetPokeHomeUseCase.Params(limit = PAGE_SIZE, offset = currentPage * PAGE_SIZE),
                        )
                    if (outcome.source == RemoteDataSource.CACHE) {
                        usedCacheThisSession = true
                    }
                    outcome.value.results
                }.onSuccess { newPokemons ->
                    pokemonList.addAll(newPokemons)
                    _homeStateFlow.updateStateWith(newPokemons, usedCacheThisSession)
                    currentPage++
                }.onFailure { throwable ->
                    _homeStateFlow.value =
                        DroidHomeUiState.Error(UserErrorMapper.from(throwable))
                }
                _isLoading.value = false
            }
        }

        fun onSearchQueryChanged(query: String) {
            _searchQuery.value = query
            if (query.isBlank()) {
                currentPage = 0
                pokemonList.clear()
                usedCacheThisSession = false
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
                }.onSuccess { outcome ->
                    val fromCache = outcome.source == RemoteDataSource.CACHE
                    _homeStateFlow.value =
                        DroidHomeUiState.Success(
                            pokemons = listOf(outcome.value),
                            isSearching = true,
                            isOfflineData = fromCache,
                        )
                }.onFailure { throwable ->
                    _homeStateFlow.value =
                        DroidHomeUiState.Error(UserErrorMapper.from(throwable))
                }
            }
        }

        fun onRetry() {
            usedCacheThisSession = false
            pokemonList.clear()
            _homeStateFlow.value = DroidHomeUiState.Loading
            _searchQuery.value = ""
            currentPage = 0
            loadMorePokemons()
        }

        companion object {
            private const val PAGE_SIZE = 20
        }
    }

private fun MutableStateFlow<DroidHomeUiState>.updateStateWith(
    newPokemons: List<PokemonListItem>,
    isOfflineData: Boolean,
) {
    value =
        when (val currentState = value) {
            is DroidHomeUiState.Success -> {
                DroidHomeUiState.Success(
                    pokemons = currentState.pokemons + newPokemons,
                    isSearching = false,
                    isOfflineData = isOfflineData || currentState.isOfflineData,
                )
            }
            else ->
                DroidHomeUiState.Success(
                    pokemons = newPokemons,
                    isOfflineData = isOfflineData,
                )
        }
}
