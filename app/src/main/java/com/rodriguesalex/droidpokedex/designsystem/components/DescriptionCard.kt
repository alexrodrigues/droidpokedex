@file:Suppress("FunctionNaming", "MagicNumber")

package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun DescriptionCard(
    description: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .dashedBorder(
                    color = Colors.outlineVariant,
                    cornerRadius = Spacing.MEDIUM.dp,
                )
                .background(
                    color = Color.White.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(Spacing.MEDIUM.dp),
                )
                .padding(Spacing.LARGE.dp),
    ) {
        Text(
            text = "\"$description\"",
            color = Colors.onSurface,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 24.sp,
            textAlign = TextAlign.Start,
        )
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: androidx.compose.ui.unit.Dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 2.dp,
): Modifier =
    drawBehind {
        val stroke = strokeWidth.toPx()
        val halfStroke = stroke / 2f
        drawRoundRect(
            color = color,
            topLeft = Offset(halfStroke, halfStroke),
            size =
                Size(
                    width = size.width - stroke,
                    height = size.height - stroke,
                ),
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style =
                Stroke(
                    width = stroke,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)),
                ),
        )
    }

@Preview(showBackground = true)
@Composable
fun DescriptionCardPreview() {
    DescriptionCard(
        description =
            "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
    )
}
