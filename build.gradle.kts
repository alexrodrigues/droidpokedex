// Top-level build file where you can add configuration options common to all sub-projects/modules.
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    plugins.apply("io.gitlab.arturbosch.detekt") // Apply Detekt plugin
    plugins.apply("org.jlleitschuh.gradle.ktlint")

    afterEvaluate {
        extensions.findByType<io.gitlab.arturbosch.detekt.extensions.DetektExtension>()?.apply {
            toolVersion = "1.23.0"
            config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
            allRules = false
            autoCorrect = true
        }

        // Detekt 1.23 does not accept JVM target 20+; pin to match Android modules (Java 17).
        tasks.withType<Detekt>().configureEach {
            jvmTarget = "17"
        }

        extensions.findByType<org.jlleitschuh.gradle.ktlint.KtlintExtension>()?.apply {
            android.set(true)
            outputToConsole.set(true)
            ignoreFailures.set(false)
            enableExperimentalRules.set(true)
        }
    }
}