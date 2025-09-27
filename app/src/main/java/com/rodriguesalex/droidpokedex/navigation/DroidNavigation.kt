package com.rodriguesalex.droidpokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rodriguesalex.droidpokedex.details.DroidDetailsScreen
import com.rodriguesalex.droidpokedex.home.DroidHomeScreen

@Composable
fun DroidNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            DroidHomeScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate("details/$pokemonId")
                },
            )
        }

        composable("details/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId") ?: ""
            DroidDetailsScreen(
                pokemonId = pokemonId,
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
