package com.dezdeqness.feature.personallist.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dezdeqness.contract.anime.model.UserRateStatusEntity
import com.dezdeqness.core.ui.views.GeneralEmpty
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.feature.personallist.search.composables.PersonalListTabs
import com.dezdeqness.feature.personallist.search.composables.PersonalSearchList
import com.dezdeqness.feature.personallist.search.model.SearchUserRateUiModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PersonalListSearchExpandedPage(
    stateFlow: StateFlow<PersonalListSearchState>,
    actions: PersonalListSearchActions,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    val state by stateFlow.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {

        when (state.status) {
            PersonalListSearchStatus.Loaded -> {
                val availableTabs = remember(state.list) {
                    val tabs = state.list
                        .mapNotNull { it.status.toTab() }
                        .distinct()

                    if (tabs.size > 1) {
                        listOf(PersonalListTab.All) + tabs
                    } else {
                        tabs
                    }
                }

                val selectedId = state.selectedTab
                val selectedIndex = remember(availableTabs, selectedId) {
                    availableTabs.indexOfFirst { it == selectedId }.coerceAtLeast(0)
                }

                val pager = rememberPagerState(
                    pageCount = { availableTabs.size.coerceAtLeast(1) }
                )

                LaunchedEffect(selectedIndex, availableTabs.size) {
                    if (availableTabs.isNotEmpty() && pager.currentPage != selectedIndex) {
                        pager.animateScrollToPage(selectedIndex)
                    }
                }

                LaunchedEffect(pager) {
                    snapshotFlow { pager.currentPage }.collectLatest { page ->
                        val id = availableTabs.getOrNull(page) ?: return@collectLatest
                        actions.onTabSelect(id)
                    }
                }

                Column {
                    PersonalListTabs(
                        targetPage = pager.targetPage,
                        items = availableTabs,
                        onClick = { index ->
                            scope.launch {
                                pager.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalPager(
                        state = pager,
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Top,
                    ) { page ->
                        val tab = availableTabs[page]
                        val filteredList = filterList(state.list, tab)

                        PersonalSearchList(
                            list = filteredList,
                            onAnimeClick = actions::onAnimeClick,
                        )
                    }
                }
            }

            PersonalListSearchStatus.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            PersonalListSearchStatus.Empty -> {
                GeneralEmpty(modifier = Modifier.align(Alignment.Center))
            }

            PersonalListSearchStatus.Error -> {
                GeneralError(modifier = Modifier.align(Alignment.Center))
            }

            PersonalListSearchStatus.Initial -> {
            }
        }
    }
}

fun UserRateStatusEntity.toTab(): PersonalListTab? =
    when (this) {
        UserRateStatusEntity.PLANNED -> PersonalListTab.Planned
        UserRateStatusEntity.WATCHING -> PersonalListTab.Watching
        UserRateStatusEntity.REWATCHING -> PersonalListTab.Rewatching
        UserRateStatusEntity.COMPLETED -> PersonalListTab.Completed
        UserRateStatusEntity.ON_HOLD -> PersonalListTab.OnHold
        UserRateStatusEntity.DROPPED -> PersonalListTab.Dropped
        else -> null
    }

fun filterList(
    list: List<SearchUserRateUiModel>,
    tab: PersonalListTab
): List<SearchUserRateUiModel> = when (tab) {
    PersonalListTab.All -> list
    PersonalListTab.Planned -> list.filter { it.status == UserRateStatusEntity.PLANNED }
    PersonalListTab.Watching -> list.filter { it.status == UserRateStatusEntity.WATCHING }
    PersonalListTab.Rewatching -> list.filter { it.status == UserRateStatusEntity.REWATCHING }
    PersonalListTab.Completed -> list.filter { it.status == UserRateStatusEntity.COMPLETED }
    PersonalListTab.OnHold -> list.filter { it.status == UserRateStatusEntity.ON_HOLD }
    PersonalListTab.Dropped -> list.filter { it.status == UserRateStatusEntity.DROPPED }
}
