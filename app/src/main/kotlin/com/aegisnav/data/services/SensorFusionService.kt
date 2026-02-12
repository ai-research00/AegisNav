package com.aegisnav.data.services

import android.content.Context
import com.aegisnav.data.sensors.CompassSensor
import com.aegisnav.data.sensors.GyroSensor
import com.aegisnav.data.sensors.AccelerometerSensor
import com.aegisnav.data.sensors.IMUFusion
import com.aegisnav.domain.models.HeadingData
import com.aegisnav.domain.models.SensorData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages sensor fusion for accurate heading calculation
 */
@Singleton
class SensorFusionService @Inject constructor(context: Context) {
    private val compassSensor = CompassSensor(context)
    private val gyroSensor = GyroSensor(context)
    private val accelSensor = AccelerometerSensor(context)
    private val imuFusion = IMUFusion()
    
    private val _sensorDataFlow = MutableStateFlow<SensorData?>(null)
    val sensorDataFlow = _sensorDataFlow.asStateFlow()
    
    private var lastHeadingData: HeadingData? = null

    fun startFusion() {
        compassSensor.startListening { headingData ->
            lastHeadingData = headingData
            imuFusion.updateWithMagnetometer(headingData)
        }
        
        gyroSensor.startListening { gyroValues ->
            imuFusion.updateWithGyroscope(gyroValues)
        }
        
        accelSensor.startListening { accelValues, _ ->
            lastHeadingData?.let { heading ->
                val fusedData = imuFusion.fuse(heading, gyroValues, accelValues)
                _sensorDataFlow.value = SensorData(
                    heading = fusedData.heading,
                    pitch = imuFusion.getPitch(),
                    roll = imuFusion.getRoll(),
                    accuracy = fusedData.accuracy
                )
            }
        }
    }

    fun stopFusion() {
        compassSensor.stopListening()
        gyroSensor.stopListening()
        accelSensor.stopListening()
    }

    fun getCurrentHeading(): SensorData? = _sensorDataFlow.value

    fun getStepCount(): Int = accelSensor.getStepCount()

    fun calibrateCompass() {
        imuFusion.reset()
    }

    fun reset() {
        imuFusion.reset()
    }
}
