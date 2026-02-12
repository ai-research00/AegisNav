# Augmented Reality (AR) Integration

## Purpose
- Overlay navigation cues on camera feed
- Anchor direction arrows to landmarks or waypoints
- Improve situational awareness in GPS-denied areas

## Supported Platforms
- Android: ARCore
- iOS: ARKit
- Cross-platform: Unity AR Foundation or Flutter AR plugins

## AR Anchors
- Maximum of 3 per frame:
  1. Distant landmark (tower, mountain)
  2. Nearby waypoint
  3. Contextual cue (street sign, path)
- Distance range: 5-50 meters
- Visual style: high contrast, minimalist, semi-transparent

## Fallback
- 2D top-down map view if AR not available
