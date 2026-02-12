README.md

AegisNav is a multi-platform AI navigation agent designed for GPS-denied or high-risk environments. It provides fully offline map-and-compass guidance using smartphone sensors (magnetometer, gyroscope, accelerometer) and pre-downloaded maps. By fusing these sensors, AegisNav maintains an accurate heading (within a few degrees even indoors) without any satellite or network connection. Users see AR overlays or map cues on their device showing the route to their destination, with turn-by-turn directions and landmarks rendered in the camera view (or on a map UI) entirely on-device.

The system uses open-source map data (e.g. OpenStreetMap) stored locally. Applications like OsmAnd and Organic Maps demonstrate this approach: they download country-wide vector maps so navigation and search work 100% offline. AegisNav builds on those ideas but adds an AR-compass interface and intelligent routing. Before heading out, the user downloads map regions (via Wi-Fi) so they have street-level maps offline. The app then computes routes on-device using a local routing engine (e.g. GraphHopper or OSRM) and guides the user step-by-step. All data stays on the device – no internet needed, no tracking – matching privacy-focused navigation apps.

Smartphone AR-compass technology underpins AegisNav’s guidance. Modern phones can overlay navigation cues on the camera feed by fusing magnetometer and gyroscope data. Crucially, AR-compass mode works entirely offline: it needs only orientation, not location fixes. For example, iOS’s Compass and Android’s AR Walking mode fuse sensors to keep “true north” accurate within ~2° even without GPS. Users should calibrate by waving the phone in a figure-eight (recalibrating the magnetometer) before starting. Once calibrated, the app can point arrows toward landmarks or the next waypoint and even display street names anchored to buildings.

AegisNav emphasizes robustness and security. In high-risk zones (e.g. disaster sites or contested regions), connectivity is unreliable and data can be sensitive. By design, AegisNav requires no network or GPS: it runs entirely on the user’s device. This means no remote servers to hack and no network needed for core functionality. Offline apps like Organic Maps prove this works: “download maps, throw away your SIM card, and go for a weeklong trip… without any byte sent to the network”. AegisNav similarly encrypts any stored data and uses minimal permissions (only sensors and offline maps) so that users’ routes and location remain private.

Finally, note that while some systems use hardware like drones or LiDAR for GPS-denied navigation, AegisNav focuses on personal navigation via common devices. Autonomous drones typically perform SLAM with cameras/LiDAR for mapping, but for walkers or teams on foot, smartphone AR and compass guidance is simpler and more practical. In short, AegisNav provides a self-contained, offline navigation tool: users load maps ahead of time, calibrate their device, and then let the AI agent guide them safely to their destination using local sensors and context. The system fuses open data, sensor fusion, AR UI, and secure offline algorithms to achieve reliable wayfinding without GPS or connectivity.

TODO.md

Gather and integrate offline map data: Decide on map format (e.g. OpenStreetMap vector/SQLite or MBTiles) and implement offline loading (using libraries like MapLibre or OsmAnd code).

Implement route planning engine: Embed or develop an on-device router (e.g. GraphHopper or OSRM in offline mode) to compute paths without internet. Ensure support for pedestrian and off-road modes for hikers and first responders.

Develop sensor fusion module: Read smartphone IMU (accelerometer, gyro, magnetometer) to track orientation and steps. Implement dead-reckoning (step counting and heading) and frequent calibration (figure-eight routine) to mitigate drift.

AR and UI development: Use ARKit (iOS) and ARCore (Android) or a cross-platform AR framework to overlay direction arrows and POI labels on the camera view. Handle offline labels (street names, landmarks) by anchoring them to user-downloaded map data. Design a fallback 2D map view if AR is unavailable.

Voice and visual guidance: Integrate text-to-speech for turn announcements in multiple languages. Implement concise on-screen instructions and icons (e.g. arrow, distance) for clarity in stressful situations.

Resilience and security: Encrypt any local data storage (maps, waypoints). Ensure minimal permissions (no unnecessary network or camera access beyond AR view). Include failsafes if sensors degrade (e.g. prompt recalibration).

Testing in GPS-denied scenarios: Simulate or field-test in areas without GPS (indoors, urban canyons, mines). Verify that AegisNav’s instructions remain accurate and that users can reach waypoints.

Optional hardware integration (future): Research connecting to ground drones or robots if available. For now, ensure architecture can accept external mapping data or sensor feeds if needed.

agent-instructions.md

Purpose: AegisNav’s agent provides offline navigational assistance. It must use preloaded map data and device sensors to guide the user from point A to B in high-risk, no-GPS conditions. The agent does not assume internet or GPS; it relies solely on onboard resources.

Capabilities:

Load and cache map regions (OpenStreetMap or similar) from device storage.

Compute routes using local routing data.

Read and fuse IMU sensor data (compass heading, step count) to estimate movement.

Overlay AR arrows or map symbols to indicate direction of travel and turns.

Respond to user queries like “How do I get to X?” by using offline data.

Provide voice cues and textual turn-by-turn instructions without cloud services.

Behavior:

Always confirm the user’s starting location on the offline map before routing. If unknown, ask user to select their point on the map.

Encourage calibration: instruct the user to perform a quick “figure-eight” motion with the phone to stabilize the compass.

Use the device’s compass to align the map to true north. Emphasize that AR markers show cardinal directions, not “forward”—the user should orient themselves accordingly.

If the user deviates from the path, the agent should automatically recompute the route (fully offline) and guide back to the original track.

Limit AR anchors to at most three per scene (a distant fixed landmark, a nearby feature, and one contextual cue) to avoid confusion.

Only present information from the offline database (no real-time POIs or traffic). Explain this limitation to the user: e.g. “Remember, all data is offline – I can’t update route for real-time conditions.”

Best Practices:

Begin answers with clear, concise directions. For example: “Head north 150 meters, then turn left.”

Cross-validate with environment: e.g., “Keep an eye on that church tower ahead as your anchor.” (Using AR anchors improves accuracy.)

Alert the user if accuracy might be low: e.g., “Your compass drifted; please recalibrate by circling.”

Maintain a calm, reassuring tone. In a rescue or security context, emphasize safety (“Proceed carefully. Look for visible landmarks”).

Always remind users to stay aware of surroundings – AegisNav augments, not replaces, human judgment.

project-structure.md

AegisNav is organized as follows:

/app/ – Mobile application source code (separate modules for Android and iOS or a shared cross-platform project). Contains UI components, AR overlay code, sensor access, and map handling.

/lib/ – Shared libraries and algorithms. Includes the offline routing engine, sensor-fusion algorithms (dead-reckoning, step detection), and utilities for map processing.

/data/ – Offline map and navigation data. This holds downloaded OSM map files (vector tiles or SQLite databases), POI lists, and any precomputed graph indices for routing.

/assets/ – Static assets used by the app: icons, AR marker models, language strings, and documentation.

/docs/ – Design documents and references, including this README and research notes.

/agents/ – (If using an AI agent framework) Agent configuration and skill definitions (e.g. openai.yaml, SKILLS.md).

/requirements.md – Lists software and hardware requirements.

/TODO.md, /agent-instructions.md – Project management and agent guidance files (as above).

This structure separates the user-facing app (/app) from core algorithms (/lib) and data assets (/data). It allows parallel development (e.g. Android vs iOS) while sharing routing and navigation logic.

SKILLS.md

Offline Mapping: Load and query local map data (roads, trails, landmarks) from OpenStreetMap without internet.

Route Planning: Compute shortest or safest paths on-demand using on-device routing (GraphHopper/OSRM) for walking, biking, etc.

Compass & Orientation: Read magnetometer and gyroscope to maintain true-heading orientation. Support calibration (e.g. figure-eight) to correct interference.

Dead-Reckoning: Use accelerometer/pedometer to estimate distance traveled from a known start point. Fuse with compass to track approximate location when GPS is off.

Augmented Reality Guidance: Overlay directional arrows and distance labels in the camera view. Anchor virtual markers to real-world landmarks to improve spatial awareness.

Turn-by-Turn Instructions: Present step-by-step directions via text, icons, and optional voice (offline TTS) for each segment of the route.

Privacy & Security: Operate entirely offline; encrypt stored maps and user data. Prevent any tracking or networking.

Resilience Checks: Detect sensor anomalies or drift. Prompt user to recalibrate if compass error exceeds a threshold.

Multi-language Support: Provide navigation prompts and UI in multiple languages (using built-in offline language packs).

User Interaction: Answer user questions about routes or landmarks using only onboard knowledge. Clarify that the agent cannot fetch new data while offline.

requirements.md

Platforms: Modern smartphone or tablet (Android 8+/ARCore or iOS 14+/ARKit) with magnetometer, accelerometer, and gyroscope. Multi-platform frameworks (e.g. Flutter, React Native with AR plugins, or native Kotlin/Swift) to target both iOS and Android.

Software: Offline map library (MapLibre GL or Mapsforge) for vector rendering; offline routing engine (GraphHopper/OSRM). Augmented reality SDK (Google ARCore, Apple ARKit) for camera overlays. On-device database (SQLite, MBTiles) for map storage.

Data: OpenStreetMap map extracts for target regions. POI and landmark datasets (could be from OSM) for offline use. Optionally, precomputed graph indexes for faster routing.

Algorithms: Sensor fusion code (complementary filter or Kalman filter) to combine magnetometer and gyro. Pedometer algorithm to count steps. Figure-eight calibration routine for compass. Optional lightweight ML model (TinyML) for step detection or image landmark recognition.

Security: Use device’s hardware-backed keystore or similar to encrypt stored map tiles and any logs. Limit app permissions to sensors and local storage only.

Dependencies: Open-source libraries for mapping (e.g. OsmDroid, OSMDroid for Android; Mapbox GL Native if using with offline packs), ARCore/ARKit frameworks, and any voice synthesis libraries for offline TTS.

Hardware (optional): Although not required, AegisNav’s design allows future integration with external sensors (e.g. a drone or robot streaming SLAM data) if available. Initial development focuses on handheld devices only.

Each requirement is chosen for offline, resilient operation. For example, OsmAnd demonstrates that routing and voice guidance can run entirely offline on device. By using similar tools and techniques, AegisNav will enable secure, self-contained navigation even where GPS and networks are unavailable.

Sources: Information adapted from research on offline navigation and AR-compass technology


