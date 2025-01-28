package com.rodriguesalex.droidpokedex.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.rodriguesalex.droidpokedex.R
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun DroidHomeCell(
    pokemonName: String,
    pokemonNumber: Int,
    pokemonImageUrl: String,
    types: List<String>,
    backgroundColor: Color,
    pokeballImageRes: Int,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(Spacing.SMALL.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .height(150.dp)
                    .background(backgroundColor)
                    .padding(Spacing.MEDIUM.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Stack images using Box
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp),
            ) {
                // Background Poké Ball Image
                Image(
                    painter = rememberAsyncImagePainter(model = pokeballImageRes),
                    contentDescription = "Poké Ball Background",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier =
                        Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.6f)),
                )

                // Foreground Pokémon Image
                Image(
                    painter = rememberAsyncImagePainter(model = pokemonImageUrl),
                    contentDescription = "$pokemonName image",
                    modifier =
                        Modifier
                            .size(90.dp)
                            .clip(CircleShape),
                )
            }

            Spacer(modifier = Modifier.width(Spacing.MEDIUM.dp))

            // Pokemon Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "#$pokemonNumber - ${pokemonName.uppercase()}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Types
                types.forEach { type ->
                    Box(
                        modifier =
                            Modifier
                                .padding(vertical = Spacing.XXSMALL.dp)
                                .clip(RoundedCornerShape(Spacing.SMALL.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                                .padding(horizontal = Spacing.SMALL.dp, vertical = Spacing.XSMALL.dp)
                                .fillMaxWidth(),
                    ) {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonListScreenPreview() {
    DroidHomeCell(
        pokemonName = "bulbasaur",
        pokemonNumber = 1,
        pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        types = listOf("grass", "poison"),
        backgroundColor = Color(0xFF63B556),
        pokeballImageRes = R.drawable.pokeball,
    )
}
