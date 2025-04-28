package com.dezdeqness.presentation.features.genericlistscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.dezdeqness.presentation.features.animedetails.composables.list.DetailsError
import com.dezdeqness.presentation.features.genericlistscreen.composable.GenericList
import com.dezdeqness.presentation.features.genericlistscreen.composable.GenericListLoading
import com.dezdeqness.presentation.features.genericlistscreen.composable.GenericListToolbar
import kotlinx.coroutines.flow.StateFlow

@Composable
fun GenericListPage(
    modifier: Modifier = Modifier,
    title: String,
    stateFlow: StateFlow<GenericListableState>,
    actions: GenericListableActions,
) {
    val state by stateFlow.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {

        when (state.status) {
            GenericListableStatus.Loading, GenericListableStatus.Initial -> {
                GenericListLoading(modifier = Modifier.fillMaxSize())
            }

            GenericListableStatus.Loaded -> {
                GenericList(
                    list = state.list,
                    onClick = { action -> actions.onActionReceive(action) }
                )
            }

            GenericListableStatus.Error -> {
                DetailsError(onRetryClick = actions::onRetryClicked)
            }
        }

        GenericListToolbar(
            title = title,
            onBackClick = actions::onBackPressed,
        )
    }
}
