package com.dezdeqness.presentation.features.forum

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.di.modules.TopicsArgsModule
import com.dezdeqness.feature.topics.presentation.TopicListActions
import com.dezdeqness.feature.topics.presentation.TopicListPage
import com.dezdeqness.feature.topics.presentation.TopicListViewModel
import com.dezdeqness.feature.topics.presentation.store.TopicListNamespace
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.TopicDetails

@Composable
fun ForumTopicsStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    permalink: String,
    title: String,
) {
    val context = LocalContext.current
    val newsComponent = remember(permalink) {
        (context.applicationContext as ShikimoriApp).appComponent
            .topicsComponent()
            .argsModule(TopicsArgsModule(forumType = permalink))
            .build()
    }

    val viewModel = viewModel<TopicListViewModel>(
        key = permalink,
        factory = newsComponent.viewModelFactory(),
    )

    TopicListPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        actions = object : TopicListActions {
            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onLoadMore() {
                viewModel.onLoadMore()
            }

            override fun onItemClicked(topicId: Long) {
                navController.navigate(TopicDetails(topicId))
            }
        },
        title = title,
        onBackPressed = { navController.popBackStack() },
    )

    viewModel.effects.collectEvents {
        when (it) {
            TopicListNamespace.Effect.Error -> {
                viewModel.onErrorMessage()
            }
        }
    }
}
