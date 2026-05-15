package com.dezdeqness.feature.topicdetails.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.feature.topicdetails.R
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityState
import com.dezdeqness.feature.topicdetails.presentation.models.LinkedEntityUiModel
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_ANIME
import com.dezdeqness.shared.presentation.feature.topic.TopicPresentationComposer.Companion.LINKED_TYPE_CHARACTER
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel

@Composable
fun TopicDetailsContent(
    topic: TopicPresentationModel,
    linkedEntity: LinkedEntityState,
    onLinkedEntityClick: (LinkedEntityUiModel) -> Unit,
    onVideoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            TopicDetailsItem(
                topic = topic,
                onVideoClick = onVideoClick,
            )
        }

        if (linkedEntity != LinkedEntityState.Empty) {
            val headerRes = when (topic.linkedType) {
                LINKED_TYPE_CHARACTER -> R.string.topic_details_related_character
                LINKED_TYPE_ANIME -> R.string.topic_details_related_anime
                else -> R.string.topic_details_related_anime
            }
            item {
                Column {
                    Text(
                        text = stringResource(headerRes),
                        color = AppTheme.colors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    when (linkedEntity) {
                        is LinkedEntityState.Loaded -> {
                            LinkedEntityCard(
                                entity = linkedEntity.entity,
                                modifier = Modifier.padding(bottom = 24.dp),
                                onClick = { onLinkedEntityClick(linkedEntity.entity) },
                            )
                        }

                        else -> LinkedEntityShimmerCard(modifier = Modifier.padding(bottom = 24.dp))
                    }
                }
            }
        }

        item {
            Box(Modifier.padding(bottom = 36.dp))
        }
    }
}
