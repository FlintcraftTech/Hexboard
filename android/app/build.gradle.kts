plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Optional per-machine build output location. Google Drive syncing files Gradle is still
// writing, and Windows' 260-character path ceiling, both bite when the build output sits deep
// inside a synced project folder. Setting `hexboard.buildDir` in android/local.properties —
// which is gitignored, so it never reaches anyone who clones this — moves the output somewhere
// short and unsynced. Absent or unreadable, Gradle's default is left alone, so a fresh clone
// builds unchanged.
run {
    val localProperties = rootProject.file("local.properties")
    if (localProperties.isFile) {
        val configured = java.util.Properties().apply {
            localProperties.inputStream().use { load(it) }
        }.getProperty("hexboard.buildDir")?.trim()
        if (!configured.isNullOrEmpty()) {
            layout.buildDirectory.set(File(configured))
        }
    }
}

// The key-layout configs in resources/ are the single source of truth for the key inventory.
// They are copied into the app's assets at build time so no second copy is checked in — every
// key-layout*.json, so a further language reaches the app by adding one file and nothing else.
//
// The copy is filtered rather than wholesale. resources/ also holds the generated manifests and
// an images folder: the manifest is generated FROM a config for people to read, so shipping it
// would put a second copy of what the app already parses into the APK, and nothing reads the
// images. So the task takes key-layout*.json and leaves the rest.
//
// The copy is wired through the Variant API (androidComponents / addGeneratedSourceDirectory)
// rather than sourceSets[...].assets.srcDir(...): that call is deprecated in this plugin
// version, its Provider-taking overload has already become an error, and the Variant API is
// the route the plugin's own message recommends. It also restores the task dependency —
// the plugin runs the copy whenever the assets are merged — and the plugin, not this file,
// chooses the generated directory the task writes into.
abstract class CopyKeyLayoutConfig : DefaultTask() {
    @get:InputDirectory
    abstract val configDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun copy() {
        val target = outputDir.get().asFile
        target.mkdirs()
        val source = configDir.get().asFile
        val configs = source.listFiles { file ->
            file.isFile && file.name.startsWith("key-layout") && file.name.endsWith(".json")
        }.orEmpty() + listOfNotNull(
            // Unicode's published emoji list, which fills the emoji panels. Data rather than
            // config, and named exactly, so it travels with the configs without widening the
            // filter that keeps the manifests and images out.
            source.resolve("emoji-test.txt").takeIf { it.isFile }
        )
        // The generated assets directory is flat, so each config keeps its own filename and
        // that is what distinguishes one layout from another at runtime.
        configs.forEach { it.copyTo(File(target, it.name), overwrite = true) }
    }
}

val copyKeyLayoutConfig = tasks.register<CopyKeyLayoutConfig>("copyKeyLayoutConfig") {
    description = "Copies every resources/key-layout*.json, and Unicode's emoji-test.txt, into the app's assets."
    configDir.set(rootProject.file("../resources"))
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
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.savedstate)
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
