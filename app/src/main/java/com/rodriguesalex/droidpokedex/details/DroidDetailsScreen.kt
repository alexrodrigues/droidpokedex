package com.rodriguesalex.droidpokedex.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsUiState
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsViewModel
import com.rodriguesalex.domain.model.DroidPokemonTypeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroidDetailsScreen(
    pokemonId: String,
    onBackClick: () -> Unit,
    viewModel: DroidDetailsViewModel = hiltViewModel()
) {
    val detailsState by viewModel.detailsStateFlow.collectAsState()

    val pokemonTypeColor: Color = when (val currentState = detailsState) {
        is DroidDetailsUiState.Success -> {
            DroidPokemonTypeColor.getPokemonColor(
                currentState.pokemonDetails.primaryType ?: "normal"
            ).primary
        }
        else -> DroidPokemonTypeColor.getPokemonColor("normal").primary
    }

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetails(pokemonId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokemon #$pokemonId") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = pokemonTypeColor
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(pokemonTypeColor)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = detailsState) {
                is DroidDetailsUiState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading Pokemon details...",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
                is DroidDetailsUiState.Success -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentState.pokemonDetails.name.replaceFirstChar { char -> char.uppercase() },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Pokemon #${currentState.pokemonDetails.id}",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Height: ${currentState.pokemonDetails.heightMeters}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Weight: ${currentState.pokemonDetails.weightKg}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Base Experience: ${currentState.pokemonDetails.baseExperience}",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
                is DroidDetailsUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading Pokemon details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.onRetry(pokemonId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Retry",
                                color = pokemonTypeColor
                            )
                        }
                    }
                }
            }
        }
    }
}