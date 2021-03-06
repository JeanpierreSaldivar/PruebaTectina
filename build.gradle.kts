buildscript {
    val kotlin_version by extra("1.4.31")
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10")
        classpath("com.android.tools.build:gradle:4.2.0-beta05")
        classpath ("com.squareup.sqldelight:gradle-plugin:1.4.4")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.30")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")

    }

}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/badoo/maven")
    }
}
