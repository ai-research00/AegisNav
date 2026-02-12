package com.aegisnav.data.services

import com.aegisnav.domain.models.Location
import com.aegisnav.domain.models.Route
import com.aegisnav.domain.models.TurnInstruction
import com.aegisnav.domain.models.Turn
import com.aegisnav.data.sensors.calculateDistance
import com.aegisnav.data.sensors.calculateBearing
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages route calculation and optimization
 */
@Singleton
class RoutingService @Inject constructor() {

    fun calculateRoute(start: Location, end: Location, waypoints: List<Location> = emptyList()): Route {
        val allPoints = mutableListOf(start) + waypoints + listOf(end)
        val distance = calculateTotalDistance(allPoints)
        val duration = estimateDuration(distance)
        
        val instructions = generateInstructions(allPoints)
        
        return Route(
            start = start,
            end = end,
            waypoints = waypoints,
            instructions = instructions,
            distance = distance,
            estimatedDuration = duration
        )
    }

    fun recalculateRoute(currentLocation: Location, destination: Location, currentRoute: Route): Route {
        // Simplified recalculation - in production, would use actual routing engine
        return calculateRoute(currentLocation, destination, currentRoute.waypoints)
    }

    fun optimizeWaypoints(waypoints: List<Location>): List<Location> {
        // Nearest neighbor algorithm for simple optimization
        if (waypoints.isEmpty()) return emptyList()
        
        val result = mutableListOf(waypoints[0])
        val remaining = waypoints.drop(1).toMutableList()
        
        while (remaining.isNotEmpty()) {
            val lastPoint = result.last()
            var nearestIndex = 0
            var minDistance = Double.MAX_VALUE
            
            remaining.forEachIndexed { index, point ->
                val distance = calculateDistance(
                    lastPoint.latitude, lastPoint.longitude,
                    point.latitude, point.longitude
                )
                if (distance < minDistance) {
                    minDistance = distance
                    nearestIndex = index
                }
            }
            
            result.add(remaining.removeAt(nearestIndex))
        }
        
        return result
    }

    private fun calculateTotalDistance(points: List<Location>): Double {
        var total = 0.0
        for (i in 0 until points.size - 1) {
            total += calculateDistance(
                points[i].latitude, points[i].longitude,
                points[i + 1].latitude, points[i + 1].longitude
            )
        }
        return total
    }

    private fun estimateDuration(distanceKm: Double): Long {
        // Estimate 1 meter per second average walking speed (3.6 km/h)
        return (distanceKm * 1000 / 1.0).toLong()
    }

    private fun generateInstructions(points: List<Location>): List<TurnInstruction> {
        val instructions = mutableListOf<TurnInstruction>()
        
        for (i in 1 until points.size - 1) {
            val prev = points[i - 1]
            val curr = points[i]
            val next = points[i + 1]
            
            val bearing1 = calculateBearing(prev.latitude, prev.longitude, curr.latitude, curr.longitude)
            val bearing2 = calculateBearing(curr.latitude, curr.longitude, next.latitude, next.longitude)
            
            val turnAngle = (bearing2 - bearing1 + 360) % 360
            
            val turn = when {
                turnAngle < 30 || turnAngle > 330 -> Turn.GO_STRAIGHT
                turnAngle < 150 -> Turn.TURN_LEFT
                turnAngle < 210 -> Turn.U_TURN
                else -> Turn.TURN_RIGHT
            }
            
            val distance = calculateDistance(
                curr.latitude, curr.longitude,
                next.latitude, next.longitude
            )
            
            instructions.add(TurnInstruction(
                turn = turn,
                streetName = "Street ${i + 1}",
                distance = distance,
                bearing = bearing2,
                waypoints = listOf(curr, next)
            ))
        }
        
        // Add final arrival instruction
        instructions.add(TurnInstruction(
            turn = Turn.ARRIVE,
            streetName = "Destination",
            distance = 0.0,
            bearing = 0f,
            waypoints = listOf(points.last())
        ))
        
        return instructions
    }
}
