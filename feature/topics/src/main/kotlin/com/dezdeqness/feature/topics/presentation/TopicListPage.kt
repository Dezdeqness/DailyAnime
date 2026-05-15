package com.dezdeqness.feature.topics.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.feature.topics.R
import com.dezdeqness.feature.topics.presentation.composables.TopicListContent
import com.dezdeqness.feature.topics.presentation.composables.TopicListShimmerLoading
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace
import com.dezdeqness.feature.topics.presentation.store.TopicListStatus
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.GeneralEmpty
import com.dezdeqness.foundation.ui.views.GeneralError
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TopicListPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<TopicListNamespace.State>,
    actions: TopicListActions,
    title: String = stringResource(R.string.topics_toolbar_title),
    showToolbar: Boolean = true,
    onBackPressed: (() -> Unit)? = null,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (showToolbar) {
                AppToolbar(
                    title = title,
                    navigationIcon = if (onBackPressed != null) {
                        Icons.AutoMirrored.Filled.ArrowBack
                    } else {
                        null
                    },
                    navigationClick = { onBackPressed?.invoke() },
                )
            }
        },
    ) { contentPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isPullDownRefreshing,
            onRefresh = { actions.onPullDownRefreshed() },
        )

        Box(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center,
        ) {
            when (state.status) {
                TopicListStatus.Initial, TopicListStatus.Loading -> {
                    TopicListShimmerLoading(modifier = Modifier.fillMaxSize())
                }

                TopicListStatus.Error -> {
                    GeneralError()
                }

                TopicListStatus.Empty -> {
                    GeneralEmpty()
                }

                TopicListStatus.Loaded -> {
                    var isPageLoading by remember { mutableStateOf(false) }

                    LaunchedEffect(state.list, state.isPullDownRefreshing) {
                        isPageLoading = false
                    }

                    TopicListContent(
                        list = state.list,
                        hasNextPage = state.hasNextPage,
                        isPageLoading = isPageLoading,
                        onLoadMore = {
                            actions.onLoadMore()
                            isPageLoading = true
                        },
                        onItemClicked = { topicId ->
                            actions.onItemClicked(topicId)
                        },
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = state.isPullDownRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}
