package com.dezdeqness.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.dezdeqness.contract.settings.models.ThemeMode
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.theme.amoledColors
import com.dezdeqness.foundation.ui.theme.darkColors
import com.dezdeqness.foundation.ui.theme.lightColors
import com.dezdeqness.foundation.ui.theme.toAmoledMaterialScheme
import com.dezdeqness.foundation.ui.theme.toDarkMaterialScheme
import com.dezdeqness.foundation.ui.theme.toLightMaterialScheme

@Composable
fun AppContentTheme(
    themeMode: ThemeMode?,
    content: @Composable () -> Unit,
) {
    AppTheme(
        colors = when (themeMode) {
            ThemeMode.AMOLED -> amoledColors()
            ThemeMode.DARK -> darkColors()
            ThemeMode.LIGHT -> lightColors()
            ThemeMode.SYSTEM, null ->
                if (isSystemInDarkTheme()) darkColors() else lightColors()
        },
        materialDefaultTheme = when (themeMode) {
            ThemeMode.AMOLED -> toAmoledMaterialScheme()
            ThemeMode.DARK -> toDarkMaterialScheme()
            ThemeMode.LIGHT -> toLightMaterialScheme()
            ThemeMode.SYSTEM, null ->
                if (isSystemInDarkTheme()) toDarkMaterialScheme() else toLightMaterialScheme()
        },
        content = content,
    )
}
