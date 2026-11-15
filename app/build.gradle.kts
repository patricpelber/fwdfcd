plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "com.example.festivalwidget"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.festivalwidget"
        minSdk = 26; targetSdk = 34; versionCode = 1; versionName = "0.1"
    }
    buildTypes { release { isMinifyEnabled = true; isShrinkResources = true
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt")) } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
// Zero dependencies on purpose.
