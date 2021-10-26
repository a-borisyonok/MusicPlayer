plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")


    defaultConfig {
        applicationId("com.rsschool.seka.musicplayer")
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")
        vectorDrawables.useSupportLibrary = true

    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        getByName("release") {
            minifyEnabled(false)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("androidx.appcompat:appcompat:1.3.1")


    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    // ExoPlayer
    api("com.google.android.exoplayer:exoplayer-core:2.15.1")
    api("com.google.android.exoplayer:exoplayer-ui:2.15.1")
    api("com.google.android.exoplayer:extension-mediasession:2.15.1")


}
kapt {
    correctErrorTypes = true
}
