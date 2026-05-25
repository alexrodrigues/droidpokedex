@file:Suppress("FunctionNaming", "MagicNumber")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun DetailsTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    hardwareHeader: @Composable () -> Unit = { DroidPokeHeader(modifier = Modifier.padding(horizontal = Spacing.LARGE.dp)) },
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Colors.pokedexRed),
    ) {
        hardwareHeader()
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Colors.pokedexRed)
                    .padding(horizontal = Spacing.SMALL.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Colors.white,
                )
            }
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                color = Colors.white,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Colors.white.copy(alpha = 0.85f),
                )
            }
        }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Colors.pokedexRedDark),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsTopBarPreview() {
    DetailsTopBar(
        title = "Bulbasaur Entry #001",
        onBackClick = {},
    )
}
