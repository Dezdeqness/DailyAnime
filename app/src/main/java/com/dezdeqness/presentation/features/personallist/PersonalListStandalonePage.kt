package com.dezdeqness.presentation.features.personallist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.core.ui.theme.AppTheme
import com.dezdeqness.core.ui.views.GeneralError
import com.dezdeqness.feature.personallist.BottomSheet
import com.dezdeqness.feature.personallist.DataStatus
import com.dezdeqness.feature.personallist.PersonalListTabsViewModel
import com.dezdeqness.feature.personallist.composable.PersonalListSearch
import com.dezdeqness.feature.personallist.composable.PersonalRibbon
import com.dezdeqness.feature.personallist.composable.RibbonEmptyState
import com.dezdeqness.feature.personallist.composable.ShimmerPersonalLoading
import com.dezdeqness.feature.personallist.tab.PersonalListViewModel
import com.dezdeqness.presentation.Details
import com.dezdeqness.presentation.features.userrate.UserRateDialogStandalone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PersonalListStandalonePage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val personalListComponent = remember {
        (context.applicationContext as ShikimoriApp).appComponent
            .personalListComponent()
            .create()
    }
    val analyticsManager = personalListComponent.analyticsManager()
    val viewModelFactory = personalListComponent.viewModelFactory()
    val viewModel = viewModel<PersonalListTabsViewModel>(factory = viewModelFactory)

    val scope = rememberCoroutineScope()

    val pagerState by viewModel.pagerStateFlow.collectAsStateWithLifecycle()
    val bottomSheet by viewModel.bottomSheetFlow.collectAsStateWithLifecycle()

    val ribbon = pagerState.ribbon
    val selectedId = pagerState.selectedRibbonId
    val selectedIndex = remember(ribbon, selectedId) {
        ribbon.indexOfFirst { it.id == selectedId }.coerceAtLeast(0)
    }

    val pager = rememberPagerState(
        initialPage = selectedIndex,
        pageCount = { ribbon.size.coerceAtLeast(1) },
    )

    LaunchedEffect(selectedIndex, ribbon.size) {
        if (ribbon.isNotEmpty() && pager.currentPage != selectedIndex) {
            pager.animateScrollToPage(selectedIndex)
        }
    }

    LaunchedEffect(pager) {
        snapshotFlow { pager.currentPage }.collectLatest { page ->
            val id = ribbon.getOrNull(page)?.id ?: return@collectLatest
            viewModel.onRibbonItemSelected(id)
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            PersonalListSearch(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onQueryChanged = { },
            )
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .fillMaxSize(),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (ribbon.isNotEmpty()) {
                    PersonalRibbon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        items = ribbon,
                        targetPage = pager.targetPage,
                        onClick = { index ->
                            scope.launch {
                                pager.animateScrollToPage(index)
                            }
                        },
                    )
                }

                when (pagerState.status) {
                    DataStatus.Loading -> ShimmerPersonalLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                    )

                    DataStatus.Error -> GeneralError(modifier = Modifier.fillMaxSize())
                    DataStatus.Empty -> RibbonEmptyState(modifier = Modifier.fillMaxSize())
                    else -> {
                        if (ribbon.isNotEmpty()) {
                            PersonalListPageStandalonePage(
                                pager = pager,
                                ribbon = ribbon,
                                onOpenEditRate = { userRateId, title ->
                                    viewModel.openEditRateBottomSheet(
                                        userRateId = userRateId,
                                        title = title,
                                    )
                                },
                                onDetailsClick = { animeId, title ->
                                    analyticsManager.detailsTracked(
                                        id = animeId.toString(),
                                        title = title,
                                    )
                                    navController.navigate(Details(animeId))
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    val editRateBottomSheet = bottomSheet as? BottomSheet.EditRate
    if (editRateBottomSheet != null) {
        val tabViewModel = viewModel<PersonalListViewModel>(
            key = "personal_list_tab_${ribbon[pager.currentPage].id}",
            factory = viewModelFactory,
        )
        UserRateDialogStandalone(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            onClosed = {
                viewModel.onUserRateBottomDialogClosed()
            },
            userRateId = editRateBottomSheet.userRateId,
            title = editRateBottomSheet.title,
            onSaveClicked = { userRate ->
                viewModel.onUserRateBottomDialogClosed()
                tabViewModel.onUserRateChanged(userRate)
            },
        )
    }
}
