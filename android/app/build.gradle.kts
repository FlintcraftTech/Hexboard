plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// resources/key-layout.json is the single source of truth for the key inventory.
// It is copied into the app's assets at build time so no second copy is checked in.
val keyLayoutConfig = rootProject.file("../resources/key-layout.json")
val generatedAssetsDir = layout.buildDirectory.dir("generated/keyLayoutAssets")

val copyKeyLayoutConfig by tasks.registering(Copy::class) {
    description = "Copies resources/key-layout.json into the app's assets."
    from(keyLayoutConfig)
    into(generatedAssetsDir)
}

tasks.named("preBuild") {
    dependsOn(copyKeyLayoutConfig)
}

android {
    namespace = "tech.flintcraft.hexboard"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "tech.flintcraft.hexboard"
        minSdk = 26
        targetSdk = 36
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
        compose = true
    }

    sourceSets["main"].assets.srcDir(generatedAssetsDir)

    testOptions {
        unitTests.all {
            // The key-config validator reads resources/key-layout.json from the repo,
            // so it needs the repo root rather than the module directory.
            it.systemProperty("hexboard.repoRoot", rootProject.file("..").absolutePath)
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.gson)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}