@file:Suppress("FunctionNaming", "MagicNumber")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun PokemonHeroSection(
    pokemonId: Int,
    imageUrl: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(1f),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(Spacing.MEDIUM.dp))
                    .checkeredBackground(backgroundColor)
                    .padding(Spacing.LARGE.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(200.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(Spacing.SMALL.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Pokemon artwork",
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(Spacing.MEDIUM.dp),
                )
            }
        }
        Text(
            text = "POKEMON #${pokemonId.toString().padStart(3, '0')}",
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-Spacing.SMALL).dp, y = (-Spacing.SMALL).dp)
                    .background(
                        color = Colors.pokedexRedContainer,
                        shape = RoundedCornerShape(Spacing.XXLARGE.dp),
                    )
                    .padding(horizontal = Spacing.MEDIUM.dp, vertical = Spacing.XSMALL.dp),
            color = Colors.white,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
        )
    }
}

private fun Modifier.checkeredBackground(baseColor: Color): Modifier =
    drawBehind {
        val cellSize = 16.dp.toPx()
        val lightColor = baseColor.copy(alpha = 0.35f)
        val darkColor = baseColor.copy(alpha = 0.55f)
        drawRect(color = lightColor)
        var row = 0
        var y = 0f
        while (y < size.height) {
            var col = 0
            var x = 0f
            while (x < size.width) {
                if ((row + col) % 2 == 0) {
                    drawRect(
                        color = darkColor,
                        topLeft = Offset(x, y),
                        size = androidx.compose.ui.geometry.Size(cellSize, cellSize),
                    )
                }
                x += cellSize
                col++
            }
            y += cellSize
            row++
        }
    }

@Preview(showBackground = true)
@Composable
fun PokemonHeroSectionPreview() {
    PokemonHeroSection(
        pokemonId = 1,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
        backgroundColor = Color(0xFF62B957),
    )
}
