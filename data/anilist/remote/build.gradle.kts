plugins {
    alias(libs.plugins.com.dezdeqness.data)
    alias(libs.plugins.apollo)
}

android {
    namespace = "com.dezdeqness.data.anilist.remote"
}

dependencies {
    api(libs.apollo.runtime)
    implementation(libs.square.okhttp)

    implementation(project(":common:foundation"))
    implementation(project(":data:core"))
}

apollo {
    service("anilist") {
        packageName.set("com.dezdeqness.data.anilist.graphql")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
    }
}
