import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
    kotlin("plugin.serialization")

}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        val commonMain by getting{
            dependencies{
                // SQL Delight
                implementation("com.squareup.sqldelight:runtime:1.4.4")
                //badoo
                implementation ("com.badoo.reaktive:reaktive:1.1.20")
                implementation ("com.badoo.reaktive:reaktive-annotations:1.1.20")
                implementation ("com.badoo.reaktive:coroutines-interop:1.1.20")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
                implementation("io.ktor:ktor-client-core:1.4.0")
                implementation("io.ktor:ktor-client-serialization:1.4.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.google.android.material:material:1.2.1")
                // SQL Delight
                implementation("com.squareup.sqldelight:android-driver:1.4.4")
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")
                implementation("io.ktor:ktor-client-android:1.4.0")
                /*implementation ("com.badoo.reaktive:reaktive-android:1.1.20")
                implementation ("com.badoo.reaktive:reaktive-annotations:1.1.20")
                implementation ("com.badoo.reaktive:coroutines-interop:1.1.20")*/
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val iosMain by getting{
            dependencies{
                implementation("com.squareup.sqldelight:native-driver:1.4.4")
                implementation("io.ktor:ktor-client-ios:1.4.0")
            }

        }
        val iosTest by getting
    }


}
sqldelight {
    database("AppDatabase") {
        packageName = "com.saldivar.pruebatectina.shared.cache"
    }
}
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}

/*tasks.getByName("build").dependsOn(packForXcode)
dependencies {
    implementation("androidx.room:room-runtime:2.2.5")
    annotationProcessor("androidx.room:room-compiler:2.2.5")
}*/
