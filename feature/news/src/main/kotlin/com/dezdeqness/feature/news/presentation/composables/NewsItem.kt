package com.dezdeqness.feature.news.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.news.presentation.composables.blocks.ContentBlockRenderer
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.image.AppImage

@Composable
fun NewsItem(
    item: NewsUiModel.NewsItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.onPrimary,
        ),
        shape = AppTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    item.userAvatarUrl,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.userNickname,
                        color = AppTheme.colors.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = item.date,
                        color = AppTheme.colors.textSecondary,
                        fontSize = 11.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                color = AppTheme.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContentBlockRenderer(blocks = item.contentBlocks)

            if (item.footerBlocks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                ContentBlockRenderer(
                    blocks = item.footerBlocks,
                )
            }
        }
    }
}

