package com.dezdeqness.buildlogic

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class FlavorPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        extensions.configure<AppExtension> {
            buildTypes {
                getByName("release") {
                    isMinifyEnabled = true
                    isDebuggable = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }

                getByName("debug") {
                    isMinifyEnabled = false
                    isDebuggable = true
                    applicationIdSuffix = ".debug"
                    proguardFiles("proguard-rules.pro")
                }

                create("qa") {
                    initWith(getByName("debug"))
                    applicationIdSuffix = ".qa"
                }
            }
        }


        afterEvaluate {
            extensions.configure<AppExtension> {
                buildTypes {
                    getByName("release").signingConfig = signingConfigs.getByName("release")
                    getByName("qa").signingConfig = signingConfigs.getByName("release")
                }
            }
        }
    }
}
