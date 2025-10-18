package com.dezdeqness.buildlogic

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("jacoco")
        target.extensions.configure<BaseExtension> {
            compileSdkVersion(35)
            defaultConfig {
                minSdk = 24
                targetSdk = 35
            }
        }
    }
}
