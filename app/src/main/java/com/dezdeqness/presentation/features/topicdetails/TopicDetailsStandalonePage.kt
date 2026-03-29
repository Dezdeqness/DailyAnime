package com.dezdeqness.presentation.features.topicdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.topicdetails.presentation.TopicDetailsActions
import com.dezdeqness.feature.topicdetails.presentation.TopicDetailsPage
import com.dezdeqness.feature.topicdetails.presentation.TopicDetailsViewModel
import com.dezdeqness.feature.topicdetails.presentation.TopicIdKey
import com.dezdeqness.feature.topicdetails.presentation.store.TopicDetailsNamespace
import com.dezdeqness.foundation.utils.collectEvents
import com.dezdeqness.presentation.Details
import androidx.core.net.toUri

@Composable
fun TopicDetailsStandalonePage(
    topicId: Long,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val topicDetailsComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .topicDetailsComponent()
            .create()
    }

    val owner = LocalViewModelStoreOwner.current
    val extras = if (owner is HasDefaultViewModelProviderFactory) {
        owner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }

    val viewModel = viewModel<TopicDetailsViewModel>(
        key = "topic_details_$topicId",
        factory = topicDetailsComponent.viewModelFactory(),
        extras = MutableCreationExtras(extras).apply {
            set(TopicIdKey, topicId)
        },
    )

    TopicDetailsPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        actions = object : TopicDetailsActions {
            override fun onBackPressed() {
                navController.popBackStack()
            }

            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onRelatedAnimeClicked(animeId: Long) {
                navController.navigate(Details(animeId))
            }

            override fun onVideoClicked(url: String) {
                context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
            }
        },
    )

    viewModel.effects.collectEvents {
        when (it) {
            TopicDetailsNamespace.Effect.Error -> {
                viewModel.onErrorMessage()
            }
        }
    }
}
