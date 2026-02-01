package com.dezdeqness.presentation.features.personallist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.feature.personallist.tab.PersonalListActions
import com.dezdeqness.feature.personallist.tab.PersonalListPage
import com.dezdeqness.feature.personallist.tab.PersonalListViewModel
import com.dezdeqness.feature.personallist.tab.StatusIdKey
import com.dezdeqness.shared.presentation.model.RibbonStatusUiModel

@Composable
fun PersonalListPageStandalonePage(
    pager: PagerState,
    ribbon: List<RibbonStatusUiModel>,
    onDetailsClick: (animeId: Long, title: String) -> Unit,
    onOpenEditRate: (userRateId: Long, title: String) -> Unit,
) {
    HorizontalPager(
        state = pager,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        val statusId = ribbon.getOrNull(page)?.id ?: return@HorizontalPager

        val owner = LocalViewModelStoreOwner.current

        val extras = if (owner is HasDefaultViewModelProviderFactory) {
            owner.defaultViewModelCreationExtras
        } else {
            CreationExtras.Empty
        }

        val context = LocalContext.current
        val personalListTabComponent = remember(statusId) {
            (context.applicationContext as ShikimoriApp).appComponent
                .personalListTabComponent()
                .create()
        }
        val viewModelFactory = personalListTabComponent.viewModelFactory()
        val tabViewModel = viewModel<PersonalListViewModel>(
            key = "personal_list_tab_$statusId",
            factory = viewModelFactory,
            extras = MutableCreationExtras(extras).apply {
                set(StatusIdKey, statusId)
            }
        )

        val actions = remember {
            object : PersonalListActions {
                override fun onPullDownRefreshed() {
                    tabViewModel.onRefresh()
                }

                override fun onLoadMore() {
                    tabViewModel.onLoadMore()
                }

                override fun onAnimeClicked(animeId: Long, displayName: String) {
                    onDetailsClick(animeId, displayName)
                }

                override fun onOpenEditRateClicked(editRateId: Long, displayName: String) {
                    onOpenEditRate(editRateId, displayName)
                }

                override fun onUserRateIncrement(editRateId: Long) {
                    tabViewModel.onUserRateIncrement(editRateId)
                }

            }
        }

        PersonalListPage(
            stateFlow = tabViewModel.state,
            actions = actions,
        )
    }
}
