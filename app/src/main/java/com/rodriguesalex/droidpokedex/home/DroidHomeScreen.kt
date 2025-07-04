@file:Suppress("FunctionNaming")

package com.rodriguesalex.droidpokedex.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodriguesalex.droidpokedex.R
import com.rodriguesalex.droidpokedex.designsystem.components.DroidErrorComponent
import com.rodriguesalex.droidpokedex.designsystem.components.DroidPokeHeader
import com.rodriguesalex.droidpokedex.designsystem.components.DroidPaginationLoadingIndicator
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing
import com.rodriguesalex.droidpokedex.designsystem.components.DroidHomeCell
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeViewModel
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeUiState

@Composable
@Suppress("LongMethod", "MagicNumber")
internal fun DroidHomeScreen(viewModel: DroidHomeViewModel = hiltViewModel()) {
    val homePageState by viewModel.homeStateFlow.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Colors.defaultRed)
                    .padding(innerPadding),
        ) {
            DroidPokeHeader(
                modifier = Modifier.padding(Spacing.MEDIUM.dp),
            )

            PokeSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged,
            )

            when (homePageState) {
                is DroidHomeUiState.Error -> {
                    DroidErrorComponent(
                        message = stringResource(id = R.string.error_message),
                        onRetryClick = {
                            viewModel.onRetry()
                        }
                    )
                }

                is DroidHomeUiState.Loading -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                    ) {
                        DroidPaginationLoadingIndicator(
                            modifier = Modifier.align(Center),
                        )
                    }
                }

                is DroidHomeUiState.Success -> {
                    val pokemons = (homePageState as DroidHomeUiState.Success).pokemons

                    LazyColumn {
                        items(pokemons.size) { index ->
                            val pokemon = pokemons[index]
                            DroidHomeCell(
                                pokemonName = pokemon.name,
                                pokemonNumber = pokemon.id,
                                pokemonImageUrl = pokemon.pokemonImageUrl,
                                types = pokemon.types.map { it.type.name },
                                backgroundColor = pokemon.backgroundColor,
                                pokeballImageRes = R.drawable.pokeball,
                            )

                            val isSearching = (homePageState as DroidHomeUiState.Success).isSearching

                            if (index >= pokemons.size - 3 && !isLoading && !isSearching) {
                                viewModel.loadMorePokemons()
                            }
                        }

                        if (isLoading) {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = Spacing.MEDIUM.dp),
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.align(Center),
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
@Suppress("LongMethod")
fun PokeSearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
) {
    TextField(
        value = searchQuery,
        onValueChange = { newText ->
            onSearchQueryChanged(newText)
        },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(Spacing.MEDIUM.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(Spacing.MEDIUM.dp),
                )
                .shadow(4.dp, shape = RoundedCornerShape(Spacing.MEDIUM.dp)),
        placeholder = { SearchPlaceholderText() },
        singleLine = true,
        leadingIcon = { SearchIcon() },
        colors = searchTextFieldColors(),
        textStyle = TextStyle(fontSize = Spacing.MEDIUM.sp),
    )
}

@Composable
private fun SearchPlaceholderText() {
    Text(
        text = "Search Pokémon...",
        color = Color.Gray,
    )
}

@Composable
private fun SearchIcon() {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search Icon",
        tint = Color.Gray,
    )
}

@Composable
private fun searchTextFieldColors() =
    TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        cursorColor = Color.Red,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White,
    )

