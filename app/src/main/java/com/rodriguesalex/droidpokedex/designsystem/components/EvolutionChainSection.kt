@file:Suppress("FunctionNaming", "MagicNumber")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun EvolutionChainSection(
    stages: List<PokemonDetails.EvolutionStage>,
    currentPokemonId: Int,
    primaryType: String,
    modifier: Modifier = Modifier,
) {
    if (stages.isEmpty()) return

    val highlightColor = DroidPokemonTypeColor.getPokemonColor(primaryType).primary

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "EVOLUTION CHAIN",
            color = Colors.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(Spacing.LARGE.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            stages.forEachIndexed { index, stage ->
                if (index > 0) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Colors.outlineVariant,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(Spacing.SMALL.dp))
                }
                EvolutionStageItem(
                    stage = stage,
                    isCurrent = stage.id == currentPokemonId,
                    highlightColor = highlightColor,
                )
            }
        }
    }
}

@Composable
private fun EvolutionStageItem(
    stage: PokemonDetails.EvolutionStage,
    isCurrent: Boolean,
    highlightColor: Color,
) {
    val spriteSize = if (isCurrent) 80.dp else 64.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = Spacing.XSMALL.dp),
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.size(spriteSize),
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = stage.spriteUrl),
                contentDescription = stage.name,
                modifier =
                    Modifier
                        .size(spriteSize)
                        .clip(RoundedCornerShape(Spacing.SMALL.dp)),
                alpha = if (isCurrent) 1f else 0.55f,
            )
            if (isCurrent) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .width(48.dp)
                            .height(2.dp)
                            .background(
                                color = highlightColor,
                                shape = RoundedCornerShape(1.dp),
                            ),
                )
            }
        }
        Spacer(modifier = Modifier.height(Spacing.XSMALL.dp))
        Text(
            text = "#${stage.id.toString().padStart(3, '0')}",
            color = if (isCurrent) highlightColor else Colors.onSurfaceVariant,
            fontSize = 8.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EvolutionChainSectionPreview() {
    EvolutionChainSection(
        stages =
            listOf(
                PokemonDetails.EvolutionStage(1, "bulbasaur", ""),
                PokemonDetails.EvolutionStage(2, "ivysaur", ""),
                PokemonDetails.EvolutionStage(3, "venusaur", ""),
            ),
        currentPokemonId = 1,
        primaryType = "grass",
    )
}
