package com.dezdeqness.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun GenericHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        title,
        style = AppTheme.typography.labelLarge.copy(fontSize = 20.sp),
        modifier = modifier,
        color = AppTheme.colors.textPrimary
    )
}
