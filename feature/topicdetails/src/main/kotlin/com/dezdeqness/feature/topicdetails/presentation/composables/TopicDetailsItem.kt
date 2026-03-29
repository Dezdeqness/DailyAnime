package com.dezdeqness.feature.topicdetails.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.shared.presentation.feature.topic.composables.TopicUserInfoRow
import com.dezdeqness.shared.presentation.feature.topic.composables.blocks.ContentBlockRenderer
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

@Composable
fun TopicDetailsItem(
    modifier: Modifier = Modifier,
    topic: TopicPresentationModel,
    onVideoClick: (String) -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.onPrimary),
        shape = AppTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            TopicUserInfoRow(
                userAvatarUrl = topic.userAvatarUrl,
                userNickname = topic.userNickname,
                date = topic.date,
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = topic.title,
                color = AppTheme.colors.textPrimary,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.size(16.dp))

            ContentBlockRenderer(
                blocks = topic.contentBlocks,
                onVideoClick = onVideoClick,
            )

            if (topic.footerBlocks.isNotEmpty()) {
                Spacer(modifier = Modifier.size(12.dp))
                ContentBlockRenderer(
                    blocks = topic.footerBlocks,
                    onVideoClick = onVideoClick,
                )
            }
        }
    }
}
