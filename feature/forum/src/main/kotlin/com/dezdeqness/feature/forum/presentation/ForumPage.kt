package com.dezdeqness.feature.forum.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.feature.forum.R
import com.dezdeqness.feature.forum.presentation.composables.ForumSectionItem
import com.dezdeqness.feature.forum.presentation.composables.ForumShimmerLoading
import com.dezdeqness.feature.forum.presentation.composables.HotTopicCard
import com.dezdeqness.feature.forum.presentation.models.ForumUiModel
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace
import com.dezdeqness.feature.forum.presentation.store.ForumStatus
import com.dezdeqness.feature.forum.presentation.store.HotTopicsStatus
import com.dezdeqness.foundation.ui.theme.AppTheme
import com.dezdeqness.foundation.ui.views.GeneralEmpty
import com.dezdeqness.foundation.ui.views.GeneralError
import com.dezdeqness.foundation.ui.views.header.Header
import com.dezdeqness.foundation.ui.views.toolbar.AppToolbar
import com.dezdeqness.shared.presentation.feature.topic.model.TopicPresentationModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ForumPage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<ForumNamespace.State>,
    actions: ForumActions,
) {
    val state by stateFlow.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppToolbar(title = stringResource(R.string.forum_toolbar_title))
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
                ForumStatus.Initial, ForumStatus.Loading -> {
                    ForumShimmerLoading(modifier = Modifier.fillMaxSize())
                }

                ForumStatus.Error -> {
                    GeneralError()
                }

                ForumStatus.Empty -> {
                    GeneralEmpty()
                }

                ForumStatus.Loaded -> {
                    ForumLoadedList(
                        sections = state.list,
                        hotTopics = state.hotTopics,
                        hotTopicsStatus = state.hotTopicsStatus,
                        onSectionClicked = { item ->
                            actions.onForumSectionClicked(item.permalink, item.name)
                        },
                        onHotTopicClicked = actions::onHotTopicClicked,
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

@Composable
private fun ForumLoadedList(
    sections: List<ForumUiModel>,
    hotTopics: List<TopicPresentationModel>,
    hotTopicsStatus: HotTopicsStatus,
    onSectionClicked: (ForumUiModel) -> Unit,
    onHotTopicClicked: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = sections,
            key = { "section-${it.id}" },
        ) { item ->
            ForumSectionItem(
                item = item,
                onClick = { onSectionClicked(item) },
                modifier = Modifier.animateItem(),
            )
        }

        if (hotTopicsStatus == HotTopicsStatus.Loaded && hotTopics.isNotEmpty()) {
            item(key = "hot-topics-header") {
                Header(
                    title = stringResource(R.string.forum_hot_topics_header),
                    titleStyle = AppTheme.typography.labelLarge.copy(fontSize = 18.sp),
                )
            }

            items(
                items = hotTopics,
                key = { "hot-preview-${it.topicId}" },
            ) { topic ->
                HotTopicCard(
                    item = topic,
                    onClick = { onHotTopicClicked(topic.topicId) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}
