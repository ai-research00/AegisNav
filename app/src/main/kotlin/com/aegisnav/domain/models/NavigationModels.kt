package com.aegisnav.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Represents a geographic location with coordinates and metadata
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    val accuracy: Float = 10.0f,
    val bearing: Float = 0.0f,
    val speed: Float = 0.0f,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Represents heading data with accuracy and calibration info
 */
data class HeadingData(
    val heading: Float,              // 0-360 degrees
    val accuracy: Float,              // estimated accuracy
    val timestamp: Long = System.currentTimeMillis(),
    val isCalibrated: Boolean = false,
    val rawValues: FloatArray = FloatArray(3)  // raw sensor values
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as HeadingData
        return heading == other.heading && accuracy == other.accuracy && timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        return 31 * heading.hashCode() + accuracy.hashCode() + timestamp.hashCode()
    }
}

/**
 * Represents a point of interest
 */
data class POI(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val location: Location,
    val type: POIType,
    val description: String = "",
    val distance: Double = 0.0,
    val rating: Float = 0.0f
)

enum class POIType {
    LANDMARK,
    BUILDING,
    INTERSECTION,
    WAYPOINT,
    DESTINATION,
    CUSTOM
}

/**
 * Represents a turn instruction for navigation
 */
data class TurnInstruction(
    val turn: Turn,
    val streetName: String,
    val distance: Double,
    val bearing: Float,
    val waypoints: List<Location> = emptyList()
)

enum class Turn {
    TURN_LEFT,
    TURN_RIGHT,
    GO_STRAIGHT,
    U_TURN,
    FORK_LEFT,
    FORK_RIGHT,
    MERGE,
    EXIT_LEFT,
    EXIT_RIGHT,
    ROUNDABOUT,
    ARRIVE
}

/**
 * Represents a complete navigation route
 */
data class Route(
    val id: String = UUID.randomUUID().toString(),
    val start: Location,
    val end: Location,
    val waypoints: List<Location> = emptyList(),
    val instructions: List<TurnInstruction> = emptyList(),
    val distance: Double = 0.0,
    val estimatedDuration: Long = 0L,  // in seconds
    val bounds: RouteBounds = RouteBounds(),
    val timestamp: Long = System.currentTimeMillis()
)

data class RouteBounds(
    val minLat: Double = 0.0,
    val minLon: Double = 0.0,
    val maxLat: Double = 0.0,
    val maxLon: Double = 0.0
)

/**
 * Navigation state for UI
 */
data class NavigationState(
    val route: Route? = null,
    val currentLocation: Location? = null,
    val currentHeading: HeadingData? = null,
    val currentInstruction: TurnInstruction? = null,
    val remainingDistance: Double = 0.0,
    val remainingDuration: Long = 0L,
    val distanceToNextTurn: Double = 0.0,
    val progress: Float = 0.0f,
    val isNavigating: Boolean = false,
    val error: String? = null
)

/**
 * Sensor fusion data
 */
data class SensorData(
    val heading: Float,
    val pitch: Float,
    val roll: Float,
    val accuracy: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val confidence: Float = 1.0f
)

/**
 * Calibration state
 */
data class CalibrationData(
    val minX: Float = 0f,
    val maxX: Float = 0f,
    val minY: Float = 0f,
    val maxY: Float = 0f,
    val minZ: Float = 0f,
    val maxZ: Float = 0f,
    val isCalibrated: Boolean = false,
    val calibrationAccuracy: Float = 0f,
    val lastCalibrationTime: Long = 0L
)
