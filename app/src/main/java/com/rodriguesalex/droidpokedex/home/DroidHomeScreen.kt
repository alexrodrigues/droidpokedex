package com.rodriguesalex.droidpokedex.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodriguesalex.droidpokedex.designsystem.components.DroidPokeHeader
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.spacing
import com.rodriguesalex.droidpokedex.home.components.DroidHomeCell
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeViewModel
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeViewState

@Composable
internal fun DroidHomeScreen(
    viewModel: DroidHomeViewModel = hiltViewModel()
) {

    val homePageState by viewModel.homeStateFlow.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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

            when (homePageState) {
                is DroidHomeViewState.Error -> {
                    // TODO: Implement error state
                }

                is DroidHomeViewState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        LoadingIndicator(
                            modifier = Modifier.align(Center)
                        )
                    }
                }

                is DroidHomeViewState.Success -> {
                    val pokemons = (homePageState as DroidHomeViewState.Success).pokemons

                    LazyColumn {
                        items(pokemons.size) { index ->
                            val pokemon = pokemons[index]
                            DroidHomeCell(
                                pokemonName = pokemon.name,
                                pokemonNumber = pokemon.id,
                                pokemonImageUrl = pokemon.pokemonImageUrl,
                                types = pokemon.types.map { it.type.name },
                                backgroundColor = pokemon.backgroundColor
                            )

                            if (index >= pokemons.size - 3 && !isLoading) {
                                viewModel.loadMorePokemons()
                            }
                        }

                        if (isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = spacing.medium.dp)
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.align(Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(
    modifier: Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium.dp),
    ) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = modifier
        )
    }
}