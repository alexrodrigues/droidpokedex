package com.rodriguesalex.droidpokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rodriguesalex.droidpokedex.home.DroidHomeScreen
import com.rodriguesalex.droidpokedex.ui.theme.DroidPokedexTheme
import dagger.hilt.android.AndroidEntryPoint
// Temporarily commented out Flutter imports
// import io.flutter.embedding.engine.FlutterEngine
// import io.flutter.embedding.engine.FlutterEngineCache
// import io.flutter.embedding.engine.dart.DartExecutor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    // Temporarily commented out Flutter engine
    // private lateinit var flutterEngine: FlutterEngine
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Temporarily commented out Flutter engine initialization
        // initializeFlutterEngine()
        
        enableEdgeToEdge()
        setContent {
            DroidPokedexTheme {
                DroidHomeScreen()
            }
        }
    }
    
    // Temporarily commented out Flutter engine methods
    /*
    private fun initializeFlutterEngine() {
        flutterEngine = FlutterEngine(this).apply {
            dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        }
        
        // Cache the Flutter engine
        FlutterEngineCache
            .getInstance()
            .put("flutter_engine", flutterEngine)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        flutterEngine.destroy()
    }
    */
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DroidPokedexTheme {
        DroidHomeScreen()
    }
}
