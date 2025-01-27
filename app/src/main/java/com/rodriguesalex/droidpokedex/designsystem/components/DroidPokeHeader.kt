@file:Suppress("FunctionNaming")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DroidPokeHeader(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.Center) {
            BuildBall(color = Color.White, size = 70.dp)
            BuildBall(color = Color(0xFF2196F3), size = 64.dp)
        }
        Spacer(modifier = Modifier.width(32.dp)) // Red ball
        BuildBall(color = Color(0xFFD10C41), size = 18.dp)
        Spacer(modifier = Modifier.width(8.dp)) // Spacing between balls
        BuildBall(color = Color.Yellow, size = 18.dp) // Yellow ball
        Spacer(modifier = Modifier.width(8.dp)) // Spacing between balls
        BuildBall(color = Color.Green, size = 18.dp) // Green ball
    }
}

@Composable
fun BuildBall(
    color: Color,
    size: Dp,
) {
    Box(
        modifier =
            Modifier
                .size(size)
                .shadow(4.dp, shape = CircleShape)
                .background(color = color, shape = CircleShape),
    )
}
