plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.com.dezdeqness.config)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        create("qa") {
            // You can add specific fields if needed later
        }
    }

    namespace = "com.dezdeqness.advancedrecycler"
}

dependencies {
    // Material Design
    implementation(libs.google.material)

    // Core
    implementation(libs.androidx.core)

    // Appcompat
    implementation(libs.androidx.appcompat)

    // Collection
    implementation(libs.androidx.collection)
    
    // Unit Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)

    // Android Testing
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}