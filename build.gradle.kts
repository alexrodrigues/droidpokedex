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
    jacoco
}

jacoco {
    toolVersion = "0.8.11"
}

subprojects {
    plugins.apply("io.gitlab.arturbosch.detekt")
    plugins.apply("org.jlleitschuh.gradle.ktlint")
    plugins.apply("jacoco")

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

        extensions.findByType<JacocoPluginExtension>()?.apply {
            toolVersion = "0.8.11"
        }

        tasks.withType<Test>().configureEach {
            configure<JacocoTaskExtension> {
                isIncludeNoLocationClasses = true
                excludes = listOf("jdk.internal.*")
            }
        }
    }
}

tasks.register<JacocoReport>("jacocoFullReport") {
    group = "verification"
    description = "Generate unified JaCoCo coverage report for all modules"

    val coverageModules = listOf(
        "app",
        "details:domain",
        "details:data",
        "home:domain",
        "home:data",
    )

    dependsOn(coverageModules.map { ":$it:testDebugUnitTest" })

    val excludePatterns = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/di/**",
        "**/*Module*",
        "**/*_Factory*",
        "**/*_MembersInjector*",
        "**/Hilt_*",
        "**/*Hilt*",
        "**/*_Impl*",
        "**/designsystem/**",
        "**/navigation/**",
        "**/ui/theme/**",
        "**/*Screen*",
        "**/*Preview*",
    )

    val srcDirs = coverageModules.map { modulePath ->
        file("${modulePath.replace(":", "/")}/src/main/java")
    }

    val classDirs = coverageModules.map { modulePath ->
        fileTree("${modulePath.replace(":", "/")}/build/tmp/kotlin-classes/debug") {
            exclude(excludePatterns)
        }
    }

    sourceDirectories.setFrom(files(srcDirs))
    classDirectories.setFrom(classDirs)
    executionData.setFrom(fileTree(rootDir) {
        include(coverageModules.map { "${it.replace(":", "/")}/build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec" })
    })

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacoco.xml"))
    }
}