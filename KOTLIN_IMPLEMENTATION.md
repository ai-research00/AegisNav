# AegisNav - Kotlin Core Implementation
## Start Building the App

---

## Step 1: Core Data Models

### Location.kt
```kotlin
package com.aegisnav.domain.model

import kotlin.math.*

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 0f,  // meters
) {
    fun distanceTo(other: Location): Double {
        val earthRadiusM = 6371000
        val dLat = Math.toRadians(other.latitude - this.latitude)
        val dLon = Math.toRadians(other.longitude - this.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(this.latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusM * c
    }

    fun bearingTo(other: Location): Double {
        val dLon = Math.toRadians(other.longitude - this.longitude)
        val lat1 = Math.toRadians(this.latitude)
        val lat2 = Math.toRadians(other.latitude)
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        val bearing = atan2(y, x)
        return (Math.toDegrees(bearing) + 360) % 360
    }
}
```

### HeadingData.kt
```kotlin
package com.aegisnav.domain.model

data class HeadingData(
    val heading: Double,        // 0-360 degrees
    val accuracy: Double,       // ±X degrees
    val timestamp: Long = System.currentTimeMillis(),
    val isCalibrated: Boolean = false,
)
```

### Route.kt
```kotlin
package com.aegisnav.domain.model

data class Route(
    val id: String = java.util.UUID.randomUUID().toString(),
    val waypoints: List<Location>,
    val distanceMeters: Double,
    val estimatedMinutes: Int,
    val turns: List<TurnInstruction>,
    val landmarks: List<Landmark>,
)

data class TurnInstruction(
    val turn: TurnType,
    val distanceMeters: Double,
    val streetName: String,
    val location: Location,
    val landmarkHints: List<String> = emptyList(),
)

enum class TurnType {
    LEFT, RIGHT, STRAIGHT, UTURN, ARRIVAL
}
```

### POI.kt
```kotlin
package com.aegisnav.domain.model

data class POI(
    val id: String,
    val name: String,
    val location: Location,
    val type: POIType,
    val description: String? = null,
)

enum class POIType {
    LANDMARK, PARK, BUILDING, CHURCH, TOWER, SCHOOL, OTHER
}
```

### NavigationState.kt
```kotlin
package com.aegisnav.domain.model

data class NavigationState(
    val currentRoute: Route? = null,
    val distanceRemaining: Double = 0.0,
    val minutesRemaining: Int = 0,
    val nextTurnIndex: Int = 0,
    val isNavigating: Boolean = false,
    val currentLocation: Location? = null,
)
```

### CalibrationStatus.kt
```kotlin
package com.aegisnav.domain.model

data class CalibrationStatus(
    val isCalibrated: Boolean = false,
    val accuracy: Double = 180.0,  // ±degrees
    val lastCalibrationTime: Long? = null,
    val recommendedAction: String? = null,
)
```

---

## Step 2: Sensor Classes

### CompassSensor.kt
```kotlin
package com.aegisnav.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlin.math.*

class CompassSensor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val headingChannel = BroadcastChannel<Double>(Channel.BUFFERED)
    val headingFlow = headingChannel.asFlow()

    private val magnetometerReading = FloatArray(3)
    private val accelerometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    var isActive = false
        private set

    fun start() {
        if (!isActive) {
            magnetometerSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
            accelerometerSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
            isActive = true
        }
    }

    fun stop() {
        if (isActive) {
            sensorManager.unregisterListener(this)
            isActive = false
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magnetometerReading, 0, 3)
            }
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, accelerometerReading, 0, 3)
            }
        }
        updateHeading()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Update calibration status based on accuracy
    }

    private fun updateHeading() {
        SensorManager.getRotationMatrix(
            rotationMatrix, null,
            accelerometerReading, magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
        
        var heading = Math.toDegrees(orientationAngles[0].toDouble())
        heading = (heading + 360) % 360
        
        headingChannel.trySend(heading)
    }

    fun calibrate(calibrationRoutine: (progress: Float) -> Unit) {
        // Figure-eight calibration
        // Called when user performs figure-eight motion
    }
}
```

### GyroSensor.kt
```kotlin
package com.aegisnav.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlin.math.*

class GyroSensor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private val rotationChannel = BroadcastChannel<Float>(Channel.BUFFERED)
    val rotationFlow = rotationChannel.asFlow()

    private var lastTimestamp = 0L
    private var currentRotation = 0f

    var isActive = false
        private set

    fun start() {
        if (!isActive) {
            gyroscopeSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
            isActive = true
        }
    }

    fun stop() {
        if (isActive) {
            sensorManager.unregisterListener(this)
            isActive = false
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        
        if (lastTimestamp != 0L) {
            val dt = (currentTime - lastTimestamp) / 1000f  // seconds
            val rotationDelta = event.values[2] * dt  // Z-axis rotation
            currentRotation += rotationDelta
            currentRotation = (currentRotation + 360) % 360
            
            rotationChannel.trySend(currentRotation)
        }
        
        lastTimestamp = currentTime
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
}
```

### AccelerometerSensor.kt
```kotlin
package com.aegisnav.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlin.math.*

class AccelerometerSensor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val stepChannel = BroadcastChannel<Int>(Channel.BUFFERED)
    val stepFlow = stepChannel.asFlow()

    private var stepCount = 0
    private var lastZValue = 0f
    private val threshold = 20f

    var isActive = false
        private set

    fun start() {
        if (!isActive) {
            accelerometerSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
            }
            isActive = true
        }
    }

    fun stop() {
        if (isActive) {
            sensorManager.unregisterListener(this)
            isActive = false
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val zValue = event.values[2]
        
        // Detect step from Z-axis acceleration peaks
        if (zValue > threshold && lastZValue <= threshold) {
            stepCount++
            stepChannel.trySend(stepCount)
        }
        
        lastZValue = zValue
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    fun resetSteps() {
        stepCount = 0
    }
}
```

### IMUFusion.kt (Sensor Fusion Algorithm)
```kotlin
package com.aegisnav.sensor

import kotlin.math.*

class IMUFusion {
    // Complementary filter parameters
    private val alpha = 0.95f  // Gyro weight (0.95 gyro, 0.05 compass)
    
    private var fusedHeading = 0.0
    private var lastGyroHeading = 0.0
    private var lastCompassHeading = 0.0
    
    fun fuseSensorData(
        compassHeading: Double,
        gyroHeading: Float,
        deltaTime: Float = 0.016f
    ): Double {
        // Complementary filter: fuse fast (gyro) with accurate (compass)
        lastGyroHeading = gyroHeading.toDouble()
        lastCompassHeading = compassHeading
        
        fusedHeading = (alpha * lastGyroHeading + (1 - alpha) * lastCompassHeading)
        fusedHeading = (fusedHeading + 360) % 360
        
        return fusedHeading
    }
    
    fun getHeadingAccuracy(compassAccuracy: Double, gyroNoise: Double): Double {
        // Calculate combined accuracy
        return sqrt((compassAccuracy * compassAccuracy) + (gyroNoise * gyroNoise))
    }
    
    fun estimatePositionDelta(
        steps: Int,
        strideLength: Float,
        heading: Double
    ): Pair<Double, Double> {
        // Calculate X, Y position change
        val distance = steps * strideLength
        val radians = Math.toRadians(heading)
        val dx = distance * sin(radians)
        val dy = distance * cos(radians)
        return Pair(dx, dy)
    }
}
```

---

## Step 3: Core Services

### SensorFusionService.kt
```kotlin
package com.aegisnav.service

import android.content.Context
import com.aegisnav.domain.model.CalibrationStatus
import com.aegisnav.domain.model.HeadingData
import com.aegisnav.sensor.AccelerometerSensor
import com.aegisnav.sensor.CompassSensor
import com.aegisnav.sensor.GyroSensor
import com.aegisnav.sensor.IMUFusion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorFusionService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val compassSensor = CompassSensor(context)
    private val gyroSensor = GyroSensor(context)
    private val accelerometerSensor = AccelerometerSensor(context)
    private val imuFusion = IMUFusion()

    private var calibrationStatus = CalibrationStatus()

    // Emit heading with accuracy
    val headingFlow: Flow<HeadingData> = combine(
        compassSensor.headingFlow,
        gyroSensor.rotationFlow
    ) { compassHeading, gyroHeading ->
        val fusedHeading = imuFusion.fuseSensorData(compassHeading, gyroHeading)
        val accuracy = imuFusion.getHeadingAccuracy(5.0, 2.0)  // Mock accuracy
        
        HeadingData(
            heading = fusedHeading,
            accuracy = accuracy,
            isCalibrated = calibrationStatus.isCalibrated
        )
    }

    // Emit step count
    val stepFlow: Flow<Int> = accelerometerSensor.stepFlow

    fun startSensors() {
        compassSensor.start()
        gyroSensor.start()
        accelerometerSensor.start()
    }

    fun stopSensors() {
        compassSensor.stop()
        gyroSensor.stop()
        accelerometerSensor.stop()
    }

    fun calibrate(): Flow<Float> {
        // Emit calibration progress
        return compassSensor.headingFlow.map { 1.0f }  // Mock: 100% done
    }

    fun getCalibrationStatus(): CalibrationStatus = calibrationStatus

    fun isCalibrated(): Boolean = calibrationStatus.isCalibrated
}
```

### NavigationService.kt
```kotlin
package com.aegisnav.service

import com.aegisnav.domain.model.*
import com.aegisnav.data.repository.RoutingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NavigationService @Inject constructor(
    private val routingRepository: RoutingRepository,
) {
    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState())
    val navigationState: StateFlow<NavigationState> = _navigationState

    suspend fun startNavigation(
        start: Location,
        destination: Location,
        mode: RouteMode = RouteMode.PEDESTRIAN,
    ) {
        val route = routingRepository.calculateRoute(start, destination, mode)
        _navigationState.value = NavigationState(
            currentRoute = route,
            distanceRemaining = route.distanceMeters,
            minutesRemaining = route.estimatedMinutes,
            isNavigating = true,
        )
    }

    fun stopNavigation() {
        _navigationState.value = NavigationState()
    }

    fun updateProgress(distanceRemaining: Double, nextTurnIndex: Int) {
        _navigationState.value = _navigationState.value.copy(
            distanceRemaining = distanceRemaining,
            nextTurnIndex = nextTurnIndex,
        )
    }

    suspend fun recalculateRoute(currentLocation: Location) {
        val currentRoute = _navigationState.value.currentRoute ?: return
        val destination = currentRoute.waypoints.last()
        
        val newRoute = routingRepository.calculateRoute(
            currentLocation, destination, RouteMode.PEDESTRIAN
        )
        
        _navigationState.value = _navigationState.value.copy(
            currentRoute = newRoute,
            distanceRemaining = newRoute.distanceMeters,
        )
    }
}

enum class RouteMode {
    PEDESTRIAN, TRAIL, OFFROAD, EMERGENCY
}
```

### MapService.kt
```kotlin
package com.aegisnav.service

import com.aegisnav.domain.model.Location
import com.aegisnav.domain.model.POI
import com.aegisnav.data.repository.MapRepository
import kotlinx.coroutines.flow.Flow
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MapService @Inject constructor(
    private val mapRepository: MapRepository,
) {
    suspend fun loadMapRegion(regionId: String) {
        mapRepository.loadMapRegion(regionId)
    }

    fun getMapCoverage(): Flow<List<MapBounds>> {
        return mapRepository.getMapCoverage()
    }

    suspend fun queryPOIs(
        location: Location,
        radiusMeters: Double,
        poiType: POIType? = null,
    ): List<POI> {
        return mapRepository.queryPOIs(location, radiusMeters, poiType)
    }

    suspend fun downloadRegion(regionId: String, onProgress: (Float) -> Unit) {
        mapRepository.downloadRegion(regionId, onProgress)
    }
}

data class MapBounds(
    val north: Double,
    val south: Double,
    val east: Double,
    val west: Double,
)
```

### RoutingService.kt
```kotlin
package com.aegisnav.service

import com.aegisnav.domain.model.Location
import com.aegisnav.domain.model.Route
import com.aegisnav.data.repository.RoutingRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RoutingService @Inject constructor(
    private val routingRepository: RoutingRepository,
) {
    suspend fun calculateRoute(
        start: Location,
        destination: Location,
        mode: RouteMode = RouteMode.PEDESTRIAN,
    ): Route {
        return routingRepository.calculateRoute(start, destination, mode)
    }

    suspend fun calculateRoutes(
        start: Location,
        destination: Location,
        mode: RouteMode = RouteMode.PEDESTRIAN,
        alternatives: Int = 3,
    ): List<Route> {
        return routingRepository.calculateRoutes(start, destination, mode, alternatives)
    }
}
```

### VoiceGuidanceService.kt
```kotlin
package com.aegisnav.service

import android.content.Context
import android.speech.tts.TextToSpeech
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import java.util.*

@ActivityScoped
class VoiceGuidanceService @Inject constructor(
    context: Context,
) : TextToSpeech.OnInitListener {
    
    private var tts: TextToSpeech = TextToSpeech(context, this)
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
        }
    }
    
    fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }
    
    fun speakTurnInstruction(turn: String, street: String, distance: Double) {
        val text = "In ${distance.toInt()} meters, turn $turn onto $street"
        speak(text)
    }
    
    fun stop() {
        tts.stop()
    }
    
    fun shutdown() {
        tts.shutdown()
    }
}
```

---

## Step 4: Dependency Injection Setup

### AppModule.kt
```kotlin
package com.aegisnav.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.aegisnav.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "aegisnav_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSensorFusionService(
        @ApplicationContext context: Context
    ): SensorFusionService {
        return SensorFusionService(context)
    }

    @Provides
    @Singleton
    fun provideVoiceGuidanceService(
        @ApplicationContext context: Context
    ): VoiceGuidanceService {
        return VoiceGuidanceService(context)
    }
}
```

### DatabaseModule.kt
```kotlin
package com.aegisnav.di

import android.content.Context
import androidx.room.Room
import com.aegisnav.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aegisnav.db"
        ).build()
    }
}
```

---

## Step 5: Main Application Entry Point

### AegisNavApplication.kt
```kotlin
package com.aegisnav

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.util.Log

@HiltAndroidApp
class AegisNavApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("AegisNav", "Application initialized")
    }
}
```

### MainActivity.kt
```kotlin
package com.aegisnav.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.aegisnav.presentation.ui.theme.AegisNavTheme
import com.aegisnav.presentation.ui.screen.NavigationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AegisNavTheme {
                Surface {
                    NavigationScreen()
                }
            }
        }
    }
}
```

---

## Step 6: ViewModels

### NavigationViewModel.kt
```kotlin
package com.aegisnav.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aegisnav.domain.model.*
import com.aegisnav.presentation.state.NavigationUiState
import com.aegisnav.service.NavigationService
import com.aegisnav.service.SensorFusionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val sensorFusionService: SensorFusionService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NavigationUiState>(NavigationUiState.Idle)
    val uiState: StateFlow<NavigationUiState> = _uiState

    init {
        sensorFusionService.startSensors()
    }

    fun startNavigation(start: Location, destination: Location) {
        viewModelScope.launch {
            _uiState.value = NavigationUiState.Loading
            try {
                navigationService.startNavigation(start, destination)
                _uiState.value = NavigationUiState.Navigating(
                    navigationService.navigationState.value
                )
            } catch (e: Exception) {
                _uiState.value = NavigationUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun stopNavigation() {
        navigationService.stopNavigation()
        _uiState.value = NavigationUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        sensorFusionService.stopSensors()
    }
}
```

### NavigationUiState.kt
```kotlin
package com.aegisnav.presentation.state

import com.aegisnav.domain.model.NavigationState

sealed class NavigationUiState {
    object Idle : NavigationUiState()
    object Loading : NavigationUiState()
    data class Navigating(val state: NavigationState) : NavigationUiState()
    data class Error(val message: String) : NavigationUiState()
}
```

---

## Next Steps

1. ✅ Create Android project with this structure
2. ✅ Implement core services (provided above)
3. [ ] Build UI components with Jetpack Compose
4. [ ] Integrate offline maps (MapLibre GL)
5. [ ] Add ARCore integration
6. [ ] Implement routing engine
7. [ ] Add comprehensive tests
8. [ ] Security hardening

---

**Status**: 🟢 Core Kotlin Implementation Ready  
**Next**: Create the UI layer with Jetpack Compose  
**Time**: ~40 hours to complete Phase 1  
