package com.dezdeqness.presentation.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.dezdeqness.R
import com.dezdeqness.presentation.features.home.composable.HomeBanner
import com.dezdeqness.presentation.features.home.composable.HomeSection
import com.dezdeqness.presentation.features.home.composable.SectionStatus
import com.dezdeqness.presentation.features.home.composable.ShimmerHomeLoading
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    stateFlow: StateFlow<HomeState>,
    actions: HomeActions,
) {
    val state by stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        actions.onInitialLoad()
    }

    val sections = state.sectionsState.sections
    val authorizedState = state.authorizedState

    val isLoadingVisible = remember(sections) {
        sections.map { it.status }.any { it == SectionStatus.Initial || it == SectionStatus.Loading }
    }

    val isEmptyContent = remember(sections) {
        sections.map { it.items }.all { it.isEmpty() }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_tint)),
        userScrollEnabled = isLoadingVisible.not(),
    ) {
        item {
            HomeBanner(
                authorizedState = authorizedState,
            )
        }

        if (isLoadingVisible && isEmptyContent) {
            item {
                ShimmerHomeLoading()
            }
        } else {
            items(
                count = sections.size,
                key = { index -> sections[index].toString() },
            ) { index ->
                val section = sections[index]
                HomeSection(
                    title = section.title,
                    items = section.items,
                    status = section.status,
                    onActionReceive = actions::onActionReceived,
                )
            }
        }
    }

}
