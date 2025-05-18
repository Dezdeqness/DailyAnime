package com.dezdeqness.presentation.features.personallist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dezdeqness.R
import com.dezdeqness.core.ui.GeneralError
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.presentation.features.personallist.composable.PersonalList
import com.dezdeqness.presentation.features.personallist.composable.UserRateEmptyState
import com.dezdeqness.presentation.features.personallist.composable.PersonalListSearch
import com.dezdeqness.presentation.features.personallist.composable.PersonalListSelectOrderDialog
import com.dezdeqness.presentation.features.personallist.composable.PersonalRibbon
import com.dezdeqness.presentation.features.personallist.composable.RibbonEmptyState
import com.dezdeqness.presentation.features.personallist.composable.ShimmerPersonalLoading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonalListPage(
    stateFlow: StateFlow<PersonalListState>,
    actions: PersonalListActions,
    modifier: Modifier = Modifier,
) {

    val state by stateFlow.collectAsState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        actions.onInitialLoad()
    }

    val isOrderDialogOpened = rememberSaveable {
        mutableStateOf(false)
    }

    if (isOrderDialogOpened.value) {
        PersonalListSelectOrderDialog(
            onDismissRequest = {
                isOrderDialogOpened.value = false
            },
            onSelectedItem = {
                actions.onOrderChanged(it)
                isOrderDialogOpened.value = false
            },
            selectedId = state.currentSortId,
        )
    }

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    PersonalListSearch(
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                        onQueryChanged = { query ->
                            actions.onQueryChanged(query)
                        },
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 4.dp),
                ) {
                    IconButton(
                        onClick = {
                            isOrderDialogOpened.value = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter_action),
                            contentDescription = null,
                            tint = AppTheme.colors.onSurface
                        )
                    }
                }
            }
        },
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isPullDownRefreshing,
            onRefresh = {
                actions.onPullDownRefreshed()
            },
        )
        Box(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center,
        ) {
            val localModifier = Modifier.fillMaxSize()

            val isRibbonVisible by remember(state.isLoadingStateShowing, state.placeholder) {
                derivedStateOf {
                    state.ribbon.isEmpty().not() && state.placeholder !is Placeholder.Ribbon
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                if (isRibbonVisible) {
                    PersonalRibbon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        items = state.ribbon,
                        selectedRibbonId = state.currentRibbonId,
                        onClick = {
                            actions.onRibbonItemSelected(it)
                        }
                    )
                }

                if (state.isLoadingStateShowing) {
                    ShimmerPersonalLoading(
                        modifier = localModifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 4.dp,
                            ),
                    )
                }

                when (state.placeholder) {
                    Placeholder.UserRate.Error,
                    Placeholder.Ribbon.Error -> {
                        GeneralError(modifier = localModifier)
                    }

                    Placeholder.Ribbon.Empty -> {
                        RibbonEmptyState(modifier = localModifier)
                    }

                    Placeholder.UserRate.Empty -> {
                        UserRateEmptyState(modifier = localModifier)
                    }

                    else -> {}
                }

                PersonalList(
                    list = state.items,
                    isScrollNeed = state.isScrollNeed,
                    onActionReceive = { action ->
                        actions.onActionReceived(action)
                    },
                    onNeedScroll = { listState ->
                        scope.launch {
                            actions.onScrolled()
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            PullRefreshIndicator(
                refreshing = state.isPullDownRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

}
