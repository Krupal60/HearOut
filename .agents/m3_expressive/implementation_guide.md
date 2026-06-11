# Implementation Guide: Material 3 Expressive

To successfully implement M3 Expressive and the latest I/O '26 features, follow these technical and
design guidelines.

---

## 1. Technical Requirements

### Library Dependencies (as of I/O '26)

M3 Expressive features rolled out across **1.3.x** through **1.5.x** tracks. For the latest I/O '26
components:

```toml
# libs.versions.toml
[versions]
material3 = "1.5.0-alpha21"    # Latest M3E components
material3-adaptive = "1.0.0"    # NavigationSuiteScaffold

[libraries]
androidx-material3 = { group = "androidx.compose.material3", name = "material3", version.ref = "material3" }
androidx-material3-adaptive = { group = "androidx.compose.material3", name = "material3-adaptive", version.ref = "material3-adaptive" }
```

```kotlin
// build.gradle.kts (module)
dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.adaptive)
}
```

**Version History:**
| Version | Key Expressive Additions |
|---------|--------------------------|
| 1.3.x | First expressive APIs (SplitButton, LoadingIndicator) |
| 1.4.0 (Stable) | HorizontalCenteredHeroCarousel, VerticalDragHandle, SecureTextField,
TimePickerDialog, Search rework |
| 1.5.0-alpha19+ | MaterialExpressiveTheme, MotionScheme, expressiveLightColorScheme |
| 1.5.0-alpha21 | Material3ExpressiveApi (no opt-in needed), TextField rounded/tonal,
TimePickerShapes, Menu gaps |

### Experimental API Opt-in

In 1.5.0-alpha21+, some expressive APIs use `@Material3ExpressiveApi` which **no longer requires
explicit opt-in**:

```kotlin
// 1.5.0-alpha21+: No @OptIn needed for these
@Material3ExpressiveApi
fun someExpressiveFunction() {
}
```

For older APIs still behind `@ExperimentalMaterial3ExpressiveApi`:

```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyExpressiveScreen() {
    SplitButton(/* ... */)
}
```

---

## 2. Theme Setup

### MaterialExpressiveTheme (1.5.0-alpha19+)

New top-level theme composable with `motionScheme` support:

```kotlin
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.MotionTokens
import androidx.compose.material3.expressiveLightColorScheme

@Composable
fun HotelDeskApp() {
    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = when {
        isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        else -> expressiveLightColorScheme()
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        motionScheme = MotionScheme(
            spatial = MotionTokens.spatialStandard,
            expressive = MotionTokens.expressiveBouncy
        )
    ) {
        AppContent()
    }
}
```

### Expressive Color Scheme

`expressiveLightColorScheme()` provides brighter text/icons and higher chroma across all hues:

```kotlin
// Default expressive color scheme override values:
fun expressiveLightColorScheme() = lightColorScheme(
    onPrimaryContainer = PaletteTokens.Primary30,
    onSecondaryContainer = PaletteTokens.Secondary30,
    onTertiaryContainer = PaletteTokens.Tertiary30,
    onErrorContainer = PaletteTokens.Error30,
)
```

### Motion Scheme

Tokenized spring physics system replacing fixed duration/easing:

```kotlin
val motionScheme = MotionScheme(
    spatial = MotionTokens.spatialStandard,      // Standard transitions
    expressive = MotionTokens.expressiveBouncy,   // Bouncy/fluid effects
    // Custom springs:
    custom = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )
)

// Usage in components:
val scale by animateFloatAsState(
    targetValue = if (pressed) 0.95f else 1f,
    animationSpec = motionScheme.expressive // Use scheme token
)
```

### Shape Scale

Expanded to 10 values matching the type scale concept:

```kotlin
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
    smallIncreased = RoundedCornerShape(8.dp),
    mediumIncreased = RoundedCornerShape(16.dp),
    largeIncreased = RoundedCornerShape(24.dp),
    extraLargeIncreased = RoundedCornerShape(28.dp),
    full = CircleShape
)
```

---

## 3. Expressive Motion (Spring Physics)

M3 Expressive moves from fixed `durationMillis`/`Easing` to **Spring-based physics**:

### Standard Springs

```kotlin
// Efficient, utilitarian motion
spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
```

### Expressive Springs

```kotlin
// Bouncy, fluid, reactive
spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
```

### Shape Morph Animation

Buttons and button groups use built-in shape morph on press:

```kotlin
// Button supports built-in shape morph via `shapes` parameter
Button(
    onClick = { },
    shape = ButtonDefaults.buttonShape(ButtonShape.Rounded),
    // shape morphs between states automatically
) {
    Text("Press me")
}
```

---

## 4. Adaptive Canonical Layouts

Use `material3-adaptive` library for multi-pane reflow:

```kotlin
// List-Detail: messaging, settings
// Supporting Pane: dashboards, editors
implementation("androidx.compose.material3:material3-adaptive:1.0.0")
```

---

## 5. Shape & Surface Rules

### Corner Radius Matrix (Expressive)

| Component Type                  |           Corner Radius | Shape Token                 |
|:--------------------------------|------------------------:|:----------------------------|
| **Large Cards / Hero Carousel** |                 `24.dp` | `extraLarge`                |
| **Medium Containers / Cards**   |                 `16.dp` | `large` / `mediumIncreased` |
| **Small Items / Chips**         |                  `8.dp` | `small`                     |
| **Action Buttons (Primary)**    | `CircleShape` / `24.dp` | `full` / `extraLarge`       |
| **Text Fields**                 |                 `12.dp` | `medium`                    |
| **Expressive Buttons**          |                 `16.dp` | `large`                     |

### Expressive Button Sizes

| Size |  Height | Content Padding                                           |
|:-----|--------:|:----------------------------------------------------------|
| XS   | `32.dp` | `ButtonDefaults.contentPaddingFor(ButtonSize.ExtraSmall)` |
| S    | `36.dp` | `ButtonDefaults.contentPaddingFor(ButtonSize.Small)`      |
| M    | `40.dp` | `ButtonDefaults.contentPaddingFor(ButtonSize.Medium)`     |
| L    | `48.dp` | `ButtonDefaults.contentPaddingFor(ButtonSize.Large)`      |
| XL   | `56.dp` | `ButtonDefaults.contentPaddingFor(ButtonSize.ExtraLarge)` |

---

## 6. Common Design Patterns

### Sticky Footer Pattern (Save Button)

```kotlin
Column(modifier = Modifier.fillMaxSize()) {
    // 1. Sticky Header
    HeaderSection(...)

    // 2. Scrollable Content
    Column(modifier = Modifier.weight(1f).verticalScroll(state)) {
        // Form Fields
    }

    // 3. Sticky Action (Expressive Button)
    Button(
        onClick = { /* Save */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        shape = CircleShape,
        contentPadding = ButtonDefaults.contentPaddingFor(ButtonSize.ExtraLarge)
    ) {
        Icon(Icons.Default.Save, null)
        Spacer(Modifier.width(8.dp))
        Text("Save")
    }
}
```

### High-Contrast Surfaces

```kotlin
Surface(
    shape = RoundedCornerShape(24.dp),
    color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    // Input fields
}
```

### Password Field (Secure Text Field)

```kotlin
@Composable
fun PasswordFields() {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SecureTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            supportingText = { Text("At least 8 characters") }
        )
        OutlinedSecureTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text("Confirm Password") },
            isError = password != confirm
        )
    }
}
```

---

## 7. Migration Checklist (M3 -> M3 Expressive)

- [ ] Update `material3` to `1.5.0-alpha21+` or latest
- [ ] Add `material3-adaptive` for NavigationSuiteScaffold
- [ ] Add `materialThemeExpress` and `expressiveLightColorScheme()` to theme
- [ ] Add `MotionScheme` with spring tokens
- [ ] Audit hardcoded corner radii (buttons: `12.dp` -> `16.dp`, cards: `4.dp` -> `20.dp`)
- [ ] Check touch targets (minimum `48.dp`)
- [ ] Review typography (headlines moved from SemiBold to Bold)
- [ ] Test animation timing (defaults ~15% longer)
- [ ] Replace `BottomAppBar` with `DockedToolbar`
- [ ] Replace `CircularProgressIndicator` (indeterminate) with `LoadingIndicator`
- [ ] Replace speed-dial patterns with `FABMenu`
- [ ] Replace `DropdownMenuItem` with standard `DropdownMenu`/`MenuItems`
- [ ] Test dark theme contrast ratios with expressive color scheme
