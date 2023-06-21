plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
    kotlin("kapt")
}

android {
    namespace = "com.franco.demomode"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.franco.demomode"
        minSdk = 24
        targetSdk = 33
        versionName = "1.6"
        versionCode = 1910030257
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    lint {
        abortOnError = false
        checkAllWarnings = false
        disable += "InvalidPackage"
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt"
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.material)
    kapt(libs.androidx.lifecycle.compiler)
}
