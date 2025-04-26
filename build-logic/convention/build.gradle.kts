import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.dezdeqness.buildlogic.gradleplugins"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin)
    compileOnly(libs.android.tools.common)
}

gradlePlugin {
    plugins {
        register("androidCompose") {
            id = "com.dezdeqness.compose"
            implementationClass = "com.dezdeqness.buildlogic.AndroidComposePlugin"
        }
        register("androidRoom") {
            id = "com.dezdeqness.room"
            implementationClass = "com.dezdeqness.buildlogic.RoomPlugin"
        }
    }
}
