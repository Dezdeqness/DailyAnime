package com.dezdeqness.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography(
    val displayLarge: TextStyle = displayStyle(57.sp, 64.sp, (-0.25).sp),
    val displayMedium: TextStyle = displayStyle(45.sp, 52.sp, 0.sp),
    val displaySmall: TextStyle = displayStyle(36.sp, 44.sp, 0.sp),

    val headlineLarge: TextStyle = headlineStyle(32.sp, 40.sp, 0.sp),
    val headlineMedium: TextStyle = headlineStyle(28.sp, 36.sp, 0.sp),
    val headlineSmall: TextStyle = headlineStyle(24.sp, 32.sp, 0.sp),

    val titleLarge: TextStyle = titleStyle(22.sp, 28.sp, 0.sp),
    val titleMedium: TextStyle = titleStyle(16.sp, 24.sp, 0.1.sp),
    val titleSmall: TextStyle = titleStyle(14.sp, 20.sp, 0.1.sp),

    val bodyLarge: TextStyle = bodyStyle(16.sp, 24.sp, 0.5.sp),
    val bodyMedium: TextStyle = bodyStyle(14.sp, 20.sp, 0.25.sp),
    val bodySmall: TextStyle = bodyStyle(12.sp, 16.sp, 0.4.sp),

    val labelLarge: TextStyle = labelStyle(14.sp, 20.sp, 0.1.sp),
    val labelMedium: TextStyle = labelStyle(12.sp, 16.sp, 0.5.sp),
    val labelSmall: TextStyle = labelStyle(11.sp, 16.sp, 0.5.sp),
)

private fun displayStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
) = TextStyle(
    fontSize = fontSize,
    fontWeight = fontWeight,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

private fun headlineStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
    fontWeight: FontWeight = FontWeight.SemiBold,
) = TextStyle(
    fontSize = fontSize,
    fontWeight = fontWeight,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

private fun titleStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
    fontWeight: FontWeight = FontWeight.Medium,
) = TextStyle(
    fontSize = fontSize,
    fontWeight = fontWeight,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

private fun bodyStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
    fontWeight: FontWeight = FontWeight.Normal,
) = TextStyle(
    fontSize = fontSize,
    fontWeight = fontWeight,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

private fun labelStyle(
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit,
    fontWeight: FontWeight = FontWeight.Medium,
) = TextStyle(
    fontSize = fontSize,
    fontWeight = fontWeight,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

val LocalTypography = staticCompositionLocalOf { AppTypography() }
