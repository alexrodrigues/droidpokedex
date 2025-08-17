package com.rodriguesalex.droidpokedex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class DroidFlutterActivity : FlutterActivity() {
    
    companion object {
        private const val EXTRA_ROUTE = "route"
        private const val FLUTTER_ENGINE_ID = "droid_pokedex_engine"
        
        fun createIntent(context: Context, route: String? = null): Intent {
            return Intent(context, DroidFlutterActivity::class.java).apply {
                putExtra(EXTRA_ROUTE, route)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        // Create and cache Flutter engine
        val flutterEngine = FlutterEngine(context).apply {
            dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        }
        FlutterEngineCache.getInstance().put(FLUTTER_ENGINE_ID, flutterEngine)
        return flutterEngine
    }
    
    override fun getInitialRoute(): String? {
        return intent.getStringExtra(EXTRA_ROUTE)
    }
} 
