@file:Suppress("FunctionNaming", "MagicNumber")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun TypePillTag(
    type: String,
    modifier: Modifier = Modifier,
) {
    val typeColor = DroidPokemonTypeColor.getPokemonColor(type)
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(Spacing.XXLARGE.dp))
                .background(typeColor.primary.copy(alpha = 0.12f))
                .border(
                    width = 1.dp,
                    color = typeColor.primary.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(Spacing.XXLARGE.dp),
                )
                .padding(horizontal = Spacing.LARGE.dp, vertical = Spacing.SMALL.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(typeColor.primary),
        )
        Spacer(modifier = Modifier.width(Spacing.SMALL.dp))
        Text(
            text = type.lowercase(),
            color = typeColor.primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TypePillTagPreview() {
    Row {
        TypePillTag(type = "grass")
        Spacer(modifier = Modifier.width(Spacing.MEDIUM.dp))
        TypePillTag(type = "poison")
    }
}
