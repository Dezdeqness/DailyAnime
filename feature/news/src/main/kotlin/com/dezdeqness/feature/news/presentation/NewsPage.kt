package com.dezdeqness.feature.news.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
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
import com.dezdeqness.feature.news.R
import com.dezdeqness.feature.news.presentation.composables.NewsList
import com.dezdeqness.feature.news.presentation.composables.NewsShimmerLoading
import com.dezdeqness.feature.news.presentation.store.NewsNamespace
import com.dezdeqness.feature.news.presentation.store.NewsStatus
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.GeneralEmpty
import com.dezdeqness.foundation.ui.views.GeneralError
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NewsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<NewsNamespace.State>,
    actions: NewsActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                title = stringResource(R.string.news_toolbar_title),
                navigationIcon = null,
            )
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
                NewsStatus.Initial, NewsStatus.Loading -> {
                    NewsShimmerLoading(modifier = Modifier.fillMaxSize())
                }

                NewsStatus.Error -> {
                    GeneralError()
                }

                NewsStatus.Empty -> {
                    GeneralEmpty()
                }

                NewsStatus.Loaded -> {
                    var isPageLoading by remember { mutableStateOf(false) }

                    LaunchedEffect(state.list, state.isPullDownRefreshing) {
                        isPageLoading = false
                    }

                    NewsList(
                        list = state.list,
                        hasNextPage = state.hasNextPage,
                        isPageLoading = isPageLoading,
                        onLoadMore = {
                            actions.onLoadMore()
                            isPageLoading = true
                        },
                        onItemClicked = { topicId ->
                            actions.onNewsItemClicked(topicId)
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
