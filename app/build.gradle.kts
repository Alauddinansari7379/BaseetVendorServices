plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.amtech.vendorservices"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.amtech.vendorservices"
        minSdk = 24
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
        viewBinding = true
    }
}

dependencies {
   // implementation("com.google.android.gms:play-services-location:21.2.0")
    val nav_version = "2.5.2"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    implementation("com.google.android.material:material:1.11.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // (Retrofit dependencies)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    implementation ("com.squareup.picasso:picasso:2.71828")

    //okhttp3
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //Navigation Architecture Component
    //noinspection GradleDependency
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //SweetAlert
    implementation ("com.github.f0ris.sweetalert:library:1.6.2")
    //Circule image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //Counter Code Picker
  //  implementation ("com.hbb20:ccp:2.5.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Check Internet Connection
    implementation ("com.github.raheemadamboev:check-internet-android:1.1.1")
    implementation ("com.github.rrsaikat:ConnectionStatusLikeYoutube:v1.0.1")

    //shimmerlayout
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
   // implementation ("io.supercharge:shimmerlayout:2.1.0")

    //Searchable Spinner
    implementation ("com.github.DonMat:searchablespinner:v1.0.1")
//    implementation 'com.github.mudassar1:SearchableSpinner:v1.1'
//    implementation 'com.github.leoncydsilva:SearchableSpinner:1.0.1'
    implementation ("com.github.chivorns:smartmaterialspinner:1.5.0")

    //PDF Viewer
    implementation ("com.github.afreakyelf:Pdf-Viewer:v1.0.7")

    // Image Picker From Camera
    implementation ("com.github.dhaval2404:imagepicker:2.1")

    //Multiselection Spinner
    implementation ("com.github.AnuraganuPunalur:Multi-Selection-Spinner-Android:1.0")
    implementation ("com.github.telichada:SearchableMultiSelectSpinner:2.0")

    //Location
    implementation ("com.github.prabhat1707:EasyWayLocation:2.4")
    //Permission
    implementation ("com.guolindev.permissionx:permissionx:1.6.4")
    // Location API
    implementation ("com.google.android.gms:play-services-location:16.0.0")

//    implementation ("androidx.startup:startup-runtime:1.1.1")

    //Multiple Date Picker
    implementation ("com.aminography:primedatepicker:3.6.0")
    implementation ("com.aminography:primecalendar:1.7.0")
    //Multiselection Spinner
    implementation ("com.github.AnuraganuPunalur:Multi-Selection-Spinner-Android:1.0")
    implementation ("com.github.telichada:SearchableMultiSelectSpinner:2.0")

    //firebase
    implementation ("com.google.firebase:firebase-auth:21.1.0")
    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.android.gms:play-services-auth:20.4.0")
    implementation ("com.google.firebase:firebase-iid:21.1.0")
    implementation ("com.google.firebase:firebase-messaging:23.1.2")
    implementation ("com.google.firebase:firebase-appdistribution-api-ktx:16.0.0-beta05")
    implementation ("com.google.firebase:firebase-messaging:23.1.1")
    implementation ("com.google.firebase:firebase-analytics")
    // (Jitsi dependencies)
    implementation("org.jitsi.react:jitsi-meet-sdk:8.5.0")
  }


