package com.dezdeqness.presentation.features.details.composables.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun DetailsTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
        color = AppTheme.colors.textPrimary,
        fontSize = 24.sp,
        modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        textAlign = TextAlign.Center,
    )
}
