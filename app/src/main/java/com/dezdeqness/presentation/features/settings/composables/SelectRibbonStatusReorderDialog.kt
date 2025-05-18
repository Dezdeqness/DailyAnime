package com.dezdeqness.presentation.features.settings.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dezdeqness.R
import com.dezdeqness.core.ui.ReorderHapticFeedbackType
import com.dezdeqness.core.ui.rememberReorderHapticFeedback
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppButton
import com.dezdeqness.presentation.models.RibbonStatusUiModel
import com.google.common.collect.ImmutableList
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun SelectRibbonStatusReorderDialog(
    modifier: Modifier = Modifier,
    onDoneClicked: (List<RibbonStatusUiModel>) -> Unit,
    statuses: ImmutableList<RibbonStatusUiModel>,
    onDismissRequest: () -> Unit,
) {
    val haptic = rememberReorderHapticFeedback()

    var list by remember { mutableStateOf(statuses.toList()) }
    val lazyListState = rememberLazyListState()
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyListState) { from, to ->
        list = list.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }

        haptic.performHapticFeedback(ReorderHapticFeedbackType.MOVE)
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.onPrimary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.reorder_statuses_title),
                    style = AppTheme.typography.titleLarge,
                    color = AppTheme.colors.textPrimary,
                )

                LazyColumn(
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(list, key = { _, item -> item.id }) { index, item ->
                        ReorderableItem(reorderableLazyColumnState, item.id) {
                            val interactionSource = remember { MutableInteractionSource() }

                            Card(
                                onClick = {},
                                interactionSource = interactionSource,
                                colors = CardDefaults.cardColors(
                                    containerColor = AppTheme.colors.onPrimary
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        item.displayName,
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        color = AppTheme.colors.textPrimary,
                                    )
                                    IconButton(
                                        modifier = Modifier
                                            .draggableHandle(
                                                onDragStarted = {
                                                    haptic.performHapticFeedback(
                                                        ReorderHapticFeedbackType.START
                                                    )
                                                },
                                                onDragStopped = {
                                                    haptic.performHapticFeedback(
                                                        ReorderHapticFeedbackType.END
                                                    )
                                                },
                                                interactionSource = interactionSource,
                                            )
                                            .clearAndSetSemantics { },
                                        onClick = {},
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_palm),
                                            tint = AppTheme.colors.onSurface,
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                AppButton(
                    title = stringResource(R.string.reorder_statuses_button_save_title),
                    onClick = {
                        onDoneClicked(list)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

}
