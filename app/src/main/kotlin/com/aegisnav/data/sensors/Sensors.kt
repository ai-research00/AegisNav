package com.aegisnav.data.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.aegisnav.domain.models.HeadingData
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Magnetometer sensor for compass heading
 */
class CompassSensor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    private var accelerometerValues = FloatArray(3)
    private var magnetometerValues = FloatArray(3)
    private var rotationMatrix = FloatArray(9)
    private var orientation = FloatArray(3)
    
    private var listener: ((HeadingData) -> Unit)? = null
    private var lastHeading = 0f
    private var isCalibrated = false

    fun startListening(listener: (HeadingData) -> Unit) {
        this.listener = listener
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this, magnetometer)
        sensorManager.unregisterListener(this, accelerometer)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, accelerometerValues, 0, 3)
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magnetometerValues, 0, 3)
            }
        }

        // Calculate heading
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magnetometerValues)) {
            SensorManager.getOrientation(rotationMatrix, orientation)
            val heading = Math.toDegrees(orientation[0].toDouble()).toFloat()
            lastHeading = (heading + 360) % 360
            
            val accuracy = calculateAccuracy(magnetometerValues)
            val headingData = HeadingData(
                heading = lastHeading,
                accuracy = accuracy,
                isCalibrated = isCalibrated,
                rawValues = magnetometerValues.copyOf()
            )
            listener?.invoke(headingData)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        if (sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            isCalibrated = accuracy >= SensorManager.SENSOR_STATUS_ACCURACY_HIGH
        }
    }

    private fun calculateAccuracy(values: FloatArray): Float {
        val magnitude = sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2])
        return magnitude / 50f // Normalized to 0-2 range
    }

    fun getLastHeading(): Float = lastHeading
}

/**
 * Gyroscope sensor for rotation tracking
 */
class GyroSensor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    
    private var listener: ((FloatArray) -> Unit)? = null
    private var lastValues = FloatArray(3)

    fun startListening(listener: (FloatArray) -> Unit) {
        this.listener = listener
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this, gyroscope)
    }

    override fun onSensorChanged(event: SensorEvent) {
        System.arraycopy(event.values, 0, lastValues, 0, 3)
        listener?.invoke(lastValues.copyOf())
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    fun getLastValues(): FloatArray = lastValues.copyOf()
}

/**
 * Accelerometer sensor for detecting motion and steps
 */
class AccelerometerSensor(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    private var listener: ((FloatArray, Float) -> Unit)? = null
    private var lastValues = FloatArray(3)
    private var lastMagnitude = 0f
    private var isMoving = false
    private var stepCount = 0

    fun startListening(listener: (FloatArray, Float) -> Unit) {
        this.listener = listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this, accelerometer)
    }

    override fun onSensorChanged(event: SensorEvent) {
        System.arraycopy(event.values, 0, lastValues, 0, 3)
        
        val magnitude = sqrt(
            lastValues[0] * lastValues[0] +
            lastValues[1] * lastValues[1] +
            lastValues[2] * lastValues[2]
        )
        
        val delta = magnitude - lastMagnitude
        if (delta > 2f && !isMoving) {
            isMoving = true
            stepCount++
        } else if (delta < 1f && isMoving) {
            isMoving = false
        }
        
        lastMagnitude = magnitude
        listener?.invoke(lastValues.copyOf(), magnitude)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    fun getStepCount(): Int = stepCount
    fun resetStepCount() { stepCount = 0 }
    fun getLastValues(): FloatArray = lastValues.copyOf()
}
