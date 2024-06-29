package com.dezdeqness.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        get() = LocalTypography.current

    val shapes: AppShapes
        @Composable
        get() = LocalShapes.current
}

@Composable
fun AppTheme(
    colors: AppColors = if (isSystemInDarkTheme()) darkColors() else lightColors(),
    typography: AppTypography = AppTheme.typography,
    shapes: AppShapes = AppTheme.shapes,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography,
        LocalShapes provides shapes,
    ) {
        content()
    }
}
