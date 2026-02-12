package com.aegisnav.data.services

import com.aegisnav.domain.models.Location
import com.aegisnav.domain.models.POI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages offline map operations
 */
@Singleton
class MapService @Inject constructor() {
    private val _mapBoundsFlow = MutableStateFlow<Pair<Location, Location>?>(null)
    val mapBoundsFlow = _mapBoundsFlow.asStateFlow()
    
    private val _poiFlow = MutableStateFlow<List<POI>>(emptyList())
    val poiFlow = _poiFlow.asStateFlow()
    
    private val poiCache = mutableListOf<POI>()

    fun loadOfflineMap(bounds: Pair<Location, Location>) {
        _mapBoundsFlow.value = bounds
    }

    fun addPOI(poi: POI) {
        poiCache.add(poi)
        _poiFlow.value = poiCache.toList()
    }

    fun addPOIs(pois: List<POI>) {
        poiCache.addAll(pois)
        _poiFlow.value = poiCache.toList()
    }

    fun queryPOIsNearLocation(location: Location, radiusKm: Double): List<POI> {
        return poiCache.filter { poi ->
            val dx = (poi.location.latitude - location.latitude) * 111.0
            val dy = (poi.location.longitude - location.longitude) * 111.0 * 
                    kotlin.math.cos(kotlin.math.toRadians(location.latitude.toDouble()))
            val distance = kotlin.math.sqrt(dx * dx + dy * dy)
            distance <= radiusKm
        }
    }

    fun clearPOIs() {
        poiCache.clear()
        _poiFlow.value = emptyList()
    }

    fun getPOIs(): List<POI> = poiCache.toList()
}
