# AegisNav Project Structure

```
AegisNav/
├── /app/                          # Mobile application (cross-platform)
│   ├── /android/                  # Android-specific code (Kotlin)
│   ├── /ios/                      # iOS-specific code (Swift)
│   └── /shared/                   # Shared cross-platform code
│       ├── /ui/
│       │   ├── /components/       # Reusable UI components
│       │   ├── /screens/          # Full screen layouts
│       │   ├── /theme/            # Design tokens, colors, typography
│       │   └── /styles/           # Global styles and layouts
│       ├── /navigation/           # Navigation logic and routing
│       ├── /services/             # Core services (sensor, routing, map)
│       └── /state/                # State management (Redux/BLoC)
│
├── /lib/                          # Core algorithms and engines
│   ├── /routing/                  # GraphHopper/OSRM integration
│   ├── /sensors/                  # Sensor fusion algorithms
│   ├── /maps/                     # Offline map handling
│   ├── /ar/                       # AR calculation and anchoring
│   └── /crypto/                   # Encryption utilities
│
├── /data/                         # Offline data and assets
│   ├── /maps/                     # MBTiles/SQLite map files
│   ├── /pois/                     # Points of Interest databases
│   └── /graphs/                   # Precomputed routing graphs
│
├── /design-system/                # Design tokens and UI patterns
│   ├── /colors/                   # Color palette (Anduril/Palantir inspired)
│   ├── /typography/               # Font definitions
│   ├── /icons/                    # Icon library
│   └── /components/               # Component specifications
│
├── /docs/                         # Documentation
│   ├── /architecture/
│   ├── /design/
│   ├── /api/
│   └── /deployment/
│
├── /tests/                        # Testing
│   ├── /unit/
│   ├── /integration/
│   └── /e2e/
│
├── /assets/                       # Static assets
│   ├── /images/
│   ├── /fonts/
│   ├── /sounds/
│   └── /models/                   # 3D AR models
│
├── /agents/                       # AI agent configurations
│   ├── agent-instructions.md      # Agent behavior and capabilities
│   └── SKILLS.md                  # Agent skills definition
│
├── config.md                      # Configuration guidelines
├── offline-maps.md                # Offline maps documentation
├── sensor-fusion.md               # Sensor fusion specifications
├── ar-integration.md              # AR integration guide
├── routing-engine.md              # Routing engine documentation
├── privacy-security.md            # Privacy and security specs
├── dev-roadmap.md                 # Development roadmap
├── ui-design.md                   # UI/UX design guidelines
├── requirements.md                # Technical requirements
└── README.md                      # Project overview
```

## Directory Purposes

- **/app/**: User-facing mobile applications with cross-platform architecture
- **/lib/**: Reusable algorithms and core business logic
- **/data/**: Offline maps, POIs, and routing data
- **/design-system/**: Centralized design tokens and component library
- **/docs/**: Architecture and API documentation
- **/tests/**: Automated testing suites
- **/assets/**: Media and 3D models for AR
- **/agents/**: AI agent skill definitions and behaviors

## Technology Stack

- **Frontend**: Flutter (cross-platform) or React Native + native modules
- **Backend Logic**: Dart/Kotlin/Swift native implementations
- **Maps**: MapLibre GL + MBTiles
- **Routing**: GraphHopper or OSRM (offline)
- **AR**: ARCore (Android), ARKit (iOS), or Unity AR Foundation
- **Sensors**: Native IMU APIs (Android Sensor Framework, Core Motion)
- **Database**: SQLite for local storage
- **Testing**: Mockito, test, integration_test
