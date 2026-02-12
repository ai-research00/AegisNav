package com.aegisnav.data.sensors

import com.aegisnav.domain.models.HeadingData
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Sensor fusion using complementary filter
 * Combines magnetometer, gyroscope, and accelerometer data for accurate heading
 */
class IMUFusion {
    private var fusedHeading = 0f
    private var fusedPitch = 0f
    private var fusedRoll = 0f
    
    // Complementary filter coefficients
    private val alpha = 0.95f  // Weight for gyroscope (high pass)
    private val beta = 0.05f   // Weight for accelerometer/magnetometer (low pass)
    
    private var lastUpdateTime = System.currentTimeMillis()

    fun updateWithMagnetometer(headingData: HeadingData) {
        fusedHeading = headingData.heading
    }

    fun updateWithGyroscope(gyroValues: FloatArray): Float {
        val currentTime = System.currentTimeMillis()
        val timeDelta = (currentTime - lastUpdateTime) / 1000f
        lastUpdateTime = currentTime

        // Integrate gyroscope angular velocity over time
        if (timeDelta < 0.05f) {  // Ignore large jumps
            fusedHeading += gyroValues[2] * timeDelta * 57.2958f  // Convert rad/s to deg/s
        }

        fusedHeading = (fusedHeading + 360) % 360
        return fusedHeading
    }

    fun updateWithAccelerometer(accelValues: FloatArray): FloatArray {
        // Calculate pitch and roll from accelerometer
        val ax = accelValues[0]
        val ay = accelValues[1]
        val az = accelValues[2]
        
        val pitch = atan2(ax, sqrt(ay * ay + az * az)) * 57.2958f
        val roll = atan2(ay, az) * 57.2958f
        
        // Complementary filter
        fusedPitch = alpha * fusedPitch + beta * pitch
        fusedRoll = alpha * fusedRoll + beta * roll
        
        return floatArrayOf(fusedHeading, fusedPitch, fusedRoll)
    }

    fun fuse(
        magnetometerData: HeadingData,
        gyroValues: FloatArray,
        accelValues: FloatArray
    ): HeadingData {
        // Update with gyroscope (high-frequency, low accuracy)
        updateWithGyroscope(gyroValues)
        
        // Correct with magnetometer (low-frequency, high accuracy)
        if (magnetometerData.timestamp > (lastUpdateTime - 200)) {
            val magnetometerHeading = magnetometerData.heading
            fusedHeading = alpha * fusedHeading + beta * magnetometerHeading
        }
        
        // Update with accelerometer for stability
        updateWithAccelerometer(accelValues)
        
        fusedHeading = (fusedHeading + 360) % 360
        
        return HeadingData(
            heading = fusedHeading,
            accuracy = magnetometerData.accuracy,
            isCalibrated = magnetometerData.isCalibrated,
            rawValues = floatArrayOf(fusedHeading, fusedPitch, fusedRoll)
        )
    }

    fun reset() {
        fusedHeading = 0f
        fusedPitch = 0f
        fusedRoll = 0f
        lastUpdateTime = System.currentTimeMillis()
    }

    fun getHeading(): Float = fusedHeading
    fun getPitch(): Float = fusedPitch
    fun getRoll(): Float = fusedRoll
}

/**
 * Calculates distance between two GPS coordinates using Haversine formula
 */
fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadiusKm = 6371.0
    
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    
    val sinDLat = sin(dLat / 2)
    val sinDLon = sin(dLon / 2)
    
    val a = sinDLat * sinDLat +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sinDLon * sinDLon
    
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadiusKm * c
}

/**
 * Calculates bearing (azimuth) between two coordinates
 */
fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
    val dLon = Math.toRadians(lon2 - lon1)
    
    val y = sin(dLon) * cos(Math.toRadians(lat2))
    val x = cos(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) -
            sin(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * cos(dLon)
    
    val bearing = (Math.toDegrees(atan2(y, x)) + 360) % 360
    return bearing.toFloat()
}

/**
 * Estimates position using dead reckoning
 */
fun deadReckoning(
    currentLat: Double,
    currentLon: Double,
    bearing: Float,
    distance: Double  // in kilometers
): Pair<Double, Double> {
    val earthRadiusKm = 6371.0
    val brng = Math.toRadians(bearing.toDouble())
    val d = distance / earthRadiusKm
    
    val lat1 = Math.toRadians(currentLat)
    val lon1 = Math.toRadians(currentLon)
    
    val lat2 = Math.asin(
        sin(lat1) * cos(d) +
        cos(lat1) * sin(d) * cos(brng)
    )
    
    val lon2 = lon1 + atan2(
        sin(brng) * sin(d) * cos(lat1),
        cos(d) - sin(lat1) * sin(lat2)
    )
    
    return Pair(Math.toDegrees(lat2), Math.toDegrees(lon2))
}
