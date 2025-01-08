package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DroidHomeViewModel @Inject constructor(
    private val getHomeUseCase: GetPokeHomeUseCase
)  : ViewModel() {

    // TODO: use stateflow instead
    val homePageLiveData = MutableLiveData<List<PokemonListItem>>()

    init {
        viewModelScope.launch {
            val pokemons = getHomeUseCase.invoke(GetPokeHomeUseCase.Params(150, 0))

            homePageLiveData.value = pokemons.results
        }
    }
}