# AegisNav Configuration Guidelines

## Map Data
- Format: MBTiles or SQLite vector tiles (OpenStreetMap recommended)
- Storage: /data/maps/
- Preprocessing: Precompute routing graphs for pedestrian navigation.
- Region Selection: Allow user to select map regions to download offline.

## Sensor Calibration
- Compass: Figure-eight calibration required before first use.
- Gyroscope/Accelerometer: Optional bias correction during startup.
- Calibration Routine: Prompt user on startup for ~10s of figure-eight motion.

## AR Overlay Settings
- Max anchors per frame: 3
- Display distance: 5-50 meters
- Visual Style: Minimalist, high contrast for low-light conditions
- Fallback: 2D map view if AR not supported

## Routing
- Engine: GraphHopper or OSRM offline
- Modes: Pedestrian, urban trails, off-road
- Re-routing: Automatic if deviation > 10m

## Security
- Map tiles encrypted on device
- Local database access only
- No external network calls
