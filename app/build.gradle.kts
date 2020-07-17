plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
//    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Sdk.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(Sdk.MIN_SDK_VERSION)
        targetSdkVersion(Sdk.TARGET_SDK_VERSION)

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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

    android.sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }
}

dependencies {

//    implementation(fileTree(org.gradle.internal.impldep.bsh.commands.dir: "libs", include: ["*.jar"]))

    implementation(kotlin("stdlib-jdk7"))
    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)
    implementation(SupportLibs.RECYCLER_VIEW)
    implementation(SupportLibs.ANDROIDX_LEGACY_SUPPORT)
    implementation(SupportLibs.FRAGMENT_KTX)
    implementation(SupportLibs.ACTIVITY_KTX)
    implementation(SupportLibs.BROWSER)
    implementation(Libraries.MATERIAL)
    implementation(Libraries.COIL)
    implementation(Libraries.CIRCLE_IMAGE_VIEW)
    implementation(Libraries.NAVIGATION_FRAGMENT)
    implementation(Libraries.NAVIGATION_UI)
    implementation(Libraries.LIFECYCLE_LIVEDATA)
    implementation(Libraries.LIFECYCLE_VIEWMODEL)
    implementation(Libraries.LIFECYCLE_VIEWMODEL_SAVEDSTATE)
    implementation(Libraries.KOTLIN_COROUTINES_CORE)
    implementation(Libraries.KOTLIN_COROUTINES_ANDROID)
    implementation(Libraries.OKHTTP)
    implementation(Libraries.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Libraries.RETROFIT)
    implementation(Libraries.RETROFIT_MOSHI_CONVERTER)
    implementation(Libraries.RETROFIT_COROUTINES_ADAPTER)
    implementation(Libraries.TIMBER)
    implementation(Libraries.WORK_MANAGER)

    testImplementation(TestingLib.JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}