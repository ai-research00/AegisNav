# AegisNav Android (Kotlin)
## Native Mobile Navigation App - Development Started

---

## 🎯 Project Overview

**AegisNav** is now a **native Android application** built with Kotlin, designed for GPS-denied navigation using offline maps, sensor fusion, and AR guidance.

### Technology Stack (Kotlin/Android)
- **Language**: Kotlin 1.9+
- **UI Framework**: Jetpack Compose (modern declarative UI)
- **Architecture**: MVVM with Clean Architecture
- **State Management**: ViewModel + StateFlow
- **DI Container**: Hilt
- **Maps**: MapLibre GL Android
- **Sensors**: Android Sensor Framework
- **AR**: ARCore
- **Database**: Room (SQLite wrapper)
- **Networking**: Retrofit + OkHttp (for map downloads)
- **Serialization**: Kotlinx Serialization
- **Testing**: JUnit 4, Mockito, Espresso

---

## 📁 Project Structure

```
aegisnav/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/aegisnav/
│   │   │   │   ├── AegisNavApplication.kt          # App entry point
│   │   │   │   │
│   │   │   │   ├── di/                             # Dependency injection
│   │   │   │   │   ├── AppModule.kt
│   │   │   │   │   ├── NetworkModule.kt
│   │   │   │   │   ├── DatabaseModule.kt
│   │   │   │   │   └── SensorModule.kt
│   │   │   │   │
│   │   │   │   ├── core/
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── Extensions.kt
│   │   │   │   │   ├── Result.kt
│   │   │   │   │   └── utils/
│   │   │   │   │       ├── Logger.kt
│   │   │   │   │       └── Converters.kt
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── database/
│   │   │   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   │   │   ├── RouteDao.kt
│   │   │   │   │   │   │   │   ├── POIDao.kt
│   │   │   │   │   │   │   │   └── CalibrationDao.kt
│   │   │   │   │   │   │   └── entity/
│   │   │   │   │   │   │       ├── RouteEntity.kt
│   │   │   │   │   │   │       ├── POIEntity.kt
│   │   │   │   │   │   │       └── CalibrationEntity.kt
│   │   │   │   │   │   ├── preferences/
│   │   │   │   │   │   │   └── AppPreferences.kt
│   │   │   │   │   │   └── cache/
│   │   │   │   │   │       └── MapTileCache.kt
│   │   │   │   │   │
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── api/
│   │   │   │   │   │   │   ├── MapDownloadApi.kt
│   │   │   │   │   │   │   ├── RoutingApi.kt
│   │   │   │   │   │   │   └── PoiApi.kt
│   │   │   │   │   │   └── dto/
│   │   │   │   │   │       ├── RouteDto.kt
│   │   │   │   │   │       └── PoiDto.kt
│   │   │   │   │   │
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── MapRepository.kt
│   │   │   │   │   │   ├── NavigationRepository.kt
│   │   │   │   │   │   ├── SensorRepository.kt
│   │   │   │   │   │   ├── RoutingRepository.kt
│   │   │   │   │   │   ├── POIRepository.kt
│   │   │   │   │   │   └── CalibrationRepository.kt
│   │   │   │   │   │
│   │   │   │   │   └── mapper/
│   │   │   │   │       ├── RouteMapper.kt
│   │   │   │   │       ├── POIMapper.kt
│   │   │   │   │       └── SensorMapper.kt
│   │   │   │   │
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Location.kt
│   │   │   │   │   │   ├── Route.kt
│   │   │   │   │   │   ├── TurnInstruction.kt
│   │   │   │   │   │   ├── POI.kt
│   │   │   │   │   │   ├── Landmark.kt
│   │   │   │   │   │   ├── SensorData.kt
│   │   │   │   │   │   ├── HeadingData.kt
│   │   │   │   │   │   ├── CalibrationStatus.kt
│   │   │   │   │   │   └── NavigationState.kt
│   │   │   │   │   │
│   │   │   │   │   └── usecase/
│   │   │   │   │       ├── navigation/
│   │   │   │   │       │   ├── StartNavigationUseCase.kt
│   │   │   │   │       │   ├── GetNavProgressUseCase.kt
│   │   │   │   │       │   └── RecalculateRouteUseCase.kt
│   │   │   │   │       ├── sensor/
│   │   │   │   │       │   ├── GetHeadingUseCase.kt
│   │   │   │   │       │   ├── CalibrateCompassUseCase.kt
│   │   │   │   │       │   └── DetectStepsUseCase.kt
│   │   │   │   │       ├── map/
│   │   │   │   │       │   ├── LoadMapRegionUseCase.kt
│   │   │   │   │       │   ├── QueryPOIsUseCase.kt
│   │   │   │   │       │   └── GetMapCoverageUseCase.kt
│   │   │   │   │       └── routing/
│   │   │   │   │           ├── CalculateRouteUseCase.kt
│   │   │   │   │           └── QueryLandmarksUseCase.kt
│   │   │   │   │
│   │   │   │   ├── presentation/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   │
│   │   │   │   │   ├── ui/
│   │   │   │   │   │   ├── theme/
│   │   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   │   ├── Type.kt
│   │   │   │   │   │   │   └── Theme.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── screen/
│   │   │   │   │   │   │   ├── NavigationScreen.kt
│   │   │   │   │   │   │   ├── MapScreen.kt
│   │   │   │   │   │   │   ├── CalibrationScreen.kt
│   │   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   │   └── SplashScreen.kt
│   │   │   │   │   │   │
│   │   │   │   │   │   └── component/
│   │   │   │   │   │       ├── ARCameraViewport.kt
│   │   │   │   │   │       ├── TurnInstructionCard.kt
│   │   │   │   │   │       ├── CompassDisplay.kt
│   │   │   │   │   │       ├── RouteProgressBar.kt
│   │   │   │   │   │       ├── StatusIndicator.kt
│   │   │   │   │   │       ├── StatusBar.kt
│   │   │   │   │   │       └── BottomNavigation.kt
│   │   │   │   │   │
│   │   │   │   │   ├── viewmodel/
│   │   │   │   │   │   ├── NavigationViewModel.kt
│   │   │   │   │   │   ├── SensorViewModel.kt
│   │   │   │   │   │   ├── MapViewModel.kt
│   │   │   │   │   │   ├── ARViewModel.kt
│   │   │   │   │   │   └── CalibrationViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   └── state/
│   │   │   │   │       ├── NavigationUiState.kt
│   │   │   │   │       ├── SensorUiState.kt
│   │   │   │   │       ├── MapUiState.kt
│   │   │   │   │       └── ARUiState.kt
│   │   │   │   │
│   │   │   │   ├── service/
│   │   │   │   │   ├── SensorFusionService.kt      # Heading calculation
│   │   │   │   │   ├── NavigationService.kt        # Route management
│   │   │   │   │   ├── MapService.kt               # Offline map loading
│   │   │   │   │   ├── RoutingService.kt           # Route calculation
│   │   │   │   │   ├── ARGuidanceService.kt        # AR overlays
│   │   │   │   │   ├── VoiceGuidanceService.kt     # TTS
│   │   │   │   │   ├── CalibrationService.kt       # Compass calibration
│   │   │   │   │   └── LocationTrackingService.kt  # Position estimation
│   │   │   │   │
│   │   │   │   └── sensor/
│   │   │   │       ├── CompassSensor.kt            # Magnetometer
│   │   │   │       ├── GyroSensor.kt               # Gyroscope
│   │   │   │       ├── AccelerometerSensor.kt      # Accelerometer
│   │   │   │       └── IMUFusion.kt                # Sensor fusion algorithm
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── ic_launcher.xml
│   │   │   │   │   ├── ic_turn_left.xml
│   │   │   │   │   ├── ic_turn_right.xml
│   │   │   │   │   ├── ic_compass.xml
│   │   │   │   │   └── ...
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── dimens.xml
│   │   │   │   ├── values-night/
│   │   │   │   │   └── colors.xml
│   │   │   │   └── mipmap/
│   │   │   │       └── ic_launcher.*
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/
│   │       ├── kotlin/com/aegisnav/
│   │       │   ├── sensor/
│   │       │   │   ├── CompassSensorTest.kt
│   │       │   │   └── IMUFusionTest.kt
│   │       │   ├── service/
│   │       │   │   ├── SensorFusionServiceTest.kt
│   │       │   │   └── RoutingServiceTest.kt
│   │       │   ├── domain/
│   │       │   │   └── usecase/
│   │       │   │       └── CalculateRouteUseCaseTest.kt
│   │       │   └── viewmodel/
│   │       │       └── NavigationViewModelTest.kt
│   │       └── resources/
│   │           ├── mock_sensor_data.json
│   │           └── mock_routes.json
│   │
│   └── build.gradle.kts
│
├── buildSrc/
│   ├── src/main/kotlin/
│   │   └── Dependencies.kt                   # Centralized dependency versions
│   └── build.gradle.kts
│
├── build.gradle.kts                          # Root build config
├── settings.gradle.kts                       # Project structure
├── gradle.properties                         # Gradle properties
│
└── docs/
    ├── ARCHITECTURE.md
    ├── DEVELOPMENT.md
    └── API_REFERENCE.md
```

---

## 🚀 Development Status

### Phase 1: Project Setup & Infrastructure ✅ Starting
- [ ] Android project initialization
- [ ] Gradle configuration
- [ ] Dependency setup
- [ ] DI container setup
- [ ] Database initialization
- [ ] Preferences setup

### Phase 2: Sensor Integration (Next)
- [ ] Sensor access implementation
- [ ] Sensor fusion algorithm
- [ ] Compass calibration
- [ ] Step detection

### Phase 3: Offline Maps & Routing
- [ ] Map loading from MBTiles
- [ ] Map rendering
- [ ] Route calculation
- [ ] POI querying

### Phase 4: AR Integration
- [ ] ARCore setup
- [ ] Camera feed
- [ ] Landmark anchoring
- [ ] Turn visualization

### Phase 5: Navigation UI
- [ ] Navigation screen
- [ ] Status panels
- [ ] Responsive layout
- [ ] Bottom navigation

---

## ⚡ Quick Start Development

```bash
# Clone the project
git clone https://github.com/yourusername/aegisnav.git
cd aegisnav

# Build the project
./gradlew build

# Run on emulator/device
./gradlew installDebug
adb shell am start -n com.aegisnav/.presentation.MainActivity

# Run tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

---

## 📦 Key Dependencies

```kotlin
// Core Android
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.11.0

// Jetpack Compose
androidx.compose.ui:ui:1.6.0
androidx.compose.material3:material3:1.1.0
androidx.activity:activity-compose:1.8.0

// Architecture
androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2
androidx.lifecycle:lifecycle-runtime-ktx:2.6.2

// Dependency Injection
com.google.dagger:hilt-android:2.48
com.google.dagger:hilt-compiler:2.48

// Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1
androidx.room:room-compiler:2.6.1

// Maps
org.maplibre.gl:android:10.0.0

// Sensors
androidx.sensor.sensor:sensor:1.0.0

// AR
com.google.ar:core:1.42.0

// Voice
com.google.android.tts:tts:1.0.0

// Serialization
org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0

// Testing
junit:junit:4.13.2
androidx.test.espresso:espresso-core:3.5.1
org.mockito.kotlin:mockito-kotlin:5.1.0
```

---

## 🔧 Build Configuration

### build.gradle.kts (Root)
```kotlin
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
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
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    // [All dependencies listed above]
}

kapt {
    correctErrorTypes = true
}
```

---

## 🎨 Jetpack Compose Theme

Core design system is **ready in Kotlin** with modern Android patterns.

```kotlin
// Colors (from design-system.md)
private val PrimaryBlue = Color(0xFF0E7AB5)
private val AccentGreen = Color(0xFF00D084)
private val WarningOrange = Color(0xFFFF9500)
private val ErrorRed = Color(0xFFFF3B30)
private val DarkBg = Color(0xFF0A0E27)

// Typography
private val AegisNavTypography = Typography(
    displayLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    headlineSmall = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    labelMedium = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium),
)

// Theme
@Composable
fun AegisNavTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = PrimaryBlue,
        secondary = AccentGreen,
        tertiary = WarningOrange,
        error = ErrorRed,
        background = DarkBg,
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AegisNavTypography,
        content = content
    )
}
```

---

## 🔐 Security

- ✅ Encrypted SharedPreferences for sensitive data
- ✅ Hardware-backed keystore for encryption keys
- ✅ Minimal permissions (sensors, storage only)
- ✅ No internet permission for core functionality
- ✅ Encrypted database with SQLCipher

---

## 📱 Target Devices

- **Minimum Android**: 8.0 (API 26)
- **Target Android**: 14 (API 34)
- **Sensors Required**: Magnetometer, Gyroscope, Accelerometer
- **AR**: ARCore compatible devices

---

## ✨ Next Steps

1. Initialize Android Studio project from this structure
2. Set up gradle build system
3. Implement core services (Phase 2)
4. Add sensor integration
5. Build UI with Jetpack Compose
6. Integrate offline maps
7. Add AR guidance
8. Comprehensive testing
9. Security hardening
10. App store release

---

**Status**: 🟢 Ready for Kotlin Implementation  
**Next Phase**: Phase 1 - Android Project Setup  
**Estimated Duration**: 26 weeks to production  

Proceed to: `KOTLIN_IMPLEMENTATION.md` for detailed development guide.
