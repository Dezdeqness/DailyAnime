package com.dezdeqness.core_ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    onPrimary: Color,
    onSecondary: Color,
    textPrimary: Color,
    textSecondary: Color,
    rippleColor: Color,
    error: Color,
    isLight: Boolean
) {
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var onSecondary by mutableStateOf(onSecondary)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var rippleColor by mutableStateOf(rippleColor)
        private set
    var error by mutableStateOf(error)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onSecondary,
        textPrimary: Color = this.textPrimary,
        textSecondary: Color = this.textSecondary,
        rippleColor: Color = this.rippleColor,
        error: Color = this.error,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        onPrimary,
        onSecondary,
        textPrimary,
        textSecondary,
        rippleColor,
        error,
        isLight
    )

    fun updateColorsFrom(other: AppColors) {
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        rippleColor = other.rippleColor
        error = other.error
    }
}

fun lightColors(
    onPrimary: Color = white,
    onSecondary: Color = black,
    textPrimary: Color = black,
    textSecondary: Color = white,
    rippleColor: Color = black,
    error: Color = white,
): AppColors = AppColors(
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    rippleColor = rippleColor,
    error = error,
    isLight = true
)

fun darkColors(
    onPrimary: Color = black,
    onSecondary: Color = white,
    textPrimary: Color = white,
    textSecondary: Color = black,
    rippleColor: Color = white,
    error: Color = black,
): AppColors = AppColors(
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    textPrimary = textPrimary,
    textSecondary = textSecondary,
    rippleColor = rippleColor,
    error = error,
    isLight = false
)

internal val LocalColors = staticCompositionLocalOf { lightColors() }
