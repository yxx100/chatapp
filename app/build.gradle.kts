import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.chatapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(fileTree("libs") {
        include("*.jar", "*.har")
    })
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //Bmob后端云依赖
    implementation("io.github.bmob:android-sdk:4.1.0")
    implementation("io.reactivex.rxjava3:rxjava:3.1.9")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.8.1")
    implementation("com.squareup.okio:okio:2.2.2")
    implementation("com.google.code.gson:gson:2.8.5")
    //环信SDK依赖
    implementation("io.hyphenate:hyphenate-chat:4.17.0")
//    UIKit 依赖
//    implementation("io.hyphenate:ease-chat-kit:4.13.0")
    //Glide依赖
    implementation("com.github.bumptech.glide:glide:4.15.1")
//kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
    //callkit依赖
    implementation("io.hyphenate:chat-call-kit:4.16.0")
    //Wavesidebar依赖
    implementation("io.openharmony.tpc.thirdlib:WaveSideBar:1.0.3")
    implementation("com.gjiazhe:wavesidebar:1.3")
}