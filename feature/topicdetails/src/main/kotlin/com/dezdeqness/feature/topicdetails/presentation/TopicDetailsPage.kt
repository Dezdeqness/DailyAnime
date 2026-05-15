package com.dezdeqness.feature.topicdetails.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.feature.topicdetails.R
import com.dezdeqness.feature.topicdetails.presentation.composables.TopicDetailsContent
import com.dezdeqness.feature.topicdetails.presentation.composables.TopicDetailsLoading
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsStatus
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.GeneralError
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TopicDetailsPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<TopicDetailsNamespace.State>,
    actions: TopicDetailsActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppTheme.colors.onPrimary,
        topBar = {
            AppToolbar(
                title = stringResource(R.string.topic_details_toolbar_title),
                navigationClick = actions::onBackPressed,
            )
        },
    ) { padding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isPullDownRefreshing,
            onRefresh = actions::onPullDownRefreshed,
        )

        Box(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.Center,
        ) {
            when (state.status) {
                TopicDetailsStatus.Initial, TopicDetailsStatus.Loading -> {
                    TopicDetailsLoading()
                }

                TopicDetailsStatus.Error -> {
                    GeneralError()
                }

                TopicDetailsStatus.Loaded -> {
                    state.topic?.let { topic ->
                        TopicDetailsContent(
                            topic = topic,
                            linkedEntity = state.linkedEntity,
                            onLinkedEntityClick = actions::onLinkedEntityClicked,
                            onVideoClick = actions::onVideoClicked,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
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
