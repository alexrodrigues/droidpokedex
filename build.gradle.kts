// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    plugins.apply("io.gitlab.arturbosch.detekt") // Apply Detekt plugin

    afterEvaluate {
        extensions.findByType<io.gitlab.arturbosch.detekt.extensions.DetektExtension>()?.apply {
            toolVersion = "1.23.0"
            config = files("$rootDir/config/detekt/detekt.yml")
            buildUponDefaultConfig = true
            allRules = false
            autoCorrect = true
        }
    }
}