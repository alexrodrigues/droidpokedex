package com.rodriguesalex.droidpokedex

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity

/**
 * Simple Flutter integration helper
 * This class provides basic functionality to integrate Flutter content
 */
object FlutterIntegration {
    
    /**
     * Navigate to a Flutter screen
     * Note: This requires the Flutter module to be properly configured
     */
    fun navigateToFlutter(context: Context, route: String? = null) {
        val intent = DroidFlutterActivity.createIntent(context, route)
        context.startActivity(intent)
    }
    
    /**
     * Check if Flutter is available
     */
    fun isFlutterAvailable(): Boolean {
        return try {
            // Try to access Flutter classes to check availability
            Class.forName("io.flutter.embedding.android.FlutterActivity")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }
}
