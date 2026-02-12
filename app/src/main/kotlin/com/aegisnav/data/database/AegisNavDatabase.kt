package com.aegisnav.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aegisnav.data.database.entities.RouteEntity
import com.aegisnav.data.database.entities.LocationEntity
import com.aegisnav.data.database.entities.POIEntity
import com.aegisnav.data.database.dao.RouteDao
import com.aegisnav.data.database.dao.LocationDao
import com.aegisnav.data.database.dao.POIDao

@Database(
    entities = [RouteEntity::class, LocationEntity::class, POIEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AegisNavDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
    abstract fun locationDao(): LocationDao
    abstract fun poiDao(): POIDao
}
