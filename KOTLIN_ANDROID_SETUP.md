# AegisNav Kotlin - Android Development Started
## Complete Setup & Development Guide

---

## 🎉 Project Status

### ✅ Completed
- Complete Kotlin architecture and project structure
- Core domain models and data classes
- Sensor integration (Magnetometer, Gyroscope, Accelerometer)
- Sensor fusion algorithm (IMU + Complementary Filter)
- Core services (Navigation, Map, Routing, Voice, etc.)
- Dependency injection setup (Hilt)
- Complete Jetpack Compose UI theme system
- Reusable UI components
- Navigation screen with all layouts
- ViewModel and state management
- AndroidManifest configuration

### 📁 File Structure Ready
```
app/src/main/
├── kotlin/com/aegisnav/          ← All code files
├── res/                           ← Resources (strings, colors, icons)
└── AndroidManifest.xml
```

### 🚀 Ready for Development

---

## Step 1: Create Android Project

### Option A: Android Studio (Recommended)

1. **Create New Project**
   - Open Android Studio
   - File → New → New Android Project
   - Name: `AegisNav`
   - Package: `com.aegisnav`
   - Min SDK: API 26 (Android 8.0)
   - Target SDK: API 34 (Android 14)
   - Template: Empty Activity

2. **Enable Compose**
   - File → Project Structure
   - Android Gradle Plugin Version: 8.1.0
   - Gradle Version: 8.1
   - Check "Use Kotlin DSL for Gradle"

3. **Convert Activity to Compose**
   - Replace `MainActivity.kt` content with code from JETPACK_COMPOSE_UI.md

### Option B: Command Line

```bash
# Create project using Gradle
mkdir -p ~/projects/aegisnav
cd ~/projects/aegisnav

# Create gradle structure
mkdir -p app/src/main/kotlin/com/aegisnav
mkdir -p app/src/main/res/{values,drawable,mipmap}
mkdir -p app/src/test/kotlin
mkdir -p buildSrc/src/main/kotlin
```

---

## Step 2: Gradle Configuration

### build.gradle.kts (Root)

```kotlin
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
```

### app/build.gradle.kts

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.aegisnav"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aegisnav"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
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
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/proguard/androidx-*.pro",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        )
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Maps
    implementation("org.maplibre.gl:android:10.0.0")

    // Sensors
    implementation("androidx.sensor:sensor:1.0.0")

    // AR
    implementation("com.google.ar:core:1.42.0")

    // Voice (TTS)
    implementation("androidx.speech:speech:1.0.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
    
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}
```

### settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "aegisnav"
include(":app")
```

### gradle.properties

```properties
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m
org.gradle.parallel=true
org.gradle.caching=true

android.useAndroidX=true
android.nonTransitiveRClass=true
kotlin.code.style=official
```

---

## Step 3: Create Resources

### res/values/strings.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">AegisNav</string>
    <string name="app_description">Offline Navigation for GPS-Denied Environments</string>
    
    <!-- Navigation -->
    <string name="turn_left">Turn Left</string>
    <string name="turn_right">Turn Right</string>
    <string name="go_straight">Go Straight</string>
    <string name="uturn">Make a U-Turn</string>
    
    <!-- Status -->
    <string name="ready">Ready</string>
    <string name="navigating">Navigating</string>
    <string name="error">Error</string>
    <string name="calibrating">Calibrating</string>
    
    <!-- Actions -->
    <string name="start_navigation">Start Navigation</string>
    <string name="stop_navigation">Stop Navigation</string>
    <string name="calibrate">Calibrate</string>
    <string name="settings">Settings</string>
</resources>
```

### res/values/colors.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary_blue">#0E7AB5</color>
    <color name="accent_green">#00D084</color>
    <color name="warning_orange">#FF9500</color>
    <color name="error_red">#FF3B30</color>
    <color name="dark_bg">#0A0E27</color>
    <color name="surface_dark">#151B3D</color>
    <color name="text_primary">#FFFFFF</color>
    <color name="text_secondary">#A8B5D1</color>
    <color name="text_tertiary">#7A8AAF</color>
    <color name="border">#2D3A5C</color>
</resources>
```

### res/values/dimens.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="spacing_4">4dp</dimen>
    <dimen name="spacing_8">8dp</dimen>
    <dimen name="spacing_12">12dp</dimen>
    <dimen name="spacing_16">16dp</dimen>
    <dimen name="spacing_24">24dp</dimen>
    <dimen name="spacing_32">32dp</dimen>
    
    <dimen name="corner_small">4dp</dimen>
    <dimen name="corner_medium">8dp</dimen>
    <dimen name="corner_large">12dp</dimen>
    
    <dimen name="touch_target">44dp</dimen>
</resources>
```

---

## Step 4: Copy Implementation Files

### From KOTLIN_IMPLEMENTATION.md:
1. Copy all Domain Models (Location.kt, Route.kt, etc.)
2. Copy all Sensor Classes (CompassSensor.kt, etc.)
3. Copy all Service Classes
4. Copy DI Setup (AppModule.kt, etc.)
5. Copy AegisNavApplication.kt and MainActivity.kt

### From JETPACK_COMPOSE_UI.md:
1. Copy all Theme files (Color.kt, Type.kt, Theme.kt)
2. Copy all UI Components
3. Copy NavigationScreen.kt

### File Structure After Copying:
```
app/src/main/kotlin/com/aegisnav/
├── AegisNavApplication.kt
├── di/
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   └── ...
├── domain/
│   ├── model/
│   │   ├── Location.kt
│   │   ├── Route.kt
│   │   ├── TurnInstruction.kt
│   │   ├── POI.kt
│   │   ├── CalibrationStatus.kt
│   │   ├── NavigationState.kt
│   │   └── HeadingData.kt
│   └── usecase/
│       └── ...
├── data/
│   ├── local/
│   ├── remote/
│   └── repository/
├── presentation/
│   ├── MainActivity.kt
│   ├── ui/
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Type.kt
│   │   │   └── Theme.kt
│   │   ├── component/
│   │   │   ├── AegisCard.kt
│   │   │   ├── TurnInstructionCard.kt
│   │   │   ├── CompassDisplay.kt
│   │   │   └── RouteProgressBar.kt
│   │   └── screen/
│   │       └── NavigationScreen.kt
│   ├── viewmodel/
│   │   ├── NavigationViewModel.kt
│   │   └── ...
│   └── state/
│       └── NavigationUiState.kt
├── service/
│   ├── SensorFusionService.kt
│   ├── NavigationService.kt
│   ├── MapService.kt
│   ├── RoutingService.kt
│   ├── VoiceGuidanceService.kt
│   └── ...
└── sensor/
    ├── CompassSensor.kt
    ├── GyroSensor.kt
    ├── AccelerometerSensor.kt
    └── IMUFusion.kt
```

---

## Step 5: Build & Run

```bash
# Navigate to project directory
cd ~/path/to/aegisnav

# Clean build
./gradlew clean

# Build APK
./gradlew assembleDebug

# Install on emulator/device
./gradlew installDebug

# View logs
adb logcat | grep AegisNav

# Run tests
./gradlew test
```

---

## Step 6: Android Emulator Setup

```bash
# List available emulators
emulator -list-avds

# Create new emulator (if needed)
# Android Studio → Device Manager → Create Device
# Select: Pixel 4 or higher
# API Level: 34 (Android 14)
# GPU Acceleration: Enabled

# Start emulator
emulator -avd Pixel_4_API_34 &

# Verify connection
adb devices
```

---

## Step 7: Verify Installation

```bash
# Check Android SDK
sdkmanager --list_installed

# Verify Kotlin
kotlinc -version

# Test Gradle
./gradlew --version

# Run Android Studio
# File → Settings → SDK Manager
# Install:
#   - Android SDK Platform 34
#   - Build Tools 34.0.0
#   - Android Emulator
#   - Android SDK Tools
```

---

## Development Workflow

### Daily Development
```bash
# 1. Start emulator
emulator -avd Pixel_4_API_34 &

# 2. Build and install
./gradlew installDebug

# 3. Run app
adb shell am start -n com.aegisnav/.presentation.MainActivity

# 4. View logs
adb logcat -s AegisNav

# 5. Make changes → rebuild
./gradlew installDebug

# 6. Run tests
./gradlew test
```

### Code Quality
```bash
# Lint check
./gradlew lint

# Format code
./gradlew spotlessApply

# Run tests with coverage
./gradlew jacocoTestReport
```

### Release Build
```bash
# Create release APK
./gradlew bundleRelease

# Sign APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
  -keystore ~/keystore.jks app/build/outputs/bundle/release/app-release.aab
```

---

## Common Commands Reference

```bash
# Build
./gradlew build              # Full build
./gradlew assembleDebug      # Debug APK
./gradlew bundleRelease      # Release bundle

# Install
./gradlew installDebug       # Install debug APK
./gradlew uninstallAll       # Uninstall all

# Run & Test
./gradlew run               # Run app
./gradlew test              # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests

# Clean
./gradlew clean             # Clean build

# Logs
adb logcat                  # All logs
adb logcat -s AegisNav      # App logs only
adb logcat -c               # Clear logs

# Debugging
./gradlew tasks             # List available tasks
./gradlew dependencies      # Show dependencies
./gradlew --scan            # Generate build report
```

---

## Troubleshooting

### Issue: "Gradle daemon not responding"
```bash
./gradlew --stop
./gradlew clean build
```

### Issue: "Kotlin compiler error"
```bash
# Update Kotlin
./gradlew -PkotlinVersion=1.9.0 build

# Clear caches
rm -rf ~/.gradle
./gradlew clean build
```

### Issue: "Cannot find Android SDK"
```bash
# Set ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### Issue: "Emulator won't start"
```bash
# Check available emulators
emulator -list-avds

# Start with verbose output
emulator -avd Pixel_4_API_34 -verbose

# Force delete and recreate
rm -rf ~/.android/avd
# Android Studio → Device Manager → Create New Device
```

---

## Next Phases

### Phase 2: Offline Maps Integration (Week 2-3)
- [ ] Integrate MapLibre GL
- [ ] Load MBTiles map files
- [ ] Implement map rendering
- [ ] Add zoom/pan controls
- [ ] Test map loading performance

### Phase 3: Routing Engine (Week 3-4)
- [ ] Integrate GraphHopper
- [ ] Implement route calculation
- [ ] Add waypoint management
- [ ] Test routing accuracy

### Phase 4: AR Integration (Week 4-5)
- [ ] Set up ARCore
- [ ] Implement camera feed
- [ ] Add landmark anchoring
- [ ] Create AR overlays

### Phase 5: Testing & Optimization (Week 6+)
- [ ] Unit tests for services
- [ ] Integration tests
- [ ] Field testing
- [ ] Performance optimization
- [ ] Security hardening

---

## Resources

- **Android Studio**: https://developer.android.com/studio
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **Kotlin**: https://kotlinlang.org
- **Gradle**: https://gradle.org
- **Hilt DI**: https://dagger.dev/hilt/
- **Room Database**: https://developer.android.com/training/data-storage/room

---

## Success Checklist

- [ ] Android Studio installed
- [ ] Project created with proper structure
- [ ] build.gradle.kts configured
- [ ] All dependencies installed
- [ ] Domain models created
- [ ] Services implemented
- [ ] UI components created
- [ ] App builds without errors
- [ ] App runs on emulator
- [ ] Sensors initialize correctly
- [ ] All tests pass

---

**Status**: 🟢 Kotlin Android Development Started  
**Next**: Build and run the first version  
**Timeline**: Phase 1 Complete (3 weeks) → Production ready (26 weeks)  

Let's build! 🚀
