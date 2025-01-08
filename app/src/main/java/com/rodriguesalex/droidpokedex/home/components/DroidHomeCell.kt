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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.rodriguesalex.droidpokedex.designsystem.spacing

@Composable
fun DroidHomeCell(
    pokemonName: String,
    pokemonNumber: Int,
    pokemonImageUrl: String,
    types: List<String>,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing.small.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(spacing.medium.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Pokemon Image
            Image(
                painter = rememberAsyncImagePainter(model = pokemonImageUrl),
                contentDescription = "$pokemonName image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(spacing.medium.dp))

            // Pokemon Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "#$pokemonNumber - ${pokemonName.uppercase()}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Types
                types.forEach { type ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = spacing.xxsmall.dp)
                            .clip(RoundedCornerShape(spacing.small.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                            .padding(horizontal = spacing.small.dp, vertical = spacing.xsmall.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = type,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
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
        backgroundColor = Color(0xFF63B556)
    )
}