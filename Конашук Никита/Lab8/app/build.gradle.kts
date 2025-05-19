plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lab8"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lab8"
        minSdk = 24
        targetSdk = 35
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
    // Базовые библиотеки
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 🔥 OpenStreetMap (osmdroid)
    implementation("org.osmdroid:osmdroid-android:6.1.16")

    // 📍 Геолокация (Google Play Services)
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // 🔹 Дополнительно: Room (если потребуется сохранять маршрут в БД)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
}
