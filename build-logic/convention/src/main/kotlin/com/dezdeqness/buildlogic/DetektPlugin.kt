package com.dezdeqness.buildlogic

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class DetektPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("io.gitlab.arturbosch.detekt")

        extensions.configure<DetektExtension> {
            toolVersion = "1.23.7" // Or use your libs.versions.toml
            config.setFrom(files("${rootDir.absolutePath}/config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
        }
    }
}