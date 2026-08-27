plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.google.protobuf") version "0.9.4"
    id("com.dezdeqness.config")
    id("com.dezdeqness.detekt")
    id("com.dezdeqness.room")
}

android {
    namespace = "com.dezdeqness.data.user"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        create("qa") {}
    }
}

dependencies {
    implementation(project(":contract:auth"))
    implementation(project(":contract:history"))
    implementation(project(":contract:user"))
    implementation(project(":contract:anime"))
    implementation(project(":shared:shared-domain"))

    implementation(project(":data:core"))
    implementation(project(":data:remote-common"))

    // Crypto
    implementation(libs.google.tink)
    implementation(libs.protobuf.javalite)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Coroutines
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)

    // Network
    implementation(libs.square.okhttp)
    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)

    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.30.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
