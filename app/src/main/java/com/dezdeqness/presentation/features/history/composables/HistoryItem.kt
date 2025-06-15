package com.dezdeqness.presentation.features.history.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.image.AppImage
import com.dezdeqness.presentation.features.history.models.HistoryModel

@Composable
fun HistoryItem(modifier: Modifier = Modifier, item: HistoryModel.HistoryUiModel) {

    Card(modifier = modifier) {
        Box(contentAlignment = Alignment.Center) {
            AppImage(
                data = item.imageUrl,
                colorFilter = ColorFilter.tint(
                    AppTheme.colors.onPrimary.copy(alpha = 0.7f),
                    blendMode = BlendMode.SrcOver,
                ),
                modifier = Modifier.matchParentSize()
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                Text(
                    item.name,
                    color = AppTheme.colors.textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                Text(
                    item.action,
                    color = AppTheme.colors.textPrimary.copy(0.8f),
                    fontSize = 14.sp,
                )
            }
        }

    }
}
