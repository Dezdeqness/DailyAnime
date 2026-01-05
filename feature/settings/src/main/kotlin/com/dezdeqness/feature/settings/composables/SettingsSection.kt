package com.dezdeqness.feature.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    sectionTitle: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HeaderCustomSettingsView(title = sectionTitle)
        content()
    }
}
