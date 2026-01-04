plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.detekt)
    alias(libs.plugins.com.dezdeqness.compose)
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
    namespace = "com.dezdeqness.feature.settings"
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

    implementation(libs.androidx.runtime)
    implementation(libs.androidx.appcompat)

    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Common
    implementation(project(":common:core-ui"))
    implementation(project(":common:core"))

    // Unit Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Testing
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.espresso)

    implementation(libs.coil)

    // Mock
    testImplementation(libs.mockk.mockk)

    implementation(libs.reorderable)

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(project(":contract:settings"))
    implementation(project(":contract:auth"))

    implementation(project(":feature:personallist"))

    implementation(project(":shared:shared-presentation"))
}
