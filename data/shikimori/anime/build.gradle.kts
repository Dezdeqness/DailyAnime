plugins {
    alias(libs.plugins.com.dezdeqness.data)
}

android {
    namespace = "com.dezdeqness.data.shikimori.anime"
}

dependencies {
    implementation(project(":contract:anime"))
    implementation(project(":contract:settings"))

    implementation(project(":data:core"))

    implementation(libs.square.retrofit)
}
