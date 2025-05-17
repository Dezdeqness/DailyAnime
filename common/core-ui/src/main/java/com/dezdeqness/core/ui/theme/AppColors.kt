package com.dezdeqness.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

fun lightColors(): AppColors = AppColors(
    primaryColor = LightAnimeColors.Primary,
    primaryVariantColor = LightAnimeColors.PrimaryVariant,
    secondaryColor = LightAnimeColors.Secondary,
    backgroundColor = LightAnimeColors.Background,
    surfaceColor = LightAnimeColors.Surface,
    surfaceVariantColor = LightAnimeColors.SurfaceVariant,
    borderColor = LightAnimeColors.Border,
    onPrimaryColor = LightAnimeColors.OnPrimary,
    onSecondaryColor = LightAnimeColors.OnSecondary,
    onBackgroundColor = LightAnimeColors.OnBackground,
    onSurfaceColor = LightAnimeColors.OnSurface,
    textPrimaryColor = LightAnimeColors.TextPrimary,
    textSecondaryColor = LightAnimeColors.TextSecondary,
    textDisabledColor = LightAnimeColors.TextDisabled,
    rippleColor = LightAnimeColors.Ripple,
    errorColor = LightAnimeColors.Error,
    successColor = LightAnimeColors.Success,
    warningColor = LightAnimeColors.Warning,
    accentColor = LightAnimeColors.Accent,
)

fun darkColors(): AppColors = AppColors(
    primaryColor = DarkAnimeColors.Primary,
    primaryVariantColor = DarkAnimeColors.PrimaryVariant,
    secondaryColor = DarkAnimeColors.Secondary,
    backgroundColor = DarkAnimeColors.Background,
    surfaceColor = DarkAnimeColors.Surface,
    surfaceVariantColor = DarkAnimeColors.SurfaceVariant,
    borderColor = DarkAnimeColors.Border,
    onPrimaryColor = DarkAnimeColors.OnPrimary,
    onSecondaryColor = DarkAnimeColors.OnSecondary,
    onBackgroundColor = DarkAnimeColors.OnBackground,
    onSurfaceColor = DarkAnimeColors.OnSurface,
    textPrimaryColor = DarkAnimeColors.TextPrimary,
    textSecondaryColor = DarkAnimeColors.TextSecondary,
    textDisabledColor = DarkAnimeColors.TextDisabled,
    rippleColor = DarkAnimeColors.Ripple,
    errorColor = DarkAnimeColors.Error,
    successColor = DarkAnimeColors.Success,
    warningColor = DarkAnimeColors.Warning,
    accentColor = DarkAnimeColors.Accent,
)

@Immutable
class AppColors(
    private val primaryColor: Color,
    private val primaryVariantColor: Color,
    private val secondaryColor: Color,
    private val backgroundColor: Color,
    private val surfaceColor: Color,
    private val surfaceVariantColor: Color,
    private val borderColor: Color,
    private val onPrimaryColor: Color,
    private val onSecondaryColor: Color,
    private val onBackgroundColor: Color,
    private val onSurfaceColor: Color,
    private val textPrimaryColor: Color,
    private val textSecondaryColor: Color,
    private val textDisabledColor: Color,
    private val rippleColor: Color,
    private val errorColor: Color,
    private val successColor: Color,
    private val warningColor: Color,
    private val accentColor: Color,
) {

    var primary by mutableStateOf(primaryColor)
        private set
    var primaryVariant by mutableStateOf(primaryVariantColor)
        private set
    var secondary by mutableStateOf(secondaryColor)
        private set
    var background by mutableStateOf(backgroundColor)
        private set
    var surface by mutableStateOf(surfaceColor)
        private set
    var surfaceVariant by mutableStateOf(surfaceVariantColor)
        private set
    var border by mutableStateOf(borderColor)
        private set
    var onPrimary by mutableStateOf(onPrimaryColor)
        private set
    var onSecondary by mutableStateOf(onSecondaryColor)
        private set
    var onBackground by mutableStateOf(onBackgroundColor)
        private set
    var onSurface by mutableStateOf(onSurfaceColor)
        private set
    var textPrimary by mutableStateOf(textPrimaryColor)
        private set
    var textSecondary by mutableStateOf(textSecondaryColor)
        private set
    var textDisabled by mutableStateOf(textDisabledColor)
        private set
    var ripple by mutableStateOf(rippleColor)
        private set
    var error by mutableStateOf(errorColor)
        private set
    var success by mutableStateOf(successColor)
        private set
    var warning by mutableStateOf(warningColor)
        private set
    var accent by mutableStateOf(accentColor)
        private set

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
        secondary = other.secondary
        background = other.background
        surface = other.surface
        surfaceVariant = other.surfaceVariant
        border = other.border
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
        onBackground = other.onBackground
        onSurface = other.onSurface
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textDisabled = other.textDisabled
        ripple = other.ripple
        error = other.error
        success = other.success
        warning = other.warning
        accent = other.accent
    }

    fun copy(
        primary: Color = this.primary,
        primaryVariant: Color = this.primaryVariant,
        secondary: Color = this.secondary,
        background: Color = this.background,
        surface: Color = this.surface,
        surfaceVariant: Color = this.surfaceVariant,
        border: Color = this.border,
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onSecondary,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        textDisabled: Color = this.textDisabled,
        ripple: Color = this.ripple,
        error: Color = this.error,
        success: Color = this.success,
        warning: Color = this.warning,
        accent: Color = this.accent,
    ): AppColors = AppColors(
        primaryColor = primary,
        primaryVariantColor = primaryVariant,
        secondaryColor = secondary,
        backgroundColor = background,
        surfaceColor = surface,
        surfaceVariantColor = surfaceVariant,
        borderColor = border,
        textPrimaryColor = textPrimary,
        textSecondaryColor = textSecondary,
        textDisabledColor = textDisabled,
        rippleColor = ripple,
        errorColor = error,
        successColor = success,
        warningColor = warning,
        onPrimaryColor = onPrimary,
        onSecondaryColor = onSecondary,
        onBackgroundColor = onBackground,
        onSurfaceColor = onSurface,
        accentColor = accent,
    )
}

internal val LocalColors =
    staticCompositionLocalOf<AppColors> { throw UnsupportedOperationException() }
