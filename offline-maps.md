# Offline Maps Management

AegisNav relies entirely on offline maps. These maps provide both visual guidance and routing data.

## Recommended Map Sources
- **OpenStreetMap (OSM)**: Free, open-source, global coverage.
- **Mapbox Offline Packs**: Paid option; allows vector tiles offline.
- **OSMDroid**: Android library supporting offline OSM tiles.

## Storage
- Maps stored in `/data/maps/`
- Recommended format: MBTiles (SQLite container)
- Ensure map files are <1GB per region for mobile devices

## Map Preprocessing
- Generate routing graphs (GraphHopper/OSRM)
- Extract Points of Interest (POIs) and landmarks
- Index landmarks for AR anchoring

## Updating Maps
- Updates must be downloaded via Wi-Fi
- Device offline operation: all new routing or search must use preloaded data
