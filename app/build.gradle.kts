plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
    kotlin("kapt")
}

android {
    namespace = "ir.alirezaivaz.demo_mode"
    compileSdk = 33

    defaultConfig {
        applicationId = "ir.alirezaivaz.demo_mode"
        minSdk = 24
        targetSdk = 33
        versionName = "1.0.0"
        versionCode = 100
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.material)
    implementation(libs.tablericons)
    kapt(libs.androidx.lifecycle.compiler)
}
