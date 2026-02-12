# AegisNav Development Quick Reference

## 📦 Project Initialization

```bash
# 1. Navigate to project
cd /home/hssn/Documents/AegisNAV

# 2. Make setup script executable
chmod +x setup.sh

# 3. Run setup
./setup.sh

# 4. Build project
./gradlew build

# 5. Install on emulator
./gradlew installDebug
```

---

## 🎯 Quick Build Commands

```bash
# Clean build
./gradlew clean build

# Fast build (incremental)
./gradlew build --build-cache

# Build and install
./gradlew installDebug

# Run tests
./gradlew test

# Check code style
./gradlew lint

# View gradle tasks
./gradlew tasks
```

---

## 📱 Emulator Commands

```bash
# List emulators
emulator -list-avds

# Start emulator
emulator -avd Pixel_4_API_34 &

# Install app
adb install app/build/outputs/apk/debug/app-debug.apk

# Run app
adb shell am start -n com.aegisnav/.MainActivity

# View logs
adb logcat -s AegisNav

# Clear app data
adb shell pm clear com.aegisnav
```

---

## 🗂️ File Locations

| Type | Path |
|------|------|
| Kotlin Code | `app/src/main/kotlin/com/aegisnav/` |
| Resources | `app/src/main/res/` |
| Colors | `app/src/main/res/values/colors.xml` |
| Strings | `app/src/main/res/values/strings.xml` |
| Dimensions | `app/src/main/res/values/dimens.xml` |
| Manifest | `app/src/main/AndroidManifest.xml` |
| Gradle Config | `app/build.gradle.kts` |
| Tests | `app/src/test/kotlin/com/aegisnav/` |
| UI Theme | `app/src/main/kotlin/com/aegisnav/ui/theme/` |

---

## 🎨 Color Customization

All colors are in `app/src/main/res/values/colors.xml`:

```xml
<color name="black_primary">#000000</color>
<color name="grey_500">#808080</color>
<color name="white">#ffffff</color>
<color name="success_green">#2d5016</color>
<color name="error_red">#5d1a1a</color>
```

Or in Compose theme `Color.kt`:

```kotlin
val Black = Color(0xFF000000)
val Grey500 = Color(0xFF808080)
val White = Color(0xFFffffff)
```

---

## 🧠 Architecture Overview

```
Presentation (Compose UI)
    ↓
ViewModels (MVVM)
    ↓
Services (Business Logic)
    ↓
Data Layer (Sensors, Database, Network)
    ↓
Models (Domain)
```

### Key Classes

- **MainActivity** - App entry point
- **AegisNavApplication** - Application class
- **NavigationScreen** - Main UI
- **NavigationViewModel** - State management
- **NavigationService** - Navigation logic
- **SensorFusionService** - Sensor data processing
- **MapService** - Offline maps
- **RoutingService** - Route calculation
- **VoiceGuidanceService** - Text-to-speech

---

## 🔧 Common Modifications

### Add a New Service

1. Create file: `app/src/main/kotlin/com/aegisnav/data/services/MyService.kt`
2. Add `@Singleton` and `@Inject`
3. Register in `di/Modules.kt`
4. Inject in ViewModel or Service

### Add a New UI Component

1. Create file: `app/src/main/kotlin/com/aegisnav/ui/components/MyComponent.kt`
2. Mark with `@Composable`
3. Use in screens

### Add String Resources

Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="my_string">Text here</string>
```

### Add Color

Edit `app/src/main/res/values/colors.xml`:
```xml
<color name="my_color">#FF0000</color>
```

---

## 🚨 Debugging

### View Logs
```bash
adb logcat -s AegisNav
adb logcat | grep AegisNav
```

### Timber Logging (already configured)
```kotlin
Timber.d("Debug message")
Timber.i("Info message")
Timber.e("Error message")
```

### Android Studio Debugging
1. Set breakpoint (click line number)
2. Run in debug: Shift+F9
3. Step through code: F10/F11
4. Inspect variables in Variables panel

### Memory Profiling
1. Android Studio → Profiler
2. Select app → Memory tab
3. Monitor heap usage

---

## 📊 Project Statistics

```
Languages:        Kotlin, XML
Total Lines:      3,000+
Kotlin Files:     25+
Resource Files:   10+
Dependencies:     45+
Min SDK:          26 (Android 8.0)
Target SDK:       34 (Android 14)
Compile SDK:      34
```

---

## 🔐 Release Build

```bash
# 1. Create keystore (first time only)
keytool -genkey -v -keystore aegisnav.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias aegisnav_key

# 2. Build signed APK
./gradlew bundleRelease

# 3. Find output
# app/build/outputs/bundle/release/app-release.aab
```

---

## 📋 Testing

### Unit Test Example
```kotlin
@Test
fun testSensorFusion() {
    val fusion = IMUFusion()
    val heading = fusion.getHeading()
    assertEquals(0f, heading)
}
```

### Run Tests
```bash
./gradlew test
```

### Integration Test
```bash
./gradlew connectedAndroidTest
```

---

## 📱 App Permissions

Requested in `AndroidManifest.xml`:
- ✓ ACCESS_FINE_LOCATION
- ✓ ACCESS_COARSE_LOCATION  
- ✓ INTERNET
- ✓ CAMERA
- ✓ RECORD_AUDIO
- ✓ READ_EXTERNAL_STORAGE
- ✓ WRITE_EXTERNAL_STORAGE

Handle runtime permissions in code:
```kotlin
val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
if (ContextCompat.checkSelfPermission(context, permission) 
    != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(activity, arrayOf(permission), 101)
}
```

---

## 🎯 Development Workflow

1. **Edit Code** → Kotlin files in `app/src/main/kotlin/`
2. **Build** → `./gradlew build`
3. **Test** → Run on emulator → `./gradlew installDebug`
4. **Debug** → Use Logcat and debugger
5. **Iterate** → Make changes, rebuild, test
6. **Release** → `./gradlew bundleRelease`

---

## 📚 Documentation Files

- **SETUP_GUIDE.md** - Detailed setup instructions
- **KOTLIN_DEVELOPMENT_STARTED.md** - Project status overview
- **KOTLIN_PROJECT_START.md** - Architecture details
- **design-system.md** - Design tokens
- **MODERN_UI_LAYOUT.md** - Layout specifications

---

## 💡 Tips & Best Practices

✅ Use Timber for logging  
✅ Use Coroutines for async operations  
✅ Use StateFlow for reactive UI  
✅ Keep services injection clean  
✅ Compose is preferred over XML layouts  
✅ Use data classes for models  
✅ Test sensor data thoroughly  
✅ Profile before optimizing  
✅ Follow Kotlin style guide  
✅ Keep UI off main thread  

---

## 🚀 You're All Set!

Everything is in place. Start development:

```bash
./gradlew build && ./gradlew installDebug
```

**Happy coding!** 🎉
