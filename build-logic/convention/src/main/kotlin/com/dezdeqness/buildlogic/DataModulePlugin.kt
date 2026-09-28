package com.dezdeqness.buildlogic

import com.android.build.api.dsl.LibraryExtension
import com.dezdeqness.buildlogic.gradleplugins.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class DataModulePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("com.google.devtools.ksp")
            apply("com.dezdeqness.config")
            apply("com.dezdeqness.detekt")
        }

        extensions.configure<LibraryExtension> {
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }
            buildTypes {
                maybeCreate("qa")
            }
        }

        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion.set(JavaLanguageVersion.of(JVM_VERSION))
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            jvmToolchain(JVM_VERSION)
        }

        dependencies {
            add("implementation", libs.findLibrary("dagger-dagger").get())
            add("ksp", libs.findLibrary("dagger-compilier").get())
        }
    }

    private companion object {
        const val JVM_VERSION = 21
    }
}
