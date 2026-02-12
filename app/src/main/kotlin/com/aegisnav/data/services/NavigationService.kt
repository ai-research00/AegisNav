package com.aegisnav.data.services

import com.aegisnav.domain.models.Location
import com.aegisnav.domain.models.Route
import com.aegisnav.domain.models.NavigationState
import com.aegisnav.domain.models.TurnInstruction
import com.aegisnav.data.sensors.calculateDistance
import com.aegisnav.data.sensors.calculateBearing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages navigation state and route progression
 */
@Singleton
class NavigationService @Inject constructor() {
    private val _navigationStateFlow = MutableStateFlow<NavigationState>(NavigationState())
    val navigationStateFlow = _navigationStateFlow.asStateFlow()
    
    private var currentRoute: Route? = null
    private var currentWaypointIndex = 0
    private var isNavigating = false

    fun startNavigation(route: Route) {
        currentRoute = route
        currentWaypointIndex = 0
        isNavigating = true
        updateNavigationState()
    }

    fun stopNavigation() {
        isNavigating = false
        currentRoute = null
        currentWaypointIndex = 0
        _navigationStateFlow.value = NavigationState()
    }

    fun updateLocation(location: Location) {
        if (!isNavigating || currentRoute == null) return
        
        val route = currentRoute ?: return
        val remaining = calculateRemainingDistance(location, route)
        val progress = ((route.distance - remaining) / route.distance).coerceIn(0f, 1f)
        
        val instruction = getCurrentInstruction(location)
        val nextWaypointDist = if (currentWaypointIndex < route.waypoints.size) {
            calculateDistance(
                location.latitude,
                location.longitude,
                route.waypoints[currentWaypointIndex].latitude,
                route.waypoints[currentWaypointIndex].longitude
            )
        } else {
            calculateDistance(
                location.latitude,
                location.longitude,
                route.end.latitude,
                route.end.longitude
            )
        }

        // Update waypoint if close enough (10 meters)
        if (nextWaypointDist < 0.01 && currentWaypointIndex < route.waypoints.size) {
            currentWaypointIndex++
        }

        _navigationStateFlow.value = _navigationStateFlow.value.copy(
            route = route,
            currentLocation = location,
            currentInstruction = instruction,
            remainingDistance = remaining,
            distanceToNextTurn = nextWaypointDist,
            progress = progress,
            isNavigating = true
        )

        // Check if arrived
        if (remaining < 0.02) {  // 20 meters
            stopNavigation()
        }
    }

    fun updateHeading(heading: Float) {
        _navigationStateFlow.value = _navigationStateFlow.value.copy(
            currentHeading = _navigationStateFlow.value.currentHeading?.copy(heading = heading)
        )
    }

    private fun getCurrentInstruction(location: Location): TurnInstruction? {
        val route = currentRoute ?: return null
        if (currentWaypointIndex >= route.instructions.size) return null
        
        return route.instructions[currentWaypointIndex]
    }

    private fun calculateRemainingDistance(location: Location, route: Route): Double {
        if (currentWaypointIndex >= route.waypoints.size) {
            return calculateDistance(
                location.latitude,
                location.longitude,
                route.end.latitude,
                route.end.longitude
            )
        }
        
        var remaining = 0.0
        
        // Distance to next waypoint
        remaining += calculateDistance(
            location.latitude,
            location.longitude,
            route.waypoints[currentWaypointIndex].latitude,
            route.waypoints[currentWaypointIndex].longitude
        )
        
        // Distance between waypoints
        for (i in currentWaypointIndex until route.waypoints.size - 1) {
            remaining += calculateDistance(
                route.waypoints[i].latitude,
                route.waypoints[i].longitude,
                route.waypoints[i + 1].latitude,
                route.waypoints[i + 1].longitude
            )
        }
        
        // Distance to final destination
        remaining += calculateDistance(
            route.waypoints.last().latitude,
            route.waypoints.last().longitude,
            route.end.latitude,
            route.end.longitude
        )
        
        return remaining
    }

    private fun updateNavigationState() {
        currentRoute?.let { route ->
            _navigationStateFlow.value = NavigationState(
                route = route,
                isNavigating = true,
                remainingDistance = route.distance,
                progress = 0f
            )
        }
    }

    fun isNavigating(): Boolean = isNavigating
}
