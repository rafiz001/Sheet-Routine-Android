plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.rafizuddin.sheetroutine"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rafizuddin.sheetroutine"
        minSdk = 14
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

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

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("androidx.activity:activity:1.2.0") // Last version with minSdk 14
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("org.nanohttpd:nanohttpd:2.3.1")
}