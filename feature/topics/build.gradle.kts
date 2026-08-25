plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.detekt)
    alias(libs.plugins.com.dezdeqness.compose)
    alias(libs.plugins.screenshot)
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
    namespace = "com.dezdeqness.feature.topics"
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

    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)

    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Common
    implementation(project(":common:foundation-ui"))
    implementation(project(":common:foundation"))
    implementation(project(":shared:shared-presentation"))

    // Domain
    implementation(project(":domain:topic"))
    implementation(project(":contract:topic"))
    implementation(project(":contract:anime"))

    // Data
    implementation(project(":data:core"))

    // Network
    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)

    // Elm
    implementation(libs.elmslie.core)

    // Unit Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(project(":common:foundation-test"))

    // Android Testing
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.espresso)

    // Mock
    testImplementation(libs.mockk.mockk)
}
