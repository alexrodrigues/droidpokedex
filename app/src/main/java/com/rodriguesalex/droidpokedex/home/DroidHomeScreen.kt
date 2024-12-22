package com.rodriguesalex.droidpokedex.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeViewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DroidHomeScreen(
    viewModel: DroidHomeViewModel = hiltViewModel()
) {

    val navigationEvent by viewModel.testEvent.observeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Droid Home") },)
        }
    ) { innerPadding ->
        Column {
            navigationEvent?.let {
                Text(
                    it,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            Text(
                "Home screen",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}