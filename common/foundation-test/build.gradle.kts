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
    namespace = "com.dezdeqness.foundation.test"

    compileOptions {
        val javaVersion = JavaVersion.VERSION_21
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    buildTypes {
        create("qa") {}
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(":common:architecture"))
    api(libs.elmslie.core)

    // Coroutines
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)
    api(libs.kotlinx.coroutines.test)

    // Unit Testing
    api(libs.junit.api)
    runtimeOnly(libs.junit.engine)
    implementation(libs.androidx.test.junit)
}
