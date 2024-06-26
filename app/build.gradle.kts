plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // For room
    id("com.google.devtools.ksp")

    // For hilt
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.vviiblue.pixelprobeqrdeluxe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vviiblue.pixelprobeqrdeluxe"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.vviiblue.pixelprobeqrdeluxe.utils.CustomTestRunner"
    }

    buildTypes {
        getByName("release") {
            resValue("string", "nombreDeLaApp", "PixelProbeQR")
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug"){
            resValue("string", "nombreDeLaApp", "[DEBUG] PixelProbeQR")
            isDebuggable = true
            resValue("string", "arisname", "[DEBUG] PixelProbeQR")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    kotlin{
        jvmToolchain(17)
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(libs.androidx.rules)
    val navVersion = "2.7.1"
    val roomVersion = "2.5.2"
    val cameraVersion = "1.2.3"
    val daggerHiltVersion = "2.48"

    // Navegation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Camera X
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-camera2:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")
    implementation("androidx.camera:camera-extensions:$cameraVersion")

    // Scan QR
    implementation("com.google.mlkit:barcode-scanning:17.0.0")

    // Generate QR
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")

    //Es una implementación mejorada de WebView, para la visualización de páginas web
    implementation ("androidx.webkit:webkit:1.4.0")


    // Database Room
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // DaggerHilt
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    kapt("com.google.dagger:hilt-compiler:$daggerHiltVersion")

    // Lifecycles
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Coroutines
 //   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")

    // Librerías de AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")

    //**UnitTesting
    testImplementation ("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation ("io.mockk:mockk:1.12.3")
    testImplementation("org.mockito:mockito-core:4.0.0")
    //Mockito
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    androidTestImplementation ("org.mockito:mockito-core:4.0.0")
    androidTestImplementation ("org.mockito:mockito-android:4.0.0")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")

    //Espresso
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.4.0")

    //Robolectric
    testImplementation("org.robolectric:robolectric:4.12.2")
    //Hamcrest
    testImplementation("org.hamcrest:hamcrest:2.2")

    //Fragment Testing
    debugImplementation("androidx.fragment:fragment-testing:1.7.1")

    //Arquitectura de componentes
    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")



}