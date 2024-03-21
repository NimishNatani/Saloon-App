plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
}

android {
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
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    //this is the code for navigation between screens
    val nav_version = "2.7.7"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    //data base
    val room = "2.6.0"
    implementation("androidx.room:room-ktx:$room")
    kapt("androidx.room:room-compiler:$room")


    val composeVersion = "1.6.2"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.2.1")

    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")


    //Network call to connect to internet and API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //Json to kotlin object mapping ,this will convert json object to kotlin object
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")



//    lifecycle
    val lifecycle_version = "2.7.0"
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
    implementation ("androidx.activity:activity-compose:1.8.2")

//For PickVisualMedia contract
    implementation("androidx.activity:activity-ktx:1.8.2")

    //Koin

}