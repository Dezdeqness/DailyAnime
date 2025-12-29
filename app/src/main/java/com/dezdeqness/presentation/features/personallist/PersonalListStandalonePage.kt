package com.dezdeqness.presentation.features.personallist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
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
import com.dezdeqness.feature.personallist.PersonalListViewModel
import com.dezdeqness.feature.personallist.Placeholder
import com.dezdeqness.feature.personallist.composable.PersonalListSearch
import com.dezdeqness.feature.personallist.composable.PersonalListSelectOrderDialog
import com.dezdeqness.feature.personallist.composable.PersonalRibbon
import com.dezdeqness.feature.personallist.composable.RibbonEmptyState
import com.dezdeqness.feature.personallist.composable.ShimmerPersonalLoading
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
    val viewModel = viewModel<PersonalListViewModel>(factory = viewModelFactory)

    val scope = rememberCoroutineScope()

    val pagerState by viewModel.pagerStateFlow.collectAsStateWithLifecycle()
    val bottomSheet by viewModel.bottomSheetFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onInitialLoad()
    }

    val isOrderDialogOpened = rememberSaveable {
        mutableStateOf(false)
    }

    if (isOrderDialogOpened.value) {
        PersonalListSelectOrderDialog(
            onDismissRequest = {
                isOrderDialogOpened.value = false
            },
            onSelectedItem = {
                viewModel.onSortChanged(it)
                isOrderDialogOpened.value = false
            },
            selectedId = pagerState.currentSortId,
        )
    }

    val ribbon = pagerState.ribbon
    val selectedId = pagerState.selectedRibbonId
    val selectedIndex = remember(ribbon, selectedId) {
        ribbon.indexOfFirst { it.id == selectedId }.coerceAtLeast(0)
    }

    val pager = rememberPagerState(
        initialPage = selectedIndex,
        pageCount = { ribbon.size.coerceAtLeast(1) },
    )

    val ribbonState by rememberUpdatedState(ribbon)

    LaunchedEffect(selectedIndex, ribbon.size) {
        if (ribbon.isNotEmpty() && pager.currentPage != selectedIndex) {
            pager.animateScrollToPage(selectedIndex)
        }
    }

    LaunchedEffect(pager) {
        snapshotFlow { pager.currentPage }.collectLatest { page ->
            val id = ribbonState.getOrNull(page)?.id ?: return@collectLatest
            viewModel.onRibbonItemSelected(id)
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.onPrimary,
        modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    PersonalListSearch(
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                        onQueryChanged = { },
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 4.dp),
                ) {
                    IconButton(
                        onClick = {
                            isOrderDialogOpened.value = true
                        },
                    ) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = null,
                            tint = AppTheme.colors.onSurface
                        )
                    }
                }
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(top = contentPadding.calculateTopPadding())
                .fillMaxSize(),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                val isRibbonVisible by remember(
                    pagerState.isInitialLoading,
                    pagerState.placeholder,
                    ribbon
                ) {
                    derivedStateOf { ribbon.isNotEmpty() && pagerState.placeholder !is Placeholder.Ribbon }
                }

                if (isRibbonVisible) {
                    PersonalRibbon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        items = ribbon,
                        selectedRibbonId = selectedId,
                        onClick = { id ->
                            val index = ribbon.indexOfFirst { it.id == id }
                            if (index >= 0) {
                                scope.launch {
                                    pager.animateScrollToPage(index)
                                }
                            }
                        },
                    )
                }

                if (pagerState.isInitialLoading) {
                    ShimmerPersonalLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                }

                when (pagerState.placeholder) {
                    Placeholder.Ribbon.Error -> GeneralError(modifier = Modifier.fillMaxSize())
                    Placeholder.Ribbon.Empty -> RibbonEmptyState(modifier = Modifier.fillMaxSize())
                    else -> {
                        if (ribbon.isNotEmpty()) {
                            PersonalListTabsPager(
                                pager = pager,
                                ribbon = ribbon,
                                currentSortId = pagerState.currentSortId,
                                viewModelFactory = viewModelFactory,
                                analyticsManager = analyticsManager,
                                onOpenEditRate = { userRateId, title ->
                                    viewModel.openEditRateBottomSheet(
                                        userRateId = userRateId,
                                        title = title,
                                    )
                                },
                                refreshStatusFlow = viewModel.refreshStatusFlow,
                                onDetailsClick = { animeId ->
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
        UserRateDialogStandalone(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            onClosed = {
                viewModel.onUserRateBottomDialogClosed()
            },
            userRateId = editRateBottomSheet.userRateId,
            title = editRateBottomSheet.title,
            onSaveClicked = viewModel::onUserRateChanged,
        )
    }
}
