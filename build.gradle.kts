// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jlleitschuh.gradle.ktlint") version BuildPluginsVersion.KTLINT
    id("io.gitlab.arturbosch.detekt") version BuildPluginsVersion.DETEKT
}

buildscript {

    repositories {
        google()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${BuildPluginsVersion.AGP}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildPluginsVersion.KOTLIN}")
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("io.gitlab.arturbosch.detekt")
    }

    // Optionally configure plugin
    ktlint {
        debug.set(true)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        additionalEditorconfigFile.set(file(".editorconfig"))
        disabledRules.set(setOf("final-newline"))
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        failFast = true // fail build on any finding
        buildUponDefaultConfig = true // preconfigure defaults
        config = files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
        baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt

        reports {
            html.enabled = true // observe findings in your browser with structure and code snippets
            xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
            txt.enabled = true // similar to the console output, contains issue signature to manually edit baseline files
        }
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
