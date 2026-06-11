# M3 Expressive Reference Library

This directory contains the complete design and implementation documentation for Material 3
Expressive within the HotelDesk project. Updated for Google I/O '26 (M3E + 1.5.x).

---

## Quick Links

| Document                                               | Description                                                       |
|:-------------------------------------------------------|-------------------------------------------------------------------|
| [**Main Design Guide**](MATERIAL3_EXPRESSIVE_GUIDE.md) | Core design principles, color, typography, shape, motion          |
| [**Component Library**](components.md)                 | Full inventory of 25+ new/updated components with code examples   |
| [**Implementation Guide**](implementation_guide.md)    | Technical setup, dependencies, theme, motion, migration checklist |

---

## Component Inventory (I/O '25 + '26)

### New Components

See [Components: 1. Buttons & Selection](components.md#1-buttons--selection) — SplitButton,
ButtonGroup, FloatingToolbar, DockedToolbar
See [Components: 2. Progress & Loading](components.md#2-progress--loading) — LoadingIndicator,
ContainedLoadingIndicator
See [Components: 7. Pickers & Dialogs](components.md#7-pickers--dialogs) — TimePickerDialog
See [Components: 5. Text Fields & Input](components.md#5-text-fields--input) — SecureTextField,
OutlinedSecureTextField
See [Components: 8. Carousel](components.md#8-carousel) — HorizontalCenteredHeroCarousel
See [Components: 9. Layout & Utility](components.md#9-layout--utility) — VerticalDragHandle
See [Components: 6. Search](components.md#6-search) — AppBarWithSearch,
ExpandedFullScreenSearchBar, ExpandedDockedSearchBar
See [Components: 4. Navigation](components.md#4-navigation) — NavigationSuiteScaffold
See [Components: 1. Buttons & Selection](components.md#fab-menu) — FABMenu
See [Implementation: 2. Theme Setup](implementation_guide.md#2-theme-setup) —
MaterialExpressiveTheme, expressiveLightColorScheme

### Updated Components

See [Components: 1. Buttons & Selection](components.md#common-button-updates) — Button (5 sizes,
shape morph, squared/rounded variants)
See [Components: 1. Buttons & Selection](components.md#fab--extended-fab-updates) — FAB /
ExtendedFAB (medium size, 3 color mappings)
See [Components: 4. Navigation](components.md#navigation-bar-updated) — NavigationBar,
NavigationRail
See [Components: 2. Progress & Loading](components.md#wavy-progress-indicators) —
ProgressIndicator (wavy style)
See [Components: 5. Text Fields & Input](components.md#text-field-expressive-styles-150-alpha21) —
TextField / OutlinedTextField (rounded, tonal, inside label)
See [Components: 7. Pickers & Dialogs](components.md#time-picker-shapes-150-alpha21) —
TimePicker (TimePickerShapes)
See [Components: 9. Layout & Utility](components.md#menu-updates-150-alpha21) — Menu (gaps,
submenu motion)
See [Components: 3. Advanced List Items](components.md#3-advanced-list-items) — ListItem (
expressive variant)

---

## External References

- [**Material 3 Expressive Blog**](https://m3.material.io/blog/building-with-m3-expressive) — Full
  introduction to M3E
- [**What's New at I/O '26**](https://m3.material.io/blog/whats-new-at-io26) — Latest M3 updates
- [**Material Design 3 (Official)**](https://m3.material.io/) — Core documentation
- [**Compose M3 API Reference
  **](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary) —
  API docs
- [**Compose M3 Releases
  **](https://developer.android.com/jetpack/androidx/releases/compose-material3) — Release notes
- [**Material Expressive Figma Kit**](https://www.figma.com/community/file/1035203688168086460) —
  Design assets
- [**M3E Design Research
  **](https://design.google/library/expressive-material-design-google-research) — UX research
  findings

---

## Version Tracking

| Version        | Date    | Key Changes                                                                  |
|:---------------|---------|:-----------------------------------------------------------------------------|
| 1.3.x          | I/O '24 | Initial expressive APIs (SplitButton, LoadingIndicator, WavyProgress)        |
| 1.4.0          | Sep '25 | Stable release: HeroCarousel, DragHandle, SecureTextField, TimePickerDialog  |
| 1.5.0-alpha19+ | May '26 | MaterialExpressiveTheme, MotionScheme, expressiveLightColorScheme            |
| 1.5.0-alpha21  | Jun '26 | Material3ExpressiveApi, TextField rounded/tonal, TimePickerShapes, Menu gaps |

---

## Project Patterns

See detailed patterns
in [MATERIAL3_EXPRESSIVE_GUIDE.md](MATERIAL3_EXPRESSIVE_GUIDE.md#6-component-patterns)
and [implementation_guide.md](implementation_guide.md#6-common-design-patterns).

- **Sticky Layouts:** Primary actions always visible at
  bottom — [Guide §6](MATERIAL3_EXPRESSIVE_GUIDE.md#sticky-bottom-bars--headers)
- **Expressive Shapes:** `24.dp` and `CircleShape` for high-impact
  areas — [Guide §4](MATERIAL3_EXPRESSIVE_GUIDE.md#4-shape-language)
- **Variable Type:** Headers for improved
  scannability — [Guide §2](MATERIAL3_EXPRESSIVE_GUIDE.md#2-typography-the-roboto-flex-system)
- **Spring Motion:** Prefer `spring()` over `tween()` for interactive
  elements — [Impl §3](implementation_guide.md#3-expressive-motion-spring-physics)
- **Secure Fields:** Use `SecureTextField` for all password
  inputs — [Impl §6](implementation_guide.md#password-field-secure-text-field)
- **Loading States:** Use `LoadingIndicator` (morphing polygons) for <5s
  loads — [Components §2](components.md#loading-indicator-polygon-morphing)
- **Navigation:** Use `NavigationSuiteScaffold` for adaptive
  nav — [Components §4](components.md#navigation-suite-scaffold)
- **Bottom Actions:** Replace `BottomAppBar` with
  `DockedToolbar` — [Components §1](components.md#docked-toolbar)
