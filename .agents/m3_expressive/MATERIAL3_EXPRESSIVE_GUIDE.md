# Material 3 Expressive Design Guide (HotelDesk)

This guide outlines the implementation of **Material 3 Expressive (M3 Expressive)** principles
within the HotelDesk project. M3 Expressive builds on Material 3 to create more emotionally
resonant, personal, and modern user interfaces. Updated for Google I/O '26.

---

## 1. Core Principles

Material 3 Expressive is defined by "emotion-driven UX" — moving from static, clean designs toward
interfaces that feel "alive." Backed by 46 research studies with 18,000+ participants.

- **Dynamic Variable Typography** (Roboto Flex) with emphasized styles
- **Rich, Vibrant Color Palettes** with higher chroma and brighter text/icons
- **Expanded Shape Library** — 35 decorative shapes, 10 radius values, shape morph motion
- **Spring-Based Physics & Fluid Motion** — tokenized motion scheme
- **Component Flexibility** — adaptive, configurable, multiple sizes and styles

---

## 2. Typography (The Roboto Flex System)

HotelDesk uses **Roboto Flex** variable font for dynamic scaling and emphasis.

### Type Scale with Emphasized Styles

The M3 Expressive type scale adds new emphasized variants (bolder weights):

| Style           |   Weight | Usage                         |
|:----------------|---------:|:------------------------------|
| `displayLarge`  |     Bold | Hero moments, splash screens  |
| `headlineLarge` |     Bold | Section landing headers       |
| `titleLarge`    | SemiBold | Main section headers (sticky) |
| `titleMedium`   | SemiBold | Card titles, sub-headers      |
| `bodyLarge`     |   Normal | Primary content               |
| `labelMedium`   |   Medium | Metadata, dates, categories   |
| `labelSmall`    |   Medium | Supporting labels             |

### Implementation

```kotlin
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.25).sp
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    )
)
```

### Variable Font Animation

```kotlin
val animatedWeight by animateFloatAsState(
    targetValue = if (isFocused) 800f else 400f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
)

Text(
    text = "HOTEL DESK",
    style = MaterialTheme.typography.displayLarge.copy(
        fontVariationSettings = "'wght' $animatedWeight"
    )
)
```

---

## 3. Color & Surfaces

M3 Expressive uses a deeper tonal palette. The `expressiveLightColorScheme()` provides brighter
on-container colors and higher chroma.

### Color Tokens in HotelDesk

```kotlin
// Theme setup
MaterialExpressiveTheme(
    colorScheme = expressiveLightColorScheme(),
    shapes = AppShapes,
    motionScheme = motionScheme
) { /* ... */ }
```

| Token                  | Usage                               | Expressive Change              |
|:-----------------------|-------------------------------------|:-------------------------------|
| `surfaceContainerLow`  | Primary item cards (Rooms, Parties) | Soft, non-intrusive background |
| `surfaceContainerHigh` | Input sections, grouping containers | Depth + focus                  |
| `primaryContainer`     | Active editing states               | Highlight                      |
| `onPrimaryContainer`   | Text on primary surfaces            | Brighter (Primary30)           |
| `onSecondaryContainer` | Text on secondary surfaces          | Brighter (Secondary30)         |
| `onTertiaryContainer`  | Tertiary surface text               | Brighter (Tertiary30)          |

### High Contrast

Important actions (Save) use `primary` color with high tonal contrast. Surface containers use
`surfaceContainerHigh` for input regions.

```kotlin
Surface(
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
    shape = RoundedCornerShape(24.dp)
) {
    // Input fields
}
```

---

## 4. Shape Language

Expressive shapes are more distinct and varied. The shape scale now mirrors the type scale with 10
levels.

### Shape Scale

```kotlin
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
    full = CircleShape
)
```

### Component Shape Guidelines

| Component                            |                             Shape |         Token         |
|:-------------------------------------|----------------------------------:|:---------------------:|
| **Main Cards** (RoomItem, PartyItem) |                           `24.dp` |     `extraLarge`      |
| **Input Fields**                     |                           `16.dp` |        `large`        |
| **Primary Action (Save)**            |          `CircleShape` or `24.dp` | `full` / `extraLarge` |
| **Secondary Buttons**                |            `12.dp` rounded square |       `medium`        |
| **Selected Item Border**             | 1-2dp `primary` border on `16.dp` |        `large`        |

### Shape Morph

Buttons and button groups use shape morph animation on press:

```kotlin
Button(
    onClick = { },
    shape = ButtonDefaults.buttonShape(ButtonShape.Rounded) // morphs on press
) {
    Text("Interactive Shape")
}
```

---

## 5. Motion & Animation

Spring-based physics replace fixed durations. Two motion schemes:

### Standard (Efficient)

```kotlin
spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
```

### Expressive (Bouncy/Fluid)

```kotlin
spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
```

### Motion Scheme Setup

```kotlin
MaterialExpressiveTheme(
    motionScheme = MotionScheme(
        spatial = MotionTokens.spatialStandard,
        expressive = MotionTokens.expressiveBouncy
    )
) {
    // Components inherit motion automatically
}
```

---

## 6. Component Patterns

### Sticky Bottom Bars & Headers

```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    // Sticky Header
    HeaderSection(...)

    // Scrollable Content
    Column(modifier = Modifier.weight(1f).verticalScroll(state)) {
        // Form Fields
    }

    // Sticky Action (Expressive Button - XL size)
    Button(
        onClick = { },
        modifier = Modifier.fillMaxWidth().height(56.dp).padding(16.dp),
        shape = CircleShape,
        contentPadding = ButtonDefaults.contentPaddingFor(ButtonSize.ExtraLarge)
    ) {
        Text("Save")
    }
}
```

### Expressive Surface Container

```kotlin
Surface(
    modifier = modifier,
    shape = MaterialTheme.shapes.extraLarge,
    color = MaterialTheme.colorScheme.surfaceContainerLow
) {
    // Content
}
```

### High-Contrast Label Chip

```kotlin
Surface(
    shape = RoundedCornerShape(8.dp),
    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
) {
    Text(
        text = "CATEGORY",
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}
```

### Iconography & Targets

- **Supporting Icons:** `14.dp` to `16.dp`
- **Icon Containers:** `24.dp` to `32.dp` backgrounds for chip look
- **Action Targets:** `40.dp` to `48.dp` for touch-friendly tappable areas

---

## 7. Design Benefits for HotelDesk

1. **4x Faster Scannability** — Key info (Room Status, Names) spotted instantly (eye-tracking
   research)
2. **Accessible-by-Default** — Contrast, touch targets exceed standard requirements
3. **Modern Professionalism** — Variable fonts, morphing shapes, spring motion create premium feel
4. **Reduced Perceived Wait Time** — Wavy progress indicators and morphing loaders make loading feel
   faster
5. **Emotionally Engaging** — Bouncy motion, vibrant colors, tactile feedback foster user connection

---

## 8. M3 Expressive Tactics (from Material Research)

1. **Use varied shapes** — mix round and square for visual tension and focus
2. **Apply rich colors** — use surface tones for hierarchy, contrast between color roles
3. **Guide with typography** — emphasized styles create editorial moments
4. **Contain content** — logical groupings with surface colors for prominence
5. **Add fluid motion** — shape morph, surface effects, spring physics
6. **Leverage component flexibility** — adapt to user context and screen size
7. **Create hero moments** — combine multiple tactics for standout interactions
