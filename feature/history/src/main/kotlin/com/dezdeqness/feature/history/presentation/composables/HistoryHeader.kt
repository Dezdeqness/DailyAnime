package com.dezdeqness.feature.history.presentation.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun HistoryHeader(modifier: Modifier = Modifier, header: String) {
    Text(
        header,
        color = AppTheme.colors.textPrimary,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = modifier,
    )
}
