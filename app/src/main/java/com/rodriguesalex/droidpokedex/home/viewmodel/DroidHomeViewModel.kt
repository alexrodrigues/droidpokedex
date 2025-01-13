package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DroidHomeViewModel @Inject constructor(
    private val getHomeUseCase: GetPokeHomeUseCase
)  : ViewModel() {

    private val _homeStateFlow = MutableStateFlow<DroidHomeViewState>(DroidHomeViewState.Loading)
    val homeStateFlow = _homeStateFlow

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 0
    private val pageSize = 20

    init {
        loadMorePokemons()
    }

    fun loadMorePokemons() {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            runCatching {
                getHomeUseCase.invoke(
                    GetPokeHomeUseCase.Params(limit = pageSize, offset = currentPage * pageSize)
                ).results
            }.onSuccess { newPokemons ->
                _homeStateFlow.updateStateWith(newPokemons)
                currentPage++
            }.onFailure { throwable ->
                _homeStateFlow.value = DroidHomeViewState.Error
            }
            _isLoading.value = false
        }
    }
}

private fun MutableStateFlow<DroidHomeViewState>.updateStateWith(newPokemons: List<PokemonListItem>) {
    value = when (val currentState = value) {
        is DroidHomeViewState.Success -> {
            DroidHomeViewState.Success(currentState.pokemons + newPokemons)
        }
        else -> DroidHomeViewState.Success(newPokemons)
    }
}