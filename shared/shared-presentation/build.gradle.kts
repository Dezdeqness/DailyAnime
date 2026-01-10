plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.detekt)
    alias(libs.plugins.ksp)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "com.dezdeqness.shared.presentation"

    compileOptions {
        val javaVersion = JavaVersion.VERSION_21
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    buildTypes {
        create("qa") {}
    }
}

dependencies {
    // Appcompat
    implementation(libs.androidx.appcompat)

    // Common
    implementation(project(":common:core"))

    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Contracts
    implementation(project(":contract:anime"))
    implementation(project(":contract:settings"))

    implementation(project(":shared:shared-domain"))
}
