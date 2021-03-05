plugins {
    id("com.android.application")
    kotlin("android")
    id("com.squareup.sqldelight")
    id("kotlin-android")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    //Imagen circular
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //corutinas
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.3.3")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.saldivar.pruebatectina.androidApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}