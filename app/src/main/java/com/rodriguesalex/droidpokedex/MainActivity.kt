package com.rodriguesalex.droidpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.rodriguesalex.droidpokedex.navigation.DroidNavigation
import com.rodriguesalex.droidpokedex.ui.theme.DroidPokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DroidPokedexTheme {
                val navController = rememberNavController()
                DroidNavigation(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DroidPokedexTheme {
        val navController = rememberNavController()
        DroidNavigation(navController = navController)
    }
}
