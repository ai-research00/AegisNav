package com.aegisnav.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aegisnav.data.database.entities.RouteEntity
import com.aegisnav.data.database.entities.LocationEntity
import com.aegisnav.data.database.entities.POIEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Insert
    suspend fun insertRoute(route: RouteEntity)

    @Update
    suspend fun updateRoute(route: RouteEntity)

    @Delete
    suspend fun deleteRoute(route: RouteEntity)

    @Query("SELECT * FROM routes WHERE id = :id")
    suspend fun getRoute(id: String): RouteEntity?

    @Query("SELECT * FROM routes ORDER BY timestamp DESC")
    fun getAllRoutes(): Flow<List<RouteEntity>>

    @Query("DELETE FROM routes")
    suspend fun clearAll()
}

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE timestamp >= :startTime")
    suspend fun getLocationsSince(startTime: Long): List<LocationEntity>

    @Query("SELECT * FROM locations ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentLocations(limit: Int): List<LocationEntity>

    @Query("DELETE FROM locations WHERE timestamp < :beforeTime")
    suspend fun deleteOldLocations(beforeTime: Long)

    @Query("DELETE FROM locations")
    suspend fun clearAll()
}

@Dao
interface POIDao {
    @Insert
    suspend fun insertPOI(poi: POIEntity)

    @Insert
    suspend fun insertPOIs(pois: List<POIEntity>)

    @Update
    suspend fun updatePOI(poi: POIEntity)

    @Delete
    suspend fun deletePOI(poi: POIEntity)

    @Query("SELECT * FROM pois WHERE id = :id")
    suspend fun getPOI(id: String): POIEntity?

    @Query("SELECT * FROM pois WHERE type = :type")
    fun getPOIsByType(type: String): Flow<List<POIEntity>>

    @Query("SELECT * FROM pois ORDER BY rating DESC")
    fun getAllPOIs(): Flow<List<POIEntity>>

    @Query("DELETE FROM pois")
    suspend fun clearAll()
}
