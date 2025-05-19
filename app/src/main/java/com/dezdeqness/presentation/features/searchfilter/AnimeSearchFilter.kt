package com.dezdeqness.presentation.features.searchfilter

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.HorizontalAnimationLayout
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.buttons.AppButton
import com.dezdeqness.core.ui.views.buttons.AppOutlinedButton
import com.dezdeqness.presentation.features.searchfilter.composables.Section
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeSearchFilter(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<AnimeSearchFilterState>,
    actions: AnimeSearchFilterActions,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val state by stateFlow.collectAsState()

    val progress by animateFloatAsState(
        if (state.selectedCells.isNotEmpty()) 1f else 0f,
        animationSpec = tween(600)
    )

    if (state.isFilterVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                actions.onDismissed()
            },
            containerColor = AppTheme.colors.onPrimary,
            sheetState = sheetState,
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding(),
            shape = RoundedCornerShape(0.dp),
            dragHandle = null
        ) {
            val listState = rememberLazyListState()

            Box(Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                ) {
                    items(
                        state.items.size,
                        key = { index ->
                            state.items[index].value.innerId
                        }
                    ) { index ->
                        Section(
                            sectionData = state.items[index],
                            onClick = { innerId, cellId, isSelected ->
                                actions.onCellClicked(innerId, cellId, isSelected)
                            },
                            onScrollNeed = {
                                scope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.size(64.dp))
                    }
                }

                HorizontalAnimationLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    progress = progress
                ) {
                    AppOutlinedButton(
                        modifier = Modifier.padding(end = 8.dp),
                        title = stringResource(R.string.search_filter_reset)
                    ) {
                        actions.onResetFilter()
                    }

                    AppButton(
                        modifier = Modifier.animateContentSize(),
                        title = stringResource(R.string.search_filter_apply)
                    ) {
                        actions.onApplyFilter()
                    }
                }

            }
        }
    }

}
