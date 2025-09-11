package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HeaderSettingsView(
    modifier: Modifier = Modifier,
    title: String
) {
    TextSettingsView(
        modifier = modifier,
        title = title,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    )
}
