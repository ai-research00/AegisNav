package com.aegisnav.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey val id: String,
    val startLat: Double,
    val startLon: Double,
    val endLat: Double,
    val endLon: Double,
    val distance: Double,
    val duration: Long,
    val timestamp: Long
)

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val bearing: Float,
    val speed: Float,
    val timestamp: Long
)

@Entity(tableName = "pois")
data class POIEntity(
    @PrimaryKey val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val description: String,
    val rating: Float
)
