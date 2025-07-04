package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rodriguesalex.droidpokedex.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DroidErrorComponent(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier =
            modifier
                .width(200.dp)
                .height(200.dp),
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.stat_notify_error),
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.align(
                Alignment.CenterHorizontally,
            ),
        )
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .padding(
                    top = 8.dp
                )
                .align(
                    Alignment.CenterHorizontally,
                ),
        )
    }
}

@Preview()
@Composable
fun DroidErrorComponentPreview() {
    DroidErrorComponent(
        message = stringResource(id = R.string.error_message),
    )
}