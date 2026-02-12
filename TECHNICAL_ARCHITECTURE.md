# AegisNav Technical Architecture
## System Design & Integration Points

---

## System Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│              UI/Presentation Layer                  │
│  (Screens, Components, Theme, State Management)     │
└────────────┬────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────┐
│           Service Layer (Business Logic)            │
│  ├─ Navigation Service                             │
│  ├─ Sensor Fusion Service                          │
│  ├─ Map Service                                    │
│  ├─ Routing Service                                │
│  └─ AR Guidance Service                            │
└────────────┬────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────┐
│            Repository Layer (Data Access)          │
│  ├─ Map Repository                                 │
│  ├─ Route Repository                               │
│  ├─ POI Repository                                 │
│  └─ Calibration Repository                         │
└────────────┬────────────────────────────────────────┘
             │
┌────────────▼────────────────────────────────────────┐
│        Data Source Layer (Device & Storage)        │
│  ├─ Native Sensor APIs (IMU, Magnetometer)        │
│  ├─ Camera (ARCore/ARKit)                          │
│  ├─ Local Database (SQLite)                        │
│  └─ File System (MBTiles, routing graphs)          │
└─────────────────────────────────────────────────────┘
```

---

## Core Services

### 1. Sensor Fusion Service

**Purpose**: Fuse IMU data to provide accurate heading and movement estimation.

```dart
abstract class SensorFusionService {
  /// Get current heading (0-360°)
  Stream<double> get headingStream;
  
  /// Get heading with accuracy estimate
  Stream<HeadingData> get headingWithAccuracyStream;
  
  /// Get step count
  Stream<int> get stepCountStream;
  
  /// Get estimated position change
  Stream<PositionDelta> get positionDeltaStream;
  
  /// Start sensor reading
  Future<void> startSensors();
  
  /// Stop sensor reading
  Future<void> stopSensors();
  
  /// Trigger figure-eight calibration
  Future<void> calibrate();
  
  /// Get calibration status
  Future<CalibrationStatus> getCalibrationStatus();
}

class HeadingData {
  final double heading; // 0-360°
  final double accuracy; // ±X°
  final DateTime timestamp;
  final bool isCalibrated;
}

class PositionDelta {
  final double deltaX; // meters
  final double deltaY; // meters
  final int stepCount;
  final DateTime timestamp;
}

class CalibrationStatus {
  final bool isCalibrated;
  final double accuracy;
  final DateTime lastCalibration;
  final String? recommendedAction;
}
```

### 2. Navigation Service

**Purpose**: Manage navigation state, routing, and waypoint management.

```dart
abstract class NavigationService {
  /// Start navigation to destination
  Future<void> startNavigation({
    required LatLng start,
    required LatLng destination,
    required RouteMode mode, // pedestrian, trail, offroad
  });
  
  /// Stop navigation
  Future<void> stopNavigation();
  
  /// Get current route
  Stream<Route> get currentRouteStream;
  
  /// Get navigation progress
  Stream<NavigationProgress> get progressStream;
  
  /// Get next turn instruction
  Stream<TurnInstruction> get nextTurnStream;
  
  /// Get deviation status
  Stream<DeviationStatus> get deviationStream;
  
  /// Recalculate route
  Future<Route> recalculateRoute();
}

class Route {
  final List<LatLng> waypoints;
  final double distanceMeters;
  final int estimatedMinutes;
  final List<TurnInstruction> turns;
  final List<Landmark> landmarksAlong;
}

class TurnInstruction {
  final String turn; // LEFT, RIGHT, STRAIGHT, UTURN
  final double distanceMeters;
  final String street;
  final LatLng location;
  final List<String> landmarks; // nearby features to aid navigation
}

class NavigationProgress {
  final double distanceRemaining;
  final int minutesRemaining;
  final double percentComplete;
  final LatLng currentPosition;
  final int nextWaypointIndex;
}

class DeviationStatus {
  final bool isDeviated;
  final double deviationMeters;
  final DateTime detectedAt;
  final Route? newRoute; // if recalculated
}
```

### 3. Offline Map Service

**Purpose**: Load, cache, and query offline map data.

```dart
abstract class MapService {
  /// Load map region from storage
  Future<void> loadMapRegion(String regionId);
  
  /// Get map tile for given location and zoom
  Future<MapTile?> getTile({
    required LatLng location,
    required int zoom,
  });
  
  /// Query roads near location
  Future<List<Road>> queryRoads({
    required LatLng location,
    required double radiusMeters,
  });
  
  /// Query POIs near location
  Future<List<POI>> queryPOIs({
    required LatLng location,
    required double radiusMeters,
    required POIType? type,
  });
  
  /// Get available regions for download
  Future<List<MapRegion>> getAvailableRegions();
  
  /// Download region
  Future<void> downloadRegion({
    required String regionId,
    required void Function(double progress) onProgress,
  });
  
  /// Delete region
  Future<void> deleteRegion(String regionId);
  
  /// Get offline map coverage
  Future<List<LatLngBounds>> getMapCoverage();
}

class MapRegion {
  final String id;
  final String name;
  final LatLngBounds bounds;
  final double sizeBytes;
  final DateTime lastUpdated;
  final bool isDownloaded;
}

class Road {
  final String name;
  final RoadType type; // primary, secondary, tertiary, path
  final List<LatLng> geometry;
  final bool isOneWay;
  final double speedLimitKmh;
}

class POI {
  final String name;
  final LatLng location;
  final POIType type;
  final String? description;
  final Map<String, String>? metadata;
}

enum POIType { landmark, park, building, church, tower, school }
```

### 4. Routing Engine Service

**Purpose**: Compute routes using offline routing graphs.

```dart
abstract class RoutingService {
  /// Calculate route
  Future<Route> calculateRoute({
    required LatLng start,
    required LatLng destination,
    required RouteMode mode,
  });
  
  /// Calculate multiple routes (alternatives)
  Future<List<Route>> calculateRoutes({
    required LatLng start,
    required LatLng destination,
    required RouteMode mode,
    int alternatives = 3,
  });
  
  /// Get route matrix (distance/time between points)
  Future<RouteMatrix> getRouteMatrix({
    required List<LatLng> locations,
    required RouteMode mode,
  });
  
  /// Check if routing is available
  Future<bool> isRoutingAvailable(LatLng location);
}

enum RouteMode { pedestrian, trail, offroad, emergency }

class RouteMatrix {
  final List<List<double>> distances; // meters
  final List<List<int>> durations; // seconds
}
```

### 5. AR Guidance Service

**Purpose**: Manage AR visualization and landmark anchoring.

```dart
abstract class ARGuidanceService {
  /// Initialize AR session
  Future<void> initARSession();
  
  /// Get visible landmarks
  Stream<List<ARLandmark>> get visibleLandmarksStream;
  
  /// Get heading arrow direction
  Stream<double> get headingArrowStream;
  
  /// Get distance indicator
  Stream<ARDistanceIndicator> get distanceIndicatorStream;
  
  /// Get next turn visualization
  Stream<ARTurnVisualization> get turnVisualizationStream;
  
  /// Place AR anchor at location
  Future<ARObject> placeAnchor({
    required LatLng location,
    required String label,
    required ARObjectType type,
  });
  
  /// Remove AR anchor
  Future<void> removeAnchor(String anchorId);
  
  /// Get AR camera frame
  Stream<ARCameraFrame> get cameraFrameStream;
  
  /// Update AR lighting
  Future<void> updateLighting(ARLighting lighting);
}

class ARLandmark {
  final String id;
  final String name;
  final LatLng location;
  final double distanceMeters;
  final double headingFromUser;
  final ARConfidence confidence;
}

class ARDistanceIndicator {
  final double distanceMeters;
  final int minutesRemaining;
  final bool isApproaching;
  final double confidencePercent;
}

class ARTurnVisualization {
  final String turn; // LEFT, RIGHT, STRAIGHT, UTURN
  final double distanceMeters;
  final String street;
  final ARConfidence confidence;
}

enum ARObjectType { arrow, landmark, waypoint, hazard }
enum ARConfidence { high, medium, low }
```

### 6. Voice Guidance Service

**Purpose**: Provide offline text-to-speech guidance.

```dart
abstract class VoiceGuidanceService {
  /// Speak turn instruction
  Future<void> speakTurnInstruction(TurnInstruction turn);
  
  /// Speak distance update
  Future<void> speakDistanceUpdate(double distanceMeters);
  
  /// Speak landmark alert
  Future<void> speakLandmarkAlert(String landmarkName);
  
  /// Speak alert message
  Future<void> speakAlert(String message, AlertPriority priority);
  
  /// Set language
  Future<void> setLanguage(String languageCode);
  
  /// Set voice speed
  Future<void> setVoiceSpeed(double speed); // 0.5 to 2.0
  
  /// Set voice volume
  Future<void> setVoiceVolume(double volume); // 0.0 to 1.0
  
  /// Stop speaking
  Future<void> stop();
  
  /// Get available languages
  Future<List<String>> getAvailableLanguages();
}

enum AlertPriority { info, warning, critical }
```

---

## State Management (BLoC Architecture)

```dart
// Navigation State
class NavigationState {
  final Route? currentRoute;
  final NavigationProgress? progress;
  final TurnInstruction? nextTurn;
  final DeviationStatus? deviation;
  final NavigationStatus status; // idle, navigating, paused, completed, error
  final String? errorMessage;
}

// Sensor State
class SensorState {
  final double heading;
  final double headingAccuracy;
  final int stepCount;
  final CalibrationStatus calibration;
  final SensorHealth health; // all sensors OK, has warnings, error
}

// Map State
class MapState {
  final LatLng? userLocation;
  final int zoomLevel;
  final LatLngBounds visibleBounds;
  final List<MapRegion> downloadedRegions;
  final List<POI> visiblePOIs;
  final List<Road> visibleRoads;
  final double mapLoadingProgress;
}

// AR State
class ARState {
  final bool isARAvailable;
  final bool isARSessionActive;
  final List<ARLandmark> visibleLandmarks;
  final ARCameraFrame? currentFrame;
  final ARSessionQuality quality;
  final String? arError;
}

enum NavigationStatus { idle, navigating, paused, completed, error }
enum SensorHealth { ok, warning, error }
enum ARSessionQuality { high, medium, low, poor }
```

---

## Data Models

```dart
class LatLng {
  final double latitude;
  final double longitude;
  
  LatLng(this.latitude, this.longitude);
  
  double distanceTo(LatLng other) {
    // Haversine formula implementation
  }
  
  double bearingTo(LatLng other) {
    // Calculate bearing in degrees
  }
}

class LatLngBounds {
  final LatLng southwest;
  final LatLng northeast;
  
  bool contains(LatLng point) { ... }
}

class MapTile {
  final int x;
  final int y;
  final int zoom;
  final Uint8List imageData;
  final DateTime loadedAt;
}

class ARCameraFrame {
  final Uint8List imageData;
  final double width;
  final double height;
  final Matrix4 projectionMatrix;
  final DateTime timestamp;
}

class ARLighting {
  final double ambientBrightness;
  final Color dominantColor;
  final bool isIndoor;
}
```

---

## Error Handling Strategy

```dart
sealed class AegisNavException implements Exception {
  final String message;
  AegisNavException(this.message);
}

class SensorException extends AegisNavException {
  SensorException(String message) : super(message);
}

class MapLoadException extends AegisNavException {
  MapLoadException(String message) : super(message);
}

class RoutingException extends AegisNavException {
  RoutingException(String message) : super(message);
}

class ARException extends AegisNavException {
  ARException(String message) : super(message);
}

class CalibrationException extends AegisNavException {
  CalibrationException(String message) : super(message);
}

class StorageException extends AegisNavException {
  StorageException(String message) : super(message);
}
```

---

## Dependency Injection

```dart
// Service locator setup
final getIt = GetIt.instance;

void setupServiceLocator() {
  // Register repositories
  getIt.registerSingleton<MapRepository>(MapRepositoryImpl());
  getIt.registerSingleton<RouteRepository>(RouteRepositoryImpl());
  getIt.registerSingleton<CalibrationRepository>(CalibrationRepositoryImpl());
  
  // Register services
  getIt.registerSingleton<SensorFusionService>(
    SensorFusionServiceImpl(mapRepository: getIt()),
  );
  getIt.registerSingleton<NavigationService>(
    NavigationServiceImpl(routingService: getIt()),
  );
  getIt.registerSingleton<MapService>(
    MapServiceImpl(mapRepository: getIt()),
  );
  getIt.registerSingleton<RoutingService>(
    RoutingServiceImpl(routeRepository: getIt()),
  );
  getIt.registerSingleton<ARGuidanceService>(
    ARGuidanceServiceImpl(),
  );
  getIt.registerSingleton<VoiceGuidanceService>(
    VoiceGuidanceServiceImpl(),
  );
  
  // Register BLoCs
  getIt.registerSingleton<NavigationBloc>(
    NavigationBloc(navigationService: getIt()),
  );
  getIt.registerSingleton<SensorBloc>(
    SensorBloc(sensorFusionService: getIt()),
  );
  getIt.registerSingleton<MapBloc>(
    MapBloc(mapService: getIt()),
  );
  getIt.registerSingleton<ARBloc>(
    ARBloc(arService: getIt()),
  );
}
```

---

## Security & Encryption

```dart
abstract class CryptoService {
  /// Encrypt sensitive data
  Future<String> encrypt(String plaintext);
  
  /// Decrypt sensitive data
  Future<String> decrypt(String ciphertext);
  
  /// Generate secure random key
  String generateKey();
  
  /// Hash data (one-way)
  String hash(String data);
  
  /// Verify hash
  bool verifyHash(String data, String hash);
}

class CryptoServiceImpl implements CryptoService {
  // Uses AES-256-GCM for encryption
  // Uses SHA-256 for hashing
  // Stores keys in hardware keystore (Android Keystore / iOS Secure Enclave)
}
```

This architecture ensures clean separation of concerns, testability, and scalability across the AegisNav system.
