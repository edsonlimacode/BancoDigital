plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")

}

android {
    namespace = "com.edsonlimadev.bancodigital"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.edsonlimadev.bancodigital"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //daggerhil
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation(libs.androidx.legacy.support.v4)
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.5")

    //mask
    implementation("io.github.vicmikhailau:MaskedEditText:5.0.2")

    //swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //https://github.com/cheonjaeung/shapedimageview
    implementation("io.woong.shapedimageview:shapedimageview:1.4.3")

    //https://square.github.io/picasso/
    implementation("com.squareup.picasso:picasso:2.8")

    //https://github.com/ParkSangGwon/TedPermission
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.4.2")

    //https://github.com/Ferfalk/SimpleSearchView
    implementation("com.github.Ferfalk:SimpleSearchView:0.2.1")
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}