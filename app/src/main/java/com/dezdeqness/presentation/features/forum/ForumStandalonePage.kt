package com.dezdeqness.presentation.features.forum

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.forum.presentation.ForumActions
import com.dezdeqness.feature.forum.presentation.ForumPage
import com.dezdeqness.feature.forum.presentation.ForumViewModel
import com.dezdeqness.feature.forum.presentation.store.ForumNamespace
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.ForumTopics
import com.dezdeqness.presentation.TopicDetails

@Composable
fun ForumStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val forumComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .forumComponent()
            .create()
    }

    val forumViewModel = viewModel<ForumViewModel>(
        factory = forumComponent.viewModelFactory(),
    )

    ForumPage(
        modifier = modifier,
        stateFlow = forumViewModel.state,
        actions = object : ForumActions {
            override fun onPullDownRefreshed() {
                forumViewModel.onPullDownRefreshed()
            }

            override fun onForumSectionClicked(permalink: String, name: String) {
                navController.navigate(ForumTopics(permalink = permalink, title = name))
            }

            override fun onHotTopicClicked(topicId: Long) {
                navController.navigate(TopicDetails(topicId))
            }
        },
    )

    forumViewModel.effects.collectEvents {
        when (it) {
            ForumNamespace.Effect.Error -> {
                forumViewModel.onErrorMessage()
            }
        }
    }
}
