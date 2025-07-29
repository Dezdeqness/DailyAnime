package com.dezdeqness.presentation.features.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.utils.collectEvents
import com.dezdeqness.presentation.features.history.store.HistoryNamespace

@Composable
fun HistoryStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val historyComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .historyComponent()
            .create()
    }

    val viewModel = viewModel<HistoryViewModel>(factory = historyComponent.viewModelFactory())

    HistoryPage(
        modifier = modifier,
        stateFlow = viewModel.state,
        actions = object : HistoryActions {
            override fun onPullDownRefreshed() {
                viewModel.onPullDownRefreshed()
            }

            override fun onLoadMore() {
                viewModel.onLoadMore()
            }

            override fun onBackPressed() {
                navController.popBackStack()
            }
        }
    )

    viewModel.effects.collectEvents {
        when (it) {
            HistoryNamespace.Effect.Error -> {
                viewModel.onErrorMessage()
            }
        }
    }

}
