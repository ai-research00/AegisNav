package com.aegisnav.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.hiltViewModel
import com.aegisnav.domain.models.Location
import com.aegisnav.domain.models.NavigationState
import com.aegisnav.ui.components.CompassDisplay
import com.aegisnav.ui.components.RouteProgressBar
import com.aegisnav.ui.components.TurnInstructionCard
import com.aegisnav.ui.viewmodels.NavigationViewModel

/**
 * Main navigation screen with split layout (camera + navigation info)
 */
@Composable
fun NavigationScreen(
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val navigationState by viewModel.navigationStateFlow.collectAsState()
    val isNavigating by viewModel.isNavigatingFlow.collectAsState()
    val errorMessage by viewModel.errorMessageFlow.collectAsState()

    if (!isNavigating) {
        NavigationSetupScreen(viewModel)
    } else {
        ActiveNavigationScreen(navigationState, viewModel)
    }
}

/**
 * Screen for setting up navigation
 */
@Composable
private fun NavigationSetupScreen(
    viewModel: NavigationViewModel
) {
    var startLat by remember { mutableStateOf("0.0") }
    var startLon by remember { mutableStateOf("0.0") }
    var endLat by remember { mutableStateOf("0.0") }
    var endLon by remember { mutableStateOf("0.0") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AegisNav",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Start Navigation",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = startLat,
            onValueChange = { startLat = it },
            label = { Text("Start Latitude") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        TextField(
            value = startLon,
            onValueChange = { startLon = it },
            label = { Text("Start Longitude") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        TextField(
            value = endLat,
            onValueChange = { endLat = it },
            label = { Text("End Latitude") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        TextField(
            value = endLon,
            onValueChange = { endLon = it },
            label = { Text("End Longitude") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Button(
            onClick = {
                try {
                    val start = Location(startLat.toDouble(), startLon.toDouble())
                    val end = Location(endLat.toDouble(), endLon.toDouble())
                    viewModel.startNavigation(start, end)
                } catch (e: Exception) {
                    // Show error
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Start Navigation", style = MaterialTheme.typography.titleMedium)
        }
    }
}

/**
 * Active navigation screen showing route guidance
 */
@Composable
private fun ActiveNavigationScreen(
    navigationState: NavigationState,
    viewModel: NavigationViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Camera area (45%)
        Box(
            modifier = Modifier
                .fillMaxSize(0.45f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AR Camera Feed\n(MapLibre/ARCore)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Navigation info (55%)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Navigation",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Current instruction
            navigationState.currentInstruction?.let { instruction ->
                TurnInstructionCard(
                    instruction = instruction,
                    distanceToTurn = navigationState.distanceToNextTurn,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Compass
            navigationState.currentHeading?.let { heading ->
                CompassDisplay(
                    heading = heading.heading,
                    accuracy = heading.accuracy,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Route progress
            RouteProgressBar(
                progress = navigationState.progress,
                distanceRemaining = navigationState.remainingDistance,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stop button
            Button(
                onClick = { viewModel.stopNavigation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Stop Navigation", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
