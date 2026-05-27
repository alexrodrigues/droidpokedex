@file:Suppress("FunctionNaming", "MagicNumber", "LongMethod")

package com.rodriguesalex.droidpokedex.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.droidpokedex.R
import com.rodriguesalex.droidpokedex.designsystem.components.BaseStatsSection
import com.rodriguesalex.droidpokedex.designsystem.components.DescriptionCard
import com.rodriguesalex.droidpokedex.designsystem.components.DetailsTopBar
import com.rodriguesalex.droidpokedex.designsystem.components.DroidErrorComponent
import com.rodriguesalex.droidpokedex.designsystem.components.EvolutionChainSection
import com.rodriguesalex.droidpokedex.designsystem.components.PokemonHeroSection
import com.rodriguesalex.droidpokedex.designsystem.components.StatInfoCard
import com.rodriguesalex.droidpokedex.designsystem.components.TypePillTag
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsUiState
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsViewModel
import java.util.Locale

@Composable
fun DroidDetailsScreen(
    pokemonId: String,
    onBackClick: () -> Unit,
    viewModel: DroidDetailsViewModel = hiltViewModel(),
) {
    val detailsState by viewModel.detailsStateFlow.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetails(pokemonId)
    }

    val topBarTitle = detailsTopBarTitle(detailsState, pokemonId)

    DroidDetailsScreenContent(
        topBarTitle = topBarTitle,
        state = detailsState,
        onBackClick = onBackClick,
        onRetry = { viewModel.onRetry(pokemonId) },
    )
}

@Composable
internal fun DroidDetailsScreenContent(
    topBarTitle: String,
    state: DroidDetailsUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold(
        containerColor = Colors.pokedexRed,
        topBar = {
            DetailsTopBar(
                title = topBarTitle,
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Colors.pokedexRed),
        ) {
            when (val currentState = state) {
                is DroidDetailsUiState.Loading -> DetailsLoadingContent()
                is DroidDetailsUiState.Success ->
                    DetailsSuccessContent(
                        details = currentState.pokemonDetails,
                        isOfflineData = currentState.isOfflineData,
                    )
                is DroidDetailsUiState.Error ->
                    DroidErrorComponent(
                        title = stringResource(id = currentState.info.titleRes),
                        message = stringResource(id = currentState.info.messageRes),
                        detail =
                            currentState.info.detailRes?.let { resId ->
                                when (val arg = currentState.info.detailArg) {
                                    is Int -> stringResource(id = resId, arg)
                                    else -> stringResource(id = resId)
                                }
                            },
                        onRetryClick = onRetry,
                    )
            }
        }
    }
}

internal fun detailsTopBarTitle(
    state: DroidDetailsUiState,
    pokemonId: String,
): String =
    when (state) {
        is DroidDetailsUiState.Success -> {
            val name =
                state.pokemonDetails.name.replaceFirstChar { char ->
                    if (char.isLowerCase()) {
                        char.titlecase(Locale.getDefault())
                    } else {
                        char.toString()
                    }
                }
            "$name Entry #${state.pokemonDetails.id.toString().padStart(3, '0')}"
        }
        else -> "Pokemon #$pokemonId"
    }

@Composable
private fun DetailsLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Colors.white)
    }
}

@Composable
private fun DetailsSuccessContent(
    details: PokemonDetails,
    isOfflineData: Boolean,
) {
    val typeColor = DroidPokemonTypeColor.getPokemonColor(details.primaryType ?: "normal")
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.LARGE.dp)
                .verticalScroll(scrollState),
    ) {
        if (isOfflineData) {
            OfflineBanner()
            Spacer(modifier = Modifier.height(Spacing.SMALL.dp))
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = Spacing.MEDIUM.dp, topEnd = Spacing.MEDIUM.dp),
            color = Colors.surfaceCream,
            shadowElevation = 4.dp,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(Spacing.LARGE.dp),
            ) {
                PokemonHeroSection(
                    pokemonId = details.id,
                    imageUrl = details.officialArtworkUrl,
                    backgroundColor = typeColor.secondary,
                )

                Spacer(modifier = Modifier.height(Spacing.LARGE.dp))

                Text(
                    text = details.name.uppercase(Locale.getDefault()),
                    modifier = Modifier.fillMaxWidth(),
                    color = Colors.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                )

                details.genus?.let { genus ->
                    Spacer(modifier = Modifier.height(Spacing.XSMALL.dp))
                    Text(
                        text = genus,
                        modifier = Modifier.fillMaxWidth(),
                        color = Colors.onSurfaceVariant,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }

                if (details.types.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Spacing.MEDIUM.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        details.types.forEachIndexed { index, type ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.padding(horizontal = Spacing.SMALL.dp))
                            }
                            TypePillTag(type = type)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.LARGE.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.MEDIUM.dp),
                ) {
                    StatInfoCard(
                        label = "HEIGHT",
                        value = formatHeight(details.heightMeters),
                        icon = Icons.Outlined.Straighten,
                        iconTint = typeColor.primary,
                        modifier = Modifier.weight(1f),
                    )
                    StatInfoCard(
                        label = "WEIGHT",
                        value = formatWeight(details.weightKg),
                        icon = Icons.Outlined.FitnessCenter,
                        iconTint = Colors.statDef,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.LARGE.dp))

                BaseStatsSection(stats = details.stats)

                details.flavorText?.let { flavorText ->
                    Spacer(modifier = Modifier.height(Spacing.LARGE.dp))
                    DescriptionCard(description = flavorText)
                }

                if (details.evolutionChain.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(Spacing.LARGE.dp))
                    EvolutionChainSection(
                        stages = details.evolutionChain,
                        currentPokemonId = details.id,
                        primaryType = details.primaryType ?: "normal",
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.XXLARGE.dp))
            }
        }
    }
}

@Composable
private fun OfflineBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.28f),
        shape = RoundedCornerShape(Spacing.SMALL.dp),
    ) {
        Text(
            text = stringResource(id = R.string.offline_data_banner),
            color = Colors.white,
            fontSize = 14.sp,
            modifier = Modifier.padding(Spacing.MEDIUM.dp),
        )
    }
}

private fun formatHeight(meters: Double): String = String.format(Locale.US, "%.1f M", meters)

private fun formatWeight(kg: Double): String = String.format(Locale.US, "%.1f KG", kg)

@Preview
@Composable
fun DetailsScreenPreview() {
    val details = previewBulbasaurDetails()
    DroidDetailsScreenContent(
        topBarTitle = detailsTopBarTitle(DroidDetailsUiState.Success(details), "1"),
        state = DroidDetailsUiState.Success(details),
        onBackClick = {},
        onRetry = {},
    )
}

private fun previewBulbasaurDetails(): PokemonDetails =
    PokemonDetails(
        id = 1,
        name = "bulbasaur",
        baseExperience = 64,
        heightMeters = 0.7,
        weightKg = 6.9,
        types = listOf("grass", "poison"),
        abilities = emptyList(),
        stats =
            mapOf(
                "hp" to 45,
                "attack" to 49,
                "defense" to 49,
                "special-attack" to 65,
                "special-defense" to 65,
                "speed" to 45,
            ),
        sprites =
            PokemonDetails.Sprites(
                frontDefault = null,
                backDefault = null,
                frontShiny = null,
                backShiny = null,
            ),
        officialArtworkUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        genus = "The Seed Pokémon",
        flavorText =
            "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
        evolutionChain =
            listOf(
                PokemonDetails.EvolutionStage(
                    id = 1,
                    name = "bulbasaur",
                    spriteUrl =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                ),
                PokemonDetails.EvolutionStage(
                    id = 2,
                    name = "ivysaur",
                    spriteUrl =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png",
                ),
                PokemonDetails.EvolutionStage(
                    id = 3,
                    name = "venusaur",
                    spriteUrl =
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png",
                ),
            ),
    )
