plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.detekt)
    alias(libs.plugins.com.dezdeqness.compose)
    alias(libs.plugins.com.dezdeqness.room)
    id("kotlin-parcelize")
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
    namespace = "com.dezdeqness.feature.userrate"
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
    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Common
    implementation(project(":common:foundation-ui"))
    implementation(project(":common:foundation"))

    // Unit Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Testing
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.espresso)

    // Mock
    testImplementation(libs.mockk.mockk)

    implementation(project(":contract:anime"))
    implementation(project(":contract:userrate"))
    implementation(project(":contract:user"))
    implementation(project(":contract:settings"))

    implementation(project(":data:core"))
    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
}
