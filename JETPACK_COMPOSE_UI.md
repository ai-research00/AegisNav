# AegisNav - Jetpack Compose UI Components
## Modern Android UI Implementation

---

## 1. Theme System

### Color.kt
```kotlin
package com.aegisnav.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors
val PrimaryBlue = Color(0xFF0E7AB5)
val AccentGreen = Color(0xFF00D084)
val WarningOrange = Color(0xFFFF9500)
val ErrorRed = Color(0xFFFF3B30)

// Backgrounds
val DarkBg = Color(0xFF0A0E27)
val SurfaceDark = Color(0xFF151B3D)
val SurfaceLight = Color(0xFF1E2747)

// Text
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA8B5D1)
val TextTertiary = Color(0xFF7A8AAF)

// Borders
val BorderColor = Color(0xFF2D3A5C)

// Status
val StatusActive = Color(0xFF00D084)
val StatusIdle = Color(0xFFFFA500)
val StatusOffline = Color(0xFFFF3B30)
val StatusCalibrating = Color(0xFF0E7AB5)
```

### Type.kt
```kotlin
package com.aegisnav.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AegisNavTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        color = TextPrimary,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = TextPrimary,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = TextPrimary,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        color = TextSecondary,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        color = TextSecondary,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        color = TextPrimary,
    ),
)
```

### Theme.kt
```kotlin
package com.aegisnav.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = AccentGreen,
    tertiary = WarningOrange,
    background = DarkBg,
    surface = SurfaceDark,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onError = Color.White,
)

@Composable
fun AegisNavTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AegisNavTypography,
        content = content,
    )
}
```

---

## 2. Reusable Components

### AegisCard.kt
```kotlin
package com.aegisnav.presentation.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aegisnav.presentation.ui.theme.BorderColor
import com.aegisnav.presentation.ui.theme.SurfaceDark

@Composable
fun AegisCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderColor, RoundedCornerShape(8.dp))
            .let { if (onClick != null) it.clickable(onClick = onClick) else it },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
    ) {
        content()
    }
}
```

### TurnInstructionCard.kt
```kotlin
package com.aegisnav.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.aegisnav.domain.model.TurnType
import com.aegisnav.presentation.ui.theme.*

@Composable
fun TurnInstructionCard(
    turn: TurnType,
    streetName: String,
    distanceMeters: Double,
    remainingKm: Double,
    etaMinutes: Int,
    isCalibrated: Boolean = true,
) {
    AegisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Turn icon and distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = getTurnIcon(turn),
                    contentDescription = turn.name,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = "In ${distanceMeters.toInt()}m",
                        style = AegisNavTypography.bodySmall,
                        color = TextSecondary,
                    )
                    Text(
                        text = "TURN ${turn.name}",
                        style = AegisNavTypography.headlineSmall,
                        color = PrimaryBlue,
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Street name
            Text(
                text = "onto $streetName",
                style = AegisNavTypography.bodyLarge,
                color = TextPrimary,
                maxLines = 1,
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Divider
            androidx.compose.material3.Divider(
                color = BorderColor,
                thickness = 1.dp,
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Route info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$remainingKm km remaining",
                    style = AegisNavTypography.bodySmall,
                    color = TextSecondary,
                )
                Text(
                    text = "$etaMinutes min ETA",
                    style = AegisNavTypography.bodySmall,
                    color = TextSecondary,
                )
            }
            
            // Calibration status
            if (!isCalibrated) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⚠ Recalibration needed",
                    style = AegisNavTypography.labelMedium,
                    color = WarningOrange,
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ Calibrated",
                    style = AegisNavTypography.labelMedium,
                    color = AccentGreen,
                )
            }
        }
    }
}

@Composable
private fun getTurnIcon(turn: TurnType): ImageVector {
    return when (turn) {
        TurnType.LEFT -> Icons.Default.TurnLeft
        TurnType.RIGHT -> Icons.Default.TurnRight
        TurnType.STRAIGHT -> Icons.Default.ArrowUpward
        TurnType.UTURN -> Icons.Default.UTurnLeft
        TurnType.ARRIVAL -> Icons.Default.CheckCircle
    }
}
```

### CompassDisplay.kt
```kotlin
package com.aegisnav.presentation.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.aegisnav.presentation.ui.theme.*

@Composable
fun CompassDisplay(
    heading: Double,
    accuracy: Double,
    isCalibrating: Boolean = false,
) {
    AegisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Heading display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Heading",
                        style = AegisNavTypography.labelMedium,
                        color = TextSecondary,
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row {
                        Text(
                            text = String.format("%.0f°", heading),
                            style = AegisNavTypography.headlineSmall,
                            color = PrimaryBlue,
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = getCardinalDirection(heading),
                            style = AegisNavTypography.bodyLarge,
                            color = TextSecondary,
                        )
                    }
                }
                
                // Compass rose
                CompassRose(
                    heading = heading,
                    modifier = Modifier.size(80.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Divider
            androidx.compose.material3.Divider(
                color = BorderColor,
                thickness = 1.dp,
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Accuracy info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Accuracy: ±${String.format("%.1f", accuracy)}°",
                    style = AegisNavTypography.bodySmall,
                    color = TextSecondary,
                )
                
                if (isCalibrating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 1.dp,
                        color = WarningOrange,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Calibrated",
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CompassRose(
    heading: Double,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .border(2.dp, PrimaryBlue, CircleShape),
        onDraw = {
            rotate((-heading).toFloat(), pivot = center) {
                val arrowLength = size.width / 3
                drawLine(
                    color = PrimaryBlue,
                    start = center,
                    end = Offset(center.x, center.y - arrowLength),
                    strokeWidth = 2f,
                )
            }
            
            // Center dot
            drawCircle(
                color = PrimaryBlue,
                radius = 4.dp.toPx(),
                center = center,
            )
        }
    )
}

@Composable
private fun getCardinalDirection(heading: Double): String {
    return when {
        heading < 22.5 || heading >= 337.5 -> "N"
        heading < 67.5 -> "NE"
        heading < 112.5 -> "E"
        heading < 157.5 -> "SE"
        heading < 202.5 -> "S"
        heading < 247.5 -> "SW"
        heading < 292.5 -> "W"
        else -> "NW"
    }
}
```

### RouteProgressBar.kt
```kotlin
package com.aegisnav.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aegisnav.presentation.ui.theme.*

@Composable
fun RouteProgressBar(
    completion: Float,  // 0.0 to 1.0
    distanceRemaining: String,
    timeRemaining: String,
) {
    AegisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Route Progress",
                style = AegisNavTypography.labelMedium,
                color = TextSecondary,
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = completion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = AccentGreen,
                trackColor = DarkBg,
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "${(completion * 100).toInt()}% complete",
                    style = AegisNavTypography.bodySmall,
                    color = TextSecondary,
                )
                Text(
                    text = "$distanceRemaining remaining",
                    style = AegisNavTypography.bodySmall,
                    color = TextSecondary,
                )
            }
        }
    }
}
```

---

## 3. Main Navigation Screen

### NavigationScreen.kt
```kotlin
package com.aegisnav.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aegisnav.presentation.ui.component.*
import com.aegisnav.presentation.ui.theme.*
import com.aegisnav.presentation.viewmodel.NavigationViewModel
import com.aegisnav.presentation.state.NavigationUiState

@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = DarkBg,
        topBar = { StatusBar() },
        bottomBar = { BottomNavigation() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
                .padding(paddingValues)
        ) {
            when (uiState) {
                is NavigationUiState.Idle -> {
                    IdleScreen()
                }
                is NavigationUiState.Loading -> {
                    LoadingScreen()
                }
                is NavigationUiState.Navigating -> {
                    val state = (uiState as NavigationUiState.Navigating).state
                    ActiveNavigationScreen(state)
                }
                is NavigationUiState.Error -> {
                    val message = (uiState as NavigationUiState.Error).message
                    ErrorScreen(message)
                }
            }
        }
    }
}

@Composable
private fun StatusBar() {
    TopAppBar(
        title = { },
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBg),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBg,
        ),
        actions = {
            Text("08:34", style = AegisNavTypography.labelMedium)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Info, null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.BatteryStd, null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(16.dp))
        }
    )
}

@Composable
private fun ActiveNavigationScreen(
    navigationState: com.aegisnav.domain.model.NavigationState,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // AR Camera Viewport (45%)
        Box(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .background(Color.Black)
                .border(1.dp, BorderColor),
            contentAlignment = Alignment.Center,
        ) {
            Text("AR Camera Feed", color = TextSecondary)
        }

        // Navigation Panel (55%)
        Column(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            // Turn instruction
            navigationState.currentRoute?.turns
                ?.getOrNull(navigationState.nextTurnIndex)
                ?.let { turn ->
                    TurnInstructionCard(
                        turn = turn.turn,
                        streetName = turn.streetName,
                        distanceMeters = turn.distanceMeters,
                        remainingKm = navigationState.distanceRemaining / 1000,
                        etaMinutes = navigationState.minutesRemaining,
                    )
                }

            Spacer(modifier = Modifier.height(12.dp))

            // Status and context panels
            CompassDisplay(heading = 342.0, accuracy = 4.0)
            Spacer(modifier = Modifier.height(12.dp))
            
            RouteProgressBar(
                completion = 0.65f,
                distanceRemaining = String.format("%.1f km", navigationState.distanceRemaining / 1000),
                timeRemaining = "${navigationState.minutesRemaining} min",
            )
        }
    }
}

@Composable
private fun IdleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Ready for Navigation", style = AegisNavTypography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {}) {
            Text("Start Navigation")
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = PrimaryBlue)
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Error", style = AegisNavTypography.headlineSmall, color = ErrorRed)
        Spacer(modifier = Modifier.height(8.dp))
        Text(message, style = AegisNavTypography.bodyMedium)
    }
}

@Composable
private fun BottomNavigation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(DarkBg)
            .border(1.dp, BorderColor),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NavigationBarItem("Map View", Icons.Default.Map)
        NavigationBarItem("Options", Icons.Default.MoreVert)
        NavigationBarItem("Calibrate", Icons.Default.Sync)
        NavigationBarItem("Help", Icons.Default.Help)
    }
}

@Composable
private fun NavigationBarItem(label: String, icon: androidx.compose.material.icons.Icons.Default) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, style = AegisNavTypography.labelMedium, color = TextSecondary)
    }
}
```

---

## 4. AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.aegisnav">

    <!-- Sensors -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    
    <!-- Storage -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    
    <!-- Camera for AR -->
    <uses-permission android:name="android.permission.CAMERA" />
    
    <!-- Audio for TTS -->
    <uses-permission android:name="android.permission.RECORD_AUDIO" />

    <uses-feature android:name="android.hardware.sensor.compass" />
    <uses-feature android:name="android.hardware.sensor.accelerometer" />
    <uses-feature android:name="android.hardware.sensor.gyroscope" />
    <uses-feature android:name="android.hardware.camera.ar" android:required="false" />

    <application
        android:name=".AegisNavApplication"
        android:allowBackup="false"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.AegisNav">

        <activity
            android:name=".presentation.MainActivity"
            android:exported="true"
            android:screenOrientation="portrait">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## 5. Build & Run

```bash
# Build APK
./gradlew build

# Install and run
./gradlew installDebug
adb shell am start -n com.aegisnav/.presentation.MainActivity

# View logs
adb logcat | grep AegisNav
```

---

**Status**: ✅ Complete Kotlin + Jetpack Compose UI Layer  
**Next**: Integrate offline maps and routing  
**Estimated Hours**: Phase 1 Complete (~40 hours)  
