package com.rodriguesalex.droidpokedex.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsUiState
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsViewModel
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.droidpokedex.designsystem.components.DroidDetailsHeader
import com.rodriguesalex.droidpokedex.designsystem.components.DroidDetailsHeaderVo

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
                title = { Text("") },
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
                is DroidDetailsUiState.Loading -> PokemonDetailsLoading()

                is DroidDetailsUiState.Success -> PokemonDetailsSuccess(
                    pokemonDetails = currentState.pokemonDetails,
                    pokemonTypeColor = pokemonTypeColor
                )

                is DroidDetailsUiState.Error -> PokemonDetailsError(
                    pokemonTypeColor = pokemonTypeColor
                ) {
                    viewModel.onRetry(pokemonId)
                }
            }
        }
    }
}

@Composable
fun PokemonDetailsSuccess(
    pokemonDetails: PokemonDetails,
    pokemonTypeColor: Color,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize(),
    ) {
        DroidDetailsHeader(
            vo = DroidDetailsHeaderVo(
                pokemonName = pokemonDetails.name.replaceFirstChar { char -> char.uppercase() },
                pokemonNumber = pokemonDetails.id,
                backgroundColor = pokemonTypeColor,
                pokemonUrl = pokemonDetails.pokemonImageUrl
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        PokemonDetailSheet(
            pokemonDetails = pokemonDetails,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
fun PokemonDetailSheet(pokemonDetails: PokemonDetails, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Pokemon #${pokemonDetails.id}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))
            Text("Height: ${pokemonDetails.heightMeters}", color = Color.Black)
            Text("Weight: ${pokemonDetails.weightKg}", color = Color.Black)
            Text("Base Experience: ${pokemonDetails.baseExperience}", color = Color.Black)
        }
    }
}

@Composable
fun PokemonDetailsLoading() {
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

@Composable
fun PokemonDetailsError(
    pokemonTypeColor: Color,
    retryCallback: () -> Unit
) {
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
            onClick = {
                retryCallback()
            },
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