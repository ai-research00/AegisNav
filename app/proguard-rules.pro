-keep class com.aegisnav.** { *; }
-keepclassmembers class com.aegisnav.** { *; }

# Hilt
-keep class dagger.hilt.android.internal.managers.** { *; }
-keep class com.aegisnav.di.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

# Room
-keep class androidx.room.** { *; }
-keepattributes *Annotation*

# Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Data classes
-keepclassmembers class com.aegisnav.domain.models.** { *; }
-keepclassmembers class com.aegisnav.data.database.entities.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

# Timber
-keep class timber.log.** { *; }
