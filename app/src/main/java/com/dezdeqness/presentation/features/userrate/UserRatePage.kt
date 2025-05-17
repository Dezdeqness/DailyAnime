package com.dezdeqness.presentation.features.userrate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dezdeqness.R
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.toolbar.AppToolbar
import com.dezdeqness.presentation.features.userrate.composable.CommentTextField
import com.dezdeqness.presentation.features.userrate.composable.ScoreSlider
import com.dezdeqness.presentation.features.userrate.composable.SelectStatusDialog
import com.dezdeqness.presentation.features.userrate.composable.SelectStatusItem
import com.dezdeqness.presentation.features.userrate.composable.rememberCommentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRatePage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<UserRateState>,
    actions: UserRateActions,
) {
    val state by stateFlow.collectAsState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    val listStatuses = remember {
        SelectStatusItem.composeList(context)
    }

    Scaffold(
        containerColor = colorResource(id = R.color.background_tint),
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                navigationIcon = Icons.Default.Close,
                actions = {
                    TextButton(
                        shape = AppTheme.shapes.medium,
                        onClick = { actions.onResetClicked() },
                    ) {
                        Text(
                            text = stringResource(id = R.string.edit_rate_reset_title),
                            style = AppTheme.typography.titleMedium,
                        )
                    }

                    val changeTitleId =
                        if (state.isEditMode) {
                            R.string.edit_rate_update_title
                        } else {
                            R.string.edit_rate_create_title
                        }

                    TextButton(
                        shape = AppTheme.shapes.medium,
                        onClick = { actions.onChangeRateClicked() },
                        enabled = state.isContentChanged,
                        colors = ButtonDefaults
                            .textButtonColors()
                            .copy(
                                disabledContentColor = ButtonDefaults
                                    .textButtonColors()
                                    .contentColor
                                    .copy(alpha = 0.4F)
                            ),
                    ) {
                        Text(
                            text = stringResource(id = changeTitleId),
                            style = AppTheme.typography.titleMedium,
                        )
                    }
                },
                navigationClick = actions::onBackPressed,
            )
        },
    ) { innerPadding ->
        val verticalState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(verticalState)
        ) {
            Text(
                text = state.title,
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                color = AppTheme.colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                )
            )

            Text(
                text = stringResource(id = R.string.edit_rate_status_title),
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                )
            )

            MaterialTheme {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(
                            onClick = actions::onSelectStatusClicked,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = AppTheme.colors.ripple)
                        )
                ) {
                    Text(
                        text = listStatuses
                            .firstOrNull { it.id == state.selectedStatus }
                            ?.title
                            ?: stringResource(id = R.string.edit_rate_status_no_status),
                        modifier = Modifier.padding(16.dp),
                        color = AppTheme.colors.textPrimary,
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.edit_rate_score_title),
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                )
            )

            ScoreSlider(
                modifier = Modifier.padding(horizontal = 16.dp),
                score = state.score,
                onScoreChanged = actions::onScoreChanged,
            )

            Text(
                text = stringResource(id = R.string.edit_rate_episodes_header),
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(
                    top = 4.dp,
                    end = 16.dp,
                    start = 16.dp,
                )
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.edit_rate_episodes_title),
                    style = AppTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    ),
                    color = AppTheme.colors.textPrimary,
                )

                Text(
                    state.episode.toString(),
                    style = AppTheme.typography.titleMedium.copy(
                        color = AppTheme.colors.textPrimary,
                    ),
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.weight(1f))

                MaterialTheme {
                    Row {
                        IconButton(onClick = actions::onDecrementEpisode) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_minus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                        IconButton(onClick = actions::onIncrementEpisode) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.edit_rate_episodes_header),
                    style = AppTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    ),
                    color = AppTheme.colors.textPrimary,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    )
                )

            }

            Text(
                text = stringResource(id = R.string.edit_rate_comment_header),
                style = AppTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                color = AppTheme.colors.textPrimary,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
            )

            val commentState = rememberCommentState(state.comment)

            CommentTextField(
                state = commentState,
                onCommentChanged = {
                    actions.onCommentChanged(it)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            commentState.comment = ""
                            actions.onCommentChanged("")

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = AppTheme.colors.onSecondary
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.fillMaxSize())

            if (state.isSelectStatusDialogShowed) {
                SelectStatusDialog(
                    selectedId = state.selectedStatus,
                    state = sheetState,
                    statuses = listStatuses,
                    onSelectedItem = actions::onStatusChanged,
                    onCloseClicked = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (sheetState.isVisible.not()) {
                                actions.onCloseSelectStatusClicked()
                            }
                        }
                    }
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserRatePagePreview() {
    AppTheme {
        UserRatePage(
            stateFlow = MutableStateFlow(
                UserRateState(
                    title = "Восстание Лелуша"
                )
            ),
            actions = object : UserRateActions {
                override fun onStatusChanged(id: String) = Unit
                override fun onScoreChanged(score: Long) = Unit
                override fun onSelectStatusClicked() = Unit
                override fun onCloseSelectStatusClicked() = Unit
                override fun onResetClicked() = Unit
                override fun onChangeRateClicked() = Unit
                override fun onBackPressed() = Unit
                override fun onIncrementEpisode() = Unit
                override fun onDecrementEpisode() = Unit
                override fun onEpisodesChanged(episodes: String) = Unit
                override fun onCommentChanged(comment: String) = Unit
            }
        )
    }
}
