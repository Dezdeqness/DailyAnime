package com.dezdeqness.feature.news.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.news.presentation.models.NewsUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.shared.presentation.feature.topic.composables.TopicUserInfoRow
import com.dezdeqness.shared.presentation.feature.topic.composables.blocks.ContentBlockRenderer

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
            TopicUserInfoRow(
                userAvatarUrl = item.content.userAvatarUrl,
                userNickname = item.content.userNickname,
                date = item.content.date,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.content.title,
                color = AppTheme.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContentBlockRenderer(blocks = item.content.contentBlocks, isPreview = true)

            if (item.content.footerBlocks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                ContentBlockRenderer(
                    blocks = item.content.footerBlocks,
                )
            }
        }
    }
}
