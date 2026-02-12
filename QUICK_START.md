# AegisNav Quick Start Guide
## Get Running in 30 Minutes

---

## Prerequisites Checklist

Before starting, ensure you have:

- [ ] **Flutter SDK** installed (v3.16+)
- [ ] **Git** installed
- [ ] **Android Studio** with Android SDK (for Android development)
- [ ] **Xcode** (for iOS development on macOS)
- [ ] **VS Code** or preferred IDE
- [ ] **8GB+ RAM** available
- [ ] **2GB free disk space** for project

**Installation Time: 45 minutes**

---

## Step 1: Verify Installation (5 min)

```bash
# Check Flutter
flutter --version
# Should show: Flutter 3.16.0 or higher

# Check Dart
dart --version
# Should show: Dart 3.22+ (comes with Flutter)

# Full health check
flutter doctor
# Should show all checkmarks except possibly iOS (if on Linux/Windows)
```

If `flutter doctor` shows issues:
- Follow the suggestions printed to fix each issue
- Re-run `flutter doctor` until resolved

---

## Step 2: Create Flutter Project (5 min)

```bash
# Navigate to where you want the project
cd ~/Documents

# Create new Flutter app
flutter create aegisnav --template app

# Navigate into project
cd aegisnav
```

### Verify Project Structure
```bash
ls -la
# You should see:
# - android/
# - ios/
# - lib/
# - web/
# - pubspec.yaml
# - etc.
```

---

## Step 3: Add Dependencies (3 min)

Open `pubspec.yaml` and update the `dependencies` section:

```yaml
dependencies:
  flutter:
    sdk: flutter
  flutter_bloc: ^8.1.0
  get_it: ^7.6.0
  go_router: ^13.0.0
  sqflite: ^2.3.0
  path: ^1.8.0
  freezed_annotation: ^2.4.0
  sensors_plus: ^5.0.0
  flutter_tts: ^0.14.0

dev_dependencies:
  flutter_test:
    sdk: flutter
  build_runner: ^2.4.0
  freezed: ^2.4.0
  json_serializable: ^6.7.0
  mockito: ^5.4.0
```

Then run:
```bash
flutter pub get
```

---

## Step 4: Create Project Structure (3 min)

Create the directory structure (copy from PROJECT_STRUCTURE.md):

```bash
mkdir -p lib/{core,data,domain,presentation,services,di,config}
mkdir -p lib/presentation/{bloc,pages,widgets,theme,navigation}
mkdir -p lib/data/{datasources,models,repositories}
mkdir -p lib/domain/{entities,repositories,usecases}
mkdir -p test/{unit,integration,fixtures}

# Verify structure
tree lib  # or: ls -R lib
```

---

## Step 5: Create Design System (5 min)

Create `lib/design/colors.dart`:
```dart
// Copy from UI_COMPONENTS.md - AegisNav Colors class
// File: lib/design/colors.dart
```

Create `lib/design/typography.dart`:
```dart
// Copy from UI_COMPONENTS.md - AegisNav Typography class
// File: lib/design/typography.dart
```

Create `lib/design/theme.dart`:
```dart
// Copy from UI_COMPONENTS.md - buildAegisNavTheme() function
// File: lib/design/theme.dart
```

---

## Step 6: Create Main App File (3 min)

Create `lib/main.dart`:
```dart
import 'package:flutter/material.dart';
import 'design/theme.dart';

void main() {
  runApp(const AegisNavApp());
}

class AegisNavApp extends StatelessWidget {
  const AegisNavApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'AegisNav',
      theme: buildAegisNavTheme(),
      home: const Scaffold(
        body: Center(
          child: Text('AegisNav - Offline Navigation'),
        ),
      ),
    );
  }
}
```

---

## Step 7: Run the App (3 min)

```bash
# List available devices
flutter devices

# Run on Android emulator
flutter run -d emulator-5554

# OR run on iOS simulator (macOS only)
flutter run -d iPhone

# OR run on connected physical device
flutter run

# OR run in debug mode with hot reload
flutter run --debug
```

**You should see**: App launches with "AegisNav - Offline Navigation" text

---

## Step 8: Test Hot Reload (2 min)

In `lib/main.dart`, change the text to `'AegisNav - Ready!'`

Save the file → Press `R` in the terminal → **App updates without restarting!**

This is hot reload. Use it constantly during development.

---

## What You've Built So Far

✅ Flutter project initialized  
✅ Design system (colors, typography, theme)  
✅ Main app structure  
✅ App running on device/emulator  
✅ Hot reload working  

**Total Time: 30 minutes**

---

## Next Steps (Immediate)

### Phase 1: Create Navigation UI (Next 2-3 hours)

1. Copy `AegisCard` component from UI_COMPONENTS.md
2. Copy `TurnInstructionCard` from UI_COMPONENTS.md
3. Copy `CompassDisplay` from UI_COMPONENTS.md
4. Implement `NavigationScreen` from UI_COMPONENTS.md
5. Replace placeholder in `main.dart` with `NavigationScreen()`

### Phase 2: Create Core Services (Next 4-5 hours)

1. Create abstract service interfaces (from TECHNICAL_ARCHITECTURE.md)
2. Implement sensor fusion service stubs
3. Implement navigation service stubs
4. Implement map service stubs
5. Create service locator in `lib/di/service_locator.dart`

### Phase 3: Integrate Sensors (Next 6-8 hours)

1. Request sensor permissions in AndroidManifest.xml and Info.plist
2. Implement native sensor reading using `sensors_plus`
3. Implement heading calculation algorithm
4. Create compass reading stream
5. Test heading accuracy

---

## Common Commands Reference

```bash
# Development
flutter run                          # Run app
flutter run -v                       # Run with verbose logging
flutter run --profile               # Run in profile mode (optimized)
flutter run --release               # Run in release mode (fastest)

# Hot Features
r                                   # Hot reload (in running app)
R                                   # Hot restart (in running app)

# Testing
flutter test                        # Run all tests
flutter test --coverage            # Run tests with coverage report
flutter test test/unit/            # Run tests in specific directory

# Code Quality
flutter analyze                     # Check for lint issues
flutter format lib/                # Format all files
flutter pub outdated               # Check for outdated packages

# Building
flutter build apk --release        # Build Android APK
flutter build appbundle --release  # Build Android App Bundle
flutter build ios --release        # Build iOS app
flutter build web                  # Build web version

# Project
flutter doctor                      # Verify environment setup
flutter devices                     # List connected devices
flutter clean                       # Clean build artifacts
flutter pub get                     # Get dependencies
flutter pub upgrade                 # Update dependencies

# Debugging
flutter devices -v                  # Verbose device info
flutter attach                      # Attach to running app
```

---

## Troubleshooting

### Issue: "flutter: command not found"
**Solution**: Add Flutter to PATH
```bash
export PATH="$PATH:[path-to-flutter]/bin"
# Add this line to ~/.bashrc or ~/.zshrc to make permanent
```

### Issue: Android emulator won't start
**Solution**: Use Android Studio to create/manage emulator
```bash
# Or from command line:
emulator -list-avds          # List available emulators
emulator -avd Pixel_4_API_33 # Start specific emulator
```

### Issue: iOS build fails with CocoaPods error
**Solution**: Reinstall pods
```bash
cd ios
rm -rf Pods Podfile.lock
pod install
cd ..
flutter clean
flutter run -d ios
```

### Issue: Build fails with "Duplicate class"
**Solution**: Clean build
```bash
flutter clean
flutter pub get
flutter run
```

### Issue: Hot reload not working
**Solution**: Use hot restart
```bash
# In the running app terminal:
R  # (capital R for hot restart)
```

---

## Testing Checklist

Once app is running:

- [ ] **Navigation Screen Displays**: See AR camera placeholder + turn instruction card
- [ ] **Status Bar Works**: See time, battery, compass icon
- [ ] **Bottom Navigation**: See 4 buttons (Map, Options, Calibrate, Help)
- [ ] **Theme Applied**: Dark background, correct colors
- [ ] **Hot Reload Works**: Change text → press R → updates instantly
- [ ] **No Console Errors**: Terminal shows no exceptions
- [ ] **Responsive Layout**: Rotate device → layout adjusts
- [ ] **Touch Responsive**: Can tap buttons (they highlight)

---

## Development Workflow

### Daily Workflow
```bash
# Start day
flutter pub get           # Get any new dependencies
flutter run               # Run the app

# During development
# ... edit files, use hot reload (press R)

# Before commit
flutter analyze           # Check for issues
flutter format lib/       # Format code
flutter test              # Run tests
git add .
git commit -m "Feature: description"
git push
```

### Best Practices

1. **Use Hot Reload**: Don't restart the app, press `R` in terminal
2. **Test Frequently**: Run `flutter test` before committing
3. **Format Code**: Always `flutter format` before commits
4. **Analyze First**: Run `flutter analyze` to catch issues early
5. **Use DevTools**: Open with `flutter pub global run devtools` for debugging

---

## Key Files to Modify

During Phase 1-2, you'll modify these main files:

```
lib/
├── main.dart                    # App entry point
├── design/
│   ├── colors.dart             # Copy from UI_COMPONENTS.md
│   ├── typography.dart         # Copy from UI_COMPONENTS.md
│   └── theme.dart              # Copy from UI_COMPONENTS.md
├── presentation/
│   ├── widgets/
│   │   ├── aegis_card.dart
│   │   ├── turn_instruction_card.dart
│   │   ├── compass_display.dart
│   │   └── route_progress_bar.dart
│   └── pages/
│       └── navigation_page.dart
├── services/
│   ├── sensor_fusion_service.dart
│   ├── navigation_service.dart
│   ├── map_service.dart
│   ├── routing_service.dart
│   ├── ar_guidance_service.dart
│   └── voice_guidance_service.dart
└── di/
    └── service_locator.dart
```

---

## Performance Tips

For faster development:

```bash
# Skip unused dart:io imports
flutter run --dart-define=DART_VM_OPTIONS=--no-lazy-async-frames

# Use generic emulator profile
flutter run -d emulator-5554 --no-fast-start

# Profile specific code
flutter run --profile
# Then use DevTools for performance analysis
```

---

## Getting Help

1. **Flutter Docs**: https://flutter.dev/docs
2. **BLoC Pattern**: https://bloclibrary.dev/
3. **Stack Overflow**: Tag questions with `flutter`
4. **Flutter Discord**: https://discord.gg/flutter

---

## Next Milestone

Once you complete these 30 minutes:

✅ **You can**: Run the app, understand the structure, make quick edits  
✅ **Next**: Read IMPLEMENTATION_ROADMAP.md Phase 1 in detail  
✅ **Then**: Build out the design system and navigation UI  

---

## Celebrate! 🎉

You now have:
- ✅ Working Flutter environment
- ✅ AegisNav project created
- ✅ Design system foundation
- ✅ App running on device
- ✅ Hot reload working

**You're 30 minutes into your journey to building AegisNav!**

---

**Total Time Invested**: 30 minutes  
**Total Time Until Complete App**: ~26 weeks (following IMPLEMENTATION_ROADMAP.md)  
**Current Status**: Phase 0 Complete ✅ → Ready for Phase 1  

Next: Open `IMPLEMENTATION_ROADMAP.md` to continue!
