plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
//    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

android {
//    signingConfigs {
//        create("release") {
//            storeFile = file("F:\\android_studio_projects_kotlin\\Saloon-App\\app\\build\\jks\\salon.jks")
//            storePassword = "Yash@2004"
//            keyAlias = "key0"
//            keyPassword = "Yash@2004"
//        }
//    }
    namespace = "com.practicecoding.sallonapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.practicecoding.sallonapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("androidx.compose.material3:material3-android:1.3.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
//    implementation("com.google.firebase:firebase-perf-ktx:21.0.0")
    //this is the code for navigation between screens
    val nav_version = "2.7.7"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    //data base
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    kapt("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")



    val composeVersion = "1.7.3"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.3.0")

    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")


    //Network call to connect to internet and API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //Json to kotlin object mapping ,this will convert json object to kotlin object
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.animation:animation:1.7.3")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")



//    lifecycle
    val lifecycle_version = "2.8.6"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    //livedata
    implementation ("androidx.compose.runtime:runtime-livedata:$composeVersion")



    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    //hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    //dagger-hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    //animation
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.35.0-alpha")

//photo picker
    implementation("io.coil-kt:coil-compose:2.4.0")
    //For rememberLauncherForActivityResult()
    implementation ("androidx.activity:activity-compose:1.9.2")

//For PickVisualMedia contract
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("com.exyte:animated-navigation-bar:1.0.0")

//lottie animation
    implementation ( "com.airbnb.android:lottie-compose:6.4.0" )
    implementation("com.exyte:animated-navigation-bar:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")

    // Extended Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.3")

    //Ads
    implementation("com.google.android.gms:play-services-ads:23.4.0")
    implementation ("io.github.farimarwat:admobnative-compose:1.2")

}
