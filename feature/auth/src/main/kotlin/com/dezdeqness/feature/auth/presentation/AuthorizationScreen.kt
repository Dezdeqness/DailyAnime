package com.dezdeqness.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dezdeqness.foundation.ui.theme.AppTheme

@Composable
fun AuthorizationScreen(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.onPrimary),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = AppTheme.colors.accent)
        }
    }
}

@PreviewLightDark
@Composable
fun AuthorizationScreenPreview() {
    AppTheme {
        AuthorizationScreen(isLoading = true)
    }
}
