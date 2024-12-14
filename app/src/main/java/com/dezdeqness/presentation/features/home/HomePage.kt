package com.dezdeqness.presentation.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.dezdeqness.R
import com.dezdeqness.presentation.features.home.composable.HomeBanner
import com.dezdeqness.presentation.features.home.composable.HomeSection

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    state: HomeState,
    actions: HomeActions,
) {
    LaunchedEffect(Unit) {
        actions.onInitialLoad()
    }

    val sections = state.sectionsState.sections
    val authorizedState = state.authorizedState

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_tint)),
    ) {
        item {
            HomeBanner(
                authorizedState = authorizedState,
            )
        }

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
