package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors

@Composable
fun DroidSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        colors =
            ButtonDefaults.buttonColors(
                containerColor = Colors.white700,
                contentColor = Colors.black,
            ),
        modifier = modifier,
        shape =
            RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomEnd = 8.dp,
                bottomStart = 8.dp,
            ),
        onClick = {
            onClick.invoke()
        },
        content = {
            Text(text = text)
        },
    )
}

@Preview
@Composable
fun DroidSecondaryButtonPreview() {
    DroidSecondaryButton(
        text = "Primary Button",
        onClick = {},
    )
}
