import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.kosa.selp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kosa.selp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val envFile = rootProject.file(".env")
        val properties = Properties()
        if (envFile.exists()) {
            properties.load(envFile.inputStream())
        }

        // BuildConfig에 추가
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            properties.getProperty("KAKAO_NATIVE_APP_KEY", "")
        )
        buildConfigField(
            "String",
            "BACKEND_BASE_URL",
            properties.getProperty("BACKEND_BASE_URL", "")
        )

        // AndroidManifest.xml에서 사용할 수 있도록 추가
        manifestPlaceholders["kakaoAppKey"] =
            properties.getProperty("KAKAO_NATIVE_APP_KEY", "").removeSurrounding("\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.lottie.compose)

    implementation("com.kakao.sdk:v2-user:2.19.0")

    // API 호출
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 날짜 선택
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    // Kotlin Coroutines core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    // Kotlin Coroutines Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}