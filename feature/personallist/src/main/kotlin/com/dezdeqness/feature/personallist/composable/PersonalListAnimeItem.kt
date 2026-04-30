package com.dezdeqness.feature.personallist.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.image.AppImage
import com.dezdeqness.feature.personallist.model.UserRateUiModel
import com.dezdeqness.feature.personallist.tab.PersonalListAction


@Composable
fun PersonalListAnimeItem(
    modifier: Modifier = Modifier,
    userRateUiModel: UserRateUiModel,
    onActionReceive: (PersonalListAction) -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    onActionReceive(
                        PersonalListAction.AnimeClick(
                            userRateUiModel.id,
                            userRateUiModel.name
                        )
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = AppTheme.colors.ripple),
            )
            .fillMaxWidth(),
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            AppImage(
                data = userRateUiModel.logoUrl,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(180.dp)
                    .aspectRatio(2f / 3)
            )

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(8.dp).weight(1f)) {
                    Text(
                        text = userRateUiModel.name,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        color = AppTheme.colors.textPrimary,
                        style = AppTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = userRateUiModel.kind,
                        textAlign = TextAlign.Start,
                        fontSize = 12.sp,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colors.textPrimary.copy(alpha = 0.8f),
                    )

                    userRateUiModel
                        .score
                        .takeIf { it.isNotEmpty() }
                        ?.let { score ->
                            Text(
                                text = score,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                color = AppTheme.colors.textPrimary,
                                style = AppTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row {
                            Text(
                                text = "${userRateUiModel.episodes}/${userRateUiModel.overallEpisodes}",
                                modifier = Modifier.padding(8.dp),
                                color = AppTheme.colors.textPrimary,
                                fontWeight = FontWeight.Bold,
                                style = AppTheme.typography.bodySmall,
                            )
                        }

                        Row(horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
                            if (userRateUiModel.isFinished.not()) {
                                IconButton(
                                    onClick = {
                                        onActionReceive(
                                            PersonalListAction.UserRateIncrement(
                                                userRateUiModel.rateId
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.ExposurePlus1,
                                        contentDescription = null,
                                        tint = AppTheme.colors.onSurface
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    onActionReceive(
                                        PersonalListAction.EditRateClicked(
                                            editRateId = userRateUiModel.rateId,
                                            displayName = userRateUiModel.name,
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = AppTheme.colors.onSurface
                                )
                            }
                        }
                    }

                    val progressValue = if (userRateUiModel.isAnimeInProgress) {
                        userRateUiModel.episodes.toFloat() / userRateUiModel.overallEpisodes
                    } else {
                        0f
                    }

                    LinearProgressIndicator(
                        progress = { progressValue },
                        trackColor = AppTheme.colors.border,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(4.dp)
                            .fillMaxWidth(),
                        drawStopIndicator = {}
                    )
                }
            }

        }
    }
}

@Preview(fontScale = 1f)
@Composable
fun PersonalListAnimeItemPreview() {
    AppTheme {
        val userRateUiModel = UserRateUiModel(
            rateId = 1L,
            id = 1L,
            name = "Rebellion of Lelouch",
            kind = "TV",
            score = "9",
            episodes = 4,
            logoUrl = "logoUrl",
            overallEpisodes = 12,
            updatedAtTimestamp = System.currentTimeMillis(),
        )

        PersonalListAnimeItem(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.onPrimary)
                .padding(16.dp),
            userRateUiModel = userRateUiModel,
            onActionReceive = {},
        )
    }
}
