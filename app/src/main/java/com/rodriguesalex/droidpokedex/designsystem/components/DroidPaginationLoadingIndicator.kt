package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun DroidPaginationLoadingIndicator(modifier: Modifier) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(Spacing.MEDIUM.dp),
    ) {
        CircularProgressIndicator(
            color = Color.White,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
fun PreviewLoadingIndicator() {
    DroidPaginationLoadingIndicator(
        modifier = Modifier.padding(Spacing.MEDIUM.dp),
    )
}
