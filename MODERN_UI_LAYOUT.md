# AegisNav Modern UI Layout System
## Split-Screen Design with Camera Viewport + Data Panels

### Layout Philosophy

**Camera never occupies the entire screen.** The interface balances:
- **AR Camera Feed** (40-50% of screen): Real-time visual guidance
- **Navigation Panel** (25-30%): Turn-by-turn, distance, next maneuver
- **Status & Context** (20-25%): Compass, sensors, route progress, alerts

This design maintains situational awareness while providing critical navigation data without context switching.

---

## Screen Layouts

### 1. Navigation (Primary Screen)

```
┌─────────────────────────────────────────┐
│         STATUS BAR (4% height)          │
│  [Time] [Signal] [Battery] [Compass]   │
├──────────────────┬──────────────────────┤
│                  │                      │
│   AR CAMERA      │  NAVIGATION PANEL    │
│   VIEWPORT       │  ┌──────────────────┤
│   (45% width)    │  │ Turn in 150m      │
│                  │  │ [Turn Icon]       │
│   Live compass   │  │ Left onto Oak St  │
│   overlay        │  │                   │
│                  │  │ ├─ 2.3km remain  │
│   Distance arc   │  │ ├─ 12 min ETA    │
│   at bottom      │  │ └─ Calibrated    │
│                  │  └──────────────────┤
│                  │  CONTEXT PANEL      │
│                  │  ┌──────────────────┤
│                  │  │ [Route Progress] │
│                  │  │ ████████░░░░░░░░ │
│                  │  │ Heading: 342°N   │
│                  │  │ Accuracy: ±4°   │
│                  │  │ Landmarks: 3     │
│                  │  └──────────────────┤
└──────────────────┴──────────────────────┘
```

### 2. Landscape Orientation (Wider Screens)

```
┌────────────────────────────────────────────────────────┐
│              STATUS BAR (compact)                      │
│    [Compass] [Battery] [Signal] [Calibration]         │
├──────────────────┬──────────────────────┬──────────────┤
│                  │                      │              │
│   AR CAMERA      │  NAVIGATION PANEL    │  STATS &    │
│   VIEWPORT       │  ┌──────────────────┤  CONTEXT    │
│   (50% width)    │  │ Next: Turn Left  │  ┌─────────┤
│                  │  │ Distance: 250m   │  │ Progress│
│   Live heading   │  │ Time: 3 min      │  │ 65% →   │
│   with arrow     │  │                  │  │         │
│                  │  │ [Route Preview]  │  │ Heading │
│   Distance gauge │  │ Mini 3D View     │  │ 045° NE │
│   at corner      │  │                  │  │         │
│                  │  │ [Landmarks Ahead]│  │ Accuracy│
│                  │  │ • Tower (250m)   │  │ ±3°    │
│                  │  │ • Church (400m)  │  └─────────┤
│                  │  └──────────────────┘              │
└──────────────────┴──────────────────────┴──────────────┘
```

### 3. Portrait Orientation (Phone)

```
┌─────────────────────────────────────┐
│  [↑] [⚡] [📡] [⊙] [☰]  08:34       │
├─────────────────────────────────────┤
│                                     │
│   AR CAMERA VIEWPORT (50%)          │
│                                     │
│   [Live heading overlay]            │
│   [Distance arc indicator]          │
│                                     │
├─────────────────────────────────────┤
│  TURN INSTRUCTION                   │
│  ┌───────────────────────────────┐  │
│  │ ↖  In 150m                    │  │
│  │ TURN LEFT                     │  │
│  │ onto Main Street              │  │
│  │                               │  │
│  │ ━━━━━━━━━━━━━━━━░░░░░░░░░░░  │
│  │ 2.3 km remaining  •  12 min  │  │
│  └───────────────────────────────┘  │
├─────────────────────────────────────┤
│  STATUS INDICATORS                  │
│  ┌───────────────────────────────┐  │
│  │ Compass: 342°N  [●●●●●●●○○]  │  │
│  │ Accuracy: ±4m drift           │  │
│  │ Landmarks visible: 3          │  │
│  │ Next waypoint: 450m           │  │
│  └───────────────────────────────┘  │
├─────────────────────────────────────┤
│  [Options ☰] [Map View] [Calibrate] │
└─────────────────────────────────────┘
```

---

## Component Details

### AR Camera Viewport
- **Dimensions**: 40-50% of screen width/height
- **Border**: Subtle 1px border, `#2D3A5C`
- **Overlay Elements**:
  - Central crosshair (reticle)
  - Heading arrow (points to next waypoint)
  - Distance arc (bottom, shows remaining distance)
  - Landmark anchors (max 3 virtual markers)
  - Calibration hint (flashing figure-eight if needed)

### Navigation Panel
- **Dimensions**: Remaining right/bottom space
- **Background**: `#151B3D` (slightly elevated)
- **Content Hierarchy**:
  1. **Turn Instruction** (largest, primary blue)
  2. **Distance & Time** (secondary text)
  3. **Route Progress Bar** (visual indication)
  4. **Next Landmarks** (contextual cues)
  5. **Waypoint Name/POI** (if available)

### Status & Context Panel
- **Compact Data Display**:
  - Compass heading (numeric + visual indicator)
  - Accuracy meter (±X meters/degrees)
  - Visible landmarks count
  - Sensor status (✓ magnetometer, ✓ gyro, ✗ accel)
  - Route completion percentage
  - ETA or time remaining

### Status Bar
- **Fixed at top**, `#0A0E27`
- **Elements** (right-aligned):
  - System time (12:34)
  - Signal indicator (Wi-Fi/offline badge)
  - Battery percentage
  - Compass calibration indicator
  - Menu/settings icon

### Bottom Navigation (Mobile)
- **Fixed at bottom**, 56px height
- **Buttons**: Map View, Options, Calibrate, Help
- **Spacing**: 16px horizontal padding, centered

---

## Interaction States

### Camera Viewport
- **Idle**: Faded, shows static landmark data
- **Active Navigation**: Full brightness, live heading arrow
- **Calibrating**: Pulsing border, figure-eight hint overlay
- **Error**: Red tint, "Recalibrate" prompt

### Navigation Panel
- **Normal**: High contrast, primary blue headings
- **Off-route**: Orange alert bar at top, new route highlighted
- **Arrival**: Green checkmark, celebration animation
- **Searching**: Loading spinner

### Status Indicators
- **Optimal**: Green dots
- **Warning**: Orange/yellow dots
- **Error**: Red dots with exclamation
- **Calibrating**: Blue pulsing indicator

---

## Responsive Behavior

| Device Type | Camera % | Nav Panel % | Orientation | Notes |
|---|---|---|---|---|
| Phone Portrait | 50 | 50 | Portrait | Full-height camera, turn instruction below |
| Phone Landscape | 60 | 40 | Landscape | Side-by-side layout |
| Tablet Portrait | 45 | 55 | Portrait | Larger navigation panel, more context |
| Tablet Landscape | 50 | 25 + 25 | Landscape | Split nav and context panels |
| AR Glasses | 80 | 20 | Fixed | Minimal overlay, focus on real-world view |

---

## Dark Mode Adherence

- All backgrounds use `#0A0E27` (primary), `#151B3D` (secondary)
- Text hierarchy: white > secondary gray > tertiary gray
- Accent colors maintain contrast ratio ≥ 4.5:1
- No bright white backgrounds to prevent eye strain
- Subtle elevation shadows using primary blue with opacity

---

## Animation & Transitions

- **Panel transitions**: 300ms ease-out
- **Turn instruction change**: 200ms fade + slide-up
- **Compass needle**: Smooth 100ms rotation
- **Distance updates**: Subtle number transition (no flashing)
- **Alert appearance**: 150ms slide-in from top
- **Calibration pulse**: 1.5s cycle, gentle opacity change

---

## Accessibility

- **Touch Targets**: All interactive elements ≥ 44x44px
- **Text Size**: Minimum 12px (caption), adjustable by user
- **Color**: Never rely solely on color; use icons + labels
- **Focus**: Bright 2px border highlight, no disappearing focus
- **Voice**: Screen reader support for all navigation cues
- **Motion**: Respect `prefers-reduced-motion` (disable pulsing, animations)
