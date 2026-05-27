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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodriguesalex.droidpokedex.designsystem.tokens.Colors
import com.rodriguesalex.droidpokedex.designsystem.tokens.Spacing

@Composable
fun StatInfoCard(
    label: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(Spacing.MEDIUM.dp),
                )
                .border(
                    width = 1.dp,
                    color = Colors.outlineVariant,
                    shape = RoundedCornerShape(Spacing.MEDIUM.dp),
                )
                .padding(Spacing.MEDIUM.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.size(Spacing.SMALL.dp))
            Text(
                text = label,
                color = Colors.onSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            )
        }
        Spacer(modifier = Modifier.height(Spacing.SMALL.dp))
        Text(
            text = value,
            color = Colors.onSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatInfoCardPreview() {
    StatInfoCard(
        label = "HEIGHT",
        value = "0.7 M",
        icon = Icons.Outlined.Straighten,
        iconTint = Colors.statHp,
    )
}
