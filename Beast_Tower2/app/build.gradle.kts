plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.rafael.beast_tower"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rafael.beast_tower"
        minSdk = 23
        targetSdk = 33
        versionCode = 6
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.mapbox.maps:android:10.16.0")
    implementation("com.mapbox.search:mapbox-search-android-ui:1.0.0-rc.6")
    implementation("com.mapbox.navigation:android:2.15.2")


    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1"){
        exclude("com.android.support", null)
        exclude(null, "appcompat-v7")
        exclude(null, "support-v4")
    }
}