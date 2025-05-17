package com.dezdeqness.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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
    materialDefaultTheme: ColorScheme = if (isSystemInDarkTheme()) toDarkMaterialScheme() else toLightMaterialScheme(),
    typography: AppTypography = AppTypography(),
    shapes: AppShapes = AppShapes(),
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography,
        LocalShapes provides shapes,
    ) {
        MaterialTheme(colorScheme = materialDefaultTheme) {
            content()
        }
    }
}

fun toLightMaterialScheme(): ColorScheme = lightColorScheme(
    primary = LightAnimeColors.Primary,
    onPrimary = LightAnimeColors.OnPrimary,
    secondary = LightAnimeColors.Secondary,
    onSecondary = LightAnimeColors.OnSecondary,
    background = LightAnimeColors.Background,
    onBackground = LightAnimeColors.OnBackground,
    surface = LightAnimeColors.Surface,
    onSurface = LightAnimeColors.OnSurface,
    surfaceVariant = LightAnimeColors.SurfaceVariant,
    error = LightAnimeColors.Error,
)

fun toDarkMaterialScheme(): ColorScheme = darkColorScheme(
    primary = DarkAnimeColors.Primary,
    onPrimary = DarkAnimeColors.OnPrimary,
    secondary = DarkAnimeColors.Secondary,
    onSecondary = DarkAnimeColors.OnSecondary,
    background = DarkAnimeColors.Background,
    onBackground = DarkAnimeColors.OnBackground,
    surface = DarkAnimeColors.Surface,
    onSurface = DarkAnimeColors.OnSurface,
    surfaceVariant = DarkAnimeColors.SurfaceVariant,
    error = DarkAnimeColors.Error,
)
