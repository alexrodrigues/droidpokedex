package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DroidHomeViewModel @Inject constructor(
    private val getHomeUseCase: GetPokeHomeUseCase
)  : ViewModel() {

    val testEvent = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val pokemons = getHomeUseCase.invoke(GetPokeHomeUseCase.Params(150, 0))

            testEvent.value = pokemons.toString()
        }
    }
}