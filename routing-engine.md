# Offline Routing Engine

## Engines Supported
- **GraphHopper**: Java-based, supports offline routing
- **OSRM**: C++-based, high-performance offline router

## Features
- Turn-by-turn pedestrian navigation
- Support for trails, urban streets, and off-road paths
- Automatic recalculation if deviation occurs

## Preprocessing
- Import map data into routing engine
- Generate routing graphs (precompute shortest paths)
- Include POIs and landmarks for AR guidance

## Integration
- Route request → routing engine → sequence of waypoints → sensor fusion & AR overlay
