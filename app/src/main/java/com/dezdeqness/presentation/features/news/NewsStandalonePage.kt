package com.dezdeqness.presentation.features.news

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.feature.news.presentation.NewsActions
import com.dezdeqness.feature.news.presentation.NewsPage
import com.dezdeqness.feature.news.presentation.NewsViewModel
import com.dezdeqness.feature.news.presentation.store.NewsNamespace

@Composable
fun NewsStandalonePage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val newsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .newsComponent()
            .create()
    }

    val viewModel = viewModel<NewsViewModel>(factory = newsComponent.viewModelFactory())

    NewsPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        actions = object : NewsActions {
            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onLoadMore() {
                viewModel.onLoadMore()
            }

            override fun onNewsItemClicked(topicId: Long) {}
        },
    )

    viewModel.effects.collectEvents {
        when (it) {
            NewsNamespace.Effect.Error -> {
                viewModel.onErrorMessage()
            }
        }
    }
}
