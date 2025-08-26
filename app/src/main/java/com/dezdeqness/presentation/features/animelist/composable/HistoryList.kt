package com.dezdeqness.presentation.features.animelist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme

@Composable
fun HistoryItem(
    modifier: Modifier = Modifier,
    title: String,
    onClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    onFulFillClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .padding(start = 16.dp, end = 8.dp)
            .clickable(onClick = onClicked),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.History,
            contentDescription = null,
            tint = AppTheme.colors.onSurface,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            title,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = AppTheme.typography.titleMedium.copy(
                fontSize = 14.sp,
                color = AppTheme.colors.textPrimary,
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemoveClicked) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
            )
        }

        IconButton(onClick = onFulFillClicked) {
            Icon(
                Icons.Default.ArrowOutward,
                contentDescription = null,
                tint = AppTheme.colors.onSurface,
            )
        }
    }
}
