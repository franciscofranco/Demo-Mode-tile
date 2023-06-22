plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
    kotlin("kapt")
}

val githubUrl = "https://github.com/AlirezaIvaz/DemoMode"

android {
    namespace = "ir.alirezaivaz.demo_mode"
    compileSdk = 33

    defaultConfig {
        applicationId = "ir.alirezaivaz.demo_mode"
        minSdk = 24
        targetSdk = 33
        versionName = "1.0.0"
        versionCode = 100
        resourceConfigurations += arrayOf(
            "en", "fa"
        )
        buildConfigField(
            "String",
            "GITHUB_REPO_URL",
            "\"$githubUrl\""
        )
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

    flavorDimensions += "distributor"
    productFlavors {
        create("github") {
            dimension = "distributor"
            versionNameSuffix = "-GH"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"$githubUrl\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"$githubUrl/issues\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"https://github.com/AlirezaIvaz\""
            )
        }
        create("cafebazaar") {
            dimension = "distributor"
            versionNameSuffix = "-CB"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"https://cafebazaar.ir/app/${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"bazaar://details?id=${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"bazaar://collection?slug=by_author&aid=alirezaivaz\""
            )
        }
        create("myket") {
            dimension = "distributor"
            versionNameSuffix = "-MK"
            buildConfigField(
                "String",
                "DOWNLOAD_LINK",
                "\"https://myket.ir/app/${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "RATE_INTENT",
                "\"myket://comment?id=${defaultConfig.applicationId}\""
            )
            buildConfigField(
                "String",
                "APPS_INTENT",
                "\"myket://developer/${defaultConfig.applicationId}\""
            )
        }
    }

    buildFeatures {
        buildConfig = true
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
