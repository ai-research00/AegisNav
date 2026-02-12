# AegisNav Design System
## Modern Enterprise Design (Anduril/Palantir Aesthetics)

### Color Palette

#### Primary Colors
- **Primary Blue**: `#0E7AB5` — Trust, navigation, primary actions
- **Accent Green**: `#00D084` — Success, confirmed routes, active status
- **Warning Orange**: `#FF9500` — Alerts, recalibration, deviation warnings
- **Error Red**: `#FF3B30` — Critical issues, lost signal, emergency

#### Background & Neutrals
- **Dark Background**: `#0A0E27` — Primary background (very dark blue-black)
- **Surface Dark**: `#151B3D` — Card and panel backgrounds
- **Surface Light**: `#1E2747` — Elevated surfaces
- **Text Primary**: `#FFFFFF` — Main text
- **Text Secondary**: `#A8B5D1` — Secondary info
- **Text Tertiary**: `#7A8AAF` — Disabled/subtle text
- **Border**: `#2D3A5C` — Dividers and borders

#### Status Indicators
- **Active/Online**: `#00D084`
- **Idle**: `#FFA500`
- **Offline/Error**: `#FF3B30`
- **Calibrating**: `#0E7AB5`

### Typography

#### Font Families
- **Primary Font**: "Inter" or "IBM Plex Sans" (clean, professional, excellent for data displays)
- **Monospace**: "IBM Plex Mono" (for metrics, distances, coordinates)

#### Font Scales
- **Display**: 32px, weight 600, line-height 1.2 (screen titles)
- **Heading 1**: 24px, weight 600, line-height 1.3
- **Heading 2**: 20px, weight 600, line-height 1.3
- **Body Large**: 16px, weight 400, line-height 1.5
- **Body Regular**: 14px, weight 400, line-height 1.5
- **Body Small**: 12px, weight 400, line-height 1.4
- **Label**: 11px, weight 500, line-height 1.3 (buttons, tags)
- **Caption**: 10px, weight 400, line-height 1.2 (small hints)

### Spacing System

```
4px   — Tight spacing
8px   — Small gap
12px  — Default padding
16px  — Standard margin
24px  — Large spacing
32px  — Extra large spacing
48px  — Section spacing
```

### Component Elevations

- **Level 0**: Flat surfaces
- **Level 1**: `rgba(14, 122, 181, 0.1)` shadow
- **Level 2**: `rgba(14, 122, 181, 0.15)` shadow
- **Level 3**: `rgba(14, 122, 181, 0.25)` shadow (floating panels)

### Border Radius

- **Sharp**: 0px (maps, AR viewport)
- **Small**: 4px (buttons, small cards)
- **Medium**: 8px (input fields, standard cards)
- **Large**: 12px (modal dialogs)
- **Full**: 50% (circular badges, avatars)

### Icons

- **Style**: Minimalist, 24x24px grid
- **Weight**: 2px stroke for consistency
- **Library**: Heroicons, Feather Icons, or custom SVG set

### Opacity Scale

- **Full**: 1.0 (primary content)
- **High**: 0.87 (secondary content)
- **Medium**: 0.60 (tertiary/disabled)
- **Low**: 0.38 (subtle hints)

### Interactions

- **Hover**: Increase opacity +10%, slight color shift
- **Active**: Increase opacity +15%, add border highlight
- **Disabled**: Reduce opacity to 0.5, grayscale
- **Transition**: 200ms ease-in-out for smooth interactions

### Data Visualization

- **Primary Metric**: Large, bold, primary blue
- **Secondary Metric**: Smaller, secondary color
- **Gauge**: Circular progress with gradient
- **Timeline**: Horizontal line with markers
- **Map**: Dark theme with high-contrast labels

### Accessibility

- **Contrast Ratio**: Minimum 4.5:1 for text
- **Touch Targets**: Minimum 44x44px
- **Focus Indicators**: Bright highlight, 2px border
- **Motion**: Respect `prefers-reduced-motion` system setting
