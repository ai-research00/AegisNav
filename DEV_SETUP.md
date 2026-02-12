# AegisNav Development Environment Setup
## Complete Setup Guide

---

## Prerequisites

### System Requirements
- **OS**: macOS 12+, Windows 10+, or Ubuntu 18.04+
- **RAM**: 8GB minimum (16GB recommended)
- **Disk**: 10GB free space
- **Internet**: Required for initial setup and dependencies

### Required Tools

#### 1. Flutter SDK
```bash
# Install Flutter (latest stable)
# https://flutter.dev/docs/get-started/install

# Verify installation
flutter --version
flutter doctor
```

#### 2. Android Development
```bash
# Install Android Studio
# https://developer.android.com/studio

# Install Android SDK
sdkmanager --list
sdkmanager "platform-tools" "platforms;android-34"
sdkmanager "build-tools;34.0.0"

# Set ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

#### 3. iOS Development (macOS only)
```bash
# Install Xcode
xcode-select --install

# Install CocoaPods
sudo gem install cocoapods

# Verify
xcode-select -p
```

#### 4. Git
```bash
# Install Git
# https://git-scm.com/download

git --version
```

#### 5. IDEs
- **VS Code** with Flutter extension
- **Android Studio** (comes with tools)
- **Xcode** (macOS)

---

## Project Setup

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/aegisnav.git
cd aegisnav
```

### 2. Get Dependencies
```bash
# Get Flutter packages
flutter pub get

# Get iOS pods
cd ios
pod install
cd ..
```

### 3. Generate Code
```bash
# Generate freezed models and go_router configs
flutter pub run build_runner build

# Watch mode (auto-regenerate)
flutter pub run build_runner watch
```

### 4. Configure Environment Files
```bash
# Create .env.development
echo "MAP_API_KEY=dev-key" > .env.development
echo "ROUTING_PROVIDER=graphhopper" >> .env.development

# Create local.properties (Android)
echo "sdk.dir=$ANDROID_HOME" > android/local.properties
```

---

## Development Workflow

### Running the App

```bash
# Run on Android
flutter run -d android

# Run on iOS
flutter run -d ios

# Run on web (for testing)
flutter run -d web

# Run with specific flavor
flutter run -t lib/main_dev.dart
```

### Building

```bash
# Android APK
flutter build apk --release

# Android App Bundle
flutter build appbundle --release

# iOS App
flutter build ios --release
```

### Testing

```bash
# Run all tests
flutter test

# Run tests with coverage
flutter test --coverage

# Run specific test file
flutter test test/unit/sensors/sensor_fusion_test.dart
```

### Code Quality

```bash
# Analyze code
flutter analyze

# Format code
flutter format lib/

# Run linter
dart run custom_lint

# Generate coverage report
flutter test --coverage && lcov --list coverage/lcov.info
```

---

## Project Structure Details

### `/lib` Directory Structure
```
lib/
├── main.dart                    # App entry point
├── main_dev.dart               # Development flavor
├── main_prod.dart              # Production flavor
│
├── core/
│   ├── constants/              # App-wide constants
│   ├── errors/                 # Error handling
│   ├── extensions/             # Dart extensions
│   ├── utils/                  # Utility functions
│   └── theme/                  # Global theme
│
├── data/
│   ├── datasources/            # Local/remote data sources
│   │   ├── local/
│   │   │   ├── sqlite_datasource.dart
│   │   │   └── sensor_datasource.dart
│   │   └── remote/
│   │       └── api_client.dart
│   ├── models/                 # Data models
│   │   ├── location_model.dart
│   │   ├── route_model.dart
│   │   ├── poi_model.dart
│   │   └── sensor_model.dart
│   ├── repositories/           # Repository implementations
│   │   ├── map_repository_impl.dart
│   │   ├── routing_repository_impl.dart
│   │   ├── calibration_repository_impl.dart
│   │   └── pois_repository_impl.dart
│   └── local/
│       ├── databases/          # SQLite schemas
│       └── migrations/         # DB migrations
│
├── domain/
│   ├── entities/               # Business logic entities
│   │   ├── location.dart
│   │   ├── route.dart
│   │   ├── turn_instruction.dart
│   │   └── ar_landmark.dart
│   ├── repositories/           # Repository abstractions
│   │   ├── map_repository.dart
│   │   ├── routing_repository.dart
│   │   └── calibration_repository.dart
│   └── usecases/              # Business logic
│       ├── calculate_route_usecase.dart
│       ├── start_navigation_usecase.dart
│       ├── calibrate_sensors_usecase.dart
│       └── get_visible_landmarks_usecase.dart
│
├── presentation/
│   ├── bloc/                   # BLoC state management
│   │   ├── navigation/
│   │   ├── sensor/
│   │   ├── map/
│   │   └── ar/
│   ├── pages/                  # Full screens
│   │   ├── navigation_page.dart
│   │   ├── map_page.dart
│   │   ├── calibration_page.dart
│   │   └── settings_page.dart
│   ├── widgets/                # Reusable components
│   │   ├── ar_camera_viewport.dart
│   │   ├── turn_instruction_card.dart
│   │   ├── compass_display.dart
│   │   ├── route_progress_bar.dart
│   │   ├── status_indicator.dart
│   │   └── status_bar.dart
│   ├── theme/                  # UI theme
│   │   ├── colors.dart
│   │   ├── typography.dart
│   │   ├── theme.dart
│   │   └── app_theme.dart
│   └── navigation/             # App routing
│       └── app_router.dart
│
├── services/                   # Core services
│   ├── sensor_fusion_service.dart
│   ├── navigation_service.dart
│   ├── map_service.dart
│   ├── routing_service.dart
│   ├── ar_guidance_service.dart
│   └── voice_guidance_service.dart
│
├── di/                        # Dependency injection
│   └── service_locator.dart
│
└── config/
    ├── flavor_config.dart
    └── app_config.dart
```

### `/test` Directory Structure
```
test/
├── unit/
│   ├── sensors/
│   │   ├── sensor_fusion_test.dart
│   │   ├── heading_calculation_test.dart
│   │   └── step_detection_test.dart
│   ├── routing/
│   │   ├── route_calculation_test.dart
│   │   └── waypoint_test.dart
│   ├── map/
│   │   ├── map_tile_loader_test.dart
│   │   └── poi_query_test.dart
│   └── ar/
│       ├── landmark_anchoring_test.dart
│       └── ar_visualization_test.dart
│
├── integration/
│   ├── navigation_flow_test.dart
│   ├── map_loading_test.dart
│   └── sensor_calibration_test.dart
│
└── fixtures/
    ├── mock_map_data.dart
    ├── mock_sensor_data.dart
    └── mock_routes.dart
```

---

## Dependencies

### Core Framework
```yaml
dependencies:
  flutter:
    sdk: flutter
  flutter_localizations:
    sdk: flutter

  # State Management
  flutter_bloc: ^8.1.0
  get_it: ^7.6.0
  
  # Navigation
  go_router: ^13.0.0
  
  # Networking
  http: ^1.1.0
  dio: ^5.3.0
  
  # Local Database
  sqflite: ^2.3.0
  path: ^1.8.0
  
  # Models & Serialization
  freezed_annotation: ^2.4.0
  json_serializable: ^6.7.0
  
  # Maps
  flutter_map: ^6.0.0
  map_launcher: ^3.0.0
  
  # AR (Platform specific)
  google_ar_core: ^1.0.0  # Android ARCore
  arcore_flutter_plugin: ^3.2.0
  
  # Sensors
  sensors_plus: ^5.0.0
  geolocator: ^9.0.0
  
  # Voice
  flutter_tts: ^0.14.0
  
  # Cryptography
  pointycastle: ^3.7.0
  
  # Utilities
  intl: ^0.19.0
  logger: ^2.0.0
  package_info_plus: ^7.0.0
```

### Dev Dependencies
```yaml
dev_dependencies:
  flutter_test:
    sdk: flutter
  flutter_lints: ^3.0.0
  
  # Code Generation
  build_runner: ^2.4.0
  freezed: ^2.4.0
  json_serializable: ^6.7.0
  
  # Testing
  mockito: ^5.4.0
  mocktail: ^1.0.0
  
  # Code Coverage
  coverage: ^7.2.0
  
  # Linting
  custom_lint: ^0.5.0
```

---

## CI/CD Pipeline

### GitHub Actions (.github/workflows/tests.yml)
```yaml
name: Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up Flutter
      uses: subosito/flutter-action@v2
      with:
        flutter-version: '3.16.0'
    
    - name: Get dependencies
      run: flutter pub get
    
    - name: Generate code
      run: flutter pub run build_runner build
    
    - name: Run tests
      run: flutter test --coverage
    
    - name: Upload coverage
      uses: codecov/codecov-action@v3
      with:
        files: ./coverage/lcov.info
```

### Pre-commit Hooks
```bash
#!/bin/bash
# .git/hooks/pre-commit

echo "Running Flutter format..."
flutter format --set-exit-if-changed lib test

echo "Running Flutter analyze..."
flutter analyze lib test

echo "Running tests..."
flutter test

if [ $? -ne 0 ]; then
  echo "Tests failed. Commit cancelled."
  exit 1
fi
```

---

## Debugging

### Debug Mode
```bash
# Run with verbose logging
flutter run -v

# Run with debug logging
flutter run --verbose --analyze-size
```

### Remote Debugging
```bash
# Connect Android device via USB
adb devices
flutter run -d <device_id>

# View logs
adb logcat | grep flutter
```

### DevTools
```bash
# Open DevTools
flutter pub global activate devtools
devtools

# Or open in browser after running app
# http://localhost:9100
```

---

## Performance Profiling

```bash
# Profile app performance
flutter run --profile

# Generate performance report
flutter run --profile | grep -i performance

# Memory profiling
# Use Android Studio Profiler or Xcode Instruments
```

---

## Release Checklist

- [ ] All tests passing (100% critical path)
- [ ] Code coverage ≥ 80%
- [ ] No lint warnings
- [ ] All TODOs addressed
- [ ] Performance profiling complete
- [ ] Security audit passed
- [ ] Documentation updated
- [ ] CHANGELOG updated
- [ ] Version bumped (semantic versioning)
- [ ] Build tested on real devices
- [ ] All features tested in GPS-denied environment

---

## Troubleshooting

### Issue: Flutter doctor shows errors
```bash
# Fully resolve issues
flutter doctor -v

# Reset Flutter
flutter clean
flutter pub get
```

### Issue: Build fails on iOS
```bash
cd ios
rm -rf Pods Podfile.lock
pod install
cd ..
flutter clean
flutter pub get
flutter run -d ios
```

### Issue: Sensors not working
```bash
# Check permissions in AndroidManifest.xml and Info.plist
# Test with sensor test app:
flutter pub run sensors_plus --test
```

### Issue: Map tiles not loading
```bash
# Check MBTiles file is present in /data/maps/
# Verify SQLite database integrity:
sqlite3 map.mbtiles ".tables"
```

This setup guide ensures a smooth development experience across all platforms and team members.
