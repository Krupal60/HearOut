# Material 3 Expressive Component Library

This document details all Expressive components introduced at **Google I/O '25 & '26** available in
`androidx.compose.material3`. These components focus on emotion-driven UX through motion, morphing,
tactile feedback, and expanded customization.

**Stable:** `1.4.0` | **Latest Alpha:** `1.5.0-alpha21` (June 2026)

---

## 1. Buttons & Selection

### Split Button

Dual-action component combining a primary action with a trailing dropdown menu. Features corner
morphing animation on interaction.

- **Annotation:** `@ExperimentalMaterial3ExpressiveApi`
- **Sizes:** XS through XL (5 sizes matching label & icon buttons)
- **Styles:** Elevated, Filled, Tonal, Outlined

```kotlin
SplitButton(
    onPrimaryClick = { /* Primary action */ },
    onSecondaryClick = { /* Dropdown action */ },
    primaryButton = {
        SplitButtonDefaults.LeadingButton(
            onClick = { /* ... */ }
        ) {
            Text("Save")
        }
    },
    secondaryButton = {
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
    }
)
```

### Button Group (Connected)

Groups related buttons into a cohesive container with shape-shifting "bump" animations on
interaction. Supports single and multi-selection.

- **Sizes:** All button sizes from XS to XL

```kotlin
ButtonGroup {
    ToggleableButton(
        selected = isSelected,
        onSelectedChange = { isSelected = it },
        colors = ButtonGroupDefaults.toggleableButtonColors()
    ) {
        Text("Option A")
    }
    ToggleableButton(/* ... */) { Text("Option B") }
    ToggleableButton(/* ... */) { Text("Option C") }
}
```

### Floating Toolbar

Dynamic toolbar that floats above content. Can be horizontal or vertical, frequently paired with a
FAB.

- **Usage:** Photo editing tools, media controls, contextual actions

```kotlin
FloatingToolbar(
    expanded = true,
    leadingContent = { /* Icons/Actions */ },
    trailingContent = { /* FAB or Secondary Actions */ }
)
```

### Docked Toolbar

Replaces the deprecated Bottom App Bar. Shorter, more flexible, holds controls and can be paired
with a FAB.

```kotlin
DockedToolbar(
    content = {
        IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = {}) { Icon(Icons.Default.Search, null) }
    }
)
```

### Toolbar (General)

Flexible component to display frequently used actions. Holds a variety of controls like buttons,
toggles, and can be paired with a FAB.

```kotlin
Toolbar(
    navigationContent = { IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, null) } },
    titleContent = { Text("Title") },
    actionContent = { IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, null) } }
)
```

### Common Button Updates

All buttons (Button, FilledTonalButton, OutlinedButton, TextButton, ElevatedButton) now offer:

- **5 Sizes:** XS, S, M, L, XL via `contentPaddingFor(size)`
- **Shape Morphing:** `shapes` parameter animates between interactive states
- **Squared & Rounded Variants:** `ButtonShape.Squared` / `ButtonShape.Rounded`

```kotlin
Button(
    onClick = { },
    shape = ButtonDefaults.buttonShape(ButtonShape.Rounded),
    contentPadding = ButtonDefaults.contentPaddingFor(ButtonSize.Large)
) {
    Icon(Icons.Default.Favorite, null)
    Spacer(Modifier.width(8.dp))
    Text("Large Rounded Button")
}
```

### FAB & Extended FAB Updates

- **New Medium Size:** `FabSize.Medium` for foldables and medium windows
- **New Color Mappings:** 3 tonal options via `FilledTonalFabDefaults`

```kotlin
FloatingActionButton(
    onClick = { },
    size = FabSize.Medium,
    containerColor = MaterialTheme.colorScheme.tertiaryContainer
) {
    Icon(Icons.Default.Add, null)
}
```

### FAB Menu

Brings back the speed-dial pattern from M2. Provides easy access to related actions with expressive
unfurling motion.

- **Opens from:** Any FAB size, compatible with any FAB color style

```kotlin
FABMenu(
    expanded = isExpanded,
    onExpandedChange = { isExpanded = it },
    fab = {
        FloatingActionButton(onClick = { isExpanded = !isExpanded }) {
            Icon(Icons.Default.Share, null)
        }
    }
) {
    FABMenuItem(
        onClick = { /* Share to Email */ },
        icon = { Icon(Icons.Default.Email, null) },
        label = { Text("Email") }
    )
    FABMenuItem(
        onClick = { /* Share to SMS */ },
        icon = { Icon(Icons.Default.Sms, null) },
        label = { Text("SMS") }
    )
}
```

---

## 2. Progress & Loading

### Loading Indicator (Polygon Morphing)

An indeterminate indicator that fluidly morphs between geometric polygons. Designed for content
loading under 5 seconds. Replaces most indeterminate circular progress indicators.

```kotlin
// (Stable in 1.5.0-alpha21)
LoadingIndicator(
    polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
)
```

### Contained Loading Indicator

Loading indicator within a colored container shape.

```kotlin
ContainedLoadingIndicator(
    polygons = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons
)
```

### Wavy Progress Indicators

Expressive variant of linear and circular progress using organic wavy animation instead of flat
bars.

```kotlin
WavyLinearProgressIndicator(
    progress = { 0.5f },
    trackHeight = 8.dp
)

WavyCircularProgressIndicator(
    progress = { 0.75f }
)
```

---

## 3. Advanced List Items

### Expressive List Item (2026)

Evolved `ListItem` supporting segmented styling, interactive child components, toggles, and
different selection states that automatically transform the container.

```kotlin
@ExperimentalMaterial3ExpressiveApi
@Composable
fun ExpressiveListItem(
    headlineContent = { Text("Title") },
    supportingContent = { Text("Description") },
    leadingContent = { Icon(Icons.Default.Star, null) },
    trailingContent = { Switch(checked = false, onCheckedChange = {}) }
)
```

---

## 4. Navigation

### Navigation Suite Scaffold

Automatically adapts navigation UI based on window size class:

- **Mobile:** Bottom Navigation Bar
- **Tablet:** Navigation Rail (collapsible)
- **Desktop:** Navigation Rail (expanded)

```kotlin
NavigationSuiteScaffold(
    navigationSuiteItems = {
        item(
            selected = isSelected,
            onClick = { /* ... */ },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        item(
            selected = false,
            onClick = { /* ... */ },
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search") }
        )
    }
) {
    // Screen Content
}
```

### Navigation Bar (Updated)

Revised colors making active item more visible. Flexible layout with vertical or horizontal icon
arrangement for medium windows.

```kotlin
NavigationBar(
    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    NavigationBarItem(
        selected = true,
        onClick = { },
        icon = { Icon(Icons.Default.Home, null) },
        label = { Text("Home") }
    )
}
```

### Navigation Rail (Updated)

Now features both collapsed and expanded states, replacing the previously separate navigation
drawer.

```kotlin
NavigationRail(
    header = { FloatingActionButton(onClick = {}) { Icon(Icons.Default.Add, null) } }
) {
    NavigationRailItem(
        selected = true,
        onClick = { },
        icon = { Icon(Icons.Default.Home, null) },
        label = { Text("Home") }
    )
}
```

---

## 5. Text Fields & Input

### Secure Text Fields (1.4.0+)

Dedicated password entry fields with disabled cut/copy/drag for security.

```kotlin
var password by remember { mutableStateOf("") }

SecureTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("Password") }
)

OutlinedSecureTextField(
    value = password,
    onValueChange = { password = it },
    label = { Text("Confirm Password") }
)
```

### Text Field Expressive Styles (1.5.0-alpha21+)

New `roundedShape`, `tonalColors()`, and `TextFieldLabelPosition.Inside` with minimized/expanded
alignment control.

```kotlin
TextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Label") },
    shape = TextFieldDefaults.roundedShape(),
    colors = TextFieldDefaults.tonalColors()
)

// Inside label with always-minimized option
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Inside Label") },
    labelPosition = TextFieldLabelPosition.Inside(
        isAlwaysMinimized = false,
        minimizedAlignment = Alignment.Start,
        expandedAlignment = Alignment.Start
    )
)
```

---

## 6. Search

### Search Bars (1.4.0+/1.5.0+)

Reworked into separate composables for collapsed and expanded states with dedicated navigation and
action slots.

```kotlin
val searchState = rememberSearchBarState()

SearchBar(
    state = searchState,
    inputField = {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = { query = it },
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, null) }
        )
    },
    expanded = false
)

// Expanded variants:
ExpandedFullScreenSearchBar(/* ... */) { }
ExpandedDockedSearchBar(/* ... */) { }
```

### Search Bar with App Bar

New AppBar-with-search composable with dedicated slots for navigation and actions outside the search
container.

```kotlin
AppBarWithSearch(
    state = searchState,
    navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, null) } },
    actionContent = {
        IconButton(onClick = {}) { Icon(Icons.Default.FilterList, null) }
    },
    inputField = {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = { },
            placeholder = { Text("Search rooms...") }
        )
    }
)
```

---

## 7. Pickers & Dialogs

### Time Picker Dialog (1.4.0+)

Unified dialog hosting clock-style picker, text input, or switchable mode.

```kotlin
var showTimePicker by remember { mutableStateOf(false) }
val state = rememberTimePickerState(initialHour = 14, initialMinute = 30)

Button(onClick = { showTimePicker = true }) {
    Text("Select Time")
}

if (showTimePicker) {
    TimePickerDialog(
        onDismissRequest = { showTimePicker = false },
        confirmButton = {
            TextButton(onClick = { showTimePicker = false }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
        }
    ) {
        TimePicker(state = state)
    }
}
```

### Time Picker Shapes (1.5.0-alpha21+)

Customizable shapes for time fields and period selector.

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val shapes = TimePickerShapes(
        timeFieldShape = RoundedCornerShape(12.dp),
        periodSelectorShape = RoundedCornerShape(24.dp)
    )

TimePicker(
    state = state,
    shapes = shapes
)
```

---

## 8. Carousel

### Horizontal Centered Hero Carousel (1.4.0+)

Horizontal carousel that centers one large "hero" item between two smaller items.

```kotlin
val state = rememberCarouselState { items.count() }

HorizontalCenteredHeroCarousel(
    state = state,
    modifier = Modifier.fillMaxWidth().height(220.dp),
    itemSpacing = 8.dp,
    contentPadding = PaddingValues(horizontal = 16.dp)
) { index ->
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Text(
            text = items[index].title,
            modifier = Modifier.padding(16.dp)
        )
    }
}
```

---

## 9. Layout & Utility

### Vertical Drag Handle (1.4.0+)

Capsule-shaped drag handle for resizable panes and Bottom Sheets.

```kotlin
VerticalDragHandle(
    modifier = Modifier.padding(12.dp),
    sizes = VerticalDragHandleDefaults.sizes(width = 32.dp, height = 4.dp),
    colors = VerticalDragHandleDefaults.colors()
)
```

### Menu Updates (1.5.0-alpha21+)

- Refined submenu motion
- Expanded shape and color palette
- New `Gaps` layout feature for breathing room between items
- `horizontalArrangement` parameter on `MenuItems`

```kotlin
DropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false }
) {
    MenuItems(
        items = listOf("Item 1", "Item 2", "Item 3"),
        onClick = { /* ... */ },
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    )
}
```

---

## 10. Typography

### Emphasized Type Styles

Extended type scale with stronger weight variants for editorial impact.

```kotlin
Text(
    text = "Emphasized Headline",
    style = MaterialTheme.typography.displayLarge,
    fontWeight = FontWeight.Bold
)

Text(
    text = "Regular Body",
    style = MaterialTheme.typography.bodyLarge,
    fontWeight = FontWeight.Normal
)
```

### Variable Font Animation

Utilize variable font axes (Roboto Flex) for dynamic weight/width animation.

```kotlin
val animatedWeight by animateFloatAsState(
    targetValue = if (isFocused) 800f else 400f,
    animationSpec = spring(dampingRatio = 0.5f)
)

Text(
    text = "HOTEL DESK",
    style = MaterialTheme.typography.displayLarge.copy(
        fontVariationSettings = "'wght' $animatedWeight"
    )
)
```

---

## 11. Shapes & Motion

### Shape Library

35 new decorative shapes plus 10 radius values on the shape scale. Shape morph animation built into
buttons and button groups.

```kotlin
// Shape scale: extraSmall, small, medium, large, extraLarge,
//               smallIncreased, mediumIncreased, largeIncreased, extraLargeIncreased, full
Surface(
    shape = MaterialTheme.shapes.extraLarge,
    color = MaterialTheme.colorScheme.surfaceContainerLow
) { /* ... */ }
```

### Motion Scheme

Token-based physics system with spring tokens replacing fixed durations.

```kotlin
MaterialExpressiveTheme(
    motionScheme = MotionScheme(
        spatial = MotionTokens.spatialStandard,
        expressive = MotionTokens.expressiveBouncy
    )
) {
    // Content
}
```

---

## Complete Component Inventory (I/O '25 + '26)

| Category    | Component                                                    | Status      |
|-------------|--------------------------------------------------------------|-------------|
| **New**     | SplitButton                                                  | Expressive  |
| **New**     | ButtonGroup / ToggleableButton                               | Expressive  |
| **New**     | FloatingToolbar                                              | Expressive  |
| **New**     | DockedToolbar (replaces BottomAppBar)                        | Expressive  |
| **New**     | LoadingIndicator / ContainedLoadingIndicator                 | Expressive  |
| **New**     | FABMenu                                                      | Expressive  |
| **New**     | NavigationSuiteScaffold                                      | Adaptive    |
| **New**     | HorizontalCenteredHeroCarousel                               | 1.4.0       |
| **New**     | VerticalDragHandle                                           | 1.4.0       |
| **New**     | SecureTextField / OutlinedSecureTextField                    | 1.4.0       |
| **New**     | TimePickerDialog                                             | 1.4.0       |
| **New**     | AppBarWithSearch                                             | 1.5.0-alpha |
| **New**     | ExpandedFullScreenSearchBar                                  | 1.5.0-alpha |
| **New**     | ExpandedDockedSearchBar                                      | 1.5.0-alpha |
| **New**     | MaterialExpressiveTheme                                      | 1.5.0-alpha |
| **Updated** | Button (5 sizes, shape morph, squared/rounded)               | Expressive  |
| **Updated** | FAB / ExtendedFAB (medium size, 3 color maps)                | Expressive  |
| **Updated** | IconButton (new sizes)                                       | Expressive  |
| **Updated** | NavigationBar (revised colors, flex layout)                  | Expressive  |
| **Updated** | NavigationRail (collapsed/expanded states)                   | Expressive  |
| **Updated** | ProgressIndicator (wavy style, track height)                 | Expressive  |
| **Updated** | ListItem (expressive variant, toggles, selection)            | 2026        |
| **Updated** | TopAppBar                                                    | Expressive  |
| **Updated** | Carousel (text support, overflow config)                     | Updated     |
| **Updated** | TextField / OutlinedTextField (rounded, tonal, inside label) | 1.5.0-alpha |
| **Updated** | TimePicker (TimePickerShapes)                                | 1.5.0-alpha |
| **Updated** | Menu (submenu motion, gaps, colors/shapes)                   | 1.5.0-alpha |
| **Updated** | Slider (expressive styles)                                   | Expressive  |
