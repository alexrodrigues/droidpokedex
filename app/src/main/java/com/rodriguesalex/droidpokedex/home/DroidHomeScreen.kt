package com.rodriguesalex.droidpokedex.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import com.rodriguesalex.droidpokedex.designsystem.components.DroidPokeHeader
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.spacing
import com.rodriguesalex.droidpokedex.home.components.DroidHomeCell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DroidHomeScreen(
    viewModel: DroidHomeViewModel = hiltViewModel()
) {

    val homePageLiveData by viewModel.homePageLiveData.observeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.defaultRed)
                .padding(innerPadding)
        ) {

            DroidPokeHeader(
                modifier = Modifier.padding(spacing.medium.dp)
            )

            homePageLiveData?.let {
                LazyColumn(
                ) {
                    it.forEach { pokemon ->
                        item {
                            DroidHomeCell(
                                pokemonName = pokemon.name,
                                pokemonNumber = pokemon.id,
                                pokemonImageUrl = pokemon.pokemonImageUrl,
                                types = pokemon.types.map {
                                    it.type.name
                                },
                                backgroundColor = pokemon.backgroundColor
                            )
                        }
                    }
                }
            }
        }
    }
}