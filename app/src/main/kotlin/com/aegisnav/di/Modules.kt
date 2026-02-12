package com.aegisnav.di

import android.content.Context
import androidx.room.Room
import com.aegisnav.data.database.AegisNavDatabase
import com.aegisnav.data.services.MapService
import com.aegisnav.data.services.NavigationService
import com.aegisnav.data.services.RoutingService
import com.aegisnav.data.services.SensorFusionService
import com.aegisnav.data.services.VoiceGuidanceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSensorFusionService(
        @ApplicationContext context: Context
    ): SensorFusionService {
        return SensorFusionService(context)
    }

    @Singleton
    @Provides
    fun provideNavigationService(): NavigationService {
        return NavigationService()
    }

    @Singleton
    @Provides
    fun provideMapService(): MapService {
        return MapService()
    }

    @Singleton
    @Provides
    fun provideRoutingService(): RoutingService {
        return RoutingService()
    }

    @Singleton
    @Provides
    fun provideVoiceGuidanceService(
        @ApplicationContext context: Context
    ): VoiceGuidanceService {
        return VoiceGuidanceService(context)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAegisNavDatabase(
        @ApplicationContext context: Context
    ): AegisNavDatabase {
        return Room.databaseBuilder(
            context,
            AegisNavDatabase::class.java,
            "aegisnav_database"
        ).build()
    }
}
