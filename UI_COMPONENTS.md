# AegisNav UI Component Library Specifications
## Enterprise-Grade Component Definitions

---

## Color System Component

```dart
// lib/design/colors.dart
class AegisNavColors {
  // Primary Colors
  static const primary = Color(0xFF0E7AB5);      // Trust blue
  static const accent = Color(0xFF00D084);       // Success green
  static const warning = Color(0xFFFF9500);      // Alert orange
  static const error = Color(0xFFFF3B30);        // Error red

  // Backgrounds
  static const bgDark = Color(0xFF0A0E27);       // Primary bg
  static const bgSurface = Color(0xFF151B3D);    // Card bg
  static const bgSurfaceLight = Color(0xFF1E2747); // Elevated bg

  // Text
  static const textPrimary = Color(0xFFFFFFFF);
  static const textSecondary = Color(0xFFA8B5D1);
  static const textTertiary = Color(0xFF7A8AAF);
  
  // Borders
  static const border = Color(0xFF2D3A5C);

  // Status
  static const statusActive = Color(0xFF00D084);
  static const statusIdle = Color(0xFFFFA500);
  static const statusOffline = Color(0xFFFF3B30);
  static const statusCalibrating = Color(0xFF0E7AB5);
}
```

---

## Typography Component

```dart
// lib/design/typography.dart
class AegisNavTypography {
  static const display = TextStyle(
    fontFamily: 'Inter',
    fontSize: 32,
    fontWeight: FontWeight.w600,
    height: 1.2,
    color: AegisNavColors.textPrimary,
  );

  static const heading1 = TextStyle(
    fontFamily: 'Inter',
    fontSize: 24,
    fontWeight: FontWeight.w600,
    height: 1.3,
  );

  static const heading2 = TextStyle(
    fontFamily: 'Inter',
    fontSize: 20,
    fontWeight: FontWeight.w600,
    height: 1.3,
  );

  static const bodyLarge = TextStyle(
    fontFamily: 'Inter',
    fontSize: 16,
    fontWeight: FontWeight.w400,
    height: 1.5,
  );

  static const bodyRegular = TextStyle(
    fontFamily: 'Inter',
    fontSize: 14,
    fontWeight: FontWeight.w400,
    height: 1.5,
  );

  static const bodySmall = TextStyle(
    fontFamily: 'Inter',
    fontSize: 12,
    fontWeight: FontWeight.w400,
    height: 1.4,
  );

  static const label = TextStyle(
    fontFamily: 'Inter',
    fontSize: 11,
    fontWeight: FontWeight.w500,
    height: 1.3,
  );

  static const caption = TextStyle(
    fontFamily: 'Inter',
    fontSize: 10,
    fontWeight: FontWeight.w400,
    height: 1.2,
  );

  static const monosmall = TextStyle(
    fontFamily: 'IBM Plex Mono',
    fontSize: 12,
    fontWeight: FontWeight.w400,
  );
}
```

---

## Card Component (Reusable Panel)

```dart
// lib/ui/components/aegis_card.dart
class AegisCard extends StatelessWidget {
  final Widget child;
  final EdgeInsets? padding;
  final double? elevation;
  final Color? backgroundColor;
  final VoidCallback? onTap;

  const AegisCard({
    required this.child,
    this.padding = const EdgeInsets.all(16),
    this.elevation = 1,
    this.backgroundColor,
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Material(
      color: backgroundColor ?? AegisNavColors.bgSurface,
      elevation: elevation ?? 0,
      borderRadius: BorderRadius.circular(8),
      child: InkWell(
        onTap: onTap,
        child: Container(
          decoration: BoxDecoration(
            border: Border.all(
              color: AegisNavColors.border,
              width: 1,
            ),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Padding(
            padding: padding ?? EdgeInsets.zero,
            child: child,
          ),
        ),
      ),
    );
  }
}
```

---

## Turn Instruction Card

```dart
// lib/ui/components/turn_instruction_card.dart
class TurnInstructionCard extends StatelessWidget {
  final String turn; // "LEFT", "RIGHT", "STRAIGHT", "UTURN"
  final String street;
  final double distanceMeters;
  final double remainingKm;
  final int etaMinutes;
  final bool isCalibrated;

  const TurnInstructionCard({
    required this.turn,
    required this.street,
    required this.distanceMeters,
    required this.remainingKm,
    required this.etaMinutes,
    this.isCalibrated = true,
  });

  IconData _getTurnIcon() {
    switch (turn.toUpperCase()) {
      case 'LEFT':
        return Icons.turn_left;
      case 'RIGHT':
        return Icons.turn_right;
      case 'STRAIGHT':
        return Icons.arrow_upward;
      case 'UTURN':
        return Icons.u_turn_left;
      default:
        return Icons.arrow_forward;
    }
  }

  @override
  Widget build(BuildContext context) {
    return AegisCard(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Distance and turn icon
          Row(
            children: [
              Icon(
                _getTurnIcon(),
                color: AegisNavColors.primary,
                size: 32,
              ),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'In ${distanceMeters.toStringAsFixed(0)}m',
                    style: AegisNavTypography.bodySmall.copyWith(
                      color: AegisNavColors.textSecondary,
                    ),
                  ),
                  Text(
                    'TURN ${turn.toUpperCase()}',
                    style: AegisNavTypography.heading2.copyWith(
                      color: AegisNavColors.primary,
                    ),
                  ),
                ],
              ),
            ],
          ),
          const SizedBox(height: 12),
          // Street name
          Text(
            'onto $street',
            style: AegisNavTypography.bodyLarge,
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
          ),
          const SizedBox(height: 12),
          // Progress divider
          Container(
            height: 1,
            color: AegisNavColors.border,
          ),
          const SizedBox(height: 12),
          // Remaining info
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                '${remainingKm.toStringAsFixed(1)} km remaining',
                style: AegisNavTypography.bodySmall.copyWith(
                  color: AegisNavColors.textSecondary,
                ),
              ),
              Text(
                '$etaMinutes min ETA',
                style: AegisNavTypography.bodySmall.copyWith(
                  color: AegisNavColors.textSecondary,
                ),
              ),
            ],
          ),
          // Calibration status
          if (!isCalibrated)
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Text(
                '⚠ Recalibration needed',
                style: AegisNavTypography.caption.copyWith(
                  color: AegisNavColors.warning,
                ),
              ),
            )
          else
            Padding(
              padding: const EdgeInsets.only(top: 8),
              child: Text(
                '✓ Calibrated',
                style: AegisNavTypography.caption.copyWith(
                  color: AegisNavColors.accent,
                ),
              ),
            ),
        ],
      ),
    );
  }
}
```

---

## Status Indicator Component

```dart
// lib/ui/components/status_indicator.dart
class StatusIndicator extends StatelessWidget {
  final String label;
  final String value;
  final Color statusColor;
  final IconData icon;

  const StatusIndicator({
    required this.label,
    required this.value,
    required this.statusColor,
    required this.icon,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        border: Border.all(color: AegisNavColors.border),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, color: statusColor, size: 20),
          const SizedBox(height: 4),
          Text(
            value,
            style: AegisNavTypography.bodySmall.copyWith(
              color: statusColor,
              fontWeight: FontWeight.w600,
            ),
          ),
          Text(
            label,
            style: AegisNavTypography.caption.copyWith(
              color: AegisNavColors.textTertiary,
            ),
          ),
        ],
      ),
    );
  }
}
```

---

## Compass Heading Display

```dart
// lib/ui/components/compass_display.dart
class CompassDisplay extends StatefulWidget {
  final double heading; // 0-360 degrees
  final double accuracy; // ±X degrees
  final bool isCalibrating;

  const CompassDisplay({
    required this.heading,
    required this.accuracy,
    this.isCalibrating = false,
  });

  @override
  _CompassDisplayState createState() => _CompassDisplayState();
}

class _CompassDisplayState extends State<CompassDisplay>
    with SingleTickerProviderStateMixin {
  late AnimationController _animController;

  @override
  void initState() {
    super.initState();
    _animController = AnimationController(
      duration: const Duration(milliseconds: 500),
      vsync: this,
    );
  }

  @override
  void didUpdateWidget(CompassDisplay oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.heading != widget.heading) {
      _animController.forward(from: 0.0);
    }
  }

  String _getCardinalDirection(double heading) {
    if (heading < 22.5 || heading >= 337.5) return 'N';
    if (heading < 67.5) return 'NE';
    if (heading < 112.5) return 'E';
    if (heading < 157.5) return 'SE';
    if (heading < 202.5) return 'S';
    if (heading < 247.5) return 'SW';
    if (heading < 292.5) return 'W';
    return 'NW';
  }

  @override
  Widget build(BuildContext context) {
    return AegisCard(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          // Heading display
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Heading',
                    style: AegisNavTypography.label.copyWith(
                      color: AegisNavColors.textSecondary,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Row(
                    children: [
                      Text(
                        '${widget.heading.toStringAsFixed(0)}°',
                        style: AegisNavTypography.heading2.copyWith(
                          color: AegisNavColors.primary,
                          fontFamily: 'IBM Plex Mono',
                        ),
                      ),
                      const SizedBox(width: 8),
                      Text(
                        _getCardinalDirection(widget.heading),
                        style: AegisNavTypography.bodyLarge.copyWith(
                          color: AegisNavColors.textSecondary,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
              Container(
                width: 80,
                height: 80,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(
                    color: AegisNavColors.primary,
                    width: 2,
                  ),
                ),
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    // Compass rose
                    CustomPaint(
                      size: const Size(80, 80),
                      painter: CompassRosePainter(
                        heading: widget.heading,
                      ),
                    ),
                    // Center dot
                    Container(
                      width: 8,
                      height: 8,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: AegisNavColors.primary,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          const SizedBox(height: 12),
          // Accuracy bar
          Container(
            height: 1,
            color: AegisNavColors.border,
          ),
          const SizedBox(height: 12),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                'Accuracy: ±${widget.accuracy.toStringAsFixed(1)}°',
                style: AegisNavTypography.bodySmall.copyWith(
                  color: AegisNavColors.textSecondary,
                ),
              ),
              if (widget.isCalibrating)
                SizedBox(
                  width: 12,
                  height: 12,
                  child: CircularProgressIndicator(
                    strokeWidth: 2,
                    valueColor: AlwaysStoppedAnimation(
                      AegisNavColors.warning,
                    ),
                  ),
                )
              else
                Icon(
                  Icons.check_circle,
                  size: 16,
                  color: AegisNavColors.accent,
                ),
            ],
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _animController.dispose();
    super.dispose();
  }
}

// Custom painter for compass rose
class CompassRosePainter extends CustomPainter {
  final double heading;

  CompassRosePainter({required this.heading});

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = size.width / 2 - 4;

    // Rotate canvas by heading
    canvas.translate(center.dx, center.dy);
    canvas.rotate(-heading * (3.14159 / 180));
    canvas.translate(-center.dx, -center.dy);

    // Draw N indicator (north arrow)
    final paint = Paint()
      ..color = AegisNavColors.primary
      ..strokeWidth = 2
      ..strokeCap = StrokeCap.round;

    final northArrow = Offset(center.dx, center.dy - radius + 8);
    canvas.drawLine(center, northArrow, paint);
  }

  @override
  bool shouldRepaint(CompassRosePainter oldDelegate) {
    return oldDelegate.heading != heading;
  }
}
```

---

## Route Progress Bar Component

```dart
// lib/ui/components/route_progress.dart
class RouteProgressBar extends StatelessWidget {
  final double completion; // 0.0 to 1.0
  final String distanceRemaining;
  final String timeRemaining;

  const RouteProgressBar({
    required this.completion,
    required this.distanceRemaining,
    required this.timeRemaining,
  });

  @override
  Widget build(BuildContext context) {
    return AegisCard(
      padding: const EdgeInsets.all(12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Route Progress',
            style: AegisNavTypography.label.copyWith(
              color: AegisNavColors.textSecondary,
            ),
          ),
          const SizedBox(height: 8),
          // Progress bar
          ClipRRect(
            borderRadius: BorderRadius.circular(4),
            child: LinearProgressIndicator(
              value: completion,
              backgroundColor: AegisNavColors.bgDark,
              valueColor: AlwaysStoppedAnimation(
                AegisNavColors.accent,
              ),
              minHeight: 8,
            ),
          ),
          const SizedBox(height: 8),
          // Stats
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                '${(completion * 100).toStringAsFixed(0)}% complete',
                style: AegisNavTypography.bodySmall.copyWith(
                  color: AegisNavColors.textSecondary,
                ),
              ),
              Text(
                '$distanceRemaining remaining',
                style: AegisNavTypography.bodySmall.copyWith(
                  color: AegisNavColors.textSecondary,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
```

---

## Navigation Screen Layout (Main)

```dart
// lib/ui/screens/navigation_screen.dart
class NavigationScreen extends StatefulWidget {
  const NavigationScreen({Key? key}) : super(key: key);

  @override
  _NavigationScreenState createState() => _NavigationScreenState();
}

class _NavigationScreenState extends State<NavigationScreen> {
  // Sensor and navigation states would come from providers/bloc

  @override
  Widget build(BuildContext context) {
    final isPortrait =
        MediaQuery.of(context).orientation == Orientation.portrait;

    return Scaffold(
      backgroundColor: AegisNavColors.bgDark,
      body: isPortrait ? _buildPortraitLayout() : _buildLandscapeLayout(),
    );
  }

  Widget _buildPortraitLayout() {
    return Column(
      children: [
        // Status bar
        _buildStatusBar(),
        // Main content
        Expanded(
          child: Row(
            children: [
              // AR Camera Viewport (50%)
              Expanded(
                flex: 1,
                child: _buildCameraViewport(),
              ),
            ],
          ),
        ),
        // Navigation and Status Panels (50%)
        Expanded(
          flex: 1,
          child: _buildNavigationPanels(),
        ),
        // Bottom navigation
        _buildBottomNavigation(),
      ],
    );
  }

  Widget _buildLandscapeLayout() {
    return Column(
      children: [
        _buildStatusBar(),
        Expanded(
          child: Row(
            children: [
              // AR Camera (60%)
              Expanded(
                flex: 3,
                child: _buildCameraViewport(),
              ),
              // Navigation (25%)
              Expanded(
                flex: 1,
                child: SingleChildScrollView(
                  child: Padding(
                    padding: const EdgeInsets.all(8),
                    child: _buildNavigationCard(),
                  ),
                ),
              ),
              // Context (15%)
              Expanded(
                flex: 1,
                child: SingleChildScrollView(
                  child: Padding(
                    padding: const EdgeInsets.all(8),
                    child: _buildContextPanel(),
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildStatusBar() {
    return Container(
      height: 56,
      color: AegisNavColors.bgDark,
      border: Border(bottom: BorderSide(color: AegisNavColors.border)),
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          Text(
            DateTime.now().toString().split('.')[0],
            style: AegisNavTypography.bodySmall.copyWith(
              color: AegisNavColors.textSecondary,
            ),
          ),
          Row(
            children: [
              Icon(Icons.signal_cellular_4_bar,
                  color: AegisNavColors.textSecondary, size: 16),
              const SizedBox(width: 8),
              Icon(Icons.battery_full,
                  color: AegisNavColors.accent, size: 16),
              const SizedBox(width: 8),
              Icon(Icons.compass_calibration,
                  color: AegisNavColors.accent, size: 16),
              const SizedBox(width: 12),
              Icon(Icons.menu, color: AegisNavColors.textSecondary, size: 20),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildCameraViewport() {
    return Container(
      color: AegisNavColors.bgSurface,
      border: Border(right: BorderSide(color: AegisNavColors.border)),
      child: Stack(
        alignment: Alignment.center,
        children: [
          // Camera feed placeholder
          Container(
            color: Colors.black87,
            child: const Center(
              child: Text('AR Camera Feed'),
            ),
          ),
          // Heading arrow overlay
          Positioned(
            top: 40,
            child: Icon(Icons.arrow_upward,
                color: AegisNavColors.primary, size: 48),
          ),
          // Distance arc (bottom)
          Positioned(
            bottom: 20,
            child: Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              decoration: BoxDecoration(
                color: AegisNavColors.bgSurface.withOpacity(0.8),
                borderRadius: BorderRadius.circular(20),
              ),
              child: Text(
                '▼ 150m',
                style: AegisNavTypography.label.copyWith(
                  color: AegisNavColors.primary,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildNavigationPanels() {
    return SingleChildScrollView(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          children: [
            _buildNavigationCard(),
            const SizedBox(height: 12),
            _buildContextPanel(),
          ],
        ),
      ),
    );
  }

  Widget _buildNavigationCard() {
    return TurnInstructionCard(
      turn: 'LEFT',
      street: 'Main Street',
      distanceMeters: 150,
      remainingKm: 2.3,
      etaMinutes: 12,
      isCalibrated: true,
    );
  }

  Widget _buildContextPanel() {
    return AegisCard(
      padding: const EdgeInsets.all(12),
      child: Column(
        children: [
          CompassDisplay(
            heading: 342,
            accuracy: 4,
            isCalibrating: false,
          ),
          const SizedBox(height: 12),
          RouteProgressBar(
            completion: 0.65,
            distanceRemaining: '2.3 km',
            timeRemaining: '12 min',
          ),
          const SizedBox(height: 12),
          Text(
            'Landmarks: 3 visible',
            style: AegisNavTypography.bodySmall.copyWith(
              color: AegisNavColors.textSecondary,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildBottomNavigation() {
    return Container(
      height: 56,
      color: AegisNavColors.bgDark,
      border: Border(top: BorderSide(color: AegisNavColors.border)),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          _buildBottomNavItem(Icons.map, 'Map View'),
          _buildBottomNavItem(Icons.more_horiz, 'Options'),
          _buildBottomNavItem(Icons.sync, 'Calibrate'),
          _buildBottomNavItem(Icons.help, 'Help'),
        ],
      ),
    );
  }

  Widget _buildBottomNavItem(IconData icon, String label) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Icon(icon, color: AegisNavColors.primary, size: 20),
        const SizedBox(height: 2),
        Text(
          label,
          style: AegisNavTypography.caption.copyWith(
            color: AegisNavColors.textSecondary,
          ),
        ),
      ],
    );
  }
}
```

---

## Theme Configuration

```dart
// lib/design/theme.dart
ThemeData buildAegisNavTheme() {
  return ThemeData(
    useMaterial3: true,
    brightness: Brightness.dark,
    scaffoldBackgroundColor: AegisNavColors.bgDark,
    primaryColor: AegisNavColors.primary,
    colorScheme: ColorScheme.dark(
      primary: AegisNavColors.primary,
      secondary: AegisNavColors.accent,
      surface: AegisNavColors.bgSurface,
      background: AegisNavColors.bgDark,
      error: AegisNavColors.error,
      onPrimary: Colors.white,
      onSecondary: Colors.white,
      onSurface: AegisNavColors.textPrimary,
      onBackground: AegisNavColors.textPrimary,
      onError: Colors.white,
    ),
    appBarTheme: AppBarTheme(
      backgroundColor: AegisNavColors.bgDark,
      elevation: 0,
      iconTheme: const IconThemeData(color: AegisNavColors.textPrimary),
      titleTextStyle: AegisNavTypography.heading2.copyWith(
        color: AegisNavColors.textPrimary,
      ),
    ),
    inputDecorationTheme: InputDecorationTheme(
      filled: true,
      fillColor: AegisNavColors.bgSurface,
      border: OutlineInputBorder(
        borderRadius: BorderRadius.circular(8),
        borderSide: const BorderSide(color: AegisNavColors.border),
      ),
      enabledBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(8),
        borderSide: const BorderSide(color: AegisNavColors.border),
      ),
      focusedBorder: OutlineInputBorder(
        borderRadius: BorderRadius.circular(8),
        borderSide: const BorderSide(color: AegisNavColors.primary, width: 2),
      ),
    ),
  );
}
```

This component library provides a solid foundation for building the modern AegisNav UI with Anduril/Palantir enterprise aesthetics.
