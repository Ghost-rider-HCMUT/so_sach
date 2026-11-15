package com.sotraubo.app.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val LocalCompactFontScale = staticCompositionLocalOf { 1f }

@Composable
fun responsiveSp(baseSp: Float): TextUnit = (baseSp * LocalCompactFontScale.current).sp

fun scaledTypography(scale: Float): Typography {
    if (scale == 1f) return SoTrauBoTypography
    fun TextStyle.scaled() = copy(
        fontSize = (fontSize.value * scale).sp,
        lineHeight = (lineHeight.value * scale).sp
    )
    return Typography(
        headlineLarge = SoTrauBoTypography.headlineLarge.scaled(),
        headlineMedium = SoTrauBoTypography.headlineMedium.scaled(),
        titleLarge = SoTrauBoTypography.titleLarge.scaled(),
        titleMedium = SoTrauBoTypography.titleMedium.scaled(),
        titleSmall = SoTrauBoTypography.titleSmall.scaled(),
        bodyLarge = SoTrauBoTypography.bodyLarge.scaled(),
        bodyMedium = SoTrauBoTypography.bodyMedium.scaled(),
        bodySmall = SoTrauBoTypography.bodySmall.scaled(),
        labelLarge = SoTrauBoTypography.labelLarge.scaled(),
        labelMedium = SoTrauBoTypography.labelMedium.scaled(),
        labelSmall = SoTrauBoTypography.labelSmall.scaled()
    )
}

val SoTrauBoTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        fontFeatureSettings = "tnum"
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontFeatureSettings = "tnum"
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
