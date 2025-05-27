plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.finalprojectappraisal"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finalprojectappraisal"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
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
    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
        }
    }


}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    //implementation(libs.firebase.auth)
    //implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    //implementation ("com.google.android.gms:play-services-identity:20.4.0")
    implementation("androidx.cardview:cardview:1.0.0")

// Firebase BoM
   // implementation(platform("com.google.firebase:firebase-bom:33.9.0"))

// Firebase core services
    //implementation("com.google.firebase:firebase-auth")
   // implementation("com.google.firebase:firebase-firestore")

   // implementation("com.google.protobuf:protobuf-javalite:3.25.1")

    //implementation ("com.google.android.gms:play-services-auth:21.3.0")


////

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")


    // Gemini AI
    implementation ("com.google.ai.client.generativeai:generativeai:0.2.1")

    // Google Cloud Storage
    implementation("com.google.cloud:google-cloud-storage:2.22.5")
    //    exclude(group= "com.google.protobuf", module= "protobuf-java")
    //}

    //implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0") //
    //    exclude(group= "com.google.api.grpc", module= "proto-google-common-protos")
   // }
    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Image loading
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // CameraX
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")

    // Testing
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")


}