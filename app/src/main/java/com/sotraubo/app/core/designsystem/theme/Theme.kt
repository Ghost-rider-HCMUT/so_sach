package com.sotraubo.app.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color(0xFF06162E),
    primaryContainer = BlueContainer,
    onPrimaryContainer = OnBlueContainer,
    secondary = GreenReceive,
    onSecondary = Color(0xFF032B1F),
    secondaryContainer = GreenReceiveContainer,
    onSecondaryContainer = OnGreenReceiveContainer,
    tertiary = OrangePayable,
    onTertiary = Color(0xFF331400),
    tertiaryContainer = OrangePayableContainer,
    onTertiaryContainer = OnOrangePayableContainer,
    error = RedTransfer,
    onError = Color(0xFF3B0808),
    errorContainer = RedTransferContainer,
    onErrorContainer = OnRedTransferContainer,
    background = BackgroundDark,
    onBackground = TextPrimaryLight,
    surface = SurfaceDarkCard,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceElevatedDark,
    onSurfaceVariant = TextSecondaryLight,
    outline = DividerDark
)

@Composable
fun SoTrauBoTheme(
    content: @Composable () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val compactScale = when {
        screenWidthDp < 380 -> 0.92f
        screenWidthDp < 405 -> 0.98f
        else -> 1.16f
    }
    val typography = remember(compactScale) { scaledTypography(compactScale) }

    CompositionLocalProvider(LocalCompactFontScale provides compactScale) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = typography,
            content = content
        )
    }
}
