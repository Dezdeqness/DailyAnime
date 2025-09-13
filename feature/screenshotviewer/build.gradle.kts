plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.detekt)
    alias(libs.plugins.com.dezdeqness.compose)
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
    namespace = "com.dezdeqness.feature.screenshotsviewer"
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

    //Elm
    implementation(libs.elmslie.core)

    // Zoomable
    implementation(libs.engawapg.zoomable)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil)

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

    // Common
    implementation(project(":common:core-ui"))
    implementation(project(":common:core"))

    implementation(project(":data"))
    implementation(project(":domain"))
}
