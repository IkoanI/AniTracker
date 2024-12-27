plugins {
    alias(libs.plugins.android.application)
    id("com.apollographql.apollo3").version("3.8.5")
}

android {
    namespace = "com.example.anitracker"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    defaultConfig {
        applicationId = "com.example.anitracker"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

apollo {
    service("anilist") {
        packageName.set("com.example.anitracker")
        generateKotlinModels.set(false)
    }
}

dependencies {
    implementation(libs.apache.commons.lang3)
    implementation(libs.apollo.runtime)
    implementation(libs.apollo.rx3.support)
    implementation(libs.glide)
    implementation(libs.jsoup)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.paging.rxjava3)
    implementation(libs.paging.runtime)
    implementation(libs.keyboardvisibilityevent)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.viewpager2)
    implementation(libs.google.flexbox)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}