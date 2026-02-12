package com.aegisnav.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aegisnav.domain.models.Turn
import com.aegisnav.domain.models.TurnInstruction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

/**
 * Reusable card component with consistent styling
 */
@Composable
fun AegisCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}

/**
 * Turn instruction card showing navigation guidance
 */
@Composable
fun TurnInstructionCard(
    instruction: TurnInstruction,
    distanceToTurn: Double,
    modifier: Modifier = Modifier
) {
    AegisCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Turn icon
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = getTurnIcon(instruction.turn),
                        contentDescription = instruction.turn.name,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                Spacer(modifier = Modifier.padding(12.dp))
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = instruction.turn.toReadableString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = instruction.streetName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = String.format("%.0f m", distanceToTurn * 1000),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Bearing: ${instruction.bearing.toInt()}°",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Compass display showing heading with accuracy
 */
@Composable
fun CompassDisplay(
    heading: Float,
    accuracy: Float,
    modifier: Modifier = Modifier
) {
    AegisCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Heading",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.1f°", heading),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Accuracy",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("±%.1f°", accuracy),
                        style = MaterialTheme.typography.titleSmall,
                        color = when {
                            accuracy < 5f -> MaterialTheme.colorScheme.primary
                            accuracy < 10f -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(((360 - heading) / 360).coerceIn(0f, 1f))
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}

/**
 * Route progress indicator
 */
@Composable
fun RouteProgressBar(
    progress: Float,
    distanceRemaining: Double,
    modifier: Modifier = Modifier
) {
    AegisCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Route Progress",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Remaining",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format("%.2f km", distanceRemaining),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

// Helper function to get turn icon
private fun getTurnIcon(turn: Turn): ImageVector {
    return when (turn) {
        Turn.TURN_LEFT, Turn.FORK_LEFT, Turn.EXIT_LEFT -> Icons.Filled.TrendingUp
        Turn.TURN_RIGHT, Turn.FORK_RIGHT, Turn.EXIT_RIGHT -> Icons.Filled.TrendingDown
        else -> Icons.Filled.TrendingFlat
    }
}

// Helper function to convert turn enum to readable string
private fun Turn.toReadableString(): String {
    return when (this) {
        Turn.TURN_LEFT -> "Turn Left"
        Turn.TURN_RIGHT -> "Turn Right"
        Turn.GO_STRAIGHT -> "Go Straight"
        Turn.U_TURN -> "U-Turn"
        Turn.FORK_LEFT -> "Fork Left"
        Turn.FORK_RIGHT -> "Fork Right"
        Turn.MERGE -> "Merge"
        Turn.EXIT_LEFT -> "Exit Left"
        Turn.EXIT_RIGHT -> "Exit Right"
        Turn.ROUNDABOUT -> "Roundabout"
        Turn.ARRIVE -> "Arrive"
    }
}
