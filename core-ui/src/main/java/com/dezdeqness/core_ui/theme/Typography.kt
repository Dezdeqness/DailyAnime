package com.dezdeqness.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

private val defaultTypography = Typography()

data class AppTypography(
    val displayLarge: TextStyle = defaultTypography.displayLarge,
    val displayMedium: TextStyle = defaultTypography.displayMedium,
    val displaySmall: TextStyle = defaultTypography.displaySmall,
    val headlineLarge: TextStyle = defaultTypography.headlineLarge,
    val headlineMedium: TextStyle = defaultTypography.headlineMedium,
    val headlineSmall: TextStyle = defaultTypography.headlineSmall,
    val titleLarge: TextStyle = defaultTypography.titleLarge,
    val titleMedium: TextStyle = defaultTypography.titleMedium,
    val titleSmall: TextStyle = defaultTypography.titleSmall,
    val bodyLarge: TextStyle = defaultTypography.bodyLarge,
    val bodyMedium: TextStyle = defaultTypography.bodyMedium,
    val bodySmall: TextStyle = defaultTypography.bodySmall,
    val labelLarge: TextStyle = defaultTypography.labelLarge,
    val labelMedium: TextStyle = defaultTypography.labelMedium,
    val labelSmall: TextStyle = defaultTypography.labelSmall,
)
internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
