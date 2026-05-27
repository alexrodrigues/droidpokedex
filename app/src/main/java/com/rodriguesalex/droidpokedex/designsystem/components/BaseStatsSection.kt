@file:Suppress("FunctionNaming", "MagicNumber")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

private const val MAX_STAT_VALUE = 255f

data class BaseStatRow(
    val label: String,
    val apiKey: String,
    val barColor: Color,
)

private val orderedStats =
    listOf(
        BaseStatRow("HP", "hp", Colors.statHp),
        BaseStatRow("ATK", "attack", Colors.statAtk),
        BaseStatRow("DEF", "defense", Colors.statDef),
        BaseStatRow("SPA", "special-attack", Colors.statSpa),
        BaseStatRow("SPD", "special-defense", Colors.statSpd),
        BaseStatRow("SPE", "speed", Colors.statSpe),
    )

@Composable
fun BaseStatsSection(
    stats: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = Colors.surfaceContainer,
                    shape = RoundedCornerShape(Spacing.MEDIUM.dp),
                )
                .border(
                    width = 1.dp,
                    color = Colors.outlineVariant,
                    shape = RoundedCornerShape(Spacing.MEDIUM.dp),
                )
                .padding(Spacing.LARGE.dp),
    ) {
        Text(
            text = "BASE STATS",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Colors.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
        Spacer(modifier = Modifier.height(Spacing.LARGE.dp))
        orderedStats.forEach { statRow ->
            val value = stats[statRow.apiKey] ?: 0
            BaseStatBarRow(
                label = statRow.label,
                value = value,
                barColor = statRow.barColor,
            )
            Spacer(modifier = Modifier.height(Spacing.MEDIUM.dp))
        }
    }
}

@Composable
private fun BaseStatBarRow(
    label: String,
    value: Int,
    barColor: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.width(40.dp),
            color = Colors.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
        LinearProgressIndicator(
            progress = { value / MAX_STAT_VALUE },
            modifier =
                Modifier
                    .weight(1f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(Spacing.SMALL.dp))
                    .border(
                        width = 1.dp,
                        color = Colors.outlineVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(Spacing.SMALL.dp),
                    ),
            color = barColor,
            trackColor = Color.White,
            strokeCap = StrokeCap.Round,
        )
        Spacer(modifier = Modifier.width(Spacing.SMALL.dp))
        Text(
            text = value.toString(),
            modifier = Modifier.width(32.dp),
            color = Colors.onSurface,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BaseStatsSectionPreview() {
    BaseStatsSection(
        stats =
            mapOf(
                "hp" to 45,
                "attack" to 49,
                "defense" to 49,
                "special-attack" to 65,
                "special-defense" to 65,
                "speed" to 45,
            ),
    )
}
