package com.aegisnav.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aegisnav.data.services.NavigationService
import com.aegisnav.data.services.SensorFusionService
import com.aegisnav.data.services.MapService
import com.aegisnav.data.services.RoutingService
import com.aegisnav.data.services.VoiceGuidanceService
import com.aegisnav.domain.models.Location
import com.aegisnav.domain.models.NavigationState
import com.aegisnav.domain.models.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for navigation screen
 */
@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationService: NavigationService,
    private val sensorFusionService: SensorFusionService,
    private val mapService: MapService,
    private val routingService: RoutingService,
    private val voiceGuidanceService: VoiceGuidanceService
) : ViewModel() {

    private val _navigationStateFlow = MutableStateFlow<NavigationState>(NavigationState())
    val navigationStateFlow = _navigationStateFlow.asStateFlow()

    private val _isNavigatingFlow = MutableStateFlow(false)
    val isNavigatingFlow = _isNavigatingFlow.asStateFlow()

    private val _errorMessageFlow = MutableStateFlow<String?>(null)
    val errorMessageFlow = _errorMessageFlow.asStateFlow()

    init {
        startSensorFusion()
        observeNavigationUpdates()
    }

    private fun startSensorFusion() {
        sensorFusionService.startFusion()
    }

    private fun observeNavigationUpdates() {
        viewModelScope.launch {
            navigationService.navigationStateFlow.collectLatest { state ->
                _navigationStateFlow.value = state
                _isNavigatingFlow.value = state.isNavigating
                
                // Update heading from sensor data
                sensorFusionService.getCurrentHeading()?.let { sensorData ->
                    navigationService.updateHeading(sensorData.heading)
                }
            }
        }
    }

    fun startNavigation(start: Location, end: Location) {
        viewModelScope.launch {
            try {
                val route = routingService.calculateRoute(start, end)
                navigationService.startNavigation(route)
                voiceGuidanceService.speak("Navigation started")
                _isNavigatingFlow.value = true
            } catch (e: Exception) {
                _errorMessageFlow.value = "Failed to start navigation: ${e.message}"
                voiceGuidanceService.speak("Navigation failed")
            }
        }
    }

    fun updateLocation(location: Location) {
        navigationService.updateLocation(location)
    }

    fun stopNavigation() {
        navigationService.stopNavigation()
        voiceGuidanceService.speak("Navigation stopped")
        _isNavigatingFlow.value = false
    }

    fun getStepCount(): Int = sensorFusionService.getStepCount()

    override fun onCleared() {
        super.onCleared()
        navigationService.stopNavigation()
        sensorFusionService.stopFusion()
        voiceGuidanceService.shutdown()
    }
}

/**
 * Navigation UI state
 */
data class NavigationUiState(
    val isNavigating: Boolean = false,
    val currentHeading: Float = 0f,
    val currentDistance: Double = 0.0,
    val remainingDistance: Double = 0.0,
    val progress: Float = 0f,
    val errorMessage: String? = null,
    val navigationState: NavigationState = NavigationState()
)
