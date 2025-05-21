plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lab7rpomp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lab7rpomp"
        minSdk = 29
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

    implementation (libs.appcompat.v161)
    implementation (libs.material.v1110)
    implementation (libs.constraintlayout)

    implementation (libs.camera.core)
    implementation (libs.camera.camera2)
    implementation (libs.camera.lifecycle)
    implementation (libs.camera.video)
    implementation (libs.androidx.camera.view)

    implementation (libs.androidx.room.runtime)
    annotationProcessor (libs.androidx.room.compiler)
    implementation (libs.play.services.location)
    testImplementation (libs.junit)
    androidTestImplementation (libs.ext.junit)
    androidTestImplementation (libs.androidx.espresso.core.v351)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}