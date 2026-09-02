plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// resources/key-layout.json is the single source of truth for the key inventory.
// It is copied into the app's assets at build time so no second copy is checked in.
//
// The copy is wired through the Variant API (androidComponents / addGeneratedSourceDirectory)
// rather than sourceSets[...].assets.srcDir(...): that call is deprecated in this plugin
// version, its Provider-taking overload has already become an error, and the Variant API is
// the route the plugin's own message recommends. It also restores the task dependency —
// the plugin runs the copy whenever the assets are merged — and the plugin, not this file,
// chooses the generated directory the task writes into.
abstract class CopyKeyLayoutConfig : DefaultTask() {
    @get:InputFile
    abstract val config: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun copy() {
        val target = outputDir.get().asFile
        target.mkdirs()
        config.get().asFile.copyTo(File(target, "key-layout.json"), overwrite = true)
    }
}

val copyKeyLayoutConfig = tasks.register<CopyKeyLayoutConfig>("copyKeyLayoutConfig") {
    description = "Copies resources/key-layout.json into the app's assets."
    config.set(rootProject.file("../resources/key-layout.json"))
}

androidComponents {
    onVariants { variant ->
        variant.sources.assets?.addGeneratedSourceDirectory(
            copyKeyLayoutConfig,
            CopyKeyLayoutConfig::outputDir
        )
    }
}

android {
    namespace = "tech.flintcraft.hexboard"
    compileSdk {
        version = release(37)
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
    // The app parses the key config it already ships in its assets, so Gson is a main
    // dependency rather than a test-only one.
    implementation(libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
