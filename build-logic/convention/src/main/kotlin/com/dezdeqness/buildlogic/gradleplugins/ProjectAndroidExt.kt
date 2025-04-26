package com.dezdeqness.buildlogic.gradleplugins

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginInstantiationException
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.findByType

val Project.libs: VersionCatalog
    get() = extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")

val Project.commonExtension: CommonExtension<*, *, *, *, *, *>
    get() = applicationExtension
        ?: libraryExtension
        ?: throw PluginInstantiationException("Can only be applied on an android Application or Library")

val Project.applicationExtension: ApplicationExtension?
    get() = extensions.findByType<ApplicationExtension>()

val Project.libraryExtension: LibraryExtension?
    get() = extensions.findByType<LibraryExtension>()

fun DependencyHandlerScope.implementationLib(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandlerScope.debugImplementationLib(dependency: Any) {
    add("debugImplementation", dependency)
}

fun DependencyHandlerScope.androidTestImplementationLib(dependency: Any) {
    add("androidTestImplementation", dependency)
}
